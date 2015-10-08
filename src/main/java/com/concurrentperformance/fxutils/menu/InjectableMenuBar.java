package com.concurrentperformance.fxutils.menu;

import com.concurrentperformance.fxutils.events.EventDefinition;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class InjectableMenuBar extends MenuBar {

	public InjectableMenuBar() {
		setUseSystemMenuBar(true);
	}

	public void setMenuBarElements(MenuDefinition menuHolder) {

		List<MenuItem> menus = new ArrayList<>();
		build(menuHolder, menus);

		for (MenuItem menu : menus) {
			getMenus().add((Menu)menu);
		}
	}

	private void build(MenuDefinition menuHolder, List<MenuItem> menus) {

		EventDefinition event = menuHolder.getEvent();

		if (menuHolder.getSubItems() != null) {
			Menu menu = new Menu(event.getName());
			menus.add(menu);
			for (MenuDefinition menuDefinition : menuHolder.getSubItems()) {
				build(menuDefinition, menu.getItems());
			}
		}
		else {
			MenuItem menuItem = new MenuItem(event.getName(), new ImageView(event.getImage()));
			menuItem.setOnAction(event::handle);
			menuItem.disableProperty().bind(event.disableProperty());
			menus.add(menuItem);
		}
	}

}
