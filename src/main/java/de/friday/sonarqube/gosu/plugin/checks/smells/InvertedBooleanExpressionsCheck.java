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
import org.sonar.check.Rule;

@Rule(key = InvertedBooleanExpressionsCheck.KEY)
public class InvertedBooleanExpressionsCheck extends AbstractCheckBase {
    static final String KEY = "InvertedBooleanExpressionsCheck";

    @Override
    public void exitUnaryExpressionNot(GosuParser.UnaryExpressionNotContext ctx) {
        GosuParser.ExpressionContext expression = ctx.expression();
        if (!(expression instanceof GosuParser.PrimaryExpressionContext)) {
            return;
        }
        GosuParser.PrimaryContext primaryExpression = ((GosuParser.PrimaryExpressionContext) expression).primary();
        if (primaryExpression.expression() instanceof GosuParser.RelationalExpressionContext) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(ctx)
                    .withMessage("Boolean checks should not be inverted")
                    .build());
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
