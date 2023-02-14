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
package de.friday.sonarqube.gosu.plugin.issues;

import de.friday.sonarqube.gosu.plugin.utils.TextRangeUtil;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sonar.api.batch.fs.TextRange;

public class SecondaryIssue implements Comparable<SecondaryIssue> {
    final TextRange range;
    @Nullable
    final String message;

    public SecondaryIssue(TextRange range, @Nullable String message) {
        this.range = range;
        this.message = message;
    }

    public SecondaryIssue(Token token, @Nullable String message) {
        this.range = TextRangeUtil.fromToken(token);
        this.message = message;
    }

    public SecondaryIssue(ParserRuleContext context, @Nullable String message) {
        this.range = TextRangeUtil.fromContext(context);
        this.message = message;
    }

    public SecondaryIssue(TerminalNode terminalNode, @Nullable String message) {
        this.range = TextRangeUtil.fromTerminalNode(terminalNode);
        this.message = message;
    }

    public static SecondaryIssue copyOf(SecondaryIssue issue) {
        return new SecondaryIssue(issue.range, issue.message);
    }

    public boolean overlap(@Nonnull SecondaryIssue otherIssue) {
        return this.range.overlap(otherIssue.range);
    }

    @Override
    public int compareTo(@Nonnull SecondaryIssue o) {
        int compareStart = this.range.start().compareTo(o.range.start());
        return compareStart == 0
                ? range.end().compareTo(o.range.end())
                : compareStart;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return this.getClass() == obj.getClass()
                && range.equals(((SecondaryIssue) obj).range)
                && compareMessages(((SecondaryIssue) obj).message);
    }

    private boolean compareMessages(String otherMessage) {
        return this.message == null ? otherMessage == null : this.message.equals(otherMessage);
    }

    @Override
    public int hashCode() {
        int result = range.hashCode();
        result = message == null
                ? 31 * result - 1
                : 31 * result + message.hashCode();
        return result;
    }
}
