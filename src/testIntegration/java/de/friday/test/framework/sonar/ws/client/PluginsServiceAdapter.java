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

import org.sonarqube.ws.Plugins;
import org.sonarqube.ws.client.WsConnector;
import org.sonarqube.ws.client.plugins.InstalledRequest;
import org.sonarqube.ws.client.plugins.PluginsService;

public class PluginsServiceAdapter extends PluginsService {

    public PluginsServiceAdapter(WsConnector wsConnector) {
        super(wsConnector);
    }

    public Plugins.InstalledPluginsWsResponse allInstalledPlugins() {
        return installedPlugins(new InstalledRequest());
    }

    public Plugins.InstalledPluginsWsResponse installedPlugins(InstalledRequest request) {
        return JsonToProtobufParser.from(this.installed(request))
                .toMessage(Plugins.InstalledPluginsWsResponse.newBuilder());
    }

}
