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

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ChecksRuleKeysTest {

    @Test
    void shouldReturnAllRuleKeysByCheckType() {
        // when
        final Map<String, CheckType> allRuleKeys = new ChecksRuleKeys().getRuleKeysByCheckType();

        // then
        assertThat(allRuleKeys).isNotEmpty().containsExactlyInAnyOrderEntriesOf(expectedRuleKeysByCheckType());
    }

    private Map<String, CheckType> expectedRuleKeysByCheckType() {
        final Map<String, CheckType> expectedChecks = new HashMap<>();

        expectedChecks.putAll(bugs());
        expectedChecks.putAll(metrics());
        expectedChecks.putAll(codeSmells());
        expectedChecks.putAll(vulnerabilities());

        return expectedChecks;
    }

    private Map<String, CheckType> bugs() {
        final Map<String, CheckType> bugChecks = new HashMap<>();

        bugChecks.put("SameConditionsInIfCheck", CheckType.BUGS);
        bugChecks.put("StringBuilderInstantiationCheck", CheckType.BUGS);
        bugChecks.put("SystemClockUnawareDateCheck", CheckType.BUGS);

        return bugChecks;
    }

    private Map<String, CheckType> metrics() {
        final Map<String, CheckType> metrics = new HashMap<>();

        metrics.put("CognitiveComplexityCheck", CheckType.METRICS);
        metrics.put("CyclomaticComplexityCheck", CheckType.METRICS);
        metrics.put("LinesOfCodeCheck", CheckType.METRICS);

        return metrics;
    }

    private Map<String, CheckType> codeSmells() {
        final Map<String, CheckType> codeSmells = new HashMap<>();

        codeSmells.put("TODOsCheck", CheckType.CODE_SMELLS);
        codeSmells.put("AutomaticDowncastCheck", CheckType.CODE_SMELLS);
        codeSmells.put("CollectionIsEmptyCheck", CheckType.CODE_SMELLS);
        codeSmells.put("DefaultModifiersCheck", CheckType.CODE_SMELLS);
        codeSmells.put("EmptyLineCheck", CheckType.CODE_SMELLS);
        codeSmells.put("HardcodedEntityFieldValueCheck", CheckType.CODE_SMELLS);
        codeSmells.put("IfElseIfCheck", CheckType.CODE_SMELLS);
        codeSmells.put("InternalImportsCheck", CheckType.CODE_SMELLS);
        codeSmells.put("InvertedBooleanExpressionsCheck", CheckType.CODE_SMELLS);
        codeSmells.put("LoggerCheck", CheckType.CODE_SMELLS);

        return codeSmells;
    }

    private Map<String, CheckType> vulnerabilities() {
        final Map<String, CheckType> vulnerabilities = new HashMap<>();

        vulnerabilities.put("EmptyCatchCheck", CheckType.VULNERABILITIES);
        vulnerabilities.put("PublicStaticFieldCheck", CheckType.VULNERABILITIES);

        return vulnerabilities;
    }

}
