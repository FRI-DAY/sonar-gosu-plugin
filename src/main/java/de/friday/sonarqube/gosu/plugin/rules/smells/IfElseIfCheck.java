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
import org.sonar.check.Rule;

@Rule(key = IfElseIfCheck.KEY)
public class IfElseIfCheck extends BaseGosuRule {
    static final String KEY = "IfElseIfCheck";

    @Override
    public void exitElseStatement(GosuParser.ElseStatementContext ctx) {
        GosuParser.StatementContext statementInElse = ctx.statement();

        if (statementInElse.ifStatement() != null
                && statementInElse.ifStatement().elseStatement() == null) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("\"if ... else if\" constructs should end with \"else\" clauses")
                    .onContext(statementInElse.ifStatement())
                    .onTokenRange(ctx.ELSE().getSymbol(), statementInElse.ifStatement().IF().getSymbol())
                    .build());
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
