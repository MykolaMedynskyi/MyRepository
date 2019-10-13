def __compute_prefix_function(pattern):
    l = len(pattern)
    pi = []
    for i in range(l):
        pi.append(0)
    i = 1
    j = 0
    while i < l:
        if pattern[i] == pattern[j]:
            pi[i] = j+1
            i += 1
            j += 1
        elif j == 0:
            pi[i] = 0
            i += 1
        else:
            j = pi[j-1]

    return pi


def kmp_matcher(text, pattern):
    n = len(text)
    m = len(pattern)
    pi = __compute_prefix_function(pattern)

    k = 0
    l = 0

    while k < n:
        if text[k] == pattern[l]:
            k += 1
            l += 1
            if l == m:
                print("Pattern occurs with shift " + str(k - m))
                l = 0
        elif l == 0:
            k += 1
        else:
            l = pi[l-1]

