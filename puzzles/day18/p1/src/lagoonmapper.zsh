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

set_cell()
{
  local srow="$1"
  local scol="$2"
  local sc="$3"
  local skey="${srow},${scol}"

  grid["${skey}"]="$sc"
  echo "set $srow $scol $sc" >&2
}

get_cell()
{
  local grow="$1"
  local gcol="$2"
  local gkey="${grow},${gcol}"
  local gval=${grid[(k)"${gkey}"]}
  if [ "x${gval}" = "x" ]; then 
    gval="${EMPTY}"
  fi
  echo "get $grow $gcol -> $gval" >&2
  return ${gval}
}

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
      get_cell "${row}" "${col}"
      val=$?
      case $val in
        $DUG)
          sval='#'
          ;;
        $EMPTY)
          sval='.'
          ;;
        $OUT)
          sval='O'
          ;;
      esac
      printf "%s" "${sval}"
      col=$((col+1))
    done
    row=$((row+1))
    printf "\n"
  done
}

fill_in_grid()
{
  local startrow=$1
  local startcol=$2
  local endrow=$3
  local endcol=$4

  local row=$startrow

  while [ ${row} -le ${endrow} ]; do
    local col=$startcol
    while [ ${col} -le ${endcol} ]; do
      get_cell "${row}" "${col}"
      if [ $? -eq $EMPTY ]; then
        set_cell "${row}" "${col}" "${EMPTY}"
      fi
      col=$((col+1))
    done
    row=$((row+1))
  done
}

set_neighbour_dots()
{
  local startrow=$1
  local startcol=$2
  local endrow=$3
  local endcol=$4
  local nrow=$5
  local ncol=$6

  local context=${NOCHANGE}

  local lcol=$((ncol-1))
  local rcol=$((ncol+1))
  local lrow=$((nrow-1))
  local rrow=$((nrow+1))
  local cval 

set -x
  get_cell "${nrow}" "${lcol}"
  cval=$?
  if [ ${cval} -eq ${EMPTY} ] ; then
    set_cell "${nrow}" "${lcol}" "${OUT}"
    context=${UPDATE}
  fi
  get_cell "${nrow}" "${rcol}"
  cval=$?
  if [ ${cval} -eq ${EMPTY} ] ; then
    set_cell "${nrow}" "${rcol}" "${OUT}"
    context=${UPDATE}
  fi
  get_cell "${lrow}" "${ncol}"
  cval=$?
  if [ ${cval} -eq ${EMPTY} ] ; then
    set_cell "${lrow}" "${ncol}" "${OUT}"
    context=${UPDATE}
  fi
  get_cell "${rrow}" "${ncol}"
  cval=$?
  if [ ${cval} -eq ${EMPTY} ] ; then
    set_cell "${rrow}" "${ncol}" "${OUT}"
    context=${UPDATE}
  fi
set +x

  #echo "cell[$row][$col] ${context}" >&2
  return ${context}
}

flood_fill()
{
  local startrow=$1
  local startcol=$2
  local endrow=$3
  local endcol=$4

  print_grid "${startrow}" "${startcol}" "${endrow}" "${endcol}"
  set_cell "${startrow}" "${startcol}" "${OUT}"
  set_cell "${startrow}" "${endcol}" "${OUT}"
  set_cell "${endrow}" "${startcol}" "${OUT}"
  set_cell "${endrow}" "${endcol}" "${OUT}"

  typeset -p grid >&2

  local context=${UPDATE}
  local iters=0
  while [ ${context} -eq ${UPDATE} ]; do
    typeset -p grid >&2
    print_grid "${startrow}" "${startcol}" "${endrow}" "${endcol}" 
    echo "Iterate on flood"
    iters=$((iters+1))
    if [ $iters -gt 3 ]; then 
      return 0
    fi
    context=${NOCHANGE}
    local row=$startrow
    while [ ${row} -le ${endrow} ]; do
      local col=$startcol
      while [ ${col} -le ${endcol} ]; do
        get_cell "${row}" "${col}"
        val=$?
        if [ ${val} -eq ${OUT} ]; then
          set_neighbour_dots "${startrow}" "${startcol}" "${endrow}" "${endcol}" "${row}" "${col}"
          thiscontext=$?
          if [ ${thiscontext} -eq ${UPDATE} ]; then
            context=${UPDATE}
          fi
        fi
        col=$((col+1))
      done
      row=$((row+1))
    done
  done
}

