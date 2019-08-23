package exceptions;

/**
 * The {@link InsufficientBalanceException} exception deals with Balance Excpetions.
 */
public class InsufficientBalanceException extends RevolutException {

	/**
	 * Instantiates a new {@link InsufficientBalanceException}.
	 *
	 * @param message the message that contains information about the raised exception
	 */
	public InsufficientBalanceException(final String message) {
		super(message);
	}

}
