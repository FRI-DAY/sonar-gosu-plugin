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

import java.util.Collections;
import java.util.List;
import org.sonarqube.ws.Issues;
import org.sonarqube.ws.client.WsConnector;
import org.sonarqube.ws.client.issues.IssuesService;
import org.sonarqube.ws.client.issues.SearchRequest;

public class IssuesServiceAdapter extends IssuesService {

    public IssuesServiceAdapter(WsConnector wsConnector) {
        super(wsConnector);
    }

    public List<Issues.Issue> find(String projectKey) {
        return this.search(
                new SearchRequest().setProjects(Collections.singletonList(projectKey))
        ).getIssuesList();
    }

    public List<Issues.Issue> findIssuesOnComponent(String componentKey) {
        return this.search(
                new SearchRequest().setComponentKeys(Collections.singletonList(componentKey))
        ).getIssuesList();
    }
}
