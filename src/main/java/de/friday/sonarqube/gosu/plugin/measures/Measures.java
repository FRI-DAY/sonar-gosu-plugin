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
package de.friday.sonarqube.gosu.plugin.measures;

import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.measures.tools.GosuDuplicates;
import de.friday.sonarqube.gosu.plugin.measures.tools.GosuHighlighting;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;

public final class Measures {
    private final GosuFileProperties gosuFileProperties;

    private Measures(GosuFileProperties gosuFileProperties) {
        this.gosuFileProperties = gosuFileProperties;
    }

    public static Measures of(GosuFileProperties gosuFileProperties) {
        return new Measures(gosuFileProperties);
    }

    public void addProcessedTokensTo(SensorContext context) {
        final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(gosuFileProperties.getFile());
        final NewHighlighting highlighting = context.newHighlighting().onFile(gosuFileProperties.getFile());
        final CommonTokenStream tokenStream = gosuFileProperties.getTokenStream();

        for (Token token : tokenStream.getTokens()) {
            GosuHighlighting.highlightToken(token, highlighting, gosuFileProperties);
            GosuDuplicates.addCopyAndPasteDetectionToken(token, cpdTokens);
        }

        highlighting.save();
        cpdTokens.save();
    }
}
