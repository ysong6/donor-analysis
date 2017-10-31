# Table of Contents
1. [Introduction](README.md#Introduction)
2. [Data extraction](README.md#Data-extraction)
3. [Main Approach](README.md#Main-Approach)
4. [Instructions](README.md#Instructions)

# Introduction
This project analyzes donors for a variety of election campaigns. The result consists of two parts:

a. The running median, total dollar amount and total number of contributions by recipient and zip code for each record of the input data.

b. The calculated median, total dollar amount and total number of contributions by recipient and date of the input data.

_Data sources: http://classic.fec.gov/finance/disclosure/ftpdet.shtml_

# Data extraction
_Note: first remove all leading and trailing white space of the data string._

`CMTE_ID`: if recipientID is empty, discarding this record.

`ZIP_CODE`: if the length of zipCode part is less than 5 or is malformed, should not add this record into `medianvals_by_zip.txt`

`TRANSACTION_DT`: if the date is malformed, should not add this record into `medianvals_by_date.txt`

`TRANSACTION_AMT`: If amount is malformed, discarding the record.

`OTHER_ID`: if this part is not empty, discarding this record.

# Main Approach
1. Key classes:

DonorAnalysis: The main class of this project. It consists of 5 methods:
a. Analyse: extract the valid data and call the other methods to analyze the data and output to result.
b. addContributionWithDate: add a new valid data into date relative data structure.
c. addContributionWithZip: add a new valid data into zipcode relative data structure and return the current info for output.
d. outputMedianByDate: output the final analysis result to `medianvals_by_date.txt`
e: outputMedianByZip: output current analysis info to `medianvals_by_zip.txt`

ContributionWithZip: Record contribution info for specified recipient and zipcode

ContributionWithDate: Record contribution info for specified recipient and date

2. Main data structure:

donationWithZip: A hashMap. The key is the combination of recipient id and zipcode, the value is a class, which track the total amount and total contribution number. Using two balanced heaps to caculate the median.

donationWithDate: A treeMap to keep the recipient in order. The key is the recipient id, the value is also a treeMap. The key of this treeMap is date info, the value of this treeMap is a class, which record the total amount and total contribution number and contributions amount info.

3. Calculate running median: 

The data structure to compute the running median uses 2 heaps: a max-heap which stores the smaller half of all values of the stream and a min-heap which stores the larger half of all the values in the stream. If the total number of values is even, each heap will contain the same number of values and the median is the average of root values of the two heaps. If the total number of values is odd, the size of th heaps will differ by 1. In this case, make the algorithm to guarantee the size of max-heap larger than min-heap. So the median is the root number of the max-heap.

Every time when a new value come to the stream, this property is maintained in the following way:

Insert the new value to the max-heap, then pop the root of the max-heap and insert into min-heap. If the size of the max-heap is less than size of the min-heap, pop the root of the min-heap and insert into the max-heap.

So inserting a new value takes logarithmic time complexity.

Retrieving the median is a constant time operation:

If the size of two heaps are equal, then the median is average of the root of two heaps. 
Otherwise the median is the root of max-heap.

# Instructions
Java version: 1.8

The project has 3 input parameters:

1. input file
2. output_zipfile
3. output_datefile

run.sh:

1.compile the project

2.run the this project with 3 paramters: "./input/itcont.txt", "./output/medianvals_by_zip.txt", "./output/medianvals_by_date.txt"
