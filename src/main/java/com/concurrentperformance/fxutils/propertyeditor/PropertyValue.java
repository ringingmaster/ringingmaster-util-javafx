package com.concurrentperformance.fxutils.propertyeditor;

import com.concurrentperformance.util.listener.Listenable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * TODO comments ???
 *
 * @author Lake
 */
public interface PropertyValue extends Listenable<PropertyValueListener> {

	String getName();

	Region getEditor();

	void setFont(Font font);

	void positionEditor(double x, double y, double width, double height);

	void draw(GraphicsContext gc, double top, double bottom, double left, double right,
	          double center, double horzPadding, double vertPadding,
	          Color backgroundColor,
	          Color linesColor,
	          Color textColor, Color textColorDisabled);

	void setVisible(boolean visible);

	void setDisable(boolean disable);

}
