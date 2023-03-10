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

import de.friday.sonarqube.gosu.plugin.rules.RuleType;
import de.friday.sonarqube.gosu.plugin.rules.RulesKeys;
import java.util.Map;
import java.util.Set;

public final class RulesKeysExtractor {
    private static final Map<String, RuleType> rulesKeysToRuleType;

    static {
        rulesKeysToRuleType = new RulesKeys().getRuleKeysByRuleType();
    }

    private RulesKeysExtractor() {
    }

    public static String getRulePackage(String ruleKey) {
        return rulesKeysToRuleType.get(ruleKey).getPackageSuffix();
    }

    public static Set<String> getAllRulesKeys() {
        return rulesKeysToRuleType.keySet();
    }
}
