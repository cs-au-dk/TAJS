var x = {x:1,y:2};
var y = {};

foo:
for (p in x) {
	y[p] = x[p];
	if (p==="y")
		break foo;
}

TAJS_dumpObject(y);
