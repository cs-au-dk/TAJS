var foo = {};
var obj = {a : 1, b : 2};

function f(name) {
    return obj[name];
}

for (var name in obj) {
    var name_obj = {name: name};

    var name_obj2 = name_obj;
    foo[name_obj.name] = f(name_obj2.name); // Split on .name
}

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}

if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}