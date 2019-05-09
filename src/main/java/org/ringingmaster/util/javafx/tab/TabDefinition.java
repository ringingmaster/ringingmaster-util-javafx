package org.ringingmaster.util.javafx.tab;

import javafx.scene.Node;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class TabDefinition {
    private final String name;
    private final Node node;


    public TabDefinition(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public Node getNode() {
        return node;
    }
}
