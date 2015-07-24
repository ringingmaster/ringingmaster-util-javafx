package com.concurrentperformance.fxutils.tab;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class InjectableTabPane extends TabPane {

	public void setTabDefinitions(List<TabDefinition> tabDefinitions) {
		getTabs().clear();

		for (TabDefinition element : tabDefinitions) {
			Tab tab = new Tab();
			tab.setText(element.getName());
			tab.setContent(element.getNode());
			getTabs().add(tab);
		}
	}



}
