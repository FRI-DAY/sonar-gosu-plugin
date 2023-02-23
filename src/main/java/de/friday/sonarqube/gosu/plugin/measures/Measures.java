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

import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.sonarqube.gosu.plugin.measures.tools.GosuDuplicates;
import de.friday.sonarqube.gosu.plugin.measures.tools.GosuHighlighting;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;

public final class Measures {
    private final Properties properties;

    private Measures(Properties properties) {
        this.properties = properties;
    }

    public static Measures of(Properties properties) {
        return new Measures(properties);
    }

    public void addProcessedTokensTo(SensorContext context) {
        final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(properties.getFile());
        final NewHighlighting highlighting = context.newHighlighting().onFile(properties.getFile());
        final CommonTokenStream tokenStream = properties.getTokenStream();

        for (Token token : tokenStream.getTokens()) {
            GosuHighlighting.highlightToken(token, highlighting, properties);
            GosuDuplicates.addCopyAndPasteDetectionToken(token, cpdTokens);
        }

        highlighting.save();
        cpdTokens.save();
    }
}
