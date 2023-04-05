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

import de.friday.test.config.IntegrationTest;
import de.friday.test.framework.sonar.server.SonarServer;
import de.friday.test.framework.sonar.ws.client.SonarWebServicesClient;
import org.junit.jupiter.api.Test;
import org.sonarqube.ws.Plugins;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class PluginInstallationIT {

    private final SonarWebServicesClient sonarClient;

    PluginInstallationIT(SonarServer sonarServer) {
        this.sonarClient = sonarServer.getClient();
    }

    @Test
    void shouldBeAvailableOnSonar() {
        //when
        final Plugins.InstalledPluginsWsResponse installedPlugins = sonarClient.plugins().allInstalledPlugins();

        //then
        assertThat(installedPlugins.getPluginsList()).satisfiesOnlyOnce(
                pluginDetails -> {
                    assertThat(pluginDetails.getKey()).isEqualTo("communityGosuPlugin");
                    assertThat(pluginDetails.getName()).isEqualTo("Community Gosu Plugin");
                    assertThat(pluginDetails.getDescription()).isEqualTo("Gosu Programming Language Plugin for SonarQube");
                    assertThat(pluginDetails.getLicense()).isEqualTo("GNU AGPL 3");
                    assertThat(pluginDetails.getOrganizationName()).isEqualTo("FRIDAY Insurance S.A.");
                }
        );
    }
}
