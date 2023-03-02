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
package de.friday.sonarqube.gosu.plugin.rules.bugs;

import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import org.sonar.check.Rule;

@Rule(key = SystemClockUnawareDateRule.KEY)
public class SystemClockUnawareDateRule extends BaseGosuRule {
    static final String KEY = "SystemClockUnawareDateRule";

    @Override
    public void exitNewExpression(GosuParser.NewExpressionContext ctx) {
        GosuParser.ClassOrInterfaceTypeContext constructor = ctx.classOrInterfaceType();
        GosuParser.ArgumentsContext arguments = ctx.arguments();

        if (constructor == null || arguments == null) {
            return;
        }

        if (constructor.getText().equals("Date") && arguments.argExpression().isEmpty()) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(ctx)
                    .withMessage("\"Date\" should not be instantiated.")
                    .build());
        }
    }

    @Override
    public void enterLocalVarStatement(GosuParser.LocalVarStatementContext ctx) {
        ctx.getText();
        super.enterLocalVarStatement(ctx);
    }

    @Override
    public void exitIdentifier(GosuParser.IdentifierContext ctx) {
        if (ctx.getText().equals("now")) {
            try {
                if (ctx.parent.parent.parent.getText().equals("LocalDate.")) {
                    addIssue(new GosuIssue.GosuIssueBuilder(this)
                            .onContext(ctx)
                            .withMessage("\"LocalDate.now()\" should not be instantiated.")
                            .build());
                }
            } catch (NullPointerException ex) {
                // there were not enough parent objects.
            }
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
