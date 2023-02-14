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
package de.friday.sonarqube.gosu.plugin.measures.metrics;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.Properties;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;

public class LinesOfCodeMetric extends AbstractMetricBase {
    private final Properties properties;
    private final SensorContext context;
    private int linesOfCode;

    @Inject
    public LinesOfCodeMetric(Properties properties, SensorContext context) {
        this.properties = properties;
        this.context = context;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    @Override
    @SuppressWarnings("squid:CommentedOutCodeLine")
    public void enterStart(GosuParser.StartContext ctx) {
        InputFile file = properties.getFile();
        linesOfCode = file.lines();
        if (file instanceof DefaultInputFile) {
            linesOfCode = ((DefaultInputFile) file).nonBlankLines();
        }
        /*
         * Lines of code metric is set to 1 for each file as SQ license limit is 100k lines of code for all projects in total.
         * Uncomment it when license lets scan more lines of code.
         * saveMetric(context, file, CoreMetrics.NCLOC, linesOfCode);
         */
        saveMetric(context, file, CoreMetrics.NCLOC, 1);
    }
}
