package exceptions;


/**
 * The {@link AccountNoFoundException} exception deals with Balance Excpetions.
 */
public class AccountNoFoundException extends RevolutException{


	/**
	 * Instantiates a new {@link AccountNoFoundException}.
	 *
	 * @param message the message that contains information about the raised exception
	 */
	public AccountNoFoundException(final String message) {
		super(message);
	}

}
