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
package de.friday.sonarqube.gosu.plugin.measures.metrics;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;

public class CodeSizeMetrics extends BaseMetric {

    private int classes = 0;
    private int functions = 0;
    private int statements = 0;

    @Inject
    public CodeSizeMetrics(SensorContext context, GosuFileProperties gosuFileProperties) {
        super(context, gosuFileProperties);
    }

    @Override
    public void enterClassDeclaration(GosuParser.ClassDeclarationContext ctx) {
        this.classes++;
    }

    @Override
    public void enterFunctionSignature(GosuParser.FunctionSignatureContext ctx) {
        this.functions++;
    }

    @Override
    public void enterPropertySignature(GosuParser.PropertySignatureContext ctx) {
        //Gosu properties are analogous to get / set functions
        this.functions++;
    }

    @Override
    public void enterStatement(GosuParser.StatementContext ctx) {
        this.statements++;
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        saveMetricOnContext(CoreMetrics.CLASSES, classes);
        saveMetricOnContext(CoreMetrics.FUNCTIONS, functions);
        saveMetricOnContext(CoreMetrics.STATEMENTS, statements);
    }
}
