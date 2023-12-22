#!/bin/env zsh

set_cell()
{
  local srow=$1
  local scol=$2
  local sc=$3
  local skey="${srow},${scol}"

  grid["${skey}"]="$sc"
  echo "set $srow $scol $sc" >&2
  return $sc
}

get_cell()
{
  local grow=$1
  local gcol=$2
  local gkey="${grow},${gcol}"
  local gval=${grid[(k)"${gkey}"]}
  echo "get $grow $gcol $gval" >&2
  echo "${gval}"
  return $gval
}

set_deepest() 
{
  local row=$1
  local col=$2
  x=5
  typeset -p grid >&2
  set_cell "${row}" "${col}" "2"
  val=$?
  typeset -p grid >&2
  return $val
}

set_deeper()
{
  local row=$1
  local col=$2
  x=4
  col=$((col+1))
  row=$((row+1))
  typeset -p grid >&2
  y=$(set_deepest "$row" "$col")
  val=$?
  return $val
}

set -x
declare -A grid

v=$(get_cell "1010" "1008")
echo "$v"

set_cell "1010" "1008" "1"
v=$(get_cell "1010" "1008")
echo "$v"

v=$(get_cell "1010" "1008")
echo "$v"

row=1009
col=1007
export x=2
echo $x
q=$(set_deeper "$row" "$col")
val=$?
echo $val
echo $x

v=$(get_cell "1010" "1008")
  typeset -p grid >&2
echo $v
