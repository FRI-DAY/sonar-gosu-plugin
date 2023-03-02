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
package de.friday.sonarqube.gosu.plugin.rules;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RulesKeys {

    public Map<String, RuleType> getRuleKeysByRuleType() {
        final Map<String, RuleType> ruleKeysByRuleType = new HashMap<>();

        Arrays.stream(RuleType.values()).forEach(ruleType -> addRulesKeysTo(ruleKeysByRuleType, ruleType));

        return Collections.unmodifiableMap(ruleKeysByRuleType);
    }

    private void addRulesKeysTo(Map<String, RuleType> ruleKeysByRuleType, RuleType ruleType) {
        ruleType.getRuleKeys().forEach(
                ruleKey -> ruleKeysByRuleType.put(ruleKey, ruleType)
        );
    }
}

