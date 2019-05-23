package domainV2.question;

public class InvalidAnswerValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidAnswerValueException() {
		super();
	}

	/**
	 * This exception is triggered if the answer is not valid
	 * 
	 * @param s String of the exception
	 */
	public InvalidAnswerValueException(String s) {
		super(s);
	}
}
