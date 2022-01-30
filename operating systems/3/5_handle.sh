#!/usr/bin/env bash

shopt -s extglob

mode="*"
value=1

function stop() {
    echo $1
    killall tail
    exit $2
}

(tail -f pipe) | while true
do
    read line
    # echo -n $line | hexdump -d
    case $line in
        QUIT)
            stop "Exit" 0;;
        \*)
            mode="*"
            ;;
        +)
            mode="+"
            ;;
        +([0-9]))
            value=$(($value $mode $line))
            echo $value
            ;;
        *)
            stop "Invalid input" 1;;
    esac
done