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

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.sonar.check.Rule;
import static de.friday.sonarqube.gosu.plugin.rules.smells.TODOsCheck.KEY;

@Rule(key = KEY)
public class TODOsCheck extends BaseGosuRule {
    static final String KEY = "TODOsCheck";
    private Set<Integer> commentTokens = new HashSet<>(Arrays.asList(GosuLexer.COMMENT, GosuLexer.LINE_COMMENT));
    private GosuFileProperties gosuFileProperties;

    @Inject
    TODOsCheck(GosuFileProperties gosuFileProperties) {
        this.gosuFileProperties = gosuFileProperties;
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        final List<Token> tokens = gosuFileProperties.getTokenStream()
                .getTokens()
                .stream()
                .filter(token -> commentTokens.contains(token.getType()))
                .collect(Collectors.toList());

        for (Token token : tokens) {
            if (StringUtils.containsIgnoreCase(token.getText(), "TODO")) {
                addIssue(new GosuIssue.GosuIssueBuilder(this)
                        .onToken(token)
                        .withMessage("Complete the task associated to this TODO comment.")
                        .build());
            }
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
