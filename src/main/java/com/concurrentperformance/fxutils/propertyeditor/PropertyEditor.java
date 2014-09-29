package com.concurrentperformance.fxutils.propertyeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class PropertyEditor extends ScrollPane {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static final int ROOM_FOR_VERT_LINE = 1;

	private final GroupedValues propertyValues = new GroupedValues();

	private Pane managerPane;
	private PropertyInteractionPane interactionPane = new PropertyInteractionPane(this);
	private PropertyGeometry propertyGeometry = new PropertyGeometry();

	private double nominalWidth = propertyGeometry.getMinUnderlyingControlWidth();

	public PropertyEditor() {

		managerPane = new Pane(interactionPane);
		setContent(managerPane);

		setFitToHeight(true);

		widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				nominalWidth = Math.max(propertyGeometry.getMinUnderlyingControlWidth(), observable.getValue().doubleValue());
				interactionPane.setWidth(nominalWidth);
				if (propertyGeometry.getVertSeparatorPosition() > nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge()) {
					propertyGeometry.setVertSeparatorPosition(nominalWidth - propertyGeometry.getClosestVertSeparatorCanBeToEdge());
				}
				updateControl();
			}
		});

		heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				interactionPane.setHeight(Math.max(propertyGeometry.getMinUnderlyingControlHeight(), observable.getValue().doubleValue()));
				updateControl();
			}
		});
	}

	public void add(String group, PropertyValue propertyValue) {
		propertyValues.add(group, propertyValue);
		propertyValue.setFont(propertyGeometry.getFont());
		Node editor = propertyValue.getEditor();
		managerPane.getChildren().add(editor);
		updateControl();
	}

	void updateControl() {
		interactionPane.draw();
		relocateEditors();
	}

	private void relocateEditors() {
		boolean visible = false;
		for (int index=0;index<propertyValues.size();index++) {
			PropertyValue propertyValue = propertyValues.get(index);
			if (propertyValue instanceof GroupPropertyValue) {
				visible = ((GroupPropertyValue)propertyValue).isGroupVisible();
			}
			else {
				propertyValue.updateVisibleState(visible);
			}
		}
		
		for (int index=0;index<propertyValues.sizeCollapsed();index++) {
			PropertyValue propertyValue = propertyValues.getAsCollapsed(index);
			propertyValue.positionEditor(propertyGeometry.getVertSeparatorPosition() + ROOM_FOR_VERT_LINE,
										(index * propertyGeometry.getHeight()) + propertyGeometry.getPadding(),
										nominalWidth - propertyGeometry.getVertSeparatorPosition() - ROOM_FOR_VERT_LINE, 10);
		}
	}

	PropertyGeometry getPropertyGeometry() {
		return propertyGeometry;
	}

	public GroupedValues getPropertyValues() {
		return propertyValues;
	}


}