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

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckTypeTest {

    @ParameterizedTest(name = "should return package {1} rule keys for CheckType {0}")
    @MethodSource("checkTypesAndRuleKeysCount")
    void shouldReturnRuleKeysOfCheckType(CheckType checkType, int expectedNumberOfRuleKeys) {
        //when
        final List<String> ruleKeys = checkType.getRuleKeys();

        //then
        assertThat(ruleKeys).hasSize(expectedNumberOfRuleKeys);
    }

    private Stream<Arguments> checkTypesAndRuleKeysCount() {
        return Stream.of(
                arguments(CheckType.BUGS, 3),
                arguments(CheckType.CODE_SMELLS, 12),
                arguments(CheckType.METRICS, 3),
                arguments(CheckType.VULNERABILITIES, 2)
        );
    }

    @ParameterizedTest(name = "should return package {1} for CheckType {0}")
    @MethodSource("checkTypesAndPackages")
    void shouldReturnPackageSuffix(CheckType checkType, String expectedPackageSuffix) {
        //when
        final String packageSuffix = checkType.getPackageSuffix();

        //then
        assertThat(packageSuffix).isEqualTo(expectedPackageSuffix);
    }

    private Stream<Arguments> checkTypesAndPackages() {
        return Stream.of(
                arguments(CheckType.BUGS, "bugs"),
                arguments(CheckType.CODE_SMELLS, "smells"),
                arguments(CheckType.METRICS, "metrics"),
                arguments(CheckType.VULNERABILITIES, "vulnerabilities")
        );
    }
}
