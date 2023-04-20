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
package de.friday.test.framework.sonar.ws.client;

import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.projects.ProjectsService;

public class SonarWebServicesClient {

    private final String serverUrl;
    private final HttpConnector connector;

    public SonarWebServicesClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.connector = HttpConnector.newBuilder()
                .url(serverUrl)
                .credentials("admin", "admin")
                .build();
    }

    public PluginsServiceAdapter plugins() {
        return new PluginsServiceAdapter(connector);
    }

    public IssuesServiceAdapter issues() {
        return new IssuesServiceAdapter(connector);
    }

    public ProjectsService projects() {
        return new ProjectsService(connector);
    }

    public QualityProfilesServiceAdapter qualityProfiles() {
        return new QualityProfilesServiceAdapter(connector);
    }
}
