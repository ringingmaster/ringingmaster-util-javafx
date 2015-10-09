package com.concurrentperformance.fxutils.menu;

import com.concurrentperformance.fxutils.events.EventDefinition;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class MenuDefinitionLeafEvent implements MenuDefinition {

	private final EventDefinition event;
	public MenuDefinitionLeafEvent(EventDefinition event) {
		this.event = event;
	}

	@Override
	public MenuItem getMenu() {
		MenuItem menuItem = new MenuItem(event.getName(), new ImageView(event.getImage()));
		menuItem.setOnAction(event::handle);
		menuItem.disableProperty().bind(event.disableProperty());
		return menuItem;
	}
}