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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.api.config.internal.MapSettings;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GosuLanguageTest {
    private MapSettings settings;
    // SUT
    private GosuLanguage gosuLanguage;

    @BeforeEach
    void setUp() {
        settings = new MapSettings();
        gosuLanguage = new GosuLanguage(settings.asConfig());
    }

    @Test
    void shouldReturnDefaultFileSuffixes() {
        // given
        settings.setProperty(GosuLangProperties.FILE_SUFFIXES_KEY, "");

        // when
        final List<String> suffixes = Arrays.asList(gosuLanguage.getFileSuffixes());

        // then
        assertThat(suffixes).containsExactlyInAnyOrder(".gs", ".gsx");
    }

    @Test
    void shouldReturnCustomFileSuffixes() {
        // given
        settings.setProperty(GosuLangProperties.FILE_SUFFIXES_KEY, ".gs, ,.gsx, .etx, ,.eti");

        // when
        final List<String> suffixes = Arrays.asList(gosuLanguage.getFileSuffixes());

        // then
        assertThat(suffixes)
                .doesNotContain(" ")
                .containsExactlyInAnyOrder(".gs", ".gsx", ".etx", ".eti");
    }
}