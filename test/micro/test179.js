var x = "hello";
var a = [], b = [];
for (var p in x) {
	dumpValue(p); // uint
	dumpValue(x[p]); // string
	a.push(p);
	b.push(x[p]);
}
dumpObject(a); // ["0", "1", "2", "3", "4"] (array of uints)
dumpObject(b); // ["h", "e", "l", "l", "o"] (array of strings)
