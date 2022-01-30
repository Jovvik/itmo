#!/usr/bin/env bash

value=1

usr1()
{
    value=$(($value + 2))
    echo $value
}

usr2()
{
    value=$(($value * 2))
    echo $value
}

term()
{
    echo "Exiting as per signal of the generator"
    exit
}

echo $$ > pid

trap 'usr1' USR1
trap 'usr2' USR2
trap 'term' SIGTERM

while true
do
    :
done