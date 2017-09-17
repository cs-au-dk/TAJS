var erct = 42;

function foo(x) {
	var y = 6;
	function bar() {return x + y + erct}
	return bar()
}
var www = foo(12);
TAJS_dumpValue(www);
