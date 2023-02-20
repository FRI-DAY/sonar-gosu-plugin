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

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.sonar.check.Rule;

@Rule(key = EmptyLineCheck.KEY)
public class EmptyLineCheck extends AbstractCheckBase {
    static final String KEY = "EmptyLineCheck";

    private Properties properties;

    @Inject
    EmptyLineCheck(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        List<Token> tokens = properties.getTokenStream().getTokens();
        int lastTokenIndex = tokens.size() - 1;
        Token eofToken = tokens.get(lastTokenIndex);

        int eofTokenLine = eofToken.getLine();
        int lastTokenLine = tokens.get(lastTokenIndex - 1).getLine();

        if (eofTokenLine == lastTokenLine) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Add a new line at the end of this file.")
                    .build());
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
