var foo = function() {
	this.baz = 42;
} 

var x1 = new foo();
var x2 = new foo();

dumpValue(x1.baz);
dumpValue(x2.baz);
