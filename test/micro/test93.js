function f(x,y) {
	return x + y;
}

var a = f(1,2);
var b = f(1,"2");

TAJS_dumpValue(a);
TAJS_dumpValue(b);
