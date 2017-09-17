(function () {
    var root = this;
    var ArrayProto = Array.prototype;
    var slice = ArrayProto.slice;
    var _ = function () {
    };
    root._ = _;
    _.delay = function (func) {
        var args = slice.call(arguments);
        return setTimeout(function () {
            return func.apply(args);
        });
    };
    _.throttle = function () {
        return function () {
        };
    };
    (function () {
        _.each(function () {
            'TAJS_split(0)';
            TAJS_split(0, 'StringSetSplit', 'name');
        });
    });
}.call());
var throttledIncr = _.throttle();
_.delay(throttledIncr);
_.delay();