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
		dumpState();
		v1 = v1 + "#";
		v2 = v2 + "#";
		w1 = w1 + "#";
		w2 = w2 + "#";
		var foo = 42;
		dumpValue(v1);
		dumpValue(v2);
		dumpValue(w1);
		dumpValue(w2);
		dumpValue(foo);
	}
	dumpState();
}
dumpState();
dumpValue(x.v1);
dumpValue(y.w1);
dumpValue(foo);
