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

import de.friday.sonarqube.gosu.antlr.GosuParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class GosuUtil {
    private GosuUtil() {
    }

    /**
     * ANTLR trigger exitContext when it will find full matching context.
     * For this reason local variable declaration such as: "var x = 1 + 2"
     * will have two exitLocalVariableDeclaration exit methods.
     * One will be "var x = 1" and second will be full "var x = 1 + 2"
     * First context is not fully parsed so it will have no stop Token used in some calculations
     * Thus we have to recalculate stop as stop of the last child.
     *
     * @param context ParserRuleContext
     * @return stop Token or calculated new stop Token if default stop is null
     */
    public static Token getStopToken(ParserRuleContext context) {
        if (context.getStop() == null) {
            int lastChildIndex = context.getChildCount() - 1;
            ParseTree lastChild = context.getChild(lastChildIndex);

            return lastChild instanceof ParserRuleContext
                    ? ((ParserRuleContext) lastChild).getStop()
                    : ((TerminalNode) lastChild).getSymbol();
        } else {
            return context.getStop();
        }
    }

    /**
     * @param modifiers ModifiersContext
     * @return true if @Override annotation or override keyword are present
     */
    public static boolean isOverridden(GosuParser.ModifiersContext modifiers) {
        if (modifiers == null) {
            return false;
        }
        if (!modifiers.OVERRIDE().isEmpty()) {
            return true;
        }
        for (GosuParser.AnnotationContext annotation : modifiers.annotation()) {
            if ("Override".equals(annotation.identifier(0).getText())) {
                return true;
            }
        }
        return false;
    }
}
