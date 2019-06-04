rleDecode [] = []
rleDecode ((count, elem):xs) = decode count elem xs []

decode 0 _ [] acc = reverse acc
decode count elem [] acc = decode 0 elem [] (write count elem acc)
decode count elem ((count', elem'):xs) acc = decode count' elem' xs (write count elem acc)

write 0 _ acc = acc
write count elem acc = write (count-1) elem (elem:acc)