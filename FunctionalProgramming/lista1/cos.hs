easy x y =
    if y == 0 || y == x then 1
    else easy (x-1) (y-1) + easy (x-1) y
