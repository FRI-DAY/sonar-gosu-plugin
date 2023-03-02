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
import java.util.List;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = MagicNumbersRule.KEY)
public class MagicNumbersRule extends BaseGosuRule {
    static final String KEY = "MagicNumbersRule";
    private static final int TWO_LETTERS_SUFFIX_LENGTH = 2;
    private static final String DEFAULT_NUMBERS = "-1,0,1";
    @SuppressWarnings("squid:S4784")
    private static final Pattern FLOATING_POINT_PATTERN = Pattern.compile("[+-]?([0-9]*)?[.]0+([a-zA-Z])?");
    @RuleProperty(
            key = "Authorized numbers",
            description = "Comma separated list of authorized numbers. Example: -1,0,1,2",
            defaultValue = "" + DEFAULT_NUMBERS)
    private String approvedNumbers = DEFAULT_NUMBERS;
    private final List<String> numbers = Arrays.asList(approvedNumbers.split(",", -1));
    private IN_HASHCODE hashCodeFlag = IN_HASHCODE.FALSE;
    private GosuFileProperties gosuFileProperties;

    private enum IN_HASHCODE {
        TRUE,
        FALSE
    }

    @Inject
    MagicNumbersRule(GosuFileProperties gosuFileProperties) {
        this.gosuFileProperties = gosuFileProperties;
    }

    @Override
    public void exitFunctionSignature(GosuParser.FunctionSignatureContext ctx) {
        if ("hashCode".equals(ctx.identifier().getText())) {
            hashCodeFlag = IN_HASHCODE.TRUE;
        }
    }

    @Override
    public void exitFunction(GosuParser.FunctionContext ctx) {
        hashCodeFlag = IN_HASHCODE.FALSE;
    }

    @Override
    public void exitLocalVarStatement(GosuParser.LocalVarStatementContext ctx) {
        if (ctx.expression() instanceof GosuParser.PrimaryExpressionContext
                || ctx.EQUALS() == null
                || hashCodeFlag == IN_HASHCODE.TRUE) {
            return;
        }
        int equalsTokenIndex = ctx.EQUALS().getSymbol().getTokenIndex();
        Token token = gosuFileProperties.getToken(equalsTokenIndex + 1);

        if (token.getType() == GosuLexer.NumberLiteral) {
            tryAddIssue(token);
        }
    }

    @Override
    public void exitField(GosuParser.FieldContext ctx) {
        if (ctx.expression() instanceof GosuParser.PrimaryExpressionContext
                || ctx.EQUALS() == null) {
            return;
        }
        int equalsTokenIndex = ctx.EQUALS().getSymbol().getTokenIndex();
        Token token = gosuFileProperties.getToken(equalsTokenIndex + 1);

        if (token.getType() == GosuLexer.NumberLiteral) {
            tryAddIssue(token);
        }
    }

    @Override
    public void exitNumberLiteral(GosuParser.NumberLiteralContext ctx) {
        ParserRuleContext statementWithMagicNumber = ctx.getParent()  //literal
                .getParent()                                          //primary
                .getParent()                                          //primaryExpression
                .getParent();

        if (statementWithMagicNumber instanceof GosuParser.LocalVarStatementContext
                || statementWithMagicNumber instanceof GosuParser.FieldContext
                || statementWithMagicNumber instanceof GosuParser.CaseOrDefaultStatementContext
                || hashCodeFlag == IN_HASHCODE.TRUE) {
            return;
        }
        tryAddIssue(ctx.NumberLiteral().getSymbol());
    }

    private void tryAddIssue(Token token) {
        String literal = removeSuffix(token.getText());

        if (!numbers.contains(literal) && !numbers.contains(removeFloatingPoint(literal))) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Assign this magic number " + token.getText() + " to a well-named constant, and use the constant instead.")
                    .onToken(token)
                    .build());
        }
    }

    private static String removeSuffix(String literal) {

        if (Character.isDigit(literal.charAt(literal.length() - 1))) {
            return literal;
        } else if (Character.isDigit(literal.charAt(literal.length() - TWO_LETTERS_SUFFIX_LENGTH))) {
            return literal.substring(0, literal.length() - 1);
        } else {
            return literal.substring(0, literal.length() - TWO_LETTERS_SUFFIX_LENGTH);
        }
    }

    private static String removeFloatingPoint(String literal) {
        if (FLOATING_POINT_PATTERN.matcher(literal).matches()) {
            literal = literal.substring(0, literal.indexOf('.'));
            return literal.length() == 0 ? "0" : literal;
        }
        return literal;
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
