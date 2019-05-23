package dataAccessV3.events;

import java.util.Calendar;
import java.util.List;

import domainV2.event.Event;
import domainV2.event.EventTag;
import domainV2.util.Language;
import domainV2.util.Translation;

public interface DataAccessEvents {

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
	public long storeEvent(String publicName, Calendar deadline, Calendar resolution, Translation eventDescription,
			List<EventTag> tags);

	/**
	 * This function creates a new event without translations
	 * 
	 * @param publicName of the event
	 * @param deadline   of the event
	 * @param resolution of the event return id of the event
	 * @return id of the event
	 */
	public long storeNewEvent(String publicName, Calendar deadline, Calendar resolution);

	/**
	 * This function creates a event giving a event
	 * 
	 * @param e event to be stored
	 * @return the identifier of the event
	 */
	public long storeNewEvent(Event e);

	/**
	 * This function returns a event given its id
	 * 
	 * @param id of the event
	 * @return event that match that id
	 */
	public Event getEventByID(long id);

	/**
	 * This function returns all the events
	 * 
	 * @return list with all the events
	 */
	public List<Event> getAllEvents();

	/**
	 * This function returns all the events by tag
	 * 
	 * @param tag of the event
	 * @return list with all the events that match the tag
	 */
	public List<Event> getEventsByTag(String tag);

	/**
	 * This function returns a list with all the events that match several tags
	 * 
	 * @param tags list of tags
	 * @return list of events that match all the tags
	 */
	public List<Event> getEventsByMultipleTags(List<EventTag> tags);

	/**
	 * This function returns events by date
	 * 
	 * @param date   date of the event
	 * @param amount number of events to retrieve
	 * @return list with n events that match the date
	 */
	public List<Event> getEventsByDate(Calendar date, long amount);

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
	 * This function returns N events by a starting id
	 * 
	 * @param startId the id from we want to retrieve events
	 * @param offset  number of events to retrieve
	 * @return list with events in the given range
	 */
	public List<Event> getEventByIdRange(long startId, int offset);

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
	 * This function a new translation to a event
	 * 
	 * @param id   of the event
	 * @param text if the event
	 * @param lang new language to add
	 */
	public void addTranslationToEventDescription(long id, String text, Language lang);

	/**
	 * This function adds a tag to a event
	 * 
	 * @param id  of the event
	 * @param Tag new tag to add
	 */
	public void addTagToEvent(long id, String Tag);

	/**
	 * This function removes a tag from a event
	 * 
	 * @param id  of the event
	 * @param Tag the tag to be removed
	 */
	public void removeTagFromEvent(long id, String Tag);

	/**
	 * This function adds a valid set question to a event
	 * 
	 * @param id                 of the event
	 * @param questionText       translations of the question
	 * @param questionIdentifier the identifier of the question
	 * @param initialValidSet    the values of the set
	 * @param validSetPayouts    the payoffs of the set
	 * @return id of the question
	 */
	public long eventAddQuestionValidSet(long id, Translation questionText, String questionIdentifier,
			List<String> initialValidSet, List<Float> validSetPayouts);

	/**
	 * This function add a numeric question to a event
	 * 
	 * @param id                 of the event
	 * @param questionText       translations of the event
	 * @param questionIdentifier identifier of the event
	 * @param expectedResult     the expected result
	 * @param maxExpectedScore   the maximum possible score
	 * @return id of the question
	 */
	public long eventAddQuestionNumeric(long id, Translation questionText, String questionIdentifier,
			int[] expectedResult, int maxExpectedScore);

	/**
	 * This function resolves a event
	 * 
	 * @param id id of event to be resolved
	 */
	public void resolveEvent(long id);

	/**
	 * This function cancels a event
	 * 
	 * @param id if of the event to be cancelled
	 */
	public void cancelEvent(long id);

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
	 * This function returns all the events by resolution
	 * 
	 * @param resolution of the event
	 * @return list with all the events by resolution
	 */
	public List<Event> getEventByResolution(boolean resolution);

	/**
	 * This function returns n events where can be bet by a tag
	 * 
	 * @param amount number of events to retrieve
	 * @param tag    of the event
	 * @return list with N events where betting is available with tag
	 */
	public List<Event> getEventsByCanBet(String tag, int amount);

	/**
	 * This function returns n events where betting is available
	 * 
	 * @param n number of events to retrieve
	 * @return list with n events where betting is available
	 */
	public List<Event> getEventsByCanBet(int n);

}
