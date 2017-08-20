package org.ringingmaster.util.javafx.lifecycle;

/**
 * TODO Comments
 *
 * @author Lake
 */
public interface ShutdownServiceListener {

	enum ShutdownOptions{
		ALLOW_SHUTDOWN,
		PREVENT_SHUTDOWN,
	}

	ShutdownOptions shutdownService_shutdown();
}
