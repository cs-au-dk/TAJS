var foo = {};
var obj = {a : 1, b : 2};
var names = Object.keys(obj);

function f(name) {
    return obj[name];
}
var i = 0;
while (i < names.length) {
    var name_obj = {name: names[i]};

    foo[name_obj.name] = f(name_obj.name); // Split on name_obj.name
    i++;
}

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}

if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}
