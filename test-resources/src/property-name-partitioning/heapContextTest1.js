var fun = function() {};
var obj2 = {a:1, b:2};

(function() {
    var name = TAJS_join("a", "b");
    var val = fun[name] = obj2[name];
//    TAJS_dumpState();
    fun.prototype[name] = function () {
//        TAJS_dumpState()
           this;
        TAJS_assertEquals(1, val);
    }
//    TAJS_dumpState();
}())

TAJS_dumpState();
var x = new fun()
x.a();
