package com.concurrentperformance.fxutils.propertyeditor;

/**
 * TODO Comments
 *
 * @author Lake
 */
public interface PropertyValueListener {
	void propertyValue_renderingChanged(PropertyValue propertyValue);
	void propertyValue_editorSelected(PropertyValue propertyValue);
	void propertyValue_doubleClick(PropertyValue propertyValue);

}
