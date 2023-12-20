#!/bin/env zsh
set_grid() 
{
  local row=$1
  local col=$2
  local val=$3
  local key="${row},${col}"
  grid["${key}"]="${val}"
}

declare -A grid

col=1
row=1
set_grid $col $row '#'
col=$((col+1))
row=$((row+1))
set_grid $col $row '#'

col=4
entry=${grid[(k)"$col,$row"]}

if [ -z ${entry} ]; then
  echo 'empty'
fi
echo "$entry"

col=7
row=7
set_grid $col $row 'A'
row=8
set_grid $col $row 'B'
row=7
set_grid $col $row 'A'

for key value in ${(kv)grid}; do
  echo "$key -> $value"
done
