#!/bin/env zsh

#
# zsh arrays start at index 1
#
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

# 
# An interesting way to simulate 2D arrays using an associative map
# https://stackoverflow.com/questions/16487258/how-to-declare-2d-array-in-bash
#

print_grid()
{
  local startrow=$1
  local startcol=$2
  local endrow=$3
  local endcol=$4

  local row=$startrow

  while [ ${row} -le ${endrow} ]; do
    local col=$startcol
    while [ ${col} -le ${endcol} ]; do
      local val=${grid[(k)"${row},${col}"]}
      if [ -z ${val} ]; then
        printf "%s" '.'
      else
        key="${row},${col}"
        printf "%s" "${val}"
      fi
      col=$((col+1))
    done
    row=$((row+1))
    printf "\n"
  done
}
      
update_horizontal()
{
  local row=$1
  local startcol=$2
  local length=$3
  local c=$4
  local count=0
  while [ ${count} -lt ${length} ]; do
    local col=$((startcol+count))
    local val=${grid[(k)"${row},${col}"]}
    if [ -z ${val} ]; then
      key="${row},${col}"
      grid["${key}"]="$c"
    fi
    count=$((count+1))
  done
}

update_vertical()
{
  local col=$1
  local startrow=$2
  local length=$3
  local c=$4
  local count=0
  while [ ${count} -lt ${length} ]; do
    local row=$((startrow+count))
    local val=${grid[(k)"${row},${col}"]}
    if [ -z ${val} ]; then
      key="${row},${col}"
      grid["${key}"]="$c"
    fi
    count=$((count+1))
  done
}

excavate()
{
  # grid will be read/written to 
  # col and row will be read/written to
  # maxcol and maxrow will be read/written to
  local cmd=$1

  local direction=${cmd%%:*}
  local length=${cmd##*:}
  
  case ${direction} in
    R)
      update_horizontal ${row} $((col+1)) ${length} '#'
      col=$((col+length))
      ;;
    L)
      update_horizontal ${row} $((col-length)) ${length} '#'
      col=$((col-length))
      ;;
    D)
      update_vertical ${col} $((row+1)) ${length} '#'
      row=$((row+length))
      ;;
    U)
      update_vertical ${col} $((row-length)) ${length} '#'
      row=$((row-length))
      ;;
  esac
  echo "${row},${col}"
  if [ ${minrow} -gt ${row} ]; then
    minrow=${row}
  fi
  if [ ${mincol} -gt ${col} ]; then
    mincol=${col}
  fi
  if [ ${maxrow} -lt ${row} ]; then
    maxrow=${row}
  fi
  if [ ${maxcol} -lt ${col} ]; then
    maxcol=${col}
  fi

  return 0 
}

declare -a rawtext
declare -a dig
dig=()

if [ $# -lt 1 ]; then
  echo "Syntax: ${ZSH_ARGZERO} <mapfile>" >&2
  exit 4
fi

mapfile=$1
rawtext=("${(@f)$(< ${mapfile})}")

echo "Map raw text to commands"

if ! map_rawtext_to_commands ; then
  echo "Mapping failed" >&2
  exit 4
fi

declare -A grid
grid=()

local dignum=1

#
# Set the starting point to be a number in the 'middle' of the grid
# It will keep the math simpler
#
col=1000
row=1000
key="${row},${col}"
grid["${key}"]='#'

maxcol=0
maxrow=0
mincol=$((col*10000))
minrow=$((row*10000))

echo "Start excavating"

while [ ${dignum} -le ${#dig[@]} ]; do
  echo "${dig[${dignum}]}"
  if ! excavate ${dig[${dignum}]} ; then
    echo "Excavation failed" >&2
    exit 8
  fi
  dignum=$((dignum+1))
done

echo "Range: ${minrow},${mincol} to ${maxrow},${maxcol}"

print_grid ${minrow} ${mincol} ${maxrow} ${maxcol}
