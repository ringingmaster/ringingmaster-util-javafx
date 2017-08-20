package org.ringingmaster.util.javafx.toolbar;

import javafx.scene.control.ToolBar;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class InjectableToolBar extends ToolBar {

	public void setDefinitions(List<ToolbarDefinitionItem> toolbarDefinitionItems) {

		for (ToolbarDefinitionItem toolbarDefinitionItem : toolbarDefinitionItems) {
			getItems().add(toolbarDefinitionItem.getItem());
		}
	}
}
