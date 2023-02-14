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
package de.friday.sonarqube.gosu.plugin.tools.listeners;

import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.antlr.GosuParserBaseListener;
import de.friday.sonarqube.gosu.plugin.utils.TextRangeUtil;
import de.friday.sonarqube.gosu.plugin.utils.annotations.UnitTestMissing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.ParserRuleContext;
import org.sonar.api.batch.fs.TextRange;

@UnitTestMissing
public final class SuppressWarningsListener extends GosuParserBaseListener {
    private static final String SUPPRESS_WARNINGS = "SuppressWarnings";
    private static final Pattern GOSU_SUPPRESS_WARNINGS_KEY_PATTERN = Pattern.compile("gosu:\\w+");

    private Map<String, List<TextRange>> gosuSuppressWarningsMap = new HashMap<>();

    public Map<String, List<TextRange>> getSuppressWarnings() {
        return gosuSuppressWarningsMap;
    }

    @Override
    public void exitClassDeclaration(GosuParser.ClassDeclarationContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.classSignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitInterfaceDeclaration(GosuParser.InterfaceDeclarationContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.interfaceSignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitEnumDeclaration(GosuParser.EnumDeclarationContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.enumSignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitEnhancementDeclaration(GosuParser.EnhancementDeclarationContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.enhancementSignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitAnnotationDeclaration(GosuParser.AnnotationDeclarationContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.annotationSignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitConstructor(GosuParser.ConstructorContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.constructorSignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitFunction(GosuParser.FunctionContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.functionSignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitField(GosuParser.FieldContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    @Override
    public void exitProperty(GosuParser.PropertyContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.propertySignature().modifiers();
        processSuppressWarnings(modifiers, ctx);
    }

    private void processSuppressWarnings(GosuParser.ModifiersContext modifiers, ParserRuleContext context) {
        List<String> suppressWarningsKeys = getSuppressWarningsForGosuChecks(modifiers);
        addKeysToMap(suppressWarningsKeys, context);
    }

    private static List<String> getSuppressWarningsForGosuChecks(GosuParser.ModifiersContext modifiers) {
        if (modifiers == null || modifiers.annotation().isEmpty()) {
            return Collections.emptyList();
        }
        List<String> keys = new ArrayList<>();
        for (GosuParser.AnnotationContext annotation : modifiers.annotation()) {
            if (SUPPRESS_WARNINGS.equals(annotation.identifier(0).getText())) {
                keys.addAll(processAnnotationArguments(annotation));
            }
        }
        return keys;
    }

    private static List<String> processAnnotationArguments(GosuParser.AnnotationContext annotation) {
        List<String> gosuSuppressWarningsKeys = new ArrayList<>();
        String[] annotationArguments = splitArguments(annotation.arguments().argExpression(0));

        for (String arg : annotationArguments) {
            getGosuSuppressWarningKey(arg).ifPresent(gosuSuppressWarningsKeys::add);
        }

        return gosuSuppressWarningsKeys;
    }

    private static String[] splitArguments(GosuParser.ArgExpressionContext argumentList) {
        String arguments = argumentList.getText();
        arguments = removeCurlyBrackets(arguments);
        return arguments.split(",");
    }

    private static Optional<String> getGosuSuppressWarningKey(String argument) {
        argument = removeQuotes(argument);
        return GOSU_SUPPRESS_WARNINGS_KEY_PATTERN.matcher(argument).matches()
                ? Optional.of(getKey(argument))
                : Optional.empty();
    }

    private static String getKey(String keyWithNamespace) {
        int start = keyWithNamespace.lastIndexOf(':') + 1;
        int end = keyWithNamespace.length();
        return keyWithNamespace.substring(start, end);
    }

    private static String removeCurlyBrackets(String string) {
        return string.startsWith("{")
                ? string.substring(1, string.length() - 1)
                : string;
    }

    private static String removeQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }

    private void addKeysToMap(List<String> keys, ParserRuleContext context) {
        TextRange contextTextRange = TextRangeUtil.fromContext(context);
        for (String key : keys) {
            addGosuKeyToMap(key, contextTextRange);
        }
    }

    private void addGosuKeyToMap(String key, TextRange textRange) {
        if (gosuSuppressWarningsMap.containsKey(key)) {
            gosuSuppressWarningsMap.get(key).add(textRange);
        } else {
            List<TextRange> positionList = new ArrayList<>();
            positionList.add(textRange);
            gosuSuppressWarningsMap.put(key, positionList);
        }
    }
}
