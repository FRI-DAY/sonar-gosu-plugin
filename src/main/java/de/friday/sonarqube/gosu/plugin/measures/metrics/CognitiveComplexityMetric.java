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
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;

/**
 * Implementation base on Sonarqube documentation:
 * https://www.sonarsource.com/docs/CognitiveComplexity.pdf
 */
public class CognitiveComplexityMetric extends BaseMetric {
    private final SensorContext context;
    private final GosuFileProperties gosuFileProperties;
    private int classComplexity;
    private int methodComplexity;
    private int nestedLevel;

    @Inject
    public CognitiveComplexityMetric(SensorContext context, GosuFileProperties gosuFileProperties) {
        this.context = context;
        this.gosuFileProperties = gosuFileProperties;
    }

    public int getMethodComplexity() {
        return methodComplexity;
    }

    public int getNestedLevel() {
        return nestedLevel;
    }

    @Override
    public void enterStart(GosuParser.StartContext ctx) {
        initialize();
        classComplexity = 0;
    }

    @Override
    public void enterFunction(GosuParser.FunctionContext ctx) {
        initialize();
    }

    @Override
    public void enterConstructor(GosuParser.ConstructorContext ctx) {
        initialize();
    }

    @Override
    public void enterProperty(GosuParser.PropertyContext ctx) {
        initialize();
    }

    @Override
    public void enterIfStatement(GosuParser.IfStatementContext ctx) {
        if (ctx.getParent().getParent() instanceof GosuParser.ElseStatementContext) {
            return;
        }
        nestedLevel++;
    }

    @Override
    public void enterDoWhileStatement(GosuParser.DoWhileStatementContext ctx) {
        nestedLevel++;
    }

    @Override
    public void enterWhileStatement(GosuParser.WhileStatementContext ctx) {
        nestedLevel++;
    }

    @Override
    public void enterForEachStatement(GosuParser.ForEachStatementContext ctx) {
        nestedLevel++;
    }

    @Override
    public void enterSwitchStatement(GosuParser.SwitchStatementContext ctx) {
        nestedLevel++;
    }

    @Override
    public void enterTernaryExpression(GosuParser.TernaryExpressionContext ctx) {
        nestedLevel++;
    }

    @Override
    public void enterSafeTernaryExpression(GosuParser.SafeTernaryExpressionContext ctx) {
        nestedLevel++;
    }

    @Override
    public void enterLambdaBody(GosuParser.LambdaBodyContext ctx) {
        nestedLevel++;
    }

    @Override
    public void exitLocalVarStatement(GosuParser.LocalVarStatementContext ctx) {
        if (ctx.expression() instanceof GosuParser.LogicalExpressionContext) {
            calculateComplexity(ctx.expression(), -1);
        }
    }

    @Override
    public void exitLambdaBody(GosuParser.LambdaBodyContext ctx) {
        nestedLevel--;
    }

    @Override
    public void exitCatchClause(GosuParser.CatchClauseContext ctx) {
        increaseComplexity(0);
    }

    @Override
    public void exitElseStatement(GosuParser.ElseStatementContext ctx) {
        if (ctx.statement().ifStatement() == null) {
            increaseComplexity(0);
        }
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext ctx) {
        if (ctx.getParent().getParent() instanceof GosuParser.ElseStatementContext) {
            calculateComplexity(ctx.expression(), 0);
        } else {
            nestedLevel--;
            calculateComplexity(ctx.expression(), nestedLevel);
        }
    }

    @Override
    public void exitWhileStatement(GosuParser.WhileStatementContext ctx) {
        nestedLevel--;
        calculateComplexity(ctx.expression(), nestedLevel);
    }

    @Override
    public void exitDoWhileStatement(GosuParser.DoWhileStatementContext ctx) {
        nestedLevel--;
        calculateComplexity(ctx.expression(), nestedLevel);
    }

    @Override
    public void exitTernaryExpression(GosuParser.TernaryExpressionContext ctx) {
        nestedLevel--;
        calculateComplexity(ctx.expression(0), nestedLevel);
    }

    @Override
    public void exitSafeTernaryExpression(GosuParser.SafeTernaryExpressionContext ctx) {
        nestedLevel--;
        calculateComplexity(ctx.expression(0), nestedLevel);
    }

    @Override
    public void exitForEachStatement(GosuParser.ForEachStatementContext ctx) {
        nestedLevel--;
        calculateComplexity(ctx.expression(), nestedLevel);
    }

    @Override
    public void exitSwitchStatement(GosuParser.SwitchStatementContext ctx) {
        nestedLevel--;
        increaseComplexity(nestedLevel);
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        saveMetric(context, gosuFileProperties.getFile(), CoreMetrics.COGNITIVE_COMPLEXITY, classComplexity);
    }

    private void initialize() {
        methodComplexity = 0;
        nestedLevel = 0;
    }

    private void calculateComplexity(GosuParser.ExpressionContext context, int nestedLevel) {
        int initialComplexity = 1 + nestedLevel;
        List<Token> tokenList = gosuFileProperties
                .getTokenStream()
                .get(context.getStart().getTokenIndex(), GosuUtil.getStopToken(context).getTokenIndex());

        int previousTokenType = 0;
        for (Token token : tokenList) {
            if (BaseMetric.isComplexityOperator(token.getType())) {
                if (previousTokenType != token.getType()) {
                    initialComplexity++;
                }
                previousTokenType = token.getType();
            }
        }
        classComplexity += initialComplexity;
        methodComplexity += initialComplexity;
    }

    private void increaseComplexity(int nestedLevel) {
        methodComplexity = methodComplexity + nestedLevel + 1;
        classComplexity = classComplexity + nestedLevel + 1;
    }
}
