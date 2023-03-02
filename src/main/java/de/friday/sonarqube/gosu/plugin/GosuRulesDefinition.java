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
import java.util.ArrayList;
import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

public class GosuRulesDefinition implements RulesDefinition {
    private final SonarRuntime sonarRuntime;

    public GosuRulesDefinition(SonarRuntime sonarRuntime) {
        this.sonarRuntime = sonarRuntime;
    }

    @Override
    public void define(Context context) {
        final NewRepository repository = context
                .createRepository(GosuLanguage.REPOSITORY_KEY, GosuLanguage.KEY)
                .setName(GosuLanguage.REPOSITORY_NAME);

        final RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(
                GosuLanguage.GOSU_RESOURCE_PATH,
                GosuLanguage.DEFAULT_PROFILE_PATH,
                sonarRuntime
        );

        ruleMetadataLoader.addRulesByAnnotatedClass(repository, new ArrayList<>(ClassExtractor.getRules().values()));
        repository.rules().forEach(rule -> rule.setActivatedByDefault(true));
        repository.done();
    }
}
