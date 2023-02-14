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
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import static org.assertj.core.api.Assertions.assertThat;

class TextRangeUtilTest {

    @Test
    void shouldReturnTextRangeFromLocations() {
        TextPointer start = new DefaultTextPointer(0, 1);
        TextPointer stop = new DefaultTextPointer(0, 5);
        TextRange textRange = new DefaultTextRange(start, stop);

        assertThat(TextRangeUtil.fromPosition(0, 1, 0, 5)).isEqualTo(textRange);
        assertThat(TextRangeUtil.fromPointers(start, stop)).isEqualTo(textRange);
    }

    @Test
    void shouldReturnTextRangeFromContext() {
        TextPointer start = new DefaultTextPointer(12, 1);
        TextPointer stop = new DefaultTextPointer(12, 1 + "NameOfClass".length());
        TextRange textRange = new DefaultTextRange(start, stop);

        TextPointer stop2 = new DefaultTextPointer(13, 9 + "SomeTextHere".length());
        TextRange textRange2 = new DefaultTextRange(start, stop2);

        CommonToken token = new CommonToken(GosuLexer.IDENTIFIER, "NameOfClass");
        token.setLine(12);
        token.setCharPositionInLine(1);
        CommonToken token2 = new CommonToken(GosuLexer.IDENTIFIER, "SomeTextHere");
        token2.setLine(13);
        token2.setCharPositionInLine(9);

        ParserRuleContext parserRuleContext = new ParserRuleContext();
        parserRuleContext.start = token;
        parserRuleContext.stop = token;

        assertThat(TextRangeUtil.fromToken(token)).isEqualTo(textRange);
        assertThat(TextRangeUtil.fromContext(parserRuleContext)).isEqualTo(textRange);
        assertThat(TextRangeUtil.fromTokens(token, token2)).isEqualTo(textRange2);
    }

    @Test
    void shouldReturnTextRangeFromMultilineComment() {
        TextPointer start = new DefaultTextPointer(1, 1);
        TextPointer stop = new DefaultTextPointer(1, 1 + "var x = 12".length());
        TextRange textRange = new DefaultTextRange(start, stop);

        CommonToken token = new CommonToken(GosuLexer.COMMENT, "var x = 12");
        token.setLine(1);
        token.setCharPositionInLine(1);

        TextPointer commentStart = new DefaultTextPointer(1, 11);
        TextPointer commentStop = new DefaultTextPointer(3, 12);
        TextRange commentTextRange = new DefaultTextRange(commentStart, commentStop);

        CommonToken comment = new CommonToken(GosuLexer.COMMENT, "/*\r\n  *some text here\r\n And Here *\\");
        comment.setLine(1);
        comment.setCharPositionInLine(11);

        TextRange textRangeFromTwoTokens = new DefaultTextRange(start, commentStop);


        assertThat(TextRangeUtil.fromToken(token)).isEqualTo(textRange);
        assertThat(TextRangeUtil.fromToken(comment)).isEqualTo(commentTextRange);
        assertThat(TextRangeUtil.fromTokens(token, comment)).isEqualTo(textRangeFromTwoTokens);
    }

    @Test
    void shouldReturnTextRangeFromEOFToken() {
        TextPointer start = new DefaultTextPointer(1, 1);
        TextRange textRange = new DefaultTextRange(start, start);

        CommonToken comment = new CommonToken(GosuLexer.EOF, "<EOF>");
        comment.setLine(1);
        comment.setCharPositionInLine(1);
        comment.setType(-1);

        assertThat(TextRangeUtil.fromToken(comment)).isEqualTo(textRange);
    }
}
