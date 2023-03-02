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

import de.friday.test.support.rules.dsl.specification.TextLocations;
import java.util.Arrays;
import java.util.List;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import static java.util.stream.Collectors.toList;

public final class GosuIssueLocations implements TextLocations {
    private final List<TextRange> textRanges;

    @SafeVarargs
    private GosuIssueLocations(List<Integer>... locations) {
        this.textRanges = from(locations);
    }

    @SafeVarargs
    public static GosuIssueLocations of(List<Integer>... locations) {
        return new GosuIssueLocations(locations);
    }

    @Override
    public List<TextRange> get() {
        return textRanges;
    }

    @SafeVarargs
    private final List<TextRange> from(List<Integer>... locations) {
        return Arrays.stream(locations).map(this::createTextRange).collect(toList());
    }

    private TextRange createTextRange(List<Integer> location) {
        if (location.size() != 4) {
            throw new RuntimeException("Issue coordinates are wrong.");
        }
        DefaultTextPointer start = new DefaultTextPointer(location.get(0), location.get(1) - 1);
        DefaultTextPointer stop = new DefaultTextPointer(location.get(2), location.get(3) - 1);

        return new DefaultTextRange(start, stop);
    }

}
