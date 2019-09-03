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

`    java -jar revolut-1.0-SNAPSHOT.jar --server`

## API Reference

### Create new account

`POST http://localhost:8080/revolut/accounts?name=Flo`

Response example:
```
200 OK

```

or in case of error:

```
400 Bad request

Account Flo already exists
```

### Get account

`GET http://localhost:8080/revolut/accounts/Flo`

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

`PUT http://localhost:8080/revolut/accounts/Flo/deposit?amount=20`

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

`PUT http://localhost:8080/revolut/accounts/Flo/withdraw?amount=20`

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

`PUT http://localhost:8080/revolut/accounts/Flo/transfer/Bill?amount=5`

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

Insufficient balance for Flo
```
### Tests
`    mvn test`

