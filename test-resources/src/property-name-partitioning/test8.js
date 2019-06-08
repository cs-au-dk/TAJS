var foo = {};
var obj = {a: 1, b: 2};
var names = Object.keys(obj);

names.forEach(function f(name) {
    foo[name] = obj[name]; // Split on name
})

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}
if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}
TAJS_dumpValue(foo.a)
TAJS_dumpValue(foo.b)