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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = LoggerRule.KEY)
public class LoggerRule extends BaseGosuRule {
    static final String KEY = "LoggerRule";
    private static final String DEFAULT_LOGGER_REGEX = "LOG(?:GER)?";
    private static final String LOGGER = "Logger";
    private static final String GET_LOGGER = "getLogger";
    private static final String CLASS_SUFFIX = "\\.[Cc]lass";
    private static final Logger LOG = Loggers.get(LoggerRule.class);
    private static final Pattern MATCH_ALL_LOGGERS_PATTERN = Pattern.compile("\\w+");
    @RuleProperty(
            key = "format",
            description = "Regular expression used to check the logger names against.",
            defaultValue = "" + DEFAULT_LOGGER_REGEX)
    private String regex = DEFAULT_LOGGER_REGEX;
    private Pattern loggerPattern;
    private String className;

    @Override
    public void enterStart(GosuParser.StartContext ctx) {
        try {
            loggerPattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            LOG.error("LoggerRule - wrong syntax of \"format\" property - " + regex, e);
            loggerPattern = MATCH_ALL_LOGGERS_PATTERN;
        }
    }

    @Override
    public void exitClassSignature(GosuParser.ClassSignatureContext ctx) {
        className = ctx.identifier().getText();
    }

    @Override
    public void exitField(GosuParser.FieldContext ctx) {
        GosuParser.ExpressionContext expression = ctx.expression();
        if (!(expression instanceof GosuParser.MemberAccessContext)
                || !isMethodCall((GosuParser.MemberAccessContext) expression)) {
            return;
        }
        GosuParser.MemberAccessContext memberAccessContext = (GosuParser.MemberAccessContext) expression;
        GosuParser.MethodCallContext methodCallContext
                = (GosuParser.MethodCallContext) memberAccessContext.expression(1);

        if (isNotLogger(memberAccessContext, methodCallContext)) {
            return;
        }

        verifyLogger(ctx, methodCallContext.arguments().argExpression(0));
    }

    private static boolean isMethodCall(GosuParser.MemberAccessContext ctx) {
        return ctx.expression(1) instanceof GosuParser.MethodCallContext;
    }

    private static boolean isNotLogger(GosuParser.MemberAccessContext memberAccessContext,
                                       GosuParser.MethodCallContext methodCallContext) {
        return !isLoggerFactory(memberAccessContext.expression(0))
                || !hasGetLoggerMethod(methodCallContext.expression())
                || hasNoArguments(methodCallContext);
    }

    private static boolean isLoggerFactory(GosuParser.ExpressionContext context) {
        return context.getText().contains(LOGGER);
    }

    private static boolean hasGetLoggerMethod(GosuParser.ExpressionContext context) {
        return GET_LOGGER.equals(context.getText());
    }

    private static boolean hasNoArguments(GosuParser.MethodCallContext methodCallContext) {
        return methodCallContext.arguments().argExpression().isEmpty();
    }

    private void verifyLogger(GosuParser.FieldContext ctx, GosuParser.ArgExpressionContext methodCallParameter) {
        checkClassName(ctx, methodCallParameter);
        checkFieldName(ctx);
        checkModifiers(ctx);
    }

    private void checkClassName(GosuParser.FieldContext ctx, GosuParser.ArgExpressionContext methodCallParameter) {
        if (isExactName(methodCallParameter) || isNameWithClassSuffix(methodCallParameter)) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(ctx)
                    .withMessage("Class name passed in logger should be the same as class name")
                    .build());
        }
    }

    private boolean isExactName(GosuParser.ArgExpressionContext methodCallParameter) {
        return methodCallParameter.expression() instanceof GosuParser.PrimaryExpressionContext
                && !className.equals(methodCallParameter.getText());
    }

    private boolean isNameWithClassSuffix(GosuParser.ArgExpressionContext methodCallParameter) {
        if (!(methodCallParameter.expression() instanceof GosuParser.MemberAccessContext)) {
            return false;
        }
        GosuParser.MemberAccessContext memberAccessContext
                = (GosuParser.MemberAccessContext) methodCallParameter.expression();

        if (!(memberAccessContext.expression(0) instanceof GosuParser.PrimaryExpressionContext)
                || !(memberAccessContext.expression(1) instanceof GosuParser.PrimaryExpressionContext)) {
            return false;
        }

        String identifier = memberAccessContext.getText();
        return !identifier.matches(className + CLASS_SUFFIX);
    }

    private void checkFieldName(GosuParser.FieldContext ctx) {
        if (!loggerPattern.matcher(ctx.identifier(0).getText()).matches()) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(ctx)
                    .withMessage("Logger name should match convention")
                    .build());
        }
    }

    private void checkModifiers(GosuParser.FieldContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        if (modifiers == null) {
            return;
        }
        if (modifiers.STATIC().isEmpty()
                || modifiers.FINAL().isEmpty()
                || !modifiers.PUBLIC().isEmpty()
                || !modifiers.PROTECTED().isEmpty()) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(ctx)
                    .withMessage("Make the \"" + ctx.identifier(0).getText() + "\" logger private static final.")
                    .build());
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
