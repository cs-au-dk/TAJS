function f(x) {
	return x+x;
}
var v = f("a") + f("b");
TAJS_dumpValue(v); // "aabb"

var obj = {aabb:42};

TAJS_dumpValue(obj[v]); // 42

obj[v] = "foo";
TAJS_dumpValue(obj[v]); // "foo"
TAJS_dumpValue(obj.aabb); // "foo"

var c = 487;
function r() {
	return c;
}
var ww = r();
TAJS_dumpValue(ww); // 487.0


var bb = 123;
bb = !bb;
TAJS_dumpValue(bb); // false

var qwe = {y1:true}
TAJS_dumpValue("y1" in qwe); // true
TAJS_dumpValue(v in qwe); // false
TAJS_dumpValue("y2" in qwe); //false

TAJS_dumpValue(typeof(42)); // "number"

function F() {
	this.a = 123;
}
F.prototype = "dyt";

function F2() {return 1234345}
F2.prototype = new F;

var pp = new F();
TAJS_dumpObject(pp);
var pp2 = new F2();
TAJS_dumpObject(pp2);
