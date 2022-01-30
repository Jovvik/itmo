#!/usr/bin/env bash

ps ax | awk '{if ($5 ~ /\/sbin\/.*/) print $1}' -