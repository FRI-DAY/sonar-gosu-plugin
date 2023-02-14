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

import de.friday.sonarqube.gosu.antlr.GosuParserBaseListener;
import org.sonar.api.rule.RuleKey;
import static de.friday.sonarqube.gosu.language.GosuLanguage.REPOSITORY_KEY;

/**
 * Base class for all Gosu Sonarqube checks.
 */
public abstract class AbstractCheckBase extends GosuParserBaseListener {

    /**
     * @return Check Key
     */
    protected abstract String getKey();

    /**
     * Returns Check specific Rule Key
     * Used to associate with Check description
     *
     * @return Check Rule Key
     */
    public RuleKey getRuleKey() {
        return RuleKey.of(REPOSITORY_KEY, getKey());
    }

}
