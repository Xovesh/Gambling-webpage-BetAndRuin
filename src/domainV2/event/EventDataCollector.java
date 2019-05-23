package domainV2.event;

public abstract class EventDataCollector {

	protected long apiEventId;
	protected Event event;

	public abstract void generateEventQuestions();

	public abstract void answerGeneratedQuestions() throws Exception;
}
