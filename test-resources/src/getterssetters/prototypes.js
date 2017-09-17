function noProtypes() {
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
    o.p = 42;

    TAJS_assert(get_triggered);
    TAJS_assert(set_triggered);
}
function accessors_in_protype() {
    var get_triggered = false;
    var set_triggered = false;

    function K() {
    }

    Object.defineProperty(K.prototype, 'p', {
        get: function () {
            get_triggered = true;
        },
        set: function () {
            set_triggered = true;
        }
    });

    var o = new K();
    o.p;
    o.p = 42;

    TAJS_assert(get_triggered);
    TAJS_assert(set_triggered);
}

function accessors_weakly_in_protype() {
    var get_triggered = false;
    var set_triggered = false;

    function K() {
    }

    if (Math.random()) {
        Object.defineProperty(K.prototype, 'p', {
            get: function () {
                get_triggered = true;
            },
            set: function () {
                set_triggered = true;
            }
        });
    }


    var o = new K();
    o.p;
    o.p = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
}

function accessors_shadowed_in_protype() {
    var get_triggered = false;
    var set_triggered = false;

    function K_K() {
    }

    function K() {
    }

    K.prototype = new K_K();
    K.prototype.p = 42;
    Object.defineProperty(K_K.prototype, 'p', {
        get: function () {
            get_triggered = true;
        },
        set: function () {
            set_triggered = true;
        }
    });

    var o = new K();
    o.p;
    o.p = 42;

    TAJS_assert(!get_triggered);
    TAJS_assert(!set_triggered);
}

function accessors_weakly_shadowed_in_protype_1() {
    var get_triggered = false;
    var set_triggered = false;

    function K_K() {
    }

    function K() {
    }

    K.prototype = new K_K();
    if (Math.random()) {
        K.prototype.p = 42;
    }
    Object.defineProperty(K_K.prototype, 'p', {
        get: function () {
            get_triggered = true;
        },
        set: function () {
            set_triggered = true;
        }
    });

    var o = new K();
    o.p;
    o.p = 42;

    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
}

function accessors_weakly_shadowed_in_protype_2() {
    var old_get_triggered = false;
    var old_set_triggered = false;

    var get_triggered = false;
    var set_triggered = false;

    function K_K() {
    }

    function K() {
    }

    K.prototype = new K_K();
    if (Math.random()) {
        Object.defineProperty(K.prototype, 'p', {
            get: function () {
                old_get_triggered = true;
            },
            set: function () {
                old_set_triggered = true;
            }
        });
    }
    Object.defineProperty(K_K.prototype, 'p', {
        get: function () {
            get_triggered = true;
        },
        set: function () {
            set_triggered = true;
        }
    });

    var o = new K();
    o.p;
    o.p = 42;

    TAJS_assert(old_get_triggered, 'isMaybeAnyBool');
    TAJS_assert(old_set_triggered, 'isMaybeAnyBool');
    TAJS_assert(get_triggered, 'isMaybeAnyBool');
    TAJS_assert(set_triggered, 'isMaybeAnyBool');
}

noProtypes();
accessors_in_protype();
accessors_weakly_in_protype();
accessors_shadowed_in_protype();
accessors_weakly_shadowed_in_protype_1();
accessors_weakly_shadowed_in_protype_2();

while (Math.random()) { // backwards flow
    noProtypes();
    accessors_in_protype();
    accessors_weakly_in_protype();
    accessors_shadowed_in_protype();
    accessors_weakly_shadowed_in_protype_1();
    accessors_weakly_shadowed_in_protype_2();
}