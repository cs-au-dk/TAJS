function person(g) {
    this.age = 5678 + g;
}
var linus = new person(6000);
var rms = linus.age;

TAJS_assert(rms == 11678);
TAJS_dumpValue(rms);

function foo(x) {
	x = x +1;
	function qwerty() {
		return x + 2;
	}
	return qwerty() + 1234;
}

var bar = foo(5678);
var bar2 = foo(5678);
TAJS_dumpValue(bar);
TAJS_assert(bar == 6915);
