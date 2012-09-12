var a = new RegExp("ab*", "ig");
dumpValue(a);
dumpValue(a.source);
dumpValue(a.lastIndex);
dumpValue(a.ignoreCase);
var b = RegExp("c|d");
dumpValue(b);
var c = RegExp(a);
dumpValue(c);

var e = a.exec("abbb");
dumpValue(e);
dumpValue(e.length);
dumpValue(e.input);
dumpValue(e.index);
dumpValue(e[0]);
dumpValue(e[1]);

dumpValue(a.toString());

var r = /ab+/ig
dumpValue(r);


var f = new Object();
f.tos = a.exec;
f.tos("nudel")
var d = RegExp(c, "xxxxyyyy");

