function weakDefinition() {
    var get_triggered = false;
    var set_triggered = false;
    var o = {};
    if (Math.random()) {
        o.__defineGetter__('p', function () {
            get_triggered = true;
        });
        o.__defineSetter__('p', function () {
            set_triggered = true;
        });
    }
    o.p;
    o.p = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
}

function weakRead() {
    var get_triggered = false;
    var set_triggered = false;
    var o = {};

    o.__defineGetter__('p', function () {
        get_triggered = true;
    });
    o.__defineSetter__('p', function () {
        set_triggered = true;
    });

    if (Math.random()) {
        o.p;
    }
    o.p = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered);
}
function weakWrite() {
    var get_triggered = false;
    var set_triggered = false;
    var o = {};
    o.__defineGetter__('p', function () {
        get_triggered = true;
    });
    o.__defineSetter__('p', function () {
        set_triggered = true;
    });
    o.p;
    if (Math.random()) {
        o.p = 42;
    }

    TAJS_assert(get_triggered);
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
}
function weakRedefinition_1() {
    var get_triggered = false;
    var set_triggered = false;
    var o = {p: 'foo'};
    if (Math.random()) {
        o.__defineGetter__('p', function () {
            get_triggered = true;
        });
        o.__defineSetter__('p', function () {
            set_triggered = true;
        });
    }
    o.p;
    o.p = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(o.p, 'isMaybeSingleNum||isMaybeSingleStr');
}
function weakRedefinition_2() {
    var old_get_triggered = false;
    var old_set_triggered = false;

    var get_triggered = false;
    var set_triggered = false;

    var o = {};
    o.__defineGetter__('p', function () {
        old_get_triggered = true;
    });
    o.__defineSetter__('p', function () {
        old_set_triggered = true;
    });
    if (Math.random()) {
        o.__defineGetter__('p', function () {
            get_triggered = true;
        });
        o.__defineSetter__('p', function () {
            set_triggered = true;
        });
    }
    o.p;
    o.p = 42;

    TAJS_assert(old_get_triggered, 'isMaybeAnyBool');
    TAJS_assert(old_set_triggered, 'isMaybeAnyBool');
    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(o.p === undefined);
}
weakDefinition();
weakRead();
weakWrite();
weakRedefinition_1();
weakRedefinition_2();

while (Math.random()) { // backwards flow
    weakDefinition();
    weakRead();
    weakWrite();
    weakRedefinition_1();
    weakRedefinition_2();
}