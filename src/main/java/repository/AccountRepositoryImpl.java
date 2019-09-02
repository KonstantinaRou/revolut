package repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import model.Account;

@Slf4j
public class AccountRepositoryImpl implements AccountRepository {


	private Map<String, Account> accountHashMap = new HashMap<>();

	public Map<String, Account> getMap(){
		return accountHashMap;
	}

	/**{@inheritDoc}*/
	@Override
	public  synchronized void createAcount(String name) throws AccountAlreadyExistsException {
		if (!existsAccount(name)){
			accountHashMap.put(name, new Account(name));
		}
		else {
			throw new AccountAlreadyExistsException(String.format("Account %s already exists", name));
		}
	}

	/**{@inheritDoc}*/
	@Override
	public  Account getAccount(final String name) throws AccountNoFoundException {
		if (existsAccount(name)) {
			return accountHashMap.get(name);
		}
		else {
			throw new AccountNoFoundException(String.format("Account %s doesnt exist", name));
		}
	}
	/**{@inheritDoc}*/
	@Override
	public Boolean existsAccount(String name) {
		return accountHashMap.containsKey(name);
	}


	/**{@inheritDoc}*/
	@Override
	public synchronized void withdraw(final Account account, final int balance) throws InsufficientBalanceException {
		if (balance<=account.getBalance()){
			int newBalance = account.getBalance()-balance;
			account.setBalance(newBalance);

		}
		else{
			throw new InsufficientBalanceException(String.format("Insufficient balance for %s", account.getName()));
		}
	}
	/**{@inheritDoc}*/
	@Override
	public synchronized void deposit(final Account account, final int balance) {
		int newBalance = account.getBalance()+balance;
		account.setBalance(newBalance);
	}


}
