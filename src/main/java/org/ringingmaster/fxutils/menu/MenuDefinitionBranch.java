package org.ringingmaster.fxutils.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class MenuDefinitionBranch implements MenuDefinition {

	private final List<MenuDefinition> menuDefinitions = new ArrayList<>();
	private final String name;

	MenuDefinitionBranch(List<MenuDefinition> menuDefinitions, String name) {
		this.menuDefinitions.addAll(menuDefinitions);
		this.name = name;
	}

	@Override
	public MenuItem getMenu() {
		Menu menu = new Menu(name);
		for (MenuDefinition menuDefinition : menuDefinitions) {
			menu.getItems().add(menuDefinition.getMenu());
		}
		return menu;
	}

}
