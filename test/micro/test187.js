var x = {a:42};
dumpValue(delete x.a); // true
dumpValue(x.a); // Undef
dumpValue(delete x.b); // true
dumpValue(x.b); // Undef
dumpValue(delete x); // false, cannot delete because property has [[DontDelete]]
dumpValue(x);

y = 7;
dumpValue(delete y); // true
// dumpValue(y); // ReferenceError

(function() {
	var x = 7;
	dumpValue(delete x); // false, cannot delete because property has [[DontDelete]]
	dumpValue(x); // not deleted

	z = 8;
	dumpValue(delete z); // true
	// dumpValue(z); // ReferenceError
})();

dumpValue(delete q); // true

function f() {}
dumpValue(delete f); // false, cannot delete because property has [[DontDelete]]

