#!/usr/bin/env bash

log_file="full.log"

: > $log_file

information=""

while IFS= read line
do
    tag=$(echo "$line" | awk '{ print $3 }')
    case $tag in
        "(WW)") echo "${line/(WW)/Warning:}" >> $log_file;;
        "(II)") information+="${line/(II)/Information:}\n"
    esac
done < /var/log/Xorg.0.log
echo -e "$information" >> $log_file