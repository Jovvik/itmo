#!/usr/bin/env bash

while true
do
    read line
    case $line in
        \*)
            kill -USR2 $(cat pid)
            ;;
        +)
            kill -USR1 $(cat pid)
            ;;
        *TERM*)
            kill -SIGTERM $(cat pid)
            exit
            ;;
    esac
done