package com.concurrentperformance.fxutils.shutdown;

/**
 * TODO Comments
 *
 * @author Lake
 */
public interface ShutdownServiceVeto {

	enum ShutdownOptions{
		ALLOW_SHUTDOWN,
		PREVENT_SHUTDOWN,
	}

	ShutdownOptions shutdownServiceVeto_allowShutdown();
}
