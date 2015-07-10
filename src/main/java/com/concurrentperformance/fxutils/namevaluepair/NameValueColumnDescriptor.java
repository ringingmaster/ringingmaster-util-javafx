package com.concurrentperformance.fxutils.namevaluepair;

import javafx.scene.paint.Color;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class NameValueColumnDescriptor {
	private final String text;
	private final Color backgroundColor;
	private final boolean invalid;


	public NameValueColumnDescriptor(String text, Color backgroundColor, boolean invalid) {
		this.invalid = invalid;
		this.text = checkNotNull(text);
		this.backgroundColor = backgroundColor;
	}

	public String getText() {
		return text;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public boolean isInvalid() {
		return invalid;
	}

	@Override
	public String toString() {
		return text;
	}
}