function f(x) {
    z = x
    return g(z)
}

function g(y) {
   a = y
   return eval("z = " + a)
}

dumpValue(f("3"))
dumpValue(f("5"))
