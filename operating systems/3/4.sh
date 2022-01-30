#!/usr/bin/env bash

# Это набор команд, а не рабочий скрипт

for _ in {1 .. 3}
do
    bash -c 'n=1; while true; do n=$((2 * n)); done' &
done

top

./limit.sh pid_первого_процесса

kill pid_третьего_процесса