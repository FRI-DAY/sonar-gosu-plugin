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
package de.friday.sonarqube.gosu;

import de.friday.test.support.SonarServerVersionSupported;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GosuPluginTest {

    @ParameterizedTest
    @EnumSource(SonarServerVersionSupported.class)
    void shouldAddGosuExtensionsToSonarServer(SonarServerVersionSupported sonarServerVersion) {
        // given
        final SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(sonarServerVersion.getVersion(), SonarQubeSide.SERVER);
        final Plugin.Context context = new Plugin.Context(runtime);

        // when
        assertThatCode(() -> new GosuPlugin().define(context)).doesNotThrowAnyException();

        // then
        assertThat(context.getExtensions()).hasSize(6);
    }
}
