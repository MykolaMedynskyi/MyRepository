nub [] = []
nub xs = removeD xs []

removeD [] _ = []
removeD (x:xs) ys
    | (x `elem` ys) = removeD xs ys
    | otherwise = x:removeD xs (x:ys)