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
package de.friday.sonarqube.gosu.plugin.rules.metrics;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.language.utils.GosuUtil;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import de.friday.sonarqube.gosu.plugin.issues.SecondaryIssue;
import de.friday.sonarqube.gosu.plugin.measures.metrics.BaseMetric;
import de.friday.sonarqube.gosu.plugin.measures.metrics.CognitiveComplexityMetric;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = CognitiveComplexityRule.KEY)
public class CognitiveComplexityRule extends BaseGosuRule {
    static final String KEY = "CognitiveComplexityRule";
    private static final int DEFAULT_METHOD_THRESHOLD = 10;
    @RuleProperty(
            key = "Threshold",
            description = "The maximum authorized complexity.",
            defaultValue = "" + DEFAULT_METHOD_THRESHOLD)
    private int max = DEFAULT_METHOD_THRESHOLD;

    private List<SecondaryIssue> secondaryIssuesList = new ArrayList<>();

    private CognitiveComplexityMetric metric;
    private GosuFileProperties gosuFileProperties;

    @Inject
    CognitiveComplexityRule(CognitiveComplexityMetric metric, GosuFileProperties gosuFileProperties) {
        this.gosuFileProperties = gosuFileProperties;
        this.metric = metric;
    }

    @Override
    public void exitLocalVarStatement(GosuParser.LocalVarStatementContext ctx) {
        if (ctx.expression() instanceof GosuParser.LogicalExpressionContext) {
            addSecondariesInsideExpression(ctx.expression());
        }
    }

    @Override
    public void exitCatchClause(GosuParser.CatchClauseContext ctx) {
        addSecondary(ctx.CATCH(), 0);
    }

    @Override
    public void exitElseStatement(GosuParser.ElseStatementContext ctx) {
        if (ctx.statement().ifStatement() == null) {
            addSecondary(ctx.ELSE(), 0);
        }
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext ctx) {
        if (ctx.getParent().getParent() instanceof GosuParser.ElseStatementContext) {
            addSecondariesInsideExpression(ctx.expression());
            addSecondary(ctx.IF(), 0);
        } else {
            addSecondary(ctx.IF(), metric.getNestedLevel());
            addSecondariesInsideExpression(ctx.expression());
        }
    }

    @Override
    public void exitWhileStatement(GosuParser.WhileStatementContext ctx) {
        addSecondary(ctx.WHILE(), metric.getNestedLevel());
        addSecondariesInsideExpression(ctx.expression());
    }

    @Override
    public void exitDoWhileStatement(GosuParser.DoWhileStatementContext ctx) {
        addSecondary(ctx.WHILE(), metric.getNestedLevel());
        addSecondariesInsideExpression(ctx.expression());
    }

    @Override
    public void exitTernaryExpression(GosuParser.TernaryExpressionContext ctx) {
        addSecondariesInsideExpression(ctx.expression(0));
        addSecondary(ctx.QUESTION(), metric.getNestedLevel());
    }

    @Override
    public void exitSafeTernaryExpression(GosuParser.SafeTernaryExpressionContext ctx) {
        addSecondariesInsideExpression(ctx.expression(0));
        addSecondary(ctx.QUESTION_COLON(), metric.getNestedLevel());
    }

    @Override
    public void exitForEachStatement(GosuParser.ForEachStatementContext ctx) {
        if (ctx.FOR() != null) {
            addSecondary(ctx.FOR(), metric.getNestedLevel());
        } else {
            addSecondary(ctx.FOREACH(), metric.getNestedLevel());
        }
        addSecondariesInsideExpression(ctx.expression());
    }

    @Override
    public void exitSwitchStatement(GosuParser.SwitchStatementContext ctx) {
        addSecondary(ctx.SWITCH(), metric.getNestedLevel());
    }

    @Override
    public void exitFunction(GosuParser.FunctionContext ctx) {
        Token identifier = ctx.functionSignature().identifier().getStart();
        addComplexityIssue(identifier,
                buildMessage("method \"" + identifier.getText() + "\""));
    }

    @Override
    public void exitConstructor(GosuParser.ConstructorContext ctx) {
        Token identifier = ctx.constructorSignature().CONSTRUCT().getSymbol();
        addComplexityIssue(identifier,
                buildMessage("constructor"));
    }

    @Override
    public void exitProperty(GosuParser.PropertyContext ctx) {
        Token identifier = ctx.propertySignature().identifier().getStart();
        addComplexityIssue(identifier,
                buildMessage("property \"" + identifier.getText()) + "\"");
    }

    private void addComplexityIssue(Token token, String message) {
        if (metric.getMethodComplexity() > max) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withSecondaryIssues(secondaryIssuesList)
                    .onToken(token)
                    .withGap(metric.getMethodComplexity() - max)
                    .withMessage(message)
                    .build());
        }
        secondaryIssuesList.clear();
    }

    private String buildMessage(String message) {
        return "The Cognitive Complexity of this " + message + " is " +
                metric.getMethodComplexity() + " which is greater than " + max + " authorized.";
    }

    private void addSecondariesInsideExpression(GosuParser.ExpressionContext context) {
        List<Token> tokenList = gosuFileProperties
                .getTokenStream()
                .get(context.getStart().getTokenIndex(), GosuUtil.getStopToken(context).getTokenIndex());

        int previousTokenType = 0;
        for (Token token : tokenList) {
            if (BaseMetric.isComplexityOperator(token.getType())) {
                if (previousTokenType != token.getType()) {
                    secondaryIssuesList.add(new SecondaryIssue(token, "+1"));
                }
                previousTokenType = token.getType();
            }
        }
    }

    private void addSecondary(TerminalNode terminalNode, int nestedLevel) {
        if (nestedLevel == 0) {
            secondaryIssuesList.add(new SecondaryIssue(terminalNode, "+1"));
        } else {
            secondaryIssuesList.add(new SecondaryIssue(terminalNode, "+" + (nestedLevel + 1) + " (incl " + nestedLevel + " for nesting)"));
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
