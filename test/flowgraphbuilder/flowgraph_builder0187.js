var x = {x:1,y:2};
var y = {};
dumpObject(y);

for (p in x) {
	y[p] = x[p];
	dumpObject(y);
}

dumpObject(y);
