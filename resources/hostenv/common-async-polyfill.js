(function (global) {
    var UInt = TAJS_make("AnyNumUInt");
    global.setTimeout = function (f) {
        TAJS_asyncListen(f);
        return TAJS_join(UInt, {/* nodejs object */});
    };
    global.setInterval = function (f, t) {
        TAJS_asyncListen(f);
        return TAJS_join(UInt, {/* nodejs object */});
    };
    global.clearInterval = function (id) {
        // NOOP
    };
    global.clearTimeout = function (id) {
        // NOOP
    };
})(this);