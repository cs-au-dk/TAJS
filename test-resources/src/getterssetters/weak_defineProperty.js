function weakDefinition() {
    var get_triggered = false;
    var set_triggered = false;
    var o = {};
    if (Math.random()) {
        Object.defineProperty(o, 'p', {
            get: function () {
                get_triggered = true;
            },
            set: function () {
                set_triggered = true;
            }
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

    Object.defineProperty(o, 'p', {
        get: function () {
            get_triggered = true;
        },
        set: function () {
            set_triggered = true;
        }
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
    Object.defineProperty(o, 'p', {
        get: function () {
            get_triggered = true;
        },
        set: function () {
            set_triggered = true;
        }
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
        Object.defineProperty(o, 'p', {
            get: function () {
                get_triggered = true;
            },
            set: function () {
                set_triggered = true;
            }
        });
    }
    o.p;
    o.p = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
    TAJS_assert(o.p, 'isMaybeSingleNum||isMaybeSingleStr');
}
weakDefinition();
weakRead();
weakWrite();
weakRedefinition_1();

while (Math.random()) { // backwards flow
    weakDefinition();
    weakRead();
    weakWrite();
    weakRedefinition_1();
}