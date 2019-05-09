package org.ringingmaster.util.javafx.propertyeditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * TODO comments ???
 *
 * @author Steve Lake
 */
public class GroupedValues {

    public static final int UNDEFINED_INDEX = -1;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String UNGROUPED = "<UNGROUPED>";

    private final List<PropertyValue> propertyValuesUncollapsed = new ArrayList<>();
    private final List<PropertyValue> propertyValuesCollapsed = new ArrayList<>();

    void add(String groupName, PropertyValue newPropertyValue) {
        checkNotNull(groupName);
        checkNotNull(newPropertyValue);
        checkState((findPropertyByName(newPropertyValue.getName()) == null), "Duplicate property name [%s]", newPropertyValue.getName());

        // Find the index of the last item in the required group. i.e. the one before the next group, or the end
        int insertionIndex = getInsertionIndexInGroup(groupName);
        propertyValuesUncollapsed.add(insertionIndex, newPropertyValue);

        rebuildCollapsedState();
    }

    private int getInsertionIndexInGroup(String groupName) {
        int insertionIndex = getFirstGroupChildIndex(groupName);

        for (; insertionIndex < propertyValuesUncollapsed.size(); insertionIndex++) {
            PropertyValue propertyValue = propertyValuesUncollapsed.get(insertionIndex);
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
            propertyValuesUncollapsed.add(group);
        }
        return propertyValuesUncollapsed.indexOf(group) + 1;
    }

    public PropertyValue getAsCollapsed(int collapsedIndex) {
        return propertyValuesCollapsed.get(collapsedIndex);
    }

    PropertyValue getAsUncollapsed(int uncollapsedIndex) {
        return propertyValuesUncollapsed.get(uncollapsedIndex);
    }

    public int sizeCollapsed() {
        return propertyValuesCollapsed.size();
    }

    int sizeUncollapsed() {
        return propertyValuesUncollapsed.size();
    }

    void showGroupByName(String groupName, boolean show) {
        PropertyValue groupPropertyValue = checkNotNull(findPropertyByName(groupName), "Can't find group [{}] ", groupName);
        checkState(groupPropertyValue instanceof GroupPropertyValue);
        ((GroupPropertyValue) groupPropertyValue).setGroupVisible(show);

        rebuildCollapsedState();
    }

    private void rebuildCollapsedState() {

        propertyValuesCollapsed.clear();

        boolean groupVisible = true; // Start with 'true' to handle UNGROUPED items
        for (PropertyValue propertyValue : propertyValuesUncollapsed) {
            if (propertyValue instanceof GroupPropertyValue) {
                GroupPropertyValue groupPropertyValue = (GroupPropertyValue) propertyValue;
                groupVisible = groupPropertyValue.isGroupVisible();
                propertyValuesCollapsed.add(groupPropertyValue);
            } else if (groupVisible) {
                propertyValuesCollapsed.add(propertyValue);
            }
        }
    }

    PropertyValue findPropertyByName(String name) {
        for (PropertyValue propertyValue : propertyValuesUncollapsed) {
            if (propertyValue.getName().equals(name)) {
                return propertyValue;
            }
        }
        return null;
    }

    public void clear() {
        propertyValuesUncollapsed.clear();
        propertyValuesCollapsed.clear();
    }

    public boolean isGroup(int collapsedIndex) {
        return (getAsCollapsed(collapsedIndex) instanceof GroupPropertyValue);
    }

    public void toggleGroupVisible(int index) {
        if (isGroup(index)) {
            ((GroupPropertyValue) getAsCollapsed(index)).toggleGroupVisible();
            rebuildCollapsedState();
        }
    }

    public int getIndexOf(PropertyValue propertyValue) {
        for (int index = 0; index < propertyValuesCollapsed.size(); index++) {
            if (propertyValuesCollapsed.get(index) == propertyValue) {
                return index;
            }
        }
        return UNDEFINED_INDEX;
    }
}
