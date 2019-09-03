import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import io.bootique.command.CommandOutcome;
import io.bootique.jersey.JerseyModule;
import io.bootique.test.junit.BQTestFactory;
import io.restassured.RestAssured;
import repository.AccountRepository;
import repository.AccountRepositoryImpl;
import rest.Controller;
import service.BankService;
import service.BankServiceImpl;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;


public class BankIntegrationTest  {


	@Rule
	public BQTestFactory testFactory = new BQTestFactory();

	@Before
	public void setup() {

		RestAssured.port=8080;

		CommandOutcome runtime = testFactory.app("--server")
											// ensure all classpath modules are included
											.autoLoadModules()
											// add an adhoc module specific to the test
											.module(binder -> binder.bind(BankService.class).to(BankServiceImpl.class))
											.module(binder -> binder.bind(AccountRepository.class).to(AccountRepositoryImpl.class))
											.module(binder->JerseyModule.extend(binder).addResource(Controller.class)).run();
	}



	@Test
	public void GetAccount_NoValidAccount_NotFoundStatus(){
		when().get("revolut/Konstantina/accounts").then().statusCode(404);
	}

	@Test
	public void GetAccount_ValidAccount_FoundStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		when().get("revolut/accounts/Konstantina").then().statusCode(200);
	}

	@Test
	public void GetAccount_ValidAccount_ResponseWithAccount(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		when().get("revolut/accounts/Konstantina").then().assertThat().body(equalTo("Account(balance=0, " +
																						"name=Konstantina)"));
	}

	@Test
	public void CreateAccount_ValidAccount_CreatedStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts").then().statusCode(201);
	}

	@Test
	public void CreateAccount_ExistingAccount_BaDRequestStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("name","Konstantina").when().post("revolut/accounts").then().statusCode(400);
	}

	@Test
	public void Deposit_ValidAmountValidAccount_OKStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit").then().statusCode(200);

	}


	@Test
	public void Deposit_ValidAmountValidAccount_AddsAmountToBalanceOfAccount(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		when().get("revolut/accounts/Konstantina").then().assertThat().body(equalTo("Account(balance=20, " +
																						"name=Konstantina)"));
	}
	@Test
	public void Deposit_ValidAmountNotValidAccount_NotFoundStatus(){
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit").then().statusCode(404);

	}

	@Test
	public void Withdraw_ValidAccountValidAmount_OkStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/withdraw").then().statusCode(200);

	}

	@Test
	public void Withdraw_ValidAccountValidAmount_SubtractsAmountFromBalanceOfAccount(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",50).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/withdraw");
		when().get("revolut/accounts/Konstantina").then().assertThat().body(equalTo("Account(balance=30, " +
																						"name=Konstantina)"));
	}

	@Test
	public void Withdraw_NoValidAccountValidAmount_NoFoundStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",20).when().post("revolut/accounts/Vasilis/withdraw").then().statusCode(404);
	}

	@Test
	public void Withdraw_ValidAccountNoValidAmount_BadRequestStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",50).when().post("revolut/accounts/Konstantina/withdraw").then().statusCode(400);
	}

	@Test
	public void Withdraw_NoValidAccountNoValidAmount_NoFoundStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",50).when().post("revolut/accounts/Vasilis/withdraw").then().statusCode(404);
	}

	@Test
	public void Transfer_ValidFromAccountValidToAccountValidAmount_OkStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("name","Vasilis").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",10).when().post("revolut/accounts/Konstantina/transfer/Vasilis").then().statusCode(200);

	}

	@Test
	public void Transfer_ValidFromAccountValidToAccountValidAmount_TransferAmountFromOneAccountToOther(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("name","Vasilis").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",10).when().post("revolut/accounts/Konstantina/transfer/Vasilis");
		when().get("revolut/accounts/Konstantina").then().assertThat().body(equalTo("Account(balance=10, " +
																						"name=Konstantina)"));
		when().get("revolut/accounts/Vasilis").then().assertThat().body(equalTo("Account(balance=10, " +
																						"name=Vasilis)"));
	}

	@Test
	public void Transfer_NoValidFromAccountValidToAccountValidAmount_NoFoundStatus(){
		given().queryParam("name","Vasilis").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",10).when().post("revolut/accounts/Konstantina/transfer/Vasilis").then().statusCode(404);

	}

	@Test
	public void Transfer_ValidFromAccountNoValidToAccountValidAmount_NoFoundStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",10).when().post("revolut/accounts/Konstantina/transfer/Vasilis").then().statusCode(404);

	}

	@Test
	public void Transfer_ValidFromAccountValidToAccountValidNoAmount_BadRequestStatus(){
		given().queryParam("name","Konstantina").when().post("revolut/accounts");
		given().queryParam("name","Vasilis").when().post("revolut/accounts");
		given().queryParam("amount",20).when().post("revolut/accounts/Konstantina/deposit");
		given().queryParam("amount",50).when().post("revolut/accounts/Konstantina/transfer/Vasilis").then().statusCode(400);

	}

	@Test
	public void Transfer_NoValidFromAccountNoValidToAccountNoValidNoAmount_BadRequestStatus(){
		given().queryParam("amount",50).when().post("revolut/accounts/Konstantina/transfer/Vasilis").then().statusCode(404);

	}

}
