partitions xs = zip listI listT
    where listI = inits xs
          listT = tails xs

inits [] = [[]]
inits xs = inits (init xs) ++ [xs]

tails [] = [[]]
tails xs = [xs] ++ tails (tail xs)
