var foo = {};
var obj = {a: 1, b: 2};
var names = Object.keys(obj);

var i = 0;
var name = names[i];
(function(name) {
    foo[name] = obj[name]; // Split on name
}(name));

TAJS_assertEquals(TAJS_join(1, undefined), foo.a);
TAJS_assertEquals(TAJS_join(2, undefined), foo.b);

TAJS_dumpValue(foo.a)
TAJS_dumpValue(foo.b)