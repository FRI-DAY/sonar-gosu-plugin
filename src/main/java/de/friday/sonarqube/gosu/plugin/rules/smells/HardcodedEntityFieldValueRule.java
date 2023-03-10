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
import org.sonar.check.Rule;

@Rule(key = HardcodedEntityFieldValueRule.KEY)
public class HardcodedEntityFieldValueRule extends BaseGosuRule {
    static final String KEY = "HardcodedEntityFieldValueRule";
    private static final List<String> GET_SET_FIELD_METHODS = Arrays.asList("getFieldValue", "setFieldValue");

    @Override
    public void exitMethodCall(GosuParser.MethodCallContext ctx) {
        GosuParser.ArgumentsContext arguments = ctx.arguments();
        if (arguments.argExpression().isEmpty()) {
            return;
        }

        GosuParser.ExpressionContext expression = ctx.expression();
        if (GET_SET_FIELD_METHODS.contains(expression.getText())) {
            addIssueIfHardcodedString(arguments);
        }
    }

    private void addIssueIfHardcodedString(GosuParser.ArgumentsContext arguments) {
        GosuParser.ArgExpressionContext arg = arguments.argExpression(0);
        if (arg.getText().startsWith("\"")) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Hardcoded Strings should not be used to get entity value")
                    .onContext(arg)
                    .build());
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
