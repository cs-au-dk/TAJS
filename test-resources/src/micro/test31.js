function foo(x,y) {
	this.baz = y;
	return x + y;
}
bar2 = this.foo(42,88,123);
TAJS_dumpValue(bar2);
TAJS_dumpValue(baz);
