var foo = function() {
	this.baz = 42;
} 

var x1 = new foo();
var x2 = new foo();

TAJS_dumpValue(x1.baz);
TAJS_dumpValue(x2.baz);
