function PUT(c) {
    STORE = [c];
}

function PUT_SELF() {
    PUT(this);
}

function PUT_SELF_AND_CRASH() {
    PUT(this);
    FAIL;
}

if (Math.random()) {
    new PUT_SELF_AND_CRASH();
    TAJS_assert(false);
}

new PUT_SELF();
TAJS_assert(STORE[0].toString === Object.prototype.toString);

var CALL_DIED = true;
if (Math.random()) {
    try {
        STORE[0].toString();
    } catch (e) {
        TAJS_assert(false);
    }
    CALL_DIED = false;
}
TAJS_assert(CALL_DIED, 'isMaybeAnyBool');
