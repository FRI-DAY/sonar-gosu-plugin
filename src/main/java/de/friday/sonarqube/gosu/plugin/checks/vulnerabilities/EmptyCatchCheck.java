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
package de.friday.sonarqube.gosu.plugin.checks.vulnerabilities;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.sonar.check.Rule;

@Rule(key = EmptyCatchCheck.KEY)
public class EmptyCatchCheck extends AbstractCheckBase {
    static final String KEY = "EmptyCatchCheck";
    private static final int NUMBER_OF_BRACES = 2;
    private final GosuFileProperties gosuFileProperties;

    @Inject
    EmptyCatchCheck(GosuFileProperties gosuFileProperties) {
        this.gosuFileProperties = gosuFileProperties;
    }

    @Override
    public void exitCatchClause(GosuParser.CatchClauseContext ctx) {
        final List<GosuParser.StatementContext> statements = ctx.statementBlock().statement();

        if (statements.isEmpty()) {
            final List<Token> tokens = gosuFileProperties.getTokenStream()
                    .getTokens(
                            ctx.statementBlock().LBRACE().getSymbol().getTokenIndex(),
                            ctx.statementBlock().RBRACE().getSymbol().getTokenIndex()
                    );

            if (tokens.size() > NUMBER_OF_BRACES) {
                return;
            }

            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("\"catch\" clauses should not be empty")
                    .onToken(ctx.CATCH().getSymbol())
                    .build());
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
