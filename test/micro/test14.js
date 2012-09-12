// Exception test

function MyError(msg) {
    this.message = msg;
}

function dummy(x) {
    level1(x);
}

function level1(x) {
    level2(x)
}

function level2(x) {
    throw x;
}

function et(y) {
    dummy(y);
}

try {
    var ex = new MyError("fail");
    if (Math.random())
    	et(ex);
} catch (e) {
    xxx = e.message;
}

dumpValue(xxx);
assert(xxx == "fail");
