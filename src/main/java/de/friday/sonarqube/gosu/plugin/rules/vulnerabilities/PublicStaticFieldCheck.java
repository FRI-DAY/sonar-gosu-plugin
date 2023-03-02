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
package de.friday.sonarqube.gosu.plugin.rules.vulnerabilities;

import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import org.sonar.check.Rule;

@Rule(key = PublicStaticFieldCheck.KEY)
public class PublicStaticFieldCheck extends BaseGosuRule {
    static final String KEY = "PublicStaticFieldCheck";

    @Override
    public void exitField(GosuParser.FieldContext ctx) {
        final GosuParser.ModifiersContext modifiers = ctx.modifiers();

        if (hasPublicAndStatic(modifiers) && modifiers.FINAL().isEmpty()) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .onContext(ctx.identifier(0))
                    .withMessage("\"public static\" fields should be constant")
                    .build()
            );
        }
    }

    private boolean hasPublicAndStatic(GosuParser.ModifiersContext modifiers) {
        return modifiers != null
                && !modifiers.STATIC().isEmpty()
                && !modifiers.PUBLIC().isEmpty();
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
