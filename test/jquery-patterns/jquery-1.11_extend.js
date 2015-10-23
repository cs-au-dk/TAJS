// NB: depends on precise jQuery.each implementation

var class2type = {};
var hasOwn = ({}).hasOwnProperty;
var support = {ownLast: Math.random() === 0};
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
    isFunction: function (obj) {
        return jQuery.type(obj) === "function";
    },
    type: function (obj) {
        if (obj == null) {
            return obj + "";
        }
        return typeof obj === "object" || typeof obj === "function" ?
            class2type[ toString.call(obj) ] || "object" :
            typeof obj;
    },
    isArray: Array.isArray || function (obj) {
        return jQuery.type(obj) === "array";
    },

    isWindow: function (obj) {
        /* jshint eqeqeq: false */
        return obj != null && obj == obj.window;
    },
    isPlainObject: function (obj) {
        var key;

        // Must be an Object.
        // Because of IE, we also have to check the presence of the constructor property.
        // Make sure that DOM nodes and window objects don't pass through, as well
        if (!obj || jQuery.type(obj) !== "object" || obj.nodeType || jQuery.isWindow(obj)) {
            return false;
        }

        try {
            // Not own constructor property must be Object
            if (obj.constructor && !hasOwn.call(obj, "constructor") && !hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
                return false;
            }
        } catch (e) {
            // IE8,9 Will throw exceptions on certain host objects #9897
            return false;
        }

        // Support: IE<9
        // Handle iteration over inherited properties before own properties.
        if (support.ownLast) {
            for (key in obj) {
                return hasOwn.call(obj, key);
            }
        }

        // Own properties are enumerated firstly, so to speed up,
        // if last one is own, then all properties are own.
        for (key in obj) {
        }

        return key === undefined || hasOwn.call(obj, key);
    },

    fn: {}

}
jQuery.extend = jQuery.fn.extend = function () {
    var src, copyIsArray, copy, name, options, clone,
        target = arguments[0] || {},
        i = 1,
        length = arguments.length,
        deep = false;

    // Handle a deep copy situation
    if (typeof target === "boolean") {
        deep = target;

        // skip the boolean and the target
        target = arguments[ i ] || {};
        i++;
    }

    // Handle case when target is a string or something (possible in deep copy)
    if (typeof target !== "object" && !jQuery.isFunction(target)) {
        target = {};
    }

    // extend jQuery itself if only one argument is passed
    if (i === length) {
        target = this;
        i--;
    }

    for (; i < length; i++) {
        // Only deal with non-null/undefined values
        if ((options = arguments[ i ]) != null) {
            // Extend the base object
            for (name in options) {
                src = target[ name ];
                copy = options[ name ];

                // Prevent never-ending loop
                if (target === copy) {
                    continue;
                }

                // Recurse if we're merging plain objects or arrays
                if (deep && copy && ( jQuery.isPlainObject(copy) || (copyIsArray = jQuery.isArray(copy)) )) {
                    if (copyIsArray) {
                        copyIsArray = false;
                        clone = src && jQuery.isArray(src) ? src : [];

                    } else {
                        clone = src && jQuery.isPlainObject(src) ? src : {};
                    }

                    // Never move original objects, clone them
                    target[ name ] = jQuery.extend(deep, clone, copy);

                    // Don't bring in undefined values
                } else if (copy !== undefined) {
                    target[ name ] = copy;
                }
            }
        }
    }

    // Return the modified object
    return target;
};
// Populate the class2type map
jQuery.each("Boolean Number String Function Array Date RegExp Object Error".split(" "), function (i, name) {
    class2type[ "[object " + name + "]" ] = name.toLowerCase();
});

try {
    ajaxLocation = location.href;
} catch (e) {
    // Use the href attribute of an A element
    // since IE will modify it given document.location
    ajaxLocation = document.createElement("a");
    ajaxLocation.href = "";
    ajaxLocation = ajaxLocation.href;
}
var rlocalProtocol = /^(?:about|app|app-storage|.+-extension|file|res|widget):$/;
var rurl = /^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/;
var ajaxLocParts = rurl.exec(ajaxLocation.toLowerCase()) || [];
var allTypes = "*/".concat("*");

jQuery.ajaxSettings = {
    url: ajaxLocation,
    type: "GET",
    isLocal: rlocalProtocol.test(ajaxLocParts[ 1 ]),
    global: true,
    processData: true,
    async: true,
    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    /*
     timeout: 0,
     data: null,
     dataType: null,
     username: null,
     password: null,
     cache: null,
     throws: false,
     traditional: false,
     headers: {},
     */

    accepts: {
        "*": allTypes,
        text: "text/plain",
        html: "text/html",
        xml: "application/xml, text/xml",
        json: "application/json, text/javascript"
    },

    contents: {
        xml: /xml/,
        html: /html/,
        json: /json/
    },

    responseFields: {
        xml: "responseXML",
        text: "responseText",
        json: "responseJSON"
    },

    // Data converters
    // Keys separate source (or catchall "*") and destination types with a single space
    converters: {

        // Convert anything to text
        "* text": String,

        // Text to html (true = no transformation)
        "text html": true,

        // Evaluate text as a json expression
        "text json": jQuery.parseJSON,

        // Parse text as xml
        "text xml": jQuery.parseXML
    },

    // For options that shouldn't be deep extended:
    // you can add your own custom options here if
    // and when you create one that shouldn't be
    // deep extended (see ajaxExtend)
    flatOptions: {
        url: true,
        context: true
    }
};

// A special extend for ajax options
// that takes "flat" options (not to be deep extended)
// Fixes #9887
function ajaxExtend(target, src) {
    var deep, key,
        flatOptions = jQuery.ajaxSettings.flatOptions || {};

    for (key in src) {
        if (src[ key ] !== undefined) {
            ( flatOptions[ key ] ? target : ( deep || (deep = {}) ) )[ key ] = src[ key ];
            }
        }
    if (deep) {
        jQuery.extend(true, target, deep);
    }

    return target;
}
// Creates a full fledged settings object into target
// with both ajaxSettings and settings fields.
// If target is omitted, writes into ajaxSettings.
jQuery.ajaxSetup = function (target, settings) {
    return settings ?

        // Building a settings object
        ajaxExtend(ajaxExtend(target, jQuery.ajaxSettings), settings) :

        // Extending ajaxSettings
        ajaxExtend(jQuery.ajaxSettings, target);
};

