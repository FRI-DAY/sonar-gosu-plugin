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
package de.friday.sonarqube.gosu.language.utils;

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class GosuUtilTest {

    @Test
    void shouldReturnStopTokenFromContext() {
        CommonToken startToken = new CommonToken(GosuLexer.IDENTIFIER, "NameOfClass");
        startToken.setLine(12);
        startToken.setCharPositionInLine(1);
        CommonToken stopToken = new CommonToken(GosuLexer.IDENTIFIER, "SomeTextHere");
        stopToken.setLine(13);
        stopToken.setCharPositionInLine(9);

        ParserRuleContext parserRuleContext = new ParserRuleContext();
        parserRuleContext.start = startToken;
        parserRuleContext.stop = stopToken;

        assertThat(GosuUtil.getStopToken(parserRuleContext)).isEqualTo(stopToken);
    }

    @Test
    void shouldReturnStopTokenFromChildTest() {
        CommonToken startToken = new CommonToken(GosuLexer.IDENTIFIER, "NameOfClass");
        startToken.setLine(12);
        startToken.setCharPositionInLine(1);
        CommonToken stopToken = new CommonToken(GosuLexer.IDENTIFIER, "SomeTextHere");
        stopToken.setLine(13);
        stopToken.setCharPositionInLine(9);

        ParserRuleContext parserRuleContext = new ParserRuleContext();
        parserRuleContext.start = startToken;
        parserRuleContext.stop = stopToken;

        ParserRuleContext mainParseRuleContext = new ParserRuleContext();
        mainParseRuleContext.addChild(parserRuleContext);
        mainParseRuleContext.start = startToken;

        assertThat(GosuUtil.getStopToken(mainParseRuleContext)).isEqualTo(stopToken);
    }
}
