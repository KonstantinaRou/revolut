package model;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The {@link Account} class holds the data of each Account
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
	/**
	 * The balance of the account
	 * */
	final AtomicInteger balance = new AtomicInteger(0);

	/**
	 * The name of the account
	 * */
	String name;

	@Override
	public boolean equals(final Object o) {
		if(o == null)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof Account))
			return false;
		final Account account = (Account) o;
		return balance.get() == account.balance.get() && Objects.equals(name, account.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(balance, name);
	}


}
