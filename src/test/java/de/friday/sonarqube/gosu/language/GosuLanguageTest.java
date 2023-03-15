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

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;

import java.util.Arrays;
import java.util.List;
import org.sonar.api.resources.AbstractLanguage;

import static de.friday.sonarqube.gosu.language.GosuLangProperties.FILE_SUFFIXES_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        settings.setProperty(FILE_SUFFIXES_KEY, "");

        // when
        final List<String> suffixes = Arrays.asList(gosuLanguage.getFileSuffixes());

        // then
        assertThat(suffixes).containsExactlyInAnyOrder(".gs", ".gsx");
    }

    @Test
    void shouldReturnCustomFileSuffixes() {
        // given
        settings.setProperty(FILE_SUFFIXES_KEY, ".gs, ,.gsx, .etx, ,.eti");

        // when
        final List<String> suffixes = Arrays.asList(gosuLanguage.getFileSuffixes());

        // then
        assertThat(suffixes)
                .doesNotContain(" ")
                .containsExactlyInAnyOrder(".gs", ".gsx", ".etx", ".eti");
    }

    @Test
    void shouldReturnTrueWhenInstancesAreEqual() {
        //given
        final GosuLanguage gosuLanguage1 = new GosuLanguage(settings.asConfig());
        final GosuLanguage gosuLanguage2 = new GosuLanguage(settings.asConfig());

        assertThat(gosuLanguage1)
                .isEqualTo(gosuLanguage2)
                .hasSameHashCodeAs(gosuLanguage2);
    }

    @ParameterizedTest
    @MethodSource("getDifferentGosuLanguages")
    void shouldReturnFalseWhenInstancesAreNotEqual(GosuLanguage gosuLanguage1, GosuLanguage gosuLanguage2) {
        assertThat(gosuLanguage1).isNotEqualTo(gosuLanguage2);
    }

    private Stream<Arguments> getDifferentGosuLanguages () {
        return Stream.of(
                of(new GosuLanguage(new MapSettings().asConfig()), new GosuLanguage(aConfigWith(FILE_SUFFIXES_KEY, ".gs"))),
                of(new GosuLanguage(new MapSettings().asConfig()), new RandomLanguage()),
                of(new GosuLanguage(new MapSettings().asConfig()), null)
        );
    }

    private Configuration aConfigWith(String key, String value) {
        final MapSettings settings = new MapSettings();
        settings.setProperty(key, value);
        return settings.asConfig();
    }

    private static class RandomLanguage extends GosuLanguage {

        public RandomLanguage() {
            super(new MapSettings().asConfig());
        }

        @Override
        public String[] getFileSuffixes() {
            return new String[]{".random"};
        }
    }

}
