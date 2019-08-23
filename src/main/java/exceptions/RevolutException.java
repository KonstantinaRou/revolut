package exceptions;


/**
 * The {@link RevolutException} exception is the parent of all custom exceptions used in defining our inter-class
 * communication layer.
 */
public class RevolutException extends Exception{
	/**
	 * The message associated with the exception.
	 */
	private String message;

	/**
	 * Instantiates a new {@link RevolutException} exception.
	 *
	 * @param message the log message
	 */
	public RevolutException(final String message) {
		super(message);
	}

}

