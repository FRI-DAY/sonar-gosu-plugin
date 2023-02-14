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
package de.friday.test.support.checks.dsl.specification;

public interface IssueSpecification {

    /**
     * Syntactic sugar to enable expressive issue expectations.
     * E.g.
     * <pre>
     * given("TODOsCheck/ok.gs")
     * .whenCheckedAgainst(TODOsCheck.class)
     * .then()
     * .issuesFound()
     * .hasSizeEqualTo(5);
     * .and()
     * .areLocatedOn(expectedLocations);
     * </pre>
     *
     * @return The Issue specification
     */
    IssueSpecification and();

    /**
     * Syntactic sugar to enable expressive issue expectations.
     * E.g.
     * <pre>
     * given("TODOsCheck/ok.gs").whenCheckedAgainst(TODOsCheck.class).then().issuesFound().areEmpty();
     * </pre>
     *
     * @return The Issue specification
     */
    IssueSpecification issuesFound();

    /**
     * Syntactic sugar to enable expressive issue expectations.
     * The following expression
     * <pre>
     * given("TODOsCheck/ok.gs").whenCheckedAgainst(TODOsCheck.class).then().noIssuesFound();
     * </pre>
     * has the same effect as:
     * <pre>
     * given("TODOsCheck/ok.gs").whenCheckedAgainst(TODOsCheck.class).then().issuesFound().areEmpty();
     * </pre>
     * @return The Issue specification
     */
    IssueSpecification noIssuesFound();

    /**
     * Evaluate if there were no issues found by the Check.
     * @return The Issue specification
     */
    IssueSpecification areEmpty();

    /**
     * Evaluate if the list of issues found by the Check is equal to the expectation.
     * E.g.
     * <pre>
     * given("TODOsCheck/ok.gs").whenCheckedAgainst(TODOsCheck.class).then().issuesFound().hasSizeEqualTo(10);
     * </pre>
     * @return The Issue specification
     */
    IssueSpecification hasSizeEqualTo(int expectedNumberOfIssues);

    /**
     * Evaluate if the list of issues found by the Check are located on the expected text locations on the Source code file.
     * E.g.
     * <pre>
     * given("TODOsCheck/ok.gs")
     *      .whenCheckedAgainst(TODOsCheck.class)
     *      .then()
     *      .issuesFound()
     *      .areLocatedOn(expectedTextLocations);
     * </pre>
     * @return The Issue specification
     */
    IssueSpecification areLocatedOn(TextLocations locations);

}
