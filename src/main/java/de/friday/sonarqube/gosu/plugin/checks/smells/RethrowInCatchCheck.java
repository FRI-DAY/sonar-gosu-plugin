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
import de.friday.sonarqube.gosu.plugin.issues.SecondaryIssue;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;

@Rule(key = RethrowInCatchCheck.KEY)
public class RethrowInCatchCheck extends AbstractCheckBase {
    static final String KEY = "RethrowInCatchCheck";

    @Override
    public void exitCatchClause(GosuParser.CatchClauseContext ctx) {
        List<GosuParser.StatementContext> statements = ctx.statementBlock().statement();
        String exception = ctx.identifier().getText();

        if (statements.size() == 1) {
            GosuParser.ThrowStatementContext throwStatement = statements.get(0).throwStatement();

            if (throwStatement != null && throwStatement.expression().getText().equals(exception)) {
                final SecondaryIssue secondaryIssue = new SecondaryIssue(throwStatement.THROW(), null);
                addIssue(new GosuIssue.GosuIssueBuilder(this)
                        .withMessage("\"catch\" clauses should do more than rethrow")
                        .onToken(ctx.CATCH().getSymbol())
                        .withSecondaryIssues(Collections.singletonList(secondaryIssue))
                        .build());
            }
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
