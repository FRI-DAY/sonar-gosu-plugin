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
package de.friday.test.support;

import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.rule.RuleKey;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class GosuSensorContextTester {
    private final SensorContextTester sensorContextTester;

    public GosuSensorContextTester(Path moduleBaseDir) {
        this(moduleBaseDir, null, Collections.emptyMap());
    }

    public GosuSensorContextTester(Path moduleBaseDir, String ruleKey, Map<String, String> ruleProperties) {
        this.sensorContextTester = create(moduleBaseDir, Optional.ofNullable(ruleKey), ruleProperties);
    }

    private SensorContextTester create(Path moduleBaseDir, Optional<String> ruleKey, Map<String, String> ruleProperties) {
        final SensorContextTester sensorContextTester = SensorContextTester.create(moduleBaseDir);
        ruleKey.ifPresent(key -> activateRules(key, sensorContextTester, ruleProperties));
        return sensorContextTester;
    }

    private void activateRules(String ruleKey, SensorContextTester sensorContextTester, Map<String, String> ruleProperties) {
        NewActiveRule newActiveRule = new ActiveRulesBuilder().create(
                RuleKey.of("gosu", ruleKey)
        );
        ruleProperties.forEach(newActiveRule::setParam);
        final ActiveRulesBuilder activeRulesBuilder = newActiveRule.activate();
        final ActiveRules activeRules = activeRulesBuilder.build();
        sensorContextTester.setActiveRules(activeRules);
    }

    public SensorContextTester get() {
        return sensorContextTester;
    }
}
