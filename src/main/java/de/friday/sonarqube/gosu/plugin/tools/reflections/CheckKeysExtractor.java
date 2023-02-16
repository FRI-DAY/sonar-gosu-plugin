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
import de.friday.sonarqube.gosu.plugin.utils.annotations.UnitTestMissing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.sonar.check.Rule;

@UnitTestMissing
public final class CheckKeysExtractor {
    private static final List<String> metricsKeys;
    private static final List<String> codeSmellsKeys;
    private static final List<String> bugsKeys;
    private static final List<String> vulnerabilitiesKeys;
    private static final Map<String, String> checksToPackages;

    private static final String BASE_CHECKS_PACKAGE = "de.friday.sonarqube.gosu.plugin.checks";
    private static final String METRICS_PACKAGE = BASE_CHECKS_PACKAGE + ".metrics";
    private static final String CODE_SMELLS_PACKAGE = BASE_CHECKS_PACKAGE + ".smells";
    private static final String VULNERABILITIES_PACKAGE = BASE_CHECKS_PACKAGE + ".vulnerabilities";
    private static final String BUGS_PACKAGE = BASE_CHECKS_PACKAGE + ".bugs";
    private static final String METRICS = "metrics";
    private static final String CODE_SMELLS = "smells";
    private static final String VULNERABILITIES = "vulnerabilities";
    private static final String BUGS = "bugs";

    static {
        metricsKeys = getKeys(METRICS_PACKAGE);
        codeSmellsKeys = getKeys(CODE_SMELLS_PACKAGE);
        bugsKeys = getKeys(BUGS_PACKAGE);
        vulnerabilitiesKeys = getKeys(VULNERABILITIES_PACKAGE);

        final Map<String, String> checksToPackagesMap = new HashMap<>();
        for (String check : metricsKeys) {
            checksToPackagesMap.put(check, METRICS);
        }
        for (String check : codeSmellsKeys) {
            checksToPackagesMap.put(check, CODE_SMELLS);
        }
        for (String check : bugsKeys) {
            checksToPackagesMap.put(check, BUGS);
        }
        for (String check : vulnerabilitiesKeys) {
            checksToPackagesMap.put(check, VULNERABILITIES);
        }

        checksToPackages = Collections.unmodifiableMap(checksToPackagesMap);
    }

    private CheckKeysExtractor() {

    }

    private static List<String> getKeys(String packageName) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<? extends AbstractCheckBase>> metricsClasses = reflections.getSubTypesOf(AbstractCheckBase.class);
        final List<String> keys = new ArrayList<>();

        for (Class<? extends AbstractCheckBase> clazz : metricsClasses) {
            keys.add(clazz.getAnnotation(Rule.class).key());
        }

        return Collections.unmodifiableList(keys);
    }

    public static String getCheckPackage(String check) {
        return checksToPackages.get(check);
    }

    public static Set<String> getAllKeys() {
        return checksToPackages.keySet();
    }
}
