function f(z) {
	return eval("x = " + '"hi world\'";' + z) 
}
TAJS_dumpValue(f(3))
