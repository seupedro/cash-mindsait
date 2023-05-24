#!/bin/zsh

docker-compose down

echo " ----- BUIDING CASH FLOW ----"
cd ./cash-flow || exit 1
./gradlew clean
./gradlew build -x test
cd ../ || exit 1

echo " ----- BUIDING CASH REPORT ----"
cd ./cash-report || exit 1
./gradlew clean
./gradlew build -x test
cd ../ || exit 1

docker-compose up