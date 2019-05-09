package org.ringingmaster.util.javafx.lifecycle;

import javafx.stage.Stage;
import org.ringingmaster.util.listener.ConcurrentListenable;
import org.ringingmaster.util.listener.Listenable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class ShutdownService extends ConcurrentListenable<ShutdownServiceListener> implements Listenable<ShutdownServiceListener> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void setGlobalStage(Stage globalStage) {
        globalStage.setOnCloseRequest(event -> {
            ShutdownServiceListener.ShutdownOptions result = runShutdown();
            if (result == ShutdownServiceListener.ShutdownOptions.PREVENT_SHUTDOWN) {
                // prevent the close by consuming the event
                event.consume();
            }
        });
    }

    private ShutdownServiceListener.ShutdownOptions runShutdown() {
        log.info("Attempt at shutdown");
        for (ShutdownServiceListener shutdownServiceListener : getListeners()) {
            if (shutdownServiceListener.shutdownService_shutdown() == ShutdownServiceListener.ShutdownOptions.PREVENT_SHUTDOWN) {
                log.info("[{}] is preventing shutdown", shutdownServiceListener);
                return ShutdownServiceListener.ShutdownOptions.PREVENT_SHUTDOWN;
            }
        }
        log.info("Shutdown allowed");
        return ShutdownServiceListener.ShutdownOptions.ALLOW_SHUTDOWN;
    }
}

