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
import org.junit.jupiter.api.Test;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.Context;
import static org.assertj.core.api.Assertions.assertThat;

class GosuQualityProfileTest {

    @Test
    void shouldReturnRulesProfileWithGosuChecks() {
        // given
        final Context context = new Context();
        final GosuQualityProfile gosuQualityProfile = new GosuQualityProfile();

        // when
        gosuQualityProfile.define(context);

        // then
        BuiltInQualityProfilesDefinition.BuiltInQualityProfile rulesProfile = context.profile(GosuLanguage.KEY, "Sonar way");
        assertThat(rulesProfile.name()).isEqualTo("Sonar way");
        assertThat(rulesProfile.language()).isEqualTo(GosuLanguage.KEY);
        assertThat(rulesProfile.isDefault()).isTrue();
        assertThat(rulesProfile.rules())
                .filteredOn(activeRule -> activeRule.repoKey().equals(GosuLanguage.REPOSITORY_KEY))
                .hasSize(expectedNumberOfActiveGosuRules());
        assertThat(rulesProfile.rules())
                .filteredOn(activeRule -> activeRule.repoKey().equals(GosuRulesDefinition.COMMON_REPOSITORY_KEY))
                .hasSize(1);
    }

    private int expectedNumberOfActiveGosuRules() {
        return RulesKeysExtractor.getAllRulesKeys().size();
    }
}
