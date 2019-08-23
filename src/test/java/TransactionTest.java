import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import repository.AccountRepositoryImpl;
import service.BankServiceImpl;

import static java.lang.Thread.sleep;

public class TransactionTest {
	public static void main(String[] args) throws AccountAlreadyExistsException, AccountNoFoundException{
		BankServiceImpl bank = new BankServiceImpl(new AccountRepositoryImpl());
		bank.createAcount("Kon");
		bank.createAcount("Vas");
		bank.deposit("Kon",10000);
		for (int i = 0; i <15; i++) {
			Thread t = new Thread(new Transaction(bank,new AtomicInteger(0),new AtomicInteger(0),new AtomicInteger(0)));
			t.start();
		}

	}
}