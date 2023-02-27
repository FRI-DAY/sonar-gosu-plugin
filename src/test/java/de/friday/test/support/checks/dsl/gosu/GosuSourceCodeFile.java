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
package de.friday.test.support.checks.dsl.gosu;

import de.friday.test.support.checks.dsl.specification.SourceCodeFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

public final class GosuSourceCodeFile implements SourceCodeFile {
    private final String fileName;
    private final String baseDir;

    public GosuSourceCodeFile(String filename) {
        this(filename, GosuCheckTestResources.getBaseDirPathAsString());
    }

    public GosuSourceCodeFile(String filename, String baseDir) {
        this.fileName = filename;
        this.baseDir = baseDir;
    }

    @Override
    public InputFile asInputFile() {
        final String sourceFileContent = loadFileContent();
        return new TestInputFileBuilder(
                baseDir,
                Paths.get(baseDir).toFile(),
                GosuCheckTestResources.getPathOf(fileName, baseDir).toFile()
        ).initMetadata(sourceFileContent).build();
    }

    private String loadFileContent() {
        final Path gosuFilePath = GosuCheckTestResources.getPathOf(fileName, baseDir);

        try (Stream<String> lines = Files.lines(gosuFilePath)) {
            return lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to load Gosu source file at: " + gosuFilePath);
        }
    }
}
