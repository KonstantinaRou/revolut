package service;

import javax.inject.Inject;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import repository.AccountRepository;
import repository.AccountRepositoryImpl;


@Slf4j
public class BankServiceImpl implements BankService {

	private AccountRepository accountRepository;
	@Inject
	public BankServiceImpl(final AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/**{@inheritDoc}*/
	@Override
	public void createAcount(final String name) throws AccountAlreadyExistsException {
		accountRepository.createAcount(name);
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized Account getAccount(final String name) throws AccountNoFoundException {
		return accountRepository.getAccount(name);
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized void withdraw(final String name, final int amount)
		throws AccountNoFoundException, InsufficientBalanceException {
		Account account = accountRepository.getAccount(name);
		accountRepository.withdraw(account,amount);

	}
	/**{@inheritDoc}*/
	@Override
	public synchronized void deposit(final String name, final int amount) throws AccountNoFoundException {
		Account account = accountRepository. getAccount(name);
		accountRepository.deposit(account,amount);
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized void transfer(final String fromName,final String toName, final int amount)
		throws AccountNoFoundException, InsufficientBalanceException {
			withdraw(fromName, amount);
			deposit(toName, amount);
	}


}
