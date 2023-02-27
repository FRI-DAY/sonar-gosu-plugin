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

import de.friday.sonarqube.gosu.language.GosuLangProperties;
import de.friday.sonarqube.gosu.language.GosuLanguage;
import de.friday.sonarqube.gosu.plugin.GosuQualityProfile;
import de.friday.sonarqube.gosu.plugin.GosuRulesDefinition;
import de.friday.sonarqube.gosu.plugin.GosuSensor;
import org.sonar.api.Plugin;


public class GosuPlugin implements Plugin {

    public void define(Context context) {
        context.addExtensions(
                GosuLanguage.class,
                GosuLangProperties.class,
                GosuSensor.class,
                GosuRulesDefinition.class,
                GosuLangProperties.getProperties(),
                GosuQualityProfile.class
        );
    }
}
