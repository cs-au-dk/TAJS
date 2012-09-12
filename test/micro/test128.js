/*
 * Access to the dummy scope object created at recursive functions.
 */

var scope;

var f = function g(x) {
	dumpValue(x)
	if (x == 0) {
		return this;
	}
	else {
		scope = g(0); // 'g' evaluates to a reference whose base is the dummy scope object created for the recursive function
	}
}
f(1);


dumpValue(scope); // should be the dummy object
assert(scope !== this);