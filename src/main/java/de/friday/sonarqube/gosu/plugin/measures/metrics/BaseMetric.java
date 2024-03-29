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

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.antlr.GosuParserBaseListener;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.SensorContext;

public abstract class BaseMetric extends GosuParserBaseListener {
    private static final Set<Integer> GOSU_COMPLEXITY_OPERATORS = new HashSet<>(Arrays.asList(
            GosuLexer.AND,
            GosuLexer.CONJ,
            GosuLexer.OR,
            GosuLexer.DISJ,
            GosuLexer.BITAND,
            GosuLexer.BITOR)
    );

    protected final SensorContext context;
    protected final GosuFileProperties gosuFileProperties;

    protected BaseMetric(SensorContext context, GosuFileProperties gosuFileProperties) {
        this.context = context;
        this.gosuFileProperties = gosuFileProperties;
    }

    public static boolean isComplexityOperator(int type) {
        return GOSU_COMPLEXITY_OPERATORS.contains(type);
    }

    protected boolean shouldSaveMetric() {
        return gosuFileProperties.isMainFile();
    }

    <T extends Serializable> void saveMetricOnContext(Metric<T> metric, T value) {
        if (shouldSaveMetric()) {
            context.<T>newMeasure()
                    .withValue(value)
                    .forMetric(metric)
                    .on(gosuFileProperties.getFile())
                    .save();
        }
    }
}
