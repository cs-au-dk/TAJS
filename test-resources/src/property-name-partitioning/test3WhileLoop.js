var foo = {};
var obj = {a : 1, b : 2};
var names = Object.keys(obj);

function f(name) {
    return obj[name];
}

var i = 0
while (i < names.length) {
    var name = names[i];

    var name2 = name; // Split on name line 13
    foo[name] = f(name2);
    i++;
}

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}

if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}