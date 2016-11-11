# finny-api

[![Build Status](https://snap-ci.com/finnyapp/finny-api/branch/master/build_image)](https://snap-ci.com/finnyapp/finny-api/branch/master)
[![Stories in Ready](https://badge.waffle.io/finnyapp/finny-api.png?label=ready&title=Ready)](https://waffle.io/finnyapp/finny-api)
[![Dependencies Status](https://jarkeeper.com/finnyapp/finny-api/status.svg)](https://jarkeeper.com/finnyapp/finny-api)

## Code of conduct

[Here](CODE_OF_CONDUCT.md)

## Running the app

In order to run the app, you need to setup a PostgreSQL instance, with a database named `finny`.

### Applying the database migrations

Run the database migrations:

    DATABASE_URL="postgres://username:password@hostname:5432/finny" lein migratus migrate

### Starting it up

To start the app up, run this:

    DATABASE_URL="postgres://username:password@hostname:5432/finny" lein ring server-headless

## Usage

### Creating a transaction

In order to create an income transaction with 700 as its value, run:

    curl -XPOST localhost:3000/transaction -d '{"value": 700, "type": "income"}' -H "Content-Type: application/json"

And an expense, valued 7, run:

    curl -XPOST localhost:3000/transaction -d '{"value": 7, "type": "expense"}' -H "Content-Type: application/json"

### Getting all transactions

    curl localhost:3000/transactions -H "Content-Type: application/json"

### Filtering transactions

#### by date

    curl -XGET localhost:3000/transactions -d '{"filters": {"date": {"is": "2016-01-01"}}}' -H "Content-Type: application/json"
    curl -XGET localhost:3000/transactions -d '{"filters": {"date": {"before": "2016-01-01"}}}' -H "Content-Type: application/json"
    curl -XGET localhost:3000/transactions -d '{"filters": {"date": {"after": "2015-01-01"}}}' -H "Content-Type: application/json"
    curl -XGET localhost:3000/transactions -d '{"filters": {"date": {"after": "2015-01-01", "before": "2017-01-01"}}}' -H "Content-Type: application/json"

#### by category

    curl -XGET localhost:3000/transactions -d '{"filters": {"category": "Education"}}' -H "Content-Type: application/json"; echo

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

### Running the app with docker-compose

    docker-compose up

This will bring up:

- [the database](https://github.com/finnyapp/finny-api/blob/master/docker-compose.yml#L3), with `finny` as the database
- [the api](https://github.com/finnyapp/finny-api/blob/master/docker-compose.yml#L7) on port 3000
- [an instance that'll unit tests](https://github.com/finnyapp/finny-api/blob/master/docker-compose.yml#L18)
- [an instance that'll run all tests, including db stuff](https://github.com/finnyapp/finny-api/blob/master/docker-compose.yml#L25)

### Testing

    docker run -e FINNY_ENV=test -t --rm --name finny-api finny/finny-api lein midje
