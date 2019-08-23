

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import repository.AccountRepository;
import repository.AccountRepositoryImpl;
import rest.Controller;
import service.BankService;
import service.BankServiceImpl;

public class Application {

	public static void main(String[] args) {
		Bootique
			.app(args)
			.module(new AppModule())
			.autoLoadModules()
			.exec()
			.exit();
	}

	private static class AppModule implements Module {

		@Override
		public void configure(Binder binder) {
			JerseyModule.extend(binder).addResource(Controller.class);
		}

		@Singleton
		@Provides
		AccountRepository provideAccountRepository() {
			return new AccountRepositoryImpl();
		}

		@Singleton
		@Provides
		BankService provideBankService(AccountRepository accountRepository) {
			return new BankServiceImpl(accountRepository);
		}
	}
}