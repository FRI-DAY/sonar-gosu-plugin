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
import java.util.Arrays;
import java.util.List;
import org.sonar.check.Rule;

@Rule(key = StringBuilderInstantiationCheck.KEY)
public class StringBuilderInstantiationCheck extends BaseGosuRule {
    static final String KEY = "StringBuilderInstantiationCheck";
    private static final List<String> STRING_BUILDERS = Arrays.asList("StringBuilder", "StringBuffer");

    @Override
    public void exitNewExpression(GosuParser.NewExpressionContext ctx) {
        GosuParser.ClassOrInterfaceTypeContext constructor = ctx.classOrInterfaceType();
        GosuParser.ArgumentsContext arguments = ctx.arguments();

        if (constructor == null || arguments == null) {
            return;
        }

        if (STRING_BUILDERS.contains(constructor.getText()) && isInitializedWithChar(arguments)) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(ctx)
                    .withMessage("\"StringBuilder\" and \"StringBuffer\" should not be instantiated with a character")
                    .build());
        }
    }

    private static boolean isInitializedWithChar(GosuParser.ArgumentsContext arguments) {
        return !arguments.argExpression().isEmpty() && isChar(arguments.argExpression(0));
    }

    private static boolean isChar(GosuParser.ArgExpressionContext argExpressionContext) {
        return argExpressionContext.getText().startsWith("\'");
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
