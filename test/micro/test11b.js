function foo4() {
	return arguments[0] + arguments["1"];
}

var qqq4 = foo4(1,2);
dumpValue(qqq4);
