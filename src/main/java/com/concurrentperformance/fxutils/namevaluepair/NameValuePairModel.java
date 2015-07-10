package com.concurrentperformance.fxutils.namevaluepair;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class NameValuePairModel {

	public NameValuePairModel(String name) {
		setName(new NameValueColumnDescriptor(name, null, false));
	}


	private final ObjectProperty<NameValueColumnDescriptor> name = new SimpleObjectProperty<>(this, "name");

	public ObjectProperty<NameValueColumnDescriptor> nameProperty() {
		return name;
	}

	public final NameValueColumnDescriptor getName() {
		return this.name.get();
	}

	public final void setName(final NameValueColumnDescriptor name) {
		this.name.set(name);
	}

	private final ObjectProperty<NameValueColumnDescriptor> value = new SimpleObjectProperty<>(this, "value");

	public ObjectProperty<NameValueColumnDescriptor> valueProperty() {
		return value;
	}

	public final NameValueColumnDescriptor getValue() {
		return this.value.get();
	}

	public final void setValue(final NameValueColumnDescriptor value) {
		this.value.set(value);
	}
}
