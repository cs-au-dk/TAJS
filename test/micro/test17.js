var b;
var p;
var u;

if (Math.random()) {
	b = true;
	q = 42;
	p = "foo";
} else {
	b = false;
}

TAJS_assert(b, 'isMaybeAnyBool');
TAJS_dumpValue(b);

TAJS_dumpValue(q);
TAJS_dumpValue(p);

TAJS_dumpValue(u);
