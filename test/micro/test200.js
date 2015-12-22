function f() {
	for (var x in arguments) {
		TAJS_dumpValue(x + ": " + arguments[x]);
	}
}

f(42,"foo",true)
