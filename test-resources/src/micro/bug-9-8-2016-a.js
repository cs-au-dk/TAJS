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
}

new PUT_SELF();

STORE[0].toString();

