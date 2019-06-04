span' :: (a -> Bool) -> [a] -> ([a], [a])
span' p xs = (left_span p xs, right_span p xs)

left_span p xs = left_span_pom p xs []
left_span_pom _ [] acc = acc
left_span_pom p (x:xs) acc | p x = left_span_pom p xs acc++[x]
                   | otherwise = left_span_pom p xs acc

right_span p xs = right_span_pom p xs []
right_span_pom _ [] acc = acc
right_span_pom p (x:xs) acc | p x = right_span_pom p xs acc
                    | otherwise = right_span_pom p xs acc++[x]