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
package de.friday.test.framework.sonar;

import de.friday.test.framework.sonar.server.SonarServer;
import de.friday.test.framework.sonar.server.SonarServerConfig;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SonarServerExtension implements BeforeAllCallback, ParameterResolver, ExtensionContext.Store.CloseableResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SonarServerExtension.class);
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.GLOBAL;

    static class LazyHolder {
        static final SonarServer SERVER_INSTANCE = new SonarServer(SonarServerConfig.fromSystemProperties());

        static final AtomicBoolean IS_SERVER_RUNNING = new AtomicBoolean(false);

        static void startServer() {
            if (IS_SERVER_RUNNING.get()) {
                LOGGER.debug("Sonar server already running.");
                return;
            }

            SERVER_INSTANCE.start();
            IS_SERVER_RUNNING.set(true);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return SonarServer.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public SonarServer resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return LazyHolder.SERVER_INSTANCE;
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        LazyHolder.startServer();
        registryServerShutdownCallback(context);
    }

    /**
     * Registry this extension as a closeable resource to be closed after all the tests finished.
     * Check close();
     * @param context
     */
    private void registryServerShutdownCallback(ExtensionContext context) {
        context.getRoot().getStore(NAMESPACE).put("SonarServer:Started", this);
    }

    /**
     * This ensures that the server is shutdown after all tests finished.
     */
    @Override
    public void close() {
        LOGGER.debug("Shutting down Sonar server");
        LazyHolder.SERVER_INSTANCE.stop();
    }
}
