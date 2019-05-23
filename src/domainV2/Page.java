package domainV2;

import java.util.List;

import domainV2.event.Event;

public class Page {
	private User user;
	String eventTypeMenu;
	String eventType;
	String eventSpecification;
	List<Event> events;
	List<Event> firstTagGroup;
	List<Event> secondTagGroup;
	String firstTag;
	String secondTag;
	String language;
	
	public Page(User user, String eventTypeMenu, String eventType, String eventSpecification, List<Event> events,
			List<Event> firstTagGroup, List<Event> secondTagGroup, String firstTag, String secondTag, String language) {
		super();
		this.user = user;
		this.eventTypeMenu = eventTypeMenu;
		this.eventType = eventType;
		this.eventSpecification = eventSpecification;
		this.events = events;
		this.firstTagGroup = firstTagGroup;
		this.secondTagGroup = secondTagGroup;
		this.firstTag = firstTag;
		this.secondTag = secondTag;
		this.language = language;
		

	}
	
	public Page() {		System.out.println("WAS CREATED");}
	
	public void update(User user, String eventTypeMenu, String eventType, String eventSpecification, List<Event> events,
			List<Event> firstTagGroup, List<Event> secondTagGroup, String firstTag, String secondTag, String language) {
		this.user = user;
		this.eventTypeMenu = eventTypeMenu;
		this.eventType = eventType;
		this.eventSpecification = eventSpecification;
		this.events = events;
		this.firstTagGroup = firstTagGroup;
		this.secondTagGroup = secondTagGroup;
		this.firstTag = firstTag;
		this.secondTag = secondTag;
		this.language = language;
		
		System.out.println("WAS UPDATED");
	}

	public User getUser() {
		return user;
	}

	public String getEventTypeMenu() {
		return eventTypeMenu;
	}

	public String getEventType() {
		return eventType;
	}

	public String getEventSpecification() {
		return eventSpecification;
	}

	public List<Event> getEvents() {
		return events;
	}

	public List<Event> getFirstTagGroup() {
		return firstTagGroup;
	}

	public List<Event> getSecondTagGroup() {
		return secondTagGroup;
	}

	public String getFirstTag() {
		return firstTag;
	}

	public String getSecondTag() {
		return secondTag;
	}

	public String getLanguage() {
		return language;
	}
	
	
	
	
}
