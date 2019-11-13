var v;
function f(x) {
    var res = function () {
        var y = x;
        return v;
    }
    if (Math.random())
        v = x;
    return res;
}

var f1 = f(function() { return 2});
var f2 = f(function() { return "foo"});

var res = f1()();