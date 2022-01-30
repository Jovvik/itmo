#!/usr/bin/env bash

s=""
a=""
while [[ "$a" != "q" ]]; do
  read a
  s+="$a"
done
echo "$s"