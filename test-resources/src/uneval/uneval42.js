function f(x) {
    return g(x)
}

function g(y) {
   return eval("z = " + y)
}

TAJS_dumpValue(f(3))
TAJS_dumpValue(f(5))
