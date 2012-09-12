function f(z) {
	return eval("x = " + '"hi world\'";' + z) 
}
dumpValue(f(3))
