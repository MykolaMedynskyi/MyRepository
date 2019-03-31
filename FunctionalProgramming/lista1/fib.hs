fibonacci 0 = 0
fibonacci 1 = 1
fibonacci x = fibonacci (x - 1) + fibonacci (x - 2)
fibs x = take x ([fibonacci a | a<-[0..x]])

fibonacci2 x = fibs2!!x
    where fibs2 = 0 : 1 : zipWith (+) fibs2 (tail fibs2)
fibs2 x = take x fibs2
    where fibs2 = 0 : 1 : zipWith (+) fibs2 (tail fibs2)