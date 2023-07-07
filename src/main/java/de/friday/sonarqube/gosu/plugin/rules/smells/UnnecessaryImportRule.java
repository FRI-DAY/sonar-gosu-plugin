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
package de.friday.sonarqube.gosu.plugin.rules.smells;

import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.language.statements.UsesStatement;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.check.Rule;

@Rule(key = UnnecessaryImportRule.KEY)
public class UnnecessaryImportRule extends BaseGosuRule {

    static final String KEY = "UnnecessaryImportRule";

    private final Set<UsesStatement> allImports = new HashSet<>();
    private final Set<String> allReferencedClasses = new HashSet<>();
    private String currentPackage;
    private boolean afterUsesStatements = false;
    private GosuParser.UsesStatementListContext usesStatementContext;

    @Override
    protected String getKey() {
        return KEY;
    }

    @Override
    public void exitPackageDeclaration(GosuParser.PackageDeclarationContext context) {
        currentPackage = context.namespace().getText();
    }

    @Override
    public void exitUsesStatement(GosuParser.UsesStatementContext context) {
        if (isNamespaceAvailable(context)) {
            final UsesStatement usesStatement = new UsesStatement(context);
            checkUnnecessaryImports(usesStatement);
            allImports.add(usesStatement);
        }
    }

    @Override
    public void exitUsesFeatureLiteral(GosuParser.UsesFeatureLiteralContext context) {
        final List<String> staticImportClasses = context.children.stream()
                .map(ParseTree::getPayload)
                .filter(ParserRuleContext.class::isInstance)
                .map(parserRuleContext -> ((ParserRuleContext) parserRuleContext).getText())
                .collect(Collectors.toList());
        allReferencedClasses.addAll(staticImportClasses);
    }

    private boolean isNamespaceAvailable(GosuParser.UsesStatementContext context) {
        return context.namespace() != null;
    }

    @Override
    public void exitUsesStatementList(GosuParser.UsesStatementListContext ctx) {
        usesStatementContext = ctx;
        afterUsesStatements = true;
    }

    @Override
    public void exitIdentifier(GosuParser.IdentifierContext identifierContext) {
        final String identifier = identifierContext.getText();

        if (afterUsesStatements && identifier.matches("[A-Z].*")) {
            allReferencedClasses.add(identifier);
        }
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        allImports.stream()
                .filter((usesStatement) -> !allReferencedClasses.contains(usesStatement.getClassName()))
                .forEach(unusedImport ->
                        addIssueWithMessage(
                                "There is unused import of " + unusedImport.getValue() + ".",
                                usesStatementContext
                        )
                );
    }

    private void checkUnnecessaryImports(UsesStatement usesStatement) {
        checkSamePackageImport(usesStatement);
        checkDuplicateImport(usesStatement);

        AlwaysAvailableTypes.forEach(
                alwaysAvailableType -> checkUsageOfClassesAlwaysAvailable(alwaysAvailableType, usesStatement)
        );
    }

    private void checkUsageOfClassesAlwaysAvailable(AlwaysAvailableTypes alwaysAvailableType, UsesStatement usesStatement) {
        if (usesStatement.startsWith(alwaysAvailableType.packagePrefix)) {
            addIssueWithMessage(
                    "Unnecessary import, " + alwaysAvailableType.description + " are always available.",
                    usesStatement.getContext()
            );
        }
    }

    private void checkSamePackageImport(UsesStatement usesStatement) {
        Objects.requireNonNull(currentPackage);

        if (usesStatement.hasSamePackageAs(currentPackage)) {
            addIssueWithMessage(
                    "Unnecessary import, same package classes are always available.",
                    usesStatement.getContext()
            );
        }
    }

    private void checkDuplicateImport(UsesStatement usesStatement) {
        if (allImports.contains(usesStatement)) {
            addIssueWithMessage(
                    "Unnecessary import, it is a duplicate.",
                    usesStatement.getContext()
            );
        }
    }

    private void addIssueWithMessage(String message, ParserRuleContext ctx) {
        addIssue(new GosuIssue.GosuIssueBuilder(this)
                .withMessage(message)
                .onContext(ctx)
                .build()
        );
    }

    /**
     * Types that are always available and do not require import statements.
     */
    private enum AlwaysAvailableTypes {
        JAVA_LANG("java.lang.", "Java Classes"),
        GUIDEWIRE_ENTITIES("entity.", "Entity Classes"),
        GUIDEWIRE_TYPE_KEYS("typekey.", "Typekey Classes");

        private final String packagePrefix;
        private final String description;

        AlwaysAvailableTypes(String packagePrefix, String description) {
            this.packagePrefix = packagePrefix;
            this.description = description;
        }

        static void forEach(Consumer<AlwaysAvailableTypes> action) {
            Arrays.stream(AlwaysAvailableTypes.values()).forEach(action);
        }
    }
}
