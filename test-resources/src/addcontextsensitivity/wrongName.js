function f(a){
    TAJS_addContextSensitivity("b");
    return a;
}
var v1 = f(1);
var v2 = f(2);
var v3 = v1 + v2;
