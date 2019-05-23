package dataAccessV3.events;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.event.Event;
import domainV2.event.EventTag;
import domainV2.question.Question;
import domainV2.question.QuestionMode;
import domainV2.util.Language;
import domainV2.util.Translation;

public class DataAccessEventsImplementation implements DataAccessEvents {
	private EntityManager db;
	private DataAccess dataManager;

	public DataAccessEventsImplementation(EntityManager db, DataAccess dataManager) {
		this.db = db;
		this.dataManager = dataManager;
	}

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
	@Override
	public long storeEvent(String publicName, Calendar deadline, Calendar resolution, Translation eventDescription,
			List<EventTag> tags) {
		Event e = new Event(publicName, deadline, resolution);
		for (EventTag et : tags)
			e.addTag(et);
		e.eventDescription = eventDescription;
		return storeNewEvent(e);
	}

	/**
	 * This function creates a new event without translations
	 * 
	 * @param publicName of the event
	 * @param deadline   of the event
	 * @param resolution of the event
	 */
	@Override
	public long storeNewEvent(String publicName, Calendar deadline, Calendar resolution) {
		Event e = new Event(publicName, deadline, resolution);
		return storeNewEvent(e);
	}

	/**
	 * This function creates a event giving a event
	 * 
	 * @param e event to be stored
	 * @return the identifier of the event
	 */
	@Override
	public long storeNewEvent(Event e) {
		db.getTransaction().begin();
		db.persist(e);
		db.getTransaction().commit();
		System.out.println(e + " has been saved PRINT DE DATA ACCESS EVENT");
		// Create Stats for event
		dataManager.getDataAccessStats().addEventData(e.getID());
		return e.getID();
	}

