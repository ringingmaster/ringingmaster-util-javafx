package com.concurrentperformance.fxutils.namevaluepair;

import javafx.scene.paint.Color;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class ColumnDescriptor {
	private final String text;
	private final Color backgroundColor;


	public ColumnDescriptor(String text, Color backgroundColor) {
		this.text = checkNotNull(text);
		this.backgroundColor = backgroundColor;
	}

	public String getText() {
		return text;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public String toString() {
		return text;
	}
}