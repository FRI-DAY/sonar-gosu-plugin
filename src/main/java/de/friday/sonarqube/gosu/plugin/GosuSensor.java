/*
 * Copyright (C) 2023 FRIDAY Insurance S.A.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.friday.sonarqube.gosu.plugin;

import de.friday.sonarqube.gosu.language.GosuLanguage;
import de.friday.sonarqube.gosu.plugin.issues.Issue;
import de.friday.sonarqube.gosu.plugin.measures.Measures;
import de.friday.sonarqube.gosu.plugin.reports.ReportsDirectories;
import de.friday.sonarqube.gosu.plugin.reports.ReportsScanner;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.surefire.data.UnitTestIndex;
import org.sonarsource.analyzer.commons.ProgressReport;

public class GosuSensor implements Sensor {
    private static final Logger LOG = Loggers.get(GosuSensor.class);
    private final FileSystem fileSystem;
    private final FilePredicate mainFilesPredicate;
    private final UnitTestIndex unitTestIndex;
    private final FileLinesContextFactory fileLinesContextFactory;

    public GosuSensor(
            FileSystem fileSystem,
            Configuration settings,
            PathResolver pathResolver,
            FileLinesContextFactory fileLinesContextFactory
    ) {
        this.fileSystem = fileSystem;
        this.mainFilesPredicate = fileSystem.predicates().and(fileSystem.predicates().hasLanguage(GosuLanguage.KEY));
        this.unitTestIndex = createUnitTestIndex(settings, pathResolver);
        this.fileLinesContextFactory = fileLinesContextFactory;
    }

    @Override
    public void describe(@Nonnull SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Gosu Sensor");
        sensorDescriptor.onlyOnLanguage(GosuLanguage.KEY);
    }

    @Override
    public void execute(@Nonnull SensorContext sensorContext) {
        final Iterable<InputFile> inputFiles = fileSystem.inputFiles(mainFilesPredicate);
        final ProgressReport progressReport = new ProgressReport(
                "Report about progress of Gosu analyzer",
                TimeUnit.SECONDS.toMillis(10)
        );

        startReport(progressReport, inputFiles);

        scan(inputFiles, progressReport, sensorContext);

        progressReport.stop();
    }

    private void scan(Iterable<InputFile> inputFiles, ProgressReport progressReport, SensorContext sensorContext) {
        for (final InputFile inputFile : inputFiles) {
            if (sensorContext.isCancelled()) {
                progressReport.cancel();
                return;
            }
            scanFile(sensorContext, inputFile);
            progressReport.nextFile();
        }
    }

    private UnitTestIndex createUnitTestIndex(Configuration settings, PathResolver pathResolver) {
        final ReportsScanner reportsScanner = new ReportsScanner(settings);
        final List<File> reportsDirectories = new ReportsDirectories(settings, fileSystem, pathResolver).get();
        return reportsScanner.createIndex(reportsDirectories);
    }

    private void startReport(ProgressReport report, Iterable<InputFile> inputFiles) {
        report.start(() -> new Iterator<String>() {
            final Iterator<InputFile> it = inputFiles.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public String next() {
                return it.next().toString();
            }
        });
    }

    private void scanFile(SensorContext sensorContext, InputFile inputFile) {
        final Optional<GosuFileParser> optionalParser = createParser(sensorContext, inputFile);

        if (!optionalParser.isPresent()) {
            return;
        }

        final GosuFileParser gosuFileParser = optionalParser.get();
        final List<Issue> issues = gosuFileParser.parse();

        createIssues(inputFile, sensorContext, issues);
        Measures.of(gosuFileParser.getProperties()).addProcessedTokensTo(sensorContext);
    }

    private Optional<GosuFileParser> createParser(SensorContext context, InputFile inputFile) {
        try {
            final GosuFileParser gosuFileParser = new GosuFileParser(inputFile, context, unitTestIndex, fileLinesContextFactory.createFor(inputFile));
            return Optional.of(gosuFileParser);
        } catch (IOException e) {
            context.newAnalysisError()
                    .onFile(inputFile)
                    .at(new DefaultTextPointer(0, 0))
                    .message(e.getMessage())
                    .save();
            LOG.error("Couldn't get input stream from file " + inputFile.filename(), e);
            return Optional.empty();
        }
    }

    private void createIssues(InputFile inputFile, SensorContext sensorContext, List<Issue> issues) {
        issues.forEach(issue -> issue.createIssue(sensorContext, inputFile));
    }
}
