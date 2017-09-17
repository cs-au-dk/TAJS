function foo4() {
	return arguments[0] + arguments["1"];
}

var qqq4 = foo4(1,2);
TAJS_dumpValue(qqq4);
