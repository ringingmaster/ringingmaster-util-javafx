package org.ringingmaster.util.javafx.menu;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class MenuDefinitionLeafSeparator implements MenuDefinition {

    @Override
    public MenuItem getMenu() {
        return new SeparatorMenuItem();
    }
}
