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
package de.friday.sonarqube.gosu.plugin.tools.listeners;

import de.friday.sonarqube.gosu.plugin.rules.bugs.SameConditionsInIfRule;
import de.friday.sonarqube.gosu.plugin.rules.metrics.CognitiveComplexityRule;
import de.friday.sonarqube.gosu.plugin.rules.metrics.LinesOfCodeRule;
import de.friday.sonarqube.gosu.plugin.rules.smells.TODOsRule;
import de.friday.sonarqube.gosu.plugin.rules.vulnerabilities.PublicStaticFieldCheck;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class SuppressWarningsListenerTest {

    @Test
    void shouldSuppressAllWarnings() {
        given("SuppressWarningsListener/AllWarnings.gs")
                .whenCheckedAgainst(TODOsRule.class)
                .then().issuesFound().hasSizeEqualTo(1);
    }

    @Test
    void shouldSuppressCodeSmellWarnings() {
        given("SuppressWarningsListener/CodeSmellsWarnings.gs")
                .whenCheckedAgainst(TODOsRule.class)
                .then().issuesFound().hasSizeEqualTo(1);
    }

    @Test
    void shouldSuppressSpecificCheckWarning() {
        given("SuppressWarningsListener/CheckWarnings.gs")
                .whenCheckedAgainst(TODOsRule.class)
                .then().issuesFound().hasSizeEqualTo(1);
    }

    @Test
    void shouldSuppressMetricsWarnings() {
        given("SuppressWarningsListener/MetricsWarnings.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
                .then().issuesFound().hasSizeEqualTo(12);
    }

    @Test
    void shouldSuppressBugsWarnings() {
        given("SuppressWarningsListener/BugsWarnings.gs")
                .whenCheckedAgainst(SameConditionsInIfRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void shouldSuppressVulnerabilitiesWarnings() {
        given("SuppressWarningsListener/BugsWarnings.gs")
                .whenCheckedAgainst(SameConditionsInIfRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void shouldSuppressWarningsOnFieldAndProperties() {
        given("SuppressWarningsListener/fieldsAndProperties.gs")
                .whenCheckedAgainst(TODOsRule.class)
                .then().issuesFound().areEmpty();

        given("SuppressWarningsListener/fieldsAndProperties.gs")
                .whenCheckedAgainst(PublicStaticFieldCheck.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void shouldSuppressWarningsOfMultipleChecks() {
        given("SuppressWarningsListener/arrayOfSuppressWarnings.gs")
                .whenCheckedAgainst(TODOsRule.class)
                .then().issuesFound().areEmpty();

        given("SuppressWarningsListener/arrayOfSuppressWarnings.gs")
                .whenCheckedAgainst(PublicStaticFieldCheck.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void shouldNotSuppressWarningsWhenAnnotatedCheckIsNotRelatedToIssue() {
        given("SuppressWarningsListener/notRelatedAnnotations.gs")
                .whenCheckedAgainst(PublicStaticFieldCheck.class)
                .then().issuesFound().hasSizeEqualTo(2);

        given("SuppressWarningsListener/notRelatedAnnotations.gs")
                .whenCheckedAgainst(TODOsRule.class)
                .then().issuesFound().hasSizeEqualTo(2);
    }

    @Test
    void shouldSuppressWarningsOnEnhancements() {
        given("SuppressWarningsListener/enhancementSuppressWarnings.gsx")
                .whenCheckedAgainst(TODOsRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void shouldSuppressWarningsOnCheckWithoutTextRange() {
        given("SuppressWarningsListener/lines501.gs")
                .whenCheckedAgainst(LinesOfCodeRule.class)
                .then().issuesFound().areEmpty();
    }
}
