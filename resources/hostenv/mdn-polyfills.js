// Polyfills from developer.mozilla.org Polyfill

// Adjusted to use Object.defineProperty instead of regular property assignment

/*
 Polyfills the following:
 - Array.isArray
 - Array.from
 - Array.prototype.indexOf
 - Array.prototype.lastIndexOf
 - Array.prototype.forEach
 - Array.prototype.some
 - Array.prototype.every
 - Array.prototype.map
 - Array.prototype.reduce
 - Array.prototype.reduceRight
 - Array.prototype.filter
 - Array.prototype.fill
 - Array.prototype.find
 - Array.prototype.findIndex
 - Function.prototype.bind
*/
if (!String.prototype.trim) {
    Object.defineProperty(String.prototype, 'trim', {
        writable: true, enumerable: false, configurable: true,
        value: function () {
            return this.replace(/^\s+|\s+$/g, '');
        }
    });
}

if (!Array.prototype.indexOf) {
    Object.defineProperty(Array.prototype, 'indexOf', {
        writable: true, enumerable: false, configurable: true,
        value: function (searchElement, fromIndex) {
            if (this === undefined || this === null) {
                throw new TypeError('"this" is null or not defined');
            }

            var length = this.length >>> 0; // Hack to convert object.length to a UInt32

            fromIndex = +fromIndex || 0;

            if (Math.abs(fromIndex) === Infinity) {
                fromIndex = 0;
            }

            if (fromIndex < 0) {
                fromIndex += length;
                if (fromIndex < 0) {
                    fromIndex = 0;
                }
            }

            for (; fromIndex < length; fromIndex++) {
                if (this[fromIndex] === searchElement) {
                    return fromIndex;
                }
            }

            return -1;
        }
    });
}


if (!Array.prototype.lastIndexOf) {
    Object.defineProperty(Array.prototype, "lastIndexOf", {
        writable: true, enumerable: false, configurable: true,
        value: function (searchElement /*, fromIndex*/) {
            'use strict';

            if (this === void 0 || this === null) {
                throw new TypeError();
            }

            var n, k,
                t = Object(this),
                len = t.length >>> 0;
            if (len === 0) {
                return -1;
            }

            n = len - 1;
            if (arguments.length > 1) {
                n = Number(arguments[1]);
                if (n != n) {
                    n = 0;
                }
                else if (n != 0 && n != (1 / 0) && n != -(1 / 0)) {
                    n = (n > 0 || -1) * Math.floor(Math.abs(n));
                }
            }

            for (k = n >= 0
                ? Math.min(n, len - 1)
                : len - Math.abs(n); k >= 0; k--) {
                if (k in t && t[k] === searchElement) {
                    return k;
                }
            }
            return -1;
        }
    });
}


if (!Function.prototype.bind) {
    Object.defineProperty(Function.prototype, "bind", {
        writable: true, enumerable: false, configurable: true,
        value: function (oThis) {
            if (typeof this !== "function") {
                // closest thing possible to the ECMAScript 5 internal IsCallable function
                throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
            }

            var aArgs = Array.prototype.slice.call(arguments, 1);
            var fToBind = this;
            var fNOP = function () {
            };

            function contextSensitivityHack(fToBind, oThis, aArgs0, aArgs1, aArgs2, aArgs3, aArgs4, aArgs5, aArgs6, aArgs7, aArgs) {
                return function () {
                    aArgs0;
                    aArgs1;
                    aArgs2;
                    aArgs3;
                    aArgs4;
                    aArgs5;
                    aArgs6;
                    aArgs7;
                    return fToBind.apply(this instanceof fNOP && oThis
                            ? this
                            : oThis,
                        aArgs.concat(Array.prototype.slice.call(arguments)));
                }
            }

            TAJS_makeContextSensitive(contextSensitivityHack, 1);
            TAJS_makeContextSensitive(contextSensitivityHack, 2);
            TAJS_makeContextSensitive(contextSensitivityHack, 3);
            TAJS_makeContextSensitive(contextSensitivityHack, 4);
            TAJS_makeContextSensitive(contextSensitivityHack, 5);
            TAJS_makeContextSensitive(contextSensitivityHack, 6);
            TAJS_makeContextSensitive(contextSensitivityHack, 7);
            TAJS_makeContextSensitive(contextSensitivityHack, 8);
            TAJS_makeContextSensitive(contextSensitivityHack, 9);

            var fBound = contextSensitivityHack(fToBind, oThis, aArgs[0], aArgs[1], aArgs[2], aArgs[3], aArgs[4], aArgs[5], aArgs[6], aArgs[7], aArgs);
            TAJS_makeContextSensitive(fBound, -2);

            // Make fToBind context sensitive as well, but only inside fBound.
            // Assume the object sensitivity is desired since that is the TAJS-default
            TAJS_makeContextSensitive(fToBind, 0, {caller: fBound});

            fNOP.prototype = this.prototype;
            fBound.prototype = new fNOP();
            return fBound;
        }
    });
    TAJS_makeContextSensitive(Function.prototype.bind, 0);

    // some of the optional arguments, we assume 8 is sufficient
    TAJS_makeContextSensitive(Function.prototype.bind, 1);
    TAJS_makeContextSensitive(Function.prototype.bind, 2);
    TAJS_makeContextSensitive(Function.prototype.bind, 3);
    TAJS_makeContextSensitive(Function.prototype.bind, 4);
    TAJS_makeContextSensitive(Function.prototype.bind, 5);
    TAJS_makeContextSensitive(Function.prototype.bind, 6);
    TAJS_makeContextSensitive(Function.prototype.bind, 7);
    TAJS_makeContextSensitive(Function.prototype.bind, 8);

    TAJS_makeContextSensitive(Function.prototype.bind, -1);
}

