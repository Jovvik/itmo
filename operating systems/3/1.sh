#!/usr/bin/env bash

mkdir ~/test 2>/dev/null &&
    echo "catalog test was created successfully" >> ~/report &&
    touch ~/test/"$(ps -eo pid,lstart | awk -v selfpid=$$ '{if ($1 == selfpid) {for(i=2;i<=NF;i++) printf $i" "; print ""}}' -)"

ping www.net_nikogo.ru 2>/dev/null || date '+%H:%M:%S %Y.%m.%d'