var ALL_IDENT = Math.random() ? "foo" : "bar";
var ALL_UINT = Math.random() ? 2 : 3;
var ALL_STRISH = Math.random()? ALL_IDENT: (ALL_UINT + "");
var SINGLE_UINT = 87;

function fuzzyDefinition() {
    var get_triggered = false;
    var set_triggered = false;
    var other_get_triggered = false;
    var other_set_triggered = false;
    var o = {};

    Object.prototype.__defineGetter__.call(o, ALL_STRISH, function () {
        get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_STRISH, function () {
        set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_IDENT, function () {
        other_set_triggered = true;
    });
    o[SINGLE_UINT];
    o[SINGLE_UINT] = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(!other_get_triggered);
    TAJS_assert(!other_set_triggered);
}

function fuzzyRead() {
    var get_triggered = false;
    var set_triggered = false;
    var other_get_triggered = false;
    var other_set_triggered = false;
    var o = {};

    Object.prototype.__defineGetter__.call(o, SINGLE_UINT, function () {
        get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, SINGLE_UINT, function () {
        set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_IDENT, function () {
        other_set_triggered = true;
    });

    o[ALL_STRISH];
    o[SINGLE_UINT] = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered);
    TAJS_assert(other_get_triggered, 'isMaybeAnyBool');
    TAJS_assert(!other_set_triggered);
}
function fuzzyWrite() {
    var get_triggered = false;
    var set_triggered = false;
    var other_get_triggered = false;
    var other_set_triggered = false;

    var o = {};
    Object.prototype.__defineGetter__.call(o, SINGLE_UINT, function () {
        get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, SINGLE_UINT, function () {
        set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_IDENT, function () {
        other_set_triggered = true;
    });
    o[SINGLE_UINT];
    o[ALL_STRISH] = 42;

    TAJS_assert(get_triggered);
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(!other_get_triggered);
    TAJS_assert(other_set_triggered, 'isMaybeAnyBool');
}
function fuzzyRedefinition_1() {
    var get_triggered = false;
    var set_triggered = false;
    var other_get_triggered = false;
    var other_set_triggered = false;

    var o = {p: 'foo'};
    Object.prototype.__defineGetter__.call(o, ALL_STRISH, function () {
        get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_STRISH, function () {
        set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_IDENT, function () {
        other_set_triggered = true;
    });
    o[SINGLE_UINT];
    o[SINGLE_UINT] = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(o[SINGLE_UINT], 'isMaybeSingleNum||isMaybeSingleStr');
    TAJS_assert(!other_get_triggered);
    TAJS_assert(!other_set_triggered);
}
function fuzzyRedefinition_2() {
    var old_get_triggered = false;
    var old_set_triggered = false;
    var other_get_triggered = false;
    var other_set_triggered = false;

    var get_triggered = false;
    var set_triggered = false;

    var o = {};
    Object.prototype.__defineGetter__.call(o, SINGLE_UINT, function () {
        old_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, SINGLE_UINT, function () {
        old_set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_STRISH, function () {
        get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_STRISH, function () {
        set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_IDENT, function () {
        other_set_triggered = true;
    });
    o[SINGLE_UINT];
    o[SINGLE_UINT] = 42;

    TAJS_assert(old_get_triggered, 'isMaybeAnyBool');
    TAJS_assert(old_set_triggered, 'isMaybeAnyBool');
    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(o[SINGLE_UINT] === undefined);
    TAJS_assert(!other_get_triggered);
    TAJS_assert(!other_set_triggered);
}

fuzzyDefinition();
fuzzyRead();
fuzzyWrite();
fuzzyRedefinition_1();
fuzzyRedefinition_2();

while (Math.random()) { // backwards flow
    fuzzyDefinition();
    fuzzyRead();
    fuzzyWrite();
    fuzzyRedefinition_1();
    fuzzyRedefinition_2();
}

