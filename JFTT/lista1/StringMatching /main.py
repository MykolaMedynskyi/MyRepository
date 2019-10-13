import KMPMMatcher as kmp
import FiniteAutomata as fa

text = "hello my world, hello hello hello"
pattern = "hell"

fa.finite_automata(text, pattern)

print("-------------------")

kmp.kmp_matcher(text, pattern)

