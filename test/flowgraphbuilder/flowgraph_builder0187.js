var x = {x:1,y:2};
var y = {};
TAJS_dumpObject(y);

for (p in x) {
	y[p] = x[p];
	TAJS_dumpObject(y);
}

TAJS_dumpObject(y);
