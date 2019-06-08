var foo = {};
var obj = {a : 1, b : 2};
var names = Object.keys(obj);

for (var i = 0; i < names.length; i++) {
    var name_obj = {name: names[i]};

    foo[name_obj.name] = obj[name_obj.name]; // Split on name_obj.name
}

if (foo.a !== undefined) {
    TAJS_assertEquals(1, foo.a);
}

if (foo.b !== undefined) {
    TAJS_assertEquals(2, foo.b);
}


