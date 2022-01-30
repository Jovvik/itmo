#!/bin/bash

: > report.log

count=0
arr=()

while (( ${#arr[@]} < $1 ))
do
    arr+=(1 2 3 4 5 6 7 8 9 10)
    count=$(( count + 1 ))
done