var x = {a:42};
TAJS_dumpValue(delete x.a); // true
TAJS_dumpValue(x.a); // Undef
TAJS_dumpValue(delete x.b); // true
TAJS_dumpValue(x.b); // Undef
TAJS_dumpValue(delete x); // false, cannot delete because property has [[DontDelete]]
TAJS_dumpValue(x);

y = 7;
TAJS_dumpValue(delete y); // true
// TAJS_dumpValue(y); // ReferenceError

(function() {
	var x = 7;
	TAJS_dumpValue(delete x); // false, cannot delete because property has [[DontDelete]]
	TAJS_dumpValue(x); // not deleted

	z = 8;
	TAJS_dumpValue(delete z); // true
	// TAJS_dumpValue(z); // ReferenceError
})();

TAJS_dumpValue(delete q); // true

function f() {}
TAJS_dumpValue(delete f); // false, cannot delete because property has [[DontDelete]]

