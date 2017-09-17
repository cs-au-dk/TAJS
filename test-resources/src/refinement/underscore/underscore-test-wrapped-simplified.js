// if (!Array.prototype.forEach) {
    Object.defineProperty(Array.prototype, "forEach", {
        writable: true, enumerable: false, configurable: true,
        value: function forEach(fun /*, thisArg */) {
            "use strict";
            TAJS_makeContextSensitive(fun, 1, {caller: forEach});
            TAJS_makeContextSensitive(fun, 2, {caller: forEach});
            if (this === void 0 || this === null)
                throw new TypeError();

            var t = Object(this);
            var len = t.length >>> 0;
            if (typeof fun !== "function")
                throw new TypeError();

            var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
            for (var i = 0; i < len; i++) {
                if (i in t)
                    fun.call(thisArg, t[i], i, t);
            }
        }
    });
    TAJS_makeContextSensitive(Array.prototype.forEach, 0);
    TAJS_makeContextSensitive(Array.prototype.forEach, -1);
// }

var _ = function (obj) {
    if (obj instanceof _) return obj;
    if (!(this instanceof _)) return new _(obj);
    this._wrapped = obj;
};

_.isFunction = function(obj) {
    return typeof obj === 'function';
};
TAJS_makeContextSensitive(_.isFunction, 0);

_.functions = function(obj) {
    var names = [];
    for (var key in obj) {
        if (_.isFunction(obj[key]))
            names.push(key);
    }
    return names.sort();
};
// TAJS_makeContextSensitive(_.functions, 0);

var each = function(obj, iterator, context) {
    obj.forEach(iterator, context);
};
// TAJS_makeContextSensitive(each, 0);
// TAJS_makeContextSensitive(each, 1);

_.mixin = function(obj) {
    each(_.functions(obj), function(name) {
        var func = obj[name];
        _.prototype[name] = function() {
            var args = [this._wrapped];
            return func.apply(_, args);
        };
    });
};
// TAJS_makeContextSensitive(_.mixin, 0);
var abar_called = false;
var foo = {
  abar: function() { abar_called = true; },
  baz: function() { TAJS_assert(false);}
};

_.mixin(foo);

_(2).abar();
TAJS_assert(abar_called);