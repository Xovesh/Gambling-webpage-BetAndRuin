package businessLogicV3.publicAPI.events;

import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import domainV2.event.Event;
import domainV2.event.EventTag;

public class BLFacadePublicEventsImplementation implements BLFacadePublicEvents {

	private BLFacadePrivate privateAPI;

	public BLFacadePublicEventsImplementation(BLFacadePrivate privateAPI) {
		this.privateAPI = privateAPI;
	}

	/**
	 * This function retrieves the popular tags by type
	 * 
	 * @param type of the event
	 * @return list with the tags of popular events
	 */
	@Override
	public List<String> retrievePopularEventsTagsByType(String type) {
		return privateAPI.getEvents().retrievePopularEventsTagsByType(type);
	}

	/**
	 * This function returns all the events by tag
	 * 
	 * @param tag of the event
	 * @return list with all the events that match the tag
	 */
	@Override
	public List<Event> getEventsByTag(String username, String tag) {

		return privateAPI.getEvents().getEventsByTag(tag);

	}

	/**
	 * This function returns a list with all the events that match several tags
	 * 
	 * @param tags list of tags
	 * @return list of events that match all the tags
	 */
	@Override
	public List<Event> getEventsByMultipleTags(List<EventTag> tags) {

		return privateAPI.getEvents().getEventsByMultipleTags(tags);

	}

	/**
	 * This function returns n events by tag
	 * 
	 * @param tag    of the event
	 * @param amount number of to retrieve
	 * @return list with n events that match the tag
	 */
	@Override
	public List<Event> getNEventsByTag(String username, String tag, int amount) {

		return privateAPI.getEvents().getNEventsByTag(tag, amount);

	}

	/**
	 * This function returns all the events by resolution
	 * 
	 * @param resolution of the event
	 * @return list with all the events by resolution
	 */
	@Override
	public List<Event> getEventsByResolution(String username, boolean resolution) {

		return privateAPI.getEvents().getEventsByResolution(resolution);

	}

	/**
	 * This function returns n events by resolution
	 * 
	 * @param resolution of the event
	 * @param amount     amount of events to retrieve
	 * @return list with n events that match the resolution
	 */
	@Override
	public List<Event> getNEventsByResolution(String username, boolean resolution, int amount) {

		return privateAPI.getEvents().getNEventsByResolution(resolution, amount);

	}

	/**
	 * This function returns the events where can be bet by a tag
	 * 
	 * @param tag of the event
	 * @return list of events where betting is available with a tag
	 */
	@Override
	public List<Event> getEventsByCanBet(String username, String tag) {

		return privateAPI.getEvents().getEventsByCanBet(tag);

	}

	/**
	 * This function returns n events where can be bet by a tag
	 * 
	 * @param amount number of events to retrieve
	 * @param tag    of the event
	 * @return list with N events where betting is available with tag
	 */
	@Override
	public List<Event> getNEventsByCanBet(String username, int amount, String tag) {
		return privateAPI.getEvents().getNEventsByCanBet(amount, tag);
	}

	/**
	 * This function returns n events where betting is available
	 * 
	 * @param amount number of events to retrieve
	 * @return list with n events where betting is available
	 */
	@Override
	public List<Event> getNEventsByCanBet(String username, int amount) {
		return privateAPI.getEvents().getNEventsByCanBet(amount);

	}

	/**
	 * This function returns N events by a starting id
	 * 
	 * @param startId the id from we want to retrieve events
	 * @param offset  number of events to retrieve
	 * @return list with events in the given range
	 */

	@Override
	public List<Event> getEventByIdRange(long startId, int offset) {
		return privateAPI.getEvents().getEventByIdRange(startId, offset);
	}

	/**
	 * This function returns a event given its id
	 * 
	 * @param id of the event
	 * @return event that match that id
	 */
	@Override
	public Event getEventByID(long id) {
		return privateAPI.getEvents().getEventByID(id);
	}

}
