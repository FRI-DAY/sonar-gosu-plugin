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
package de.friday.sonarqube.gosu.plugin.tools.reflections;

import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.measures.metrics.AbstractMetricBase;
import de.friday.sonarqube.gosu.plugin.utils.ChecksMetadataUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.check.Rule;

public final class ClassExtractor {
    private static final Map<String, Class<? extends AbstractCheckBase>> allChecks = new HashMap<>();
    private static final Map<String, Class<? extends AbstractCheckBase>> mainSourcesChecks = new HashMap<>();
    private static final Map<String, Class<? extends AbstractCheckBase>> testSourcesChecks = new HashMap<>();
    private static final List<Class<? extends AbstractMetricBase>> metrics;

    static {
        final Reflections reflections = new Reflections("de.friday.sonarqube.gosu.plugin.checks");
        final Set<Class<? extends AbstractCheckBase>> allClasses = reflections.getSubTypesOf(AbstractCheckBase.class);
        final Map<String, Class<? extends AbstractCheckBase>> keysToClasses = new HashMap<>();

        for (Class<? extends AbstractCheckBase> checkClass : allClasses) {
            final String key = checkClass.getAnnotation(Rule.class).key();
            keysToClasses.put(key, checkClass);
            addCheckByScope(checkClass, key);
        }
    }

    static {
        final Reflections reflections = new Reflections("de.friday.sonarqube.gosu.plugin.measures");
        final Set<Class<? extends AbstractMetricBase>> allClasses = reflections.getSubTypesOf(AbstractMetricBase.class);

        metrics = Collections.unmodifiableList(new ArrayList<>(allClasses));
    }

    private ClassExtractor() {
    }

    private static void addCheckByScope(Class<? extends AbstractCheckBase> clazz, String checkKey) {
        allChecks.put(checkKey, clazz);
        ChecksMetadataUtil.Scope scope = ChecksMetadataUtil.getCheckScope(checkKey);
        switch (scope) {
            case MAIN:
                mainSourcesChecks.put(checkKey, clazz);
                break;
            case TESTS:
                testSourcesChecks.put(checkKey, clazz);
                break;
            case ALL:
            default:
                mainSourcesChecks.put(checkKey, clazz);
                testSourcesChecks.put(checkKey, clazz);
        }
    }

    public static Map<String, Class<? extends AbstractCheckBase>> getChecks() {
        return Collections.unmodifiableMap(new HashMap<>(allChecks));
    }

    public static List<Class<? extends AbstractMetricBase>> getMetrics() {
        return metrics;
    }

    public static Optional<Class<? extends AbstractCheckBase>> getCheckForScope(String key, InputFile.Type type) {
        switch (type) {
            case MAIN:
                return Optional.ofNullable(mainSourcesChecks.get(key));
            case TEST:
                return Optional.ofNullable(testSourcesChecks.get(key));
            default:
                return Optional.ofNullable(allChecks.get(key));
        }
    }
}
