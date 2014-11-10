package com.concurrentperformance.fxutils.propertyeditor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * TODO comments ???
 *
 * @author Lake
 */
public interface PropertyValue {

	String getName();

	Region getEditor();

	void setFont(Font font);

	void positionEditor(double x, double y, double width, double height);

	void draw(GraphicsContext gc, double top, double bottom, double left, double right,
	          double center, double horzPadding, double vertPadding,
	          Color backgroundColor, Color linesColor, Color textColor);

	void setVisible(boolean visible);

	void setDisable(boolean disable);


}
