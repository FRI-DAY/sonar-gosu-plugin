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
import java.util.Arrays;
import java.util.List;
import org.sonar.check.Rule;

@Rule(key = CollectionIsEmptyCheck.KEY)
public class CollectionIsEmptyCheck extends AbstractCheckBase {
    static final String KEY = "CollectionIsEmptyCheck";
    private List<String> countingMethods = Arrays.asList(".getCount()", ".Count", ".size()", ".length", ".length()");

    @Override
    public void exitRelationalExpression(GosuParser.RelationalExpressionContext ctx) {
        if (ctx.LT() != null
                || ctx.greaterEqual() != null
                || ctx.LE() != null) {
            return;
        }

        GosuParser.ExpressionContext leftExpression = ctx.expression(0);
        GosuParser.ExpressionContext rightExpression = ctx.expression(1);

        if (("0".equals(leftExpression.getText()) || "0".equals(rightExpression.getText())) &&
                (endsWithCountMethod(leftExpression.getText()) || endsWithCountMethod(rightExpression.getText()))) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Use isEmpty()/Empty to check whether the collection or Query is empty or not.")
                    .onContext(ctx)
                    .build());
        }
    }

    private boolean endsWithCountMethod(String expression) {
        for (String method : countingMethods) {
            if (expression.endsWith(method)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
