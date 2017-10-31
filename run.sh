#!/bin/bash
#
# Use this shell script to compile (if necessary) your code and then execute it. Below is an example of what might be found in this file if your program was written in Python
#
#python ./src/find_political_donors.py ./input/itcont.txt ./output/medianvals_by_zip.txt ./output/medianvals_by_date.txt

if [[ -e "./out" ]]; then
	rm -rf ./out/*
else
	mkdir ./out
fi

javac -d ./out ./src/main/*.java
rm -rf ./output/*
java -cp ./out DonorAnalysis ./input/itcont.txt ./output/medianvals_by_zip.txt ./output/medianvals_by_date.txt