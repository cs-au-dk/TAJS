var foo = {};
var obj = {a: 1, b: 2};
var names = Object.keys(obj);

var i = 0;
var name = names[i];
obj[name]; // force partitioning of name
var t = Object.getOwnPropertyDescriptor(obj, name);
Object.defineProperty(foo, name, t);

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}
if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}

TAJS_dumpValue(foo.a)
TAJS_dumpValue(foo.b)