package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

public class PerformerDto extends PersonDto {
	private String name;
//	private List<EventDto> eventsAttended;
	private List<EventDto> events;
	
	public PerformerDto() {
	}
	//@SuppressWarnings("unchecked")
	public PerformerDto(String name) {
		this(name, Collections.EMPTY_LIST);
	}
	public PerformerDto(String name, List<EventDto> events) {
		this.setName(name);
		this.events = events;
	}
//	public PerformerDto(String name, List<EventDto> eventsA, List<EventDto> events) {
//		this.setName(name);
//		this.eventsAttended = eventsA;
//		this.events = events;
//	}
	public List<EventDto> getEvents() {
		return events;
	}

	public void setEvents(List<EventDto> events) {
		this.events = events;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
