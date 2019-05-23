package exceptions;

public class EventFinished extends Exception {
	private static final long serialVersionUID = 1L;

	public EventFinished() {
		super();
	}

	/**
	 * This exception is triggered if the event has already finished
	 * 
	 * @param s String of the exception
	 */
	public EventFinished(String s) {
		super(s);
	}
}