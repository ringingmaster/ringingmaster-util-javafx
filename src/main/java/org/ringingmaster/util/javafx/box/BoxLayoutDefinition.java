package org.ringingmaster.util.javafx.box;

import javafx.scene.Node;
import javafx.scene.layout.Priority;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class BoxLayoutDefinition {

    private final Node node;
    private final Priority priority;

    public BoxLayoutDefinition(Node node, Priority priority) {
        this.node = node;
        this.priority = priority;
    }

    public Node getNode() {
        return node;
    }

    public Priority getPriority() {
        return priority;
    }
}
