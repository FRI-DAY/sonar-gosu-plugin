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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.sonar.check.Rule;

public enum RuleType {
    BUGS("bugs"),
    CODE_SMELLS("smells"),
    METRICS("metrics"),

    VULNERABILITIES("vulnerabilities");

    private static final String RULES_BASE_PACKAGE = "de.friday.sonarqube.gosu.plugin.rules";
    private final String packageSuffix;

    RuleType(String packageSuffix) {
        this.packageSuffix = packageSuffix;
    }

    public String getPackageSuffix() {
        return packageSuffix;
    }

    private String getRuleTypePackage() {
        return RULES_BASE_PACKAGE + "." + packageSuffix;
    }

    public List<String> getRuleKeys() {
        return Collections.unmodifiableList(
                new Reflections(getRuleTypePackage())
                        .getSubTypesOf(BaseGosuRule.class)
                        .parallelStream()
                        .map(checkClass -> checkClass.getAnnotation(Rule.class).key())
                        .collect(Collectors.toList())
        );
    }
}
