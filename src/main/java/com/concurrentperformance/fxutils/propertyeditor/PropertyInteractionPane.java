package com.concurrentperformance.fxutils.propertyeditor;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class PropertyInteractionPane extends Canvas {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private boolean separatorBeingMoved = false;

	private final PropertyEditor propertyEditor;

	PropertyInteractionPane(PropertyEditor propertyEditor) {

		this.propertyEditor = propertyEditor;

		configureMouseHandlers();
	}

	private void configureMouseHandlers() {
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (separatorBeingMoved) {
					final PropertyGeometry propertyGeometry = propertyEditor.getPropertyGeometry();
					propertyGeometry.setVertSeparatorPosition(
							Math.max(Math.min(event.getX(), getWidth() - propertyGeometry.getClosestVertSeparatorCanBeToEdge()), propertyGeometry.getClosestVertSeparatorCanBeToEdge()));
					propertyEditor.updateControl();
				}
			}
		});

		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isOverSeparator(event.getX())) {
					separatorBeingMoved = true;
				}
			}
		});

		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				separatorBeingMoved = false;
			}
		});

		setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isOverSeparator(event.getX()))	{
					setCursor(Cursor.E_RESIZE);
				}
				else {
					setCursor(Cursor.DEFAULT);
				}
			}
		});

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final int propertyIndexAtPosition = getPropertyIndexAtPosition(event.getX(), event.getY());

				propertyEditor.getPropertyValues().toggleGroupItem(propertyIndexAtPosition);
				propertyEditor.updateControl();
			}
		});
	}

	private boolean isOverSeparator(double xPos) {
		final PropertyGeometry propertyGeometry = propertyEditor.getPropertyGeometry();
		return xPos > (propertyGeometry.getVertSeparatorPosition() - propertyGeometry.getVertResizeWidth()) &&
				xPos < (propertyGeometry.getVertSeparatorPosition() + propertyGeometry.getVertResizeWidth());
	}

	private int getPropertyIndexAtPosition(double xPos, double yPos) {
		int index = (int)(yPos / propertyEditor.getPropertyGeometry().getHeight());
		return index;
	}

	void draw() {
		GraphicsContext gc = getGraphicsContext2D();
		clearBackground(gc);
		drawGrid(gc);
	}

	private void clearBackground(GraphicsContext gc) {
		gc.clearRect(0,0,getWidth(), getHeight());
	}

	private void drawGrid(GraphicsContext gc) {
		gc.setLineWidth(1.0);


		final PropertyGeometry propertyGeometry = propertyEditor.getPropertyGeometry();

		final GroupedValues propertyValues = propertyEditor.getPropertyValues();

		final double left = 0.0;
		final double right = getWidth();
		final double center = propertyGeometry.getVertSeparatorPosition();
		final double horzPadding = propertyEditor.getPropertyGeometry().getPadding();
		final double vertPadding =  propertyEditor.getPropertyGeometry().getDescent() + propertyEditor.getPropertyGeometry().getPadding();

		gc.setFont(propertyEditor.getPropertyGeometry().getFont());

		for (int index=0;index<propertyValues.sizeCollapsed();index++) {
			PropertyValue propertyValue = propertyValues.getAsCollapsed(index);

			final double top = (propertyGeometry.getHeight() * (index));
			final double bottom = (propertyGeometry.getHeight() * (index+1));

			propertyValue.draw(gc, top, bottom, left, right, center, horzPadding, vertPadding,
					Color.WHITE, Color.LIGHTGRAY, Color.BLACK);
		}

		//Vertical separator for area where there is no items,.
		final double bottomOfLastProperty = (propertyEditor.getPropertyGeometry().getHeight() * (propertyValues.sizeCollapsed()));
		gc.strokeLine(propertyGeometry.getVertSeparatorPosition(), bottomOfLastProperty, propertyGeometry.getVertSeparatorPosition(), getHeight());


		//Horizontal separators
		for (int index=0;index<propertyValues.sizeCollapsed();index++) {
			final double vertPos = (propertyEditor.getPropertyGeometry().getHeight() * (index+1));
			gc.strokeLine(0.0, vertPos, getWidth(), vertPos);
		}
	}

}
