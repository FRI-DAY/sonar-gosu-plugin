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

import de.friday.sonarqube.gosu.antlr.GosuParser;
import java.util.Objects;
import org.antlr.v4.runtime.Token;

public final class UsesStatement {
    private final String uses;
    private final Token stopToken;
    private final GosuParser.UsesStatementContext context;

    public UsesStatement(GosuParser.UsesStatementContext context) {
        this.uses = context.namespace().getText();
        this.stopToken = context.getStop();
        this.context = context;
    }

    public String getValue() {
        return uses;
    }

    public GosuParser.UsesStatementContext getContext() {
        return context;
    }

    public boolean isWildcardImport() {
        return stopToken.getType() == GosuParser.MUL;
    }

    public String getClassName() {
        final int lastIndexOfDot = uses.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            throw new ClassNameUnavailableException(uses);
        }

        return uses.substring(lastIndexOfDot + 1);
    }

    public boolean startsWith(String prefix) {
        return uses.startsWith(prefix);
    }

    public boolean hasSamePackageAs(String aPackageStatement) {
        return uses.equals(aPackageStatement) || packagesAreEqual(aPackageStatement);
    }

    private boolean packagesAreEqual(String aPackageStatement) {
        return uses.startsWith(aPackageStatement)
                && uses.charAt(aPackageStatement.length()) == '.'
                && uses.indexOf('.', aPackageStatement.length() + 1) == -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsesStatement that = (UsesStatement) o;
        return Objects.equals(uses, that.uses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uses);
    }

    private final class ClassNameUnavailableException extends IllegalArgumentException {
        ClassNameUnavailableException(String usesStatement) {
            super("Could not determine the class name of the uses statement: " + usesStatement);
        }
    }
}
