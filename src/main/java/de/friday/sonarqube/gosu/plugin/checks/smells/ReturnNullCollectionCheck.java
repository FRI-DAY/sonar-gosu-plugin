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
package de.friday.sonarqube.gosu.plugin.checks.smells;

import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.sonar.check.Rule;

@Rule(key = ReturnNullCollectionCheck.KEY)
public class ReturnNullCollectionCheck extends AbstractCheckBase {
    static final String KEY = "ReturnNullCollectionCheck";
    private static final String NULL = "null";
    private ScopeTracker currentScope = new ScopeTracker();

    @Override
    public void exitFunctionSignature(GosuParser.FunctionSignatureContext ctx) {
        updateCurrentScope(ctx.typeLiteral());
    }

    @Override
    public void exitPropertySignature(GosuParser.PropertySignatureContext ctx) {
        updateCurrentScope(ctx.typeLiteral());
    }

    private void updateCurrentScope(GosuParser.TypeLiteralContext returnedValueContext) {
        currentScope.enterNewScope();
        verifyReturnedVariable(returnedValueContext);
    }

    private void verifyReturnedVariable(GosuParser.TypeLiteralContext returnedValueContext) {
        if (returnedValueContext == null || !isCollection(returnedValueContext.getText())) {
            currentScope.markAsNotReturningCollection();
            return;
        }
        currentScope.markAsReturningCollection();
    }

    private static boolean isCollection(String returnedValue) {
        returnedValue = returnedValue.endsWith(">")
                ? returnedValue.substring(0, returnedValue.indexOf('<'))
                : returnedValue;
        return returnedValue.endsWith("]")
                || returnedValue.endsWith("List");
    }

    @Override
    public void exitLocalVarStatement(GosuParser.LocalVarStatementContext ctx) {
        GosuParser.ExpressionContext expression = ctx.expression();
        if (currentScope.isNotReturningCollection() || expression == null) {
            return;
        }
        if (NULL.equals(expression.getText())) {
            currentScope.addNullVariable(ctx.identifier().getText());
        }
    }

    @Override
    public void exitReturnStatement(GosuParser.ReturnStatementContext ctx) {
        if (currentScope.isNotReturningCollection()) {
            return;
        }

        GosuParser.ExpressionContext returnedValueContext = ctx.expression();
        String returnedValue = returnedValueContext.getText();

        if (NULL.equals(returnedValue) || currentScope.containsNullVariable(returnedValue)) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(returnedValueContext)
                    .withMessage("Empty arrays and collections should be returned instead of null")
                    .build());
        }
    }

    @Override
    public void exitFunction(GosuParser.FunctionContext ctx) {
        currentScope.exitScope();
    }

    @Override
    public void exitProperty(GosuParser.PropertyContext ctx) {
        currentScope.exitScope();
    }

    @Override
    protected String getKey() {
        return KEY;
    }

    private static class ScopeTracker {
        private Map<Integer, Boolean> methodsReturningCollection = new HashMap<>();
        private Map<Integer, Set<String>> nullVariables = new HashMap<>();
        private int nestedLevel = -1;

        void enterNewScope() {
            nestedLevel++;
            nullVariables.put(nestedLevel, new HashSet<>());
        }

        void exitScope() {
            nullVariables.remove(nestedLevel);
            methodsReturningCollection.remove(nestedLevel);
            nestedLevel--;
        }

        void markAsReturningCollection() {
            methodsReturningCollection.put(nestedLevel, true);
        }

        void markAsNotReturningCollection() {
            methodsReturningCollection.put(nestedLevel, false);
        }

        void addNullVariable(String var) {
            getNullVariables().add(var);
        }

        boolean isNotReturningCollection() {
            return !methodsReturningCollection.getOrDefault(nestedLevel, false);
        }

        boolean containsNullVariable(String var) {
            for (int i = nestedLevel; i >= 0; i--) {
                if (nullVariables.get(i).contains(var)) {
                    return true;
                }
            }

            return false;
        }

        private Set<String> getNullVariables() {
            return nullVariables.get(nestedLevel);
        }
    }
}
