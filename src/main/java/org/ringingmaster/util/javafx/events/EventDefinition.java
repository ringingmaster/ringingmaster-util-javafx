package org.ringingmaster.util.javafx.events;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public interface EventDefinition {

    String getName();

    Image getImage();

    List<EventDefinition> getSubItems();

    void handle(ActionEvent event);

    BooleanProperty disableProperty();

    StringProperty tooltipTextProperty();

    BooleanProperty pressedProperty();
}
