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
package de.friday.test.support.rules.dsl.gosu;

import de.friday.test.support.rules.dsl.specification.RuleSpecification;
import de.friday.test.support.rules.dsl.specification.SourceCodeFile;

/**
 * Entrypoint for the test of Gosu Rules.
 */
public final class GosuRuleTestDsl {

    private GosuRuleTestDsl() {
    }

    public static RuleSpecification given(String sourceCodeFileName) {
        return new GosuRuleSpecification(sourceCodeFileName);
    }

    public static RuleSpecification given(SourceCodeFile sourceCodeFile) {
        return new GosuRuleSpecification(sourceCodeFile);
    }
}
