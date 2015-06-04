package com.concurrentperformance.fxutils.propertyeditor;

import com.concurrentperformance.util.listener.ConcurrentListenable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;


/**
 * TODO comments ???
 *
 * @author Lake
 */
public abstract class SkeletalPropertyValue extends ConcurrentListenable<PropertyValueListener> implements PropertyValue {

	private final String name;
	private volatile Region editor;
	private boolean selected;

	public SkeletalPropertyValue(String name) {
		this.name = checkNotNull(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public void draw(GraphicsContext gc, double top, double bottom, double left, double right,
	                 double center, double horzPadding, double vertPadding,
	                 Color backgroundColor, Color backgroundColorSelected,
	                 Color linesColor,
	                 Color textColor, Color textColorDisabled, Color textColorSelected) {
		gc.setFill(selected?backgroundColorSelected:backgroundColor);
		gc.fillRect(0,top,right,bottom-top);

		gc.setStroke(linesColor);
		gc.strokeLine(center, top, center, bottom);

		gc.setFill(selected ?textColorSelected: ((editor.isDisabled())?textColorDisabled:textColor));
		gc.fillText(getName(), horzPadding, bottom - vertPadding, center - (horzPadding *2));
	}

	public void setEditor(Region editor) {
		checkState(this.editor == null);
		this.editor = checkNotNull(editor);
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
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setVisible(boolean visible) {
		editor.setVisible(visible);
	}

	@Override
	public void setDisable(boolean disable) {
		editor.setDisable(disable);
		for (PropertyValueListener propertyValueListener : getListeners()) {
			propertyValueListener.propertyValue_renderingChanged(this);
		}
	}

	@Override
	public void positionEditor(double x, double y, double width, double height) {
		getEditor().relocate(x, y);
		getEditor().setPrefSize(width, height);
	}


}
