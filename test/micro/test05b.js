var foo2 = function bar2() {
	this.baz2 = 43;
}

var xxx = new foo2();
TAJS_dumpValue(xxx); // * or @ ?
