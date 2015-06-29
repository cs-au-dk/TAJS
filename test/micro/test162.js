var x = 1;
function f() {
	return 2;
	x = 3; // should be reported as unreachable
}
var y = f();
TAJS_dumpValue(y);
TAJS_dumpValue(x);
