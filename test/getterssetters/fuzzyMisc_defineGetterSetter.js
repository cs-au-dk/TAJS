var ALL_IDENT = Math.random() ? "foo" : "bar";
var SINGLE_IDENT = 'p';
var ALL_UINT = Math.random() ? 2 : 3;
var ALL_STRISH = Math.random() ? ALL_IDENT : (ALL_UINT + "");

function strongOverride() {
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
    Object.prototype.__defineGetter__.call(o, SINGLE_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, SINGLE_IDENT, function () {
        other_set_triggered = true;
    });
    o[SINGLE_IDENT];
    o[SINGLE_IDENT] = 42;

    TAJS_assert(!get_triggered);
    TAJS_assert(!set_triggered);
    TAJS_assert(other_get_triggered);
    TAJS_assert(other_set_triggered);
}

function strongOverride_fuzzyAccess() {
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
    Object.prototype.__defineGetter__.call(o, SINGLE_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, SINGLE_IDENT, function () {
        other_set_triggered = true;
    });
    o[ALL_STRISH];
    o[ALL_STRISH] = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(other_get_triggered, 'isMaybeAnyBool');
    TAJS_assert(other_set_triggered, 'isMaybeAnyBool');
}

function weakOverride() {
    var get_triggered = false;
    var set_triggered = false;
    var other_get_triggered = false;
    var other_set_triggered = false;
    var o = {};

    Object.prototype.__defineGetter__.call(o, SINGLE_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, SINGLE_IDENT, function () {
        other_set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_STRISH, function () {
        get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_STRISH, function () {
        set_triggered = true;
    });
    o[SINGLE_IDENT];
    o[SINGLE_IDENT] = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(other_get_triggered, 'isMaybeAnyBool');
    TAJS_assert(other_set_triggered, 'isMaybeAnyBool');
}

function weakOverride_fuzzyAccess() {
    var get_triggered = false;
    var set_triggered = false;
    var other_get_triggered = false;
    var other_set_triggered = false;
    var o = {};

    Object.prototype.__defineGetter__.call(o, SINGLE_IDENT, function () {
        other_get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, SINGLE_IDENT, function () {
        other_set_triggered = true;
    });
    Object.prototype.__defineGetter__.call(o, ALL_STRISH, function () {
        get_triggered = true;
    });
    Object.prototype.__defineSetter__.call(o, ALL_STRISH, function () {
        set_triggered = true;
    });
    o[ALL_STRISH];
    o[ALL_STRISH] = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(other_get_triggered, 'isMaybeAnyBool');
    TAJS_assert(other_set_triggered, 'isMaybeAnyBool');
}

strongOverride();
strongOverride_fuzzyAccess();
weakOverride();
weakOverride_fuzzyAccess();
while (Math.random()) { // backwards flow
    strongOverride();
    strongOverride_fuzzyAccess();
    weakOverride();
    weakOverride_fuzzyAccess();
}

