package com.concurrentperformance.fxutils.menu;

import com.concurrentperformance.fxutils.events.EventDefinition;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public interface MenuDefinition {

	EventDefinition getEvent();
	List<MenuDefinition> getSubItems();

}
