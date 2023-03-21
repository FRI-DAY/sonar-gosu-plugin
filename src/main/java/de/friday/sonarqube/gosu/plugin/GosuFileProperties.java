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

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.measures.FileLinesContext;

public class GosuFileProperties {

    private final InputFile file;
    private final CommonTokenStream tokenStream;
    private final FileLinesContext fileLinesContext;
    private GosuFileLineData fileLineData;

    public GosuFileProperties(InputFile file, CommonTokenStream tokenStream, FileLinesContext fileLinesContext) {
        this.file = file;
        this.tokenStream = tokenStream;
        this.fileLinesContext = fileLinesContext;
    }

    public CommonTokenStream getTokenStream() {
        return tokenStream;
    }

    public InputFile getFile() {
        return file;
    }

    public FileLinesContext getFileLinesContext() {
        return fileLinesContext;
    }

    public boolean isMainFile() {
        return InputFile.Type.MAIN.equals(file.type());
    }

    public boolean isTestFile() {
        return InputFile.Type.TEST.equals(file.type());
    }

    public Token getToken(int index) {
        return tokenStream.get(index);
    }

    public GosuFileLineData getFileLineData() {
        if (this.fileLineData != null) return this.fileLineData;

        this.fileLineData = new GosuFileLineData(this);

        return this.fileLineData;
    }
}
