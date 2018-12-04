// function TAJS_addContextSensitivity() {}; var TAJS_dumpValue = function(x) {console.log(x)}

var core_hasOwn = Object.prototype.hasOwnProperty;

var isFunction = function(obj) {
    return typeof(obj) === "function";
};
TAJS_addContextSensitivity(isFunction,"obj");       
        
var isArray = Array.isArray;
        
var isPlainObject = function(obj) {
            // Must be an Object.
            // Because of IE, we also have to check the presence of the constructor property.
            // Make sure that DOM nodes and window objects don't pass through, as well
            if (!obj || typeof(obj) !== "object" ) {
                return false;
            }
            try {
                // Not own constructor property must be Object
                if (obj.constructor && !core_hasOwn.call(obj, "constructor") && !core_hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
                    return false;
                }
            } catch (e) {
                // IE8,9 Will throw exceptions on certain host objects #9897
                return false;
            }
            // Own properties are enumerated firstly, so to speed up,
            // if last one is own, then all properties are own.
            var key;
            for (key in obj) {}
            return key === undefined || core_hasOwn.call(obj, key);
        };
        TAJS_addContextSensitivity(isPlainObject, 0);


var extend = function(ARG1,ARG2,ARG3) {
	var options, name, src, copy, copyIsArray, clone,
		target = arguments[0] || {},
		i = 1,
		length = arguments.length,
		deep = false;

	// Handle a deep copy situation
	if ( typeof target === "boolean" ) {
		deep = target;
		target = arguments[1] || {};
		// skip the boolean and the target
		i = 2;
	}

	// Handle case when target is a string or something (possible in deep copy)
	if ( typeof target !== "object" && !isFunction(target) ) {
		target = {};
	}

	// extend jQuery itself if only one argument is passed
	if ( length === i ) {
		target = this;
		--i;
	}

	for ( ; i < length; i++ ) {
		// Only deal with non-null/undefined values
		if ( (options = arguments[ i ]) != null ) {
			// Extend the base object
			//assumeTypeOf("options", "boolean", false);
			for ( name in options ) {
				src = target[ name ];
				copy = options[ name ];

				// Prevent never-ending loop
				if ( target === copy ) {
					continue;
				}

				// Recurse if we're merging plain objects or arrays
				if ( deep && copy && ( isPlainObject(copy) || (copyIsArray = isArray(copy)) ) ) {
					if ( copyIsArray ) {
						copyIsArray = false;
						clone = src && isArray(src) ? src : [];

					} else {
                        clone = src && isPlainObject(src) ? src : TAJS_newObject();
					}

					// Never move original objects, clone them
					target[ name ] = extend( deep, clone, copy );

				// Don't bring in undefined values
				} else if ( copy !== undefined ) {
					target[ name ] = copy;
				}
			}
		}
	}

	// Return the modified object
	return target;
};
TAJS_addContextSensitivity(extend, 0);
TAJS_addContextSensitivity(extend, 1);
TAJS_addContextSensitivity(extend, 2);

var ajaxSettings = {accepts:{}};

extend(true, ajaxSettings, {
	jsonp: "callback",
	jsonpCallback: function FOO() {}
});
extend(true, ajaxSettings, {
	accepts: {
		script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
	},
	contents: {
		script: /javascript|ecmascript/
	},
	converters: {
		"text script": function BAR( text ) {}
	}
});
TAJS_dumpState();
TAJS_dumpValue(ajaxSettings);
TAJS_dumpObject(ajaxSettings);
