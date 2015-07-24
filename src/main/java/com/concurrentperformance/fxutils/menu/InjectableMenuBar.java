package com.concurrentperformance.fxutils.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

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

	public void setMenuBarElements(List<Menu> menuBarElements) {
		getMenus().addAll(menuBarElements);

	}

//TODO remove	Menu menuFile = new Menu("File");
//	menuFile.getItems().add(0,new MenuItem("Stephen"));
//
//	getMenus().addAll(menuFile);

}
