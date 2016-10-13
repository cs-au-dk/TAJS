function toStringGetter() {
    var get_triggered = false;
    var catch_triggered = false;
    var o = {};
    o.__defineGetter__('toString', function () {
        get_triggered = true;
    });
    try {
        o + '';
        TAJS_assert(false);
    } catch (e) {
        // Uncaught TypeError: Cannot convert object to primitive value
        catch_triggered = true;
    }
    TAJS_assert(get_triggered);
    TAJS_assert(catch_triggered);
}

function toStringPrototypeGetter() {
    var get_triggered = false;
    var catch_triggered = false;

    function K() {
    }

    var o = new K();
    K.prototype.__defineGetter__('toString', function () {
        get_triggered = true;
    });
    try {
        o + '';
        TAJS_assert(false);
    } catch (e) {
        // Uncaught TypeError: Cannot convert object to primitive value
        catch_triggered = true;
    }
    TAJS_assert(get_triggered);
    TAJS_assert(catch_triggered);
}

function valueOfGetter() {
    var get_triggered = false;
    var o = {};
    o.__defineGetter__('valueOf', function () {
        get_triggered = true;
    });
    o - 1;
    TAJS_assert(get_triggered);
}

function valueOfPrototypeGetter() {
    var get_triggered = false;

    function K() {
    }

    var o = new K();
    K.prototype.__defineGetter__('valueOf', function () {
        get_triggered = true;
    });
    o - 1;
    TAJS_assert(get_triggered);
}

function valueOfFallbackGetter() {
    var get_triggered = false;
    var catch_triggered = false;

    var o = {};
    o.__defineGetter__('valueOf', function () {
        get_triggered = true;
    });
    o.toString = function () {
        return this;
    };
    try {
        o + '';
    } catch (e) {
        // Uncaught TypeError: Cannot convert object to primitive value
        catch_triggered = true;
    }
    TAJS_assert(get_triggered);
    TAJS_assert(catch_triggered, 'isMaybeAnyBool' /* weak type error */);
}

function lengthGetter() {
    var get_triggered = false;
    var o = {};
    o.__defineGetter__('length', function () {
        get_triggered = true;
    });
    TAJS_assert(!get_triggered);
    o.length = 1;
    TAJS_assert(!get_triggered);
    Array.prototype.indexOf.call(o, 'x');
    TAJS_assert(get_triggered);
}

function lengthPrototypeGetter() {
    var get_triggered = false;

    function K() {
    }

    var o = new K();
    K.prototype.__defineGetter__('length', function () {
        get_triggered = true;
    });
    TAJS_assert(!get_triggered);
    o.length = 1;
    TAJS_assert(!get_triggered);
    Array.prototype.indexOf.call(o, 'x');
    TAJS_assert(get_triggered);
}

function lengthPrototypeSetter() {
    var set_triggered = false;
    var catch_triggered = false;

    function K() {
    }

    var o = new K();
    K.prototype.__defineSetter__('length', function () {
        set_triggered = true;
    });

    try {
        Array.prototype.splice.call(o, 0, 0, 'x');
    } catch (e) {
        catch_triggered = true;
    }
    TAJS_assert(set_triggered);
    TAJS_assert(!catch_triggered);
}

function toStringGetter_triggersSetter() {
    var get_triggered = false;
    var set_triggered = false;
    var catch_triggered = false;

    var o = {};
    o.__defineGetter__('toString', function () {
        this.toString = 42;
    });
    o.__defineSetter__('toString', function () {
        get_triggered = true;
    });
    try {
        o + '';
        TAJS_assert(false);
    } catch (e) {
        // Uncaught TypeError: Cannot convert object to primitive value
        catch_triggered = true;
    }
    TAJS_assert(catch_triggered);
    TAJS_assert(get_triggered);
    TAJS_assert(!set_triggered);
}

function toStringGetter_triggersSetter_redefinesGetter_readsToString() {
    var get_triggered = false;
    var set_triggered = false;
    var get2_triggered = false;
    var catch_triggered = false;
    var o = {};
    o.__defineGetter__('toString', function () {
        get_triggered = true;
        this.toString = 42;
        return this.toString;
    });
    o.__defineSetter__('toString', function () {
        set_triggered = true;
        o.__defineGetter__('toString', function () {
            get2_triggered = true;
            return 87;
        });
    });
    try {
        o + '';
        TAJS_assert(false);
    } catch (e) {
        // Uncaught TypeError: Cannot convert object to primitive value
        catch_triggered = true;
    }
    TAJS_assert(catch_triggered);
    TAJS_assert(get_triggered);
    TAJS_assert(set_triggered);
    TAJS_assert(get2_triggered);
}

toStringGetter();
toStringPrototypeGetter();
valueOfGetter();
valueOfPrototypeGetter();
valueOfFallbackGetter();
lengthGetter();
lengthPrototypeGetter();
lengthPrototypeSetter();
toStringGetter_triggersSetter();
toStringGetter_triggersSetter_redefinesGetter_readsToString();

while (Math.random()) { // backwards flow
    toStringGetter();
    toStringPrototypeGetter();
    valueOfGetter();
    valueOfPrototypeGetter();
    valueOfPrototypeGetter();
    lengthGetter();
    lengthPrototypeGetter();
    lengthPrototypeSetter();
    toStringGetter_triggersSetter();
    toStringGetter_triggersSetter_redefinesGetter_readsToString();
}