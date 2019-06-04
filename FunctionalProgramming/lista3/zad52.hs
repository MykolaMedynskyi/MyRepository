numberEven :: [Int]-> Int
numberEven l = foldr (\_ x -> x + 1) 0 (filter even l)