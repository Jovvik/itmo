#!/usr/bin/env bash

: > watch_logs/mem.log
: > watch_logs/swap.log
: > watch_logs/params1.log
: > watch_logs/params2.log
: > watch_logs/top5.log

while true
do
    BOTH=$(free --mega | tail -n2 | awk '{print $4;}')
    echo "$BOTH" | head -n1 >> watch_logs/mem.log
    echo "$BOTH" | tail -n1 >> watch_logs/swap.log

    top -b -n1 | grep -m 1 "mem.bash" >> watch_logs/params1.log
    top -b -n1 | grep -m 1 "mem2.bash" >> watch_logs/params2.log

    top -b -n1 -o %MEM | head -n12 | tail -n5 >> watch_logs/top5.log
    echo "" >> watch_logs/top5.log

    sleep 0.1s

    echo "iter"
done