	/**
	 * This function returns a event given its id
	 * 
	 * @param id of the event
	 * @return event that match that id
	 */
	@Override
	public Event getEventByID(long id) {
		Event events = null;
		try {
			TypedQuery<Event> q2 = db
					.createQuery("SELECT us FROM Event us WHERE us.ID = ?1 ORDER BY us.resolveDate ASC", Event.class);
			q2.setParameter(1, id);
			events = q2.getSingleResult();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns all the events
	 * 
	 * @return list with all the events
	 */
	@Override
	public List<Event> getAllEvents() {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery("SELECT us FROM Event us", Event.class);
			events = q2.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns all the events by tag
	 * 
	 * @param tag of the event
	 * @return list with all the events that match the tag
	 */
	@Override
	public List<Event> getEventsByTag(String tag) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE ?1 MEMBER OF us.tags ORDER BY us.deadlineDate ASC", Event.class);
			q2.setParameter(1, EventTag.valueOf(tag));
			events = q2.getResultList();
		} catch (Exception e) {
			System.out.println("The tag seems not valid");
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns a list with all the events that match several tags
	 * 
	 * @param tags list of tags
	 * @return list of events that match all the tags
	 */
	@Override
	public List<Event> getEventsByMultipleTags(List<EventTag> tags) {
		List<Event> events = new LinkedList<Event>();
		List<Set<Event>> sets = new LinkedList<>();
		String query = "SELECT us FROM Event us WHERE ?1 MEMBER OF us.tags ORDER BY us.resolveDate ASC";
		try {
			for (EventTag t : tags) {
				TypedQuery<Event> q2 = db.createQuery(query, Event.class);
				q2.setParameter(1, t);
				sets.add(new HashSet<Event>(q2.getResultList()));
			}
			for (Set<Event> s : sets)
				sets.get(0).retainAll(s);
			Set<Event> s = sets.get(0);
			events = new LinkedList<Event>(s);
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}

		return events;
	}

	/**
	 * This function returns events by date
	 * 
	 * @param date   date of the event
	 * @param amount number of events to retrieve
	 * @return list with n events that match the date
	 */
	@Override
	public List<Event> getEventsByDate(Calendar date, long amount) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE us.resolveDate >= ?1 ORDER BY us.resolveDate ASC", Event.class);
			q2.setParameter(1, date);
			List<Event> helper = q2.getResultList();
			int j = 1;
			for (Event e : helper) {
				events.add(e);
				if (j == amount) {
					break;
				}
				j++;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns a list of events that match a tag and starts by certain
	 * id
	 * 
	 * @param tag     name of the tag
	 * @param idStart id of the event from we start to retrieve
	 * @param amount  number of events to retrieve
	 * @return list with events that match all the parameters
	 */
	@Override
	public List<Event> getEventsByTagInRange(EventTag tag, long idStart, long amount) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE ?1 MEMBER OF us.tags AND us.ID >= ?2 ORDER BY us.resolveDate ASC",
					Event.class);
			q2.setParameter(1, tag);
			q2.setParameter(2, idStart);
			List<Event> helper = q2.getResultList();
			int j = 1;
			for (Event e : helper) {
				events.add(e);
				if (j == amount) {
					break;
				}
				j++;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns a list of events by resolution and a starting id
	 * 
	 * @param resolution resolution of the event
	 * @param idStart    id of the event from we start to retrieve
	 * @param amount     of events to retrieve
	 * @return list of events that match all the parameters
	 */
	@Override
	public List<Event> getEventByResolutionInRange(boolean resolution, long idStart, long amount) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE us.resolved = ?1 AND us.ID >= ?2 ORDER BY us.resolveDate ASC",
					Event.class);
			q2.setParameter(1, resolution);
			q2.setParameter(2, idStart);
			List<Event> helper = q2.getResultList();
			int j = 1;
			for (Event e : helper) {
				events.add(e);
				if (j == amount) {
					break;
				}
				j++;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
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
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db
					.createQuery("SELECT us FROM Event us WHERE us.ID >= ?1 ORDER BY us.resolveDate ASC", Event.class);
			q2.setParameter(1, startId);
			List<Event> helper = q2.getResultList();
			int j = 1;
			for (Event e : helper) {
				events.add(e);
				if (j == offset) {
					break;
				}
				j++;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns a list of events by cancellation and starting id
	 * 
	 * @param cancel  cancellation of the event
	 * @param idStart id of the event from we start to retrieve
	 * @param amount  of events to retrieve
	 * @return list of events that match all the parameters
	 */
	@Override
	public List<Event> getEventByCancellationInRange(boolean cancel, long idStart, long amount) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE us.cancelled = ?1 AND us.ID >= ?2 ORDER BY us.resolveDate ASC",
					Event.class);
			q2.setParameter(1, cancel);
			q2.setParameter(2, idStart);
			List<Event> helper = q2.getResultList();
			int j = 1;
			for (Event e : helper) {
				events.add(e);
				if (j == amount) {
					break;
				}
				j++;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function a new translation to a event
	 * 
	 * @param id   of the event
	 * @param text if the event
	 * @param lang new language to add
	 */
	@Override
	public void addTranslationToEventDescription(long id, String text, Language lang) {
		Event e = getEventByID(id);
		db.getTransaction().begin();
		e.eventDescription.setTranslationText(lang, text);
		db.getTransaction().commit();
	}

	/**
	 * This function adds a tag to a event
	 * 
	 * @param id  of the event
	 * @param Tag new tag to add
	 */
	@Override
	public void addTagToEvent(long id, String Tag) {
		Event e = getEventByID(id);
		db.getTransaction().begin();
		e.addTag(EventTag.valueOf(Tag));
		db.getTransaction().commit();
	}

	/**
	 * This function removes a tag from a event
	 * 
	 * @param id  of the event
	 * @param Tag the tag to be removed
	 */
	@Override
	public void removeTagFromEvent(long id, String Tag) {
		Event e = getEventByID(id);
		db.getTransaction().begin();
		e.removeTag(EventTag.valueOf(Tag));
		db.getTransaction().commit();
	}

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
	@Override
	public long eventAddQuestionValidSet(long id, Translation questionText, String questionIdentifier,
			List<String> initialValidSet, List<Float> validSetPayouts) {
		Event e = getEventByID(id);
		Question q = new Question(questionText, id, QuestionMode.UseValidSet, null, 0);
		for (int i = 0; i < initialValidSet.size(); i++)
			q.addToSet(initialValidSet.get(i), validSetPayouts.get(i));

		db.getTransaction().begin();
		db.persist(q);
		e.addQuestion(questionIdentifier, q);
		db.getTransaction().commit();
		return q.getId();
	}

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
	@Override
	public long eventAddQuestionNumeric(long id, Translation questionText, String questionIdentifier,
			int[] expectedResult, int maxExpectedScore) {
		Event e = getEventByID(id);
		Question q = new Question(questionText, id, QuestionMode.UseNumericValue, expectedResult, maxExpectedScore);
		db.getTransaction().begin();
		db.persist(q);
		e.addQuestion(questionIdentifier, q);
		db.getTransaction().commit();
		return q.getId();
	}

	/**
	 * This function resolves a event
	 * 
	 * @param id id of event to be resolved
	 */
	@Override
	public void resolveEvent(long id) {
		Event e = getEventByID(id);
		e.getAllQuestions();
		db.getTransaction().begin();
		e.resolveAll();
		db.getTransaction().commit();
		for (Question q : e.getAllQuestions())
			dataManager.getDataAccessQuestions().resolveQuestion(q.getId());
	}

	/**
	 * This function cancels a event
	 * 
	 * @param id id of the event to be cancelled
	 */
	@Override
	public void cancelEvent(long id) {
		Event e = getEventByID(id);
		db.getTransaction().begin();
		e.cancel();
		db.getTransaction().commit();
		for (Question q : e.getAllQuestions())
			dataManager.getDataAccessQuestions().cancelQuestion(q.getId());
	}

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
	@Override
	public void updateEvent(long id, String publicName, Calendar deadline, Calendar resolution,
			Translation eventDescription, List<EventTag> tags) {
		Event e = getEventByID(id);
		db.getTransaction().begin();
		e.publicEventName = publicName;
		e.setDeadlineDate(deadline);
		e.setResolveDate(resolution);
		e.eventDescription = eventDescription;
		e.setTags(tags);
		db.getTransaction().commit();
	}

	/**
	 * This function returns all the events by resolution
	 * 
	 * @param resolution of the event
	 * @return list with all the events by resolution
	 */
	@Override
	public List<Event> getEventByResolution(boolean resolution) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE us.resolved = ?1 ORDER BY us.deadlineDate ASC", Event.class);
			q2.setParameter(1, resolution);
			events = q2.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns n events where can be bet by a tag
	 * 
	 * @param amount number of events to retrieve
	 * @param tag    of the event
	 * @return list with N events where betting is available with tag
	 */
	@Override
	public List<Event> getEventsByCanBet(String tag, int amount) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE us.deadlineDate > ?1 AND ?2 MEMBER OF us.tags AND us.cancelled = ?3 AND us.resolved = ?4 ORDER BY us.resolveDate ASC",
					Event.class);
			q2.setParameter(1, new Date());
			q2.setParameter(2, EventTag.valueOf(tag));
			q2.setParameter(3, false);
			q2.setParameter(4, false);
			int j = 1;
			for (Event e : q2.getResultList()) {
				events.add(e);
				if (j == amount) {
					break;
				}
				j++;
			}
		} catch (Exception e) {

			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns n events where betting is available
	 * 
	 * @param n number of events to retrieve
	 * @return list with n events where betting is available
	 */
	@Override
	public List<Event> getEventsByCanBet(int n) {
		List<Event> events = new LinkedList<Event>();
		try {
			TypedQuery<Event> q2 = db.createQuery(
					"SELECT us FROM Event us WHERE us.deadlineDate > ?1 AND us.cancelled = ?2 AND us.resolved = ?3 ORDER BY us.resolveDate ASC",
					Event.class);
			q2.setParameter(1, new Date());
			q2.setParameter(2, false);
			q2.setParameter(3, false);
			int j = 1;
			for (Event e : q2.getResultList()) {
				events.add(e);
				if (j == n) {
					break;
				}
				j++;
			}
		} catch (Exception e) {

			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

}
