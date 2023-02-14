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

import org.junit.jupiter.api.Test;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import static org.assertj.core.api.Assertions.assertThat;
class GosuLangPropertiesTest {

    @Test
    void shouldReturnGosuPropertyDefinition() {
        // when
        final PropertyDefinition properties = GosuLangProperties.getProperties().get(0);

        assertThat(properties.key()).isEqualTo("sonar.gosu.file.suffixes");
        assertThat(properties.defaultValue()).isEqualTo(".gs,.gsx");
        assertThat(properties.name()).isEqualTo("File Suffixes");
        assertThat(properties.category()).isEqualTo("Gosu");
        assertThat(properties.description()).isEqualTo("Comma-separated list of suffixes for files to analyze.");
        assertThat(properties.qualifiers()).containsOnly(Qualifiers.PROJECT);
    }
}