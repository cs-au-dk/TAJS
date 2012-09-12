var foo1 = function bar1(x1) {
	this.baz1 = 42;
	return x1+123;
}
var foo2 = function bar2(x2) {
	this.baz2 = 43;
	return x2+124;
}

foo2.prototype = new foo1(456);

var xxx = new foo2(89);
xxx.baz3 = 44;
var y1 = xxx.baz1;
var y2 = xxx.baz2;
var y3 = xxx.baz3;

var z1 = foo1(-23);

assert(y1 === 42); 
assert(y2 === 43);
assert(y3 === 44);
assert(z1 === 100); 

dumpValue(y1); 
dumpValue(z1);
