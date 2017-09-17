function foo(x,y) {
	this.baz = y;
	return x + y;
}

var t = foo;
if (Math.random()) {
  foo = t;
} else {
  foo = 44;
}
foo2 = t;
TAJS_dumpValue(foo);
TAJS_dumpValue(foo2);

bar1 = foo(42,87,123);
bar2 = this.foo(42,88,123);
bar3 = foo(42);
TAJS_dumpValue(bar1);
TAJS_dumpValue(bar2);
TAJS_dumpValue(bar3);
TAJS_dumpValue(bar4);
