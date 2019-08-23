## Revolut 
Test assignment by Revolut

## Tech/framework used
Java 8
<br>Bootique Framework


## Features

a RESTful API (including data model and the backing implementation)
for money transfers between accounts.


## Installation
`    mvn clean install`

`    cd target/`

`    java -jar revolut.jar`

## API Reference

### Create new account

`POST http://localhost:8080/create/account/?name=Flo`

Response example:
```
200 OK

Account(balance=0, name=Flo)
```

or in case of error:

```
400 Bad request

Account Flo already exists
```

### Get account

`GET http://localhost:8080/getAccount/?name=Flo`

Response example:
```
200 OK

Account(balance=0, name=Flo)
```

or in case of error:

```
400 Bad request

Account Flo doesnt exist
```

### Deposit money to the account

`POST http://localhost:8080/deposit?name=Flo&amount=20`

Response example:
```
200 ACCEPTED
```

or in case of error:

```
400 Bad request

Account Flo doesnt exist
```

### Withdraw money from the account

`POST http://localhost:8080/withdraw?name=Flo&amount=20`

Response example:
```
200 ACCEPTED
```

or in case of error:

```
400 Bad request

Account Flo doesnt exist
```
```
400 Bad request

Insufficient balance for %s
```

### Transfer money to another account

`POST http://localhost:8080/transfer?fromName=Bo&toName=Flo&amount=5`

Response example:
```
200 ACCEPTED
```
or in case of error:

```
400 Bad request

Account Flo doesnt exist
```
```
400 Bad request

Insufficient balance for %s
```
### Tests
`    mvn test`

