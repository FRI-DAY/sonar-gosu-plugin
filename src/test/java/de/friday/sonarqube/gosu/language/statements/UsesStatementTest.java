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
package de.friday.sonarqube.gosu.language.statements;

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsesStatementTest {

    @Test
    void shouldReturnTrueWhenIsWildcardImport() {
        // given
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.SomeClass", aWildcardToken()
        ));

        // when
        final boolean isWildCardImport = aUsesStatement.isWildcardImport();

        // then
        assertThat(isWildCardImport).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsNotWildcardImport() {
        // given
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.SomeClass", aIdentifierTokenFor("SomeClass")
        ));

        // when
        final boolean isWildCardImport = aUsesStatement.isWildcardImport();

        // then
        assertThat(isWildCardImport).isFalse();
    }

    @Test
    void shouldReturnUsesClassName() {
        // given
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.MyClass", aIdentifierTokenFor("MyClass")
        ));

        // when
        final String className = aUsesStatement.getClassName();

        // then
        assertThat(className).isEqualTo("MyClass");
    }

    @Test
    void shouldThrowClassNameUnavailableExceptionWhenClassNameCanNotBeDetermined() {
        // given
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "somePackage", aWildcardToken()
        ));

        // when // then
        assertThatThrownBy(aUsesStatement::getClassName)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not determine the class name of the uses statement: somePackage");
    }

    @Test
    void shouldReturnTrueWhenObjectsAreIdentical() {
        // given
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.MyClass", aIdentifierTokenFor("MyClass")
        ));
        final UsesStatement anotherUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.MyClass", aIdentifierTokenFor("MyClass")
        ));

        // when // then
        assertThat(aUsesStatement).isEqualTo(anotherUsesStatement);
    }

    @Test
    void shouldReturnFalseWhenObjectsAreDifferent() {
        // given
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.MyClass", aIdentifierTokenFor("MyClass")
        ));
        final UsesStatement anotherUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "java.HelloWorld", aIdentifierTokenFor("HelloWorld")
        ));

        // when // then
        assertThat(aUsesStatement).isNotEqualTo(anotherUsesStatement);
    }

    @Test
    void shouldReturnTrueWhenUsesStatementsHaveSamePackages() {
        // given
        final String aPackage = "de.friday.api";
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.api.PotatoesApi", aIdentifierTokenFor("PotatoesAPi")
        ));

        // when
        final boolean hasSamePackages = aUsesStatement.hasSamePackageAs(aPackage);

        // then
        assertThat(hasSamePackages).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUsesStatementsPackagesAreDifferent() {
        // given
        final String aPackage = "de.friday.filters";
        final UsesStatement aUsesStatement = new UsesStatement(aMockedUsesStatementContextWith(
                "de.friday.HelloWorld", aIdentifierTokenFor("HelloWorld")
        ));

        // when
        final boolean hasSamePackages = aUsesStatement.hasSamePackageAs(aPackage);

        // when
        assertThat(hasSamePackages).isFalse();
    }

    private GosuParser.UsesStatementContext aMockedUsesStatementContextWith(String usesStatement, Token stopToken) {
        final GosuParser.UsesStatementContext mock = Mockito.mock(GosuParser.UsesStatementContext.class);
        final GosuParser.NamespaceContext namespaceContextMock = Mockito.mock(GosuParser.NamespaceContext.class);

        Mockito.when(namespaceContextMock.getText()).thenReturn(usesStatement);
        Mockito.when(mock.namespace()).thenReturn(namespaceContextMock);
        Mockito.when(mock.getStop()).thenReturn(stopToken);

        return mock;
    }

    private CommonToken aWildcardToken() {
        return new CommonToken(GosuLexer.MUL, "*");
    }

    private CommonToken aIdentifierTokenFor(String className) {
        return new CommonToken(GosuLexer.IDENTIFIER, className);
    }
}
