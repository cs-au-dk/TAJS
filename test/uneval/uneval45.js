function f(y) {
   a = y
   return eval("z = " + a)
}

dumpValue(f("3"))
dumpValue(f("5"))
