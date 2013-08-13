var x = {foo: 1, bar: 2};

var y = {};

var i = 0;

for (var p in x) {
	y[p] = x[p];
	i++;
}

dumpValue(y);
dumpValue(i);
dumpObject(y);
