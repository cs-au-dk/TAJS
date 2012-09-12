function f(x, z) {
    return g(x, z)
}

function g(y, a) {
   return eval("z = " + y)
}

dumpValue(f(3, 9))
dumpValue(f(5, 3))
