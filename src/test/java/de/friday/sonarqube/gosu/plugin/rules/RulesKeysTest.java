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

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RulesKeysTest {

    @Test
    void shouldReturnAllRuleKeysByCheckType() {
        // when
        final Map<String, RuleType> allRuleKeys = new RulesKeys().getRuleKeysByRuleType();

        // then
        assertThat(allRuleKeys).isNotEmpty().containsExactlyInAnyOrderEntriesOf(expectedRuleKeysByCheckType());
    }

    private Map<String, RuleType> expectedRuleKeysByCheckType() {
        final Map<String, RuleType> expectedChecks = new HashMap<>();

        expectedChecks.putAll(bugs());
        expectedChecks.putAll(metrics());
        expectedChecks.putAll(codeSmells());
        expectedChecks.putAll(vulnerabilities());

        return expectedChecks;
    }

    private Map<String, RuleType> bugs() {
        final Map<String, RuleType> bugChecks = new HashMap<>();

        bugChecks.put("SameConditionsInIfRule", RuleType.BUGS);
        bugChecks.put("StringBuilderInstantiationRule", RuleType.BUGS);
        bugChecks.put("SystemClockUnawareDateRule", RuleType.BUGS);

        return bugChecks;
    }

    private Map<String, RuleType> metrics() {
        final Map<String, RuleType> metrics = new HashMap<>();

        metrics.put("CognitiveComplexityRule", RuleType.METRICS);
        metrics.put("CyclomaticComplexityRule", RuleType.METRICS);
        metrics.put("LinesOfCodeRule", RuleType.METRICS);

        return metrics;
    }

    private Map<String, RuleType> codeSmells() {
        final Map<String, RuleType> codeSmells = new HashMap<>();

        codeSmells.put("AutomaticDowncastCheck", RuleType.CODE_SMELLS);
        codeSmells.put("CollectionIsEmptyCheck", RuleType.CODE_SMELLS);
        codeSmells.put("DefaultModifiersCheck", RuleType.CODE_SMELLS);
        codeSmells.put("EmptyLineCheck", RuleType.CODE_SMELLS);
        codeSmells.put("HardcodedEntityFieldValueCheck", RuleType.CODE_SMELLS);
        codeSmells.put("IfElseIfCheck", RuleType.CODE_SMELLS);
        codeSmells.put("InternalImportsCheck", RuleType.CODE_SMELLS);
        codeSmells.put("InvertedBooleanExpressionsCheck", RuleType.CODE_SMELLS);
        codeSmells.put("LoggerCheck", RuleType.CODE_SMELLS);
        codeSmells.put("LoggerLibraryCheck", RuleType.CODE_SMELLS);
        codeSmells.put("MagicNumbersCheck", RuleType.CODE_SMELLS);
        codeSmells.put("NestedStatementsCheck", RuleType.CODE_SMELLS);
        codeSmells.put("PublicVariablesCheck", RuleType.CODE_SMELLS);
        codeSmells.put("RethrowInCatchCheck", RuleType.CODE_SMELLS);
        codeSmells.put("ReturnNullCollectionCheck", RuleType.CODE_SMELLS);
        codeSmells.put("TODOsCheck", RuleType.CODE_SMELLS);
        codeSmells.put("TooManyParamsCheck", RuleType.CODE_SMELLS);
        codeSmells.put("UnnecessaryImportCheck", RuleType.CODE_SMELLS);
        codeSmells.put("UnusedParameterCheck", RuleType.CODE_SMELLS);

        return codeSmells;
    }

    private Map<String, RuleType> vulnerabilities() {
        final Map<String, RuleType> vulnerabilities = new HashMap<>();

        vulnerabilities.put("EmptyCatchRule", RuleType.VULNERABILITIES);
        vulnerabilities.put("PublicStaticFieldCheck", RuleType.VULNERABILITIES);

        return vulnerabilities;
    }

}
