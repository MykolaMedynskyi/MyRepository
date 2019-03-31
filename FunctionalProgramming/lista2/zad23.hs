middle a b c
    | (a >= b && a <= c) || (a >= c && a <= b) = a
    | (c >= a && c <= b) || (c >= b && c <= a) = c
    | otherwise = b