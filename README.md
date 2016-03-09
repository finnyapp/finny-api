# finny-api

[![Build Status](https://orca.snap-ci.com/finnyapp/finny-api/branch/master/build_image)](https://orca.snap-ci.com/finnyapp/finny-api/branch/master)
[![Stories in Ready](https://badge.waffle.io/finnyapp/finny-api.png?label=ready&title=Ready)](https://waffle.io/finnyapp/finny-api)
[![Dependencies Status](https://jarkeeper.com/finnyapp/finny-api/status.svg)](https://jarkeeper.com/finnyapp/finny-api)

## Code of conduct

[Here](CODE_OF_CONDUCT.md)

## Running the app

In order to run the app, you need to setup a PostgreSQL instance, with a database named `finny`.

### Applying the database migrations

Run the database migrations:

    DATABASE_URL="postgres://username:password@hostname:5432/finny" lein lobos migrate

### Starting it up

To start the app up, run this:

    DATABASE_URL="postgres://username:password@hostname:5432/finny" lein ring server-headless

## Usage

### Creating a transaction

In order to create a transaction with 7 as its value, run:

    curl -XPOST finny-api.herokuapp.com/transaction -d '{"value": 7}' -H "Content-Type: application/json"

### Getting all transactions

    curl localhost:3000/transactions -H "Content-Type: application/json"

## Testing

### Running the tests

    FINNY_ENV=test lein midje

or, with autotest on:

    FINNY_ENV=test lein midje :autotest

## Checking the coverage

    FINNY_ENV=test lein cloverage

## Deployment

You should be able to just deploy the app to Heroku. Make sure you run the database migrations.

## Stuff with Docker

### Building

    docker build -t finny/finny-api .

### Running the app

    docker run -p 3000:3000 -e DATABASE_URL= -it --rm --name finny-api finny/finny-api

### Testing

    docker run -e FINNY_ENV=test -t --rm --name finny-api finny/finny-api lein midje
