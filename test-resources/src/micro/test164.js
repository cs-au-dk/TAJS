function f1() {
	this.v1 = "v1";
}
function f2() {
	this.v2 = "v2";
}
f2.prototype = new f1;
var x = new f2();

function g1() {
	this.w1 = "w1";
}
function g2() {
	this.w2 = "w2";
}
g2.prototype = new g1;
var y = new g2();

with (x) {
	with (y) {
		TAJS_dumpState();
		v1 = v1 + "#";
		v2 = v2 + "#";
		w1 = w1 + "#";
		w2 = w2 + "#";
		var foo = 42;
		TAJS_dumpValue(v1);
		TAJS_dumpValue(v2);
		TAJS_dumpValue(w1);
		TAJS_dumpValue(w2);
		TAJS_dumpValue(foo);
	}
	TAJS_dumpState();
}
TAJS_dumpState();
TAJS_dumpValue(x.v1);
TAJS_dumpValue(y.w1);
TAJS_dumpValue(foo);
