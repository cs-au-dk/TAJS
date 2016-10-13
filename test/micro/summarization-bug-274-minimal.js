function mk() {
    return {};
}
function g() {
    mk();
    mk();
}
function f() {
    var obj = mk();
    obj.prop = 42;
    g();
    TAJS_dumpValue(obj.prop);
    TAJS_assert(obj.prop, 'isMaybeOtherThanUndef');
}
mk();
f();
