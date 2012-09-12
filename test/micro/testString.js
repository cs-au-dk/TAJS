// String objects
// 15.5.1.1
var a = String();
var b = String(true);
var c = String(77);

dumpValue(a);
dumpValue(b);
dumpValue(c);
dumpValue(c.valueOf());

var d = new String();
var e = new String(false);
var f = new String(77.77);

dumpValue(d);
dumpValue(d.valueOf());
dumpValue(d.length);
dumpValue(e);
dumpValue(e.valueOf());
dumpValue(e.toUpperCase());
dumpValue(e.length);
dumpValue(f);
dumpValue(f.toString());
dumpValue(f.length);

dumpValue(String.fromCharCode());
dumpValue(String.fromCharCode(65, 66, 67, 68, 69, 70));

dumpValue(f.charAt(2));
dumpValue(f.charAt(140));

dumpValue(f.charCodeAt(2));
dumpValue(f.charCodeAt(140));
var g = new Number(42.125);
g.charCodeAt = f.charCodeAt;
dumpValue(g.charCodeAt(3));


dumpValue(f.concat(e, "finish"));
g.concat = f.concat;
dumpValue(g.concat(g));

var h = new String("testing indexOf");
dumpValue(h.indexOf("i"));
dumpValue(h.indexOf("i", 7));
dumpValue(h.indexOf("i", 40));
g.indexOf = h.indexOf;
dumpValue(g.indexOf("."));

dumpValue(h.lastIndexOf("i"));
dumpValue(h.lastIndexOf("i", 7));
dumpValue(h.lastIndexOf("i", 1));
g.lastIndexOf = h.lastIndexOf;
dumpValue(g.lastIndexOf("."));

var i = new String("testing localeCompare");
dumpValue(i.localeCompare(i));
dumpValue(i.localeCompare("test"));
dumpValue(i.localeCompare("utest"));
dumpValue(i.localeCompare());

// var j = new String("testing match");
// dumpValue(j.match(/test/));
// dumpValue(j.match(new RegExp("test")));
