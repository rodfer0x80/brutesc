#!/bin/sh
mkdir -p ./bin
g++ -pthread ./src/main.cpp ./src/brutecpp.cpp -o ./bin/brutecpp
