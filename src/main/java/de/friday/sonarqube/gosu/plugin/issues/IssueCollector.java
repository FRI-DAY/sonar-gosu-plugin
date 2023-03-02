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
package de.friday.sonarqube.gosu.plugin.issues;

import com.google.inject.Inject;

import de.friday.sonarqube.gosu.plugin.tools.listeners.SuppressWarningsListener;
import de.friday.sonarqube.gosu.plugin.tools.reflections.RulesKeysExtractor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.sonar.api.batch.fs.TextRange;

public class IssueCollector {
    private static final String CODE_SMELLS_TAG = "code_smells";
    private static final String CODE_SMELLS_DIR = "smells";
    private static final String ALL_RULES_TAG = "all";
    @Inject
    private SuppressWarningsListener suppressWarningsListener;
    private Map<String, List<TextRange>> suppressWarnings;

    private final List<Issue> issues = new ArrayList<>();

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public List<Issue> getIssues() {
        suppressWarnings = suppressWarningsListener.getSuppressWarnings();
        return suppressWarnings.isEmpty()
                ? Collections.unmodifiableList(issues)
                : Collections.unmodifiableList(filterIssues());
    }

    private List<Issue> filterIssues() {
        final List<Issue> filteredIssues = new ArrayList<>();
        for (Issue issue : issues) {
            getUnsuppressedIssue(issue).ifPresent(filteredIssues::add);
        }
        return filteredIssues;
    }

    private Optional<Issue> getUnsuppressedIssue(Issue issue) {
        return isSuppressed(issue) ? Optional.empty() : Optional.of(issue);
    }

    private boolean isSuppressed(Issue issue) {
        final TextRange issuePosition = issue.getPosition();
        final String issueRuleKey = issue.getRuleKey().rule();
        final String issuePackageTag = getRulePackageTag(issueRuleKey);

        return isInAllWarnings(issuePosition)
                || isInPackageWarnings(issuePackageTag, issuePosition)
                || isInRuleWarnings(issueRuleKey, issuePosition);
    }

    private String getRulePackageTag(String issueKey) {
        final String directory = RulesKeysExtractor.getRulePackage(issueKey);
        return CODE_SMELLS_DIR.equals(directory) ? CODE_SMELLS_TAG : directory;
    }

    private boolean isInAllWarnings(TextRange issueTextRange) {
        return compareTextRanges(suppressWarnings.get(ALL_RULES_TAG), issueTextRange);
    }

    private boolean isInPackageWarnings(String issuePackageTag, TextRange issueTextRange) {
        return compareTextRanges(suppressWarnings.get(issuePackageTag), issueTextRange);
    }

    private boolean isInRuleWarnings(String issueRuleKey, TextRange issueTextRange) {
        return compareTextRanges(suppressWarnings.get(issueRuleKey), issueTextRange);
    }

    private boolean compareTextRanges(List<TextRange> textRangeList, TextRange issueTextRange) {
        if (textRangeList == null) {
            return false;
        }

        if (issueTextRange == null) {
            return true;
        }

        for (TextRange textRange : textRangeList) {
            if (issueTextRange.overlap(textRange)) {
                return true;
            }
        }
        return false;
    }
}
