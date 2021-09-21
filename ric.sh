#!/bin/sh

printf "Run In Container (RIC)\n"

docker exec -it ports-tide-schedule-app "$@"
