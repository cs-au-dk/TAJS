var foo = 42;
foo = foo;
function f() {
	return foo;
}
dumpValue(f());
assert(this.foo == 42);
dumpValue(this.foo);
