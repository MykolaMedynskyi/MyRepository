paket [] = [[]]
paket (x:xs) = pack [x] xs []

pack current [] acc = reverse (current:acc)
pack current (x:xs) acc | x == head current = pack (x:current) xs acc
                        | otherwise = pack [x] xs (current:acc)