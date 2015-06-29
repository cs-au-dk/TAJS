this.foo = 42;

function q() {
	foo = "sdf";
}

TAJS_dumpValue(foo);
q();
TAJS_dumpValue(foo);
TAJS_assert(foo == "sdf");
