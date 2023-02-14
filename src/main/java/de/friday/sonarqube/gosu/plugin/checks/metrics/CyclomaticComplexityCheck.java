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
package de.friday.sonarqube.gosu.plugin.checks.metrics;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.language.utils.GosuUtil;
import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import de.friday.sonarqube.gosu.plugin.issues.SecondaryIssue;
import de.friday.sonarqube.gosu.plugin.measures.metrics.AbstractMetricBase;
import de.friday.sonarqube.gosu.plugin.measures.metrics.CyclomaticComplexityMetric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = CyclomaticComplexityCheck.KEY)
public class CyclomaticComplexityCheck extends AbstractCheckBase {
    static final String KEY = "CyclomaticComplexityCheck";
    private static final List<String> EXCLUDED_METHODS = Arrays.asList("equals", "hashCode");
    private static final int DEFAULT_METHOD_THRESHOLD = 10;
    @RuleProperty(
            key = "Threshold",
            description = "The maximum authorized complexity.",
            defaultValue = "" + DEFAULT_METHOD_THRESHOLD)
    private int max = DEFAULT_METHOD_THRESHOLD;
    private List<SecondaryIssue> secondaryIssuesList = new ArrayList<>();
    private Properties properties;
    private CyclomaticComplexityMetric metric;

    @Inject
    CyclomaticComplexityCheck(Properties properties, CyclomaticComplexityMetric metric) {
        this.properties = properties;
        this.metric = metric;
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext ctx) {
        addSecondary(ctx.IF());
        addSecondariesInsideExpression(ctx.expression());
    }

    @Override
    public void exitWhileStatement(GosuParser.WhileStatementContext ctx) {
        addSecondary(ctx.WHILE());
        addSecondariesInsideExpression(ctx.expression());
    }

    @Override
    public void exitDoWhileStatement(GosuParser.DoWhileStatementContext ctx) {
        addSecondary(ctx.WHILE());
        addSecondariesInsideExpression(ctx.expression());
    }

    @Override
    public void exitTernaryExpression(GosuParser.TernaryExpressionContext ctx) {
        addSecondary(ctx.COLON());
        addSecondariesInsideExpression(ctx.expression(0));
    }

    @Override
    public void exitSafeTernaryExpression(GosuParser.SafeTernaryExpressionContext ctx) {
        addSecondary(ctx.QUESTION_COLON());
        addSecondariesInsideExpression(ctx.expression(0));
    }

    @Override
    public void exitForEachStatement(GosuParser.ForEachStatementContext ctx) {
        if (ctx.FOR() != null) {
            addSecondary(ctx.FOR());
        } else {
            addSecondary(ctx.FOREACH());
        }
        addSecondariesInsideExpression(ctx.expression());
    }

    @Override
    public void exitCaseOrDefaultStatement(GosuParser.CaseOrDefaultStatementContext ctx) {
        if (ctx.CASE() != null) {
            addSecondary(ctx.CASE());
        } else {
            addSecondary(ctx.DEFAULT());
        }
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
        if (EXCLUDED_METHODS.contains(token.getText())) {
            secondaryIssuesList.clear();
            return;
        }
        if (metric.getMethodComplexity() > max) {
            secondaryIssuesList.add(new SecondaryIssue(token, "+1"));
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withSecondaryIssues(secondaryIssuesList)
                    .onToken(token)
                    .withGap(metric.getMethodComplexity() - max)
                    .withMessage(message)
                    .build());
            secondaryIssuesList.clear();
        }
    }

    private String buildMessage(String message) {
        return "The Cyclomatic Complexity of this " + message + " is " +
                metric.getMethodComplexity() + " which is greater than " + max + " authorized.";
    }

    private void addSecondariesInsideExpression(GosuParser.ExpressionContext context) {
        List<Token> tokenList = properties
                .getTokenStream()
                .get(context.getStart().getTokenIndex(), GosuUtil.getStopToken(context).getTokenIndex());

        for (Token token : tokenList) {
            if (AbstractMetricBase.isComplexityOperator(token.getType())) {
                secondaryIssuesList.add(new SecondaryIssue(token, "+1"));
            }
        }
    }

    private void addSecondary(TerminalNode terminalNode) {
        secondaryIssuesList.add(new SecondaryIssue(terminalNode, "+1"));
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
