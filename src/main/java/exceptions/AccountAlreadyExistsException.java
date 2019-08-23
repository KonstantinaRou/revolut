package exceptions;



/**
 * The {@link AccountAlreadyExistsException} exception deals with Balance Excpetions.
 */
public class AccountAlreadyExistsException extends RevolutException {
	/**
	 * Instantiates a new {@link AccountAlreadyExistsException}.
	 *
	 * @param message the message that contains information about the raised exception
	 */
	public AccountAlreadyExistsException(final String message) {
		super(message);
	}
}
