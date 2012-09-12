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
dumpValue(foo);
dumpValue(foo2);

bar1 = foo(42,87,123);
bar2 = this.foo(42,88,123);
bar3 = foo(42);
dumpValue(bar1);
dumpValue(bar2);
dumpValue(bar3);
dumpValue(bar4);
