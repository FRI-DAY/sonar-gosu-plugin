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
package de.friday.sonarqube.gosu.antlr;

import org.junit.jupiter.params.provider.Arguments;

class ComplexClassTestFixture {
    final String fileName;
    int expectedNumberOfLambdaExpressions = 0;
    int expectedNumberOfNewExpressions = 0;
    int expectedNumberOfSwitchBlocks = 0;
    int expectedNumberOfFields = 0;
    int expectedNumberOfIfs = 0;
    int expectedNumberOfUnaryExpressions = 0;
    int expectedNumberOfReturns = 0;
    int expectedNumberOfAtTokens = 0;
    int expectedNumberOfFunctions = 0;
    int expectedNumberOfUsesTokens = 0;
    int expectedNumberOfMethodCalls = 0;
    int expectedNumberOfPropertiesSignatures = 0;

    public ComplexClassTestFixture(String fileName) {
        this.fileName = fileName;
    }

    Arguments asArgument() {
        return Arguments.of(this);
    }

    public static Builder forGosuFile(String fileName) {
        return new Builder(fileName);
    }

    @Override
    public String toString() {
        return this.fileName;
    }

    static class Builder {

        final ComplexClassTestFixture testFixture;

        public Builder(String fileName) {
            this.testFixture = new ComplexClassTestFixture(fileName);
        }

        public Builder expects() {
            return this;
        }

        public Builder fields(int numberOfFields) {
            this.testFixture.expectedNumberOfFields = numberOfFields;
            return this;
        }

        public Builder ifs(int numberOfIfs) {
            this.testFixture.expectedNumberOfIfs = numberOfIfs;
            return this;
        }

        public Builder unaryExpressions(int numberOfUnaryExpressions) {
            this.testFixture.expectedNumberOfUnaryExpressions = numberOfUnaryExpressions;
            return this;
        }

        public Builder returns(int numberOfReturns) {
            this.testFixture.expectedNumberOfReturns = numberOfReturns;
            return this;
        }

        public Builder functions(int numberOfFunctions) {
            this.testFixture.expectedNumberOfFunctions = numberOfFunctions;
            return this;
        }

        public Builder atIdentifiers(int numberOfAtIdentifiers) {
            this.testFixture.expectedNumberOfAtTokens = numberOfAtIdentifiers;
            return this;
        }

        public Builder uses(int numberOfUses) {
            this.testFixture.expectedNumberOfUsesTokens = numberOfUses;
            return this;
        }

        public Builder methodCalls(int numberOfMethodCalls) {
            this.testFixture.expectedNumberOfMethodCalls = numberOfMethodCalls;
            return this;
        }

        public Builder properties(int numberOfProperties) {
            this.testFixture.expectedNumberOfPropertiesSignatures = numberOfProperties;
            return this;
        }

        public Builder newExpressions(int numberOfNewExpressions) {
            this.testFixture.expectedNumberOfNewExpressions = numberOfNewExpressions;
            return this;
        }

        public Builder switchBlocks(int numberOfSwitchBlocks) {
            this.testFixture.expectedNumberOfSwitchBlocks = numberOfSwitchBlocks;
            return this;
        }

        public Builder lambdas(int numberOfLambdaExpressions) {
            this.testFixture.expectedNumberOfLambdaExpressions = numberOfLambdaExpressions;
            return this;
        }

        public ComplexClassTestFixture build() {
            return testFixture;
        }
    }
}
