var x = ["a","b","c"];
for (var i = 0; i < x.length; i++) {
	dumpValue(i);
}

var q1 = 0, q2 = 1;
var w;
if (Math.random())
	w1 = q1;
else
	w1 = q2;
dumpValue(w1);
var w2 = w1 + 1;
dumpValue(w2);

dumpValue(w1.toString());
dumpValue(w2.toString());

