var x = {valueOf: function() {return 1;}}

var y = {};

y.foo = x+x;

TAJS_dumpValue(y.foo);
