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
import de.friday.sonarqube.gosu.language.utils.GosuUtil;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import de.friday.sonarqube.gosu.plugin.issues.SecondaryIssue;
import de.friday.sonarqube.gosu.plugin.utils.TextRangeUtil;
import java.util.ArrayList;
import java.util.List;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = TooManyParamsRule.KEY)
public class TooManyParamsRule extends BaseGosuRule {
    static final String KEY = "TooManyParamsRule";
    private static final int CONSTRUCTOR_MAX = 7;
    private static final int METHOD_MAX = 7;

    @RuleProperty(
            key = "Constructor Max",
            description = "Maximum authorized number of parameters for a constructor",
            defaultValue = "" + CONSTRUCTOR_MAX)
    private int maxForConstructor = CONSTRUCTOR_MAX;

    @RuleProperty(
            key = "Max",
            description = "Maximum authorized number of parameters",
            defaultValue = "" + METHOD_MAX)
    private int maxForMethod = METHOD_MAX;

    @Override
    public void exitConstructorSignature(GosuParser.ConstructorSignatureContext ctx) {
        TextRange constructorTextRange = TextRangeUtil.fromTerminalNode(ctx.CONSTRUCT());
        addIssueIfTooManyParams(ctx.parameterDeclarationList(), constructorTextRange, maxForConstructor, "constructor");
    }

    @Override
    public void exitFunctionSignature(GosuParser.FunctionSignatureContext ctx) {
        if (GosuUtil.isOverridden(ctx.modifiers())) {
            return;
        }

        TextRange methodNameTextRange = TextRangeUtil.fromContext(ctx.identifier());
        addIssueIfTooManyParams(ctx.parameterDeclarationList(), methodNameTextRange, maxForMethod, "function");
    }

    private void addIssueIfTooManyParams(GosuParser.ParameterDeclarationListContext params, TextRange textRange, int max, String methodType) {
        int paramsNumber = getParamNumber(params);

        if (paramsNumber != -1 && paramsNumber > max) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onTextRange(textRange)
                    .withMessage(buildMessage(methodType, paramsNumber, max))
                    .withSecondaryIssues(getSecondaryIssues(params))
                    .build());
        }
    }

    private static int getParamNumber(GosuParser.ParameterDeclarationListContext params) {
        if (params == null) {
            return -1;
        }
        return params.parameterDeclaration().size();
    }

    private List<SecondaryIssue> getSecondaryIssues(GosuParser.ParameterDeclarationListContext parameters) {
        List<SecondaryIssue> secondaryIssues = new ArrayList<>();
        for (GosuParser.ParameterDeclarationContext param : parameters.parameterDeclaration()) {
            secondaryIssues.add(new SecondaryIssue(param, null));
        }
        return secondaryIssues;
    }

    private static String buildMessage(String method, int paramsNumber, int max) {
        return "This " + method + " has " + paramsNumber + " parameters which is greater than " + max + " authorized";
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
