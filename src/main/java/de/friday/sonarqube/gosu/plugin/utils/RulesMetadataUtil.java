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
package de.friday.sonarqube.gosu.plugin.utils;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import de.friday.sonarqube.gosu.language.GosuLanguage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;

public final class RulesMetadataUtil {
    private static final Gson GSON = new Gson();

    public enum Scope {
        ALL("All"),
        MAIN("Main"),
        TESTS("Tests");

        private String text;

        Scope(String text) {
            this.text = text;
        }

        private static Scope getDefault() {
            return MAIN;
        }

        private static Scope fromString(String text) {
            for (Scope scope : Scope.values()) {
                if (scope.text.equalsIgnoreCase(text)) {
                    return scope;
                }
            }
            return getDefault();
        }
    }

    private RulesMetadataUtil() {
    }

    public static Scope getRuleScope(String ruleKey) {
        RuleMetadata metadata = readRuleMetadata(ruleKey);
        return metadata == null
                ? Scope.getDefault()
                : Scope.fromString(metadata.scope);
    }

    @Nullable
    private static RuleMetadata readRuleMetadata(String metadataKey) {
        URL resource = RuleMetadata.class.getResource('/' + GosuLanguage.GOSU_RESOURCE_PATH + '/' + metadataKey + ".json");

        return resource != null ? GSON.fromJson(readResource(resource), RuleMetadata.class) : null;
    }

    private static String readResource(URL resource) {
        try {
            return Resources.toString(resource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read: " + resource, e);
        }
    }

    /*
     * Static classes copied from SonarQube Java Plugin: RuleMetadata, SecurityStandards, Remediation
     */
    static class RuleMetadata {
        private static final String SECURITY_HOTSPOT = "SECURITY_HOTSPOT";

        String title;
        String status;
        @Nullable
        Remediation remediation;

        String type;
        String[] tags;
        String defaultSeverity;
        @Nullable
        String scope;
        SecurityStandards securityStandards = new SecurityStandards();

        boolean isSecurityHotspot() {
            return SECURITY_HOTSPOT.equals(type);
        }
    }

    /*
     * SuppressWarnings - Field names should comply with a naming convention
     */
    @SuppressWarnings("squid:S00116")
    static class SecurityStandards {
        int[] CWE = {};
        String[] OWASP = {};
    }

    static class Remediation {
        String func;
        String constantCost;
        String linearDesc;
        String linearOffset;
        String linearFactor;

        public DebtRemediationFunction remediationFunction(RulesDefinition.DebtRemediationFunctions drf) {
            if (func.startsWith("Constant")) {
                return drf.constantPerIssue(constantCost.replace("mn", "min"));
            }
            if ("Linear".equals(func)) {
                return drf.linear(linearFactor.replace("mn", "min"));
            }
            return drf.linearWithOffset(linearFactor.replace("mn", "min"), linearOffset.replace("mn", "min"));
        }
    }
}
