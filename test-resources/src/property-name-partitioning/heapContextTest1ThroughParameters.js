var fun = function() {};
var obj2 = {a:1, b:2};

(function() {
    var name = TAJS_make("AnyStr");
    var val = fun[name] = obj2[name];
    (function(name, val) {
        fun.prototype[name] = function () {
            TAJS_assertEquals(2, val);
        }
    }(name, val))
}())

var x = new fun()
x.b();
