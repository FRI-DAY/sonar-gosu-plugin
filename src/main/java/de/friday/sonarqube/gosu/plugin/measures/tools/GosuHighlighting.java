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
package de.friday.sonarqube.gosu.plugin.measures.tools;

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.sonarqube.gosu.plugin.utils.TextRangeUtil;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;

public final class GosuHighlighting {

    private GosuHighlighting() {
    }

    public static void highlightToken(Token token, NewHighlighting highlighting, Properties properties) {
        switch (token.getType()) {
            case GosuLexer.EOF:
            case GosuLexer.IDENTIFIER:
                break;
            case GosuLexer.LINE_COMMENT:
                addHighlighting(token, TypeOfText.COMMENT, highlighting);
                break;
            case GosuLexer.COMMENT:
                addHighlighting(token, TypeOfText.STRUCTURED_COMMENT, highlighting);
                break;
            case GosuLexer.OPEN_STRING_DQ:
            case GosuLexer.OPEN_STRING_SQ:
            case GosuLexer.CLOSE_STRING_DQ:
            case GosuLexer.CLOSE_STRING_SQ:
            case GosuLexer.DQ_TEXT:
            case GosuLexer.SQ_TEXT:
            case GosuLexer.CHAR_LITERAL:
                addHighlighting(token, TypeOfText.STRING, highlighting);
                break;
            case GosuLexer.NumberLiteral:
                addHighlighting(token, TypeOfText.CONSTANT, highlighting);
                break;
            case GosuLexer.AT:
                addHighlightingForAnnotation(token, highlighting, properties);
                break;
            case GosuLexer.CONJ:
            case GosuLexer.DISJ:
                addHighlighting(token, TypeOfText.KEYWORD, highlighting);
                break;
            default:
                if (token.getType() < GosuLexer.LPAREN) {
                    addHighlighting(token, TypeOfText.KEYWORD, highlighting);
                }
        }
    }

    private static void addHighlightingForAnnotation(Token token, NewHighlighting highlighting, Properties properties) {
        final Token nextToken = properties.getTokenStream().get(token.getTokenIndex() + 1);
        final TextRange textRange = TextRangeUtil.fromTokens(token, nextToken);
        addHighlighting(textRange, TypeOfText.ANNOTATION, highlighting);
    }

    private static void addHighlighting(Token token, TypeOfText typeOfText, NewHighlighting highlighting) {
        final TextRange textRange = TextRangeUtil.fromToken(token);
        addHighlighting(textRange, typeOfText, highlighting);
    }

    private static void addHighlighting(TextRange textRange, TypeOfText typeOfText, NewHighlighting highlighting) {
        highlighting.highlight(
                textRange.start().line(),
                textRange.start().lineOffset(),
                textRange.end().line(),
                textRange.end().lineOffset(),
                typeOfText
        );
    }

}
