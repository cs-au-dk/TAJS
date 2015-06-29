function foo(x,y) {
	return arguments[0] + arguments["1"] + arguments[2];
}
function foo2(x,y) {
	return arguments[0] + arguments["1"] + arguments[2];
}
function foo3(x,y) {
	return arguments[0] + arguments["1"] + arguments[2];
}

function foo4(x,y) {
	return arguments[0] + arguments["1"] + arguments[2];
}

var qqq1 = foo(101, 102, 103);
TAJS_dumpValue(qqq1);
TAJS_assert(qqq1 == 306);

var qqq2 = foo("x101", "x102", "x103");
TAJS_dumpValue(qqq2);
TAJS_assert(qqq2, 'isMaybeSingleNum||isMaybeStrIdentifierParts');

var qqq22 = foo2("x101", "x102", "x103");
TAJS_dumpValue(qqq22);
TAJS_assert(qqq22 == "x101x102x103");

var qqq3 = foo3(101, 102, 103, 104);
TAJS_dumpValue(qqq3);
TAJS_assert(qqq3 == 306);

var qqq4 = foo4(101, 102);
TAJS_dumpValue(qqq4);
TAJS_assert(isNaN(qqq4));
TAJS_dumpValue(isNaN(qqq4));

function bar(x,y) {
	this.qwerty = arguments[0] + arguments[1] + arguments[2];
}

var qqq1 = new bar(101, 102, 103);
TAJS_dumpValue(qqq1.qwerty);
