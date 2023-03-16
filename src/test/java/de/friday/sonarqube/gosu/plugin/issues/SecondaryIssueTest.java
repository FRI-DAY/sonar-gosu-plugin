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

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import static org.assertj.core.api.Assertions.assertThat;

class SecondaryIssueTest {

    @Test
    void shouldReturnTrueWhenSecondaryIssuesAreEqual() {
        TextPointer start0 = new DefaultTextPointer(0, 1);
        TextPointer end0 = new DefaultTextPointer(0, 5);
        TextRange textRange0 = new DefaultTextRange(start0, end0);

        TextPointer start1 = new DefaultTextPointer(0, 1);
        TextPointer end1 = new DefaultTextPointer(0, 5);
        TextRange textRange1 = new DefaultTextRange(start1, end1);

        TextPointer start2 = new DefaultTextPointer(1, 1);
        TextPointer end2 = new DefaultTextPointer(1, 5);
        TextRange textRange2 = new DefaultTextRange(start2, end2);

        TextPointer start3 = new DefaultTextPointer(0, 1);
        TextPointer end3 = new DefaultTextPointer(1, 5);
        TextRange textRange3 = new DefaultTextRange(start3, end3);

        SecondaryIssue secondaryIssue0 = new SecondaryIssue(textRange0, "SomeText");
        SecondaryIssue secondaryIssue1 = new SecondaryIssue(textRange1, "SomeText");
        SecondaryIssue secondaryIssue2 = new SecondaryIssue(textRange2, "SomeText2");
        SecondaryIssue secondaryIssue3 = new SecondaryIssue(textRange0, "SomeText2");
        SecondaryIssue secondaryIssue4 = new SecondaryIssue(textRange0, null);
        SecondaryIssue secondaryIssue5 = new SecondaryIssue(textRange1, null);
        SecondaryIssue secondaryIssue6 = new SecondaryIssue(textRange0, "SomeText");
        SecondaryIssue secondaryIssue7 = new SecondaryIssue(textRange3, "SomeText");

        //Secondary Issues
        assertThat(secondaryIssue0)
                .isEqualTo(secondaryIssue1)
                .isNotEqualTo(secondaryIssue3);
        assertThat(secondaryIssue4)
                .isEqualTo(secondaryIssue5)
                .isNotEqualTo(secondaryIssue6);

        //HashCodes
        assertThat(secondaryIssue0)
                .hasSameHashCodeAs(secondaryIssue1)
                .doesNotHaveSameHashCodeAs(secondaryIssue2)
                .doesNotHaveSameHashCodeAs(secondaryIssue3);;

        assertThat(secondaryIssue4)
                .hasSameHashCodeAs(secondaryIssue5)
                .doesNotHaveSameHashCodeAs(secondaryIssue6);

        //CompareTo
        assertThat(secondaryIssue0)
                .isEqualByComparingTo(secondaryIssue1)
                .isNotEqualByComparingTo(secondaryIssue2)
                .isLessThanOrEqualTo(secondaryIssue7);
        assertThat(secondaryIssue1)
                .isNotEqualByComparingTo(secondaryIssue2)
                .isGreaterThanOrEqualTo(secondaryIssue1);

        //Overlapping
        assertThat(secondaryIssue0.overlap(secondaryIssue1)).isTrue();
        assertThat(secondaryIssue1.overlap(secondaryIssue0)).isTrue();

        assertThat(secondaryIssue0.overlap(secondaryIssue2)).isFalse();
        assertThat(secondaryIssue2.overlap(secondaryIssue0)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenSecondaryIssueFromContextAreEqual() {
        CommonToken token = new CommonToken(GosuLexer.IDENTIFIER, "NameOfClass");
        token.setLine(12);
        token.setCharPositionInLine(1);

        ParserRuleContext parserRuleContext = new ParserRuleContext();
        parserRuleContext.start = token;
        parserRuleContext.stop = token;

        SecondaryIssue first = new SecondaryIssue(parserRuleContext, "TestMessage");
        SecondaryIssue second = new SecondaryIssue(token, "TestMessage");

        assertThat(first).isEqualTo(second);
    }
}
