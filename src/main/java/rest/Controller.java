package rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNoFoundException;
import exceptions.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import service.BankServiceImpl;

@Path("/revolut")
@Slf4j
public class Controller {

	@Inject
	BankServiceImpl bankService;


	@POST
	@Path("/accounts/{name}/withdraw")
	public Response withdraw(@PathParam("name") String name, @QueryParam("amount") int amount){
		try {
			bankService.withdraw(name,amount);
			return Response.status(200).build();
		} catch (AccountNoFoundException | InsufficientBalanceException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/accounts/{name}/deposit")
	public Response deposit(@PathParam("name") String name, @QueryParam("amount") int amount){
		try {
			bankService.deposit(name,amount);
			return Response.status(200).build();
		} catch (AccountNoFoundException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/accounts/{from}/transfer/{to}")
	public Response transfer(@PathParam("from") String from,
							 @PathParam("to") String to, @QueryParam("amount") int amount ){
		try {
			bankService.transfer(from,to,amount);
			return Response.status(200).build();
		} catch (AccountNoFoundException | InsufficientBalanceException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/accounts")
	public Response createAccount(@QueryParam("name") String name){

		try {
			bankService.createAcount(name);
			return Response.status(201).build();
		} catch (AccountAlreadyExistsException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/accounts/{name}")
	public Response getAccount(@PathParam("name") String name){
		try {
			Account account = bankService.getAccount(name);
			return Response.status(200).entity(account.toString()).build();
		} catch (AccountNoFoundException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
	}



}
