def __find_sigma(text):
    sigma = set()
    for el in text:
        sigma.add(el)
    return sigma


def __compute_transition_function(pattern, sigma):
    delta = {}
    l = len(pattern)
    for q in range(l + 1):
        for el in sigma:
            k = min((l + 1), (q + 2))
            k -= 1
            while not str((pattern[:q:] + el)).endswith(pattern[:k:]):
                k -= 1
            delta[(q, el)] = k
    return delta


def __finite_automaton_matcher(text, delta, final_state):
    q = 0
    for i in range(len(text)):
        q = delta[(q, text[i])]
        if q == final_state:
            print("Pattern occurs with shift " + str(i - final_state + 1))


def finite_automata(text, pattern):
    sigma = __find_sigma(text)
    delta = __compute_transition_function(pattern, sigma)
    final_state = len(pattern)
    __finite_automaton_matcher(text, delta, final_state)
