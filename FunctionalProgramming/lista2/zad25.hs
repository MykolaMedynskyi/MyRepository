inits [] = [[]]
inits xs = inits (init xs) ++ [xs]

