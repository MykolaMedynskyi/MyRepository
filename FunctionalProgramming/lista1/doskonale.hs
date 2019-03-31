sumDiv x = sum ([y | y<-[1..(x-1)], gcd x y == y])
perfect x = [n | n<-1:[2,4..x], n == sumDiv n]