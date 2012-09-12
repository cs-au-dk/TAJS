// Object returned thru function

function Error(msg) {
    this.message = msg;
}

function kast(x) {
    var f = new Error(x);
    return f;
}
var grot = kast(34);
var flop = grot.message;

dumpValue(flop);
assert(flop == 34);
