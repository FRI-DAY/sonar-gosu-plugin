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
import de.friday.sonarqube.gosu.language.utils.GosuUtil;
import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.sonarqube.gosu.plugin.utils.annotations.UnitTestMissing;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;

@UnitTestMissing
public class CyclomaticComplexityMetric extends AbstractMetricBase {
    private final SensorContext context;
    private final Properties properties;
    private int classComplexity;
    private int methodComplexity;

    @Inject
    public CyclomaticComplexityMetric(SensorContext context, Properties properties) {
        this.context = context;
        this.properties = properties;
    }

    public int getMethodComplexity() {
        return methodComplexity;
    }

    @Override
    public void enterStart(GosuParser.StartContext ctx) {
        classComplexity = 0;
    }

    @Override
    public void enterFunction(GosuParser.FunctionContext ctx) {
        methodComplexity = 1;
    }

    @Override
    public void enterConstructor(GosuParser.ConstructorContext ctx) {
        methodComplexity = 1;
    }

    @Override
    public void enterProperty(GosuParser.PropertyContext ctx) {
        methodComplexity = 1;
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext ctx) {
        calculateComplexity(ctx.expression());
    }

    @Override
    public void exitWhileStatement(GosuParser.WhileStatementContext ctx) {
        calculateComplexity(ctx.expression());
    }

    @Override
    public void exitDoWhileStatement(GosuParser.DoWhileStatementContext ctx) {
        calculateComplexity(ctx.expression());
    }

    @Override
    public void exitTernaryExpression(GosuParser.TernaryExpressionContext ctx) {
        calculateComplexity(ctx.expression(0));
    }

    @Override
    public void exitSafeTernaryExpression(GosuParser.SafeTernaryExpressionContext ctx) {
        calculateComplexity(ctx.expression(0));
    }

    @Override
    public void exitForEachStatement(GosuParser.ForEachStatementContext ctx) {
        calculateComplexity(ctx.expression());
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        saveMetric(context, properties.getFile(), CoreMetrics.COMPLEXITY, classComplexity);
    }

    private void calculateComplexity(GosuParser.ExpressionContext context) {
        int initialComplexity = 1;

        List<Token> tokenList = properties
                .getTokenStream()
                .get(context.getStart().getTokenIndex(), GosuUtil.getStopToken(context).getTokenIndex());

        for (Token token : tokenList) {
            if (AbstractMetricBase.isComplexityOperator(token.getType())) {
                initialComplexity++;
            }
        }
        classComplexity += initialComplexity;
        methodComplexity += initialComplexity;
    }
}
