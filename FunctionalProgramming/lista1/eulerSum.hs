eulerSum x = sum ([euler y | y<-[1..x], x `mod` y == 0 ])
eulerSumL x = [euler y | y<-[1..x], x `mod` y == 0 ]
