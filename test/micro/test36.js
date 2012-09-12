this.foo = 42;
function q() {
	foo = "sdf";
}
dumpValue(foo);
q();
dumpValue(foo);
assert(foo == "sdf");
