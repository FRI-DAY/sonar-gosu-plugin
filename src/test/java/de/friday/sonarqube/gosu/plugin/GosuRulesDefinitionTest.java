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
import de.friday.sonarqube.gosu.plugin.tools.reflections.ClassExtractor;
import de.friday.test.support.SonarServerVersionSupported;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.server.rule.RulesDefinition;
import static org.assertj.core.api.Assertions.assertThat;

class GosuRulesDefinitionTest {

    @ParameterizedTest
    @EnumSource(SonarServerVersionSupported.class)
    void shouldLoadMetadataDefinitionForAllChecks(SonarServerVersionSupported sonarServerVersion) {
        // given
        final SonarRuntime sonarRuntime = SonarRuntimeImpl.forSonarQube(sonarServerVersion.getVersion(), SonarQubeSide.SERVER);
        final RulesDefinition.Context context = new RulesDefinition.Context();

        // when
        new GosuRulesDefinition(sonarRuntime).define(context);

        // then
        final RulesDefinition.Repository repository = context.repository(GosuLanguage.KEY);
        assertThat(repository.name()).isNotNull().isEqualTo("SonarAnalyzer");
        assertThat(repository.key()).isEqualTo(GosuLanguage.REPOSITORY_KEY);
        assertThat(repository.language()).isEqualTo(GosuLanguage.KEY);
        assertThat(repository.rules()).hasSize(ClassExtractor.getChecks().size()).allSatisfy(
                rule -> {
                    assertThat(rule.activatedByDefault()).isTrue();
                    assertThat(rule.name()).isNotBlank();
                    assertThat(rule.status()).isEqualTo(RuleStatus.READY);
                    assertThat(rule.htmlDescription()).isNotBlank();
                }
        );
    }
}
