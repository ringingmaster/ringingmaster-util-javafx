package com.concurrentperformance.fxutils.shutdown;

import com.concurrentperformance.util.listener.ConcurrentListenable;
import com.concurrentperformance.util.listener.Listenable;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class ShutdownService extends ConcurrentListenable<ShutdownServiceVeto> implements Listenable<ShutdownServiceVeto> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void setGlobalStage(Stage globalStage) {
		globalStage.setOnCloseRequest(event -> {
			log.info("Attempt at shutdown");
			for (ShutdownServiceVeto shutdownServiceVeto : getListeners()) {
				if (shutdownServiceVeto.shutdownServiceVeto_allowShutdown() == ShutdownServiceVeto.ShutdownOptions.PREVENT_SHUTDOWN) {
					log.info("[{}] is preventing shutdown", shutdownServiceVeto);
					event.consume();
					return;
				}
			}
			log.info("Shutdown allowed");

		});
	}
}