update_horizontal()
{
  local row=$1
  local startcol=$2
  local length=$3
  local count=0
  while [ ${count} -lt ${length} ]; do
    local col=$((startcol+count))
    get_cell "${row}" "${col}"
    val=$?
    if [ ${val} -eq ${EMPTY} ]; then
      set_cell "${row}" "${col}" "${DUG}"
    fi
    count=$((count+1))
  done
}

update_vertical()
{
  local col=$1
  local startrow=$2
  local length=$3
  local count=0
  while [ ${count} -lt ${length} ]; do
    local row=$((startrow+count))
    get_cell "${row}" "${col}"
    val=$?
    if [ ${val} -eq ${EMPTY} ]; then
      set_cell "${row}" "${col}" "${DUG}"
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
      update_horizontal ${row} $((col+1)) ${length}
      col=$((col+length))
      ;;
    L)
      update_horizontal ${row} $((col-length)) ${length}
      col=$((col-length))
      ;;
    D)
      update_vertical ${col} $((row+1)) ${length}
      row=$((row+length))
      ;;
    U)
      update_vertical ${col} $((row-length)) ${length}
      row=$((row-length))
      ;;
  esac

  #
  # Give an edge around the min/max
  #
  if [ ${minrow} -gt ${row} ]; then
    minrow=$((row-2))
  fi
  if [ ${mincol} -gt ${col} ]; then
    mincol=$((col-2))
  fi
  if [ ${maxrow} -lt ${row} ]; then
    maxrow=$((row+2))
  fi
  if [ ${maxcol} -lt ${col} ]; then
    maxcol=$((col+2))
  fi

  return 0 
}

declare -a rawtext
declare -a dig
dig=()
typeset -A grid

if [ $# -lt 1 ]; then
  echo "Syntax: ${ZSH_ARGZERO} <mapfile>" >&2
  exit 4
fi

local mapfile=$1
rawtext=("${(@f)$(< ${mapfile})}")

echo "Map raw text to commands"

if ! map_rawtext_to_commands ; then
  echo "Mapping failed" >&2
  exit 4
fi

#
# Set the starting point to be a number in the 'middle' of the grid
# It will keep the math simpler
#
col=1000
row=1000

DUG=1
EMPTY=2
OUT=3

UPDATE=1
NOCHANGE=2

set_cell "${row}" "${col}" "${DUG}"

maxcol=0
maxrow=0
mincol=$((col*10000))
minrow=$((row*10000))

echo "Start excavating"

local dignum=1
while [ ${dignum} -le ${#dig[@]} ]; do
  echo "${dig[${dignum}]}"
  if ! excavate ${dig[${dignum}]} ; then
    echo "Excavation failed" >&2
    exit 8
  fi
  dignum=$((dignum+1))
done

echo "Range: ${minrow},${mincol} to ${maxrow},${maxcol}"

#fill_in_grid "${minrow}" "${mincol}" "${maxrow}" "${maxcol}" "${EMPTY}"

print_grid "${minrow}" "${mincol}" "${maxrow}" "${maxcol}"

#
# The dig lines cross over each other so safe approach
# is to start from the 4 corners and paint the outside
# (blank edge was inserted to ensure this is 'safe')
#

flood_fill "${minrow}" "${mincol}" "${maxrow}" "${maxcol}"

print_grid "${minrow}" "${mincol}" "${maxrow}" "${maxcol}"
