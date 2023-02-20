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
package de.friday.sonarqube.gosu.plugin.checks.smells;

import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sonar.check.Rule;

@Rule(key = DefaultModifiersCheck.KEY)
public class DefaultModifiersCheck extends AbstractCheckBase {
    static final String KEY = "DefaultModifiersCheck";
    private boolean isInInterface;
    private Map<Integer, Boolean> finalClasses = new HashMap<>();
    private int nestedLevel = -1;
    private boolean isOuter = true;

    @Override
    public void exitClassSignature(GosuParser.ClassSignatureContext ctx) {
        nestedLevel++;
        GosuParser.ModifiersContext modifiers = ctx.modifiers();

        finalClasses.put(nestedLevel, hasFinalModifier(modifiers));

        verifyPublicModifierForType(modifiers);
    }

    @Override
    public void exitEnumSignature(GosuParser.EnumSignatureContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        verifyPublicModifierForType(modifiers);
    }

    @Override
    public void exitInterfaceSignature(GosuParser.InterfaceSignatureContext ctx) {
        isInInterface = true;
        GosuParser.ModifiersContext modifiers = ctx.modifiers();

        verifyPublicModifierForType(modifiers);
    }

    @Override
    public void exitFunctionSignature(GosuParser.FunctionSignatureContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        if (hasPublicModifier(modifiers)) {
            addIssueForModifier(modifiers.PUBLIC(0));
        }

        if (isInInterface && hasAbstractModifier(modifiers)) {
            addIssueForModifier(modifiers.ABSTRACT(0));
        }

        if (isFinalClass() && hasFinalModifier(modifiers)) {
            addIssueForModifier(modifiers.FINAL(0));
        }
    }

    @Override
    public void exitPropertySignature(GosuParser.PropertySignatureContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        if (hasPublicModifier(modifiers)) {
            addIssueForModifier(modifiers.PUBLIC(0));
        }
    }

    @Override
    public void exitField(GosuParser.FieldContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        if (hasPrivateModifier(modifiers)) {
            addIssueForModifier(modifiers.PRIVATE(0));
        }
    }

    @Override
    public void exitInterfaceDeclaration(GosuParser.InterfaceDeclarationContext ctx) {
        isInInterface = false;
    }

    @Override
    public void exitClassDeclaration(GosuParser.ClassDeclarationContext ctx) {
        finalClasses.remove(nestedLevel);
        nestedLevel--;
    }

    private void verifyPublicModifierForType(GosuParser.ModifiersContext modifiers) {
        if (!isOuter) {
            return;
        }

        if (hasPublicModifier(modifiers)) {
            addIssueForModifier(modifiers.PUBLIC(0));
        }
        isOuter = false;
    }

    private static boolean hasFinalModifier(GosuParser.ModifiersContext modifiers) {
        return modifiers != null
                && !modifiers.FINAL().isEmpty();
    }

    private static boolean hasPublicModifier(GosuParser.ModifiersContext modifiers) {
        return modifiers != null
                && !modifiers.PUBLIC().isEmpty();
    }

    private static boolean hasPrivateModifier(GosuParser.ModifiersContext modifiers) {
        return modifiers != null
                && !modifiers.PRIVATE().isEmpty();
    }

    private static boolean hasAbstractModifier(GosuParser.ModifiersContext modifiers) {
        return modifiers != null
                && !modifiers.ABSTRACT().isEmpty();
    }

    private boolean isFinalClass() {
        return !finalClasses.isEmpty()
                && finalClasses.get(nestedLevel);
    }

    private void addIssueForModifier(TerminalNode terminalNode) {
        Token token = terminalNode.getSymbol();
        addIssue(new GosuIssue.GosuIssueBuilder(this)
                .onToken(token)
                .withMessage("Remove this unnecessary \"" + token.getText() + "\" modifier")
                .build());
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
