package com.concurrentperformance.fxutils.lifecycle;

import com.concurrentperformance.util.listener.ConcurrentListenable;
import com.concurrentperformance.util.listener.Listenable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkState;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class StartupService  extends ConcurrentListenable<StartupServiceListener> implements Listenable<StartupServiceListener> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private AtomicBoolean started = new AtomicBoolean(false);

	public void runStartup() {
		log.info("Attempt at startup");
		boolean success = started.compareAndSet(false,true);
		checkState(success, "Only call runStartup() once");
		for (StartupServiceListener startupServiceListener : getListeners()) {
			startupServiceListener.startupService_startup();
		}
		log.info("Startup allowed");
	}

}
