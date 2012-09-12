/*
 * Attempt to get access to an activation object (which should not be possible).
 */

var scope;

function f() {
	var g = function () {return this;}
	scope = g();
}
f();

dumpValue(scope);
assert(scope === this);
