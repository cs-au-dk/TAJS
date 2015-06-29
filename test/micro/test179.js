var x = "hello";
var a = [], b = [];
for (var p in x) {
	TAJS_dumpValue(p); // uint
	TAJS_dumpValue(x[p]); // string
	a.push(p);
	b.push(x[p]);
}
TAJS_dumpObject(a); // ["0", "1", "2", "3", "4"] (array of uints)
TAJS_dumpObject(b); // ["h", "e", "l", "l", "o"] (array of strings)
