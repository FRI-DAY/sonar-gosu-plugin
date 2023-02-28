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
import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.language.utils.GosuUtil;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import de.friday.sonarqube.gosu.plugin.issues.SecondaryIssue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.Token;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = UnusedParameterCheck.KEY)
public class UnusedParameterCheck extends AbstractCheckBase {
    static final String KEY = "UnusedParameterCheck";
    private static final boolean DEFAULT_ADD_ISSUE_IF_ANNOTATED = false;
    private static final List<String> approvedAnnotations = Arrays.asList("@SuppressWarning(\"unchecked\")", "@SuppressWarning(\"rawtypes\")");
    private final GosuFileProperties gosuFileProperties;
    private Deque<Boolean> isInFinalClass = new ArrayDeque<>();
    private Map<Integer, Set<String>> parameters = new HashMap<>();
    private int nestedLevel = -1;

    @RuleProperty(
            key = "Add issue for annotated methods",
            type = "BOOLEAN",
            description = "Boolean flag indicating if issues should be displayed on annotated methods",
            defaultValue = "" + DEFAULT_ADD_ISSUE_IF_ANNOTATED)
    private boolean isCheckingAnnotations = DEFAULT_ADD_ISSUE_IF_ANNOTATED;

    @Inject
    public UnusedParameterCheck(GosuFileProperties gosuFileProperties) {
        this.gosuFileProperties = gosuFileProperties;
    }

    @Override
    public void exitClassSignature(GosuParser.ClassSignatureContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        final boolean isFinal = modifiers != null && !modifiers.FINAL().isEmpty();

        isInFinalClass.push(isFinal);
    }

    @Override
    public void exitEnhancementSignature(GosuParser.EnhancementSignatureContext ctx) {
        isInFinalClass.push(false);
    }

    @Override
    public void exitFunctionSignature(GosuParser.FunctionSignatureContext ctx) {
        initParameters(ctx.parameterDeclarationList());
    }

    @Override
    public void exitPropertySignature(GosuParser.PropertySignatureContext ctx) {
        initParameters(ctx.parameterDeclarationList());
    }

    private void initParameters(GosuParser.ParameterDeclarationListContext parametersList) {
        nestedLevel++;
        parameters.put(nestedLevel, new HashSet<>());
        if (parametersList == null) {
            return;
        }
        for (GosuParser.ParameterDeclarationContext param : parametersList.parameterDeclaration()) {
            addParamToSet(param);
        }
    }

    private void addParamToSet(GosuParser.ParameterDeclarationContext param) {
        GosuParser.IdentifierContext identifier = param.identifier();
        if (identifier != null) {
            parameters.get(nestedLevel).add(identifier.getText());
        } else {
            GosuParser.BlockTypeContext block = param.blockType();
            identifier = block.identifier();
            if (identifier != null) {
                parameters.get(nestedLevel).add(identifier.getText());
            }
        }
    }

    @Override
    public void exitIdentifier(GosuParser.IdentifierContext ctx) {
        String identifier = ctx.getText();
        for (int i = nestedLevel; i >= 0; i--) {
            if (parameters.get(i).contains(identifier)) {
                parameters.get(i).remove(identifier);
            }
        }
    }

    @Override
    public void exitFunction(GosuParser.FunctionContext ctx) {
        Token startToken = ctx.getStart();
        GosuParser.FunctionBodyContext body = ctx.functionBody();
        if (canHaveUnusedParameters(body, startToken)) {
            GosuParser.FunctionSignatureContext signature = ctx.functionSignature();
            addIssueIfViolatingCheck(signature.modifiers(),
                    body.statementBlock(),
                    signature.identifier(),
                    signature.parameterDeclarationList());
        }
        removeParameters();
    }

    @Override
    public void exitProperty(GosuParser.PropertyContext ctx) {
        Token startToken = ctx.getStart();
        GosuParser.FunctionBodyContext body = ctx.functionBody();
        if (canHaveUnusedParameters(body, startToken)) {
            GosuParser.PropertySignatureContext signature = ctx.propertySignature();
            addIssueIfViolatingCheck(signature.modifiers(),
                    body.statementBlock(),
                    signature.identifier(),
                    signature.parameterDeclarationList());
        }
        removeParameters();
    }

