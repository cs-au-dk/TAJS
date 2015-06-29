var x = ["a","b","c"];
for (var i = 0; i < x.length; i++) {
	TAJS_dumpValue(i);
}

var q1 = 0, q2 = 1;
var w;
if (Math.random())
	w1 = q1;
else
	w1 = q2;
TAJS_dumpValue(w1);
var w2 = w1 + 1;
TAJS_dumpValue(w2);

TAJS_dumpValue(w1.toString());
TAJS_dumpValue(w2.toString());

