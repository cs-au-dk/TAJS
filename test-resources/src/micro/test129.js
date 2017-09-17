/*
 * Access to the 'with' object via a reference type
 */

var scope;

var foo = {f:function() {return this}}

with (foo) {
	scope = f();
}


TAJS_dumpValue(scope); // should be the foo object
TAJS_assert(scope === foo);