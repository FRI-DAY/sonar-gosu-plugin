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
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;

public class GosuQualityProfile extends ProfileDefinition {
    private static final String PROFILE = "Sonar way";
    private final RulesProfile rulesProfile;

    public GosuQualityProfile() {
        rulesProfile = RulesProfile.create(PROFILE, GosuLanguage.KEY);
    }

    @Override
    public RulesProfile createProfile(ValidationMessages validationMessages) {
        RulesKeysExtractor.getAllRulesKeys().forEach(this::activateRule);
        rulesProfile.setDefaultProfile(true);

        activateDefaultRules();

        return rulesProfile;
    }

    private void activateRule(String checkKey) {
        final Optional<Rule> rule = Optional.ofNullable(Rule.create(GosuLanguage.REPOSITORY_KEY, checkKey));
        rule.ifPresent(value -> rulesProfile.activateRule(value, null));
    }

    private void activateDefaultRules() {
        final Rule duplicatedBlocksRule = Rule.create("common-gosu", "DuplicatedBlocks");
        rulesProfile.activateRule(duplicatedBlocksRule, null);
    }
}
