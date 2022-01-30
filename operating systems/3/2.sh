#!/usr/bin/env bash

at now +2 minutes <<< "$(readlink -f 1.sh)"
tail -f $HOME/report