#!/usr/bin/env bash

# while true; do
read a
case $a in
    "vi") vi;;
    "nano") nano;;
    "links") links;;
    "exit") exit;;
esac
# done