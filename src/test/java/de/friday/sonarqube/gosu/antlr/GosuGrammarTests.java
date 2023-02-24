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
package de.friday.sonarqube.gosu.antlr;

import de.friday.test.support.GosuTestFileParser;
import de.friday.test.support.antlr.TreeWalker;
import java.util.Optional;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static de.friday.sonarqube.gosu.antlr.ComplexClassTestFixture.forGosuFile;
import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GosuGrammarTests {

    private static final String GRAMMAR_PARSER_TEST_RESOURCES_DIR = "/parser/";

    @Test
    void shouldParseBasicGosuClass() {
        // given
        final TreeWalker treeWalker = new TreeWalker();

        // when
        final CommonTokenStream commonTokenStream = parse("SimpleClass.gs", treeWalker);

        // then
        assertThat(numberOfNumberLiteralTokensOn(commonTokenStream)).isEqualTo(2);
        assertThat(numberOfClassTokensOn(commonTokenStream)).isOne();
        assertThat(numberOfFunctionTokensOn(commonTokenStream)).isOne();
        assertThat(treeWalker.getReturnStatement()).isOne();
    }

    @Test
    void shouldParseGosuStatements() {
        // given
        final TreeWalker treeWalker = new TreeWalker();

        // when
        final CommonTokenStream commonTokenStream = parse("statements/Statements.gs", treeWalker);

        // then
        // assignment statement
        assertThat(treeWalker.getAssignmentStatement()).isZero();

        // if/else statement
        assertThat(treeWalker.getIfStatement()).isEqualTo(5);
        assertThat(numberOfElseTokensOn(commonTokenStream)).isEqualTo(4);

        // try/catch/finally statement
        assertThat(treeWalker.getTryStatement()).isEqualTo(2);
        assertThat(treeWalker.getCatchStatement()).isEqualTo(2);
        assertThat(treeWalker.getFinallyStatement()).isEqualTo(2);

        // throw statement
        assertThat(treeWalker.getThrowStatement()).isOne();

        // return statement
        assertThat(treeWalker.getReturnStatement()).isEqualTo(2);

        // forEach loop
        assertThat(treeWalker.getForeachStatement()).isEqualTo(16);
        assertThat(treeWalker.getIntervalExpression()).isEqualTo(16);
    }

    @Test
    void shouldParseMultiLineStringLiterals() {
        // when
        final CommonTokenStream commonTokenStream = parse("expressions/MultilineString.gs");

        // then
        assertThat(numberOfOpenDoubleQuotedStringTokensOn(commonTokenStream)).isEqualTo(1);
        assertThat(numberOfTextTokensOn(commonTokenStream)).isEqualTo(1);
        assertThat(numberOfCloseDoubleQuotedStringTokensOn(commonTokenStream)).isEqualTo(1);
    }

    @Test
    void shouldParseNumberLiterals() {
        // when
        final CommonTokenStream commonTokenStream = parse("statements/NumberLiteral.gs");

        // then
        assertThat(numberOfDotTokensOn(commonTokenStream)).isEqualTo(2);
        assertThat(numberOfNumberLiteralTokensOn(commonTokenStream)).isEqualTo(3);
    }

    @ParameterizedTest
    @MethodSource("getComplexCases")
    void shouldParseComplexCases(ComplexClassTestFixture testFixture) {
        // given
        final String fileName = testFixture.fileName;
        final TreeWalker treeWalker = new TreeWalker();

        // when
        final CommonTokenStream commonTokenStream = parse(fileName, treeWalker);

        // then
        assertThat(treeWalker.getField()).as("Number of fields")
                .isEqualTo(testFixture.expectedNumberOfFields);
        assertThat(treeWalker.getIfStatement()).as("Number of if expressions")
                .isEqualTo(testFixture.expectedNumberOfIfs);
        assertThat(treeWalker.getUnaryExpression()).as("Number of unary expressions")
                .isEqualTo(testFixture.expectedNumberOfUnaryExpressions);
        assertThat(treeWalker.getReturnStatement()).as("Number of return tokens")
                .isEqualTo(testFixture.expectedNumberOfReturns);
        assertThat(treeWalker.getMethodCall()).as("Number of method calls")
                .isEqualTo(testFixture.expectedNumberOfMethodCalls);
        assertThat(treeWalker.getPropertySignature()).as("Number of property signatures")
                .isEqualTo(testFixture.expectedNumberOfPropertiesSignatures);
        assertThat(treeWalker.getSwitchBlockStatement()).as("Number of switch block statements")
                .isEqualTo(testFixture.expectedNumberOfSwitchBlocks);
        assertThat(treeWalker.getNewExpression()).as("Number of new expressions")
                .isEqualTo(testFixture.expectedNumberOfNewExpressions);
        assertThat(treeWalker.getLambdaExpression()).as("Number of lambda expressions")
                .isEqualTo(testFixture.expectedNumberOfLambdaExpressions);

        assertThat(numberOfAtTokensOn(commonTokenStream)).as("Number of at (@) tokens")
                .isEqualTo(testFixture.expectedNumberOfAtTokens);
        assertThat(numberOfUsesTokensOn(commonTokenStream)).as("Number of uses tokens")
                .isEqualTo(testFixture.expectedNumberOfUsesTokens);
        assertThat(numberOfFunctionTokensOn(commonTokenStream)).as("Number of functions")
                .isEqualTo(testFixture.expectedNumberOfFunctions);
    }

    private Stream<Arguments> getComplexCases() {
        return Stream.of(
                forGosuFile("complexCases/BatchProcesses.gs").expects()
                        .functions(5)
                        .atIdentifiers(3)
                        .uses(3)
                        .methodCalls(32)
                        .build().asArgument(),
                forGosuFile("complexCases/ClaimContactDTO.gs").expects()
                        .newExpressions(3)
                        .fields(16)
                        .ifs(12)
                        .unaryExpressions(2)
                        .returns(12)
                        .functions(7)
                        .atIdentifiers(2)
                        .uses(5)
                        .methodCalls(22)
                        .properties(6)
                        .build().asArgument(),
                forGosuFile("complexCases/VendorDTO.gs").expects()
                        .fields(12)
                        .returns(2)
                        .functions(2)
                        .build().asArgument(),
                forGosuFile("complexCases/IncidentDTO.gs").expects()
                        .newExpressions(1)
                        .fields(25)
                        .ifs(32)
                        .unaryExpressions(3)
                        .returns(16)
                        .functions(7)
                        .properties(10)
                        .methodCalls(27)
                        .atIdentifiers(2)
                        .uses(8)
                        .build().asArgument(),
                forGosuFile("complexCases/PoliciesPlugin.gs").expects()
                        .newExpressions(3)
                        .ifs(24)
                        .returns(16)
                        .methodCalls(90)
                        .atIdentifiers(1)
                        .uses(9)
                        .functions(24)
                        .properties(2)
                        .lambdas(13)
                        .build().asArgument(),
                forGosuFile("complexCases/SomeEnhancement.gsx").expects()
                        .newExpressions(1)
                        .ifs(7)
                        .switchBlocks(4)
                        .lambdas(1)
                        .returns(3)
                        .methodCalls(24)
                        .atIdentifiers(1)
                        .uses(2)
                        .functions(4)
                        .build().asArgument()
        );
    }

    private CommonTokenStream parse(String fileName) {
        return parse(fileName, null);
    }

    private CommonTokenStream parse(String fileName, TreeWalker parseListener) {
        final GosuTestFileParser parser = new GosuTestFileParser(GRAMMAR_PARSER_TEST_RESOURCES_DIR + fileName);
        final GosuTestFileParser.GosuFileParsed gosuFileParsed = parser.parse(Optional.ofNullable(parseListener));
        return gosuFileParsed.getSourceFileProperties().getTokenStream();
    }

    private long numberOfFunctionTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.FUNCTION);
    }

    private long numberOfClassTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.CLASS);
    }

    private long numberOfNumberLiteralTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.NumberLiteral);
    }

    private long numberOfElseTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.ELSE);
    }

    private long numberOfCloseDoubleQuotedStringTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.CLOSE_STRING_DQ);
    }

    private long numberOfOpenDoubleQuotedStringTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.OPEN_STRING_DQ);
    }

    private long numberOfTextTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.DQ_TEXT);
    }

    private long numberOfDotTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.DOT);
    }

    private long numberOfAtTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.AT);
    }

    private long numberOfUsesTokensOn(CommonTokenStream commonTokenStream) {
        return getNumberOfTokens(commonTokenStream, GosuLexer.USES);
    }

    private long getNumberOfTokens(CommonTokenStream commonTokenStream, int tokenType) {
        return commonTokenStream.getTokens().stream()
                .filter(token -> token.getType() == tokenType)
                .count();
    }
}
