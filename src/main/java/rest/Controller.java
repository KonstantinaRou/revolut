package rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import service.BankServiceImpl;

@Path("/")
@Slf4j
public class Controller {

	@Inject
	BankServiceImpl bankService;


	@POST
	@Path("/withdraw")
	public Response withdraw(@QueryParam("name") String name, @QueryParam("amount") int amount){
		try {
			bankService.withdraw(name,amount);
			return Response.status(200).build();
		} catch (AccountNoFoundException | InsufficientBalanceException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/deposit")
	public Response deposit(@QueryParam("name") String name, @QueryParam("amount") int amount){
		try {
			bankService.deposit(name,amount);
			return Response.status(200).build();
		} catch (AccountNoFoundException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/transfer")
	public Response transfer(@QueryParam("fromName") String fromName,
							 @QueryParam("toName") String toName, @QueryParam("amount") int amount ){
		try {
			bankService.transfer(fromName,toName,amount);
			return Response.status(200).build();
		} catch (AccountNoFoundException | InsufficientBalanceException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/create/account")
	public Response createAccount(@QueryParam("name") String name){

		try {
			bankService.createAcount(name);
			return Response.status(201).build();
		} catch (AccountAlreadyExistsException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/getAccount")
	public Response getAccount(@QueryParam("name") String name){
		try {
			Account account = bankService.getAccount(name);
			return Response.status(200).entity(account.toString()).build();
		} catch (AccountNoFoundException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}



}
