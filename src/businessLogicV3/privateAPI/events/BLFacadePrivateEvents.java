package businessLogicV3.privateAPI.events;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import domainV2.event.Event;
import domainV2.event.EventTag;
import domainV2.util.Translation;

public interface BLFacadePrivateEvents {

	/**
	 * This function generates the default popular events
	 */
	public void defaultPopularEventsTags();

	/**
	 * This function retrieves the popular tags by type
	 * 
	 * @param type of the event
	 * @return list with the tags of popular events
	 */
	public List<String> retrievePopularEventsTagsByType(String type);

	/**
	 * This function updates the popular tags of a event type
	 * 
	 * @param type       of the event
	 * @param newPopular list with the new popular tags
	 * @return true if successfully updated
	 */
	public boolean updatePopularEventsTagsByType(String type, List<String> newPopular);

	/**
	 * This function returns all the popular tags
	 * 
	 * @return hashtable with all the popular tags
	 */
	public Hashtable<String, List<String>> retrievePopularEventsTags();

	/**
	 * This function creates a new event without translations
	 * 
	 * @param publicName of the event
	 * @param deadline   of the event
	 * @param resolution of the event
	 */
	public void createNewEvent(String publicName, Calendar deadline, Calendar resolution);

	/**
	 * This function creates a new event
	 * 
	 * @param publicName       of the event
	 * @param deadline         of the event
	 * @param resolution       of the event
	 * @param eventDescription in different languages
	 * @param tags             of the event
	 * @return if of the event
	 */
	public long createEvent(String publicName, Calendar deadline, Calendar resolution, Translation eventDescription,
			List<EventTag> tags);

	/**
	 * This function returns all the events by tag
	 * 
	 * @param tag of the event
	 * @return list with all the events that match the tag
	 */
	public List<Event> getEventsByTag(String tag);

	/**
	 * This function returns n events by tag
	 * 
	 * @param tag    of the event
	 * @param amount number of to retrieve
	 * @return list with n events that match the tag
	 */
	public List<Event> getNEventsByTag(String tag, int amount);

	/**
	 * This function returns events by date
	 * 
	 * @param date   date of the event
	 * @param amount number of events to retrieve
	 * @return list with n events that match the date
	 */
	public List<Event> getEventsByDate(Calendar date, long amount);

	/**
	 * This function returns a list with all the events that match several tags
	 * 
	 * @param tags list of tags
	 * @return list of events that match all the tags
	 */
	public List<Event> getEventsByMultipleTags(List<EventTag> tags);

	/**
	 * This function returns a list of events that match a tag and starts by certain
	 * id
	 * 
	 * @param tag     name of the tag
	 * @param idStart id of the event from we start to retrieve
	 * @param amount  number of events to retrieve
	 * @return list with events that match all the parameters
	 */
	public List<Event> getEventsByTagInRange(EventTag tag, long idStart, long amount);

	/**
	 * This function returns a list of events by resolution and a starting id
	 * 
	 * @param resolution resolution of the event
	 * @param idStart    id of the event from we start to retrieve
	 * @param amount     of events to retrieve
	 * @return list of events that match all the parameters
	 */
	public List<Event> getEventByResolutionInRange(boolean resolution, long idStart, long amount);

	/**
	 * This function returns a list of events by cancellation and starting id
	 * 
	 * @param cancel  cancellation of the event
	 * @param idStart id of the event from we start to retrieve
	 * @param amount  of events to retrieve
	 * @return list of events that match all the parameters
	 */
	public List<Event> getEventByCancellationInRange(boolean cancel, long idStart, long amount);

	/**
	 * This function returns all the events by resolution
	 * 
	 * @param resolution of the event
	 * @return list with all the events by resolution
	 */
	public List<Event> getEventsByResolution(boolean resolution);

	/**
	 * This function returns n events by resolution
	 * 
	 * @param resolution of the event
	 * @param amount     amount of events to retrieve
	 * @return list with n events that match the resolution
	 */
	public List<Event> getNEventsByResolution(boolean resolution, int amount);

	/**
	 * This function returns the events where can be bet by a tag
	 * 
	 * @param tag of the event
	 * @return list of events where betting is available with a tag
	 */
	public List<Event> getEventsByCanBet(String tag);

	/**
	 * This function returns n events where can be bet by a tag
	 * 
	 * @param amount number of events to retrieve
	 * @param tag    of the event
	 * @return list with N events where betting is available with tag
	 */
	public List<Event> getNEventsByCanBet(int amount, String tag);

	/**
	 * This function returns N events by a starting id
	 * 
	 * @param startId the id from we want to retrieve events
	 * @param offset  number of events to retrieve
	 * @return list with events in the given range
	 */
	public List<Event> getEventByIdRange(long startId, int offset);

	/**
	 * This function resolves a event
	 * 
	 * @param eventId id of event to be resolved
	 */
	public void resolveEvent(long eventId);

	/**
	 * This function cancels a event
	 * 
	 * @param eventId if of the event to be cancelled
	 */
	public void cancelEvent(long eventId);

	/**
	 * This function updates the information of a given event
	 * 
	 * @param id               of the event
	 * @param publicName       name of the event
	 * @param deadline         of the event
	 * @param resolution       of the event
	 * @param eventDescription of the event
	 * @param tags             of the event
	 */
	public void updateEvent(long id, String publicName, Calendar deadline, Calendar resolution,
			Translation eventDescription, List<EventTag> tags);

	/**
	 * This function returns a event given its id
	 * 
	 * @param id of the event
	 * @return event that match that id
	 */
	public Event getEventByID(long id);

	/**
	 * This function returns n events where betting is available
	 * 
	 * @param n number of events to retrieve
	 * @return list with n events where betting is available
	 */
	public List<Event> getNEventsByCanBet(int n);
}
