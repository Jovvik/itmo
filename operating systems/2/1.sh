#!/usr/bin/env bash

filename=processes

res=$(ps aux | awk '{if ($1 == "jovvik") print $2 ":" $11}' -)
{
    wc -l <<< "$res"
    echo "$res"
} > "$filename"