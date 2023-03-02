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
package de.friday.sonarqube.gosu.plugin.rules.smells;


import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.sonar.check.Rule;

@Rule(key = AutomaticDowncastRule.KEY)
public class AutomaticDowncastRule extends BaseGosuRule {
    static final String KEY = "AutomaticDowncastRule";
    private Map<String, String> castedVariables = new HashMap<>();
    private String ifKey = "";
    private String switchKey = "";

    @Override
    public void exitTypeisExpression(GosuParser.TypeisExpressionContext ctx) {
        GosuParser.ExpressionContext identifier = ctx.expression();
        if (identifier instanceof GosuParser.PrimaryExpressionContext
                && ctx.getParent() instanceof GosuParser.IfStatementContext) {
            ifKey = identifier.getText();
            castedVariables.put(ifKey, ctx.type().getText());
            return;
        }

        if (ifKey.equals(identifier.getText())) {
            castedVariables.remove(identifier.getText());
        }
    }

    @Override
    public void exitTypeofExpression(GosuParser.TypeofExpressionContext ctx) {
        if (ctx.getParent() instanceof GosuParser.SwitchStatementContext
                && ctx.expression() instanceof GosuParser.PrimaryExpressionContext) {
            switchKey = ctx.expression().getText();
        }
    }

    @Override
    public void exitCaseOrDefaultStatement(GosuParser.CaseOrDefaultStatementContext ctx) {
        if (!"".equals(switchKey) && ctx.DEFAULT() == null) {
            castedVariables.put(switchKey, ctx.expression().getText());
        }
    }

    @Override
    public void exitSwitchBlockStatement(GosuParser.SwitchBlockStatementContext ctx) {
        if (!"".equals(switchKey) && !ctx.statement().isEmpty() && !endsWithBreak(ctx)) {
            castedVariables.remove(switchKey);
        }
    }

    @Override
    public void exitSwitchStatement(GosuParser.SwitchStatementContext ctx) {
        switchKey = "";
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext ctx) {
        castedVariables.remove(ifKey);
        ifKey = "";
    }

    @Override
    public void exitAssignStatement(GosuParser.AssignStatementContext ctx) {
        String key = ctx.expression(0).getText();
        if (castedVariables.containsKey(key)) {
            castedVariables.remove(key);
        }
    }

    @Override
    public void exitTypeCastExpression(GosuParser.TypeCastExpressionContext ctx) {
        String key = ctx.expression(0).getText();
        String value = ctx.expression(1).getText();
        if (castedVariables.containsKey(key) && castedVariables.get(key).equals(value)) {
            createIssue(ctx);
        }

    }

    private boolean endsWithBreak(GosuParser.SwitchBlockStatementContext ctx) {
        return ctx.statement().get(ctx.statement().size() - 1).breakStatement() != null;
    }

    private void createIssue(ParserRuleContext ctx) {
        addIssue(new GosuIssue.GosuIssueBuilder(this)
                .withMessage("Remove this unnecessary cast")
                .onContext(ctx)
                .build());
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
