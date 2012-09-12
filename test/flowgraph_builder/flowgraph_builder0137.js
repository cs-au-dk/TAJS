var foo = {Object: function() {return this;}};

with (foo) {
 dumpValue(Object(null));
 dumpValue(foo); // should give the same value as the previous line
}

