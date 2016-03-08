# finny-api

[![Build Status](https://orca.snap-ci.com/gregoriomelo/finny-api/branch/master/build_image)](https://orca.snap-ci.com/gregoriomelo/finny-api/branch/master)
[![Stories in Ready](https://badge.waffle.io/gregoriomelo/finny-api.png?label=ready&title=Ready)](https://waffle.io/gregoriomelo/finny-api)
[![Dependencies Status](https://jarkeeper.com/gregoriomelo/finny-api/status.svg)](https://jarkeeper.com/gregoriomelo/finny-api)

## Building with Docker

    docker build -t finny-api .

## Running with Docker

    docker run -p 3000:3000 -e DATABASE_URL= -it --rm --name finny-api finny-api
