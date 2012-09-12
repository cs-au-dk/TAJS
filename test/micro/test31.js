function foo(x,y) {
	this.baz = y;
	return x + y;
}
bar2 = this.foo(42,88,123);
dumpValue(bar2);
dumpValue(baz);
