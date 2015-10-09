package com.concurrentperformance.fxutils.toolbar;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class InjectableToolBarOld extends ToolBar {

	public void setToolBarElements(List<Node> elements) {
		getItems().addAll(elements);

	}
}
