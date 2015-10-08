package com.concurrentperformance.fxutils.menu;

import com.concurrentperformance.fxutils.events.EventDefinition;

import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class MenuDefinitionEvent implements MenuDefinition {

	private final EventDefinition event;
	public MenuDefinitionEvent(EventDefinition event) {
		this.event = event;
	}

	@Override
	public EventDefinition getEvent() {
		return event;
	}

	@Override
	public List<MenuDefinition> getSubItems() {
		return null;
	}
}
