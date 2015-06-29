// String objects
// 15.5.1.1
var a = String();
var b = String(true);
var c = String(77);

TAJS_dumpValue(a);
TAJS_dumpValue(b);
TAJS_dumpValue(c);
TAJS_dumpValue(c.valueOf());

var d = new String();
var e = new String(false);
var f = new String(77.77);

TAJS_dumpValue(d);
TAJS_dumpValue(d.valueOf());
TAJS_dumpValue(d.length);
TAJS_dumpValue(e);
TAJS_dumpValue(e.valueOf());
TAJS_dumpValue(e.toUpperCase());
TAJS_dumpValue(e.length);
TAJS_dumpValue(f);
TAJS_dumpValue(f.toString());
TAJS_dumpValue(f.length);

TAJS_dumpValue(String.fromCharCode());
TAJS_dumpValue(String.fromCharCode(65, 66, 67, 68, 69, 70));

TAJS_dumpValue(f.charAt(2));
TAJS_dumpValue(f.charAt(140));

TAJS_dumpValue(f.charCodeAt(2));
TAJS_dumpValue(f.charCodeAt(140));
var g = new Number(42.125);
g.charCodeAt = f.charCodeAt;
TAJS_dumpValue(g.charCodeAt(3));


TAJS_dumpValue(f.concat(e, "finish"));
g.concat = f.concat;
TAJS_dumpValue(g.concat(g));

var h = new String("testing indexOf");
TAJS_dumpValue(h.indexOf("i"));
TAJS_dumpValue(h.indexOf("i", 7));
TAJS_dumpValue(h.indexOf("i", 40));
g.indexOf = h.indexOf;
TAJS_dumpValue(g.indexOf("."));

TAJS_dumpValue(h.lastIndexOf("i"));
TAJS_dumpValue(h.lastIndexOf("i", 7));
TAJS_dumpValue(h.lastIndexOf("i", 1));
g.lastIndexOf = h.lastIndexOf;
TAJS_dumpValue(g.lastIndexOf("."));

var i = new String("testing localeCompare");
TAJS_dumpValue(i.localeCompare(i));
TAJS_dumpValue(i.localeCompare("test"));
TAJS_dumpValue(i.localeCompare("utest"));
TAJS_dumpValue(i.localeCompare());

// var j = new String("testing match");
// TAJS_dumpValue(j.match(/test/));
// TAJS_dumpValue(j.match(new RegExp("test")));
