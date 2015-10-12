package com.concurrentperformance.fxutils.toolbar;

import com.concurrentperformance.fxutils.events.EventDefinition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class ToolbarDefinitionEvent implements ToolbarDefinitionItem {

	private final EventDefinition eventDefinition;

	public ToolbarDefinitionEvent(EventDefinition eventDefinition) {
		this.eventDefinition = eventDefinition;
	}

	@Override
	public Node getItem() {
		Button button = new Button("", new ImageView(eventDefinition.getImage()));

		button.setOnAction(eventDefinition::handle);
		button.disableProperty().bind(eventDefinition.disableProperty());
		Tooltip tooltip = new Tooltip();
		tooltip.textProperty().bind(eventDefinition.tooltipTextProperty());
		button.setTooltip(tooltip);
//TODO 		button.setPressed();

		return button;
	}
}
