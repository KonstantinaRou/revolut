package service;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import model.Account;

/**
 * The {@link BankService} interface contains the definition of all business actions related to bank accounts
 * */
public interface BankService {

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
	Account getAccount(String name) throws AccountNoFoundException;

	/**
	 * Withdraws the amount from the account with the given name if its balance is bigger than the @param amount.
	 *
	 * @param name the name of the account
	 * @param amount the amount we would to withdraw
	 * @throws AccountNoFoundException
	 * @throws InsufficientBalanceException
	 */
	void withdraw(String name,int amount) throws AccountNoFoundException, InsufficientBalanceException;
	/**
	 * Deposits the amount from the account with the given name.
	 *
	 * @param name the name of the account
	 * @param amount the amount we would to withdraw
	 * @throws AccountNoFoundException
	 */
	void deposit(String name, int amount) throws AccountNoFoundException;

	/**
	 * Transfers the given amount from one account to another.
	 *
	 * @param fromName the account name we would like to transfer money from
	 * @param toName the account name we would like to transfer money
	 * @param amount the amount we would like to transfer
	 * @throws AccountNoFoundException
	 * @throws InsufficientBalanceException
	 */
	void transfer(final String fromName,final String toName, final int amount)
		throws AccountNoFoundException, InsufficientBalanceException;
}
