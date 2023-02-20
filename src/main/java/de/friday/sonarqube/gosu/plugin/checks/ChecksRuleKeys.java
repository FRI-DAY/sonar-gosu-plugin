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
package de.friday.sonarqube.gosu.plugin.checks;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChecksRuleKeys {

    public Map<String, CheckType> getRuleKeysByCheckType() {
        final Map<String, CheckType> ruleKeysByCheckType = new HashMap<>();

        Arrays.stream(CheckType.values()).forEach(checkType -> addRulesKeysTo(ruleKeysByCheckType, checkType));

        return Collections.unmodifiableMap(ruleKeysByCheckType);
    }

    private void addRulesKeysTo(Map<String, CheckType> ruleKeysByCheckType, CheckType checkType) {
        checkType.getRuleKeys().forEach(
                ruleKey -> ruleKeysByCheckType.put(ruleKey, checkType)
        );
    }
}

