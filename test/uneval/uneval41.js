function f(x) {
    return g(x)
}

function g(y) {
   return eval("z = " + y)
}

dumpValue(f(3))
