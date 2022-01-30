#!/usr/bin/env bash

max=0
proc=""
for dirname in /proc/[0-9]*
do
    curr="$(awk '{print $6}' "$dirname/statm")"
    if [[ curr -ge $max ]]
    then
        proc=$(basename "$dirname")
        max=$curr
    fi
done
echo "$proc"