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
package de.friday.test.support;

import org.sonar.api.utils.Version;

public enum SonarServerVersionSupported {
    VERSION_6_5(Version.create(6, 5)),
    VERSION_7_4(Version.create(7, 5)),
    VERSION_9_9(Version.create(9, 9)),
    VERSION_10_0(Version.create(10, 0));

    private final Version version;

    SonarServerVersionSupported(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        return version;
    }
}
