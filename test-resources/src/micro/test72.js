var foo1 = function() {
	this.baz1 = 42;
} 
var foo2 = function() {}

foo2.prototype = new foo1();

var xxx = new foo2();
var y1 = xxx.baz1;
TAJS_dumpValue(y1); 

new foo1();

TAJS_assert(y1 === 42);
TAJS_dumpValue(y1); 
