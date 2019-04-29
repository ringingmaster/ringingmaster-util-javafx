package org.ringingmaster.util.javafx.box;

import javafx.scene.layout.VBox;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class InjectableVBox extends VBox {

    public void setBoxLayoutDefinitions(List<BoxLayoutDefinition> boxLayoutDefinitions) {

        for (BoxLayoutDefinition boxLayoutDefinition : boxLayoutDefinitions) {

            getChildren().add(boxLayoutDefinition.getNode());
            VBox.setVgrow(boxLayoutDefinition.getNode(), boxLayoutDefinition.getPriority());
        }

    }
}