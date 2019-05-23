package domainV2.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import domainV2.question.Question;
import domainV2.util.Language;
import domainV2.util.Translation;

@Entity
public class Event {
	// Event administration data
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;
	private String eventIdentifier;
	public String publicEventName;
	@Embedded
	public Translation eventDescription;
	private boolean resolved = false, cancelled = false;
	private Calendar deadlineDate, resolveDate;
	// Event questions
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Map<String, Question> questions;
	// Event tags
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<EventTag> tags;

	// Constructors
	public Event(String publicName, Calendar deadline, Calendar resolution) {
		questions = new HashMap<String, Question>();
		tags = new HashSet<EventTag>();
		publicEventName = publicName;
		eventDescription = new Translation("");
		deadlineDate = deadline;
		resolveDate = resolution;
	}

	// Question handling
	public void addQuestion(String identifier, Question q) {
		questions.put(identifier, q);
	}

	public Question getQuestion(String identifier) {
		return questions.get(identifier);
	}

	public List<Question> getAllQuestions() {
		return new LinkedList<>(questions.values());
	}

	public void resolveAll() {
		for (Question q : new LinkedList<>(questions.values()))
			q.notifyObservers();
		resolved = true;
	}

	// Tag handling
	public void addTag(EventTag tag) {
		tags.add(tag);
	}

	public void removeTag(EventTag tag) {
		tags.remove(tag);
	}

	public List<EventTag> getTags() {
		return new LinkedList<EventTag>(tags);
	}

	// Utility

	public void cancel() {
		this.cancelled = true;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public long getID() {
		return ID;
	}

	public String getEventIdentifier() {
		return eventIdentifier;
	}

	public Calendar getDeadlineDate() {
		return deadlineDate;
	}

	public Calendar getResolveDate() {
		return resolveDate;
	}

	public boolean isResolved() {
		return resolved;
	}

	public String serialize() {
		String s = "";
		s += "Event name: " + publicEventName + "\n";
		s += "Event Description: " + eventDescription.getTranslationText(Language.EN) + "\n";
		s += "Last date to bet: " + deadlineDate + " Event resolves: " + resolveDate + "\n";
		s += "Event Tags: ";
		for (EventTag tag : tags)
			s += tag + " ";
		s += "\n";
		s += "Event Questions: \n";
		for (String question : questions.keySet())
			s += "\t" + question + "\n";
		return s;
	}

	public boolean allQuestionAnswerd() {
		for (Question q : getAllQuestions()) {
			if (q.isAnswered()) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public String tagsToString() {
		String s = "";
		for (EventTag t : getTags()) {
			s += t.toString() + ";";
		}
		return s;
	}

	public String deadlineToString() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		String formatted = format1.format(this.deadlineDate.getTime());
		return formatted;
	}

	public String resolveToString() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		String formatted = format1.format(this.resolveDate.getTime());
		return formatted;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public void setDeadlineDate(Calendar deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public void setResolveDate(Calendar resolveDate) {
		this.resolveDate = resolveDate;
	}

	public void setTags(List<EventTag> tags) {
		for (EventTag t : tags)
			this.tags.add(t);
	}

}
