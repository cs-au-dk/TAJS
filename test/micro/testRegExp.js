var a = new RegExp("ab*", "ig");
TAJS_dumpValue(a);
TAJS_dumpValue(a.source);
TAJS_dumpValue(a.lastIndex);
TAJS_dumpValue(a.ignoreCase);
var b = RegExp("c|d");
TAJS_dumpValue(b);
var c = RegExp(a);
TAJS_dumpValue(c);

var e = a.exec("abbb");
TAJS_dumpValue(e);
TAJS_dumpValue(e.length);
TAJS_dumpValue(e.input);
TAJS_dumpValue(e.index);
TAJS_dumpValue(e[0]);
TAJS_dumpValue(e[1]);

TAJS_dumpValue(a.toString());

var r = /ab+/ig
TAJS_dumpValue(r);


var f = new Object();
f.tos = a.exec;
f.tos("nudel")
var d = RegExp(c, "xxxxyyyy");

