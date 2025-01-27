#!/bin/sh
mkdir -p ./bin
g++ -pthread "./src/main.cpp" "./src/brutecpp.cpp" -o "./bin/brutecpp"
g++ -pthread -lgtest -lgtest_main "./test/test_brutecpp.cpp" "./src/brutecpp.cpp" -o "./bin/test_brutecpp"
