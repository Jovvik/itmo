#!/bin/bash

: > report.log

count=0
arr=()

while true
do
    arr+=(1 2 3 4 5 6 7 8 9 10)
    count=$(( count + 1 ))
    if ! (( count % 100000 ))
    then
        echo ${#arr[@]} >> report.log
    fi
done