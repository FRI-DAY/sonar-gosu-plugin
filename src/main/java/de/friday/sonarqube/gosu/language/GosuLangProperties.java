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
package de.friday.sonarqube.gosu.language;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.Collections;
import java.util.List;

public final class GosuLangProperties {
    public static final String FILE_SUFFIXES_KEY = "sonar.gosu.file.suffixes";
    public static final String FILE_SUFFIXES_DEFAULT_VALUE = ".gs,.gsx";

    private GosuLangProperties() {
    }

    public static List<PropertyDefinition> getProperties() {
        return Collections.singletonList(
                PropertyDefinition.builder(FILE_SUFFIXES_KEY)
                        .defaultValue(FILE_SUFFIXES_DEFAULT_VALUE)
                        .multiValues(true)
                        .category("Gosu")
                        .name("File Suffixes")
                        .description("Comma-separated list of suffixes for files to analyze.")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build()
        );
    }
}
