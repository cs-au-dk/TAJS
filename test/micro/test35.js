this.foo = function(xx) {
	this.fooo = 54;
	return xx + 42;
}

var x = this.foo(2222);
var y = this.fooo;

assert(x == 2264);
assert(y === 54);
dumpValue(y);
