#!/usr/bin/env bash

log_file=info.log

: > $log_file

while IFS= read line
do
    if [[ $(echo "$line" | cut -d ' ' -f7) == "INFO:" ]]; then
        echo "$line" >> $log_file
    fi
done < syslog
# $(journalctl | head -n 5000)