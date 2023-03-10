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

        codeSmells.put("AutomaticDowncastRule", RuleType.CODE_SMELLS);
        codeSmells.put("CollectionIsEmptyRule", RuleType.CODE_SMELLS);
        codeSmells.put("DefaultModifiersRule", RuleType.CODE_SMELLS);
        codeSmells.put("EmptyLineRule", RuleType.CODE_SMELLS);
        codeSmells.put("HardcodedEntityFieldValueRule", RuleType.CODE_SMELLS);
        codeSmells.put("IfElseIfRule", RuleType.CODE_SMELLS);
        codeSmells.put("InternalImportsRule", RuleType.CODE_SMELLS);
        codeSmells.put("InvertedBooleanExpressionsRule", RuleType.CODE_SMELLS);
        codeSmells.put("LoggerRule", RuleType.CODE_SMELLS);
        codeSmells.put("LoggerLibraryRule", RuleType.CODE_SMELLS);
        codeSmells.put("MagicNumbersRule", RuleType.CODE_SMELLS);
        codeSmells.put("NestedStatementsRule", RuleType.CODE_SMELLS);
        codeSmells.put("PublicVariablesRule", RuleType.CODE_SMELLS);
        codeSmells.put("RethrowInCatchRule", RuleType.CODE_SMELLS);
        codeSmells.put("ReturnNullCollectionRule", RuleType.CODE_SMELLS);
        codeSmells.put("TODOsRule", RuleType.CODE_SMELLS);
        codeSmells.put("TooManyParamsRule", RuleType.CODE_SMELLS);
        codeSmells.put("UnnecessaryImportRule", RuleType.CODE_SMELLS);
        codeSmells.put("UnusedParameterRule", RuleType.CODE_SMELLS);

        return codeSmells;
    }

    private Map<String, RuleType> vulnerabilities() {
        final Map<String, RuleType> vulnerabilities = new HashMap<>();

        vulnerabilities.put("EmptyCatchRule", RuleType.VULNERABILITIES);
        vulnerabilities.put("PublicStaticFieldRule", RuleType.VULNERABILITIES);

        return vulnerabilities;
    }

}
