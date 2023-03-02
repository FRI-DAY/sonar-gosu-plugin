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

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.sonar.check.Rule;

@Rule(key = PublicVariablesCheck.KEY)
public class PublicVariablesCheck extends BaseGosuRule {
    static final String KEY = "PublicVariablesCheck";

    private GosuFileProperties gosuFileProperties;

    @Inject
    PublicVariablesCheck(GosuFileProperties gosuFileProperties) {
        this.gosuFileProperties = gosuFileProperties;
    }

    @Override
    public void exitField(GosuParser.FieldContext ctx) {
        GosuParser.ModifiersContext modifiers = ctx.modifiers();
        if (hasIncorrectModifiers(modifiers)
                && ctx.AS() == null) {
            createIssue(ctx);
        }
    }

    private static boolean hasIncorrectModifiers(GosuParser.ModifiersContext modifiers) {
        return modifiers != null
                && modifiers.STATIC().isEmpty()
                && modifiers.FINAL().isEmpty()
                && !modifiers.PUBLIC().isEmpty();
    }

    private void createIssue(GosuParser.FieldContext ctx) {
        List<GosuParser.AnnotationContext> annotations = ctx.modifiers().annotation();
        if (annotations.isEmpty()) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Use public properties instead of public variables")
                    .onContext(ctx)
                    .build());
        } else {
            Token startToken = getFirstTokenToHighlight(annotations);
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage("Use public properties instead of public variables")
                    .onTokenRange(startToken, ctx.getStop())
                    .build());
        }
    }

    private Token getFirstTokenToHighlight(List<GosuParser.AnnotationContext> annotations) {
        int lastAnnotationIndex = annotations.size() - 1;
        Token annotationStopToken = annotations.get(lastAnnotationIndex).getStop();
        int firstTokenToHighlightIndex = annotationStopToken.getTokenIndex() + 1;
        return gosuFileProperties.getToken(firstTokenToHighlightIndex);
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
