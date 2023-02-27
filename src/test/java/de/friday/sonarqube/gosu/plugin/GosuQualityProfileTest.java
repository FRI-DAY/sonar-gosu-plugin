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
import de.friday.sonarqube.gosu.plugin.tools.reflections.CheckKeysExtractor;
import org.junit.jupiter.api.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import static org.assertj.core.api.Assertions.assertThat;

class GosuQualityProfileTest {

    @Test
    void shouldReturnRulesProfileWithGosuChecks() {
        // given
        final GosuQualityProfile gosuQualityProfile = new GosuQualityProfile();

        // when
        final RulesProfile rulesProfile = gosuQualityProfile.createProfile(ValidationMessages.create());

        // then
        assertThat(rulesProfile.getName()).isEqualTo("Sonar way");
        assertThat(rulesProfile.getLanguage()).isEqualTo(GosuLanguage.KEY);
        assertThat(rulesProfile.getDefaultProfile()).isTrue();
        assertThat(rulesProfile.getActiveRules()).hasSize(expectedNumberOfActiveRules());
    }

    private int expectedNumberOfActiveRules() {
        final int defaultRulesCount = 1;
        final int numberOfGosuChecks = CheckKeysExtractor.getAllCheckKeys().size();
        return defaultRulesCount + numberOfGosuChecks;
    }
}
