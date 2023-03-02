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

import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.measures.metrics.BaseMetric;
import de.friday.sonarqube.gosu.plugin.utils.RulesMetadataUtil;
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
    private static final Map<String, Class<? extends BaseGosuRule>> allRules = new HashMap<>();
    private static final Map<String, Class<? extends BaseGosuRule>> mainSourcesRules = new HashMap<>();
    private static final Map<String, Class<? extends BaseGosuRule>> testSourcesRules = new HashMap<>();
    private static final List<Class<? extends BaseMetric>> metrics;

    static {
        final Reflections reflections = new Reflections("de.friday.sonarqube.gosu.plugin.rules");
        final Set<Class<? extends BaseGosuRule>> allClasses = reflections.getSubTypesOf(BaseGosuRule.class);
        final Map<String, Class<? extends BaseGosuRule>> keysToClasses = new HashMap<>();

        for (Class<? extends BaseGosuRule> ruleClass : allClasses) {
            final String key = ruleClass.getAnnotation(Rule.class).key();
            keysToClasses.put(key, ruleClass);
            addRuleByScope(ruleClass, key);
        }
    }

    static {
        final Reflections reflections = new Reflections("de.friday.sonarqube.gosu.plugin.measures");
        final Set<Class<? extends BaseMetric>> allClasses = reflections.getSubTypesOf(BaseMetric.class);

        metrics = Collections.unmodifiableList(new ArrayList<>(allClasses));
    }

    private ClassExtractor() {
    }

    private static void addRuleByScope(Class<? extends BaseGosuRule> clazz, String ruleKey) {
        allRules.put(ruleKey, clazz);
        RulesMetadataUtil.Scope scope = RulesMetadataUtil.getRuleScope(ruleKey);
        switch (scope) {
            case MAIN:
                mainSourcesRules.put(ruleKey, clazz);
                break;
            case TESTS:
                testSourcesRules.put(ruleKey, clazz);
                break;
            case ALL:
            default:
                mainSourcesRules.put(ruleKey, clazz);
                testSourcesRules.put(ruleKey, clazz);
        }
    }

    public static Map<String, Class<? extends BaseGosuRule>> getRules() {
        return Collections.unmodifiableMap(new HashMap<>(allRules));
    }

    public static List<Class<? extends BaseMetric>> getMetrics() {
        return metrics;
    }

    public static Optional<Class<? extends BaseGosuRule>> getRuleForScope(String key, InputFile.Type type) {
        switch (type) {
            case MAIN:
                return Optional.ofNullable(mainSourcesRules.get(key));
            case TEST:
                return Optional.ofNullable(testSourcesRules.get(key));
            default:
                return Optional.ofNullable(allRules.get(key));
        }
    }
}
