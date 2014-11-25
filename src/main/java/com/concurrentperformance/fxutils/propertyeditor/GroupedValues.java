package com.concurrentperformance.fxutils.propertyeditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class GroupedValues {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static final String UNGROUPED = "<UNGROUPED>";

	private final List<PropertyValue> propertyValuesAll = new ArrayList<>();
	private final List<PropertyValue> propertyValuesCollapsed = new ArrayList<>();

	void add(String groupName, PropertyValue newPropertyValue) {
		checkNotNull(groupName);
		checkNotNull(newPropertyValue);
		checkState((findPropertyByName(newPropertyValue.getName()) == null), "Duplicate property name [%s]", newPropertyValue.getName());

		// Find the index of the last item in the required group. i.e. the one before the next group, or the end
		int insertionIndex = getInsertionIndexInGroup(groupName);
		propertyValuesAll.add(insertionIndex, newPropertyValue);

		rebuildCollapsedState();
	}

	private int getInsertionIndexInGroup(String groupName) {
		int insertionIndex = getFirstGroupChildIndex(groupName);

		for(;insertionIndex<propertyValuesAll.size();insertionIndex++) {
			PropertyValue propertyValue = propertyValuesAll.get(insertionIndex);
			if (propertyValue instanceof GroupPropertyValue) {
				break;
			}
		}
		return insertionIndex;
	}

	int getFirstGroupChildIndex(String groupName) {
		if (groupName.equals(UNGROUPED)) {
			return 0;
		}

		PropertyValue group = findPropertyByName(groupName);
		if (group == null) {
			group = new GroupPropertyValue(groupName);
			propertyValuesAll.add(group);
		}
		return propertyValuesAll.indexOf(group) + 1;
	}

	public PropertyValue getAsCollapsed(int index) {
		return propertyValuesCollapsed.get(index);
	}

	public int sizeCollapsed() {
		return propertyValuesCollapsed.size();
	}

	PropertyValue get(int index) {
		return propertyValuesAll.get(index);
	}

	int sizeAll() {
		return propertyValuesAll.size();
	}

	void toggleGroupItem(int index) {
		if (index >= propertyValuesCollapsed.size()) {
			return;
		}

		final PropertyValue propertyValue = propertyValuesCollapsed.get(index);
		if (!(propertyValue instanceof GroupPropertyValue)) {
			return;
		}

		((GroupPropertyValue) propertyValue).toggleGroupVisible();
		rebuildCollapsedState();

	}

	void showGroupByName(String groupName, boolean show) {
		PropertyValue groupPropertyValue = checkNotNull(findPropertyByName(groupName), "Can't find group [{}] ", groupName);
		checkState(groupPropertyValue instanceof GroupPropertyValue);
		((GroupPropertyValue)groupPropertyValue).setGroupVisible(show);

		rebuildCollapsedState();
	}

	private void rebuildCollapsedState() {

		propertyValuesCollapsed.clear();

		boolean groupVisible = true; // Start with 'true' to handle UNGROUPED items
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

	PropertyValue findPropertyByName(String name) {
		for (PropertyValue propertyValue : propertyValuesAll) {
			if (propertyValue.getName().equals(name)) {
				return propertyValue;
			}
		}
		return null;
	}

	public void clear() {
		propertyValuesAll.clear();
		propertyValuesCollapsed.clear();
	}
}
