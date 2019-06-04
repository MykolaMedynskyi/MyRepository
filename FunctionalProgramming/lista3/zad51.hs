rleEncode [] = []
rleEncode (x:xs) = code x 1 xs []

code elem count [] acc = reverse ((count, elem):acc)
code elem count (x:xs) acc | x==elem = code elem (count+1) xs acc
                            | otherwise = code x 1 xs ((count, elem):acc)
