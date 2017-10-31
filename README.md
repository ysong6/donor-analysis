# Table of Contents
1. [Data extraction](README.md#Data-extraction)
2. [Approach](README.md#Approach)
3. [Instructions](README.md#Instructions)


# Data extraction
_Note: first remove all leading and trailing white space of the data string._

`CMTE_ID`: if recipientID is empty, discarding this record.

`ZIP_CODE`: if the length of zipCode part is less than 5 or is malformed, should not add this record into `medianvals_by_zip.txt`

`TRANSACTION_DT`: if the date is malformed, should not add this record into `medianvals_by_date.txt`

`TRANSACTION_AMT`: Two situations for amount of the transaction:
1. if amount is malformed, discarding the record.
2. if amount is negative number, change to positive. Because donation must not be negative, the negative sign must be add by mistake.

`OTHER_ID`: if this part is not empty, discarding this record.

# Approach

The data structure to compute the running median uses 2 heaps: a max-heap which stores the smaller half of all values of the stream and a min-heap which stores the larger half of all the values in the stream. If the total number of values is even, each heap will contain the same number of values and the median is the average of root values of the two heaps. If the total number of values is odd, the size of th heaps will differ by 1. In this case, make the algorithm to guarantee the size of max-heap larger than min-heap. So the median is the root number of the max-heap.

Every time when a new value come to the stream, this property is maintained in the following way:

Insert the new value to the max-heap, then pop the root of the max-heap and insert into min-heap. If the size of the max-heap is less than size of the min-heap, pop the root of the min-heap and insert into the max-heap.

So inserting a new value takes logarithmic time complexity.

Retrieving the median is a constant time operation:

If the size of two heaps are equal, then the median is average of the root of two heaps. 
Otherwise the median is the root of max-heap.


# Instructions
Java version: 1.8

run.sh:
javac -d ./out ./src/main/*.java
rm -rf ./output/*
java -cp ./out DonorAnalysis ./input/itcont.txt ./output/medianvals_by_zip.txt ./output/medianvals_by_date.txt
