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

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import java.util.Arrays;

public class GosuLanguage extends AbstractLanguage {

    /**
     * The gosu language key.
     */
    public static final String KEY = "gosu";
    /**
     * The gosu language name
     */
    private static final String NAME = "Gosu";
    public static final String REPOSITORY_KEY = "gosu";
    public static final String REPOSITORY_NAME = "SonarAnalyzer";
    public static final String GOSU_RESOURCE_PATH = "sonar";
    public static final String DEFAULT_PROFILE_PATH = "sonar/sonar_way_profile.json";
    private final Configuration config;

    public GosuLanguage(Configuration configuration) {
        super(KEY, NAME);
        config = configuration;
    }

    public String[] getFileSuffixes() {
        final String[] suffixes = filterEmptyStrings(config.getStringArray(GosuLangProperties.FILE_SUFFIXES_KEY));
        if (isEmpty(suffixes)) return StringUtils.split(GosuLangProperties.FILE_SUFFIXES_DEFAULT_VALUE, ",");
        return suffixes;
    }

    private boolean isEmpty(String[] suffixes) {
        return suffixes.length == 0;
    }

    private String[] filterEmptyStrings(String[] strings) {
        return Arrays.stream(strings).filter(StringUtils::isNotBlank).toArray(String[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GosuLanguage that = (GosuLanguage) o;
        return Objects.equals(config, that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), config);
    }
}

