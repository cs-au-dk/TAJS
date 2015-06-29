function f(x, z) {
    return g(x, z)
}

function g(y, a) {
   return eval("z = " + y)
}

TAJS_dumpValue(f(3, 9))
TAJS_dumpValue(f(5, 3))
