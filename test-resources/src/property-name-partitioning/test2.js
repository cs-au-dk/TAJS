var foo = {};
var obj = {a : 1, b : 2};
var names = Object.keys(obj);

var name = names[0];

function f(name) {
    return obj[name];
}

foo[name] = f(name); // Split on name

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}

if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}