if ('function' !== typeof Array.prototype.reduceRight) {
    Object.defineProperty(Array.prototype, "reduceRight", {
        writable: true, enumerable: false, configurable: true,
        value: function (callback /*, initialValue*/) {
            'use strict';
            if (null === this || 'undefined' === typeof this) {
                throw new TypeError(
                    'Array.prototype.reduce called on null or undefined');
            }
            if ('function' !== typeof callback) {
                throw new TypeError(callback + ' is not a function');
            }
            var t = Object(this), len = t.length >>> 0, k = len - 1, value;
            if (arguments.length >= 2) {
                value = arguments[1];
            } else {
                while (k >= 0 && !k in t) k--;
                if (k < 0)
                    throw new TypeError('Reduce of empty array with no initial value');
                value = t[k--];
            }
            for (; k >= 0; k--) {
                if (k in t) {
                    value = callback(value, t[k], k, t);
                }
            }
            return value;
        }
    });
}

if (!Array.prototype.forEach) {
    Object.defineProperty(Array.prototype, "forEach", {
        writable: true, enumerable: false, configurable: true,
        value: function forEach(fun /*, thisArg */) {
            "use strict";
            TAJS_makeContextSensitive(fun, 1, {caller: forEach});
            TAJS_makeContextSensitive(fun, 2, {caller: forEach});
            TAJS_makeContextSensitive(fun, 3, {caller: forEach});
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
}


if (!Array.isArray) {
    Object.defineProperty(Array, "isArray", {
        writable: true, enumerable: false, configurable: true,
        value: function (arg) {
            return Object.prototype.toString.call(arg) === '[object Array]';
        }
    });
}

if (!Array.from) {
    Object.defineProperty(Array, "from", {
        writable: true, enumerable: false, configurable: true,
        value: (function () {
            var toStr = Object.prototype.toString;
            var isCallable = function (fn) {
                return typeof fn === 'function' || toStr.call(fn) === '[object Function]';
            };
            var toInteger = function (value) {
                var number = Number(value);
                if (isNaN(number)) {
                    return 0;
                }
                if (number === 0 || !isFinite(number)) {
                    return number;
                }
                return (number > 0 ? 1 : -1) * Math.floor(Math.abs(number));
            };
            var maxSafeInteger = Math.pow(2, 53) - 1;
            var toLength = function (value) {
                var len = toInteger(value);
                return Math.min(Math.max(len, 0), maxSafeInteger);
            };

            // The length property of the from method is 1.
            return function from(arrayLike/*, mapFn, thisArg */) {
                // 1. Let C be the this value.
                var C = this;

                // 2. Let items be ToObject(arrayLike).
                var items = Object(arrayLike);

                // 3. ReturnIfAbrupt(items).
                if (arrayLike == null) {
                    throw new TypeError('Array.from requires an array-like object - not null or undefined');
                }

                // 4. If mapfn is undefined, then let mapping be false.
                var mapFn = arguments.length > 1 ? arguments[1] : void undefined;
                var T;
                if (typeof mapFn !== 'undefined') {
                    // 5. else
                    // 5. a If IsCallable(mapfn) is false, throw a TypeError exception.
                    if (!isCallable(mapFn)) {
                        throw new TypeError('Array.from: when provided, the second argument must be a function');
                    }

                    // 5. b. If thisArg was supplied, let T be thisArg; else let T be undefined.
                    if (arguments.length > 2) {
                        T = arguments[2];
                    }
                }

                // 10. Let lenValue be Get(items, "length").
                // 11. Let len be ToLength(lenValue).
                var len = toLength(items.length);

                // 13. If IsConstructor(C) is true, then
                // 13. a. Let A be the result of calling the [[Construct]] internal method
                // of C with an argument list containing the single item len.
                // 14. a. Else, Let A be ArrayCreate(len).
                var A = isCallable(C) ? Object(new C(len)) : new Array(len);

                // 16. Let k be 0.
                var k = 0;
                // 17. Repeat, while k < len… (also steps a - h)
                var kValue;
                while (k < len) {
                    kValue = items[k];
                    if (mapFn) {
                        A[k] = typeof T === 'undefined' ? mapFn(kValue, k) : mapFn.call(T, kValue, k);
                    } else {
                        A[k] = kValue;
                    }
                    k += 1;
                }
                // 18. Let putStatus be Put(A, "length", len, true).
                A.length = len;
                // 20. Return A.
                return A;
            };
        }())
    });

    TAJS_makeContextSensitive(Array.from, 0);
    TAJS_makeContextSensitive(Array.from, 1);
    TAJS_makeContextSensitive(Array.from, 2);
}

if (!Array.prototype.some) {
    Object.defineProperty(Array.prototype, "some", {
        writable: true, enumerable: false, configurable: true,
        value: function (fun /*, thisArg */) {
            'use strict';

            if (this === void 0 || this === null)
                throw new TypeError();

            var t = Object(this);
            var len = t.length >>> 0;
            if (typeof fun !== 'function')
                throw new TypeError();

            var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
            for (var i = 0; i < len; i++) {
                if (i in t && fun.call(thisArg, t[i], i, t))
                    return true;
            }

            return false;
        }
    });
}

if (!Array.prototype.every) {
    Object.defineProperty(Array.prototype, "every", {
        writable: true, enumerable: false, configurable: true,
        value: function (fun /*, thisArg */) {
            'use strict';

            if (this === void 0 || this === null)
                throw new TypeError();

            var t = Object(this);
            var len = t.length >>> 0;
            if (typeof fun !== 'function')
                throw new TypeError();

            var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
            for (var i = 0; i < len; i++) {
                if (i in t && !fun.call(thisArg, t[i], i, t))
                    return false;
            }

            return true;
        }
    });
}

if ('function' !== typeof Array.prototype.reduce) {
    Object.defineProperty(Array.prototype, "reduce", {
        writable: true, enumerable: false, configurable: true,
        value: function (callback /*, initialValue*/) {
            'use strict';
            if (null === this || 'undefined' === typeof this) {
                throw new TypeError(
                    'Array.prototype.reduce called on null or undefined');
            }
            if ('function' !== typeof callback) {
                throw new TypeError(callback + ' is not a function');
            }
            var t = Object(this), len = t.length >>> 0, k = 0, value;
            if (arguments.length >= 2) {
                value = arguments[1];
            } else {
                while (k < len && !k in t) k++;
                if (k >= len)
                    throw new TypeError('Reduce of empty array with no initial value');
                value = t[k++];
            }
            for (; k < len; k++) {
                if (k in t) {
                    value = callback(value, t[k], k, t);
                }
            }
            return value;
        }
    });
}

if (!Array.prototype.filter) {
    Object.defineProperty(Array.prototype, "filter", {
        writable: true, enumerable: false, configurable: true,
        value: function (fun /*, thisArg */) {
            "use strict";

            if (this === void 0 || this === null)
                throw new TypeError();

            var t = Object(this);
            var len = t.length >>> 0;
            if (typeof fun !== "function")
                throw new TypeError();

            var res = [];
            var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
            for (var i = 0; i < len; i++) {
                if (i in t) {
                    var val = t[i];

                    // NOTE: Technically this should Object.defineProperty at
                    //       the next index, as push can be affected by
                    //       properties on Object.prototype and Array.prototype.
                    //       But that method's new, and collisions should be
                    //       rare, so use the more-compatible alternative.
                    if (fun.call(thisArg, val, i, t))
                        res.push(val);
                }
            }

            return res;
        }
    });
}

if (!Array.prototype.map) {
    Object.defineProperty(Array.prototype, "map", {
        writable: true, enumerable: false, configurable: true,
        value: function map(fun /*, thisArg */) {
            "use strict";
            TAJS_makeContextSensitive(fun, 1, {caller: map});
            TAJS_makeContextSensitive(fun, 2, {caller: map});
            TAJS_makeContextSensitive(fun, 3, {caller: map});
            if (this === void 0 || this === null)
                throw new TypeError();

            var t = Object(this);
            var len = t.length >>> 0;
            if (typeof fun !== "function")
                throw new TypeError();

            var res = TAJS_newArray();
            var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
            for (var i = 0; i < len; i++) {
                if (Object.prototype.hasOwnProperty.call(t, i))
                    res[i] = fun.call(thisArg, t[i], i, t);
            }

            return res;
        }
    });
    TAJS_makeContextSensitive(Array.prototype.map, 0);
    TAJS_makeContextSensitive(Array.prototype.map, -1);
}

if (!Array.prototype.fill) {
    Object.defineProperty(Array.prototype, "fill", {
        writable: true, enumerable: false, configurable: true,
        value: function fill(value) {
        // Steps 1-2.
        if (this == null) {
            throw new TypeError('this is null or not defined');
        }
        var O = Object(this);

        // Steps 3-5.
        var len = O.length >>> 0;

        // Steps 6-7.
        var start = arguments[1];
        var relativeStart = start >> 0;

        // Step 8.
        var k = relativeStart < 0 ?
            Math.max(len + relativeStart, 0) :
            Math.min(relativeStart, len);

    // Steps 9-10.
        var end = arguments[2];
        var relativeEnd = end === undefined ?
            len : end >> 0;

        // Step 11.
        var final1 = relativeEnd < 0 ?
            Math.max(len + relativeEnd, 0) :
            Math.min(relativeEnd, len);

        // Step 12.
        while (k < final1) {
            O[k] = value;
            k++;
        }

        // Step 13.
        return O;
    }});
}

if (!Array.prototype.find) {
    Object.defineProperty(Array.prototype, 'find', {
        value: function(predicate) {
            'use strict';
            if (this == null) {
                throw new TypeError('Array.prototype.find called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            var list = Object(this);
            var length = list.length >>> 0;
            var thisArg = arguments[1];
            var value;

            for (var i = 0; i < length; i++) {
                value = list[i];
                if (predicate.call(thisArg, value, i, list)) {
                    return value;
                }
            }
            return undefined;
        }
    });
}

if (!Array.prototype.findIndex) {
    Object.defineProperty(Array.prototype, 'findIndex', {
        value: function(predicate) {
            'use strict';
            if (this == null) {
                throw new TypeError('Array.prototype.findIndex called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            var list = Object(this);
            var length = list.length >>> 0;
            var thisArg = arguments[1];
            var value;

            for (var i = 0; i < length; i++) {
                value = list[i];
                if (predicate.call(thisArg, value, i, list)) {
                    return i;
                }
            }
            return -1;
        },
        enumerable: false,
        configurable: false,
        writable: false
    });
}


if (!Date.prototype.toISOString) {
    (function() {

        function pad(number) {
            if (number < 10) {
                return '0' + number;
            }
            return number;
        }

        Date.prototype.toISOString = function() {
            return this.getUTCFullYear() +
                '-' + pad(this.getUTCMonth() + 1) +
                '-' + pad(this.getUTCDate()) +
                'T' + pad(this.getUTCHours()) +
                ':' + pad(this.getUTCMinutes()) +
                ':' + pad(this.getUTCSeconds()) +
                '.' + (this.getUTCMilliseconds() / 1000).toFixed(3).slice(2, 5) +
                'Z';
        };

    }());
}
/*End of developer.mozilla.org Polyfill*/