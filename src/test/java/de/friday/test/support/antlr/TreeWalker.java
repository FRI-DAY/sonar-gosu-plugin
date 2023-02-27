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
package de.friday.test.support.antlr;

import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.antlr.GosuParserBaseListener;

public class TreeWalker extends GosuParserBaseListener {
    private long methodCall = 0;
    private long unaryExpression = 0;
    private long field = 0;
    private long newExpression = 0;
    private long propertySignature = 0;
    private long lambdaExpression = 0;

    //Statements
    private long assignmentStatement = 0;
    private long tryStatement = 0;
    private long catchStatement = 0;
    private long finallyStatement = 0;
    private long throwStatement = 0;
    private long returnStatement = 0;
    private long ifStatement = 0;
    private long foreachStatement = 0;
    private long switchBlockStatement = 0;

    private long intervalExpression = 0;

    public TreeWalker() {
    }

    @Override
    public void exitIntervalExpression(GosuParser.IntervalExpressionContext ctx) {
        intervalExpression++;
    }

    @Override
    public void exitForEachStatement(GosuParser.ForEachStatementContext ctx) {
        foreachStatement++;
    }

    @Override
    public void exitThrowStatement(GosuParser.ThrowStatementContext ctx) {
        throwStatement++;
    }

    @Override
    public void exitFinallyStatement(GosuParser.FinallyStatementContext ctx) {
        finallyStatement++;
    }

    @Override
    public void exitCatchClause(GosuParser.CatchClauseContext ctx) {
        catchStatement++;
    }

    @Override
    public void exitTryCatchFinallyStatement(GosuParser.TryCatchFinallyStatementContext ctx) {
        tryStatement++;
    }

    @Override
    public void exitAssignStatement(GosuParser.AssignStatementContext ctx) {
        assignmentStatement++;
    }

    @Override
    public void exitLambdaExpression(GosuParser.LambdaExpressionContext ctx) {
        lambdaExpression++;
    }

    @Override
    public void exitPropertySignature(GosuParser.PropertySignatureContext ctx) {
        propertySignature++;
    }

    @Override
    public void exitNewExpression(GosuParser.NewExpressionContext ctx) {
        newExpression++;
    }

    @Override
    public void exitSwitchBlockStatement(GosuParser.SwitchBlockStatementContext ctx) {
        switchBlockStatement++;
    }

    @Override
    public void exitReturnStatement(GosuParser.ReturnStatementContext ctx) {
        returnStatement++;
    }

    @Override
    public void exitIfStatement(GosuParser.IfStatementContext ctx) {
        ifStatement++;
    }

    @Override
    public void exitField(GosuParser.FieldContext ctx) {
        field++;
    }

    @Override
    public void exitUnaryExpression(GosuParser.UnaryExpressionContext ctx) {
        unaryExpression++;
    }

    @Override
    public void exitMethodCall(GosuParser.MethodCallContext ctx) {
        methodCall++;
    }

    public long getMethodCall() {
        return methodCall;
    }

    public long getUnaryExpression() {
        return unaryExpression;
    }

    public long getField() {
        return field;
    }

    public long getNewExpression() {
        return newExpression;
    }

    public long getPropertySignature() {
        return propertySignature;
    }

    public long getLambdaExpression() {
        return lambdaExpression;
    }

    public long getAssignmentStatement() {
        return assignmentStatement;
    }

    public long getTryStatement() {
        return tryStatement;
    }

    public long getCatchStatement() {
        return catchStatement;
    }

    public long getFinallyStatement() {
        return finallyStatement;
    }

    public long getThrowStatement() {
        return throwStatement;
    }

    public long getReturnStatement() {
        return returnStatement;
    }

    public long getIfStatement() {
        return ifStatement;
    }

    public long getForeachStatement() {
        return foreachStatement;
    }

    public long getSwitchBlockStatement() {
        return switchBlockStatement;
    }

    public long getIntervalExpression() {
        return intervalExpression;
    }
}
