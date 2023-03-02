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
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = LoggerLibraryCheck.KEY)
public class LoggerLibraryCheck extends BaseGosuRule {
    static final String KEY = "LoggerLibraryCheck";
    private static final String DEFAULT_LOGGER = "org.slf4j,gw.api";
    private static final String LOGGER = "logger";
    @RuleProperty(
            key = "libs",
            description = "Comma separated logger libraries approved in project.",
            defaultValue = "" + DEFAULT_LOGGER)
    private String approvedLoggers = DEFAULT_LOGGER;
    private List<String> loggers = Arrays.asList(approvedLoggers.split(",", -1));

    @Override
    public void exitUsesStatement(GosuParser.UsesStatementContext ctx) {
        if(ctx == null || ctx.namespace() == null) { return; }
        String usesStatement = ctx.namespace().getText();
        if (isWrongLogger(usesStatement)) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Project specific logger API should be used")
                    .onContext(ctx)
                    .build());
        }
    }

    private boolean isWrongLogger(String usesStatement) {
        return StringUtils.containsIgnoreCase(usesStatement, LOGGER)
                && !isLibraryInPermittedLoggers(usesStatement);
    }

    private boolean isLibraryInPermittedLoggers(String usesStatement){
        for(String logger : loggers){
            logger = logger.trim();
            if(logger.endsWith("*")){
                logger = logger.substring(0, logger.length() - 1);
            }
            if(usesStatement.startsWith(logger)){
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
