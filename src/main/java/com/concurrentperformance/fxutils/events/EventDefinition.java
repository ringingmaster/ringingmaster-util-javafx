package com.concurrentperformance.fxutils.events;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public interface EventDefinition {

	String getName();
	Image getImage();

	List<EventDefinition> getSubItems();

	void handle(ActionEvent event);

	ObservableBooleanValue disableProperty();
	ObservableStringValue tooltipTextProperty();
}
