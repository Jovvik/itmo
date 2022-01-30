#!/usr/bin/env bash

# $(dirname "$0")

if [[ "$HOME" == "$(pwd)" ]]; then
    echo "$HOME"
    exit 0
else
    >&2 echo "Error: script was not run from home directory"
    exit 1
fi