var foo = {};
var obj = {a: 1, b: 2};
var names = Object.keys(obj);

var i = 0;

while (i < names.length) {if (true) 1; else 2;
    var name = names[i++];
    var t = obj[name]; // Split on name
    foo[name] = t
}

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}
if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}
TAJS_dumpValue(foo.a)
TAJS_dumpValue(foo.b)