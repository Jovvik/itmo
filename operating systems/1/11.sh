#!/usr/bin/env bash

sed -i '1s/#[^!].*$//;1!s/#.*$//' "$1"