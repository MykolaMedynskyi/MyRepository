qsort [] = []
qsort (x:xs) = qsort [a | a<-xs, a<x] ++ [x] ++ [b | b<-xs, b==x] ++ qsort [c | c<-xs, c>x]