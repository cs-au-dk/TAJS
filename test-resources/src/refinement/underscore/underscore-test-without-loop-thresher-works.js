var objA = {};

var objB = {foo: function() {}};

var anyString = thresher_debug_top ? "foo" : "bar";

objA[anyString] = objB.foo;