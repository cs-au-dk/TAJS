this.foo = function(xx) {
	this.fooo = 54;
	return xx + 42;
}

var x = this.foo(2222);
var y = this.fooo;

TAJS_assert(x == 2264);
TAJS_assert(y === 54);
TAJS_dumpValue(y);
