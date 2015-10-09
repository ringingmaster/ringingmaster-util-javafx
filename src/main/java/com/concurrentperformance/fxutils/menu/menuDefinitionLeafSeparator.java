package com.concurrentperformance.fxutils.menu;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class MenuDefinitionLeafSeparator implements MenuDefinition {

	@Override
	public MenuItem getMenu() {
		return new SeparatorMenuItem();
	}
}
