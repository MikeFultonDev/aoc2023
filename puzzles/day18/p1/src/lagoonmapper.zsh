#!/bin/env zsh

#
# zsh arrays start at index 1
#

declare -a rawtext

if [ $# -lt 1 ]; then
  echo "Syntax: ${ZSH_ARGZERO} <mapfile>" >&2
  exit 4
fi

mapfile=$1
rawtext=("${(@f)$(< ${mapfile})}")

index=1
lines=${#rawtext[@]}
while [ ${index} -le ${lines} ] ; do
  line=${rawtext[${index}]}
  echo "${line}"
  index=$((index+1))
done
