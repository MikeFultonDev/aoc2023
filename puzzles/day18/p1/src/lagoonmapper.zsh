#!/bin/env zsh

#
# zsh arrays start at index 1
#
map_rawtext_to_array()
{
  local row=1
  local rows=${#rawtext[@]}
  while [ ${row} -le ${rows} ] ; do
    local line=${rawtext[${row}]}
    local col=0
    while [ ${col} -le ${#line[@]} ] ; do
      col=$((col+1))
      map[row][col]=${#line[${col}]}
    done  
	  row=$((row+1))
  done
  return 0
}

map_rawtext_to_commands() 
{
  # rawtext will be read, and dig array will be created
  local row=1
  local rows=${#rawtext[@]}
  while [ ${row} -le ${rows} ] ; do
    local line=${rawtext[${row}]}
    local dir=$(echo ${line} | cut -d " " -f 1)
    local len=$(echo ${line} | cut -d " " -f 2)
    dig[${row}]="${dir}:${len}"
    row=$((row+1))
  done
  return 0
}

declare -a rawtext
declare -a dig

if [ $# -lt 1 ]; then
  echo "Syntax: ${ZSH_ARGZERO} <mapfile>" >&2
  exit 4
fi

mapfile=$1
rawtext=("${(@f)$(< ${mapfile})}")

if ! map_rawtext_to_commands ; then
  echo "Mapping failed" >&2
  exit 4
fi

declare -a grid
grid[1]=('#')

local dignum=1
while [ ${dignum} -le ${#dig[@]} ]; do
  echo "${dig[${dignum}]}"
  dignum=$((dignum+1))
done

