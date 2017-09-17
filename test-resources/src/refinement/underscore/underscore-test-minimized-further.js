var foo = {
  bar: function() {return 1;},
  baz: function() {return 2;}
};

function qux() {
    var _ = {};
    for(var name in foo){
        _[name] = foo[name];
    }
    TAJS_dumpObject(_);
    return _;
    // var anyString = Math.random() ? "bar" : "baz";
    // _[anyString] = foo[anyString];
};

// TAJS_dumpObject(_);
TAJS_dumpObject(qux());
// TAJS_dumpObject(_);
// TAJS_assert(_.bar != undefined);
// TAJS_assert(_.baz != undefined);
// TAJS_assert(_.qux == undefined);