package org.ringingmaster.fxutils.toolbar;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class ToolbarDefinitionSeparator implements ToolbarDefinitionItem {

	private final Orientation orientation;

	public ToolbarDefinitionSeparator(Orientation orientation) {
		this.orientation = checkNotNull(orientation);
	}

	@Override
	public Node getItem() {
		return new Separator(orientation);
	}
}
