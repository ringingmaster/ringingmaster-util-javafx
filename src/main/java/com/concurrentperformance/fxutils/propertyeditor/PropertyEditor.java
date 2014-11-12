package com.concurrentperformance.fxutils.propertyeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class PropertyEditor extends ScrollPane {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static final int ROOM_FOR_VERT_LINE = 1;

	private final GroupedValues propertyValues = new GroupedValues();

	private Pane editorsContainer;
	private PropertyInteractionPane interactionPane = new PropertyInteractionPane(this);
	private PropertyGeometry propertyGeometry = new PropertyGeometry();

	private double nominalWidth = propertyGeometry.getMinUnderlyingControlWidth();

	public PropertyEditor() {

		editorsContainer = new Pane(interactionPane);
		setContent(editorsContainer);
		editorsContainer.relocate(0, 0);

		viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				nominalWidth = Math.max(propertyGeometry.getMinUnderlyingControlWidth(), newValue.getWidth()-2);

				interactionPane.setWidth(nominalWidth);
				editorsContainer.setPrefWidth(nominalWidth);
				if (propertyGeometry.getVertSeparatorPosition() > nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge()) {
					propertyGeometry.setVertSeparatorPosition(nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge());
				}
				updateControl();
			}
		});

	}

	public void add(String group, PropertyValue propertyValue) {
		propertyValues.add(group, propertyValue);
		propertyValue.setFont(propertyGeometry.getFont());
		Node editor = propertyValue.getEditor();
		editorsContainer.getChildren().add(editor);
		updateControl();
	}

	public void showGroupByName(String groupName, boolean show) {
		PropertyValue groupPropertyValue = checkNotNull(findPropertyByName(groupName), "Can't find group [{}] ", groupName);
		checkState(groupPropertyValue instanceof GroupPropertyValue);
		((GroupPropertyValue)groupPropertyValue).setGroupVisible(show);
	}

	void updateControl() {
		setEditorsContainerHeight();
		setEditorVisibility();
		relocateEditors();
		interactionPane.draw();
	}

	private void setEditorVisibility() {
		boolean visible = false;
		for (int index=0;index<propertyValues.size();index++) {
			PropertyValue propertyValue = propertyValues.get(index);
			if (propertyValue instanceof GroupPropertyValue) {
				visible = ((GroupPropertyValue)propertyValue).isGroupVisible();
			}
			else {
				propertyValue.setVisible(visible);
			}
		}
	}

	private void setEditorsContainerHeight() {
		double height = Math.max(propertyGeometry.getMinUnderlyingControlHeight(), ((propertyValues.sizeCollapsed()) * propertyGeometry.getHeight()) + propertyGeometry.getPadding());

		interactionPane.setHeight(height);
		editorsContainer.setPrefHeight(height);
	}

	private void relocateEditors() {
		for (int index=0;index<propertyValues.sizeCollapsed();index++) {
			PropertyValue propertyValue = propertyValues.getAsCollapsed(index);
			propertyValue.positionEditor(propertyGeometry.getVertSeparatorPosition() + ROOM_FOR_VERT_LINE,
					(index * propertyGeometry.getHeight()) + propertyGeometry.getPadding(),
					nominalWidth - propertyGeometry.getVertSeparatorPosition() - ROOM_FOR_VERT_LINE, 10);
		}
	}

	public PropertyValue findPropertyByName(String name) {
		return propertyValues.findPropertyByName(name);
	}

	PropertyGeometry getPropertyGeometry() {
		return propertyGeometry;
	}

	GroupedValues getPropertyValues() {
		return propertyValues;
	}


}