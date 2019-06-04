oblicz [] = 0
oblicz xs = foldl (\x y -> x + (-1)^(y+1) * xs!!(y - 1) ) 0 [1..length xs]