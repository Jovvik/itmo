#!/usr/bin/env bash

filename=stats

function write {
    avg="$(echo "$sum" "$proc_count" | awk '{printf "%.3f", $1/$2}' -)"
    new_line="$(printf 'Average_Sleeping_Children_of_ParentID=%i is %f'\
                "$curr_ppid" "$avg")"
    sed -i "$line_num a $new_line" "$tmpfile"
}

curr_ppid=0
sum="0"
proc_count=0
line_num=-1
tmpfile="$(mktemp)"
cat "$filename" > "$tmpfile"
while IFS= read line
do
    ((line_num++))
    tmp="${line//[[:alpha:]_=:]/}"
    ppid="$(echo "$tmp" | awk '{print $2}')"
    art="$(echo "$tmp" | awk '{print $3}')"
    if [[ "$ppid" != "$curr_ppid" ]]
    then
        write
        ((line_num++))
        proc_count=1
        sum=$art
        curr_ppid=$ppid
    else
        ((proc_count++))
        sum="$(echo "$sum" "$art" | awk '{printf "%.3f", $1+$2}' -)"
    fi
done < $filename
((line_num++))
write
cat "$tmpfile" > "$filename"