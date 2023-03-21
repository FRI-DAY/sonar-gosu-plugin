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
package de.friday.sonarqube.gosu.plugin.tools.listeners;

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.test.support.GosuSensorContextTester;
import de.friday.test.support.rules.dsl.gosu.GosuSourceCodeFile;
import de.friday.test.support.rules.dsl.specification.SourceCodeFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import static org.assertj.core.api.Assertions.assertThat;

class SyntaxErrorListenerTest {

    private static final Path LISTENERS_TEST_RESOURCES_DIR = Paths.get("src/test/resources/listeners");

    @Test
    void shouldSaveSyntaxErrorOnSensorContext() {
        // given
        final SensorContextTester sensorContext = aSensorContext();
        final SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener(sensorContext, aProperties());

        // when
        syntaxErrorListener.syntaxError(
                aGosuLexer(),
                "#",
                1,
                5,
                "Syntax error",
                new DummyRecognitionException()
        );

        // then
        assertThat(sensorContext.allAnalysisErrors()).hasSize(1).allSatisfy(analysisError -> {
            assertThat(analysisError.message()).isEqualTo("Syntax error");
            assertThat(analysisError.location()).isNotNull();
            assertThat(analysisError.location().line()).isEqualTo(1);
            assertThat(analysisError.location().lineOffset()).isEqualTo(5);
            assertThat(analysisError.inputFile()).isNotNull();
        });

    }

    private GosuLexer aGosuLexer() {
        return new GosuLexer(null);
    }

    private SensorContextTester aSensorContext() {
        return new GosuSensorContextTester(LISTENERS_TEST_RESOURCES_DIR, "SomeRuleKey").get();
    }

    private GosuFileProperties aProperties() {
        final SourceCodeFile file = new GosuSourceCodeFile("syntax/GosuFile.gs", LISTENERS_TEST_RESOURCES_DIR.toFile().getPath());
        return new GosuFileProperties(file.asInputFile(), null, null);
    }

    private static class DummyRecognitionException extends RecognitionException {
        public DummyRecognitionException() {
            super(null, null, null);
        }
    }

}
