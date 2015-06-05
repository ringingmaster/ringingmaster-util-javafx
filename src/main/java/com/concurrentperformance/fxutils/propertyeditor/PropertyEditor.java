package com.concurrentperformance.fxutils.propertyeditor;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class PropertyEditor extends ScrollPane implements PropertyValueListener {

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

		viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
			nominalWidth = Math.max(propertyGeometry.getMinUnderlyingControlWidth(), newValue.getWidth()-2);

			interactionPane.setWidth(nominalWidth);
			editorsContainer.setPrefWidth(nominalWidth);
			if (propertyGeometry.getVertSeparatorPosition() > nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge()) {
				propertyGeometry.setVertSeparatorPosition(nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge());
			}
			updateControl();
		});

	}

	public void setVertSeparatorPosition(double vertSeparatorPosition) {

		if (vertSeparatorPosition > nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge()) {
			vertSeparatorPosition = nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge();
		}

		propertyGeometry.setVertSeparatorPosition(vertSeparatorPosition);
	}

	public void add(PropertyValue propertyValue) {
		add(GroupedValues.UNGROUPED, propertyValue);
	}

	public void add(String group, PropertyValue propertyValue) {
		propertyValues.add(group, propertyValue);
		propertyValue.setFont(propertyGeometry.getFont());
		Node editor = propertyValue.getEditor();
		editorsContainer.getChildren().add(editor);
		propertyValue.addListener(this);
		updateControl();
	}

	public void clear() {
		for (int i=0;i<propertyValues.sizeAll();i++) {
			editorsContainer.getChildren().remove(propertyValues.get(i).getEditor());
		}
		propertyValues.clear();
		updateControl();
	}

	public void showGroupByName(String groupName, boolean show) {
		propertyValues.showGroupByName(groupName, show);
		updateControl();
	}

	protected void updateControl() {
		setEditorsContainerHeight();
		setEditorVisibility();
		relocateEditors();
		interactionPane.draw();
	}

	private void setEditorVisibility() {
		boolean visible = true;
		for (int index=0;index<propertyValues.sizeAll();index++) {
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

	public int sizeAll() {
		return propertyValues.sizeAll();
	}

	public void allowSelection(boolean allowSelection) {
		interactionPane.allowSelection(allowSelection);
	}

	public int getSelectedIndex() {
		for (int i=0;i<propertyValues.sizeAll();i++) {
			if (propertyValues.get(i).isSelected()) {
				return i;
			}
		}
		return -1;
	}

	public PropertyValue getSelected() {
		for (int i=0;i<propertyValues.sizeAll();i++) {
			if (propertyValues.get(i).isSelected()) {
				return propertyValues.get(i);
			}
		}
		return null;
	}

	public void setSelectedIndex(int index) {
		propertyValues.setSelectedIndex(index);
	}

	@Override
	public void propertyValue_renderingChanged(PropertyValue propertyValue) {
		updateControl();
	}

	@Override
	public void propertyValue_editorSelected(PropertyValue propertyValue) {
		propertyValues.setSelectedIndex(propertyValue);
		updateControl();

	}
}