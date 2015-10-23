function f(a){
    TAJS_addContextSensitivity("a");
    if(a == "xxxxxxxxxx")
        return a;
    return f(a + "x");
}
var v0 = f("");
var v1 = f("x");
var v2 = f("xx");
TAJS_assert(v0, 'isMaybeSingleStr');
TAJS_assert(v1, 'isMaybeSingleStr');
TAJS_assert(v2, 'isMaybeSingleStr');
