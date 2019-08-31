import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import repository.AccountRepositoryImpl;
import service.BankServiceImpl;

import static org.junit.Assert.assertEquals;


@Slf4j
public class BankTest {


	private BankServiceImpl bankRepository = new BankServiceImpl(new AccountRepositoryImpl());


	@Test
	public void CreateAccount_Account_CreatesAccount() throws AccountAlreadyExistsException, AccountNoFoundException {
		bankRepository.createAcount("Konstantina");
		Account expectedAccount = new Account("Konstantina");
		assertEquals(expectedAccount,bankRepository.getAccount("Konstantina"));

	}

	@Test(expected = AccountAlreadyExistsException.class)
	public void CreateAccount_ExistingAccountName_AccountAlreadyExistsException() throws AccountAlreadyExistsException {
		bankRepository.createAcount("Konstantina");
		bankRepository.createAcount("Konstantina");
	}

	@Test
	public void Deposit_Amount_AddsAmountToBalance() throws AccountNoFoundException,
		AccountAlreadyExistsException {
		bankRepository.createAcount("Konstantina");
		bankRepository.deposit("Konstantina",20);
		Account expectedAccount = new Account("Konstantina");
		expectedAccount.getBalance().addAndGet(20);
		assertEquals(expectedAccount,bankRepository.getAccount("Konstantina"));
	}


	@Test
	public void Withdraw_Amount_subtractsAmountFromBalance()
		throws AccountNoFoundException, AccountAlreadyExistsException, InsufficientBalanceException {
		bankRepository.createAcount("Konstantina");
		bankRepository.deposit("Konstantina",20);
		bankRepository.withdraw("Konstantina",10);
		Account expectedAccount = new Account("Konstantina");
		expectedAccount.getBalance().addAndGet(10);
		assertEquals(expectedAccount,bankRepository.getAccount("Konstantina"));
	}

	@Test
	public void Transfer_Amount_subtractsAmountFromOneAccountAndAddsItToAnother()
		throws AccountNoFoundException, AccountAlreadyExistsException, InsufficientBalanceException {
		bankRepository.createAcount("Konstantina");
		bankRepository.deposit("Konstantina",20);
		bankRepository.createAcount("Vasilis");
		bankRepository.deposit("Vasilis",20);
		bankRepository.transfer("Konstantina","Vasilis",5);
		Account expectedAccount = new Account("Konstantina");
		expectedAccount.getBalance().addAndGet(15);
		assertEquals(expectedAccount,bankRepository.getAccount("Konstantina"));
	}

	@Test
	public void GetAccount_Account_ReturnsAccount() throws AccountNoFoundException, AccountAlreadyExistsException {
		bankRepository.createAcount("Konstantina");
		Account expectedAccount = new Account("Konstantina");

		assertEquals(expectedAccount,bankRepository.getAccount("Konstantina"));
	}

	@Test(expected = AccountNoFoundException.class)
	public void GetAccount_AccountThatDoesntExit_AccountNoFoundException() throws AccountNoFoundException {
		bankRepository.getAccount("Joe");
	}

	@Test(expected = InsufficientBalanceException.class)
	public void Withdraw_AmountBiggerThanExistingAccountBalance_InsufficientBalanceException()
		throws AccountNoFoundException, InsufficientBalanceException, AccountAlreadyExistsException {
		bankRepository.createAcount("Konstantina");
		bankRepository.deposit("Konstantina",20);
		bankRepository.withdraw("Konstantina",50);
	}


	//  the following two tests create multiple threads that they overlap to test the Thread Safety of the project.
	@Test
	public void createAccount_multipleThreads_createAccounts() throws ExecutionException, InterruptedException {
		int threads = 10;

		List<String> names = new ArrayList<>();


		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean running = new AtomicBoolean();
		AtomicInteger overlaps = new AtomicInteger();
		ExecutorService service = Executors.newFixedThreadPool(threads);
		Collection<Future<String>> futures = new ArrayList<>(threads);

		for (int t = 0; t < threads; ++t) {
			final String title = String.format("Account #%d", t);
			futures.add(
				service.submit(
					() -> {
						latch.await();
						if (running.get()) {
							overlaps.incrementAndGet();
						}
						running.set(true);
						String uuid = UUID.randomUUID().toString();
						bankRepository.createAcount(uuid);
						names.add(uuid);
						running.set(false);
						return uuid;
					}
				)
			);
		}
		latch.countDown();
		Set<String> ids = new HashSet<>();
		for (Future<String> f : futures) {
			ids.add(f.get());
		}
		log.info("Overlaps: {}, id size: {}" ,overlaps.get(),ids.size());
		assertEquals(ids.size(),threads);
	}

	@Test
	public void DepositWithDrawTransfer_multipleThreads_AddsSubtractsTranfersAmmount()
		throws ExecutionException, InterruptedException, AccountNoFoundException, AccountAlreadyExistsException {
		int threads = 1000;
		bankRepository.createAcount("Vasilis");
		bankRepository.createAcount("Konstantina");
		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean running = new AtomicBoolean();
		AtomicInteger overlaps = new AtomicInteger();
		ExecutorService service = Executors.newFixedThreadPool(threads);
		Collection<Future<String>> futures = new ArrayList<>(threads);

		for (int t = 0; t < threads; ++t) {
			futures.add(
				service.submit(
					() -> {
						latch.await();
						if (running.get()) {
							overlaps.incrementAndGet();
						}
						running.set(true);
						try {
							bankRepository.deposit("Konstantina",20);
							bankRepository.deposit("Vasilis",40);
							bankRepository.withdraw("Konstantina",20);
							bankRepository.transfer("Vasilis","Konstantina",10);
						} catch (AccountNoFoundException e) {
							e.printStackTrace();
						}
						running.set(false);
						return UUID.randomUUID().toString();
					}
				)
			);
		}
		latch.countDown();
		Set<String> ids = new HashSet<>();
		for (Future<String> f : futures) {
			ids.add(f.get());
		}
		log.info("Overlaps: {}, id size: {}" ,overlaps.get(),ids.size());
		assertEquals(10000,bankRepository.getAccount("Konstantina").getBalance().get());
	}



}
