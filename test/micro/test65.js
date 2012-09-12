function f(x) {
	return x+x;
}
var v = f("a") + f("b");
dumpValue(v); // "aabb"

var obj = {aabb:42};

dumpValue(obj[v]); // 42

obj[v] = "foo";
dumpValue(obj[v]); // "foo"
dumpValue(obj.aabb); // "foo"

var c = 487;
function r() {
	return c;
}
var ww = r();
dumpValue(ww); // 487.0


var bb = 123;
bb = !bb;
dumpValue(bb); // false

var qwe = {y1:true}
dumpValue("y1" in qwe); // true
dumpValue(v in qwe); // false
dumpValue("y2" in qwe); //false

dumpValue(typeof(42)); // "number"

function F() {
	this.a = 123;
}
F.prototype = "dyt";

function F2() {return 1234345}
F2.prototype = new F;

var pp = new F();
dumpObject(pp);
var pp2 = new F2();
dumpObject(pp2);
