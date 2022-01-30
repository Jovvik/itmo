#!/usr/bin/env bash

tmpfile=$(mktemp)

function get_bytes {
    awk '{if ($1 == "read_bytes:") print $2}' "/proc/$1/io" 2>/dev/null
}

for dirname in /proc/[0-9]*
do
    pid=$(basename "$dirname")
    {
        echo -n "$pid "
        get_bytes "$pid"
    } >> "$tmpfile"
done

sleep 20

tmpfile2=$(mktemp)
while IFS= read line
do
    pid=$(cut -d' ' -f1 <<< "$line")    
    bytes_before=$(cut -d' ' -f2 <<< "$line")
    bytes_diff=$(("$(get_bytes "$pid")" - "$bytes_before"))
    echo "$pid" $bytes_diff >> "$tmpfile2"
done < "$tmpfile"

sort -n -k2 -r "$tmpfile2" | head -n3 | while read line
do
    pid=$(cut -d' ' -f1 <<< "$line")
    echo -n "$pid:$(cut -d' ' -f2 <<< "$line"):"
    ps ax | awk -v pid="$pid" '{if ($1 == pid) {for(i=5;i<=NF;i++) printf $i" "; print ""}}'
done