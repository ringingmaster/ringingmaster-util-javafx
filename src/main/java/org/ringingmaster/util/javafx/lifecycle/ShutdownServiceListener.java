package org.ringingmaster.util.javafx.lifecycle;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public interface ShutdownServiceListener {

    enum ShutdownOptions {
        ALLOW_SHUTDOWN,
        PREVENT_SHUTDOWN,
    }

    ShutdownOptions shutdownService_shutdown();
}
