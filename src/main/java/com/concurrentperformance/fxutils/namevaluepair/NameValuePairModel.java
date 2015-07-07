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
		setName(new ColumnDescriptor(name, null));
	}


	private final ObjectProperty<ColumnDescriptor> name = new SimpleObjectProperty<>(this, "name");

	public ObjectProperty<ColumnDescriptor> nameProperty() {
		return name;
	}

	public final ColumnDescriptor getName() {
		return this.name.get();
	}

	public final void setName(final ColumnDescriptor name) {
		this.name.set(name);
	}

	private final ObjectProperty<ColumnDescriptor> value = new SimpleObjectProperty<>(this, "value");

	public ObjectProperty<ColumnDescriptor> valueProperty() {
		return value;
	}

	public final ColumnDescriptor getValue() {
		return this.value.get();
	}

	public final void setValue(final ColumnDescriptor value) {
		this.value.set(value);
	}
}
