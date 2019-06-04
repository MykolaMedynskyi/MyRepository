eliminator [] = []
eliminator (x:xs) = elim x xs [x]

elim _ [] acc = reverse acc
elim y (x:xs) acc | x==y = elim y xs acc
                    | otherwise = elim x xs (x:acc)