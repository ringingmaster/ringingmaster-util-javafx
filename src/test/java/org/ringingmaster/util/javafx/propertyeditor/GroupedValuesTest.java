package org.ringingmaster.util.javafx.propertyeditor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class GroupedValuesTest {

	GroupedValues groupedValues = new GroupedValues();

	@Test
	public void canAddItemToGroupedValuesAndGroupIsCreated() {


		groupedValues.add("Group1", when(mock(PropertyValue.class).getName()).thenReturn("Property1").getMock());
		assertEquals(2,groupedValues.sizeCollapsed());

		groupedValues.add("Group1", when(mock(PropertyValue.class).getName()).thenReturn("Property2").getMock());
		assertEquals(3,groupedValues.sizeCollapsed());

		groupedValues.add("Group2",when(mock(PropertyValue.class).getName()).thenReturn("Property3").getMock());
		assertEquals(5,groupedValues.sizeCollapsed());

	}
	
	@Test
	public void canCollapseGroupAndSeeItemsReduce() {
		groupedValues.add("Group1", when(mock(PropertyValue.class).getName()).thenReturn("Property1").getMock());
		groupedValues.add("Group2", when(mock(PropertyValue.class).getName()).thenReturn("Property2").getMock());

		assertEquals(4,groupedValues.sizeCollapsed());
		assertEquals(4,groupedValues.sizeUncollapsed());

		groupedValues.toggleGroupVisible(0);
		assertEquals(3,groupedValues.sizeCollapsed());
		assertEquals(4,groupedValues.sizeUncollapsed());
	}
}
