package backtracking;

/**
 * Custom exception - Use when file format is incorrect in Backtracking.java
 * @author Ryan Smith
 */
public class ImproperFormatException extends Exception {
	private String message = null;
	
	/**
	 * No argument Constructor
	 */
	public ImproperFormatException() {
	    super();
	}
	
	/**
	 * Constructor with message parameter
	 * @param message - custom error message
	 */
	public ImproperFormatException(String message) {
	    super(message);
	    this.message = message;
	}
	
	@Override
	public String toString() {
	    return message;
	}

}
