TAJS_makeContextSensitive(f, 0);
TAJS_makeContextSensitive(g, 0);
var CONSTANT = 1;

// when only varying the non-sensitive parameter, the
// context sensitivity should _not_ be toggled.
function f(a,b){
    return a+b;
}

var f_c1 = f(CONSTANT,2);
TAJS_assert(f_c1, "isMaybeNumUInt", false);
var f_c2 = f(CONSTANT,3);
TAJS_assert(f_c2, "isMaybeNumUInt", true);


// this function proves that it is the variance of the
// second parameter which forces the context sensitivity
function g(a,b){
    return a+b;
}
var g_c1 = g(CONSTANT,CONSTANT);
TAJS_assert(g_c1, "isMaybeNumUInt", false);
var g_c2 = g(CONSTANT + 1,CONSTANT);
TAJS_assert(g_c2, "isMaybeNumUInt", false);