    private boolean canHaveUnusedParameters(GosuParser.FunctionBodyContext functionBody, Token startToken) {
        return functionBody != null
                && !parameters.get(nestedLevel).isEmpty()
                && hasNoParamsInJavadoc(startToken);
    }

    private boolean hasNoParamsInJavadoc(Token firstToken) {
        Token prevToken = gosuFileProperties.getToken(firstToken.getTokenIndex() - 1);
        if (prevToken.getType() == GosuLexer.COMMENT) {
            String javadoc = prevToken.getText();
            return !javadoc.contains("@param");
        }
        return true;
    }

    private void addIssueIfViolatingCheck(GosuParser.ModifiersContext modifiers,
                                          GosuParser.StatementBlockContext statements,
                                          GosuParser.IdentifierContext identifier,
                                          GosuParser.ParameterDeclarationListContext parametersList) {
        if (violatesRules(modifiers, statements)) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(identifier)
                    .withMessage("Remove this " + parameters.get(nestedLevel).size() + " unused parameter(s)")
                    .withSecondaryIssues(createSecondaries(parametersList))
                    .build());
        }
    }

    private boolean violatesRules(GosuParser.ModifiersContext modifiers,
                                  GosuParser.StatementBlockContext statements) {
        return isNotEmpty(statements)
                && hasCompliantAnnotations(modifiers)
                && !hasOnlyThrowStatement(statements)
                && !isOverridable(modifiers)
                && !GosuUtil.isOverridden(modifiers);
    }

    private static boolean isNotEmpty(GosuParser.StatementBlockContext statements) {
        return !statements.statement().isEmpty();
    }

    private boolean hasCompliantAnnotations(GosuParser.ModifiersContext modifiers) {
        if (!isCheckingAnnotations || modifiers == null) {
            return true;
        }
        for (GosuParser.AnnotationContext annotation : modifiers.annotation()) {
            if (!approvedAnnotations.contains(annotation.getText())) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasOnlyThrowStatement(GosuParser.StatementBlockContext statements) {
        return statements.statement().size() == 1
                && statements.statement(0).throwStatement() != null;
    }

    private boolean isOverridable(GosuParser.ModifiersContext modifiers) {
        boolean isNotFinal = isOuterClassNotFinal();
        return modifiers == null
                ? isNotFinal
                : isNotFinal && isNotPrivateStaticOrFinal(modifiers);
    }

    private boolean isOuterClassNotFinal() {
        return !isInFinalClass.isEmpty()
                && !isInFinalClass.peek();
    }

    private static boolean isNotPrivateStaticOrFinal(GosuParser.ModifiersContext modifiers) {
        return modifiers.FINAL().isEmpty()
                && modifiers.STATIC().isEmpty()
                && modifiers.PRIVATE().isEmpty();
    }

    private List<SecondaryIssue> createSecondaries(GosuParser.ParameterDeclarationListContext parametersList) {
        List<SecondaryIssue> secondaries = new ArrayList<>();

        for (GosuParser.ParameterDeclarationContext param : parametersList.parameterDeclaration()) {
            GosuParser.IdentifierContext identifier = param.identifier();
            if (identifier == null) {
                identifier = param.blockType().identifier();
            }
            if (identifier != null && parameters.get(nestedLevel).contains(identifier.getText())) {
                secondaries.add(new SecondaryIssue(identifier, "unused"));
            }
        }
        return secondaries;
    }

    private void removeParameters() {
        parameters.remove(nestedLevel);
        nestedLevel--;
    }

    @Override
    public void exitClassBody(GosuParser.ClassBodyContext ctx) {
        isInFinalClass.pop();
    }

    @Override
    public void exitEnhancementBody(GosuParser.EnhancementBodyContext ctx) {
        isInFinalClass.pop();
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
