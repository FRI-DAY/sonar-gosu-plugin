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
package de.friday.sonarqube.gosu.plugin.reports;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.surefire.StaxParser;
import org.sonar.plugins.surefire.data.UnitTestIndex;

public class ReportsScanner {

    private static final Logger LOGGER = Loggers.get(ReportsScanner.class);
    private final Configuration settings;
    private UnitTestIndex index;

    public ReportsScanner(Configuration settings) {
        this.settings = settings;
    }

    public UnitTestIndex createIndex(List<File> reportsDirs) {
        index = new UnitTestIndex();
        final List<File> xmlFiles = getReports(reportsDirs, settings.hasKey(ReportsDirectories.REPORT_PATHS_PROPERTY));
        if (!xmlFiles.isEmpty()) {
            parseFiles(xmlFiles);
        }

        return index;
    }

    private List<File> getReports(List<File> dirs, boolean reportDirSetByUser) {
        return dirs.stream()
                .map(dir -> getReports(dir, reportDirSetByUser))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    private File[] getReports(File dir, boolean reportDirSetByUser) {
        if (!dir.isDirectory()) {
            if (reportDirSetByUser) {
                LOGGER.error("Reports path not found or is not a directory: " + dir.getAbsolutePath());
            }
            return new File[0];
        }
        File[] unitTestResultFiles = findXMLFilesStartingWith(dir, "TEST-");
        if (unitTestResultFiles.length == 0) {
            // case if there's only a test suite result file
            unitTestResultFiles = findXMLFilesStartingWith(dir, "TESTS-");
        }
        if (unitTestResultFiles.length == 0) {
            LOGGER.warn("Reports path contains no files matching TEST-.*.xml : " + dir.getAbsolutePath());
        }
        return unitTestResultFiles;
    }

    private File[] findXMLFilesStartingWith(File dir, final String fileNameStart) {
        return dir.listFiles((parentDir, name) -> name.startsWith(fileNameStart) && name.endsWith(".xml"));
    }

    private void parseFiles(List<File> reports) {
        parseReports(reports);
        sanitize();
    }

    private void parseReports(List<File> reports) {
        StaxParser parser = new StaxParser(index);
        for (File report : reports) {
            try {
                parser.parse(report);
            } catch (XMLStreamException e) {
                LOGGER.error("Fail to parse the junit test report: " + report, e);
            }
        }
    }

    private void sanitize() {
        for (String classname : index.getClassnames()) {
            if (StringUtils.contains(classname, "$")) {
                // reports classes whereas sonar supports files
                String parentClassName = StringUtils.substringBefore(classname, "$");
                index.merge(classname, parentClassName);
            }
        }
    }
}
