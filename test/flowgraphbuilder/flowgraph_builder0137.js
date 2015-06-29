var foo = {Object: function() {return this;}};

with (foo) {
 TAJS_dumpValue(Object(null));
 TAJS_dumpValue(foo); // should give the same value as the previous line
}

