/*
 * Access to the dummy scope object created at 'catch'
 */

var scope;

try {
	throw (function() {
		return this;
	})
} catch (x) {
	scope = x(); // 'x' evaluates to a reference whose base is the dummy scope object (which is not an activation object)
}

TAJS_dumpValue(scope); // should be the dummy object
TAJS_assert(scope !== this);