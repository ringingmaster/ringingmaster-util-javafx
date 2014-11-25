package com.concurrentperformance.fxutils.propertyeditor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class GroupedValuesTest {

	GroupedValues groupedValues = new GroupedValues();

	@Test
	public void canAddItemToGroupedValuesAndGroupIsCreated() {


		groupedValues.add("Group1", mock(PropertyValue.class));
		assertEquals(2,groupedValues.sizeCollapsed());

		groupedValues.add("Group1", mock(PropertyValue.class));
		assertEquals(3,groupedValues.sizeCollapsed());

		groupedValues.add("Group2", mock(PropertyValue.class));
		assertEquals(5,groupedValues.sizeCollapsed());

	}
	
	@Test
	public void canCollapseGroupAndSeeItemsReduce() {
		groupedValues.add("Group1", mock(PropertyValue.class));
		groupedValues.add("Group2", mock(PropertyValue.class));

		assertEquals(4,groupedValues.sizeCollapsed());
		assertEquals(4,groupedValues.sizeAll());

		groupedValues.toggleGroupItem(0);
		assertEquals(3,groupedValues.sizeCollapsed());
		assertEquals(4,groupedValues.sizeAll());
	}
}
