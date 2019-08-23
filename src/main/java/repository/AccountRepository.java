package repository;

import java.util.Map;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import model.Account;

/**
 * The {@link AccountRepository} interface contains the definition of all data actions related to the account object
 * */
public interface AccountRepository {

	/**
	 * Withdraws the @amount from {@link Account}
	 *
	 * @param account The account we would like to withdraw
	 * @param amount The amount we would like to withdraw
	 **/
	void withdraw(Account account,int amount) throws InsufficientBalanceException, InsufficientBalanceException;
	/**
	 * Deposits the @amount from {@link Account}
	 *
	 * @param account The account we would like to deposit
	 * @param amount The amount we would like to deposit
	 **/
	void deposit(Account account,int amount);

	/**
	 * Returns a {@link Boolean} if the account with the given name exists.
	 *
	 * @param name the name of the account
	 * @return {@link Boolean}
	 */
	Boolean existsAccount(String name) ;

	/***
	 * Creates a new {@link Account} entity and stores it.
	 *
	 * @param name the name of the account
	 * @throws AccountAlreadyExistsException
	 */
	void createAcount(String name) throws AccountAlreadyExistsException;

	/**
	 * Returns the {@link Account} if the account with the given name exists.
	 *
	 * @param name the name of the account
	 * @return the account if it exists
	 * @throws AccountNoFoundException
	 */
	Account getAccount(final String name) throws AccountNoFoundException;

}
