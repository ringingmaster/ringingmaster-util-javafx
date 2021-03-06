package org.ringingmaster.util.javafx.toolbar;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import org.ringingmaster.util.javafx.components.PressableButton;
import org.ringingmaster.util.javafx.events.EventDefinition;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class ToolbarDefinitionEvent implements ToolbarDefinitionItem {

    private final EventDefinition eventDefinition;

    public ToolbarDefinitionEvent(EventDefinition eventDefinition) {
        this.eventDefinition = eventDefinition;
    }

    @Override
    public Node getItem() {
        PressableButton button = new PressableButton("", new ImageView(eventDefinition.getImage()));

        button.setOnAction(eventDefinition::handle);
        button.disableProperty().bind(eventDefinition.disableProperty());
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(eventDefinition.tooltipTextProperty());
        button.setTooltip(tooltip);
        button.pressedOverrideProperty().bind(eventDefinition.pressedProperty());

        return button;
    }
}
