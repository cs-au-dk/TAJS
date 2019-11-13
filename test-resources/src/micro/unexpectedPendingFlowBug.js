function toParser(p) {
    return p == "string" ? token() : p;
}
function sequence() {
    toParser(arguments[0]);
}
function choice() {
    return function () {
        {
            var parser = toParser();
            var result = parser();
        }
    };
}
function butnot() {
    return function () {
        var p2 = toParser();
        var br = p2();
    };
}
function optional() {
    var p = toParser();
    return function () {
        var r = p();
    };
}
function assertParseFailed(parser) {
    try {
        var result = parser();
    } catch (e) {
    }
}
sequence();
assertParseFailed(toParser(choice()));
assertParseFailed(sequence(optional()));
sequence(optional());
assertParseFailed(butnot());