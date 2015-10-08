package com.concurrentperformance.fxutils.menu;

import com.concurrentperformance.fxutils.events.EventDefinition;
import com.concurrentperformance.fxutils.events.SkeletalEventDefinition;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class MenuDefinitionContainer extends MenuDefinitionEvent {

	private final List<MenuDefinition> subItems = new ArrayList<>();

	MenuDefinitionContainer(List<EventDefinition> events, String name) {
		super(new SkeletalEventDefinition("/images/save.png", name) {
			@Override
			public void handle(ActionEvent event) {

			}
		});
		for (EventDefinition event : events) {
			subItems.add(new MenuDefinitionEvent(event));
		}


	}


	@Override
	public List<MenuDefinition> getSubItems() {
		return subItems;
	}

}
