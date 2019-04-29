package org.ringingmaster.util.javafx.menu;

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

    public void setMenuBarElements(List<MenuDefinition> menuDefinitions) {

        for (MenuDefinition menuDefinition : menuDefinitions) {
            getMenus().add((Menu) menuDefinition.getMenu());
        }
    }

}
