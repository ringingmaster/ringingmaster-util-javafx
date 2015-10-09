package com.concurrentperformance.fxutils.events;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public abstract class SkeletalEventDefinition implements EventDefinition {

	private final String text;
	private final Image image;
	private final SimpleBooleanProperty disableProperty = new SimpleBooleanProperty(true);
	private final SimpleStringProperty tooltipTextProperty = new SimpleStringProperty("");


	public SkeletalEventDefinition(String imageResourceClasspath, String text) {
		InputStream imageResourceAsStream = SkeletalEventDefinition.class.getResourceAsStream(checkNotNull(imageResourceClasspath));
		image = new Image(checkNotNull(imageResourceAsStream));
		this.text = text;
	}

	@Override
	public String getName() {
		return text;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public SimpleBooleanProperty disableProperty() {
		return disableProperty;
	}

	@Override
	public SimpleStringProperty tooltipTextProperty() {
		return tooltipTextProperty;
	}

	@Override
	public List<EventDefinition> getSubItems() {
		return Collections.emptyList();
	}

}
