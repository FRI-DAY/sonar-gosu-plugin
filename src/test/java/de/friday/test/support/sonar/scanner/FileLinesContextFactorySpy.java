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
package de.friday.test.support.sonar.scanner;

import com.google.common.collect.ImmutableMap;
import de.friday.test.support.TestResourcesDirectories;
import java.util.HashMap;
import java.util.Map;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.KeyValueFormat;
import static java.util.stream.Collectors.toMap;

public class FileLinesContextFactorySpy implements FileLinesContextFactory {

    private final SensorContextTester sensorContextTester;

    public FileLinesContextFactorySpy() {
        this(SensorContextTester.create(TestResourcesDirectories.RESOURCES_DIR.getPath()));
    }

    public FileLinesContextFactorySpy(SensorContextTester sensorContextTester) {
        this.sensorContextTester = sensorContextTester;
    }

    @Override
    public FileLinesContext createFor(InputFile inputFile) {
        return new FilesLinesContextSpy(sensorContextTester, inputFile);
    }

    private static class FilesLinesContextSpy implements FileLinesContext {

        private final SensorContext context;
        private final InputFile inputFile;

        private final Map<String, Map<Integer, Object>> map = new HashMap<>();

        FilesLinesContextSpy(SensorContextTester context, InputFile inputFile) {
            this.context = context;
            this.inputFile = inputFile;
        }

        @Override
        public void setIntValue(String metricKey, int line, int value) {
            setValue(metricKey, line, value);
        }

        @Override
        public Integer getIntValue(String metricKey, int line) {
            final Map<Integer, Object> lines = map.get(metricKey);
            return (Integer) lines.get(line);
        }

        @Override
        public void setStringValue(String metricKey, int line, String value) {
            setValue(metricKey, line, value);
        }

        @Override
        public String getStringValue(String metricKey, int line) {
            final Map<Integer, Object> lines = map.get(metricKey);
            return (String) lines.get(line);
        }

        private void setValue(String metricKey, int line, Object value) {
            map.computeIfAbsent(metricKey, k -> new HashMap<>())
                    .put(line, value);
        }

        @Override
        public void save() {
            for (Map.Entry<String, Map<Integer, Object>> entry : map.entrySet()) {
                String metricKey = entry.getKey();
                Map<Integer, Object> lines = entry.getValue();
                String data = KeyValueFormat.format(optimizeStorage(metricKey, lines));
                context.newMeasure()
                        .on(inputFile)
                        .forMetric(new Metric.Builder(metricKey, metricKey, Metric.ValueType.STRING).create())
                        .withValue(data)
                        .save();
                entry.setValue(ImmutableMap.copyOf(lines));
            }
        }

        private static Map<Integer, Object> optimizeStorage(String metricKey, Map<Integer, Object> lines) {
            if (CoreMetrics.NCLOC_DATA_KEY.equals(metricKey) || CoreMetrics.COMMENT_LINES_DATA_KEY.equals(metricKey) || CoreMetrics.EXECUTABLE_LINES_DATA_KEY.equals(metricKey)) {
                return lines.entrySet().stream()
                        .filter(entry -> !entry.getValue().equals(0))
                        .collect(toMap(Map.Entry<Integer, Object>::getKey, Map.Entry<Integer, Object>::getValue));
            }
            return lines;
        }
    }
}
