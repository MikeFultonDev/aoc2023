#!/bin/env zsh
set -x
declare -A grid

col=1
row=1
grid["$col,$row"]='#'
col=2
row=2
grid["$col,$row"]='.'

for key value in ${(kv)grid}; do
  echo "$key -> $value"
done

col=4
entry=${grid[(k)"$col,$row"]}

if [ -z ${entry} ]; then
  echo 'empty'
fi
