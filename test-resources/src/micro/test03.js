function fooo() {
	return Math.random();
}

var x = {};

if (fooo())
	x.bar = 1234;
else
	x.bar = 2345;

TAJS_dumpValue(x.bar);
