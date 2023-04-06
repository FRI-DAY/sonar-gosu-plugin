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
package de.friday.test.framework.sonar.server;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.locator.FileLocation;
import de.friday.test.framework.sonar.ws.client.SonarWebServicesClient;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SonarServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SonarServer.class);

    private final Orchestrator orchestrator;

    public SonarServer(SonarServerConfig config) {
        logServerConfig(config);
        this.orchestrator = Orchestrator.builderEnv()
                .addPlugin(FileLocation.of(config.getPluginJarFile()))
                .keepBundledPlugins()
                .useDefaultAdminCredentialsForBuilds(true)
                .setSonarVersion(config.getSonarVersion().asLatestRelease())
                .build();
    }

    private void logServerConfig(SonarServerConfig config) {
        LOGGER.info(line());
        LOGGER.info("Sonar Server configured with: ");
        LOGGER.info("Sonar Version  => {}", config.getSonarVersion().asLatestRelease());
        LOGGER.info("Plugin Version => {}", config.getPluginVersion());
        LOGGER.info(line());
    }

    @NotNull
    private String line() {
        return StringUtils.repeat("-", 50);
    }

    public void start() {
        orchestrator.start();
    }

    public void stop() {
        orchestrator.stop();
    }

    public String getUrl() {
        return orchestrator.getServer().getUrl();
    }

    public SonarWebServicesClient getClient() {
        return new SonarWebServicesClient(getUrl());
    }
}
