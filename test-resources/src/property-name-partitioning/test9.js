var foo = {};
var obj = {a: 1, b: 2};
var names = Object.keys(obj);

names.forEach(function f(name) {
    var tmp;
    if (TAJS_make('AnyBool')) {
        tmp = obj[name];
    } else {
        tmp = obj[name]
    }

    foo[name] = tmp; // Split on name
})

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}
if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}
TAJS_dumpValue(foo.a)
TAJS_dumpValue(foo.b)