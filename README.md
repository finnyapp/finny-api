# finny-api

[![Build Status](https://orca.snap-ci.com/gregoriomelo/finny-api/branch/master/build_image)](https://orca.snap-ci.com/gregoriomelo/finny-api/branch/master)
[![Stories in Ready](https://badge.waffle.io/gregoriomelo/finny-api.png?label=ready&title=Ready)](https://waffle.io/gregoriomelo/finny-api)
[![Dependencies Status](https://jarkeeper.com/gregoriomelo/finny-api/status.svg)](https://jarkeeper.com/gregoriomelo/finny-api)

## Stuff with Docker

### Building

    docker build -t finny/finny-api .

### Running the app

    docker run -p 3000:3000 -e DATABASE_URL= -it --rm --name finny-api finny/finny-api

### Testing

    docker run -e FINNY_ENV=test -t --rm --name finny-api finny/finny-api lein midje
