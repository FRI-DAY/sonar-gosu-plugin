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
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;

public class LinesOfCodeMetric extends BaseMetric {
    private final GosuFileProperties gosuFileProperties;
    private final SensorContext context;

    @Inject
    public LinesOfCodeMetric(GosuFileProperties gosuFileProperties, SensorContext context) {
        this.gosuFileProperties = gosuFileProperties;
        this.context = context;
    }

    @Override
    public void enterStart(GosuParser.StartContext startContext) {
        final int linesOfCode = gosuFileProperties.getLinesOfCode();
        final InputFile file = gosuFileProperties.getFile();
        saveMetric(context, file, CoreMetrics.NCLOC, linesOfCode);
    }
}
