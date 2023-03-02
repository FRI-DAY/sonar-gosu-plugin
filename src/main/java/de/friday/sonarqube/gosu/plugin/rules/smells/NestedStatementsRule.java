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
import de.friday.sonarqube.gosu.plugin.issues.SecondaryIssue;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = NestedStatementsRule.KEY)
public class NestedStatementsRule extends BaseGosuRule {
    static final String KEY = "NestedStatementsRule";
    private static final int DEFAULT_NESTED_LEVEL = 3;
    @RuleProperty(
            key = "Max",
            description = "Maximum allowed control flow statement nesting depth.",
            defaultValue = "" + DEFAULT_NESTED_LEVEL)
    private int maxNestedLevel = DEFAULT_NESTED_LEVEL;
    private List<Pair<TerminalNode, Integer>> tree = new ArrayList<>();

    private int nestedLevel;

    @Override
    public void enterForEachStatement(GosuParser.ForEachStatementContext ctx) {
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
    public void enterIfStatement(GosuParser.IfStatementContext ctx) {
        if (ctx.getParent().getParent() instanceof GosuParser.ElseStatementContext) {
            return;
        }
        nestedLevel++;
    }

    @Override
    public void enterTryCatchFinallyStatement(GosuParser.TryCatchFinallyStatementContext ctx) {
        nestedLevel++;
    }

    @Override
    public void enterSwitchStatement(GosuParser.SwitchStatementContext ctx) {
        nestedLevel++;
    }

    @Override
    public void exitForEachStatement(GosuParser.ForEachStatementContext ctx) {
        nestedLevel--;
        if (ctx.FOR() != null) {
            handleToken(ctx.FOR());
        } else {
            handleToken(ctx.FOREACH());
        }
    }

    @Override
    public void exitDoWhileStatement(GosuParser.DoWhileStatementContext ctx) {
        nestedLevel--;
        handleToken(ctx.WHILE());
    }

    @Override
    public void exitWhileStatement(GosuParser.WhileStatementContext ctx) {
        nestedLevel--;
        handleToken(ctx.WHILE());
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext ctx) {
        if (ctx.getParent().getParent() instanceof GosuParser.ElseStatementContext) {
            return;
        }
        nestedLevel--;
        handleToken(ctx.IF());
    }

    @Override
    public void exitSwitchStatement(GosuParser.SwitchStatementContext ctx) {
        nestedLevel--;
        handleToken(ctx.SWITCH());
    }

    @Override
    public void exitTryCatchFinallyStatement(GosuParser.TryCatchFinallyStatementContext ctx) {
        nestedLevel--;
        handleToken(ctx.TRY());
    }

    private void handleToken(TerminalNode node) {
        tree.add(new ImmutablePair<>(node, nestedLevel));
        if (nestedLevel != 0) {
            return;
        }
        addIssues();
        tree.clear();
    }

    private void addIssues() {
        for (Pair<TerminalNode, Integer> pair : tree) {
            if (pair.getValue().equals(maxNestedLevel)) {
                addIssue(pair);
            }
        }
    }

    private void addIssue(Pair<TerminalNode, Integer> pair) {
        int pairIndex = tree.indexOf(pair);
        int localNestedLevel = maxNestedLevel - 1;
        List<SecondaryIssue> secondaries = new ArrayList<>();

        for (Pair<TerminalNode, Integer> p : tree.subList(pairIndex + 1, tree.size())) {
            if (p.getValue().equals(localNestedLevel)) {
                secondaries.add(new SecondaryIssue(p.getKey(), "Nesting +1"));
                localNestedLevel--;
            }
        }

        addIssue(new GosuIssue.GosuIssueBuilder(this)
                .onToken(pair.getKey().getSymbol())
                .withMessage("Refactor this code to not nest more than " + maxNestedLevel + " if/for/while/switch/try statements.")
                .withSecondaryIssues(secondaries)
                .build());
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
