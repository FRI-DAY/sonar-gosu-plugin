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
package de.friday.sonarqube.gosu.plugin.utils;

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.language.utils.GosuUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultTextRange;

public final class TextRangeUtil {
    private static final Pattern LINE_SEPARATOR = Pattern.compile("\\r?\\n");

    private TextRangeUtil() {
    }

    public static TextRange fromToken(Token token) {
        TextRange tokenTextRange;
        switch (token.getType()) {
            case GosuLexer.DQ_TEXT:
            case GosuLexer.SQ_TEXT:
            case GosuLexer.COMMENT:
                tokenTextRange = getMultilineTokenTextRange(token);
                break;
            default:
                tokenTextRange = fromPosition(token.getLine(), token.getCharPositionInLine(), token.getLine(), getStopPosition(token));
        }
        return tokenTextRange;
    }

    public static TextRange fromPointers(TextPointer start, TextPointer stop) {
        return new DefaultTextRange(start, stop);
    }

    public static TextRange fromPosition(int startLine, int startOffset, int stopLine, int stopOffset) {
        final TextPointer start = new InternalTextPointer(startLine, startOffset);
        final TextPointer stop = new InternalTextPointer(stopLine, stopOffset);

        return fromPointers(start, stop);
    }

    public static TextRange fromTokens(Token startToken, Token endToken) {
        final TextPointer start = new InternalTextPointer(startToken.getLine(), startToken.getCharPositionInLine());
        final TextPointer stop = endToken.getType() == GosuLexer.COMMENT
                ? fromToken(endToken).end()
                : new InternalTextPointer(endToken.getLine(), endToken.getCharPositionInLine() + endToken.getText().length());

        return fromPointers(start, stop);
    }

    public static TextRange fromContext(ParserRuleContext context) {
        int startOffset = context.getStart().getCharPositionInLine();
        int startLine = context.getStart().getLine();

        Token token = GosuUtil.getStopToken(context);
        int stopOffset = getStopPosition(token);
        int stopLine = token.getLine();

        return fromPosition(startLine, startOffset, stopLine, stopOffset);
    }

    public static TextRange fromTerminalNode(TerminalNode terminalNode) {
        return fromToken(terminalNode.getSymbol());
    }

    private static TextRange getMultilineTokenTextRange(Token token) {
        List<String> listOfLines = Arrays.asList(LINE_SEPARATOR.split(token.getText()));
        final TextPointer start = new InternalTextPointer(token.getLine(), token.getCharPositionInLine());
        final TextPointer stop = getEndTextPointerForMultilineToken(token, listOfLines);
        return TextRangeUtil.fromPointers(start, stop);
    }

    private static TextPointer getEndTextPointerForMultilineToken(Token token, List<String> listOfLines) {
        int lastLineIndex = listOfLines.size() - 1;
        int lastLineLength = listOfLines.get(lastLineIndex).length();

        if (lastLineIndex == 0) {
            lastLineLength += token.getCharPositionInLine();
        }
        return new InternalTextPointer(token.getLine() + lastLineIndex,
                lastLineLength);
    }

    private static int getStopPosition(Token token) {
        if (token.getType() == -1) {
            return token.getCharPositionInLine();
        } else {
            return token.getCharPositionInLine() + token.getText().length();
        }
    }

    private static class InternalTextPointer implements TextPointer {

        private final int line;
        private final int lineOffset;

        public InternalTextPointer(int line, int lineOffset) {
            this.line = line;
            this.lineOffset = lineOffset;
        }

        @Override
        public int line() {
            return line;
        }

        @Override
        public int lineOffset() {
            return lineOffset;
        }

        @Override
        public String toString() {
            return "[line=" + line + ", lineOffset=" + lineOffset + "]";
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TextPointer)) {
                return false;
            }
            final TextPointer other = (TextPointer) obj;
            return other.line() == this.line && other.lineOffset() == this.lineOffset;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.line, this.lineOffset);
        }

        @Override
        public int compareTo(TextPointer o) {
            if (this.line == o.line()) {
                return Integer.compare(this.lineOffset, o.lineOffset());
            }
            return Integer.compare(this.line, o.line());
        }

    }
}
