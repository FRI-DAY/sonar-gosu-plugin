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
package de.friday.sonarqube.gosu.plugin;

import de.friday.sonarqube.gosu.language.GosuLanguage;
import de.friday.sonarqube.gosu.plugin.tools.reflections.RulesKeysExtractor;
import java.util.Optional;
import org.sonar.api.rules.Rule;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public class GosuQualityProfile implements BuiltInQualityProfilesDefinition {
    private static final String BUILTIN_GOSU_PROFILE_NAME = "Sonar way";

    @Override
    public void define(Context context) {
        final NewBuiltInQualityProfile builtInQualityProfile = context.createBuiltInQualityProfile(
                BUILTIN_GOSU_PROFILE_NAME,
                GosuLanguage.KEY
        );
        activateRules(builtInQualityProfile);
        builtInQualityProfile.setDefault(true);
        builtInQualityProfile.done();
    }

    public void activateRules(NewBuiltInQualityProfile builtInQualityProfile) {
        RulesKeysExtractor.getAllRulesKeys().forEach(ruleKey -> activateRule(ruleKey, builtInQualityProfile));
    }

    private void activateRule(String ruleKey, NewBuiltInQualityProfile builtInQualityProfile) {
        final Optional<Rule> rule = Optional.ofNullable(Rule.create(GosuLanguage.REPOSITORY_KEY, ruleKey));
        rule.ifPresent(value -> builtInQualityProfile.activateRule(value.getRepositoryKey(), value.getKey()));
    }
}
