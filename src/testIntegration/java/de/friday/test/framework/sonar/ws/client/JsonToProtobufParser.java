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
package de.friday.test.framework.sonar.ws.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

public final class JsonToProtobufParser {

    private final String json;

    private JsonToProtobufParser(String jsonAsString) {
        this.json = jsonAsString;
    }

    public static JsonToProtobufParser from(String json) {
        return new JsonToProtobufParser(json);
    }

    public <I> I toMessage(final Message.Builder builder) {
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
            return (I) builder.build();
        } catch (InvalidProtocolBufferException exception) {
            throw new RuntimeException("Failed to adapt JSON to Protobuf message", exception);
        }
    }

}
