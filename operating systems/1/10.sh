#!/usr/bin/env bash

man bash | tr -cs '[:alpha:]' '\n' | tr '[:upper:]' '[:lower:]' | grep -E "[[:alnum:]]{4,}" | sort | uniq -c | sort -nr | head -n 4 | awk '{print $2}'