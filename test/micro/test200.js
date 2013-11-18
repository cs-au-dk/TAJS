function f() {
	for (var x in arguments) {
		dumpValue(x + ": " + arguments[x]);
	}
}

f(42,"foo",true)
