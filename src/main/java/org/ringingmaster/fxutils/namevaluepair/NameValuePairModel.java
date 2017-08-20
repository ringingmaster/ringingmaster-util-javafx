package org.ringingmaster.fxutils.namevaluepair;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class NameValuePairModel {

	public NameValuePairModel(String name) {
		setName(new NameValueCellDescriptor(name, null, false));
	}

	public NameValuePairModel(String name, String value) {
		setName(new NameValueCellDescriptor(name, null, false));
		setValue(new NameValueCellDescriptor(value, null, false));
	}

	private final ObjectProperty<NameValueCellDescriptor> name = new SimpleObjectProperty<>(this, "name");

	public ObjectProperty<NameValueCellDescriptor> nameProperty() {
		return name;
	}

	public final NameValueCellDescriptor getName() {
		return this.name.get();
	}

	public final void setName(final NameValueCellDescriptor name) {
		this.name.set(name);
	}

	private final ObjectProperty<NameValueCellDescriptor> value = new SimpleObjectProperty<>(this, "value");

	public ObjectProperty<NameValueCellDescriptor> valueProperty() {
		return value;
	}

	public final NameValueCellDescriptor getValue() {
		return this.value.get();
	}

	public final void setValue(final NameValueCellDescriptor value) {
		this.value.set(value);
	}
}
