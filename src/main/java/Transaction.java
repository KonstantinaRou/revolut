import java.util.concurrent.atomic.AtomicInteger;

import com.google.inject.Inject;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import service.BankService;
import service.BankServiceImpl;

import static java.lang.Thread.sleep;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Transaction implements Runnable {


	private BankServiceImpl bank;
	AtomicInteger fromAccount;


	AtomicInteger tempBef;
	AtomicInteger tempAft;
	public void run() {
		while (true) {
			int amount = (int) (Math.random() * 100);
			int wamo0unt = (int)(Math.random() * 100);

			while(wamo0unt>amount){
				wamo0unt = (int)(Math.random() * 100);
			}
			try {
				bank.transfer("Kon","Vas",amount);
			} catch (AccountNoFoundException e) {
				e.printStackTrace();
			} catch (InsufficientBalanceException e) {
				try {
					//sleep(1000);
					log.info("Thread OutofBalance {} {} {}", Thread.currentThread().getName(),
							 bank.getAccount("Kon").getBalance().get(),bank.getAccount("Vas").getBalance().get());
					break;

				} catch (AccountNoFoundException e1) {
					e1.printStackTrace();
				}
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			sleep(1000);
			System.out.println("# "+bank.getAccount("Kon").getBalance().get()+"   " +
								   ""+bank.getAccount("Vas").getBalance().get());
		} catch (InterruptedException | AccountNoFoundException e) {
			e.printStackTrace();
		}
	}
}