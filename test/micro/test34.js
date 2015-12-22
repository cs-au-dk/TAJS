var foo = 42;
foo = foo;
function f() {
	return foo;
}
TAJS_dumpValue(f());
TAJS_assert(this.foo == 42);
TAJS_dumpValue(this.foo);
