#!/bin/env lua
-- 
-- Find the cheapest route from upper-left to lower-right
-- with conditions on how to traverse.
--
function create_matrix(lines)
  local matrix = {}
  local rows = #lines
  for row = 1, rows do matrix[row] = {} end

  for row, line in pairs(lines) do
    cols = #line
    for col = 1, cols do
      local c = line:sub(col,col)
      matrix[row][col] = c
    end
  end
  return matrix
end

function print_matrix(matrix)
  for row = 1, #matrix do
    local line = ""
    for col = 1, #matrix[row] do
      local c = matrix[row][col]
      line=line..c
    end
    print(line)
  end
end

function calc_lowest_cost(matrix, directions, visits, row, col, cost)
  --print("cost: "..cost.." curlow: ".._Gcurlow)
  if cost >= _Gcurlow then return math.huge end
  if visits[row][col] > 0 then return math.huge end

  visits[row][col] = 1

  local max_same_dir = 3
  if directions ~= nil and string.len(directions) >= max_same_dir then
    local all_equal=true
    local cur = string.sub(directions, -1)
    local start = string.len(directions) - max_same_dir+1
    local finis = string.len(directions) - 1
    --print("Compare: "..string.sub(directions, start, finis).." to: "..cur)
    for i = start, finis do
      if string.sub(directions, i, i) ~= cur then 
        --print("All equal false")
        all_equal = false
      end
    end
    if all_equal then 
      --print("Directions: "..directions.." equal")
      visits[row][col] = 0
      return math.huge 
    end
  end

  if row == #matrix and col == #matrix[row] then 
    print("One solution: Direction: "..directions.." with cost:"..cost)
    visits[row][col] = 0
    return cost 
  end

  local calc = { math.huge, math.huge, math.huge, math.huge }
  if col < #matrix[row] then 
    --Right
    calc[1] = calc_lowest_cost(matrix, directions.."R", visits, row, col+1, cost+matrix[row][col+1])
  end
  if row < #matrix then 
    --Down
    calc[2] = calc_lowest_cost(matrix, directions.."D", visits, row+1, col, cost+matrix[row+1][col])
  end
  if col > 1 then 
    --Left
    calc[3] = calc_lowest_cost(matrix, directions.."L", visits, row, col-1, cost+matrix[row][col-1])
  end
  if row > 1 then 
    --Up
    calc[4] = calc_lowest_cost(matrix, directions.."U", visits, row-1, col, cost+matrix[row-1][col])
  end
  local min = math.huge
  for i, c in pairs(calc) do
    --print(c)
    min = math.min(min, c)
  end
  if min < _Gcurlow then
    _Gcurlow = min
  end 
  visits[row][col] = 0
  return min 
end

function file_exists(file)
  local f = io.open(file, "rb")
  if f~=nil then io.close(f) return true else return false end
end

function lines_from(file)
  if not file_exists(file) then return {} end
  local lines = {}
  for line in io.lines(file) do
    lines[#lines + 1] = line
  end
  return lines
end

if #arg < 1 then
  print("Syntax "..arg[-1].." <heatloss-map>")
  return
end

lines = lines_from(arg[1])
if #lines == 0 then 
  print("File "..arg[1].." is empty or does not exist")
  return
end

matrix = create_matrix(lines)
print_matrix(matrix)

-- The cheapest route will be no more than 9*nrows*ncols
-- Use this as a high-water mark to avoid any infinite loops

_Gcurlow = #matrix * #matrix[1] * 9
local directions = ""
local visits = {}
local rows = #matrix
for row = 1, rows do 
  visits[row] = {} 
  for col = 1, #matrix[row] do
    visits[row][col] = 0
  end
end
local cost = calc_lowest_cost(matrix, directions, visits, 1, 1, 0)

print("Lowest Cost "..cost) 
