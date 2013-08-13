var x = {x:1,y:2};
var y = {};

for (p in x) {
	y[p] = x[p];
	if (p==="y")
		throw {};
}

dumpObject(y);
