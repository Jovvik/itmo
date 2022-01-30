#!/usr/bin/env bash

shopt -s extglob

while true
do
    read line
    echo $line > pipe
    case $line in
        QUIT)
            exit 0
            ;;
        !(+|\*|+([0-9])))
            echo "Invalid input"
            exit 1
            ;;
    esac
done