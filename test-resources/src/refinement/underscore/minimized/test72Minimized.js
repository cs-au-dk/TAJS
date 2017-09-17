(function () {
    var root = this;
    var _ = function () {
    };
    root._ = _;
    var cb = function (value) {
        return _.property();
    };
    var property = function () {
        return function () {
        };
    };
    var getLength = property();
    var isArrayLike = function (collection) {
        var length = getLength(collection);
    };
    _.map = function () {
        var keys = isArrayLike(), results = Array();
        return results;
    };
    _.pluck = function () {
    };
    _.sortBy = function () {
        cb();
        return _.pluck(_.map().sort());
    };
    _.property = property;
}.call());
function deepEqual() {
}
deepEqual(_.sortBy());
var sorted = _.sortBy();
