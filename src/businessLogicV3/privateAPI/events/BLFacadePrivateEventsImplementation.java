package businessLogicV3.privateAPI.events;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;
import domainV2.event.Event;
import domainV2.event.EventTag;
import domainV2.util.Translation;

public class BLFacadePrivateEventsImplementation implements BLFacadePrivateEvents {

	private DataAccess dataManager;
	private BLFacadePrivate privateAPI;
	private Hashtable<String, List<String>> popularEvents;

	public BLFacadePrivateEventsImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.dataManager = dataManager;
		this.privateAPI = privateAPI;
		this.popularEvents = new Hashtable<String, List<String>>();
		defaultPopularEventsTags();
	}

	/**
	 * This function generates the default popular events
	 */
	@Override
	public void defaultPopularEventsTags() {
		List<String> soccer = new LinkedList<String>();
		soccer.add("LaLiga");
		soccer.add("Calcio");
		popularEvents.put("Soccer", soccer);
		popularEvents.put("Basketball", new LinkedList<String>());
		popularEvents.put("Baseball", new LinkedList<String>());
		popularEvents.put("Football", new LinkedList<String>());
		popularEvents.put("Cycling", new LinkedList<String>());
		popularEvents.put("Politics", new LinkedList<String>());
		popularEvents.put("Movies", new LinkedList<String>());
	}

	/**
	 * This function retrieves the popular tags by type
	 * 
	 * @param type
	 *            of the event
	 * @return list with the tags of popular events
	 */
	@Override
	public List<String> retrievePopularEventsTagsByType(String type) {
		return popularEvents.get("type");
	}

	/**
	 * This function updates the popular tags of a event type
	 * 
	 * @param type
	 *            of the event
	 * @param newPopular
	 *            list with the new popular tags
	 * @return true if successfully updated
	 */
	@Override
	public boolean updatePopularEventsTagsByType(String type, List<String> newPopular) {
		if (popularEvents.containsKey(type)) {
			popularEvents.replace(type, newPopular);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This function returns all the popular tags
	 * 
	 * @return hashtable with all the popular tags
	 */
	@Override
	public Hashtable<String, List<String>> retrievePopularEventsTags() {
		return popularEvents;
	}

	/**
	 * This function creates a new event without translations
	 * 
	 * @param publicName
	 *            of the event
	 * @param deadline
	 *            of the event
	 * @param resolution
	 *            of the event
	 */
	@Override
	public void createNewEvent(String publicName, Calendar deadline, Calendar resolution) {
		dataManager.getDataAccessEvents().storeNewEvent(publicName, deadline, resolution);
	}

	/**
	 * This function creates a new event
	 * 
	 * @param publicName
	 *            of the event
	 * @param deadline
	 *            of the event
	 * @param resolution
	 *            of the event
	 * @param eventDescription
	 *            in different languages
	 * @param tags
	 *            of the event
	 * @return if of the event
	 */
	@Override
	public long createEvent(String publicName, Calendar deadline, Calendar resolution, Translation eventDescription,
			List<EventTag> tags) {
		long out;
		out = dataManager.getDataAccessEvents().storeEvent(publicName, deadline, resolution, eventDescription, tags);
		return out;
	}

	/**
	 * This function returns all the events by tag
	 * 
	 * @param tag
	 *            of the event
	 * @return list with all the events that match the tag
	 */
	@Override
	public List<Event> getEventsByTag(String tag) {
		return dataManager.getDataAccessEvents().getEventsByTag(tag);
	}

	/**
	 * This function returns n events by tag
	 * 
	 * @param tag
	 *            of the event
	 * @param amount
	 *            number of to retrieve
	 * @return list with n events that match the tag
	 */
	@Override
	public List<Event> getNEventsByTag(String tag, int amount) {
		List<Event> result = null;
		result = dataManager.getDataAccessEvents().getEventsByTag(tag);
		try {
			return result.subList(0, (amount > result.size()) ? result.size() - 1 : amount - 1);
		} catch (Exception e) {
			return new LinkedList<Event>();
		}
	}

	/**
	 * This function returns events by date
	 * 
	 * @param date
	 *            date of the event
	 * @param amount
	 *            number of events to retrieve
	 * @return list with n events that match the date
	 */
	@Override
	public List<Event> getEventsByDate(Calendar date, long amount) {
		return dataManager.getDataAccessEvents().getEventsByDate(date, amount);
	}

	/**
	 * This function returns a list with all the events that match several tags
	 * 
	 * @param tags
	 *            list of tags
	 * @return list of events that match all the tags
	 */
	@Override
	public List<Event> getEventsByMultipleTags(List<EventTag> tags) {
		return dataManager.getDataAccessEvents().getEventsByMultipleTags(tags);
	}

	/**
	 * This function returns a list of events that match a tag and starts by certain
	 * id
	 * 
	 * @param tag
	 *            name of the tag
	 * @param idStart
	 *            id of the event from we start to retrieve
	 * @param amount
	 *            number of events to retrieve
	 * @return list with events that match all the parameters
	 */
	@Override
	public List<Event> getEventsByTagInRange(EventTag tag, long idStart, long amount) {
		return dataManager.getDataAccessEvents().getEventsByTagInRange(tag, idStart, amount);
	}

	/**
	 * This function returns a list of events by resolution and a starting id
	 * 
	 * @param resolution
	 *            resolution of the event
	 * @param idStart
	 *            id of the event from we start to retrieve
	 * @param amount
	 *            of events to retrieve
	 * @return list of events that match all the parameters
	 */
	@Override
	public List<Event> getEventByResolutionInRange(boolean resolution, long idStart, long amount) {
		return dataManager.getDataAccessEvents().getEventByResolutionInRange(resolution, idStart, amount);
	}

	/**
	 * This function returns a list of events by cancellation and starting id
	 * 
	 * @param cancel
	 *            cancellation of the event
	 * @param idStart
	 *            id of the event from we start to retrieve
	 * @param amount
	 *            of events to retrieve
	 * @return list of events that match all the parameters
	 */
	@Override
	public List<Event> getEventByCancellationInRange(boolean cancel, long idStart, long amount) {
		return dataManager.getDataAccessEvents().getEventByCancellationInRange(cancel, idStart, amount);
	}

	/**
	 * This function returns all the events by resolution
	 * 
	 * @param resolution
	 *            of the event
	 * @return list with all the events by resolution
	 */
	@Override
	public List<Event> getEventsByResolution(boolean resolution) {
		return dataManager.getDataAccessEvents().getEventByResolution(resolution);
	}

	/**
	 * This function returns n events by resolution
	 * 
	 * @param resolution
	 *            of the event
	 * @param amount
	 *            amount of events to retrieve
	 * @return list with n events that match the resolution
	 */
	@Override
	public List<Event> getNEventsByResolution(boolean resolution, int amount) {
		List<Event> result = null;

		result = dataManager.getDataAccessEvents().getEventByResolution(resolution);
		try {
			return result.subList(0, (amount > result.size()) ? result.size() - 1 : amount - 1);
		} catch (Exception e) {
			return new LinkedList<Event>();
		}
	}

	/**
	 * This function returns the events where can be bet by a tag
	 * 
	 * @param tag
	 *            of the event
	 * @return list of events where betting is available with a tag
	 */
	@Override
	public List<Event> getEventsByCanBet(String tag) {
		return dataManager.getDataAccessEvents().getEventsByCanBet(tag, -1);
	}

	/**
	 * This function returns n events where can be bet by a tag
	 * 
	 * @param amount
	 *            number of events to retrieve
	 * @param tag
	 *            of the event
	 * @return list with N events where betting is available with tag
	 */
	@Override
	public List<Event> getNEventsByCanBet(int amount, String tag) {
		return dataManager.getDataAccessEvents().getEventsByCanBet(tag, amount);

	}

	/**
	 * This function returns n events where betting is available
	 * 
	 * @param n
	 *            number of events to retrieve
	 * @return list with n events where betting is available
	 */
	@Override
	public List<Event> getNEventsByCanBet(int n) {
		return dataManager.getDataAccessEvents().getEventsByCanBet(n);

	}

	/**
	 * This function returns N events by a starting id
	 * 
	 * @param startId
	 *            the id from we want to retrieve events
	 * @param offset
	 *            number of events to retrieve
	 * @return list with events in the given range
	 */
	@Override
	public List<Event> getEventByIdRange(long startId, int offset) {
		return dataManager.getDataAccessEvents().getEventByIdRange(startId, offset);
	}

	/**
	 * This function resolves a event
	 * 
	 * @param eventId
	 *            id of event to be resolved
	 */
	@Override
	public void resolveEvent(long eventId) {
		dataManager.getDataAccessEvents().resolveEvent(eventId);
		privateAPI.getStats().updateDataWithEvent(eventId);
	}

	/**
	 * This function cancels a event
	 * 
	 * @param eventId
	 *            if of the event to be cancelled
	 */
	@Override
	public void cancelEvent(long eventId) {
		dataManager.getDataAccessEvents().cancelEvent(eventId);
	}

	/**
	 * This function updates the information of a given event
	 * 
	 * @param id
	 *            of the event
	 * @param publicName
	 *            name of the event
	 * @param deadline
	 *            of the event
	 * @param resolution
	 *            of the event
	 * @param eventDescription
	 *            of the event
	 * @param tags
	 *            of the event
	 */
	@Override
	public void updateEvent(long id, String publicName, Calendar deadline, Calendar resolution,
			Translation eventDescription, List<EventTag> tags) {
		dataManager.getDataAccessEvents().updateEvent(id, publicName, deadline, resolution, eventDescription, tags);
	}

	/**
	 * This function returns a event given its id
	 * 
	 * @param id
	 *            of the event
	 * @return event that match that id
	 */
	@Override
	public Event getEventByID(long id) {
		return dataManager.getDataAccessEvents().getEventByID(id);
	}
}
