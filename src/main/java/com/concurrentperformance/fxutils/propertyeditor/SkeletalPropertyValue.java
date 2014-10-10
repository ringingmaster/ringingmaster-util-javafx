package com.concurrentperformance.fxutils.propertyeditor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * TODO comments ???
 *
 * @author Lake
 */
public abstract class SkeletalPropertyValue implements PropertyValue {

	private final String name;
	private volatile Region editor;

	public SkeletalPropertyValue(String name) {
		this.name = checkNotNull(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public void draw(GraphicsContext gc, double top, double bottom, double left, double right,
	                 double center, double horzPadding, double vertPadding,
	                 Color backgroundColor, Color linesColor, Color textColor) {
		gc.setFill(backgroundColor);
		gc.fillRect(0,top,right,bottom-top);

		gc.setStroke(linesColor);
		gc.strokeLine(center, top, center, bottom);

		gc.setFill(textColor);
		gc.fillText(getName(), horzPadding, bottom - vertPadding, center - (horzPadding *2));
	}

	public void setEditor(Region editor) {
		this.editor = editor;
	}

	@Override
	public Region getEditor() {
		return editor;
	}


	@Override
	public void setFont(Font font) {
		// Do nothing - just for override.
	}

	@Override
	public void updateVisibleState(boolean visible) {
		editor.setVisible(visible);
	}

	@Override
	public void positionEditor(double x, double y, double width, int height) {
		getEditor().relocate(x, y);
		getEditor().setPrefSize(width, height);
	}


}
