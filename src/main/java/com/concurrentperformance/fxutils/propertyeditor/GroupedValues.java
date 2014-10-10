package com.concurrentperformance.fxutils.propertyeditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class GroupedValues {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final List<PropertyValue> propertyValuesAll = new ArrayList<>();
	private final List<PropertyValue> propertyValuesCollapsed = new ArrayList<>();
	private final Map<String, GroupPropertyValue> groups = new HashMap<>();

	public GroupPropertyValue add(String groupName, PropertyValue propertyValue) {
		checkNotNull(groupName);
		checkNotNull(propertyValue);

		checkState((findPropertyByName(propertyValue.getName()) == null), "Duplicate property name [%s]", propertyValue.getName());

		GroupPropertyValue group = groups.get(groupName);
		if (group == null) {
			group = new GroupPropertyValue(groupName);
			propertyValuesAll.add(group);
			groups.put(groupName, group);
		}
		propertyValuesAll.add(propertyValue);
		rebuildCollapsedState();
		return group;
	}

	public PropertyValue getAsCollapsed(int index) {
		return propertyValuesCollapsed.get(index);
	}

	public int sizeCollapsed() {
		return propertyValuesCollapsed.size();
	}

	public PropertyValue get(int index) {
		return propertyValuesAll.get(index);
	}

	public int size() {
		return propertyValuesAll.size();
	}

	public void toggleGroupItem(int index) {
		if (index >= propertyValuesCollapsed.size()) {
			return;
		}

		final PropertyValue propertyValue = propertyValuesCollapsed.get(index);
		if (!(propertyValue instanceof GroupPropertyValue)) {
			return;
		}

		((GroupPropertyValue)propertyValue).toggleViewState();
		rebuildCollapsedState();

	}

	private void rebuildCollapsedState() {

		propertyValuesCollapsed.clear();

		boolean groupVisible = false;
		for (PropertyValue propertyValue : propertyValuesAll) {
			if (propertyValue instanceof GroupPropertyValue) {
				GroupPropertyValue groupPropertyValue = (GroupPropertyValue)propertyValue;
				groupVisible = groupPropertyValue.isGroupVisible();
				propertyValuesCollapsed.add(groupPropertyValue);
			}
			else if (groupVisible) {
				propertyValuesCollapsed.add(propertyValue);
			}
		}
	}

	public PropertyValue findPropertyByName(String name) {
		for (PropertyValue propertyValue : propertyValuesAll) {
			if (propertyValue.getName().equals(name)) {
				return propertyValue;
			}
		}
		return null;
	}
}
