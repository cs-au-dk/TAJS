var objA = {};

var objB = {foo: function() {}};

var fun = function(name) {
    objA[name] = objB.foo;
};

var anyString = thresher_debug_top ? "foo" : "bar";

fun(anyString);