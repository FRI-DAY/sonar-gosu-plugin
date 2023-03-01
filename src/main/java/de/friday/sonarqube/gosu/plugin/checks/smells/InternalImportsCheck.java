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
import de.friday.sonarqube.gosu.plugin.issues.SecondaryIssue;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.sonar.check.Rule;

@Rule(key = InternalImportsCheck.KEY)
public class InternalImportsCheck extends AbstractCheckBase {
    static final String KEY = "InternalImportsCheck";
    private static final String COM_GUIDEWIRE = "com.guidewire";
    private static final String INTERNAL = "internal";

    private List<SecondaryIssue> internalImportsOccurrences = new ArrayList<>();
    private ParserRuleContext contextToHighlight;

    @Override
    public void exitUsesStatement(GosuParser.UsesStatementContext ctx) {
        final GosuParser.NamespaceContext importName = ctx.namespace();
        saveIfInternalImport(importName);
    }

    @Override
    public void exitClassOrInterfaceType(GosuParser.ClassOrInterfaceTypeContext context) {
        saveIfInternalImport(context.namespace());
    }

    @Override
    public void exitMethodCall(GosuParser.MethodCallContext context) {
        final ParserRuleContext parent = context.getParent();
        if (isNotMemberAccessContext(parent)) {
            return;
        }

        saveIfInternalImport(parent);
    }

    private boolean isNotMemberAccessContext(ParserRuleContext parent) {
        return !(parent instanceof GosuParser.MemberAccessContext);
    }

    private void saveIfInternalImport(ParserRuleContext namespace) {
        if (isNull(namespace)) return;

        if (isInternalPackage(namespace.getText())) {
            addNewOccurrence(new SecondaryIssue(namespace, "Internal import used"));
        }
    }

    private boolean isNull(ParserRuleContext parent) {
        return parent == null;
    }

    private boolean isInternalPackage(String namespace) {
        return namespace.startsWith(COM_GUIDEWIRE) || isInternalGuidewirePackage(namespace);
    }

    private boolean isInternalGuidewirePackage(String namespace) {
        return namespace.contains(INTERNAL) && isGuidewirePackage(namespace);
    }

    private boolean isGuidewirePackage(String namespace) {
        return namespace.contains("gw") || namespace.contains("guidewire");
    }

    private void addNewOccurrence(SecondaryIssue occurrence) {
        if (isNotOverlapping(occurrence)) {
            internalImportsOccurrences.add(occurrence);
        }
    }

    private boolean isNotOverlapping(SecondaryIssue issue) {
        int lastItemIndex = internalImportsOccurrences.size() - 1;
        return lastItemIndex == -1 || !internalImportsOccurrences.get(lastItemIndex).overlap(issue);
    }

    @Override
    public void exitClassSignature(GosuParser.ClassSignatureContext ctx) {
        setContextToHighlight(ctx.identifier());
    }

    @Override
    public void exitEnhancementSignature(GosuParser.EnhancementSignatureContext ctx) {
        setContextToHighlight(ctx.identifier());
    }

    @Override
    public void exitInterfaceSignature(GosuParser.InterfaceSignatureContext ctx) {
        setContextToHighlight(ctx.identifier());
    }

    private void setContextToHighlight(ParserRuleContext identifier) {
        if (contextToHighlight == null) {
            contextToHighlight = identifier;
        }
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        if (contextToHighlight != null && !internalImportsOccurrences.isEmpty()) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Consider refactor of " + contextToHighlight.getText() + " to avoid using Guidewire internal imports.")
                    .onContext(contextToHighlight)
                    .withSecondaryIssues(internalImportsOccurrences)
                    .build());
        }
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
