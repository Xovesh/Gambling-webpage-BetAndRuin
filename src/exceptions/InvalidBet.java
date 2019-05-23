package exceptions;

public class InvalidBet extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidBet() {
		super();
	}

	/**
	 * This exception is thrown when the bet can not be made
	 * 
	 * @param s String of the exception
	 */
	public InvalidBet(String s) {
		super(s);
	}

}
