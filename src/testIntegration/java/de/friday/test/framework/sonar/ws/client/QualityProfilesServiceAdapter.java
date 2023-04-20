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

import java.util.List;
import org.sonarqube.ws.Qualityprofiles;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.qualityprofiles.QualityprofilesService;
import org.sonarqube.ws.client.qualityprofiles.SearchRequest;

public class QualityProfilesServiceAdapter extends QualityprofilesService {
    public QualityProfilesServiceAdapter(HttpConnector connector) {
        super(connector);
    }

    public List<Qualityprofiles.SearchWsResponse.QualityProfile> findAllProfilesOf(String language) {
        return this.search(new SearchRequest().setLanguage(language)).getProfilesList();
    }

}
