package org.ringingmaster.util.javafx.box;

import javafx.scene.layout.HBox;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class InjectableHBox extends HBox {

    public void setBoxLayoutDefinitions(List<BoxLayoutDefinition> boxLayoutDefinitions) {

        for (BoxLayoutDefinition boxLayoutDefinition : boxLayoutDefinitions) {

            getChildren().add(boxLayoutDefinition.getNode());
            HBox.setHgrow(boxLayoutDefinition.getNode(), boxLayoutDefinition.getPriority());
        }

    }
}