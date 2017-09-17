var class2type = {};

function isArraylike(obj) {
    var length = obj.length,
        type = jQuery.type(obj);

    if (type === "function" || jQuery.isWindow(obj)) {
        return false;
    }

    if (obj.nodeType === 1 && length) {
        return true;
    }

    return type === "array" || length === 0 ||
        typeof length === "number" && length > 0 && ( length - 1 ) in obj;
}

var jQuery = {
    // args is for internal usage only
    each: function (obj, callback, args) {
        var value,
            i = 0,
            length = obj.length,
            isArray = isArraylike(obj);

        if (args) {
            if (isArray) {
                for (; i < length; i++) {
                    value = callback.apply(obj[ i ], args);

                    if (value === false) {
                        break;
                    }
                }
            } else {
                for (i in obj) {
                    value = callback.apply(obj[ i ], args);

                    if (value === false) {
                        break;
                    }
                }
            }

            // A special, fast, case for the most common use of each
        } else {
            if (isArray) {
                for (; i < length; i++) {
                    value = callback.call(obj[ i ], i, obj[ i ]);

                    if (value === false) {
                        break;
                    }
                }
            } else {
                for (i in obj) {
                    value = callback.call(obj[ i ], i, obj[ i ]);

                    if (value === false) {
                        break;
                    }
                }
            }
        }

        return obj;
    },

    isWindow: function (obj) {
        /* jshint eqeqeq: false */
        return obj != null && obj == obj.window;
    },

    type: function (obj) {
        if (obj == null) {
            return obj + "";
        }
        return typeof obj === "object" || typeof obj === "function" ?
            class2type[ toString.call(obj) ] || "object" :
            typeof obj;
    },

    fn: {}
};

// TAJS ::: $.each test #1: Iteration over array - used internally in $.each!
// Populate the class2type map
jQuery.each("Boolean Number String Function Array Date RegExp Object Error".split(" "), function (i, name) {
    class2type[ "[object " + name + "]" ] = name.toLowerCase();
});

var booleanResult = class2type["[object Boolean]"];
var FunctionResult = class2type["[object Function]"];
var ErrorResult = class2type["[object Error]"];

TAJS_assert(booleanResult === "boolean");
TAJS_assert(FunctionResult === "function");
TAJS_assert(ErrorResult === "error");
