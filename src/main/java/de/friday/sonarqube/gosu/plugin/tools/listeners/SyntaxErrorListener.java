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
package de.friday.sonarqube.gosu.plugin.tools.listeners;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

public class SyntaxErrorListener extends BaseErrorListener {
    private final SensorContext context;

    private final InputFile inputFile;

    @Inject
    public SyntaxErrorListener(SensorContext context, GosuFileProperties gosuFileProperties) {
        this.context = context;
        this.inputFile = gosuFileProperties.getFile();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        context.newAnalysisError()
                .onFile(inputFile)
                .at(inputFile.newPointer(line, charPositionInLine))
                .message(msg)
                .save();
    }
}
