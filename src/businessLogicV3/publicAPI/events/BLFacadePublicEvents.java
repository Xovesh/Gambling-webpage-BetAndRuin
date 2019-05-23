package businessLogicV3.publicAPI.events;

import java.util.List;

import domainV2.event.Event;
import domainV2.event.EventTag;

public interface BLFacadePublicEvents {
	/**
	 * This function retrieves the popular tags by type
	 * 
	 * @param type of the event
	 * @return list with the tags of popular events
	 */
	public List<String> retrievePopularEventsTagsByType(String type);

	/**
	 * This function returns all the events by tag
	 * 
	 * @param username username of the user
	 * @param tag      of the event
	 * @return list with all the events that match the tag
	 */
	public List<Event> getEventsByTag(String username, String tag);

	/**
	 * This function returns a list with all the events that match several tags
	 * 
	 * @param tags list of tags
	 * @return list of events that match all the tags
	 */
	public List<Event> getEventsByMultipleTags(List<EventTag> tags);

	/**
	 * This function returns n events by tag
	 * 
	 * @param username username of the user
	 * @param tag      of the event
	 * @param amount   number of to retrieve
	 * @return list with n events that match the tag
	 */
	public List<Event> getNEventsByTag(String username, String tag, int amount);

	/**
	 * This function returns all the events by resolution
	 * 
	 * @param username   username of the user
	 * @param resolution of the event
	 * @return list with all the events by resolution
	 */
	public List<Event> getEventsByResolution(String username, boolean resolution);

	/**
	 * This function returns n events by resolution
	 * 
	 * @param username   username of the user
	 * @param resolution of the event
	 * @param amount     amount of events to retrieve
	 * @return list with n events that match the resolution
	 */
	public List<Event> getNEventsByResolution(String username, boolean resolution, int amount);

	/**
	 * This function returns the events where can be bet by a tag
	 * 
	 * @param username username of the user
	 * @param tag      of the event
	 * @return list of events where betting is available with a tag
	 */
	public List<Event> getEventsByCanBet(String username, String tag);

	/**
	 * This function returns n events where can be bet by a tag
	 * 
	 * @param username username of the user
	 * @param amount   number of events to retrieve
	 * @param tag      of the event
	 * @return list with N events where betting is available with tag
	 */
	public List<Event> getNEventsByCanBet(String username, int amount, String tag);

	/**
	 * This function returns N events by a starting id
	 * 
	 * @param startId the id from we want to retrieve events
	 * @param offset  number of events to retrieve
	 * @return list with events in the given range
	 */
	public List<Event> getEventByIdRange(long startId, int offset);

	/**
	 * This function returns n events where betting is available
	 * 
	 * @param username username of the user
	 * @param amount   number of events to retrieve
	 * @return list with n events where betting is available
	 */
	public List<Event> getNEventsByCanBet(String username, int amount);

	/**
	 * This function returns a event given its id
	 * 
	 * @param id of the event
	 * @return event that match that id
	 */
	public Event getEventByID(long id);
}
