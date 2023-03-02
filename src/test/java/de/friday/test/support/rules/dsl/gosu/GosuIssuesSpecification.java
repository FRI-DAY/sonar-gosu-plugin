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
package de.friday.test.support.rules.dsl.gosu;

import de.friday.test.support.rules.dsl.specification.IssueSpecification;
import de.friday.test.support.rules.dsl.specification.TextLocations;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.batch.sensor.issue.IssueLocation;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

final class GosuIssuesSpecification implements IssueSpecification {
    private final List<Issue> issues;

    public GosuIssuesSpecification(List<Issue> issues) {
        Objects.requireNonNull(issues, "Issues can not be null");
        this.issues = issues;
    }

    @Override
    public IssueSpecification and() {
        return this;
    }

    @Override
    public IssueSpecification issuesFound() {
        return this;
    }

    @Override
    public void noIssuesFound() {
        this.issuesFound().areEmpty();
    }

    @Override
    public void areEmpty() {
        assertThat(this.issues)
                .as("No issues were expected to be found.")
                .isEmpty();
    }

    @Override
    public IssueSpecification hasSizeEqualTo(int expectedNumberOfIssues) {
        assertThat(this.numberOfIssuesFound())
                .as("Actual number of issues found is not what was expected.")
                .isEqualTo(expectedNumberOfIssues);
        return this;
    }

    @Override
    public IssueSpecification areLocatedOn(TextLocations locations) {
        assertThat(this.issuesTextRanges())
                .as("Expected to find issues on the given locations.")
                .isEqualTo(locations.get());
        return this;
    }

    private int numberOfIssuesFound() {
        final AtomicInteger numberOfIssues = new AtomicInteger();

        issues.forEach(issue -> {
            numberOfIssues.getAndIncrement();
            numberOfIssues.addAndGet(issue.flows().size());
        });

        return numberOfIssues.get();
    }

    private List<TextRange> issuesTextRanges() {
        final List<TextRange> actualTextRanges = new ArrayList<>();

        for (Issue issue : issues) {
            actualTextRanges.add(issue.primaryLocation().textRange());
            actualTextRanges.addAll(addSecondaryIssuesLocations(issue));
        }

        return actualTextRanges;
    }

    private List<TextRange> addSecondaryIssuesLocations(Issue issue) {
        return issue
                .flows()
                .stream()
                .flatMap(flow -> flow.locations().stream())
                .map(IssueLocation::textRange)
                .collect(toList());
    }
}
