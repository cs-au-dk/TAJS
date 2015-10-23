/*!
 * jQuery JavaScript Library v1.8.0
 * http://jquery.com/
 *
 * Includes Sizzle.js
 * http://sizzlejs.com/
 *
 * Copyright 2012 jQuery Foundation and other contributors
 * Released under the MIT license
 * http://jquery.org/license
 *
 * Date: Thu Aug 09 2012 16:24:48 GMT-0400 (Eastern Daylight Time)
 */
(function(window, undefined) {
    var // A central reference to the root jQuery(document)
    rootjQuery, // The deferred used on DOM ready
    readyList, // Use the correct document accordingly with window argument (sandbox)
    document = window.document, location = window.location, navigator = window.navigator, // Map over jQuery in case of overwrite
    _jQuery = window.jQuery, // Map over the $ in case of overwrite
    _$ = window.$, // Save a reference to some core methods
    core_push = Array.prototype.push, core_slice = Array.prototype.slice, core_indexOf = Array.prototype.indexOf, core_toString = Object.prototype.toString, core_hasOwn = Object.prototype.hasOwnProperty, core_trim = String.prototype.trim, // Define a local copy of jQuery
    jQuery = function(selector, context) {
        // The jQuery object is actually just the init constructor 'enhanced'
        return new jQuery.fn.init(selector, context, rootjQuery);
    }, // Used for matching numbers
    core_pnum = /[\-+]?(?:\d*\.|)\d+(?:[eE][\-+]?\d+|)/.source, // Used for detecting and trimming whitespace
    core_rnotwhite = /\S/, core_rspace = /\s+/, // IE doesn't match non-breaking spaces with \s
    rtrim = core_rnotwhite.test("\xA0") ? /^[\s\xA0]+|[\s\xA0]+$/g : /^\s+|\s+$/g, // A simple way to check for HTML strings
    // Prioritize #id over <tag> to avoid XSS via location.hash (#9521)
    rquickExpr = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/, // Match a standalone tag
    rsingleTag = /^<(\w+)\s*\/?>(?:<\/\1>|)$/, // JSON RegExp
    rvalidchars = /^[\],:{}\s]*$/, rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g, rvalidescape = /\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g, rvalidtokens = /"[^"\\\r\n]*"|true|false|null|-?(?:\d\d*\.|)\d+(?:[eE][\-+]?\d+|)/g, // Matches dashed string for camelizing
    rmsPrefix = /^-ms-/, rdashAlpha = /-([\da-z])/gi, // Used by jQuery.camelCase as callback to replace()
    fcamelCase = function(all, letter) {
        "dk.brics.tajs.directives.unreachable";
        return (letter + "").toUpperCase();
    }, // The ready event handler and self cleanup method
    DOMContentLoaded = function() {
        if (document.addEventListener) {
            document.removeEventListener("DOMContentLoaded", DOMContentLoaded, false);
            jQuery.ready();
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (document.readyState === "complete") {
                "dk.brics.tajs.directives.unreachable";
                // we're here because readyState === "complete" in oldIE
                // which is good enough for us to call the dom ready!
                document.detachEvent("onreadystatechange", DOMContentLoaded);
                jQuery.ready();
            }
        }
    }, // [[Class]] -> type pairs
    class2type = {};
    jQuery.fn = jQuery.prototype = {
        constructor: jQuery,
        init: function(selector, context, rootjQuery) {
            var match, elem, ret, doc;
            // Handle $(""), $(null), $(undefined), $(false)
            if (!selector) {
                "dk.brics.tajs.directives.unreachable";
                return this;
            }
            // Handle $(DOMElement)
            if (selector.nodeType) {
                this.context = this[0] = selector;
                this.length = 1;
                return this;
            }
            // Handle HTML strings
            if (typeof selector === "string") {
                "dk.brics.tajs.directives.unreachable";
                if (selector.charAt(0) === "<" && selector.charAt(selector.length - 1) === ">" && selector.length >= 3) {
                    "dk.brics.tajs.directives.unreachable";
                    // Assume that strings that start and end with <> are HTML and skip the regex check
                    match = [ null, selector, null ];
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    match = rquickExpr.exec(selector);
                }
                // Match html or make sure no context is specified for #id
                if (match && (match[1] || !context)) {
                    "dk.brics.tajs.directives.unreachable";
                    // HANDLE: $(html) -> $(array)
                    if (match[1]) {
                        "dk.brics.tajs.directives.unreachable";
                        context = context instanceof jQuery ? context[0] : context;
                        doc = context && context.nodeType ? context.ownerDocument || context : document;
                        // scripts is true for back-compat
                        selector = jQuery.parseHTML(match[1], doc, true);
                        if (rsingleTag.test(match[1]) && jQuery.isPlainObject(context)) {
                            "dk.brics.tajs.directives.unreachable";
                            this.attr.call(selector, context, true);
                        }
                        return jQuery.merge(this, selector);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        elem = document.getElementById(match[2]);
                        // Check parentNode to catch when Blackberry 4.6 returns
                        // nodes that are no longer in the document #6963
                        if (elem && elem.parentNode) {
                            "dk.brics.tajs.directives.unreachable";
                            // Handle the case where IE and Opera return items
                            // by name instead of ID
                            if (elem.id !== match[2]) {
                                "dk.brics.tajs.directives.unreachable";
                                return rootjQuery.find(selector);
                            }
                            // Otherwise, we inject the element directly into the jQuery object
                            this.length = 1;
                            this[0] = elem;
                        }
                        this.context = document;
                        this.selector = selector;
                        return this;
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (!context || context.jquery) {
                        "dk.brics.tajs.directives.unreachable";
                        return (context || rootjQuery).find(selector);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        return this.constructor(context).find(selector);
                    }
                }
            } else {
                if (jQuery.isFunction(selector)) {
                    return rootjQuery.ready(selector);
                }
            }
            "dk.brics.tajs.directives.unreachable";
            if (selector.selector !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                this.selector = selector.selector;
                this.context = selector.context;
            }
            return jQuery.makeArray(selector, this);
        },
        // Start with an empty selector
        selector: "",
        // The current version of jQuery being used
        jquery: "1.8.0",
        // The default length of a jQuery object is 0
        length: 0,
        // The number of elements contained in the matched element set
        size: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.length;
        },
        toArray: function() {
            "dk.brics.tajs.directives.unreachable";
            return core_slice.call(this);
        },
        // Get the Nth element in the matched element set OR
        // Get the whole matched element set as a clean array
        get: function(num) {
            "dk.brics.tajs.directives.unreachable";
            return num == null ? // Return a 'clean' array
            this.toArray() : // Return just the object
            num < 0 ? this[this.length + num] : this[num];
        },
        // Take an array of elements and push it onto the stack
        // (returning the new matched element set)
        pushStack: function(elems, name, selector) {
            "dk.brics.tajs.directives.unreachable";
            // Build a new jQuery matched element set
            var ret = jQuery.merge(this.constructor(), elems);
            // Add the old object onto the stack (as a reference)
            ret.prevObject = this;
            ret.context = this.context;
            if (name === "find") {
                "dk.brics.tajs.directives.unreachable";
                ret.selector = this.selector + (this.selector ? " " : "") + selector;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (name) {
                    "dk.brics.tajs.directives.unreachable";
                    ret.selector = this.selector + "." + name + "(" + selector + ")";
                }
            }
            // Return the newly-formed element set
            return ret;
        },
        // Execute a callback for every element in the matched set.
        // (You can seed the arguments with an array of args, but this is
        // only used internally.)
        each: function(callback, args) {
            return jQuery.each(this, callback, args);
        },
        ready: function(fn) {
            // Add the callback
            jQuery.ready.promise().done(fn);
            return this;
        },
        eq: function(i) {
            "dk.brics.tajs.directives.unreachable";
            i = +i;
            return i === -1 ? this.slice(i) : this.slice(i, i + 1);
        },
        first: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.eq(0);
        },
        last: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.eq(-1);
        },
        slice: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(core_slice.apply(this, arguments), "slice", core_slice.call(arguments).join(","));
        },
        map: function(callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(jQuery.map(this, function(elem, i) {
                "dk.brics.tajs.directives.unreachable";
                return callback.call(elem, i, elem);
            }));
        },
        end: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.prevObject || this.constructor(null);
        },
        // For internal use only.
        // Behaves like an Array's method, not like a jQuery method.
        push: core_push,
        sort: [].sort,
        splice: [].splice
    };
    // Give the init function the jQuery prototype for later instantiation
    jQuery.fn.init.prototype = jQuery.fn;
    jQuery.extend = jQuery.fn.extend = function(ARG1, ARG2, ARG3) {
        var options, name, src, copy, copyIsArray, clone, target = arguments[0] || {}, i = 1, length = arguments.length, deep = false;
        // Handle a deep copy situation
        if (typeof target === "boolean") {
            deep = target;
            target = arguments[1] || {};
            // skip the boolean and the target
            i = 2;
        }
        // Handle case when target is a string or something (possible in deep copy)
        if (typeof target !== "object" && !jQuery.isFunction(target)) {
            "dk.brics.tajs.directives.unreachable";
            target = {};
        }
        // extend jQuery itself if only one argument is passed
        if (length === i) {
            target = this;
            --i;
        }
        for (;i < length; i++) {
            // Only deal with non-null/undefined values
            if ((options = arguments[i]) != null) {
                // Extend the base object
                for (name in options) {
                    src = target[name];
                    copy = options[name];
                    // Prevent never-ending loop
                    if (target === copy) {
                        "dk.brics.tajs.directives.unreachable";
                        continue;
                    }
                    // Recurse if we're merging plain objects or arrays
                    if (deep && copy && (jQuery.isPlainObject(copy) || (copyIsArray = jQuery.isArray(copy)))) {
                        if (copyIsArray) {
                            "dk.brics.tajs.directives.unreachable";
                            copyIsArray = false;
                            clone = src && jQuery.isArray(src) ? src : [];
                        } else {
                            clone = src && jQuery.isPlainObject(src) ? src : {};
                        }
                        // Never move original objects, clone them
                        target[name] = jQuery.extend(deep, clone, copy);
                    } else {
                        if (copy !== undefined) {
                            target[name] = copy;
                        }
                    }
                }
            }
        }
        // Return the modified object
        return target;
    };
    jQuery.extend({
        noConflict: function(deep) {
            "dk.brics.tajs.directives.unreachable";
            if (window.$ === jQuery) {
                "dk.brics.tajs.directives.unreachable";
                window.$ = _$;
            }
            if (deep && window.jQuery === jQuery) {
                "dk.brics.tajs.directives.unreachable";
                window.jQuery = _jQuery;
            }
            return jQuery;
        },
        // Is the DOM ready to be used? Set to true once it occurs.
        isReady: false,
        // A counter to track how many items to wait for before
        // the ready event fires. See #6781
        readyWait: 1,
        // Hold (or release) the ready event
        holdReady: function(hold) {
            "dk.brics.tajs.directives.unreachable";
            if (hold) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.readyWait++;
            } else {
                "dk.brics.tajs.directives.unreachable";
                jQuery.ready(true);
            }
        },
        // Handle when the DOM is ready
        ready: function(wait) {
            // Abort if there are pending holds or we're already ready
            if (wait === true ? --jQuery.readyWait : jQuery.isReady) {
                return;
            }
            // Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
            if (!document.body) {
                "dk.brics.tajs.directives.unreachable";
                return setTimeout(jQuery.ready, 1);
            }
            // Remember that the DOM is ready
            jQuery.isReady = true;
            // If a normal DOM Ready event fired, decrement, and wait if need be
            if (wait !== true && --jQuery.readyWait > 0) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // If there are functions bound, to execute
            readyList.resolveWith(document, [ jQuery ]);
            // Trigger any bound ready events
            if (jQuery.fn.trigger) {
                jQuery(document).trigger("ready").off("ready");
            }
        },
        // See test/unit/core.js for details concerning isFunction.
        // Since version 1.3, DOM methods and functions like alert
        // aren't supported. They return false on IE (#2968).
        isFunction: function(obj) {
            return jQuery.type(obj) === "function";
        },
        isArray: Array.isArray || function(obj) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.type(obj) === "array";
        },
        isWindow: function(obj) {
            return obj != null && obj == obj.window;
        },
        isNumeric: function(obj) {
            "dk.brics.tajs.directives.unreachable";
            return !isNaN(parseFloat(obj)) && isFinite(obj);
        },
        type: function(obj) {
            return obj == null ? String(obj) : class2type[core_toString.call(obj)] || "object";
        },
        isPlainObject: function(obj) {
            // Must be an Object.
            // Because of IE, we also have to check the presence of the constructor property.
            // Make sure that DOM nodes and window objects don't pass through, as well
            if (!obj || jQuery.type(obj) !== "object" || obj.nodeType || jQuery.isWindow(obj)) {
                return false;
            }
            try {
                // Not own constructor property must be Object
                if (obj.constructor && !core_hasOwn.call(obj, "constructor") && !core_hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                // IE8,9 Will throw exceptions on certain host objects #9897
                return false;
            }
            // Own properties are enumerated firstly, so to speed up,
            // if last one is own, then all properties are own.
            var key;
            // jQuery relies on implementation details left unspecified in the ECMA standard.
            // We can achieve the same effect, but we have to rewrite the check for own properties manually
            for (key in obj) {
                if(!core_hasOwn.call(obj, key)){
                    return false;
                }
            }
            return key === undefined || true;
            for (key in obj) {}
            return key === undefined || core_hasOwn.call(obj, key);
        },
        isEmptyObject: function(obj) {
            "dk.brics.tajs.directives.unreachable";
            var name;
            for (name in obj) {
                "dk.brics.tajs.directives.unreachable";
                return false;
            }
            return true;
        },
        error: function(msg) {
            "dk.brics.tajs.directives.unreachable";
            throw new Error(msg);
        },
        // data: string of html
        // context (optional): If specified, the fragment will be created in this context, defaults to document
        // scripts (optional): If true, will include scripts passed in the html string
        parseHTML: function(data, context, scripts) {
            "dk.brics.tajs.directives.unreachable";
            var parsed;
            if (!data || typeof data !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            if (typeof context === "boolean") {
                "dk.brics.tajs.directives.unreachable";
                scripts = context;
                context = 0;
            }
            context = context || document;
            // Single tag
            if (parsed = rsingleTag.exec(data)) {
                "dk.brics.tajs.directives.unreachable";
                return [ context.createElement(parsed[1]) ];
            }
            parsed = jQuery.buildFragment([ data ], context, scripts ? null : []);
            return jQuery.merge([], (parsed.cacheable ? jQuery.clone(parsed.fragment) : parsed.fragment).childNodes);
        },
        parseJSON: function(data) {
            "dk.brics.tajs.directives.unreachable";
            if (!data || typeof data !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            // Make sure leading/trailing whitespace is removed (IE can't handle it)
            data = jQuery.trim(data);
            // Attempt to parse using the native JSON parser first
            if (window.JSON && window.JSON.parse) {
                "dk.brics.tajs.directives.unreachable";
                return window.JSON.parse(data);
            }
            // Make sure the incoming data is actual JSON
            // Logic borrowed from http://json.org/json2.js
            if (rvalidchars.test(data.replace(rvalidescape, "@").replace(rvalidtokens, "]").replace(rvalidbraces, ""))) {
                "dk.brics.tajs.directives.unreachable";
                return new Function("return " + data)();
            }
            jQuery.error("Invalid JSON: " + data);
        },
        // Cross-browser xml parsing
        parseXML: function(data) {
            "dk.brics.tajs.directives.unreachable";
            var xml, tmp;
            if (!data || typeof data !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            try {
                "dk.brics.tajs.directives.unreachable";
                if (window.DOMParser) {
                    "dk.brics.tajs.directives.unreachable";
                    // Standard
                    tmp = new DOMParser();
                    xml = tmp.parseFromString(data, "text/xml");
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // IE
                    xml = new ActiveXObject("Microsoft.XMLDOM");
                    xml.async = "false";
                    xml.loadXML(data);
                }
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                xml = undefined;
            }
            if (!xml || !xml.documentElement || xml.getElementsByTagName("parsererror").length) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.error("Invalid XML: " + data);
            }
            return xml;
        },
        noop: function() {},
        // Evaluates a script in a global context
        // Workarounds based on findings by Jim Driscoll
        // http://weblogs.java.net/blog/driscoll/archive/2009/09/08/eval-javascript-global-context
        globalEval: function(data) {
            "dk.brics.tajs.directives.unreachable";
            if (data && core_rnotwhite.test(data)) {
                "dk.brics.tajs.directives.unreachable";
                // We use execScript on Internet Explorer
                // We use an anonymous function so that context is window
                // rather than jQuery in Firefox
                (window.execScript || function(data) {
                    "dk.brics.tajs.directives.unreachable";
                    window["eval"].call(window, data);
                })(data);
            }
        },
        // Convert dashed to camelCase; used by the css and data modules
        // Microsoft forgot to hump their vendor prefix (#9572)
        camelCase: function(string) {
            "dk.brics.tajs.directives.unreachable";
            return string.replace(rmsPrefix, "ms-").replace(rdashAlpha, fcamelCase);
        },
        nodeName: function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            return elem.nodeName && elem.nodeName.toUpperCase() === name.toUpperCase();
        },
        // args is for internal usage only
        each: function(obj, callback, args) {
            var name, i = 0, length = obj.length, isObj = length === undefined || jQuery.isFunction(obj);
            if (args) {
                "dk.brics.tajs.directives.unreachable";
                if (isObj) {
                    "dk.brics.tajs.directives.unreachable";
                    for (name in obj) {
                        "dk.brics.tajs.directives.unreachable";
                        if (callback.apply(obj[name], args) === false) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    for (;i < length; ) {
                        "dk.brics.tajs.directives.unreachable";
                        if (callback.apply(obj[i++], args) === false) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
                }
            } else {
                if (isObj) {
                    for (name in obj) {
                        if (callback.call(obj[name], name, obj[name]) === false) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
                } else {
                    for (;i < length; ) {
                        if (callback.call(obj[i], i, obj[i++]) === false) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
                }
            }
            return obj;
        },
        // Use native String.trim function wherever possible
        trim: core_trim ? function(text) {
            "dk.brics.tajs.directives.unreachable";
            return text == null ? "" : core_trim.call(text);
        } : // Otherwise use our own trimming functionality
        function(text) {
            "dk.brics.tajs.directives.unreachable";
            return text == null ? "" : text.toString().replace(rtrim, "");
        },
        // results is for internal usage only
        makeArray: function(arr, results) {
            "dk.brics.tajs.directives.unreachable";
            var type, ret = results || [];
            if (arr != null) {
                "dk.brics.tajs.directives.unreachable";
                // The window, strings (and functions) also have 'length'
                // Tweaked logic slightly to handle Blackberry 4.7 RegExp issues #6930
                type = jQuery.type(arr);
                if (arr.length == null || type === "string" || type === "function" || type === "regexp" || jQuery.isWindow(arr)) {
                    "dk.brics.tajs.directives.unreachable";
                    core_push.call(ret, arr);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.merge(ret, arr);
                }
            }
            return ret;
        },
        inArray: function(elem, arr, i) {
            "dk.brics.tajs.directives.unreachable";
            var len;
            if (arr) {
                "dk.brics.tajs.directives.unreachable";
                if (core_indexOf) {
                    "dk.brics.tajs.directives.unreachable";
                    return core_indexOf.call(arr, elem, i);
                }
                len = arr.length;
                i = i ? i < 0 ? Math.max(0, len + i) : i : 0;
                for (;i < len; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    // Skip accessing in sparse arrays
                    if (i in arr && arr[i] === elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return i;
                    }
                }
            }
            return -1;
        },
        merge: function(first, second) {
            "dk.brics.tajs.directives.unreachable";
            var l = second.length, i = first.length, j = 0;
            if (typeof l === "number") {
                "dk.brics.tajs.directives.unreachable";
                for (;j < l; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    first[i++] = second[j];
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                while (second[j] !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    first[i++] = second[j++];
                }
            }
            first.length = i;
            return first;
        },
        grep: function(elems, callback, inv) {
            "dk.brics.tajs.directives.unreachable";
            var retVal, ret = [], i = 0, length = elems.length;
            inv = !!inv;
            // Go through the array, only saving the items
            // that pass the validator function
            for (;i < length; i++) {
                "dk.brics.tajs.directives.unreachable";
                retVal = !!callback(elems[i], i);
                if (inv !== retVal) {
                    "dk.brics.tajs.directives.unreachable";
                    ret.push(elems[i]);
                }
            }
            return ret;
        },
        // arg is for internal usage only
        map: function(elems, callback, arg) {
            "dk.brics.tajs.directives.unreachable";
            var value, key, ret = [], i = 0, length = elems.length, // jquery objects are treated as arrays
            isArray = elems instanceof jQuery || length !== undefined && typeof length === "number" && (length > 0 && elems[0] && elems[length - 1] || length === 0 || jQuery.isArray(elems));
            // Go through the array, translating each of the items to their
            if (isArray) {
                "dk.brics.tajs.directives.unreachable";
                for (;i < length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    value = callback(elems[i], i, arg);
                    if (value != null) {
                        "dk.brics.tajs.directives.unreachable";
                        ret[ret.length] = value;
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (key in elems) {
                    "dk.brics.tajs.directives.unreachable";
                    value = callback(elems[key], key, arg);
                    if (value != null) {
                        "dk.brics.tajs.directives.unreachable";
                        ret[ret.length] = value;
                    }
                }
            }
            // Flatten any nested arrays
            return ret.concat.apply([], ret);
        },
        // A global GUID counter for objects
        guid: 1,
        // Bind a function to a context, optionally partially applying any
        // arguments.
        proxy: function(fn, context) {
            "dk.brics.tajs.directives.unreachable";
            var tmp, args, proxy;
            if (typeof context === "string") {
                "dk.brics.tajs.directives.unreachable";
                tmp = fn[context];
                context = fn;
                fn = tmp;
            }
            // Quick check to determine if target is callable, in the spec
            // this throws a TypeError, but we will just return undefined.
            if (!jQuery.isFunction(fn)) {
                "dk.brics.tajs.directives.unreachable";
                return undefined;
            }
            // Simulated bind
            args = core_slice.call(arguments, 2);
            proxy = function() {
                "dk.brics.tajs.directives.unreachable";
                return fn.apply(context, args.concat(core_slice.call(arguments)));
            };
            // Set the guid of unique handler to the same of original handler, so it can be removed
            proxy.guid = fn.guid = fn.guid || proxy.guid || jQuery.guid++;
            return proxy;
        },
        // Multifunctional method to get and set values of a collection
        // The value/s can optionally be executed if it's a function
        access: function(elems, fn, key, value, chainable, emptyGet, pass) {
            "dk.brics.tajs.directives.unreachable";
            var exec, bulk = key == null, i = 0, length = elems.length;
            // Sets many values
            if (key && typeof key === "object") {
                "dk.brics.tajs.directives.unreachable";
                for (i in key) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.access(elems, fn, i, key[i], 1, emptyGet, value);
                }
                chainable = 1;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (value !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    // Optionally, function values get executed if exec is true
                    exec = pass === undefined && jQuery.isFunction(value);
                    if (bulk) {
                        "dk.brics.tajs.directives.unreachable";
                        // Bulk operations only iterate when executing function values
                        if (exec) {
                            "dk.brics.tajs.directives.unreachable";
                            exec = fn;
                            fn = function(elem, key, value) {
                                "dk.brics.tajs.directives.unreachable";
                                return exec.call(jQuery(elem), value);
                            };
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            fn.call(elems, value);
                            fn = null;
                        }
                    }
                    if (fn) {
                        "dk.brics.tajs.directives.unreachable";
                        for (;i < length; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            fn(elems[i], key, exec ? value.call(elems[i], i, fn(elems[i], key)) : value, pass);
                        }
                    }
                    chainable = 1;
                }
            }
            return chainable ? elems : // Gets
            bulk ? fn.call(elems) : length ? fn(elems[0], key) : emptyGet;
        },
        now: function() {
            return new Date().getTime();
        }
    });
    jQuery.ready.promise = function(obj) {
        if (!readyList) {
            readyList = jQuery.Deferred();
            // Catch cases where $(document).ready() is called after the
            // browser event has already occurred.
            if (document.readyState === "complete" || document.readyState !== "loading" && document.addEventListener) {
                "dk.brics.tajs.directives.unreachable";
                // Handle it asynchronously to allow scripts the opportunity to delay ready
                setTimeout(jQuery.ready, 1);
            } else {
                if (document.addEventListener) {
                    // Use the handy event callback
                    document.addEventListener("DOMContentLoaded", DOMContentLoaded, false);
                    // A fallback to window.onload, that will always work
                    window.addEventListener("load", jQuery.ready, false);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // Ensure firing before onload, maybe late but safe also for iframes
                    document.attachEvent("onreadystatechange", DOMContentLoaded);
                    // A fallback to window.onload, that will always work
                    window.attachEvent("onload", jQuery.ready);
                    // If IE and not a frame
                    // continually check to see if the document is ready
                    var top = false;
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        top = window.frameElement == null && document.documentElement;
                    } catch (e) {}
                    if (top && top.doScroll) {
                        "dk.brics.tajs.directives.unreachable";
                        (function doScrollCheck() {
                            "dk.brics.tajs.directives.unreachable";
                            if (!jQuery.isReady) {
                                "dk.brics.tajs.directives.unreachable";
                                try {
                                    "dk.brics.tajs.directives.unreachable";
                                    // Use the trick by Diego Perini
                                    // http://javascript.nwbox.com/IEContentLoaded/
                                    top.doScroll("left");
                                } catch (e) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return setTimeout(doScrollCheck, 50);
                                }
                                // and execute any waiting functions
                                jQuery.ready();
                            }
                        })();
                    }
                }
            }
        }
        return readyList.promise(obj);
    };
    // Populate the class2type map
    jQuery.each("Boolean Number String Function Array Date RegExp Object".split(" "), function(i, name) {
        class2type["[object " + name + "]"] = name.toLowerCase();
    });
    // All jQuery objects should point back to these
    rootjQuery = jQuery(document);
    // String to Object options format cache
    var optionsCache = {};
    // Convert String-formatted options into Object-formatted ones and store in cache
    function createOptions(options) {
        var object = optionsCache[options] = {};
        jQuery.each(options.split(core_rspace), function(_, flag) {
            object[flag] = true;
        });
        return object;
    }
    /*
 * Create a callback list using the following parameters:
 *
 *	options: an optional list of space-separated options that will change how
 *			the callback list behaves or a more traditional option object
 *
 * By default a callback list will act like an event callback list and can be
 * "fired" multiple times.
 *
 * Possible options:
 *
 *	once:			will ensure the callback list can only be fired once (like a Deferred)
 *
 *	memory:			will keep track of previous values and will call any callback added
 *					after the list has been fired right away with the latest "memorized"
 *					values (like a Deferred)
 *
 *	unique:			will ensure a callback can only be added once (no duplicate in the list)
 *
 *	stopOnFalse:	interrupt callings when a callback returns false
 *
 */
    jQuery.Callbacks = function(options) {
        // Convert options from String-formatted to Object-formatted if needed
        // (we check in cache first)
        options = typeof options === "string" ? optionsCache[options] || createOptions(options) : jQuery.extend({}, options);
        var // Last fire value (for non-forgettable lists)
        memory, // Flag to know if list was already fired
        fired, // Flag to know if list is currently firing
        firing, // First callback to fire (used internally by add and fireWith)
        firingStart, // End of the loop when firing
        firingLength, // Index of currently firing callback (modified by remove if needed)
        firingIndex, // Actual callback list
        list = [], // Stack of fire calls for repeatable lists
        stack = !options.once && [], // Fire callbacks
        fire = function(data) {
            memory = options.memory && data;
            fired = true;
            firingIndex = firingStart || 0;
            firingStart = 0;
            firingLength = list.length;
            firing = true;
            for (;list && firingIndex < firingLength; firingIndex++) {
                if (list[firingIndex].apply(data[0], data[1]) === false && options.stopOnFalse) {
                    "dk.brics.tajs.directives.unreachable";
                    memory = false;
                    // To prevent further calls using add
                    break;
                }
            }
            firing = false;
            if (list) {
                if (stack) {
                    "dk.brics.tajs.directives.unreachable";
                    if (stack.length) {
                        "dk.brics.tajs.directives.unreachable";
                        fire(stack.shift());
                    }
                } else {
                    if (memory) {
                        list = [];
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        self.disable();
                    }
                }
            }
        }, // Actual Callbacks object
        self = {
            // Add a callback or a collection of callbacks to the list
            add: function() {
                if (list) {
                    // First, we save the current length
                    var start = list.length;
                    (function add(args) {
                        jQuery.each(args, function(_, arg) {
                            if (jQuery.isFunction(arg) && (!options.unique || !self.has(arg))) {
                                list.push(arg);
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (arg && arg.length) {
                                    "dk.brics.tajs.directives.unreachable";
                                    // Inspect recursively
                                    add(arg);
                                }
                            }
                        });
                    })(arguments);
                    // Do we need to add the callbacks to the
                    // current firing batch?
                    if (firing) {
                        "dk.brics.tajs.directives.unreachable";
                        firingLength = list.length;
                    } else {
                        if (memory) {
                            "dk.brics.tajs.directives.unreachable";
                            firingStart = start;
                            fire(memory);
                        }
                    }
                }
                return this;
            },
            // Remove a callback from the list
            remove: function() {
                "dk.brics.tajs.directives.unreachable";
                if (list) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.each(arguments, function(_, arg) {
                        "dk.brics.tajs.directives.unreachable";
                        var index;
                        while ((index = jQuery.inArray(arg, list, index)) > -1) {
                            "dk.brics.tajs.directives.unreachable";
                            list.splice(index, 1);
                            // Handle firing indexes
                            if (firing) {
                                "dk.brics.tajs.directives.unreachable";
                                if (index <= firingLength) {
                                    "dk.brics.tajs.directives.unreachable";
                                    firingLength--;
                                }
                                if (index <= firingIndex) {
                                    "dk.brics.tajs.directives.unreachable";
                                    firingIndex--;
                                }
                            }
                        }
                    });
                }
                return this;
            },
            // Control if a given callback is in the list
            has: function(fn) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.inArray(fn, list) > -1;
            },
            // Remove all callbacks from the list
            empty: function() {
                "dk.brics.tajs.directives.unreachable";
                list = [];
                return this;
            },
            // Have the list do nothing anymore
            disable: function() {
                list = stack = memory = undefined;
                return this;
            },
            // Is it disabled?
            disabled: function() {
                "dk.brics.tajs.directives.unreachable";
                return !list;
            },
            // Lock the list in its current state
            lock: function() {
                stack = undefined;
                if (!memory) {
                    self.disable();
                }
                return this;
            },
            // Is it locked?
            locked: function() {
                "dk.brics.tajs.directives.unreachable";
                return !stack;
            },
            // Call all callbacks with the given context and arguments
            fireWith: function(context, args) {
                args = args || [];
                args = [ context, args.slice ? args.slice() : args ];
                if (list && (!fired || stack)) {
                    if (firing) {
                        "dk.brics.tajs.directives.unreachable";
                        stack.push(args);
                    } else {
                        fire(args);
                    }
                }
                return this;
            },
            // Call all the callbacks with the given arguments
            fire: function() {
                "dk.brics.tajs.directives.unreachable";
                self.fireWith(this, arguments);
                return this;
            },
            // To know if the callbacks have already been called at least once
            fired: function() {
                "dk.brics.tajs.directives.unreachable";
                return !!fired;
            }
        };
        return self;
    };
    jQuery.extend({
        Deferred: function(func) {
            var tuples = [ // action, add listener, listener list, final state
            [ "resolve", "done", jQuery.Callbacks("once memory"), "resolved" ], [ "reject", "fail", jQuery.Callbacks("once memory"), "rejected" ], [ "notify", "progress", jQuery.Callbacks("memory") ] ], state = "pending", promise = {
                state: function() {
                    "dk.brics.tajs.directives.unreachable";
                    return state;
                },
                always: function() {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.done(arguments).fail(arguments);
                    return this;
                },
                then: function() {
                    "dk.brics.tajs.directives.unreachable";
                    var fns = arguments;
                    return jQuery.Deferred(function(newDefer) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.each(tuples, function(i, tuple) {
                            "dk.brics.tajs.directives.unreachable";
                            var action = tuple[0], fn = fns[i];
                            // deferred[ done | fail | progress ] for forwarding actions to newDefer
                            deferred[tuple[1]](jQuery.isFunction(fn) ? function() {
                                "dk.brics.tajs.directives.unreachable";
                                var returned = fn.apply(this, arguments);
                                if (returned && jQuery.isFunction(returned.promise)) {
                                    "dk.brics.tajs.directives.unreachable";
                                    returned.promise().done(newDefer.resolve).fail(newDefer.reject).progress(newDefer.notify);
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    newDefer[action + "With"](this === deferred ? newDefer : this, [ returned ]);
                                }
                            } : newDefer[action]);
                        });
                        fns = null;
                    }).promise();
                },
                // Get a promise for this deferred
                // If obj is provided, the promise aspect is added to the object
                promise: function(obj) {
                    return typeof obj === "object" ? jQuery.extend(obj, promise) : promise;
                }
            }, deferred = {};
            // Keep pipe for back-compat
            promise.pipe = promise.then;
            // Add list-specific methods
            jQuery.each(tuples, function(i, tuple) {
                var list = tuple[2], stateString = tuple[3];
                // promise[ done | fail | progress ] = list.add
                promise[tuple[1]] = list.add;
                // Handle state
                if (stateString) {
                    list.add(function() {
                        // state = [ resolved | rejected ]
                        state = stateString;
                    }, tuples[i ^ 1][2].disable, tuples[2][2].lock);
                }
                // deferred[ resolve | reject | notify ] = list.fire
                deferred[tuple[0]] = list.fire;
                deferred[tuple[0] + "With"] = list.fireWith;
            });
            // Make the deferred a promise
            promise.promise(deferred);
            // Call given func if any
            if (func) {
                "dk.brics.tajs.directives.unreachable";
                func.call(deferred, deferred);
            }
            // All done!
            return deferred;
        },
        // Deferred helper
        when: function(subordinate) {
            "dk.brics.tajs.directives.unreachable";
            var i = 0, resolveValues = core_slice.call(arguments), length = resolveValues.length, // the count of uncompleted subordinates
            remaining = length !== 1 || subordinate && jQuery.isFunction(subordinate.promise) ? length : 0, // the master Deferred. If resolveValues consist of only a single Deferred, just use that.
            deferred = remaining === 1 ? subordinate : jQuery.Deferred(), // Update function for both resolve and progress values
            updateFunc = function(i, contexts, values) {
                "dk.brics.tajs.directives.unreachable";
                return function(value) {
                    "dk.brics.tajs.directives.unreachable";
                    contexts[i] = this;
                    values[i] = arguments.length > 1 ? core_slice.call(arguments) : value;
                    if (values === progressValues) {
                        "dk.brics.tajs.directives.unreachable";
                        deferred.notifyWith(contexts, values);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (!--remaining) {
                            "dk.brics.tajs.directives.unreachable";
                            deferred.resolveWith(contexts, values);
                        }
                    }
                };
            }, progressValues, progressContexts, resolveContexts;
            // add listeners to Deferred subordinates; treat others as resolved
            if (length > 1) {
                "dk.brics.tajs.directives.unreachable";
                progressValues = new Array(length);
                progressContexts = new Array(length);
                resolveContexts = new Array(length);
                for (;i < length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (resolveValues[i] && jQuery.isFunction(resolveValues[i].promise)) {
                        "dk.brics.tajs.directives.unreachable";
                        resolveValues[i].promise().done(updateFunc(i, resolveContexts, resolveValues)).fail(deferred.reject).progress(updateFunc(i, progressContexts, progressValues));
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        --remaining;
                    }
                }
            }
            // if we're not waiting on anything, resolve the master
            if (!remaining) {
                "dk.brics.tajs.directives.unreachable";
                deferred.resolveWith(resolveContexts, resolveValues);
            }
            return deferred.promise();
        }
    });
    jQuery.support = function() {
        var support, all, a, select, opt, input, fragment, eventName, i, isSupported, clickFn, div = document.createElement("div");
        // Preliminary tests
        div.setAttribute("className", "t");
        div.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";
        all = div.getElementsByTagName("*");
        a = div.getElementsByTagName("a")[0];
        a.style.cssText = "top:1px;float:left;opacity:.5";
        // Can't get basic test support
        if (!all || !all.length || !a) {
            "dk.brics.tajs.directives.unreachable";
            return {};
        }
        // First batch of supports tests
        select = document.createElement("select");
        opt = select.appendChild(document.createElement("option"));
        input = div.getElementsByTagName("input")[0];
        support = {
            // IE strips leading whitespace when .innerHTML is used
            leadingWhitespace: div.firstChild.nodeType === 3,
            // Make sure that tbody elements aren't automatically inserted
            // IE will insert them into empty tables
            tbody: !div.getElementsByTagName("tbody").length,
            // Make sure that link elements get serialized correctly by innerHTML
            // This requires a wrapper element in IE
            htmlSerialize: !!div.getElementsByTagName("link").length,
            // Get the style information from getAttribute
            // (IE uses .cssText instead)
            style: /top/.test(a.getAttribute("style")),
            // Make sure that URLs aren't manipulated
            // (IE normalizes it by default)
            hrefNormalized: a.getAttribute("href") === "/a",
            // Make sure that element opacity exists
            // (IE uses filter instead)
            // Use a regex to work around a WebKit issue. See #5145
            opacity: /^0.5/.test(a.style.opacity),
            // Verify style float existence
            // (IE uses styleFloat instead of cssFloat)
            cssFloat: !!a.style.cssFloat,
            // Make sure that if no value is specified for a checkbox
            // that it defaults to "on".
            // (WebKit defaults to "" instead)
            checkOn: input.value === "on",
            // Make sure that a selected-by-default option has a working selected property.
            // (WebKit defaults to false instead of true, IE too, if it's in an optgroup)
            optSelected: opt.selected,
            // Test setAttribute on camelCase class. If it works, we need attrFixes when doing get/setAttribute (ie6/7)
            getSetAttribute: div.className !== "t",
            // Tests for enctype support on a form(#6743)
            enctype: !!document.createElement("form").enctype,
            // Makes sure cloning an html5 element does not cause problems
            // Where outerHTML is undefined, this still works
            html5Clone: document.createElement("nav").cloneNode(true).outerHTML !== "<:nav></:nav>",
            // jQuery.support.boxModel DEPRECATED in 1.8 since we don't support Quirks Mode
            boxModel: document.compatMode === "CSS1Compat",
            // Will be defined later
            submitBubbles: true,
            changeBubbles: true,
            focusinBubbles: false,
            deleteExpando: true,
            noCloneEvent: true,
            inlineBlockNeedsLayout: false,
            shrinkWrapBlocks: false,
            reliableMarginRight: true,
            boxSizingReliable: true,
            pixelPosition: false
        };
        // Make sure checked status is properly cloned
        input.checked = true;
        support.noCloneChecked = input.cloneNode(true).checked;
        // Make sure that the options inside disabled selects aren't marked as disabled
        // (WebKit marks them as disabled)
        select.disabled = true;
        support.optDisabled = !opt.disabled;
        // Test to see if it's possible to delete an expando from an element
        // Fails in Internet Explorer
        try {
            delete div.test;
        } catch (e) {
            "dk.brics.tajs.directives.unreachable";
            support.deleteExpando = false;
        }
        if (!div.addEventListener && div.attachEvent && div.fireEvent) {
            "dk.brics.tajs.directives.unreachable";
            div.attachEvent("onclick", clickFn = function() {
                "dk.brics.tajs.directives.unreachable";
                // Cloning a node shouldn't copy over any
                // bound event handlers (IE does this)
                support.noCloneEvent = false;
            });
            div.cloneNode(true).fireEvent("onclick");
            div.detachEvent("onclick", clickFn);
        }
        // Check if a radio maintains its value
        // after being appended to the DOM
        input = document.createElement("input");
        input.value = "t";
        input.setAttribute("type", "radio");
        support.radioValue = input.value === "t";
        input.setAttribute("checked", "checked");
        // #11217 - WebKit loses check when the name is after the checked attribute
        input.setAttribute("name", "t");
        div.appendChild(input);
        fragment = document.createDocumentFragment();
        fragment.appendChild(div.lastChild);
        // WebKit doesn't clone checked state correctly in fragments
        support.checkClone = fragment.cloneNode(true).cloneNode(true).lastChild.checked;
        // Check if a disconnected checkbox will retain its checked
        // value of true after appended to the DOM (IE6/7)
        support.appendChecked = input.checked;
        fragment.removeChild(input);
        fragment.appendChild(div);
        // Technique from Juriy Zaytsev
        // http://perfectionkills.com/detecting-event-support-without-browser-sniffing/
        // We only care about the case where non-standard event systems
        // are used, namely in IE. Short-circuiting here helps us to
        // avoid an eval call (in setAttribute) which can cause CSP
        // to go haywire. See: https://developer.mozilla.org/en/Security/CSP
        if (div.attachEvent) {
            "dk.brics.tajs.directives.unreachable";
            for (i in {
                submit: true,
                change: true,
                focusin: true
            }) {
                "dk.brics.tajs.directives.unreachable";
                eventName = "on" + i;
                isSupported = eventName in div;
                if (!isSupported) {
                    "dk.brics.tajs.directives.unreachable";
                    div.setAttribute(eventName, "return;");
                    isSupported = typeof div[eventName] === "function";
                }
                support[i + "Bubbles"] = isSupported;
            }
        }
        // Run tests that need a body at doc ready
        jQuery(function() {
            var container, div, tds, marginDiv, divReset = "padding:0;margin:0;border:0;display:block;overflow:hidden;", body = document.getElementsByTagName("body")[0];
            if (!body) {
                "dk.brics.tajs.directives.unreachable";
                // Return for frameset docs that don't have a body
                return;
            }
            container = document.createElement("div");
            container.style.cssText = "visibility:hidden;border:0;width:0;height:0;position:static;top:0;margin-top:1px";
            body.insertBefore(container, body.firstChild);
            // Construct the test element
            div = document.createElement("div");
            container.appendChild(div);
            // Check if table cells still have offsetWidth/Height when they are set
            // to display:none and there are still other visible table cells in a
            // table row; if so, offsetWidth/Height are not reliable for use when
            // determining if an element has been hidden directly using
            // display:none (it is still safe to use offsets if a parent element is
            // hidden; don safety goggles and see bug #4512 for more information).
            // (only IE 8 fails this test)
            div.innerHTML = "<table><tr><td></td><td>t</td></tr></table>";
            tds = div.getElementsByTagName("td");
            tds[0].style.cssText = "padding:0;margin:0;border:0;display:none";
            isSupported = tds[0].offsetHeight === 0;
            tds[0].style.display = "";
            tds[1].style.display = "none";
            // Check if empty table cells still have offsetWidth/Height
            // (IE <= 8 fail this test)
            support.reliableHiddenOffsets = isSupported && tds[0].offsetHeight === 0;
            // Check box-sizing and margin behavior
            div.innerHTML = "";
            div.style.cssText = "box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;padding:1px;border:1px;display:block;width:4px;margin-top:1%;position:absolute;top:1%;";
            support.boxSizing = div.offsetWidth === 4;
            support.doesNotIncludeMarginInBodyOffset = body.offsetTop !== 1;
            // NOTE: To any future maintainer, window.getComputedStyle was used here
            // instead of getComputedStyle because it gave a better gzip size.
            // The difference between window.getComputedStyle and getComputedStyle is
            // 7 bytes
            if (window.getComputedStyle) {
                support.pixelPosition = (window.getComputedStyle(div, null) || {}).top !== "1%";
                support.boxSizingReliable = (window.getComputedStyle(div, null) || {
                    width: "4px"
                }).width === "4px";
                // Check if div with explicit width and no margin-right incorrectly
                // gets computed margin-right based on width of container. For more
                // info see bug #3333
                // Fails in WebKit before Feb 2011 nightlies
                // WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
                marginDiv = document.createElement("div");
                marginDiv.style.cssText = div.style.cssText = divReset;
                marginDiv.style.marginRight = marginDiv.style.width = "0";
                div.style.width = "1px";
                div.appendChild(marginDiv);
                support.reliableMarginRight = !parseFloat((window.getComputedStyle(marginDiv, null) || {}).marginRight);
            }
            if (typeof div.style.zoom !== "undefined") {
                // Check if natively block-level elements act like inline-block
                // elements when setting their display to 'inline' and giving
                // them layout
                // (IE < 8 does this)
                div.innerHTML = "";
                div.style.cssText = divReset + "width:1px;padding:1px;display:inline;zoom:1";
                support.inlineBlockNeedsLayout = div.offsetWidth === 3;
                // Check if elements with layout shrink-wrap their children
                // (IE 6 does this)
                div.style.display = "block";
                div.style.overflow = "visible";
                div.innerHTML = "<div></div>";
                div.firstChild.style.width = "5px";
                support.shrinkWrapBlocks = div.offsetWidth !== 3;
                container.style.zoom = 1;
            }
            // Null elements to avoid leaks in IE
            body.removeChild(container);
            container = div = tds = marginDiv = null;
        });
        // Null elements to avoid leaks in IE
        fragment.removeChild(div);
        all = a = select = opt = input = fragment = div = null;
        return support;
    }();
    var rbrace = /^(?:\{.*\}|\[.*\])$/, rmultiDash = /([A-Z])/g;
    jQuery.extend({
        cache: {},
        deletedIds: [],
        // Please use with caution
        uuid: 0,
        // Unique for each copy of jQuery on the page
        // Non-digits removed to match rinlinejQuery
        expando: "jQuery" + "TAJS_UUID",
        // The following elements throw uncatchable exceptions if you
        // attempt to add expando properties to them.
        noData: {
            embed: true,
            // Ban all objects except for Flash (which handle expandos)
            object: "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",
            applet: true
        },
        hasData: function(elem) {
            elem = elem.nodeType ? jQuery.cache[elem[jQuery.expando]] : elem[jQuery.expando];
            return !!elem && !isEmptyDataObject(elem);
        },
        data: function(elem, name, data, pvt) {
            if (!jQuery.acceptData(elem)) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var thisCache, ret, internalKey = jQuery.expando, getByName = typeof name === "string", // We have to handle DOM nodes and JS objects differently because IE6-7
            // can't GC object references properly across the DOM-JS boundary
            isNode = elem.nodeType, // Only DOM nodes need the global jQuery cache; JS object data is
            // attached directly to the object so GC can occur automatically
            cache = isNode ? jQuery.cache : elem, // Only defining an ID for JS objects if its cache already exists allows
            // the code to shortcut on the same path as a DOM node with no cache
            id = isNode ? elem[internalKey] : elem[internalKey] && internalKey;
            // Avoid doing any more work than we need to when trying to get data on an
            // object that has no data at all
            if ((!id || !cache[id] || !pvt && !cache[id].data) && getByName && data === undefined) {
                return;
            }
            "dk.brics.tajs.directives.unreachable";
            if (!id) {
                "dk.brics.tajs.directives.unreachable";
                // Only DOM nodes need a new unique ID for each element since their data
                // ends up in the global cache
                if (isNode) {
                    "dk.brics.tajs.directives.unreachable";
                    elem[internalKey] = id = jQuery.deletedIds.pop() || ++jQuery.uuid;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    id = internalKey;
                }
            }
            if (!cache[id]) {
                "dk.brics.tajs.directives.unreachable";
                cache[id] = {};
                // Avoids exposing jQuery metadata on plain JS objects when the object
                // is serialized using JSON.stringify
                if (!isNode) {
                    "dk.brics.tajs.directives.unreachable";
                    cache[id].toJSON = jQuery.noop;
                }
            }
            // An object can be passed to jQuery.data instead of a key/value pair; this gets
            // shallow copied over onto the existing cache
            if (typeof name === "object" || typeof name === "function") {
                "dk.brics.tajs.directives.unreachable";
                if (pvt) {
                    "dk.brics.tajs.directives.unreachable";
                    cache[id] = jQuery.extend(cache[id], name);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    cache[id].data = jQuery.extend(cache[id].data, name);
                }
            }
            thisCache = cache[id];
            // jQuery data() is stored in a separate object inside the object's internal data
            // cache in order to avoid key collisions between internal data and user-defined
            // data.
            if (!pvt) {
                "dk.brics.tajs.directives.unreachable";
                if (!thisCache.data) {
                    "dk.brics.tajs.directives.unreachable";
                    thisCache.data = {};
                }
                thisCache = thisCache.data;
            }
            if (data !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                thisCache[jQuery.camelCase(name)] = data;
            }
            // Check for both converted-to-camel and non-converted data property names
            // If a data property was specified
            if (getByName) {
                "dk.brics.tajs.directives.unreachable";
                // First Try to find as-is property data
                ret = thisCache[name];
                // Test for null|undefined property data
                if (ret == null) {
                    "dk.brics.tajs.directives.unreachable";
                    // Try to find the camelCased property
                    ret = thisCache[jQuery.camelCase(name)];
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                ret = thisCache;
            }
            return ret;
        },
        removeData: function(elem, name, pvt) {
            "dk.brics.tajs.directives.unreachable";
            if (!jQuery.acceptData(elem)) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var thisCache, i, l, isNode = elem.nodeType, // See jQuery.data for more information
            cache = isNode ? jQuery.cache : elem, id = isNode ? elem[jQuery.expando] : jQuery.expando;
            // If there is already no cache entry for this object, there is no
            // purpose in continuing
            if (!cache[id]) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            if (name) {
                "dk.brics.tajs.directives.unreachable";
                thisCache = pvt ? cache[id] : cache[id].data;
                if (thisCache) {
                    "dk.brics.tajs.directives.unreachable";
                    // Support array or space separated string names for data keys
                    if (!jQuery.isArray(name)) {
                        "dk.brics.tajs.directives.unreachable";
                        // try the string as a key before any manipulation
                        if (name in thisCache) {
                            "dk.brics.tajs.directives.unreachable";
                            name = [ name ];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // split the camel cased version by spaces unless a key with the spaces exists
                            name = jQuery.camelCase(name);
                            if (name in thisCache) {
                                "dk.brics.tajs.directives.unreachable";
                                name = [ name ];
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                name = name.split(" ");
                            }
                        }
                    }
                    for (i = 0, l = name.length; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        delete thisCache[name[i]];
                    }
                    // If there is no data left in the cache, we want to continue
                    // and let the cache object itself get destroyed
                    if (!(pvt ? isEmptyDataObject : jQuery.isEmptyObject)(thisCache)) {
                        "dk.brics.tajs.directives.unreachable";
                        return;
                    }
                }
            }
            // See jQuery.data for more information
            if (!pvt) {
                "dk.brics.tajs.directives.unreachable";
                delete cache[id].data;
                // Don't destroy the parent cache unless the internal data object
                // had been the only thing left in it
                if (!isEmptyDataObject(cache[id])) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
            }
            // Destroy the cache
            if (isNode) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.cleanData([ elem ], true);
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.support.deleteExpando || cache != cache.window) {
                    "dk.brics.tajs.directives.unreachable";
                    delete cache[id];
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    cache[id] = null;
                }
            }
        },
        // For internal use only.
        _data: function(elem, name, data) {
            return jQuery.data(elem, name, data, true);
        },
        // A method for determining if a DOM node can handle the data expando
        acceptData: function(elem) {
            var noData = elem.nodeName && jQuery.noData[elem.nodeName.toLowerCase()];
            // nodes accept data unless otherwise specified; rejection can be conditional
            return !noData || noData !== true && elem.getAttribute("classid") === noData;
        }
    });
    jQuery.fn.extend({
        data: function(key, value) {
            "dk.brics.tajs.directives.unreachable";
            var parts, part, attr, name, l, elem = this[0], i = 0, data = null;
            // Gets all values
            if (key === undefined) {
                "dk.brics.tajs.directives.unreachable";
                if (this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    data = jQuery.data(elem);
                    if (elem.nodeType === 1 && !jQuery._data(elem, "parsedAttrs")) {
                        "dk.brics.tajs.directives.unreachable";
                        attr = elem.attributes;
                        for (l = attr.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            name = attr[i].name;
                            if (name.indexOf("data-") === 0) {
                                "dk.brics.tajs.directives.unreachable";
                                name = jQuery.camelCase(name.substring(5));
                                dataAttr(elem, name, data[name]);
                            }
                        }
                        jQuery._data(elem, "parsedAttrs", true);
                    }
                }
                return data;
            }
            // Sets multiple values
            if (typeof key === "object") {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.data(this, key);
                });
            }
            parts = key.split(".", 2);
            parts[1] = parts[1] ? "." + parts[1] : "";
            part = parts[1] + "!";
            return jQuery.access(this, function(value) {
                "dk.brics.tajs.directives.unreachable";
                if (value === undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    data = this.triggerHandler("getData" + part, [ parts[0] ]);
                    // Try to fetch any internally stored data first
                    if (data === undefined && elem) {
                        "dk.brics.tajs.directives.unreachable";
                        data = jQuery.data(elem, key);
                        data = dataAttr(elem, key, data);
                    }
                    return data === undefined && parts[1] ? this.data(parts[0]) : data;
                }
                parts[1] = value;
                this.each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    var self = jQuery(this);
                    self.triggerHandler("setData" + part, parts);
                    jQuery.data(this, key, value);
                    self.triggerHandler("changeData" + part, parts);
                });
            }, null, value, arguments.length > 1, null, false);
        },
        removeData: function(key) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.removeData(this, key);
            });
        }
    });
    function dataAttr(elem, key, data) {
        "dk.brics.tajs.directives.unreachable";
        // If nothing was found internally, try to fetch any
        // data from the HTML5 data-* attribute
        if (data === undefined && elem.nodeType === 1) {
            "dk.brics.tajs.directives.unreachable";
            var name = "data-" + key.replace(rmultiDash, "-$1").toLowerCase();
            data = elem.getAttribute(name);
            if (typeof data === "string") {
                "dk.brics.tajs.directives.unreachable";
                try {
                    "dk.brics.tajs.directives.unreachable";
                    data = data === "true" ? true : data === "false" ? false : data === "null" ? null : // Only convert to a number if it doesn't change the string
                    +data + "" === data ? +data : rbrace.test(data) ? jQuery.parseJSON(data) : data;
                } catch (e) {}
                // Make sure we set the data so it isn't changed later
                jQuery.data(elem, key, data);
            } else {
                "dk.brics.tajs.directives.unreachable";
                data = undefined;
            }
        }
        return data;
    }
    // checks a cache object for emptiness
    function isEmptyDataObject(obj) {
        "dk.brics.tajs.directives.unreachable";
        var name;
        for (name in obj) {
            "dk.brics.tajs.directives.unreachable";
            // if the public data object is empty, the private is still empty
            if (name === "data" && jQuery.isEmptyObject(obj[name])) {
                "dk.brics.tajs.directives.unreachable";
                continue;
            }
            if (name !== "toJSON") {
                "dk.brics.tajs.directives.unreachable";
                return false;
            }
        }
        return true;
    }
    jQuery.extend({
        queue: function(elem, type, data) {
            "dk.brics.tajs.directives.unreachable";
            var queue;
            if (elem) {
                "dk.brics.tajs.directives.unreachable";
                type = (type || "fx") + "queue";
                queue = jQuery._data(elem, type);
                // Speed up dequeue by getting out quickly if this is just a lookup
                if (data) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!queue || jQuery.isArray(data)) {
                        "dk.brics.tajs.directives.unreachable";
                        queue = jQuery._data(elem, type, jQuery.makeArray(data));
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        queue.push(data);
                    }
                }
                return queue || [];
            }
        },
        dequeue: function(elem, type) {
            "dk.brics.tajs.directives.unreachable";
            type = type || "fx";
            var queue = jQuery.queue(elem, type), fn = queue.shift(), hooks = jQuery._queueHooks(elem, type), next = function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.dequeue(elem, type);
            };
            // If the fx queue is dequeued, always remove the progress sentinel
            if (fn === "inprogress") {
                "dk.brics.tajs.directives.unreachable";
                fn = queue.shift();
            }
            if (fn) {
                "dk.brics.tajs.directives.unreachable";
                // Add a progress sentinel to prevent the fx queue from being
                // automatically dequeued
                if (type === "fx") {
                    "dk.brics.tajs.directives.unreachable";
                    queue.unshift("inprogress");
                }
                // clear up the last queue stop function
                delete hooks.stop;
                fn.call(elem, next, hooks);
            }
            if (!queue.length && hooks) {
                "dk.brics.tajs.directives.unreachable";
                hooks.empty.fire();
            }
        },
        // not intended for public consumption - generates a queueHooks object, or returns the current one
        _queueHooks: function(elem, type) {
            "dk.brics.tajs.directives.unreachable";
            var key = type + "queueHooks";
            return jQuery._data(elem, key) || jQuery._data(elem, key, {
                empty: jQuery.Callbacks("once memory").add(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.removeData(elem, type + "queue", true);
                    jQuery.removeData(elem, key, true);
                })
            });
        }
    });
    jQuery.fn.extend({
        queue: function(type, data) {
            "dk.brics.tajs.directives.unreachable";
            var setter = 2;
            if (typeof type !== "string") {
                "dk.brics.tajs.directives.unreachable";
                data = type;
                type = "fx";
                setter--;
            }
            if (arguments.length < setter) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.queue(this[0], type);
            }
            return data === undefined ? this : this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                var queue = jQuery.queue(this, type, data);
                // ensure a hooks for this queue
                jQuery._queueHooks(this, type);
                if (type === "fx" && queue[0] !== "inprogress") {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.dequeue(this, type);
                }
            });
        },
        dequeue: function(type) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.dequeue(this, type);
            });
        },
        // Based off of the plugin by Clint Helfers, with permission.
        // http://blindsignals.com/index.php/2009/07/jquery-delay/
        delay: function(time, type) {
            "dk.brics.tajs.directives.unreachable";
            time = jQuery.fx ? jQuery.fx.speeds[time] || time : time;
            type = type || "fx";
            return this.queue(type, function(next, hooks) {
                "dk.brics.tajs.directives.unreachable";
                var timeout = setTimeout(next, time);
                hooks.stop = function() {
                    "dk.brics.tajs.directives.unreachable";
                    clearTimeout(timeout);
                };
            });
        },
        clearQueue: function(type) {
            "dk.brics.tajs.directives.unreachable";
            return this.queue(type || "fx", []);
        },
        // Get a promise resolved when queues of a certain type
        // are emptied (fx is the type by default)
        promise: function(type, obj) {
            "dk.brics.tajs.directives.unreachable";
            var tmp, count = 1, defer = jQuery.Deferred(), elements = this, i = this.length, resolve = function() {
                "dk.brics.tajs.directives.unreachable";
                if (!--count) {
                    "dk.brics.tajs.directives.unreachable";
                    defer.resolveWith(elements, [ elements ]);
                }
            };
            if (typeof type !== "string") {
                "dk.brics.tajs.directives.unreachable";
                obj = type;
                type = undefined;
            }
            type = type || "fx";
            while (i--) {
                "dk.brics.tajs.directives.unreachable";
                if ((tmp = jQuery._data(elements[i], type + "queueHooks")) && tmp.empty) {
                    "dk.brics.tajs.directives.unreachable";
                    count++;
                    tmp.empty.add(resolve);
                }
            }
            resolve();
            return defer.promise(obj);
        }
    });
    var nodeHook, boolHook, fixSpecified, rclass = /[\t\r\n]/g, rreturn = /\r/g, rtype = /^(?:button|input)$/i, rfocusable = /^(?:button|input|object|select|textarea)$/i, rclickable = /^a(?:rea|)$/i, rboolean = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i, getSetAttribute = jQuery.support.getSetAttribute;
    jQuery.fn.extend({
        attr: function(name, value) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, jQuery.attr, name, value, arguments.length > 1);
        },
        removeAttr: function(name) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.removeAttr(this, name);
            });
        },
        prop: function(name, value) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, jQuery.prop, name, value, arguments.length > 1);
        },
        removeProp: function(name) {
            "dk.brics.tajs.directives.unreachable";
            name = jQuery.propFix[name] || name;
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                // try/catch handles cases where IE balks (such as removing a property on window)
                try {
                    "dk.brics.tajs.directives.unreachable";
                    this[name] = undefined;
                    delete this[name];
                } catch (e) {}
            });
        },
        addClass: function(value) {
            "dk.brics.tajs.directives.unreachable";
            var classNames, i, l, elem, setClass, c, cl;
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(j) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).addClass(value.call(this, j, this.className));
                });
            }
            if (value && typeof value === "string") {
                "dk.brics.tajs.directives.unreachable";
                classNames = value.split(core_rspace);
                for (i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = this[i];
                    if (elem.nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        if (!elem.className && classNames.length === 1) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.className = value;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            setClass = " " + elem.className + " ";
                            for (c = 0, cl = classNames.length; c < cl; c++) {
                                "dk.brics.tajs.directives.unreachable";
                                if (!~setClass.indexOf(" " + classNames[c] + " ")) {
                                    "dk.brics.tajs.directives.unreachable";
                                    setClass += classNames[c] + " ";
                                }
                            }
                            elem.className = jQuery.trim(setClass);
                        }
                    }
                }
            }
            return this;
        },
        removeClass: function(value) {
            "dk.brics.tajs.directives.unreachable";
            var removes, className, elem, c, cl, i, l;
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(j) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).removeClass(value.call(this, j, this.className));
                });
            }
            if (value && typeof value === "string" || value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                removes = (value || "").split(core_rspace);
                for (i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = this[i];
                    if (elem.nodeType === 1 && elem.className) {
                        "dk.brics.tajs.directives.unreachable";
                        className = (" " + elem.className + " ").replace(rclass, " ");
                        // loop over each item in the removal list
                        for (c = 0, cl = removes.length; c < cl; c++) {
                            "dk.brics.tajs.directives.unreachable";
                            // Remove until there is nothing to remove,
                            while (className.indexOf(" " + removes[c] + " ") > -1) {
                                "dk.brics.tajs.directives.unreachable";
                                className = className.replace(" " + removes[c] + " ", " ");
                            }
                        }
                        elem.className = value ? jQuery.trim(className) : "";
                    }
                }
            }
            return this;
        },
        toggleClass: function(value, stateVal) {
            "dk.brics.tajs.directives.unreachable";
            var type = typeof value, isBool = typeof stateVal === "boolean";
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).toggleClass(value.call(this, i, this.className, stateVal), stateVal);
                });
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (type === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    // toggle individual class names
                    var className, i = 0, self = jQuery(this), state = stateVal, classNames = value.split(core_rspace);
                    while (className = classNames[i++]) {
                        "dk.brics.tajs.directives.unreachable";
                        // check each className given, space separated list
                        state = isBool ? state : !self.hasClass(className);
                        self[state ? "addClass" : "removeClass"](className);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (type === "undefined" || type === "boolean") {
                        "dk.brics.tajs.directives.unreachable";
                        if (this.className) {
                            "dk.brics.tajs.directives.unreachable";
                            // store className if set
                            jQuery._data(this, "__className__", this.className);
                        }
                        // toggle whole className
                        this.className = this.className || value === false ? "" : jQuery._data(this, "__className__") || "";
                    }
                }
            });
        },
        hasClass: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            var className = " " + selector + " ", i = 0, l = this.length;
            for (;i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (this[i].nodeType === 1 && (" " + this[i].className + " ").replace(rclass, " ").indexOf(className) > -1) {
                    "dk.brics.tajs.directives.unreachable";
                    return true;
                }
            }
            return false;
        },
        val: function(value) {
            "dk.brics.tajs.directives.unreachable";
            var hooks, ret, isFunction, elem = this[0];
            if (!arguments.length) {
                "dk.brics.tajs.directives.unreachable";
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    hooks = jQuery.valHooks[elem.type] || jQuery.valHooks[elem.nodeName.toLowerCase()];
                    if (hooks && "get" in hooks && (ret = hooks.get(elem, "value")) !== undefined) {
                        "dk.brics.tajs.directives.unreachable";
                        return ret;
                    }
                    ret = elem.value;
                    return typeof ret === "string" ? // handle most common string cases
                    ret.replace(rreturn, "") : // handle cases where value is null/undef or number
                    ret == null ? "" : ret;
                }
                return;
            }
            isFunction = jQuery.isFunction(value);
            return this.each(function(i) {
                "dk.brics.tajs.directives.unreachable";
                var val, self = jQuery(this);
                if (this.nodeType !== 1) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                if (isFunction) {
                    "dk.brics.tajs.directives.unreachable";
                    val = value.call(this, i, self.val());
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    val = value;
                }
                // Treat null/undefined as ""; convert numbers to string
                if (val == null) {
                    "dk.brics.tajs.directives.unreachable";
                    val = "";
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof val === "number") {
                        "dk.brics.tajs.directives.unreachable";
                        val += "";
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (jQuery.isArray(val)) {
                            "dk.brics.tajs.directives.unreachable";
                            val = jQuery.map(val, function(value) {
                                "dk.brics.tajs.directives.unreachable";
                                return value == null ? "" : value + "";
                            });
                        }
                    }
                }
                hooks = jQuery.valHooks[this.type] || jQuery.valHooks[this.nodeName.toLowerCase()];
                // If set returns undefined, fall back to normal setting
                if (!hooks || !("set" in hooks) || hooks.set(this, val, "value") === undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    this.value = val;
                }
            });
        }
    });
    jQuery.extend({
        valHooks: {
            option: {
                get: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // attributes.value is undefined in Blackberry 4.7 but
                    // uses .value. See #6932
                    var val = elem.attributes.value;
                    return !val || val.specified ? elem.value : elem.text;
                }
            },
            select: {
                get: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var value, i, max, option, index = elem.selectedIndex, values = [], options = elem.options, one = elem.type === "select-one";
                    // Nothing was selected
                    if (index < 0) {
                        "dk.brics.tajs.directives.unreachable";
                        return null;
                    }
                    // Loop through all the selected options
                    i = one ? index : 0;
                    max = one ? index + 1 : options.length;
                    for (;i < max; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        option = options[i];
                        // Don't return options that are disabled or in a disabled optgroup
                        if (option.selected && (jQuery.support.optDisabled ? !option.disabled : option.getAttribute("disabled") === null) && (!option.parentNode.disabled || !jQuery.nodeName(option.parentNode, "optgroup"))) {
                            "dk.brics.tajs.directives.unreachable";
                            // Get the specific value for the option
                            value = jQuery(option).val();
                            // We don't need an array for one selects
                            if (one) {
                                "dk.brics.tajs.directives.unreachable";
                                return value;
                            }
                            // Multi-Selects return an array
                            values.push(value);
                        }
                    }
                    // Fixes Bug #2551 -- select.val() broken in IE after form.reset()
                    if (one && !values.length && options.length) {
                        "dk.brics.tajs.directives.unreachable";
                        return jQuery(options[index]).val();
                    }
                    return values;
                },
                set: function(elem, value) {
                    "dk.brics.tajs.directives.unreachable";
                    var values = jQuery.makeArray(value);
                    jQuery(elem).find("option").each(function() {
                        "dk.brics.tajs.directives.unreachable";
                        this.selected = jQuery.inArray(jQuery(this).val(), values) >= 0;
                    });
                    if (!values.length) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.selectedIndex = -1;
                    }
                    return values;
                }
            }
        },
        // Unused in 1.8, left in so attrFn-stabbers won't die; remove in 1.9
        attrFn: {},
        attr: function(elem, name, value, pass) {
            "dk.brics.tajs.directives.unreachable";
            var ret, hooks, notxml, nType = elem.nodeType;
            // don't get/set attributes on text, comment and attribute nodes
            if (!elem || nType === 3 || nType === 8 || nType === 2) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            if (pass && jQuery.isFunction(jQuery.fn[name])) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery(elem)[name](value);
            }
            // Fallback to prop when attributes are not supported
            if (typeof elem.getAttribute === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.prop(elem, name, value);
            }
            notxml = nType !== 1 || !jQuery.isXMLDoc(elem);
            // All attributes are lowercase
            // Grab necessary hook if one is defined
            if (notxml) {
                "dk.brics.tajs.directives.unreachable";
                name = name.toLowerCase();
                hooks = jQuery.attrHooks[name] || (rboolean.test(name) ? boolHook : nodeHook);
            }
            if (value !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                if (value === null) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.removeAttr(elem, name);
                    return;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (hooks && "set" in hooks && notxml && (ret = hooks.set(elem, value, name)) !== undefined) {
                        "dk.brics.tajs.directives.unreachable";
                        return ret;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        elem.setAttribute(name, "" + value);
                        return value;
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (hooks && "get" in hooks && notxml && (ret = hooks.get(elem, name)) !== null) {
                    "dk.brics.tajs.directives.unreachable";
                    return ret;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    ret = elem.getAttribute(name);
                    // Non-existent attributes return null, we normalize to undefined
                    return ret === null ? undefined : ret;
                }
            }
        },
        removeAttr: function(elem, value) {
            "dk.brics.tajs.directives.unreachable";
            var propName, attrNames, name, isBool, i = 0;
            if (value && elem.nodeType === 1) {
                "dk.brics.tajs.directives.unreachable";
                attrNames = value.split(core_rspace);
                for (;i < attrNames.length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    name = attrNames[i];
                    if (name) {
                        "dk.brics.tajs.directives.unreachable";
                        propName = jQuery.propFix[name] || name;
                        isBool = rboolean.test(name);
                        // See #9699 for explanation of this approach (setting first, then removal)
                        // Do not do this for boolean attributes (see #10870)
                        if (!isBool) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.attr(elem, name, "");
                        }
                        elem.removeAttribute(getSetAttribute ? name : propName);
                        // Set corresponding property to false for boolean attributes
                        if (isBool && propName in elem) {
                            "dk.brics.tajs.directives.unreachable";
                            elem[propName] = false;
                        }
                    }
                }
            }
        },
        attrHooks: {
            type: {
                set: function(elem, value) {
                    "dk.brics.tajs.directives.unreachable";
                    // We can't allow the type property to be changed (since it causes problems in IE)
                    if (rtype.test(elem.nodeName) && elem.parentNode) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.error("type property can't be changed");
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (!jQuery.support.radioValue && value === "radio" && jQuery.nodeName(elem, "input")) {
                            "dk.brics.tajs.directives.unreachable";
                            // Setting the type on a radio button after the value resets the value in IE6-9
                            // Reset value to it's default in case type is set after value
                            // This is for element creation
                            var val = elem.value;
                            elem.setAttribute("type", value);
                            if (val) {
                                "dk.brics.tajs.directives.unreachable";
                                elem.value = val;
                            }
                            return value;
                        }
                    }
                }
            },
            // Use the value property for back compat
            // Use the nodeHook for button elements in IE6/7 (#1954)
            value: {
                get: function(elem, name) {
                    "dk.brics.tajs.directives.unreachable";
                    if (nodeHook && jQuery.nodeName(elem, "button")) {
                        "dk.brics.tajs.directives.unreachable";
                        return nodeHook.get(elem, name);
                    }
                    return name in elem ? elem.value : null;
                },
                set: function(elem, value, name) {
                    "dk.brics.tajs.directives.unreachable";
                    if (nodeHook && jQuery.nodeName(elem, "button")) {
                        "dk.brics.tajs.directives.unreachable";
                        return nodeHook.set(elem, value, name);
                    }
                    // Does not return so that setAttribute is also used
                    elem.value = value;
                }
            }
        },
        propFix: {
            tabindex: "tabIndex",
            readonly: "readOnly",
            "for": "htmlFor",
            "class": "className",
            maxlength: "maxLength",
            cellspacing: "cellSpacing",
            cellpadding: "cellPadding",
            rowspan: "rowSpan",
            colspan: "colSpan",
            usemap: "useMap",
            frameborder: "frameBorder",
            contenteditable: "contentEditable"
        },
        prop: function(elem, name, value) {
            "dk.brics.tajs.directives.unreachable";
            var ret, hooks, notxml, nType = elem.nodeType;
            // don't get/set properties on text, comment and attribute nodes
            if (!elem || nType === 3 || nType === 8 || nType === 2) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            notxml = nType !== 1 || !jQuery.isXMLDoc(elem);
            if (notxml) {
                "dk.brics.tajs.directives.unreachable";
                // Fix name and attach hooks
                name = jQuery.propFix[name] || name;
                hooks = jQuery.propHooks[name];
            }
            if (value !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                if (hooks && "set" in hooks && (ret = hooks.set(elem, value, name)) !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    return ret;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return elem[name] = value;
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (hooks && "get" in hooks && (ret = hooks.get(elem, name)) !== null) {
                    "dk.brics.tajs.directives.unreachable";
                    return ret;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return elem[name];
                }
            }
        },
        propHooks: {
            tabIndex: {
                get: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // elem.tabIndex doesn't always return the correct value when it hasn't been explicitly set
                    // http://fluidproject.org/blog/2008/01/09/getting-setting-and-removing-tabindex-values-with-javascript/
                    var attributeNode = elem.getAttributeNode("tabindex");
                    return attributeNode && attributeNode.specified ? parseInt(attributeNode.value, 10) : rfocusable.test(elem.nodeName) || rclickable.test(elem.nodeName) && elem.href ? 0 : undefined;
                }
            }
        }
    });
    // Hook for boolean attributes
    boolHook = {
        get: function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            // Align boolean attributes with corresponding properties
            // Fall back to attribute presence where some booleans are not supported
            var attrNode, property = jQuery.prop(elem, name);
            return property === true || typeof property !== "boolean" && (attrNode = elem.getAttributeNode(name)) && attrNode.nodeValue !== false ? name.toLowerCase() : undefined;
        },
        set: function(elem, value, name) {
            "dk.brics.tajs.directives.unreachable";
            var propName;
            if (value === false) {
                "dk.brics.tajs.directives.unreachable";
                // Remove boolean attributes when set to false
                jQuery.removeAttr(elem, name);
            } else {
                "dk.brics.tajs.directives.unreachable";
                // value is true since we know at this point it's type boolean and not false
                // Set boolean attributes to the same name and set the DOM property
                propName = jQuery.propFix[name] || name;
                if (propName in elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // Only set the IDL specifically if it already exists on the element
                    elem[propName] = true;
                }
                elem.setAttribute(name, name.toLowerCase());
            }
            return name;
        }
    };
    // IE6/7 do not support getting/setting some attributes with get/setAttribute
    if (!getSetAttribute) {
        "dk.brics.tajs.directives.unreachable";
        fixSpecified = {
            name: true,
            id: true,
            coords: true
        };
        // Use this for any attribute in IE6/7
        // This fixes almost every IE6/7 issue
        nodeHook = jQuery.valHooks.button = {
            get: function(elem, name) {
                "dk.brics.tajs.directives.unreachable";
                var ret;
                ret = elem.getAttributeNode(name);
                return ret && (fixSpecified[name] ? ret.value !== "" : ret.specified) ? ret.value : undefined;
            },
            set: function(elem, value, name) {
                "dk.brics.tajs.directives.unreachable";
                // Set the existing or create a new attribute node
                var ret = elem.getAttributeNode(name);
                if (!ret) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = document.createAttribute(name);
                    elem.setAttributeNode(ret);
                }
                return ret.value = value + "";
            }
        };
        // Set width and height to auto instead of 0 on empty string( Bug #8150 )
        // This is for removals
        jQuery.each([ "width", "height" ], function(i, name) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.attrHooks[name] = jQuery.extend(jQuery.attrHooks[name], {
                set: function(elem, value) {
                    "dk.brics.tajs.directives.unreachable";
                    if (value === "") {
                        "dk.brics.tajs.directives.unreachable";
                        elem.setAttribute(name, "auto");
                        return value;
                    }
                }
            });
        });
        // Set contenteditable to false on removals(#10429)
        // Setting to empty string throws an error as an invalid value
        jQuery.attrHooks.contenteditable = {
            get: nodeHook.get,
            set: function(elem, value, name) {
                "dk.brics.tajs.directives.unreachable";
                if (value === "") {
                    "dk.brics.tajs.directives.unreachable";
                    value = "false";
                }
                nodeHook.set(elem, value, name);
            }
        };
    }
    // Some attributes require a special call on IE
    if (!jQuery.support.hrefNormalized) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.each([ "href", "src", "width", "height" ], function(i, name) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.attrHooks[name] = jQuery.extend(jQuery.attrHooks[name], {
                get: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var ret = elem.getAttribute(name, 2);
                    return ret === null ? undefined : ret;
                }
            });
        });
    }
    if (!jQuery.support.style) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.attrHooks.style = {
            get: function(elem) {
                "dk.brics.tajs.directives.unreachable";
                // Return undefined in the case of empty string
                // Normalize to lowercase since IE uppercases css property names
                return elem.style.cssText.toLowerCase() || undefined;
            },
            set: function(elem, value) {
                "dk.brics.tajs.directives.unreachable";
                return elem.style.cssText = "" + value;
            }
        };
    }
    // Safari mis-reports the default selected property of an option
    // Accessing the parent's selectedIndex property fixes it
    if (!jQuery.support.optSelected) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.propHooks.selected = jQuery.extend(jQuery.propHooks.selected, {
            get: function(elem) {
                "dk.brics.tajs.directives.unreachable";
                var parent = elem.parentNode;
                if (parent) {
                    "dk.brics.tajs.directives.unreachable";
                    parent.selectedIndex;
                    // Make sure that it also works with optgroups, see #5701
                    if (parent.parentNode) {
                        "dk.brics.tajs.directives.unreachable";
                        parent.parentNode.selectedIndex;
                    }
                }
                return null;
            }
        });
    }
    // IE6/7 call enctype encoding
    if (!jQuery.support.enctype) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.propFix.enctype = "encoding";
    }
    // Radios and checkboxes getter/setter
    if (!jQuery.support.checkOn) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.each([ "radio", "checkbox" ], function() {
            "dk.brics.tajs.directives.unreachable";
            jQuery.valHooks[this] = {
                get: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // Handle the case where in Webkit "" is returned instead of "on" if a value isn't specified
                    return elem.getAttribute("value") === null ? "on" : elem.value;
                }
            };
        });
    }
    jQuery.each([ "radio", "checkbox" ], function() {
        jQuery.valHooks[this] = jQuery.extend(jQuery.valHooks[this], {
            set: function(elem, value) {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.isArray(value)) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.checked = jQuery.inArray(jQuery(elem).val(), value) >= 0;
                }
            }
        });
    });
    var rformElems = /^(?:textarea|input|select)$/i, rtypenamespace = /^([^\.]*|)(?:\.(.+)|)$/, rhoverHack = /(?:^|\s)hover(\.\S+|)\b/, rkeyEvent = /^key/, rmouseEvent = /^(?:mouse|contextmenu)|click/, rfocusMorph = /^(?:focusinfocus|focusoutblur)$/, hoverHack = function(events) {
        "dk.brics.tajs.directives.unreachable";
        return jQuery.event.special.hover ? events : events.replace(rhoverHack, "mouseenter$1 mouseleave$1");
    };
    /*
 * Helper functions for managing events -- not part of the public interface.
 * Props to Dean Edwards' addEvent library for many of the ideas.
 */
    jQuery.event = {
        add: function(elem, types, handler, data, selector) {
            "dk.brics.tajs.directives.unreachable";
            var elemData, eventHandle, events, t, tns, type, namespaces, handleObj, handleObjIn, handlers, special;
            // Don't attach events to noData or text/comment nodes (allow plain objects tho)
            if (elem.nodeType === 3 || elem.nodeType === 8 || !types || !handler || !(elemData = jQuery._data(elem))) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Caller can pass in an object of custom data in lieu of the handler
            if (handler.handler) {
                "dk.brics.tajs.directives.unreachable";
                handleObjIn = handler;
                handler = handleObjIn.handler;
                selector = handleObjIn.selector;
            }
            // Make sure that the handler has a unique ID, used to find/remove it later
            if (!handler.guid) {
                "dk.brics.tajs.directives.unreachable";
                handler.guid = jQuery.guid++;
            }
            // Init the element's event structure and main handler, if this is the first
            events = elemData.events;
            if (!events) {
                "dk.brics.tajs.directives.unreachable";
                elemData.events = events = {};
            }
            eventHandle = elemData.handle;
            if (!eventHandle) {
                "dk.brics.tajs.directives.unreachable";
                elemData.handle = eventHandle = function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    // Discard the second event of a jQuery.event.trigger() and
                    // when an event is called after a page has unloaded
                    return typeof jQuery !== "undefined" && (!e || jQuery.event.triggered !== e.type) ? jQuery.event.dispatch.apply(eventHandle.elem, arguments) : undefined;
                };
                // Add elem as a property of the handle fn to prevent a memory leak with IE non-native events
                eventHandle.elem = elem;
            }
            // Handle multiple events separated by a space
            // jQuery(...).bind("mouseover mouseout", fn);
            types = jQuery.trim(hoverHack(types)).split(" ");
            for (t = 0; t < types.length; t++) {
                "dk.brics.tajs.directives.unreachable";
                tns = rtypenamespace.exec(types[t]) || [];
                type = tns[1];
                namespaces = (tns[2] || "").split(".").sort();
                // If event changes its type, use the special event handlers for the changed type
                special = jQuery.event.special[type] || {};
                // If selector defined, determine special event api type, otherwise given type
                type = (selector ? special.delegateType : special.bindType) || type;
                // Update special based on newly reset type
                special = jQuery.event.special[type] || {};
                // handleObj is passed to all event handlers
                handleObj = jQuery.extend({
                    type: type,
                    origType: tns[1],
                    data: data,
                    handler: handler,
                    guid: handler.guid,
                    selector: selector,
                    namespace: namespaces.join(".")
                }, handleObjIn);
                // Init the event handler queue if we're the first
                handlers = events[type];
                if (!handlers) {
                    "dk.brics.tajs.directives.unreachable";
                    handlers = events[type] = [];
                    handlers.delegateCount = 0;
                    // Only use addEventListener/attachEvent if the special events handler returns false
                    if (!special.setup || special.setup.call(elem, data, namespaces, eventHandle) === false) {
                        "dk.brics.tajs.directives.unreachable";
                        // Bind the global event handler to the element
                        if (elem.addEventListener) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.addEventListener(type, eventHandle, false);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (elem.attachEvent) {
                                "dk.brics.tajs.directives.unreachable";
                                elem.attachEvent("on" + type, eventHandle);
                            }
                        }
                    }
                }
                if (special.add) {
                    "dk.brics.tajs.directives.unreachable";
                    special.add.call(elem, handleObj);
                    if (!handleObj.handler.guid) {
                        "dk.brics.tajs.directives.unreachable";
                        handleObj.handler.guid = handler.guid;
                    }
                }
                // Add to the element's handler list, delegates in front
                if (selector) {
                    "dk.brics.tajs.directives.unreachable";
                    handlers.splice(handlers.delegateCount++, 0, handleObj);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    handlers.push(handleObj);
                }
                // Keep track of which events have ever been used, for event optimization
                jQuery.event.global[type] = true;
            }
            // Nullify elem to prevent memory leaks in IE
            elem = null;
        },
        global: {},
        // Detach an event or set of events from an element
        remove: function(elem, types, handler, selector, mappedTypes) {
            var t, tns, type, origType, namespaces, origCount, j, events, special, eventType, handleObj, elemData = jQuery.hasData(elem) && jQuery._data(elem);
            if (!elemData || !(events = elemData.events)) {
                return;
            }
            "dk.brics.tajs.directives.unreachable";
            // Once for each type.namespace in types; type may be omitted
            types = jQuery.trim(hoverHack(types || "")).split(" ");
            for (t = 0; t < types.length; t++) {
                "dk.brics.tajs.directives.unreachable";
                tns = rtypenamespace.exec(types[t]) || [];
                type = origType = tns[1];
                namespaces = tns[2];
                // Unbind all events (on this namespace, if provided) for the element
                if (!type) {
                    "dk.brics.tajs.directives.unreachable";
                    for (type in events) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.remove(elem, type + types[t], handler, selector, true);
                    }
                    continue;
                }
                special = jQuery.event.special[type] || {};
                type = (selector ? special.delegateType : special.bindType) || type;
                eventType = events[type] || [];
                origCount = eventType.length;
                namespaces = namespaces ? new RegExp("(^|\\.)" + namespaces.split(".").sort().join("\\.(?:.*\\.|)") + "(\\.|$)") : null;
                // Remove matching events
                for (j = 0; j < eventType.length; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    handleObj = eventType[j];
                    if ((mappedTypes || origType === handleObj.origType) && (!handler || handler.guid === handleObj.guid) && (!namespaces || namespaces.test(handleObj.namespace)) && (!selector || selector === handleObj.selector || selector === "**" && handleObj.selector)) {
                        "dk.brics.tajs.directives.unreachable";
                        eventType.splice(j--, 1);
                        if (handleObj.selector) {
                            "dk.brics.tajs.directives.unreachable";
                            eventType.delegateCount--;
                        }
                        if (special.remove) {
                            "dk.brics.tajs.directives.unreachable";
                            special.remove.call(elem, handleObj);
                        }
                    }
                }
                // Remove generic event handler if we removed something and no more handlers exist
                // (avoids potential for endless recursion during removal of special event handlers)
                if (eventType.length === 0 && origCount !== eventType.length) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!special.teardown || special.teardown.call(elem, namespaces, elemData.handle) === false) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.removeEvent(elem, type, elemData.handle);
                    }
                    delete events[type];
                }
            }
            // Remove the expando if it's no longer used
            if (jQuery.isEmptyObject(events)) {
                "dk.brics.tajs.directives.unreachable";
                delete elemData.handle;
                // removeData also checks for emptiness and clears the expando if empty
                // so use it instead of delete
                jQuery.removeData(elem, "events", true);
            }
        },
        // Events that are safe to short-circuit if no handlers are attached.
        // Native DOM events should not be added, they may have inline handlers.
        customEvent: {
            getData: true,
            setData: true,
            changeData: true
        },
        trigger: function(event, data, elem, onlyHandlers) {
            // Don't do events on text and comment nodes
            if (elem && (elem.nodeType === 3 || elem.nodeType === 8)) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Event object or event type
            var cache, exclusive, i, cur, old, ontype, special, handle, eventPath, bubbleType, type = event.type || event, namespaces = [];
            // focus/blur morphs to focusin/out; ensure we're not firing them right now
            if (rfocusMorph.test(type + jQuery.event.triggered)) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            if (type.indexOf("!") >= 0) {
                "dk.brics.tajs.directives.unreachable";
                // Exclusive events trigger only for the exact event (no namespaces)
                type = type.slice(0, -1);
                exclusive = true;
            }
            if (type.indexOf(".") >= 0) {
                "dk.brics.tajs.directives.unreachable";
                // Namespaced trigger; create a regexp to match event type in handle()
                namespaces = type.split(".");
                type = namespaces.shift();
                namespaces.sort();
            }
            if ((!elem || jQuery.event.customEvent[type]) && !jQuery.event.global[type]) {
                "dk.brics.tajs.directives.unreachable";
                // No jQuery handlers for this event type, and it can't have inline handlers
                return;
            }
            // Caller can pass in an Event, Object, or just an event type string
            event = typeof event === "object" ? // jQuery.Event object
            event[jQuery.expando] ? event : // Object literal
            new jQuery.Event(type, event) : // Just the event type (string)
            new jQuery.Event(type);
            event.type = type;
            event.isTrigger = true;
            event.exclusive = exclusive;
            event.namespace = namespaces.join(".");
            event.namespace_re = event.namespace ? new RegExp("(^|\\.)" + namespaces.join("\\.(?:.*\\.|)") + "(\\.|$)") : null;
            ontype = type.indexOf(":") < 0 ? "on" + type : "";
            // Handle a global trigger
            if (!elem) {
                "dk.brics.tajs.directives.unreachable";
                // TODO: Stop taunting the data cache; remove global events and always attach to document
                cache = jQuery.cache;
                for (i in cache) {
                    "dk.brics.tajs.directives.unreachable";
                    if (cache[i].events && cache[i].events[type]) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.trigger(event, data, cache[i].handle.elem, true);
                    }
                }
                return;
            }
            // Clean up the event in case it is being reused
            event.result = undefined;
            if (!event.target) {
                event.target = elem;
            }
            // Clone any incoming data and prepend the event, creating the handler arg list
            data = data != null ? jQuery.makeArray(data) : [];
            data.unshift(event);
            // Allow special events to draw outside the lines
            special = jQuery.event.special[type] || {};
            if (special.trigger && special.trigger.apply(elem, data) === false) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Determine event propagation path in advance, per W3C events spec (#9951)
            // Bubble up to document, then to window; watch for a global ownerDocument var (#9724)
            eventPath = [ [ elem, special.bindType || type ] ];
            if (!onlyHandlers && !special.noBubble && !jQuery.isWindow(elem)) {
                bubbleType = special.delegateType || type;
                cur = rfocusMorph.test(bubbleType + type) ? elem : elem.parentNode;
                for (old = elem; cur; cur = cur.parentNode) {
                    "dk.brics.tajs.directives.unreachable";
                    eventPath.push([ cur, bubbleType ]);
                    old = cur;
                }
                // Only add window if we got to document (e.g., not plain obj or detached DOM)
                if (old === (elem.ownerDocument || document)) {
                    eventPath.push([ old.defaultView || old.parentWindow || window, bubbleType ]);
                }
            }
            // Fire handlers on the event path
            for (i = 0; i < eventPath.length && !event.isPropagationStopped(); i++) {
                cur = eventPath[i][0];
                event.type = eventPath[i][1];
                handle = (jQuery._data(cur, "events") || {})[event.type] && jQuery._data(cur, "handle");
                if (handle) {
                    "dk.brics.tajs.directives.unreachable";
                    handle.apply(cur, data);
                }
                // Note that this is a bare JS function and not a jQuery handler
                handle = ontype && cur[ontype];
                if (handle && jQuery.acceptData(cur) && handle.apply(cur, data) === false) {
                    "dk.brics.tajs.directives.unreachable";
                    event.preventDefault();
                }
            }
            event.type = type;
            // If nobody prevented the default action, do it now
            if (!onlyHandlers && !event.isDefaultPrevented()) {
                if ((!special._default || special._default.apply(elem.ownerDocument, data) === false) && !(type === "click" && jQuery.nodeName(elem, "a")) && jQuery.acceptData(elem)) {
                    // Call a native DOM method on the target with the same name name as the event.
                    // Can't use an .isFunction() check here because IE6/7 fails that test.
                    // Don't do default actions on window, that's where global variables be (#6170)
                    // IE<9 dies on focus/blur to hidden element (#1486)
                    if (ontype && elem[type] && (type !== "focus" && type !== "blur" || event.target.offsetWidth !== 0) && !jQuery.isWindow(elem)) {
                        "dk.brics.tajs.directives.unreachable";
                        // Don't re-trigger an onFOO event when we call its FOO() method
                        old = elem[ontype];
                        if (old) {
                            "dk.brics.tajs.directives.unreachable";
                            elem[ontype] = null;
                        }
                        // Prevent re-triggering of the same event, since we already bubbled it above
                        jQuery.event.triggered = type;
                        elem[type]();
                        jQuery.event.triggered = undefined;
                        if (old) {
                            "dk.brics.tajs.directives.unreachable";
                            elem[ontype] = old;
                        }
                    }
                }
            }
            return event.result;
        },
        dispatch: function(event) {
            "dk.brics.tajs.directives.unreachable";
            // Make a writable jQuery.Event from the native event object
            event = jQuery.event.fix(event || window.event);
            var i, j, cur, jqcur, ret, selMatch, matched, matches, handleObj, sel, related, handlers = (jQuery._data(this, "events") || {})[event.type] || [], delegateCount = handlers.delegateCount, args = [].slice.call(arguments), run_all = !event.exclusive && !event.namespace, special = jQuery.event.special[event.type] || {}, handlerQueue = [];
            // Use the fix-ed jQuery.Event rather than the (read-only) native event
            args[0] = event;
            event.delegateTarget = this;
            // Call the preDispatch hook for the mapped type, and let it bail if desired
            if (special.preDispatch && special.preDispatch.call(this, event) === false) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Determine handlers that should run if there are delegated events
            // Avoid non-left-click bubbling in Firefox (#3861)
            if (delegateCount && !(event.button && event.type === "click")) {
                "dk.brics.tajs.directives.unreachable";
                // Pregenerate a single jQuery object for reuse with .is()
                jqcur = jQuery(this);
                jqcur.context = this;
                for (cur = event.target; cur != this; cur = cur.parentNode || this) {
                    "dk.brics.tajs.directives.unreachable";
                    // Don't process clicks (ONLY) on disabled elements (#6911, #8165, #xxxx)
                    if (cur.disabled !== true || event.type !== "click") {
                        "dk.brics.tajs.directives.unreachable";
                        selMatch = {};
                        matches = [];
                        jqcur[0] = cur;
                        for (i = 0; i < delegateCount; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            handleObj = handlers[i];
                            sel = handleObj.selector;
                            if (selMatch[sel] === undefined) {
                                "dk.brics.tajs.directives.unreachable";
                                selMatch[sel] = jqcur.is(sel);
                            }
                            if (selMatch[sel]) {
                                "dk.brics.tajs.directives.unreachable";
                                matches.push(handleObj);
                            }
                        }
                        if (matches.length) {
                            "dk.brics.tajs.directives.unreachable";
                            handlerQueue.push({
                                elem: cur,
                                matches: matches
                            });
                        }
                    }
                }
            }
            // Add the remaining (directly-bound) handlers
            if (handlers.length > delegateCount) {
                "dk.brics.tajs.directives.unreachable";
                handlerQueue.push({
                    elem: this,
                    matches: handlers.slice(delegateCount)
                });
            }
            // Run delegates first; they may want to stop propagation beneath us
            for (i = 0; i < handlerQueue.length && !event.isPropagationStopped(); i++) {
                "dk.brics.tajs.directives.unreachable";
                matched = handlerQueue[i];
                event.currentTarget = matched.elem;
                for (j = 0; j < matched.matches.length && !event.isImmediatePropagationStopped(); j++) {
                    "dk.brics.tajs.directives.unreachable";
                    handleObj = matched.matches[j];
                    // Triggered event must either 1) be non-exclusive and have no namespace, or
                    // 2) have namespace(s) a subset or equal to those in the bound event (both can have no namespace).
                    if (run_all || !event.namespace && !handleObj.namespace || event.namespace_re && event.namespace_re.test(handleObj.namespace)) {
                        "dk.brics.tajs.directives.unreachable";
                        event.data = handleObj.data;
                        event.handleObj = handleObj;
                        ret = ((jQuery.event.special[handleObj.origType] || {}).handle || handleObj.handler).apply(matched.elem, args);
                        if (ret !== undefined) {
                            "dk.brics.tajs.directives.unreachable";
                            event.result = ret;
                            if (ret === false) {
                                "dk.brics.tajs.directives.unreachable";
                                event.preventDefault();
                                event.stopPropagation();
                            }
                        }
                    }
                }
            }
            // Call the postDispatch hook for the mapped type
            if (special.postDispatch) {
                "dk.brics.tajs.directives.unreachable";
                special.postDispatch.call(this, event);
            }
            return event.result;
        },
        // Includes some event props shared by KeyEvent and MouseEvent
        // *** attrChange attrName relatedNode srcElement  are not normalized, non-W3C, deprecated, will be removed in 1.8 ***
        props: "attrChange attrName relatedNode srcElement altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
        fixHooks: {},
        keyHooks: {
            props: "char charCode key keyCode".split(" "),
            filter: function(event, original) {
                "dk.brics.tajs.directives.unreachable";
                // Add which for key events
                if (event.which == null) {
                    "dk.brics.tajs.directives.unreachable";
                    event.which = original.charCode != null ? original.charCode : original.keyCode;
                }
                return event;
            }
        },
        mouseHooks: {
            props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
            filter: function(event, original) {
                "dk.brics.tajs.directives.unreachable";
                var eventDoc, doc, body, button = original.button, fromElement = original.fromElement;
                // Calculate pageX/Y if missing and clientX/Y available
                if (event.pageX == null && original.clientX != null) {
                    "dk.brics.tajs.directives.unreachable";
                    eventDoc = event.target.ownerDocument || document;
                    doc = eventDoc.documentElement;
                    body = eventDoc.body;
                    event.pageX = original.clientX + (doc && doc.scrollLeft || body && body.scrollLeft || 0) - (doc && doc.clientLeft || body && body.clientLeft || 0);
                    event.pageY = original.clientY + (doc && doc.scrollTop || body && body.scrollTop || 0) - (doc && doc.clientTop || body && body.clientTop || 0);
                }
                // Add relatedTarget, if necessary
                if (!event.relatedTarget && fromElement) {
                    "dk.brics.tajs.directives.unreachable";
                    event.relatedTarget = fromElement === event.target ? original.toElement : fromElement;
                }
                // Add which for click: 1 === left; 2 === middle; 3 === right
                // Note: button is not normalized, so don't use it
                if (!event.which && button !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    event.which = button & 1 ? 1 : button & 2 ? 3 : button & 4 ? 2 : 0;
                }
                return event;
            }
        },
        fix: function(event) {
            "dk.brics.tajs.directives.unreachable";
            if (event[jQuery.expando]) {
                "dk.brics.tajs.directives.unreachable";
                return event;
            }
            // Create a writable copy of the event object and normalize some properties
            var i, prop, originalEvent = event, fixHook = jQuery.event.fixHooks[event.type] || {}, copy = fixHook.props ? this.props.concat(fixHook.props) : this.props;
            event = jQuery.Event(originalEvent);
            for (i = copy.length; i; ) {
                "dk.brics.tajs.directives.unreachable";
                prop = copy[--i];
                event[prop] = originalEvent[prop];
            }
            // Fix target property, if necessary (#1925, IE 6/7/8 & Safari2)
            if (!event.target) {
                "dk.brics.tajs.directives.unreachable";
                event.target = originalEvent.srcElement || document;
            }
            // Target should not be a text node (#504, Safari)
            if (event.target.nodeType === 3) {
                "dk.brics.tajs.directives.unreachable";
                event.target = event.target.parentNode;
            }
            // For mouse/key events, metaKey==false if it's undefined (#3368, #11328; IE6/7/8)
            event.metaKey = !!event.metaKey;
            return fixHook.filter ? fixHook.filter(event, originalEvent) : event;
        },
        special: {
            ready: {
                // Make sure the ready event is setup
                setup: jQuery.bindReady
            },
            load: {
                // Prevent triggered image.load events from bubbling to window.load
                noBubble: true
            },
            focus: {
                delegateType: "focusin"
            },
            blur: {
                delegateType: "focusout"
            },
            beforeunload: {
                setup: function(data, namespaces, eventHandle) {
                    "dk.brics.tajs.directives.unreachable";
                    // We only want to do this special case on windows
                    if (jQuery.isWindow(this)) {
                        "dk.brics.tajs.directives.unreachable";
                        this.onbeforeunload = eventHandle;
                    }
                },
                teardown: function(namespaces, eventHandle) {
                    "dk.brics.tajs.directives.unreachable";
                    if (this.onbeforeunload === eventHandle) {
                        "dk.brics.tajs.directives.unreachable";
                        this.onbeforeunload = null;
                    }
                }
            }
        },
        simulate: function(type, elem, event, bubble) {
            "dk.brics.tajs.directives.unreachable";
            // Piggyback on a donor event to simulate a different one.
            // Fake originalEvent to avoid donor's stopPropagation, but if the
            // simulated event prevents default then we do the same on the donor.
            var e = jQuery.extend(new jQuery.Event(), event, {
                type: type,
                isSimulated: true,
                originalEvent: {}
            });
            if (bubble) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger(e, null, elem);
            } else {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.dispatch.call(elem, e);
            }
            if (e.isDefaultPrevented()) {
                "dk.brics.tajs.directives.unreachable";
                event.preventDefault();
            }
        }
    };
    // Some plugins are using, but it's undocumented/deprecated and will be removed.
    // The 1.7 special event interface should provide all the hooks needed now.
    jQuery.event.handle = jQuery.event.dispatch;
    jQuery.removeEvent = document.removeEventListener ? function(elem, type, handle) {
        "dk.brics.tajs.directives.unreachable";
        if (elem.removeEventListener) {
            "dk.brics.tajs.directives.unreachable";
            elem.removeEventListener(type, handle, false);
        }
    } : function(elem, type, handle) {
        "dk.brics.tajs.directives.unreachable";
        var name = "on" + type;
        if (elem.detachEvent) {
            "dk.brics.tajs.directives.unreachable";
            // #8545, #7054, preventing memory leaks for custom events in IE6-8 –
            // detachEvent needed property on element, by name of that event, to properly expose it to GC
            if (typeof elem[name] === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                elem[name] = null;
            }
            elem.detachEvent(name, handle);
        }
    };
    jQuery.Event = function(src, props) {
        // Allow instantiation without the 'new' keyword
        if (!(this instanceof jQuery.Event)) {
            "dk.brics.tajs.directives.unreachable";
            return new jQuery.Event(src, props);
        }
        // Event object
        if (src && src.type) {
            "dk.brics.tajs.directives.unreachable";
            this.originalEvent = src;
            this.type = src.type;
            // Events bubbling up the document may have been marked as prevented
            // by a handler lower down the tree; reflect the correct value.
            this.isDefaultPrevented = src.defaultPrevented || src.returnValue === false || src.getPreventDefault && src.getPreventDefault() ? returnTrue : returnFalse;
        } else {
            this.type = src;
        }
        // Put explicitly provided properties onto the event object
        if (props) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.extend(this, props);
        }
        // Create a timestamp if incoming event doesn't have one
        this.timeStamp = src && src.timeStamp || jQuery.now();
        // Mark it as fixed
        this[jQuery.expando] = true;
    };
    function returnFalse() {
        return false;
    }
    function returnTrue() {
        "dk.brics.tajs.directives.unreachable";
        return true;
    }
    // jQuery.Event is based on DOM3 Events as specified by the ECMAScript Language Binding
    // http://www.w3.org/TR/2003/WD-DOM-Level-3-Events-20030331/ecma-script-binding.html
    jQuery.Event.prototype = {
        preventDefault: function() {
            "dk.brics.tajs.directives.unreachable";
            this.isDefaultPrevented = returnTrue;
            var e = this.originalEvent;
            if (!e) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // if preventDefault exists run it on the original event
            if (e.preventDefault) {
                "dk.brics.tajs.directives.unreachable";
                e.preventDefault();
            } else {
                "dk.brics.tajs.directives.unreachable";
                e.returnValue = false;
            }
        },
        stopPropagation: function() {
            "dk.brics.tajs.directives.unreachable";
            this.isPropagationStopped = returnTrue;
            var e = this.originalEvent;
            if (!e) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // if stopPropagation exists run it on the original event
            if (e.stopPropagation) {
                "dk.brics.tajs.directives.unreachable";
                e.stopPropagation();
            }
            // otherwise set the cancelBubble property of the original event to true (IE)
            e.cancelBubble = true;
        },
        stopImmediatePropagation: function() {
            "dk.brics.tajs.directives.unreachable";
            this.isImmediatePropagationStopped = returnTrue;
            this.stopPropagation();
        },
        isDefaultPrevented: returnFalse,
        isPropagationStopped: returnFalse,
        isImmediatePropagationStopped: returnFalse
    };
    // Create mouseenter/leave events using mouseover/out and event-time checks
    jQuery.each({
        mouseenter: "mouseover",
        mouseleave: "mouseout"
    }, function(orig, fix) {
        jQuery.event.special[orig] = {
            delegateType: fix,
            bindType: fix,
            handle: function(event) {
                "dk.brics.tajs.directives.unreachable";
                var ret, target = this, related = event.relatedTarget, handleObj = event.handleObj, selector = handleObj.selector;
                // For mousenter/leave call the handler if related is outside the target.
                // NB: No relatedTarget if the mouse left/entered the browser window
                if (!related || related !== target && !jQuery.contains(target, related)) {
                    "dk.brics.tajs.directives.unreachable";
                    event.type = handleObj.origType;
                    ret = handleObj.handler.apply(this, arguments);
                    event.type = fix;
                }
                return ret;
            }
        };
    });
    // IE submit delegation
    if (!jQuery.support.submitBubbles) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.event.special.submit = {
            setup: function() {
                "dk.brics.tajs.directives.unreachable";
                // Only need this for delegated form submit events
                if (jQuery.nodeName(this, "form")) {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
                // Lazy-add a submit handler when a descendant form may potentially be submitted
                jQuery.event.add(this, "click._submit keypress._submit", function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    // Node name check avoids a VML-related crash in IE (#9807)
                    var elem = e.target, form = jQuery.nodeName(elem, "input") || jQuery.nodeName(elem, "button") ? elem.form : undefined;
                    if (form && !jQuery._data(form, "_submit_attached")) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(form, "submit._submit", function(event) {
                            "dk.brics.tajs.directives.unreachable";
                            event._submit_bubble = true;
                        });
                        jQuery._data(form, "_submit_attached", true);
                    }
                });
            },
            postDispatch: function(event) {
                "dk.brics.tajs.directives.unreachable";
                // If form was submitted by the user, bubble the event up the tree
                if (event._submit_bubble) {
                    "dk.brics.tajs.directives.unreachable";
                    delete event._submit_bubble;
                    if (this.parentNode && !event.isTrigger) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.simulate("submit", this.parentNode, event, true);
                    }
                }
            },
            teardown: function() {
                "dk.brics.tajs.directives.unreachable";
                // Only need this for delegated form submit events
                if (jQuery.nodeName(this, "form")) {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
                // Remove delegated handlers; cleanData eventually reaps submit handlers attached above
                jQuery.event.remove(this, "._submit");
            }
        };
    }
    // IE change delegation and checkbox/radio fix
    if (!jQuery.support.changeBubbles) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.event.special.change = {
            setup: function() {
                "dk.brics.tajs.directives.unreachable";
                if (rformElems.test(this.nodeName)) {
                    "dk.brics.tajs.directives.unreachable";
                    // IE doesn't fire change on a check/radio until blur; trigger it on click
                    // after a propertychange. Eat the blur-change in special.change.handle.
                    // This still fires onchange a second time for check/radio after blur.
                    if (this.type === "checkbox" || this.type === "radio") {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(this, "propertychange._change", function(event) {
                            "dk.brics.tajs.directives.unreachable";
                            if (event.originalEvent.propertyName === "checked") {
                                "dk.brics.tajs.directives.unreachable";
                                this._just_changed = true;
                            }
                        });
                        jQuery.event.add(this, "click._change", function(event) {
                            "dk.brics.tajs.directives.unreachable";
                            if (this._just_changed && !event.isTrigger) {
                                "dk.brics.tajs.directives.unreachable";
                                this._just_changed = false;
                            }
                            // Allow triggered, simulated change events (#11500)
                            jQuery.event.simulate("change", this, event, true);
                        });
                    }
                    return false;
                }
                // Delegated event; lazy-add a change handler on descendant inputs
                jQuery.event.add(this, "beforeactivate._change", function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = e.target;
                    if (rformElems.test(elem.nodeName) && !jQuery._data(elem, "_change_attached")) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(elem, "change._change", function(event) {
                            "dk.brics.tajs.directives.unreachable";
                            if (this.parentNode && !event.isSimulated && !event.isTrigger) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.event.simulate("change", this.parentNode, event, true);
                            }
                        });
                        jQuery._data(elem, "_change_attached", true);
                    }
                });
            },
            handle: function(event) {
                "dk.brics.tajs.directives.unreachable";
                var elem = event.target;
                // Swallow native change events from checkbox/radio, we already triggered them above
                if (this !== elem || event.isSimulated || event.isTrigger || elem.type !== "radio" && elem.type !== "checkbox") {
                    "dk.brics.tajs.directives.unreachable";
                    return event.handleObj.handler.apply(this, arguments);
                }
            },
            teardown: function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, "._change");
                return rformElems.test(this.nodeName);
            }
        };
    }
    // Create "bubbling" focus and blur events
    if (!jQuery.support.focusinBubbles) {
        jQuery.each({
            focus: "focusin",
            blur: "focusout"
        }, function(orig, fix) {
            // Attach a single capturing handler while someone wants focusin/focusout
            var attaches = 0, handler = function(event) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.simulate(fix, event.target, jQuery.event.fix(event), true);
            };
            jQuery.event.special[fix] = {
                setup: function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (attaches++ === 0) {
                        "dk.brics.tajs.directives.unreachable";
                        document.addEventListener(orig, handler, true);
                    }
                },
                teardown: function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (--attaches === 0) {
                        "dk.brics.tajs.directives.unreachable";
                        document.removeEventListener(orig, handler, true);
                    }
                }
            };
        });
    }
    jQuery.fn.extend({
        on: function(types, selector, data, fn, /*INTERNAL*/ one) {
            "dk.brics.tajs.directives.unreachable";
            var origFn, type;
            // Types can be a map of types/handlers
            if (typeof types === "object") {
                "dk.brics.tajs.directives.unreachable";
                // ( types-Object, selector, data )
                if (typeof selector !== "string") {
                    "dk.brics.tajs.directives.unreachable";
                    // && selector != null
                    // ( types-Object, data )
                    data = data || selector;
                    selector = undefined;
                }
                for (type in types) {
                    "dk.brics.tajs.directives.unreachable";
                    this.on(type, selector, data, types[type], one);
                }
                return this;
            }
            if (data == null && fn == null) {
                "dk.brics.tajs.directives.unreachable";
                // ( types, fn )
                fn = selector;
                data = selector = undefined;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (fn == null) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof selector === "string") {
                        "dk.brics.tajs.directives.unreachable";
                        // ( types, selector, fn )
                        fn = data;
                        data = undefined;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        // ( types, data, fn )
                        fn = data;
                        data = selector;
                        selector = undefined;
                    }
                }
            }
            if (fn === false) {
                "dk.brics.tajs.directives.unreachable";
                fn = returnFalse;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (!fn) {
                    "dk.brics.tajs.directives.unreachable";
                    return this;
                }
            }
            if (one === 1) {
                "dk.brics.tajs.directives.unreachable";
                origFn = fn;
                fn = function(event) {
                    "dk.brics.tajs.directives.unreachable";
                    // Can use an empty set, since event contains the info
                    jQuery().off(event);
                    return origFn.apply(this, arguments);
                };
                // Use same guid so caller can remove using origFn
                fn.guid = origFn.guid || (origFn.guid = jQuery.guid++);
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.add(this, types, fn, data, selector);
            });
        },
        one: function(types, selector, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.on(types, selector, data, fn, 1);
        },
        off: function(types, selector, fn) {
            var handleObj, type;
            if (types && types.preventDefault && types.handleObj) {
                "dk.brics.tajs.directives.unreachable";
                // ( event )  dispatched jQuery.Event
                handleObj = types.handleObj;
                jQuery(types.delegateTarget).off(handleObj.namespace ? handleObj.origType + "." + handleObj.namespace : handleObj.origType, handleObj.selector, handleObj.handler);
                return this;
            }
            if (typeof types === "object") {
                "dk.brics.tajs.directives.unreachable";
                // ( types-object [, selector] )
                for (type in types) {
                    "dk.brics.tajs.directives.unreachable";
                    this.off(type, selector, types[type]);
                }
                return this;
            }
            if (selector === false || typeof selector === "function") {
                "dk.brics.tajs.directives.unreachable";
                // ( types [, fn] )
                fn = selector;
                selector = undefined;
            }
            if (fn === false) {
                "dk.brics.tajs.directives.unreachable";
                fn = returnFalse;
            }
            return this.each(function() {
                jQuery.event.remove(this, types, fn, selector);
            });
        },
        bind: function(types, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.on(types, null, data, fn);
        },
        unbind: function(types, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.off(types, null, fn);
        },
        live: function(types, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            jQuery(this.context).on(types, this.selector, data, fn);
            return this;
        },
        die: function(types, fn) {
            "dk.brics.tajs.directives.unreachable";
            jQuery(this.context).off(types, this.selector || "**", fn);
            return this;
        },
        delegate: function(selector, types, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.on(types, selector, data, fn);
        },
        undelegate: function(selector, types, fn) {
            "dk.brics.tajs.directives.unreachable";
            // ( namespace ) or ( selector, types [, fn] )
            return arguments.length == 1 ? this.off(selector, "**") : this.off(types, selector || "**", fn);
        },
        trigger: function(type, data) {
            return this.each(function() {
                jQuery.event.trigger(type, data, this);
            });
        },
        triggerHandler: function(type, data) {
            "dk.brics.tajs.directives.unreachable";
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.event.trigger(type, data, this[0], true);
            }
        },
        toggle: function(fn) {
            "dk.brics.tajs.directives.unreachable";
            // Save reference to arguments for access in closure
            var args = arguments, guid = fn.guid || jQuery.guid++, i = 0, toggler = function(event) {
                "dk.brics.tajs.directives.unreachable";
                // Figure out which function to execute
                var lastToggle = (jQuery._data(this, "lastToggle" + fn.guid) || 0) % i;
                jQuery._data(this, "lastToggle" + fn.guid, lastToggle + 1);
                // Make sure that clicks stop
                event.preventDefault();
                // and execute the function
                return args[lastToggle].apply(this, arguments) || false;
            };
            // link all the functions, so any of them can unbind this click handler
            toggler.guid = guid;
            while (i < args.length) {
                "dk.brics.tajs.directives.unreachable";
                args[i++].guid = guid;
            }
            return this.click(toggler);
        },
        hover: function(fnOver, fnOut) {
            "dk.brics.tajs.directives.unreachable";
            return this.mouseenter(fnOver).mouseleave(fnOut || fnOver);
        }
    });
    jQuery.each(("blur focus focusin focusout load resize scroll unload click dblclick " + "mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " + "change select submit keydown keypress keyup error contextmenu").split(" "), function(i, name) {
        // Handle event binding
        jQuery.fn[name] = function(data, fn) {
            "dk.brics.tajs.directives.unreachable";
            if (fn == null) {
                "dk.brics.tajs.directives.unreachable";
                fn = data;
                data = null;
            }
            return arguments.length > 0 ? this.on(name, null, data, fn) : this.trigger(name);
        };
        if (rkeyEvent.test(name)) {
            jQuery.event.fixHooks[name] = jQuery.event.keyHooks;
        }
        if (rmouseEvent.test(name)) {
            jQuery.event.fixHooks[name] = jQuery.event.mouseHooks;
        }
    });
    /*!
 * Sizzle CSS Selector Engine
 *  Copyright 2012 jQuery Foundation and other contributors
 *  Released under the MIT license
 *  http://sizzlejs.com/
 */
    (function(window, undefined) {
        var cachedruns, dirruns, sortOrder, siblingCheck, assertGetIdNotName, document = window.document, docElem = document.documentElement, strundefined = "undefined", hasDuplicate = false, baseHasDuplicate = true, done = 0, slice = [].slice, push = [].push, expando = ("sizcache" + "TAJS_UUID"), // Regex
        // Whitespace characters http://www.w3.org/TR/css3-selectors/#whitespace
        whitespace = "[\\x20\\t\\r\\n\\f]", // http://www.w3.org/TR/css3-syntax/#characters
        characterEncoding = "(?:\\\\.|[-\\w]|[^\\x00-\\xa0])+", // Loosely modeled on CSS identifier characters
        // An unquoted value should be a CSS identifier (http://www.w3.org/TR/css3-selectors/#attribute-selectors)
        // Proper syntax: http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier
        identifier = characterEncoding.replace("w", "w#"), // Acceptable operators http://www.w3.org/TR/selectors/#attribute-selectors
        operators = "([*^$|!~]?=)", attributes = "\\[" + whitespace + "*(" + characterEncoding + ")" + whitespace + "*(?:" + operators + whitespace + "*(?:(['\"])((?:\\\\.|[^\\\\])*?)\\3|(" + identifier + ")|)|)" + whitespace + "*\\]", pseudos = ":(" + characterEncoding + ")(?:\\((?:(['\"])((?:\\\\.|[^\\\\])*?)\\2|((?:[^,]|\\\\,|(?:,(?=[^\\[]*\\]))|(?:,(?=[^\\(]*\\))))*))\\)|)", pos = ":(nth|eq|gt|lt|first|last|even|odd)(?:\\((\\d*)\\)|)(?=[^-]|$)", combinators = whitespace + "*([\\x20\\t\\r\\n\\f>+~])" + whitespace + "*", groups = "(?=[^\\x20\\t\\r\\n\\f])(?:\\\\.|" + attributes + "|" + pseudos.replace(2, 7) + "|[^\\\\(),])+", // Leading and non-escaped trailing whitespace, capturing some non-whitespace characters preceding the latter
        rtrim = new RegExp("^" + whitespace + "+|((?:^|[^\\\\])(?:\\\\.)*)" + whitespace + "+$", "g"), rcombinators = new RegExp("^" + combinators), // All simple (non-comma) selectors, excluding insignifant trailing whitespace
        rgroups = new RegExp(groups + "?(?=" + whitespace + "*,|$)", "g"), // A selector, or everything after leading whitespace
        // Optionally followed in either case by a ")" for terminating sub-selectors
        rselector = new RegExp("^(?:(?!,)(?:(?:^|,)" + whitespace + "*" + groups + ")*?|" + whitespace + "*(.*?))(\\)|$)"), // All combinators and selector components (attribute test, tag, pseudo, etc.), the latter appearing together when consecutive
        rtokens = new RegExp(groups.slice(19, -6) + "\\x20\\t\\r\\n\\f>+~])+|" + combinators, "g"), // Easily-parseable/retrievable ID or TAG or CLASS selectors
        rquickExpr = /^(?:#([\w\-]+)|(\w+)|\.([\w\-]+))$/, rsibling = /[\x20\t\r\n\f]*[+~]/, rendsWithNot = /:not\($/, rheader = /h\d/i, rinputs = /input|select|textarea|button/i, rbackslash = /\\(?!\\)/g, matchExpr = {
            ID: new RegExp("^#(" + characterEncoding + ")"),
            CLASS: new RegExp("^\\.(" + characterEncoding + ")"),
            NAME: new RegExp("^\\[name=['\"]?(" + characterEncoding + ")['\"]?\\]"),
            TAG: new RegExp("^(" + characterEncoding.replace("[-", "[-\\*") + ")"),
            ATTR: new RegExp("^" + attributes),
            PSEUDO: new RegExp("^" + pseudos),
            CHILD: new RegExp("^:(only|nth|last|first)-child(?:\\(" + whitespace + "*(even|odd|(([+-]|)(\\d*)n|)" + whitespace + "*(?:([+-]|)" + whitespace + "*(\\d+)|))" + whitespace + "*\\)|)", "i"),
            POS: new RegExp(pos, "ig"),
            // For use in libraries implementing .is()
            needsContext: new RegExp("^" + whitespace + "*[>+~]|" + pos, "i")
        }, classCache = {}, cachedClasses = [], compilerCache = {}, cachedSelectors = [], // Mark a function for use in filtering
        markFunction = function(fn) {
            fn.sizzleFilter = true;
            return fn;
        }, // Returns a function to use in pseudos for input types
        createInputFunction = function(type) {
            return function(elem) {
                "dk.brics.tajs.directives.unreachable";
                // Check the input's nodeName and type
                return elem.nodeName.toLowerCase() === "input" && elem.type === type;
            };
        }, // Returns a function to use in pseudos for buttons
        createButtonFunction = function(type) {
            return function(elem) {
                "dk.brics.tajs.directives.unreachable";
                var name = elem.nodeName.toLowerCase();
                return (name === "input" || name === "button") && elem.type === type;
            };
        }, // Used for testing something on an element
        assert = function(fn) {
            var pass = false, div = document.createElement("div");
            try {
                pass = fn(div);
            } catch (e) {}
            // release memory in IE
            div = null;
            return pass;
        }, // Check if attributes should be retrieved by attribute nodes
        assertAttributes = assert(function(div) {
            div.innerHTML = "<select></select>";
            var type = typeof div.lastChild.getAttribute("multiple");
            // IE8 returns a string for some attributes even when not present
            return type !== "boolean" && type !== "string";
        }), // Check if getElementById returns elements by name
        // Check if getElementsByName privileges form controls or returns elements by ID
        assertUsableName = assert(function(div) {
            // Inject content
            div.id = expando + 0;
            div.innerHTML = "<a name='" + expando + "'></a><div name='" + expando + "'></div>";
            docElem.insertBefore(div, docElem.firstChild);
            // Test
            var pass = document.getElementsByName && // buggy browsers will return fewer than the correct 2
            document.getElementsByName(expando).length === // buggy browsers will return more than the correct 0
            2 + document.getElementsByName(expando + 0).length;
            assertGetIdNotName = !document.getElementById(expando);
            // Cleanup
            docElem.removeChild(div);
            return pass;
        }), // Check if the browser returns only elements
        // when doing getElementsByTagName("*")
        assertTagNameNoComments = assert(function(div) {
            div.appendChild(document.createComment(""));
            return div.getElementsByTagName("*").length === 0;
        }), // Check if getAttribute returns normalized href attributes
        assertHrefNotNormalized = assert(function(div) {
            div.innerHTML = "<a href='#'></a>";
            return div.firstChild && typeof div.firstChild.getAttribute !== strundefined && div.firstChild.getAttribute("href") === "#";
        }), // Check if getElementsByClassName can be trusted
        assertUsableClassName = assert(function(div) {
            // Opera can't find a second classname (in 9.6)
            div.innerHTML = "<div class='hidden e'></div><div class='hidden'></div>";
            if (!div.getElementsByClassName || div.getElementsByClassName("e").length === 0) {
                "dk.brics.tajs.directives.unreachable";
                return false;
            }
            // Safari caches class attributes, doesn't catch changes (in 3.2)
            div.lastChild.className = "e";
            return div.getElementsByClassName("e").length !== 1;
        });
        var Sizzle = function(selector, context, results, seed) {
            "dk.brics.tajs.directives.unreachable";
            results = results || [];
            context = context || document;
            var match, elem, xml, m, nodeType = context.nodeType;
            if (nodeType !== 1 && nodeType !== 9) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            if (!selector || typeof selector !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return results;
            }
            xml = isXML(context);
            if (!xml && !seed) {
                "dk.brics.tajs.directives.unreachable";
                if (match = rquickExpr.exec(selector)) {
                    "dk.brics.tajs.directives.unreachable";
                    // Speed-up: Sizzle("#ID")
                    if (m = match[1]) {
                        "dk.brics.tajs.directives.unreachable";
                        if (nodeType === 9) {
                            "dk.brics.tajs.directives.unreachable";
                            elem = context.getElementById(m);
                            // Check parentNode to catch when Blackberry 4.6 returns
                            // nodes that are no longer in the document #6963
                            if (elem && elem.parentNode) {
                                "dk.brics.tajs.directives.unreachable";
                                // Handle the case where IE, Opera, and Webkit return items
                                // by name instead of ID
                                if (elem.id === m) {
                                    "dk.brics.tajs.directives.unreachable";
                                    results.push(elem);
                                    return results;
                                }
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                return results;
                            }
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // Context is not a document
                            if (context.ownerDocument && (elem = context.ownerDocument.getElementById(m)) && contains(context, elem) && elem.id === m) {
                                "dk.brics.tajs.directives.unreachable";
                                results.push(elem);
                                return results;
                            }
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (match[2]) {
                            "dk.brics.tajs.directives.unreachable";
                            push.apply(results, slice.call(context.getElementsByTagName(selector), 0));
                            return results;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if ((m = match[3]) && assertUsableClassName && context.getElementsByClassName) {
                                "dk.brics.tajs.directives.unreachable";
                                push.apply(results, slice.call(context.getElementsByClassName(m), 0));
                                return results;
                            }
                        }
                    }
                }
            }
            // All others
            return select(selector, context, results, seed, xml);
        };
        var Expr = Sizzle.selectors = {
            // Can be adjusted by the user
            cacheLength: 50,
            match: matchExpr,
            order: [ "ID", "TAG" ],
            attrHandle: {},
            createPseudo: markFunction,
            find: {
                ID: assertGetIdNotName ? function(id, context, xml) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof context.getElementById !== strundefined && !xml) {
                        "dk.brics.tajs.directives.unreachable";
                        var m = context.getElementById(id);
                        // Check parentNode to catch when Blackberry 4.6 returns
                        // nodes that are no longer in the document #6963
                        return m && m.parentNode ? [ m ] : [];
                    }
                } : function(id, context, xml) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof context.getElementById !== strundefined && !xml) {
                        "dk.brics.tajs.directives.unreachable";
                        var m = context.getElementById(id);
                        return m ? m.id === id || typeof m.getAttributeNode !== strundefined && m.getAttributeNode("id").value === id ? [ m ] : undefined : [];
                    }
                },
                TAG: assertTagNameNoComments ? function(tag, context) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof context.getElementsByTagName !== strundefined) {
                        "dk.brics.tajs.directives.unreachable";
                        return context.getElementsByTagName(tag);
                    }
                } : function(tag, context) {
                    "dk.brics.tajs.directives.unreachable";
                    var results = context.getElementsByTagName(tag);
                    // Filter out possible comments
                    if (tag === "*") {
                        "dk.brics.tajs.directives.unreachable";
                        var elem, tmp = [], i = 0;
                        for (;elem = results[i]; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (elem.nodeType === 1) {
                                "dk.brics.tajs.directives.unreachable";
                                tmp.push(elem);
                            }
                        }
                        return tmp;
                    }
                    return results;
                }
            },
            relative: {
                ">": {
                    dir: "parentNode",
                    first: true
                },
                " ": {
                    dir: "parentNode"
                },
                "+": {
                    dir: "previousSibling",
                    first: true
                },
                "~": {
                    dir: "previousSibling"
                }
            },
            preFilter: {
                ATTR: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    match[1] = match[1].replace(rbackslash, "");
                    // Move the given value to match[3] whether quoted or unquoted
                    match[3] = (match[4] || match[5] || "").replace(rbackslash, "");
                    if (match[2] === "~=") {
                        "dk.brics.tajs.directives.unreachable";
                        match[3] = " " + match[3] + " ";
                    }
                    return match.slice(0, 4);
                },
                CHILD: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    /* matches from matchExpr.CHILD
				1 type (only|nth|...)
				2 argument (even|odd|\d*|\d*n([+-]\d+)?|...)
				3 xn-component of xn+y argument ([+-]?\d*n|)
				4 sign of xn-component
				5 x of xn-component
				6 sign of y-component
				7 y of y-component
			*/
                    match[1] = match[1].toLowerCase();
                    if (match[1] === "nth") {
                        "dk.brics.tajs.directives.unreachable";
                        // nth-child requires argument
                        if (!match[2]) {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle.error(match[0]);
                        }
                        // numeric x and y parameters for Expr.filter.CHILD
                        // remember that false/true cast respectively to 0/1
                        match[3] = +(match[3] ? match[4] + (match[5] || 1) : 2 * (match[2] === "even" || match[2] === "odd"));
                        match[4] = +(match[6] + match[7] || match[2] === "odd");
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (match[2]) {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle.error(match[0]);
                        }
                    }
                    return match;
                },
                PSEUDO: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    var argument, unquoted = match[4];
                    if (matchExpr["CHILD"].test(match[0])) {
                        "dk.brics.tajs.directives.unreachable";
                        return null;
                    }
                    // Relinquish our claim on characters in `unquoted` from a closing parenthesis on
                    if (unquoted && (argument = rselector.exec(unquoted)) && argument.pop()) {
                        "dk.brics.tajs.directives.unreachable";
                        match[0] = match[0].slice(0, argument[0].length - unquoted.length - 1);
                        unquoted = argument[0].slice(0, -1);
                    }
                    // Quoted or unquoted, we have the full argument
                    // Return only captures needed by the pseudo filter method (type and argument)
                    match.splice(2, 3, unquoted || match[3]);
                    return match;
                }
            },
            filter: {
                ID: assertGetIdNotName ? function(id) {
                    "dk.brics.tajs.directives.unreachable";
                    id = id.replace(rbackslash, "");
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.getAttribute("id") === id;
                    };
                } : function(id) {
                    "dk.brics.tajs.directives.unreachable";
                    id = id.replace(rbackslash, "");
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        var node = typeof elem.getAttributeNode !== strundefined && elem.getAttributeNode("id");
                        return node && node.value === id;
                    };
                },
                TAG: function(nodeName) {
                    "dk.brics.tajs.directives.unreachable";
                    if (nodeName === "*") {
                        "dk.brics.tajs.directives.unreachable";
                        return function() {
                            "dk.brics.tajs.directives.unreachable";
                            return true;
                        };
                    }
                    nodeName = nodeName.replace(rbackslash, "").toLowerCase();
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.nodeName && elem.nodeName.toLowerCase() === nodeName;
                    };
                },
                CLASS: function(className) {
                    "dk.brics.tajs.directives.unreachable";
                    var pattern = classCache[className];
                    if (!pattern) {
                        "dk.brics.tajs.directives.unreachable";
                        pattern = classCache[className] = new RegExp("(^|" + whitespace + ")" + className + "(" + whitespace + "|$)");
                        cachedClasses.push(className);
                        // Avoid too large of a cache
                        if (cachedClasses.length > Expr.cacheLength) {
                            "dk.brics.tajs.directives.unreachable";
                            delete classCache[cachedClasses.shift()];
                        }
                    }
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return pattern.test(elem.className || typeof elem.getAttribute !== strundefined && elem.getAttribute("class") || "");
                    };
                },
                ATTR: function(name, operator, check) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!operator) {
                        "dk.brics.tajs.directives.unreachable";
                        return function(elem) {
                            "dk.brics.tajs.directives.unreachable";
                            return Sizzle.attr(elem, name) != null;
                        };
                    }
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        var result = Sizzle.attr(elem, name), value = result + "";
                        if (result == null) {
                            "dk.brics.tajs.directives.unreachable";
                            return operator === "!=";
                        }
                        switch (operator) {
                          case "=":
                            "dk.brics.tajs.directives.unreachable";
                            return value === check;

                          case "!=":
                            "dk.brics.tajs.directives.unreachable";
                            return value !== check;

                          case "^=":
                            "dk.brics.tajs.directives.unreachable";
                            return check && value.indexOf(check) === 0;

                          case "*=":
                            "dk.brics.tajs.directives.unreachable";
                            return check && value.indexOf(check) > -1;

                          case "$=":
                            "dk.brics.tajs.directives.unreachable";
                            return check && value.substr(value.length - check.length) === check;

                          case "~=":
                            "dk.brics.tajs.directives.unreachable";
                            return (" " + value + " ").indexOf(check) > -1;

                          case "|=":
                            "dk.brics.tajs.directives.unreachable";
                            return value === check || value.substr(0, check.length + 1) === check + "-";
                        }
                    };
                },
                CHILD: function(type, argument, first, last) {
                    "dk.brics.tajs.directives.unreachable";
                    if (type === "nth") {
                        "dk.brics.tajs.directives.unreachable";
                        var doneName = done++;
                        return function(elem) {
                            "dk.brics.tajs.directives.unreachable";
                            var parent, diff, count = 0, node = elem;
                            if (first === 1 && last === 0) {
                                "dk.brics.tajs.directives.unreachable";
                                return true;
                            }
                            parent = elem.parentNode;
                            if (parent && (parent[expando] !== doneName || !elem.sizset)) {
                                "dk.brics.tajs.directives.unreachable";
                                for (node = parent.firstChild; node; node = node.nextSibling) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (node.nodeType === 1) {
                                        "dk.brics.tajs.directives.unreachable";
                                        node.sizset = ++count;
                                        if (node === elem) {
                                            "dk.brics.tajs.directives.unreachable";
                                            break;
                                        }
                                    }
                                }
                                parent[expando] = doneName;
                            }
                            diff = elem.sizset - last;
                            if (first === 0) {
                                "dk.brics.tajs.directives.unreachable";
                                return diff === 0;
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                return diff % first === 0 && diff / first >= 0;
                            }
                        };
                    }
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        var node = elem;
                        switch (type) {
                          case "only":
                          case "first":
                            "dk.brics.tajs.directives.unreachable";
                            while (node = node.previousSibling) {
                                "dk.brics.tajs.directives.unreachable";
                                if (node.nodeType === 1) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return false;
                                }
                            }
                            if (type === "first") {
                                "dk.brics.tajs.directives.unreachable";
                                return true;
                            }
                            node = elem;

                          /* falls through */
                            case "last":
                            "dk.brics.tajs.directives.unreachable";
                            while (node = node.nextSibling) {
                                "dk.brics.tajs.directives.unreachable";
                                if (node.nodeType === 1) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return false;
                                }
                            }
                            return true;
                        }
                    };
                },
                PSEUDO: function(pseudo, argument, context, xml) {
                    "dk.brics.tajs.directives.unreachable";
                    // pseudo-class names are case-insensitive
                    // http://www.w3.org/TR/selectors/#pseudo-classes
                    // Prioritize by case sensitivity in case custom pseudos are added with uppercase letters
                    var fn = Expr.pseudos[pseudo] || Expr.pseudos[pseudo.toLowerCase()];
                    if (!fn) {
                        "dk.brics.tajs.directives.unreachable";
                        Sizzle.error("unsupported pseudo: " + pseudo);
                    }
                    // The user may set fn.sizzleFilter to indicate
                    // that arguments are needed to create the filter function
                    // just as Sizzle does
                    if (!fn.sizzleFilter) {
                        "dk.brics.tajs.directives.unreachable";
                        return fn;
                    }
                    return fn(argument, context, xml);
                }
            },
            pseudos: {
                not: markFunction(function(selector, context, xml) {
                    "dk.brics.tajs.directives.unreachable";
                    // Trim the selector passed to compile
                    // to avoid treating leading and trailing
                    // spaces as combinators
                    var matcher = compile(selector.replace(rtrim, "$1"), context, xml);
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return !matcher(elem);
                    };
                }),
                enabled: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.disabled === false;
                },
                disabled: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.disabled === true;
                },
                checked: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // In CSS3, :checked should return both checked and selected elements
                    // http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
                    var nodeName = elem.nodeName.toLowerCase();
                    return nodeName === "input" && !!elem.checked || nodeName === "option" && !!elem.selected;
                },
                selected: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // Accessing this property makes selected-by-default
                    // options in Safari work properly
                    if (elem.parentNode) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.parentNode.selectedIndex;
                    }
                    return elem.selected === true;
                },
                parent: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return !Expr.pseudos["empty"](elem);
                },
                empty: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // http://www.w3.org/TR/selectors/#empty-pseudo
                    // :empty is only affected by element nodes and content nodes(including text(3), cdata(4)),
                    //   not comment, processing instructions, or others
                    // Thanks to Diego Perini for the nodeName shortcut
                    //   Greater than "@" means alpha characters (specifically not starting with "#" or "?")
                    var nodeType;
                    elem = elem.firstChild;
                    while (elem) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem.nodeName > "@" || (nodeType = elem.nodeType) === 3 || nodeType === 4) {
                            "dk.brics.tajs.directives.unreachable";
                            return false;
                        }
                        elem = elem.nextSibling;
                    }
                    return true;
                },
                contains: markFunction(function(text) {
                    "dk.brics.tajs.directives.unreachable";
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return (elem.textContent || elem.innerText || getText(elem)).indexOf(text) > -1;
                    };
                }),
                has: markFunction(function(selector) {
                    "dk.brics.tajs.directives.unreachable";
                    return function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return Sizzle(selector, elem).length > 0;
                    };
                }),
                header: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return rheader.test(elem.nodeName);
                },
                text: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var type, attr;
                    // IE6 and 7 will map elem.type to 'text' for new HTML5 types (search, etc)
                    // use getAttribute instead to test this case
                    return elem.nodeName.toLowerCase() === "input" && (type = elem.type) === "text" && ((attr = elem.getAttribute("type")) == null || attr.toLowerCase() === type);
                },
                // Input types
                radio: createInputFunction("radio"),
                checkbox: createInputFunction("checkbox"),
                file: createInputFunction("file"),
                password: createInputFunction("password"),
                image: createInputFunction("image"),
                submit: createButtonFunction("submit"),
                reset: createButtonFunction("reset"),
                button: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = elem.nodeName.toLowerCase();
                    return name === "input" && elem.type === "button" || name === "button";
                },
                input: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return rinputs.test(elem.nodeName);
                },
                focus: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var doc = elem.ownerDocument;
                    return elem === doc.activeElement && (!doc.hasFocus || doc.hasFocus()) && !!(elem.type || elem.href);
                },
                active: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem === elem.ownerDocument.activeElement;
                }
            },
            setFilters: {
                first: function(elements, argument, not) {
                    "dk.brics.tajs.directives.unreachable";
                    return not ? elements.slice(1) : [ elements[0] ];
                },
                last: function(elements, argument, not) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = elements.pop();
                    return not ? elements : [ elem ];
                },
                even: function(elements, argument, not) {
                    "dk.brics.tajs.directives.unreachable";
                    var results = [], i = not ? 1 : 0, len = elements.length;
                    for (;i < len; i = i + 2) {
                        "dk.brics.tajs.directives.unreachable";
                        results.push(elements[i]);
                    }
                    return results;
                },
                odd: function(elements, argument, not) {
                    "dk.brics.tajs.directives.unreachable";
                    var results = [], i = not ? 0 : 1, len = elements.length;
                    for (;i < len; i = i + 2) {
                        "dk.brics.tajs.directives.unreachable";
                        results.push(elements[i]);
                    }
                    return results;
                },
                lt: function(elements, argument, not) {
                    "dk.brics.tajs.directives.unreachable";
                    return not ? elements.slice(+argument) : elements.slice(0, +argument);
                },
                gt: function(elements, argument, not) {
                    "dk.brics.tajs.directives.unreachable";
                    return not ? elements.slice(0, +argument + 1) : elements.slice(+argument + 1);
                },
                eq: function(elements, argument, not) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = elements.splice(+argument, 1);
                    return not ? elements : elem;
                }
            }
        };
        // Deprecated
        Expr.setFilters["nth"] = Expr.setFilters["eq"];
        // Back-compat
        Expr.filters = Expr.pseudos;
        // IE6/7 return a modified href
        if (!assertHrefNotNormalized) {
            "dk.brics.tajs.directives.unreachable";
            Expr.attrHandle = {
                href: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.getAttribute("href", 2);
                },
                type: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.getAttribute("type");
                }
            };
        }
        // Add getElementsByName if usable
        if (assertUsableName) {
            Expr.order.push("NAME");
            Expr.find["NAME"] = function(name, context) {
                "dk.brics.tajs.directives.unreachable";
                if (typeof context.getElementsByName !== strundefined) {
                    "dk.brics.tajs.directives.unreachable";
                    return context.getElementsByName(name);
                }
            };
        }
        // Add getElementsByClassName if usable
        if (assertUsableClassName) {
            Expr.order.splice(1, 0, "CLASS");
            Expr.find["CLASS"] = function(className, context, xml) {
                "dk.brics.tajs.directives.unreachable";
                if (typeof context.getElementsByClassName !== strundefined && !xml) {
                    "dk.brics.tajs.directives.unreachable";
                    return context.getElementsByClassName(className);
                }
            };
        }
        // If slice is not available, provide a backup
        try {
            slice.call(docElem.childNodes, 0)[0].nodeType;
        } catch (e) {
            "dk.brics.tajs.directives.unreachable";
            slice = function(i) {
                "dk.brics.tajs.directives.unreachable";
                var elem, results = [];
                for (;elem = this[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    results.push(elem);
                }
                return results;
            };
        }
        var isXML = Sizzle.isXML = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            // documentElement is verified for cases where it doesn't yet exist
            // (such as loading iframes in IE - #4833)
            var documentElement = elem && (elem.ownerDocument || elem).documentElement;
            return documentElement ? documentElement.nodeName !== "HTML" : false;
        };
        // Element contains another
        var contains = Sizzle.contains = docElem.compareDocumentPosition ? function(a, b) {
            "dk.brics.tajs.directives.unreachable";
            return !!(a.compareDocumentPosition(b) & 16);
        } : docElem.contains ? function(a, b) {
            "dk.brics.tajs.directives.unreachable";
            var adown = a.nodeType === 9 ? a.documentElement : a, bup = b.parentNode;
            return a === bup || !!(bup && bup.nodeType === 1 && adown.contains && adown.contains(bup));
        } : function(a, b) {
            "dk.brics.tajs.directives.unreachable";
            while (b = b.parentNode) {
                "dk.brics.tajs.directives.unreachable";
                if (b === a) {
                    "dk.brics.tajs.directives.unreachable";
                    return true;
                }
            }
            return false;
        };
        /**
 * Utility function for retrieving the text value of an array of DOM nodes
 * @param {Array|Element} elem
 */
        var getText = Sizzle.getText = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            var node, ret = "", i = 0, nodeType = elem.nodeType;
            if (nodeType) {
                "dk.brics.tajs.directives.unreachable";
                if (nodeType === 1 || nodeType === 9 || nodeType === 11) {
                    "dk.brics.tajs.directives.unreachable";
                    // Use textContent for elements
                    // innerText usage removed for consistency of new lines (see #11153)
                    if (typeof elem.textContent === "string") {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.textContent;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        // Traverse its children
                        for (elem = elem.firstChild; elem; elem = elem.nextSibling) {
                            "dk.brics.tajs.directives.unreachable";
                            ret += getText(elem);
                        }
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (nodeType === 3 || nodeType === 4) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.nodeValue;
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                // If no nodeType, this is expected to be an array
                for (;node = elem[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    // Do not traverse comment nodes
                    ret += getText(node);
                }
            }
            return ret;
        };
        Sizzle.attr = function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            var attr, xml = isXML(elem);
            if (!xml) {
                "dk.brics.tajs.directives.unreachable";
                name = name.toLowerCase();
            }
            if (Expr.attrHandle[name]) {
                "dk.brics.tajs.directives.unreachable";
                return Expr.attrHandle[name](elem);
            }
            if (assertAttributes || xml) {
                "dk.brics.tajs.directives.unreachable";
                return elem.getAttribute(name);
            }
            attr = elem.getAttributeNode(name);
            return attr ? typeof elem[name] === "boolean" ? elem[name] ? name : null : attr.specified ? attr.value : null : null;
        };
        Sizzle.error = function(msg) {
            "dk.brics.tajs.directives.unreachable";
            throw new Error("Syntax error, unrecognized expression: " + msg);
        };
        // Check if the JavaScript engine is using some sort of
        // optimization where it does not always call our comparision
        // function. If that is the case, discard the hasDuplicate value.
        //   Thus far that includes Google Chrome.
        [ 0, 0 ].sort(function() {
            return baseHasDuplicate = 0;
        });
        if (docElem.compareDocumentPosition) {
            sortOrder = function(a, b) {
                "dk.brics.tajs.directives.unreachable";
                if (a === b) {
                    "dk.brics.tajs.directives.unreachable";
                    hasDuplicate = true;
                    return 0;
                }
                return (!a.compareDocumentPosition || !b.compareDocumentPosition ? a.compareDocumentPosition : a.compareDocumentPosition(b) & 4) ? -1 : 1;
            };
        } else {
            "dk.brics.tajs.directives.unreachable";
            sortOrder = function(a, b) {
                "dk.brics.tajs.directives.unreachable";
                // The nodes are identical, we can exit early
                if (a === b) {
                    "dk.brics.tajs.directives.unreachable";
                    hasDuplicate = true;
                    return 0;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (a.sourceIndex && b.sourceIndex) {
                        "dk.brics.tajs.directives.unreachable";
                        return a.sourceIndex - b.sourceIndex;
                    }
                }
                var al, bl, ap = [], bp = [], aup = a.parentNode, bup = b.parentNode, cur = aup;
                // If the nodes are siblings (or identical) we can do a quick check
                if (aup === bup) {
                    "dk.brics.tajs.directives.unreachable";
                    return siblingCheck(a, b);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (!aup) {
                        "dk.brics.tajs.directives.unreachable";
                        return -1;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (!bup) {
                            "dk.brics.tajs.directives.unreachable";
                            return 1;
                        }
                    }
                }
                // Otherwise they're somewhere else in the tree so we need
                // to build up a full list of the parentNodes for comparison
                while (cur) {
                    "dk.brics.tajs.directives.unreachable";
                    ap.unshift(cur);
                    cur = cur.parentNode;
                }
                cur = bup;
                while (cur) {
                    "dk.brics.tajs.directives.unreachable";
                    bp.unshift(cur);
                    cur = cur.parentNode;
                }
                al = ap.length;
                bl = bp.length;
                // Start walking down the tree looking for a discrepancy
                for (var i = 0; i < al && i < bl; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (ap[i] !== bp[i]) {
                        "dk.brics.tajs.directives.unreachable";
                        return siblingCheck(ap[i], bp[i]);
                    }
                }
                // We ended someplace up the tree so do a sibling check
                return i === al ? siblingCheck(a, bp[i], -1) : siblingCheck(ap[i], b, 1);
            };
            siblingCheck = function(a, b, ret) {
                "dk.brics.tajs.directives.unreachable";
                if (a === b) {
                    "dk.brics.tajs.directives.unreachable";
                    return ret;
                }
                var cur = a.nextSibling;
                while (cur) {
                    "dk.brics.tajs.directives.unreachable";
                    if (cur === b) {
                        "dk.brics.tajs.directives.unreachable";
                        return -1;
                    }
                    cur = cur.nextSibling;
                }
                return 1;
            };
        }
        // Document sorting and removing duplicates
        Sizzle.uniqueSort = function(results) {
            "dk.brics.tajs.directives.unreachable";
            var elem, i = 1;
            if (sortOrder) {
                "dk.brics.tajs.directives.unreachable";
                hasDuplicate = baseHasDuplicate;
                results.sort(sortOrder);
                if (hasDuplicate) {
                    "dk.brics.tajs.directives.unreachable";
                    for (;elem = results[i]; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem === results[i - 1]) {
                            "dk.brics.tajs.directives.unreachable";
                            results.splice(i--, 1);
                        }
                    }
                }
            }
            return results;
        };
        function multipleContexts(selector, contexts, results, seed) {
            "dk.brics.tajs.directives.unreachable";
            var i = 0, len = contexts.length;
            for (;i < len; i++) {
                "dk.brics.tajs.directives.unreachable";
                Sizzle(selector, contexts[i], results, seed);
            }
        }
        function handlePOSGroup(selector, posfilter, argument, contexts, seed, not) {
            "dk.brics.tajs.directives.unreachable";
            var results, fn = Expr.setFilters[posfilter.toLowerCase()];
            if (!fn) {
                "dk.brics.tajs.directives.unreachable";
                Sizzle.error(posfilter);
            }
            if (selector || !(results = seed)) {
                "dk.brics.tajs.directives.unreachable";
                multipleContexts(selector || "*", contexts, results = [], seed);
            }
            return results.length > 0 ? fn(results, argument, not) : [];
        }
        function handlePOS(selector, context, results, seed, groups) {
            "dk.brics.tajs.directives.unreachable";
            var match, not, anchor, ret, elements, currentContexts, part, lastIndex, i = 0, len = groups.length, rpos = matchExpr["POS"], // This is generated here in case matchExpr["POS"] is extended
            rposgroups = new RegExp("^" + rpos.source + "(?!" + whitespace + ")", "i"), // This is for making sure non-participating
            // matching groups are represented cross-browser (IE6-8)
            setUndefined = function() {
                "dk.brics.tajs.directives.unreachable";
                var i = 1, len = arguments.length - 2;
                for (;i < len; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (arguments[i] === undefined) {
                        "dk.brics.tajs.directives.unreachable";
                        match[i] = undefined;
                    }
                }
            };
            for (;i < len; i++) {
                "dk.brics.tajs.directives.unreachable";
                // Reset regex index to 0
                rpos.exec("");
                selector = groups[i];
                ret = [];
                anchor = 0;
                elements = seed;
                while (match = rpos.exec(selector)) {
                    "dk.brics.tajs.directives.unreachable";
                    lastIndex = rpos.lastIndex = match.index + match[0].length;
                    if (lastIndex > anchor) {
                        "dk.brics.tajs.directives.unreachable";
                        part = selector.slice(anchor, match.index);
                        anchor = lastIndex;
                        currentContexts = [ context ];
                        if (rcombinators.test(part)) {
                            "dk.brics.tajs.directives.unreachable";
                            if (elements) {
                                "dk.brics.tajs.directives.unreachable";
                                currentContexts = elements;
                            }
                            elements = seed;
                        }
                        if (not = rendsWithNot.test(part)) {
                            "dk.brics.tajs.directives.unreachable";
                            part = part.slice(0, -5).replace(rcombinators, "$&*");
                        }
                        if (match.length > 1) {
                            "dk.brics.tajs.directives.unreachable";
                            match[0].replace(rposgroups, setUndefined);
                        }
                        elements = handlePOSGroup(part, match[1], match[2], currentContexts, elements, not);
                    }
                }
                if (elements) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = ret.concat(elements);
                    if ((part = selector.slice(anchor)) && part !== ")") {
                        "dk.brics.tajs.directives.unreachable";
                        if (rcombinators.test(part)) {
                            "dk.brics.tajs.directives.unreachable";
                            multipleContexts(part, ret, results, seed);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle(part, context, results, seed ? seed.concat(elements) : elements);
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        push.apply(results, ret);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    Sizzle(selector, context, results, seed);
                }
            }
            // Do not sort if this is a single filter
            return len === 1 ? results : Sizzle.uniqueSort(results);
        }
        function tokenize(selector, context, xml) {
            "dk.brics.tajs.directives.unreachable";
            var tokens, soFar, type, groups = [], i = 0, // Catch obvious selector issues: terminal ")"; nonempty fallback match
            // rselector never fails to match *something*
            match = rselector.exec(selector), matched = !match.pop() && !match.pop(), selectorGroups = matched && selector.match(rgroups) || [ "" ], preFilters = Expr.preFilter, filters = Expr.filter, checkContext = !xml && context !== document;
            for (;(soFar = selectorGroups[i]) != null && matched; i++) {
                "dk.brics.tajs.directives.unreachable";
                groups.push(tokens = []);
                // Need to make sure we're within a narrower context if necessary
                // Adding a descendant combinator will generate what is needed
                if (checkContext) {
                    "dk.brics.tajs.directives.unreachable";
                    soFar = " " + soFar;
                }
                while (soFar) {
                    "dk.brics.tajs.directives.unreachable";
                    matched = false;
                    // Combinators
                    if (match = rcombinators.exec(soFar)) {
                        "dk.brics.tajs.directives.unreachable";
                        soFar = soFar.slice(match[0].length);
                        // Cast descendant combinators to space
                        matched = tokens.push({
                            part: match.pop().replace(rtrim, " "),
                            captures: match
                        });
                    }
                    // Filters
                    for (type in filters) {
                        "dk.brics.tajs.directives.unreachable";
                        if ((match = matchExpr[type].exec(soFar)) && (!preFilters[type] || (match = preFilters[type](match, context, xml)))) {
                            "dk.brics.tajs.directives.unreachable";
                            soFar = soFar.slice(match.shift().length);
                            matched = tokens.push({
                                part: type,
                                captures: match
                            });
                        }
                    }
                    if (!matched) {
                        "dk.brics.tajs.directives.unreachable";
                        break;
                    }
                }
            }
            if (!matched) {
                "dk.brics.tajs.directives.unreachable";
                Sizzle.error(selector);
            }
            return groups;
        }
        function addCombinator(matcher, combinator, context) {
            "dk.brics.tajs.directives.unreachable";
            var dir = combinator.dir, doneName = done++;
            if (!matcher) {
                "dk.brics.tajs.directives.unreachable";
                // If there is no matcher to check, check against the context
                matcher = function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem === context;
                };
            }
            return combinator.first ? function(elem, context) {
                "dk.brics.tajs.directives.unreachable";
                while (elem = elem[dir]) {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        return matcher(elem, context) && elem;
                    }
                }
            } : function(elem, context) {
                "dk.brics.tajs.directives.unreachable";
                var cache, dirkey = doneName + "." + dirruns, cachedkey = dirkey + "." + cachedruns;
                while (elem = elem[dir]) {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        if ((cache = elem[expando]) === cachedkey) {
                            "dk.brics.tajs.directives.unreachable";
                            return elem.sizset;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (typeof cache === "string" && cache.indexOf(dirkey) === 0) {
                                "dk.brics.tajs.directives.unreachable";
                                if (elem.sizset) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return elem;
                                }
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                elem[expando] = cachedkey;
                                if (matcher(elem, context)) {
                                    "dk.brics.tajs.directives.unreachable";
                                    elem.sizset = true;
                                    return elem;
                                }
                                elem.sizset = false;
                            }
                        }
                    }
                }
            };
        }
        function addMatcher(higher, deeper) {
            "dk.brics.tajs.directives.unreachable";
            return higher ? function(elem, context) {
                "dk.brics.tajs.directives.unreachable";
                var result = deeper(elem, context);
                return result && higher(result === true ? elem : result, context);
            } : deeper;
        }
        // ["TAG", ">", "ID", " ", "CLASS"]
        function matcherFromTokens(tokens, context, xml) {
            "dk.brics.tajs.directives.unreachable";
            var token, matcher, i = 0;
            for (;token = tokens[i]; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (Expr.relative[token.part]) {
                    "dk.brics.tajs.directives.unreachable";
                    matcher = addCombinator(matcher, Expr.relative[token.part], context);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    token.captures.push(context, xml);
                    matcher = addMatcher(matcher, Expr.filter[token.part].apply(null, token.captures));
                }
            }
            return matcher;
        }
        function matcherFromGroupMatchers(matchers) {
            "dk.brics.tajs.directives.unreachable";
            return function(elem, context) {
                "dk.brics.tajs.directives.unreachable";
                var matcher, j = 0;
                for (;matcher = matchers[j]; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (matcher(elem, context)) {
                        "dk.brics.tajs.directives.unreachable";
                        return true;
                    }
                }
                return false;
            };
        }
        var compile = Sizzle.compile = function(selector, context, xml) {
            "dk.brics.tajs.directives.unreachable";
            var tokens, group, i, cached = compilerCache[selector];
            // Return a cached group function if already generated (context dependent)
            if (cached && cached.context === context) {
                "dk.brics.tajs.directives.unreachable";
                return cached;
            }
            // Generate a function of recursive functions that can be used to check each element
            group = tokenize(selector, context, xml);
            for (i = 0; tokens = group[i]; i++) {
                "dk.brics.tajs.directives.unreachable";
                group[i] = matcherFromTokens(tokens, context, xml);
            }
            // Cache the compiled function
            cached = compilerCache[selector] = matcherFromGroupMatchers(group);
            cached.context = context;
            cached.runs = cached.dirruns = 0;
            cachedSelectors.push(selector);
            // Ensure only the most recent are cached
            if (cachedSelectors.length > Expr.cacheLength) {
                "dk.brics.tajs.directives.unreachable";
                delete compilerCache[cachedSelectors.shift()];
            }
            return cached;
        };
        Sizzle.matches = function(expr, elements) {
            "dk.brics.tajs.directives.unreachable";
            return Sizzle(expr, null, null, elements);
        };
        Sizzle.matchesSelector = function(elem, expr) {
            "dk.brics.tajs.directives.unreachable";
            return Sizzle(expr, null, null, [ elem ]).length > 0;
        };
        var select = function(selector, context, results, seed, xml) {
            "dk.brics.tajs.directives.unreachable";
            // Remove excessive whitespace
            selector = selector.replace(rtrim, "$1");
            var elements, matcher, i, len, elem, token, type, findContext, notTokens, match = selector.match(rgroups), tokens = selector.match(rtokens), contextNodeType = context.nodeType;
            // POS handling
            if (matchExpr["POS"].test(selector)) {
                "dk.brics.tajs.directives.unreachable";
                return handlePOS(selector, context, results, seed, match);
            }
            if (seed) {
                "dk.brics.tajs.directives.unreachable";
                elements = slice.call(seed, 0);
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (match && match.length === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    // Take a shortcut and set the context if the root selector is an ID
                    if (tokens.length > 1 && contextNodeType === 9 && !xml && (match = matchExpr["ID"].exec(tokens[0]))) {
                        "dk.brics.tajs.directives.unreachable";
                        context = Expr.find["ID"](match[1], context, xml)[0];
                        if (!context) {
                            "dk.brics.tajs.directives.unreachable";
                            return results;
                        }
                        selector = selector.slice(tokens.shift().length);
                    }
                    findContext = (match = rsibling.exec(tokens[0])) && !match.index && context.parentNode || context;
                    // Get the last token, excluding :not
                    notTokens = tokens.pop();
                    token = notTokens.split(":not")[0];
                    for (i = 0, len = Expr.order.length; i < len; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        type = Expr.order[i];
                        if (match = matchExpr[type].exec(token)) {
                            "dk.brics.tajs.directives.unreachable";
                            elements = Expr.find[type]((match[1] || "").replace(rbackslash, ""), findContext, xml);
                            if (elements == null) {
                                "dk.brics.tajs.directives.unreachable";
                                continue;
                            }
                            if (token === notTokens) {
                                "dk.brics.tajs.directives.unreachable";
                                selector = selector.slice(0, selector.length - notTokens.length) + token.replace(matchExpr[type], "");
                                if (!selector) {
                                    "dk.brics.tajs.directives.unreachable";
                                    push.apply(results, slice.call(elements, 0));
                                }
                            }
                            break;
                        }
                    }
                }
            }
            // Only loop over the given elements once
            // If selector is empty, we're already done
            if (selector) {
                "dk.brics.tajs.directives.unreachable";
                matcher = compile(selector, context, xml);
                dirruns = matcher.dirruns++;
                if (elements == null) {
                    "dk.brics.tajs.directives.unreachable";
                    elements = Expr.find["TAG"]("*", rsibling.test(selector) && context.parentNode || context);
                }
                for (i = 0; elem = elements[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    cachedruns = matcher.runs++;
                    if (matcher(elem, context)) {
                        "dk.brics.tajs.directives.unreachable";
                        results.push(elem);
                    }
                }
            }
            return results;
        };
        if (document.querySelectorAll) {
            (function() {
                var disconnectedMatch, oldSelect = select, rescape = /'|\\/g, rattributeQuotes = /\=[\x20\t\r\n\f]*([^'"\]]*)[\x20\t\r\n\f]*\]/g, rbuggyQSA = [], // matchesSelector(:active) reports false when true (IE9/Opera 11.5)
                // A support test would require too much code (would include document ready)
                // just skip matchesSelector for :active
                rbuggyMatches = [ ":active" ], matches = docElem.matchesSelector || docElem.mozMatchesSelector || docElem.webkitMatchesSelector || docElem.oMatchesSelector || docElem.msMatchesSelector;
                // Build QSA regex
                // Regex strategy adopted from Diego Perini
                assert(function(div) {
                    div.innerHTML = "<select><option selected></option></select>";
                    // IE8 - Some boolean attributes are not treated correctly
                    if (!div.querySelectorAll("[selected]").length) {
                        "dk.brics.tajs.directives.unreachable";
                        rbuggyQSA.push("\\[" + whitespace + "*(?:checked|disabled|ismap|multiple|readonly|selected|value)");
                    }
                    // Webkit/Opera - :checked should return selected option elements
                    // http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
                    // IE8 throws error here (do not put tests after this one)
                    if (!div.querySelectorAll(":checked").length) {
                        "dk.brics.tajs.directives.unreachable";
                        rbuggyQSA.push(":checked");
                    }
                });
                assert(function(div) {
                    // Opera 10-12/IE9 - ^= $= *= and empty values
                    // Should not select anything
                    div.innerHTML = "<p test=''></p>";
                    if (div.querySelectorAll("[test^='']").length) {
                        "dk.brics.tajs.directives.unreachable";
                        rbuggyQSA.push("[*^$]=" + whitespace + "*(?:\"\"|'')");
                    }
                    // FF 3.5 - :enabled/:disabled and hidden elements (hidden elements are still enabled)
                    // IE8 throws error here (do not put tests after this one)
                    div.innerHTML = "<input type='hidden'>";
                    if (!div.querySelectorAll(":enabled").length) {
                        "dk.brics.tajs.directives.unreachable";
                        rbuggyQSA.push(":enabled", ":disabled");
                    }
                });
                rbuggyQSA = rbuggyQSA.length && new RegExp(rbuggyQSA.join("|"));
                select = function(selector, context, results, seed, xml) {
                    "dk.brics.tajs.directives.unreachable";
                    // Only use querySelectorAll when not filtering,
                    // when this is not xml,
                    // and when no QSA bugs apply
                    if (!seed && !xml && (!rbuggyQSA || !rbuggyQSA.test(selector))) {
                        "dk.brics.tajs.directives.unreachable";
                        if (context.nodeType === 9) {
                            "dk.brics.tajs.directives.unreachable";
                            try {
                                "dk.brics.tajs.directives.unreachable";
                                push.apply(results, slice.call(context.querySelectorAll(selector), 0));
                                return results;
                            } catch (qsaError) {}
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (context.nodeType === 1 && context.nodeName.toLowerCase() !== "object") {
                                "dk.brics.tajs.directives.unreachable";
                                var old = context.getAttribute("id"), nid = old || expando, newContext = rsibling.test(selector) && context.parentNode || context;
                                if (old) {
                                    "dk.brics.tajs.directives.unreachable";
                                    nid = nid.replace(rescape, "\\$&");
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    context.setAttribute("id", nid);
                                }
                                try {
                                    "dk.brics.tajs.directives.unreachable";
                                    push.apply(results, slice.call(newContext.querySelectorAll(selector.replace(rgroups, "[id='" + nid + "'] $&")), 0));
                                    return results;
                                } catch (qsaError) {} finally {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (!old) {
                                        "dk.brics.tajs.directives.unreachable";
                                        context.removeAttribute("id");
                                    }
                                }
                            }
                        }
                    }
                    return oldSelect(selector, context, results, seed, xml);
                };
                if (matches) {
                    assert(function(div) {
                        // Check to see if it's possible to do matchesSelector
                        // on a disconnected node (IE 9)
                        disconnectedMatch = matches.call(div, "div");
                        // This should fail with an exception
                        // Gecko does not error, returns false instead
                        try {
                            matches.call(div, "[test!='']:sizzle");
                            "dk.brics.tajs.directives.unreachable";
                            rbuggyMatches.push(Expr.match.PSEUDO);
                        } catch (e) {}
                    });
                    // rbuggyMatches always contains :active, so no need for a length check
                    rbuggyMatches = /* rbuggyMatches.length && */ new RegExp(rbuggyMatches.join("|"));
                    Sizzle.matchesSelector = function(elem, expr) {
                        "dk.brics.tajs.directives.unreachable";
                        // Make sure that attribute selectors are quoted
                        expr = expr.replace(rattributeQuotes, "='$1']");
                        // rbuggyMatches always contains :active, so no need for an existence check
                        if (!isXML(elem) && !rbuggyMatches.test(expr) && (!rbuggyQSA || !rbuggyQSA.test(expr))) {
                            "dk.brics.tajs.directives.unreachable";
                            try {
                                "dk.brics.tajs.directives.unreachable";
                                var ret = matches.call(elem, expr);
                                // IE 9's matchesSelector returns false on disconnected nodes
                                if (ret || disconnectedMatch || // As well, disconnected nodes are said to be in a document
                                // fragment in IE 9
                                elem.document && elem.document.nodeType !== 11) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return ret;
                                }
                            } catch (e) {}
                        }
                        return Sizzle(expr, null, null, [ elem ]).length > 0;
                    };
                }
            })();
        }
        // Override sizzle attribute retrieval
        Sizzle.attr = jQuery.attr;
        jQuery.find = Sizzle;
        jQuery.expr = Sizzle.selectors;
        jQuery.expr[":"] = jQuery.expr.pseudos;
        jQuery.unique = Sizzle.uniqueSort;
        jQuery.text = Sizzle.getText;
        jQuery.isXMLDoc = Sizzle.isXML;
        jQuery.contains = Sizzle.contains;
    })(window);
    var runtil = /Until$/, rparentsprev = /^(?:parents|prev(?:Until|All))/, isSimple = /^.[^:#\[\.,]*$/, rneedsContext = jQuery.expr.match.needsContext, // methods guaranteed to produce a unique set when starting from a unique set
    guaranteedUnique = {
        children: true,
        contents: true,
        next: true,
        prev: true
    };
    jQuery.fn.extend({
        find: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            var i, l, length, n, r, ret, self = this;
            if (typeof selector !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return jQuery(selector).filter(function() {
                    "dk.brics.tajs.directives.unreachable";
                    for (i = 0, l = self.length; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (jQuery.contains(self[i], this)) {
                            "dk.brics.tajs.directives.unreachable";
                            return true;
                        }
                    }
                });
            }
            ret = this.pushStack("", "find", selector);
            for (i = 0, l = this.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                length = ret.length;
                jQuery.find(selector, this[i], ret);
                if (i > 0) {
                    "dk.brics.tajs.directives.unreachable";
                    // Make sure that the results are unique
                    for (n = length; n < ret.length; n++) {
                        "dk.brics.tajs.directives.unreachable";
                        for (r = 0; r < length; r++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (ret[r] === ret[n]) {
                                "dk.brics.tajs.directives.unreachable";
                                ret.splice(n--, 1);
                                break;
                            }
                        }
                    }
                }
            }
            return ret;
        },
        has: function(target) {
            "dk.brics.tajs.directives.unreachable";
            var i, targets = jQuery(target, this), len = targets.length;
            return this.filter(function() {
                "dk.brics.tajs.directives.unreachable";
                for (i = 0; i < len; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.contains(this, targets[i])) {
                        "dk.brics.tajs.directives.unreachable";
                        return true;
                    }
                }
            });
        },
        not: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(winnow(this, selector, false), "not", selector);
        },
        filter: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(winnow(this, selector, true), "filter", selector);
        },
        is: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return !!selector && (typeof selector === "string" ? // If this is a positional/relative selector, check membership in the returned set
            // so $("p:first").is("p:last") won't return true for a doc with two "p".
            rneedsContext.test(selector) ? jQuery(selector, this.context).index(this[0]) >= 0 : jQuery.filter(selector, this).length > 0 : this.filter(selector).length > 0);
        },
        closest: function(selectors, context) {
            "dk.brics.tajs.directives.unreachable";
            var cur, i = 0, l = this.length, ret = [], pos = rneedsContext.test(selectors) || typeof selectors !== "string" ? jQuery(selectors, context || this.context) : 0;
            for (;i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                cur = this[i];
                while (cur && cur.ownerDocument && cur !== context && cur.nodeType !== 11) {
                    "dk.brics.tajs.directives.unreachable";
                    if (pos ? pos.index(cur) > -1 : jQuery.find.matchesSelector(cur, selectors)) {
                        "dk.brics.tajs.directives.unreachable";
                        ret.push(cur);
                        break;
                    }
                    cur = cur.parentNode;
                }
            }
            ret = ret.length > 1 ? jQuery.unique(ret) : ret;
            return this.pushStack(ret, "closest", selectors);
        },
        // Determine the position of an element within
        // the matched set of elements
        index: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            // No argument, return index in parent
            if (!elem) {
                "dk.brics.tajs.directives.unreachable";
                return this[0] && this[0].parentNode ? this.prevAll().length : -1;
            }
            // index in selector
            if (typeof elem === "string") {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.inArray(this[0], jQuery(elem));
            }
            // Locate the position of the desired element
            return jQuery.inArray(// If it receives a jQuery object, the first element is used
            elem.jquery ? elem[0] : elem, this);
        },
        add: function(selector, context) {
            "dk.brics.tajs.directives.unreachable";
            var set = typeof selector === "string" ? jQuery(selector, context) : jQuery.makeArray(selector && selector.nodeType ? [ selector ] : selector), all = jQuery.merge(this.get(), set);
            return this.pushStack(isDisconnected(set[0]) || isDisconnected(all[0]) ? all : jQuery.unique(all));
        },
        addBack: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return this.add(selector == null ? this.prevObject : this.prevObject.filter(selector));
        }
    });
    jQuery.fn.andSelf = jQuery.fn.addBack;
    // A painfully simple check to see if an element is disconnected
    // from a document (should be improved, where feasible).
    function isDisconnected(node) {
        "dk.brics.tajs.directives.unreachable";
        return !node || !node.parentNode || node.parentNode.nodeType === 11;
    }
    function sibling(cur, dir) {
        "dk.brics.tajs.directives.unreachable";
        do {
            "dk.brics.tajs.directives.unreachable";
            cur = cur[dir];
        } while (cur && cur.nodeType !== 1);
        return cur;
    }
    jQuery.each({
        parent: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            var parent = elem.parentNode;
            return parent && parent.nodeType !== 11 ? parent : null;
        },
        parents: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.dir(elem, "parentNode");
        },
        parentsUntil: function(elem, i, until) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.dir(elem, "parentNode", until);
        },
        next: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return sibling(elem, "nextSibling");
        },
        prev: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return sibling(elem, "previousSibling");
        },
        nextAll: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.dir(elem, "nextSibling");
        },
        prevAll: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.dir(elem, "previousSibling");
        },
        nextUntil: function(elem, i, until) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.dir(elem, "nextSibling", until);
        },
        prevUntil: function(elem, i, until) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.dir(elem, "previousSibling", until);
        },
        siblings: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.sibling((elem.parentNode || {}).firstChild, elem);
        },
        children: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.sibling(elem.firstChild);
        },
        contents: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.nodeName(elem, "iframe") ? elem.contentDocument || elem.contentWindow.document : jQuery.merge([], elem.childNodes);
        }
    }, function(name, fn) {
        jQuery.fn[name] = function(until, selector) {
            "dk.brics.tajs.directives.unreachable";
            var ret = jQuery.map(this, fn, until);
            if (!runtil.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                selector = until;
            }
            if (selector && typeof selector === "string") {
                "dk.brics.tajs.directives.unreachable";
                ret = jQuery.filter(selector, ret);
            }
            ret = this.length > 1 && !guaranteedUnique[name] ? jQuery.unique(ret) : ret;
            if (this.length > 1 && rparentsprev.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                ret = ret.reverse();
            }
            return this.pushStack(ret, name, core_slice.call(arguments).join(","));
        };
    });
    jQuery.extend({
        filter: function(expr, elems, not) {
            "dk.brics.tajs.directives.unreachable";
            if (not) {
                "dk.brics.tajs.directives.unreachable";
                expr = ":not(" + expr + ")";
            }
            return elems.length === 1 ? jQuery.find.matchesSelector(elems[0], expr) ? [ elems[0] ] : [] : jQuery.find.matches(expr, elems);
        },
        dir: function(elem, dir, until) {
            "dk.brics.tajs.directives.unreachable";
            var matched = [], cur = elem[dir];
            while (cur && cur.nodeType !== 9 && (until === undefined || cur.nodeType !== 1 || !jQuery(cur).is(until))) {
                "dk.brics.tajs.directives.unreachable";
                if (cur.nodeType === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    matched.push(cur);
                }
                cur = cur[dir];
            }
            return matched;
        },
        sibling: function(n, elem) {
            "dk.brics.tajs.directives.unreachable";
            var r = [];
            for (;n; n = n.nextSibling) {
                "dk.brics.tajs.directives.unreachable";
                if (n.nodeType === 1 && n !== elem) {
                    "dk.brics.tajs.directives.unreachable";
                    r.push(n);
                }
            }
            return r;
        }
    });
    // Implement the identical functionality for filter and not
    function winnow(elements, qualifier, keep) {
        "dk.brics.tajs.directives.unreachable";
        // Can't pass null or undefined to indexOf in Firefox 4
        // Set to 0 to skip string check
        qualifier = qualifier || 0;
        if (jQuery.isFunction(qualifier)) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.grep(elements, function(elem, i) {
                "dk.brics.tajs.directives.unreachable";
                var retVal = !!qualifier.call(elem, i, elem);
                return retVal === keep;
            });
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (qualifier.nodeType) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.grep(elements, function(elem, i) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem === qualifier === keep;
                });
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (typeof qualifier === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    var filtered = jQuery.grep(elements, function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.nodeType === 1;
                    });
                    if (isSimple.test(qualifier)) {
                        "dk.brics.tajs.directives.unreachable";
                        return jQuery.filter(qualifier, filtered, !keep);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        qualifier = jQuery.filter(qualifier, filtered);
                    }
                }
            }
        }
        return jQuery.grep(elements, function(elem, i) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.inArray(elem, qualifier) >= 0 === keep;
        });
    }
    function createSafeFragment(document) {
        var list = nodeNames.split("|"), safeFrag = document.createDocumentFragment();
        if (safeFrag.createElement) {
            "dk.brics.tajs.directives.unreachable";
            while (list.length) {
                "dk.brics.tajs.directives.unreachable";
                safeFrag.createElement(list.pop());
            }
        }
        return safeFrag;
    }
    var nodeNames = "abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|" + "header|hgroup|mark|meter|nav|output|progress|section|summary|time|video", rinlinejQuery = / jQuery\d+="(?:null|\d+)"/g, rleadingWhitespace = /^\s+/, rxhtmlTag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi, rtagName = /<([\w:]+)/, rtbody = /<tbody/i, rhtml = /<|&#?\w+;/, rnoInnerhtml = /<(?:script|style|link)/i, rnocache = /<(?:script|object|embed|option|style)/i, rnoshimcache = new RegExp("<(?:" + nodeNames + ")[\\s/>]", "i"), rcheckableType = /^(?:checkbox|radio)$/, // checked="checked" or checked
    rchecked = /checked\s*(?:[^=]|=\s*.checked.)/i, rscriptType = /\/(java|ecma)script/i, rcleanScript = /^\s*<!(?:\[CDATA\[|\-\-)|[\]\-]{2}>\s*$/g, wrapMap = {
        option: [ 1, "<select multiple='multiple'>", "</select>" ],
        legend: [ 1, "<fieldset>", "</fieldset>" ],
        thead: [ 1, "<table>", "</table>" ],
        tr: [ 2, "<table><tbody>", "</tbody></table>" ],
        td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],
        col: [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ],
        area: [ 1, "<map>", "</map>" ],
        _default: [ 0, "", "" ]
    }, safeFragment = createSafeFragment(document), fragmentDiv = safeFragment.appendChild(document.createElement("div"));
    wrapMap.optgroup = wrapMap.option;
    wrapMap.tbody = wrapMap.tfoot = wrapMap.colgroup = wrapMap.caption = wrapMap.thead;
    wrapMap.th = wrapMap.td;
    // IE6-8 can't serialize link, script, style, or any html5 (NoScope) tags,
    // unless wrapped in a div with non-breaking characters in front of it.
    if (!jQuery.support.htmlSerialize) {
        "dk.brics.tajs.directives.unreachable";
        wrapMap._default = [ 1, "X<div>", "</div>" ];
    }
    jQuery.fn.extend({
        text: function(value) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, function(value) {
                "dk.brics.tajs.directives.unreachable";
                return value === undefined ? jQuery.text(this) : this.empty().append((this[0] && this[0].ownerDocument || document).createTextNode(value));
            }, null, value, arguments.length);
        },
        wrapAll: function(html) {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(html)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).wrapAll(html.call(this, i));
                });
            }
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                // The elements to wrap the target around
                var wrap = jQuery(html, this[0].ownerDocument).eq(0).clone(true);
                if (this[0].parentNode) {
                    "dk.brics.tajs.directives.unreachable";
                    wrap.insertBefore(this[0]);
                }
                wrap.map(function() {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = this;
                    while (elem.firstChild && elem.firstChild.nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        elem = elem.firstChild;
                    }
                    return elem;
                }).append(this);
            }
            return this;
        },
        wrapInner: function(html) {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(html)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).wrapInner(html.call(this, i));
                });
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                var self = jQuery(this), contents = self.contents();
                if (contents.length) {
                    "dk.brics.tajs.directives.unreachable";
                    contents.wrapAll(html);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    self.append(html);
                }
            });
        },
        wrap: function(html) {
            "dk.brics.tajs.directives.unreachable";
            var isFunction = jQuery.isFunction(html);
            return this.each(function(i) {
                "dk.brics.tajs.directives.unreachable";
                jQuery(this).wrapAll(isFunction ? html.call(this, i) : html);
            });
        },
        unwrap: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.parent().each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (!jQuery.nodeName(this, "body")) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).replaceWith(this.childNodes);
                }
            }).end();
        },
        append: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, true, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeType === 1 || this.nodeType === 11) {
                    "dk.brics.tajs.directives.unreachable";
                    this.appendChild(elem);
                }
            });
        },
        prepend: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, true, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeType === 1 || this.nodeType === 11) {
                    "dk.brics.tajs.directives.unreachable";
                    this.insertBefore(elem, this.firstChild);
                }
            });
        },
        before: function() {
            "dk.brics.tajs.directives.unreachable";
            if (!isDisconnected(this[0])) {
                "dk.brics.tajs.directives.unreachable";
                return this.domManip(arguments, false, function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    this.parentNode.insertBefore(elem, this);
                });
            }
            if (arguments.length) {
                "dk.brics.tajs.directives.unreachable";
                var set = jQuery.clean(arguments);
                return this.pushStack(jQuery.merge(set, this), "before", this.selector);
            }
        },
        after: function() {
            "dk.brics.tajs.directives.unreachable";
            if (!isDisconnected(this[0])) {
                "dk.brics.tajs.directives.unreachable";
                return this.domManip(arguments, false, function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    this.parentNode.insertBefore(elem, this.nextSibling);
                });
            }
            if (arguments.length) {
                "dk.brics.tajs.directives.unreachable";
                var set = jQuery.clean(arguments);
                return this.pushStack(jQuery.merge(this, set), "after", this.selector);
            }
        },
        // keepData is for internal use only--do not document
        remove: function(selector, keepData) {
            "dk.brics.tajs.directives.unreachable";
            var elem, i = 0;
            for (;(elem = this[i]) != null; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (!selector || jQuery.filter(selector, [ elem ]).length) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!keepData && elem.nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.cleanData(elem.getElementsByTagName("*"));
                        jQuery.cleanData([ elem ]);
                    }
                    if (elem.parentNode) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.parentNode.removeChild(elem);
                    }
                }
            }
            return this;
        },
        empty: function() {
            "dk.brics.tajs.directives.unreachable";
            var elem, i = 0;
            for (;(elem = this[i]) != null; i++) {
                "dk.brics.tajs.directives.unreachable";
                // Remove element nodes and prevent memory leaks
                if (elem.nodeType === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.cleanData(elem.getElementsByTagName("*"));
                }
                // Remove any remaining nodes
                while (elem.firstChild) {
                    "dk.brics.tajs.directives.unreachable";
                    elem.removeChild(elem.firstChild);
                }
            }
            return this;
        },
        clone: function(dataAndEvents, deepDataAndEvents) {
            "dk.brics.tajs.directives.unreachable";
            dataAndEvents = dataAndEvents == null ? false : dataAndEvents;
            deepDataAndEvents = deepDataAndEvents == null ? dataAndEvents : deepDataAndEvents;
            return this.map(function() {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.clone(this, dataAndEvents, deepDataAndEvents);
            });
        },
        html: function(value) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, function(value) {
                "dk.brics.tajs.directives.unreachable";
                var elem = this[0] || {}, i = 0, l = this.length;
                if (value === undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeType === 1 ? elem.innerHTML.replace(rinlinejQuery, "") : undefined;
                }
                // See if we can take a shortcut and just use innerHTML
                if (typeof value === "string" && !rnoInnerhtml.test(value) && (jQuery.support.htmlSerialize || !rnoshimcache.test(value)) && (jQuery.support.leadingWhitespace || !rleadingWhitespace.test(value)) && !wrapMap[(rtagName.exec(value) || [ "", "" ])[1].toLowerCase()]) {
                    "dk.brics.tajs.directives.unreachable";
                    value = value.replace(rxhtmlTag, "<$1></$2>");
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        for (;i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            // Remove element nodes and prevent memory leaks
                            elem = this[i] || {};
                            if (elem.nodeType === 1) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.cleanData(elem.getElementsByTagName("*"));
                                elem.innerHTML = value;
                            }
                        }
                        elem = 0;
                    } catch (e) {}
                }
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    this.empty().append(value);
                }
            }, null, value, arguments.length);
        },
        replaceWith: function(value) {
            "dk.brics.tajs.directives.unreachable";
            if (!isDisconnected(this[0])) {
                "dk.brics.tajs.directives.unreachable";
                // Make sure that the elements are removed from the DOM before they are inserted
                // this can help fix replacing a parent with child elements
                if (jQuery.isFunction(value)) {
                    "dk.brics.tajs.directives.unreachable";
                    return this.each(function(i) {
                        "dk.brics.tajs.directives.unreachable";
                        var self = jQuery(this), old = self.html();
                        self.replaceWith(value.call(this, i, old));
                    });
                }
                if (typeof value !== "string") {
                    "dk.brics.tajs.directives.unreachable";
                    value = jQuery(value).detach();
                }
                return this.each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    var next = this.nextSibling, parent = this.parentNode;
                    jQuery(this).remove();
                    if (next) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery(next).before(value);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery(parent).append(value);
                    }
                });
            }
            return this.length ? this.pushStack(jQuery(jQuery.isFunction(value) ? value() : value), "replaceWith", value) : this;
        },
        detach: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return this.remove(selector, true);
        },
        domManip: function(args, table, callback) {
            "dk.brics.tajs.directives.unreachable";
            // Flatten any nested arrays
            args = [].concat.apply([], args);
            var results, first, fragment, iNoClone, i = 0, value = args[0], scripts = [], l = this.length;
            // We can't cloneNode fragments that contain checked, in WebKit
            if (!jQuery.support.checkClone && l > 1 && typeof value === "string" && rchecked.test(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).domManip(args, table, callback);
                });
            }
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    var self = jQuery(this);
                    args[0] = value.call(this, i, table ? self.html() : undefined);
                    self.domManip(args, table, callback);
                });
            }
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                results = jQuery.buildFragment(args, this, scripts);
                fragment = results.fragment;
                first = fragment.firstChild;
                if (fragment.childNodes.length === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    fragment = first;
                }
                if (first) {
                    "dk.brics.tajs.directives.unreachable";
                    table = table && jQuery.nodeName(first, "tr");
                    // Use the original fragment for the last item instead of the first because it can end up
                    // being emptied incorrectly in certain situations (#8070).
                    // Fragments from the fragment cache must always be cloned and never used in place.
                    for (iNoClone = results.cacheable || l - 1; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        callback.call(table && jQuery.nodeName(this[i], "table") ? findOrAppend(this[i], "tbody") : this[i], i === iNoClone ? fragment : jQuery.clone(fragment, true, true));
                    }
                }
                // Fix #11809: Avoid leaking memory
                fragment = first = null;
                if (scripts.length) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.each(scripts, function(i, elem) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem.src) {
                            "dk.brics.tajs.directives.unreachable";
                            if (jQuery.ajax) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.ajax({
                                    url: elem.src,
                                    type: "GET",
                                    dataType: "script",
                                    async: false,
                                    global: false,
                                    "throws": true
                                });
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.error("no ajax");
                            }
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.globalEval((elem.text || elem.textContent || elem.innerHTML || "").replace(rcleanScript, ""));
                        }
                        if (elem.parentNode) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.parentNode.removeChild(elem);
                        }
                    });
                }
            }
            return this;
        }
    });
    function findOrAppend(elem, tag) {
        "dk.brics.tajs.directives.unreachable";
        return elem.getElementsByTagName(tag)[0] || elem.appendChild(elem.ownerDocument.createElement(tag));
    }
    function cloneCopyEvent(src, dest) {
        "dk.brics.tajs.directives.unreachable";
        if (dest.nodeType !== 1 || !jQuery.hasData(src)) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        var type, i, l, oldData = jQuery._data(src), curData = jQuery._data(dest, oldData), events = oldData.events;
        if (events) {
            "dk.brics.tajs.directives.unreachable";
            delete curData.handle;
            curData.events = {};
            for (type in events) {
                "dk.brics.tajs.directives.unreachable";
                for (i = 0, l = events[type].length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(dest, type, events[type][i]);
                }
            }
        }
        // make the cloned public data object a copy from the original
        if (curData.data) {
            "dk.brics.tajs.directives.unreachable";
            curData.data = jQuery.extend({}, curData.data);
        }
    }
    function cloneFixAttributes(src, dest) {
        "dk.brics.tajs.directives.unreachable";
        var nodeName;
        // We do not need to do anything for non-Elements
        if (dest.nodeType !== 1) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        // clearAttributes removes the attributes, which we don't want,
        // but also removes the attachEvent events, which we *do* want
        if (dest.clearAttributes) {
            "dk.brics.tajs.directives.unreachable";
            dest.clearAttributes();
        }
        // mergeAttributes, in contrast, only merges back on the
        // original attributes, not the events
        if (dest.mergeAttributes) {
            "dk.brics.tajs.directives.unreachable";
            dest.mergeAttributes(src);
        }
        nodeName = dest.nodeName.toLowerCase();
        if (nodeName === "object") {
            "dk.brics.tajs.directives.unreachable";
            // IE6-10 improperly clones children of object elements using classid.
            // IE10 throws NoModificationAllowedError if parent is null, #12132.
            if (dest.parentNode) {
                "dk.brics.tajs.directives.unreachable";
                dest.outerHTML = src.outerHTML;
            }
            // This path appears unavoidable for IE9. When cloning an object
            // element in IE9, the outerHTML strategy above is not sufficient.
            // If the src has innerHTML and the destination does not,
            // copy the src.innerHTML into the dest.innerHTML. #10324
            if (jQuery.support.html5Clone && src.innerHTML && !jQuery.trim(dest.innerHTML)) {
                "dk.brics.tajs.directives.unreachable";
                dest.innerHTML = src.innerHTML;
            }
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (nodeName === "input" && rcheckableType.test(src.type)) {
                "dk.brics.tajs.directives.unreachable";
                // IE6-8 fails to persist the checked state of a cloned checkbox
                // or radio button. Worse, IE6-7 fail to give the cloned element
                // a checked appearance if the defaultChecked value isn't also set
                dest.defaultChecked = dest.checked = src.checked;
                // IE6-7 get confused and end up setting the value of a cloned
                // checkbox/radio button to an empty string instead of "on"
                if (dest.value !== src.value) {
                    "dk.brics.tajs.directives.unreachable";
                    dest.value = src.value;
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (nodeName === "option") {
                    "dk.brics.tajs.directives.unreachable";
                    dest.selected = src.defaultSelected;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (nodeName === "input" || nodeName === "textarea") {
                        "dk.brics.tajs.directives.unreachable";
                        dest.defaultValue = src.defaultValue;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (nodeName === "script" && dest.text !== src.text) {
                            "dk.brics.tajs.directives.unreachable";
                            dest.text = src.text;
                        }
                    }
                }
            }
        }
        // Event data gets referenced instead of copied if the expando
        // gets copied too
        dest.removeAttribute(jQuery.expando);
    }
    jQuery.buildFragment = function(args, context, scripts) {
        "dk.brics.tajs.directives.unreachable";
        var fragment, cacheable, cachehit, first = args[0];
        // Set context from what may come in as undefined or a jQuery collection or a node
        context = context || document;
        context = (context[0] || context).ownerDocument || context[0] || context;
        // Ensure that an attr object doesn't incorrectly stand in as a document object
        // Chrome and Firefox seem to allow this to occur and will throw exception
        // Fixes #8950
        if (typeof context.createDocumentFragment === "undefined") {
            "dk.brics.tajs.directives.unreachable";
            context = document;
        }
        // Only cache "small" (1/2 KB) HTML strings that are associated with the main document
        // Cloning options loses the selected state, so don't cache them
        // IE 6 doesn't like it when you put <object> or <embed> elements in a fragment
        // Also, WebKit does not clone 'checked' attributes on cloneNode, so don't cache
        // Lastly, IE6,7,8 will not correctly reuse cached fragments that were created from unknown elems #10501
        if (args.length === 1 && typeof first === "string" && first.length < 512 && context === document && first.charAt(0) === "<" && !rnocache.test(first) && (jQuery.support.checkClone || !rchecked.test(first)) && (jQuery.support.html5Clone || !rnoshimcache.test(first))) {
            "dk.brics.tajs.directives.unreachable";
            // Mark cacheable and look for a hit
            cacheable = true;
            fragment = jQuery.fragments[first];
            cachehit = fragment !== undefined;
        }
        if (!fragment) {
            "dk.brics.tajs.directives.unreachable";
            fragment = context.createDocumentFragment();
            jQuery.clean(args, context, fragment, scripts);
            // Update the cache, but only store false
            // unless this is a second parsing of the same content
            if (cacheable) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.fragments[first] = cachehit && fragment;
            }
        }
        return {
            fragment: fragment,
            cacheable: cacheable
        };
    };
    jQuery.fragments = {};
    jQuery.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
    }, function(name, original) {
        jQuery.fn[name] = function(selector) {
            "dk.brics.tajs.directives.unreachable";
            var elems, i = 0, ret = [], insert = jQuery(selector), l = insert.length, parent = this.length === 1 && this[0].parentNode;
            if ((parent == null || parent && parent.nodeType === 11 && parent.childNodes.length === 1) && l === 1) {
                "dk.brics.tajs.directives.unreachable";
                insert[original](this[0]);
                return this;
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (;i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elems = (i > 0 ? this.clone(true) : this).get();
                    jQuery(insert[i])[original](elems);
                    ret = ret.concat(elems);
                }
                return this.pushStack(ret, name, insert.selector);
            }
        };
    });
    function getAll(elem) {
        "dk.brics.tajs.directives.unreachable";
        if (typeof elem.getElementsByTagName !== "undefined") {
            "dk.brics.tajs.directives.unreachable";
            return elem.getElementsByTagName("*");
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (typeof elem.querySelectorAll !== "undefined") {
                "dk.brics.tajs.directives.unreachable";
                return elem.querySelectorAll("*");
            } else {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
        }
    }
    // Used in clean, fixes the defaultChecked property
    function fixDefaultChecked(elem) {
        "dk.brics.tajs.directives.unreachable";
        if (rcheckableType.test(elem.type)) {
            "dk.brics.tajs.directives.unreachable";
            elem.defaultChecked = elem.checked;
        }
    }
    jQuery.extend({
        clone: function(elem, dataAndEvents, deepDataAndEvents) {
            "dk.brics.tajs.directives.unreachable";
            var srcElements, destElements, i, clone;
            if (jQuery.support.html5Clone || jQuery.isXMLDoc(elem) || !rnoshimcache.test("<" + elem.nodeName + ">")) {
                "dk.brics.tajs.directives.unreachable";
                clone = elem.cloneNode(true);
            } else {
                "dk.brics.tajs.directives.unreachable";
                fragmentDiv.innerHTML = elem.outerHTML;
                fragmentDiv.removeChild(clone = fragmentDiv.firstChild);
            }
            if ((!jQuery.support.noCloneEvent || !jQuery.support.noCloneChecked) && (elem.nodeType === 1 || elem.nodeType === 11) && !jQuery.isXMLDoc(elem)) {
                "dk.brics.tajs.directives.unreachable";
                // IE copies events bound via attachEvent when using cloneNode.
                // Calling detachEvent on the clone will also remove the events
                // from the original. In order to get around this, we use some
                // proprietary methods to clear the events. Thanks to MooTools
                // guys for this hotness.
                cloneFixAttributes(elem, clone);
                // Using Sizzle here is crazy slow, so we use getElementsByTagName instead
                srcElements = getAll(elem);
                destElements = getAll(clone);
                // Weird iteration because IE will replace the length property
                // with an element if you are cloning the body and one of the
                // elements on the page has a name or id of "length"
                for (i = 0; srcElements[i]; ++i) {
                    "dk.brics.tajs.directives.unreachable";
                    // Ensure that the destination node is not null; Fixes #9587
                    if (destElements[i]) {
                        "dk.brics.tajs.directives.unreachable";
                        cloneFixAttributes(srcElements[i], destElements[i]);
                    }
                }
            }
            // Copy the events from the original to the clone
            if (dataAndEvents) {
                "dk.brics.tajs.directives.unreachable";
                cloneCopyEvent(elem, clone);
                if (deepDataAndEvents) {
                    "dk.brics.tajs.directives.unreachable";
                    srcElements = getAll(elem);
                    destElements = getAll(clone);
                    for (i = 0; srcElements[i]; ++i) {
                        "dk.brics.tajs.directives.unreachable";
                        cloneCopyEvent(srcElements[i], destElements[i]);
                    }
                }
            }
            srcElements = destElements = null;
            // Return the cloned set
            return clone;
        },
        clean: function(elems, context, fragment, scripts) {
            "dk.brics.tajs.directives.unreachable";
            var j, safe, elem, tag, wrap, depth, div, hasBody, tbody, len, handleScript, jsTags, i = 0, ret = [];
            // Ensure that context is a document
            if (!context || typeof context.createDocumentFragment === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                context = document;
            }
            // Use the already-created safe fragment if context permits
            for (safe = context === document && safeFragment; (elem = elems[i]) != null; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (typeof elem === "number") {
                    "dk.brics.tajs.directives.unreachable";
                    elem += "";
                }
                if (!elem) {
                    "dk.brics.tajs.directives.unreachable";
                    continue;
                }
                // Convert html string into DOM nodes
                if (typeof elem === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    if (!rhtml.test(elem)) {
                        "dk.brics.tajs.directives.unreachable";
                        elem = context.createTextNode(elem);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        // Ensure a safe container in which to render the html
                        safe = safe || createSafeFragment(context);
                        div = div || safe.appendChild(context.createElement("div"));
                        // Fix "XHTML"-style tags in all browsers
                        elem = elem.replace(rxhtmlTag, "<$1></$2>");
                        // Go to html and back, then peel off extra wrappers
                        tag = (rtagName.exec(elem) || [ "", "" ])[1].toLowerCase();
                        wrap = wrapMap[tag] || wrapMap._default;
                        depth = wrap[0];
                        div.innerHTML = wrap[1] + elem + wrap[2];
                        // Move to the right depth
                        while (depth--) {
                            "dk.brics.tajs.directives.unreachable";
                            div = div.lastChild;
                        }
                        // Remove IE's autoinserted <tbody> from table fragments
                        if (!jQuery.support.tbody) {
                            "dk.brics.tajs.directives.unreachable";
                            // String was a <table>, *may* have spurious <tbody>
                            hasBody = rtbody.test(elem);
                            tbody = tag === "table" && !hasBody ? div.firstChild && div.firstChild.childNodes : // String was a bare <thead> or <tfoot>
                            wrap[1] === "<table>" && !hasBody ? div.childNodes : [];
                            for (j = tbody.length - 1; j >= 0; --j) {
                                "dk.brics.tajs.directives.unreachable";
                                if (jQuery.nodeName(tbody[j], "tbody") && !tbody[j].childNodes.length) {
                                    "dk.brics.tajs.directives.unreachable";
                                    tbody[j].parentNode.removeChild(tbody[j]);
                                }
                            }
                        }
                        // IE completely kills leading whitespace when innerHTML is used
                        if (!jQuery.support.leadingWhitespace && rleadingWhitespace.test(elem)) {
                            "dk.brics.tajs.directives.unreachable";
                            div.insertBefore(context.createTextNode(rleadingWhitespace.exec(elem)[0]), div.firstChild);
                        }
                        elem = div.childNodes;
                        // Remember the top-level container for proper cleanup
                        div = safe.lastChild;
                    }
                }
                if (elem.nodeType) {
                    "dk.brics.tajs.directives.unreachable";
                    ret.push(elem);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    ret = jQuery.merge(ret, elem);
                }
            }
            // Fix #11356: Clear elements from safeFragment
            if (div) {
                "dk.brics.tajs.directives.unreachable";
                safe.removeChild(div);
                elem = div = safe = null;
            }
            // Reset defaultChecked for any radios and checkboxes
            // about to be appended to the DOM in IE 6/7 (#8060)
            if (!jQuery.support.appendChecked) {
                "dk.brics.tajs.directives.unreachable";
                for (i = 0; (elem = ret[i]) != null; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.nodeName(elem, "input")) {
                        "dk.brics.tajs.directives.unreachable";
                        fixDefaultChecked(elem);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (typeof elem.getElementsByTagName !== "undefined") {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.grep(elem.getElementsByTagName("input"), fixDefaultChecked);
                        }
                    }
                }
            }
            // Append elements to a provided document fragment
            if (fragment) {
                "dk.brics.tajs.directives.unreachable";
                // Special handling of each script element
                handleScript = function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // Check if we consider it executable
                    if (!elem.type || rscriptType.test(elem.type)) {
                        "dk.brics.tajs.directives.unreachable";
                        // Detach the script and store it in the scripts array (if provided) or the fragment
                        // Return truthy to indicate that it has been handled
                        return scripts ? scripts.push(elem.parentNode ? elem.parentNode.removeChild(elem) : elem) : fragment.appendChild(elem);
                    }
                };
                for (i = 0; (elem = ret[i]) != null; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    // Check if we're done after handling an executable script
                    if (!(jQuery.nodeName(elem, "script") && handleScript(elem))) {
                        "dk.brics.tajs.directives.unreachable";
                        // Append to fragment and handle embedded scripts
                        fragment.appendChild(elem);
                        if (typeof elem.getElementsByTagName !== "undefined") {
                            "dk.brics.tajs.directives.unreachable";
                            // handleScript alters the DOM, so use jQuery.merge to ensure snapshot iteration
                            jsTags = jQuery.grep(jQuery.merge([], elem.getElementsByTagName("script")), handleScript);
                            // Splice the scripts into ret after their former ancestor and advance our index beyond them
                            ret.splice.apply(ret, [ i + 1, 0 ].concat(jsTags));
                            i += jsTags.length;
                        }
                    }
                }
            }
            return ret;
        },
        cleanData: function(elems, /* internal */ acceptData) {
            "dk.brics.tajs.directives.unreachable";
            var data, id, elem, type, i = 0, internalKey = jQuery.expando, cache = jQuery.cache, deleteExpando = jQuery.support.deleteExpando, special = jQuery.event.special;
            for (;(elem = elems[i]) != null; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (acceptData || jQuery.acceptData(elem)) {
                    "dk.brics.tajs.directives.unreachable";
                    id = elem[internalKey];
                    data = id && cache[id];
                    if (data) {
                        "dk.brics.tajs.directives.unreachable";
                        if (data.events) {
                            "dk.brics.tajs.directives.unreachable";
                            for (type in data.events) {
                                "dk.brics.tajs.directives.unreachable";
                                if (special[type]) {
                                    "dk.brics.tajs.directives.unreachable";
                                    jQuery.event.remove(elem, type);
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    jQuery.removeEvent(elem, type, data.handle);
                                }
                            }
                        }
                        // Remove cache only if it was not already removed by jQuery.event.remove
                        if (cache[id]) {
                            "dk.brics.tajs.directives.unreachable";
                            delete cache[id];
                            // IE does not allow us to delete expando properties from nodes,
                            // nor does it have a removeAttribute function on Document nodes;
                            // we must handle all of these cases
                            if (deleteExpando) {
                                "dk.brics.tajs.directives.unreachable";
                                delete elem[internalKey];
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (elem.removeAttribute) {
                                    "dk.brics.tajs.directives.unreachable";
                                    elem.removeAttribute(internalKey);
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    elem[internalKey] = null;
                                }
                            }
                            jQuery.deletedIds.push(id);
                        }
                    }
                }
            }
        }
    });
    // Limit scope pollution from any deprecated API
    (function() {
        var matched, browser;
        // Use of jQuery.browser is frowned upon.
        // More details: http://api.jquery.com/jQuery.browser
        // jQuery.uaMatch maintained for back-compat
        jQuery.uaMatch = function(ua) {
            ua = ua.toLowerCase();
            var match = /(chrome)[ \/]([\w.]+)/.exec(ua) || /(webkit)[ \/]([\w.]+)/.exec(ua) || /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) || /(msie) ([\w.]+)/.exec(ua) || ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) || [];
            return {
                browser: match[1] || "",
                version: match[2] || "0"
            };
        };
        matched = jQuery.uaMatch(navigator.userAgent);
        browser = {};
        if (matched.browser) {
            browser[matched.browser] = true;
            browser.version = matched.version;
        }
        // Deprecated, use jQuery.browser.webkit instead
        // Maintained for back-compat only
        if (browser.webkit) {
            "dk.brics.tajs.directives.unreachable";
            browser.safari = true;
        }
        jQuery.browser = browser;
        jQuery.sub = function() {
            "dk.brics.tajs.directives.unreachable";
            function jQuerySub(selector, context) {
                "dk.brics.tajs.directives.unreachable";
                return new jQuerySub.fn.init(selector, context);
            }
            jQuery.extend(true, jQuerySub, this);
            jQuerySub.superclass = this;
            jQuerySub.fn = jQuerySub.prototype = this();
            jQuerySub.fn.constructor = jQuerySub;
            jQuerySub.sub = this.sub;
            jQuerySub.fn.init = function init(selector, context) {
                "dk.brics.tajs.directives.unreachable";
                if (context && context instanceof jQuery && !(context instanceof jQuerySub)) {
                    "dk.brics.tajs.directives.unreachable";
                    context = jQuerySub(context);
                }
                return jQuery.fn.init.call(this, selector, context, rootjQuerySub);
            };
            jQuerySub.fn.init.prototype = jQuerySub.fn;
            var rootjQuerySub = jQuerySub(document);
            return jQuerySub;
        };
    })();
    var curCSS, iframe, iframeDoc, ralpha = /alpha\([^)]*\)/i, ropacity = /opacity=([^)]*)/, rposition = /^(top|right|bottom|left)$/, rmargin = /^margin/, rnumsplit = new RegExp("^(" + core_pnum + ")(.*)$", "i"), rnumnonpx = new RegExp("^(" + core_pnum + ")(?!px)[a-z%]+$", "i"), rrelNum = new RegExp("^([-+])=(" + core_pnum + ")", "i"), elemdisplay = {}, cssShow = {
        position: "absolute",
        visibility: "hidden",
        display: "block"
    }, cssNormalTransform = {
        letterSpacing: 0,
        fontWeight: 400,
        lineHeight: 1
    }, cssExpand = [ "Top", "Right", "Bottom", "Left" ], cssPrefixes = [ "Webkit", "O", "Moz", "ms" ], eventsToggle = jQuery.fn.toggle;
    // return a css property mapped to a potentially vendor prefixed property
    function vendorPropName(style, name) {
        "dk.brics.tajs.directives.unreachable";
        // shortcut for names that are not vendor prefixed
        if (name in style) {
            "dk.brics.tajs.directives.unreachable";
            return name;
        }
        // check for vendor prefixed names
        var capName = name.charAt(0).toUpperCase() + name.slice(1), origName = name, i = cssPrefixes.length;
        while (i--) {
            "dk.brics.tajs.directives.unreachable";
            name = cssPrefixes[i] + capName;
            if (name in style) {
                "dk.brics.tajs.directives.unreachable";
                return name;
            }
        }
        return origName;
    }
    function isHidden(elem, el) {
        "dk.brics.tajs.directives.unreachable";
        elem = el || elem;
        return jQuery.css(elem, "display") === "none" || !jQuery.contains(elem.ownerDocument, elem);
    }
    function showHide(elements, show) {
        "dk.brics.tajs.directives.unreachable";
        var elem, display, values = [], index = 0, length = elements.length;
        for (;index < length; index++) {
            "dk.brics.tajs.directives.unreachable";
            elem = elements[index];
            if (!elem.style) {
                "dk.brics.tajs.directives.unreachable";
                continue;
            }
            values[index] = jQuery._data(elem, "olddisplay");
            if (show) {
                "dk.brics.tajs.directives.unreachable";
                // Reset the inline display of this element to learn if it is
                // being hidden by cascaded rules or not
                if (!values[index] && elem.style.display === "none") {
                    "dk.brics.tajs.directives.unreachable";
                    elem.style.display = "";
                }
                // Set elements which have been overridden with display: none
                // in a stylesheet to whatever the default browser style is
                // for such an element
                if (elem.style.display === "" && isHidden(elem)) {
                    "dk.brics.tajs.directives.unreachable";
                    values[index] = jQuery._data(elem, "olddisplay", css_defaultDisplay(elem.nodeName));
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                display = curCSS(elem, "display");
                if (!values[index] && display !== "none") {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery._data(elem, "olddisplay", display);
                }
            }
        }
        // Set the display of most of the elements in a second loop
        // to avoid the constant reflow
        for (index = 0; index < length; index++) {
            "dk.brics.tajs.directives.unreachable";
            elem = elements[index];
            if (!elem.style) {
                "dk.brics.tajs.directives.unreachable";
                continue;
            }
            if (!show || elem.style.display === "none" || elem.style.display === "") {
                "dk.brics.tajs.directives.unreachable";
                elem.style.display = show ? values[index] || "" : "none";
            }
        }
        return elements;
    }
    jQuery.fn.extend({
        css: function(name, value) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, function(elem, name, value) {
                "dk.brics.tajs.directives.unreachable";
                return value !== undefined ? jQuery.style(elem, name, value) : jQuery.css(elem, name);
            }, name, value, arguments.length > 1);
        },
        show: function() {
            "dk.brics.tajs.directives.unreachable";
            return showHide(this, true);
        },
        hide: function() {
            "dk.brics.tajs.directives.unreachable";
            return showHide(this);
        },
        toggle: function(state, fn2) {
            "dk.brics.tajs.directives.unreachable";
            var bool = typeof state === "boolean";
            if (jQuery.isFunction(state) && jQuery.isFunction(fn2)) {
                "dk.brics.tajs.directives.unreachable";
                return eventsToggle.apply(this, arguments);
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (bool ? state : isHidden(this)) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).show();
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).hide();
                }
            });
        }
    });
    jQuery.extend({
        // Add in style property hooks for overriding the default
        // behavior of getting and setting a style property
        cssHooks: {
            opacity: {
                get: function(elem, computed) {
                    "dk.brics.tajs.directives.unreachable";
                    if (computed) {
                        "dk.brics.tajs.directives.unreachable";
                        // We should always get a number back from opacity
                        var ret = curCSS(elem, "opacity");
                        return ret === "" ? "1" : ret;
                    }
                }
            }
        },
        // Exclude the following css properties to add px
        cssNumber: {
            fillOpacity: true,
            fontWeight: true,
            lineHeight: true,
            opacity: true,
            orphans: true,
            widows: true,
            zIndex: true,
            zoom: true
        },
        // Add in properties whose names you wish to fix before
        // setting or getting the value
        cssProps: {
            // normalize float css property
            "float": jQuery.support.cssFloat ? "cssFloat" : "styleFloat"
        },
        // Get and set the style property on a DOM Node
        style: function(elem, name, value, extra) {
            "dk.brics.tajs.directives.unreachable";
            // Don't set styles on text and comment nodes
            if (!elem || elem.nodeType === 3 || elem.nodeType === 8 || !elem.style) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Make sure that we're working with the right name
            var ret, type, hooks, origName = jQuery.camelCase(name), style = elem.style;
            name = jQuery.cssProps[origName] || (jQuery.cssProps[origName] = vendorPropName(style, origName));
            // gets hook for the prefixed version
            // followed by the unprefixed version
            hooks = jQuery.cssHooks[name] || jQuery.cssHooks[origName];
            // Check if we're setting a value
            if (value !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                type = typeof value;
                // convert relative number strings (+= or -=) to relative numbers. #7345
                if (type === "string" && (ret = rrelNum.exec(value))) {
                    "dk.brics.tajs.directives.unreachable";
                    value = (ret[1] + 1) * ret[2] + parseFloat(jQuery.css(elem, name));
                    // Fixes bug #9237
                    type = "number";
                }
                // Make sure that NaN and null values aren't set. See: #7116
                if (value == null || type === "number" && isNaN(value)) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                // If a number was passed in, add 'px' to the (except for certain CSS properties)
                if (type === "number" && !jQuery.cssNumber[origName]) {
                    "dk.brics.tajs.directives.unreachable";
                    value += "px";
                }
                // If a hook was provided, use that value, otherwise just set the specified value
                if (!hooks || !("set" in hooks) || (value = hooks.set(elem, value, extra)) !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    // Wrapped to prevent IE from throwing errors when 'invalid' values are provided
                    // Fixes bug #5509
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        style[name] = value;
                    } catch (e) {}
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                // If a hook was provided get the non-computed value from there
                if (hooks && "get" in hooks && (ret = hooks.get(elem, false, extra)) !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    return ret;
                }
                // Otherwise just get the value from the style object
                return style[name];
            }
        },
        css: function(elem, name, numeric, extra) {
            "dk.brics.tajs.directives.unreachable";
            var val, num, hooks, origName = jQuery.camelCase(name);
            // Make sure that we're working with the right name
            name = jQuery.cssProps[origName] || (jQuery.cssProps[origName] = vendorPropName(elem.style, origName));
            // gets hook for the prefixed version
            // followed by the unprefixed version
            hooks = jQuery.cssHooks[name] || jQuery.cssHooks[origName];
            // If a hook was provided get the computed value from there
            if (hooks && "get" in hooks) {
                "dk.brics.tajs.directives.unreachable";
                val = hooks.get(elem, true, extra);
            }
            // Otherwise, if a way to get the computed value exists, use that
            if (val === undefined) {
                "dk.brics.tajs.directives.unreachable";
                val = curCSS(elem, name);
            }
            //convert "normal" to computed value
            if (val === "normal" && name in cssNormalTransform) {
                "dk.brics.tajs.directives.unreachable";
                val = cssNormalTransform[name];
            }
            // Return, converting to number if forced or a qualifier was provided and val looks numeric
            if (numeric || extra !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                num = parseFloat(val);
                return numeric || jQuery.isNumeric(num) ? num || 0 : val;
            }
            return val;
        },
        // A method for quickly swapping in/out CSS properties to get correct calculations
        swap: function(elem, options, callback) {
            "dk.brics.tajs.directives.unreachable";
            var ret, name, old = {};
            // Remember the old values, and insert the new ones
            for (name in options) {
                "dk.brics.tajs.directives.unreachable";
                old[name] = elem.style[name];
                elem.style[name] = options[name];
            }
            ret = callback.call(elem);
            // Revert the old values
            for (name in options) {
                "dk.brics.tajs.directives.unreachable";
                elem.style[name] = old[name];
            }
            return ret;
        }
    });
    // NOTE: To any future maintainer, we've used both window.getComputedStyle
    // and getComputedStyle here to produce a better gzip size
    if (window.getComputedStyle) {
        curCSS = function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            var ret, width, minWidth, maxWidth, computed = getComputedStyle(elem, null), style = elem.style;
            if (computed) {
                "dk.brics.tajs.directives.unreachable";
                ret = computed[name];
                if (ret === "" && !jQuery.contains(elem.ownerDocument.documentElement, elem)) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = jQuery.style(elem, name);
                }
                // A tribute to the "awesome hack by Dean Edwards"
                // Chrome < 17 and Safari 5.0 uses "computed value" instead of "used value" for margin-right
                // Safari 5.1.7 (at least) returns percentage for a larger set of values, but width seems to be reliably pixels
                // this is against the CSSOM draft spec: http://dev.w3.org/csswg/cssom/#resolved-values
                if (rnumnonpx.test(ret) && rmargin.test(name)) {
                    "dk.brics.tajs.directives.unreachable";
                    width = style.width;
                    minWidth = style.minWidth;
                    maxWidth = style.maxWidth;
                    style.minWidth = style.maxWidth = style.width = ret;
                    ret = computed.width;
                    style.width = width;
                    style.minWidth = minWidth;
                    style.maxWidth = maxWidth;
                }
            }
            return ret;
        };
    } else {
        "dk.brics.tajs.directives.unreachable";
        if (document.documentElement.currentStyle) {
            "dk.brics.tajs.directives.unreachable";
            curCSS = function(elem, name) {
                "dk.brics.tajs.directives.unreachable";
                var left, rsLeft, ret = elem.currentStyle && elem.currentStyle[name], style = elem.style;
                // Avoid setting ret to empty string here
                // so we don't default to auto
                if (ret == null && style && style[name]) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = style[name];
                }
                // From the awesome hack by Dean Edwards
                // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
                // If we're not dealing with a regular pixel number
                // but a number that has a weird ending, we need to convert it to pixels
                // but not position css attributes, as those are proportional to the parent element instead
                // and we can't measure the parent instead because it might trigger a "stacking dolls" problem
                if (rnumnonpx.test(ret) && !rposition.test(name)) {
                    "dk.brics.tajs.directives.unreachable";
                    // Remember the original values
                    left = style.left;
                    rsLeft = elem.runtimeStyle && elem.runtimeStyle.left;
                    // Put in the new values to get a computed value out
                    if (rsLeft) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.runtimeStyle.left = elem.currentStyle.left;
                    }
                    style.left = name === "fontSize" ? "1em" : ret;
                    ret = style.pixelLeft + "px";
                    // Revert the changed values
                    style.left = left;
                    if (rsLeft) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.runtimeStyle.left = rsLeft;
                    }
                }
                return ret === "" ? "auto" : ret;
            };
        }
    }
    function setPositiveNumber(elem, value, subtract) {
        "dk.brics.tajs.directives.unreachable";
        var matches = rnumsplit.exec(value);
        return matches ? Math.max(0, matches[1] - (subtract || 0)) + (matches[2] || "px") : value;
    }
    function augmentWidthOrHeight(elem, name, extra, isBorderBox) {
        "dk.brics.tajs.directives.unreachable";
        var i = extra === (isBorderBox ? "border" : "content") ? // If we already have the right measurement, avoid augmentation
        4 : // Otherwise initialize for horizontal or vertical properties
        name === "width" ? 1 : 0, val = 0;
        for (;i < 4; i += 2) {
            "dk.brics.tajs.directives.unreachable";
            // both box models exclude margin, so add it if we want it
            if (extra === "margin") {
                "dk.brics.tajs.directives.unreachable";
                // we use jQuery.css instead of curCSS here
                // because of the reliableMarginRight CSS hook!
                val += jQuery.css(elem, extra + cssExpand[i], true);
            }
            // From this point on we use curCSS for maximum performance (relevant in animations)
            if (isBorderBox) {
                "dk.brics.tajs.directives.unreachable";
                // border-box includes padding, so remove it if we want content
                if (extra === "content") {
                    "dk.brics.tajs.directives.unreachable";
                    val -= parseFloat(curCSS(elem, "padding" + cssExpand[i])) || 0;
                }
                // at this point, extra isn't border nor margin, so remove border
                if (extra !== "margin") {
                    "dk.brics.tajs.directives.unreachable";
                    val -= parseFloat(curCSS(elem, "border" + cssExpand[i] + "Width")) || 0;
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                // at this point, extra isn't content, so add padding
                val += parseFloat(curCSS(elem, "padding" + cssExpand[i])) || 0;
                // at this point, extra isn't content nor padding, so add border
                if (extra !== "padding") {
                    "dk.brics.tajs.directives.unreachable";
                    val += parseFloat(curCSS(elem, "border" + cssExpand[i] + "Width")) || 0;
                }
            }
        }
        return val;
    }
    function getWidthOrHeight(elem, name, extra) {
        "dk.brics.tajs.directives.unreachable";
        // Start with offset property, which is equivalent to the border-box value
        var val = name === "width" ? elem.offsetWidth : elem.offsetHeight, valueIsBorderBox = true, isBorderBox = jQuery.support.boxSizing && jQuery.css(elem, "boxSizing") === "border-box";
        if (val <= 0) {
            "dk.brics.tajs.directives.unreachable";
            // Fall back to computed then uncomputed css if necessary
            val = curCSS(elem, name);
            if (val < 0 || val == null) {
                "dk.brics.tajs.directives.unreachable";
                val = elem.style[name];
            }
            // Computed unit is not pixels. Stop here and return.
            if (rnumnonpx.test(val)) {
                "dk.brics.tajs.directives.unreachable";
                return val;
            }
            // we need the check for style in case a browser which returns unreliable values
            // for getComputedStyle silently falls back to the reliable elem.style
            valueIsBorderBox = isBorderBox && (jQuery.support.boxSizingReliable || val === elem.style[name]);
            // Normalize "", auto, and prepare for extra
            val = parseFloat(val) || 0;
        }
        // use the active box-sizing model to add/subtract irrelevant styles
        return val + augmentWidthOrHeight(elem, name, extra || (isBorderBox ? "border" : "content"), valueIsBorderBox) + "px";
    }
    // Try to determine the default display value of an element
    function css_defaultDisplay(nodeName) {
        "dk.brics.tajs.directives.unreachable";
        if (elemdisplay[nodeName]) {
            "dk.brics.tajs.directives.unreachable";
            return elemdisplay[nodeName];
        }
        var elem = jQuery("<" + nodeName + ">").appendTo(document.body), display = elem.css("display");
        elem.remove();
        // If the simple way fails,
        // get element's real default display by attaching it to a temp iframe
        if (display === "none" || display === "") {
            "dk.brics.tajs.directives.unreachable";
            // Use the already-created iframe if possible
            iframe = document.body.appendChild(iframe || jQuery.extend(document.createElement("iframe"), {
                frameBorder: 0,
                width: 0,
                height: 0
            }));
            // Create a cacheable copy of the iframe document on first call.
            // IE and Opera will allow us to reuse the iframeDoc without re-writing the fake HTML
            // document to it; WebKit & Firefox won't allow reusing the iframe document.
            if (!iframeDoc || !iframe.createElement) {
                "dk.brics.tajs.directives.unreachable";
                iframeDoc = (iframe.contentWindow || iframe.contentDocument).document;
                iframeDoc.write("<!doctype html><html><body>");
                iframeDoc.close();
            }
            elem = iframeDoc.body.appendChild(iframeDoc.createElement(nodeName));
            display = curCSS(elem, "display");
            document.body.removeChild(iframe);
        }
        // Store the correct default display
        elemdisplay[nodeName] = display;
        return display;
    }
    jQuery.each([ "height", "width" ], function(i, name) {
        jQuery.cssHooks[name] = {
            get: function(elem, computed, extra) {
                "dk.brics.tajs.directives.unreachable";
                if (computed) {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.offsetWidth !== 0 || curCSS(elem, "display") !== "none") {
                        "dk.brics.tajs.directives.unreachable";
                        return getWidthOrHeight(elem, name, extra);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        return jQuery.swap(elem, cssShow, function() {
                            "dk.brics.tajs.directives.unreachable";
                            return getWidthOrHeight(elem, name, extra);
                        });
                    }
                }
            },
            set: function(elem, value, extra) {
                "dk.brics.tajs.directives.unreachable";
                return setPositiveNumber(elem, value, extra ? augmentWidthOrHeight(elem, name, extra, jQuery.support.boxSizing && jQuery.css(elem, "boxSizing") === "border-box") : 0);
            }
        };
    });
    if (!jQuery.support.opacity) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.cssHooks.opacity = {
            get: function(elem, computed) {
                "dk.brics.tajs.directives.unreachable";
                // IE uses filters for opacity
                return ropacity.test((computed && elem.currentStyle ? elem.currentStyle.filter : elem.style.filter) || "") ? .01 * parseFloat(RegExp.$1) + "" : computed ? "1" : "";
            },
            set: function(elem, value) {
                "dk.brics.tajs.directives.unreachable";
                var style = elem.style, currentStyle = elem.currentStyle, opacity = jQuery.isNumeric(value) ? "alpha(opacity=" + value * 100 + ")" : "", filter = currentStyle && currentStyle.filter || style.filter || "";
                // IE has trouble with opacity if it does not have layout
                // Force it by setting the zoom level
                style.zoom = 1;
                // if setting opacity to 1, and no other filters exist - attempt to remove filter attribute #6652
                if (value >= 1 && jQuery.trim(filter.replace(ralpha, "")) === "" && style.removeAttribute) {
                    "dk.brics.tajs.directives.unreachable";
                    // Setting style.filter to null, "" & " " still leave "filter:" in the cssText
                    // if "filter:" is present at all, clearType is disabled, we want to avoid this
                    // style.removeAttribute is IE Only, but so apparently is this code path...
                    style.removeAttribute("filter");
                    // if there there is no filter style applied in a css rule, we are done
                    if (currentStyle && !currentStyle.filter) {
                        "dk.brics.tajs.directives.unreachable";
                        return;
                    }
                }
                // otherwise, set new filter values
                style.filter = ralpha.test(filter) ? filter.replace(ralpha, opacity) : filter + " " + opacity;
            }
        };
    }
    // These hooks cannot be added until DOM ready because the support test
    // for it is not run until after DOM ready
    jQuery(function() {
        if (!jQuery.support.reliableMarginRight) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.cssHooks.marginRight = {
                get: function(elem, computed) {
                    "dk.brics.tajs.directives.unreachable";
                    // WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
                    // Work around by temporarily setting element display to inline-block
                    return jQuery.swap(elem, {
                        display: "inline-block"
                    }, function() {
                        "dk.brics.tajs.directives.unreachable";
                        if (computed) {
                            "dk.brics.tajs.directives.unreachable";
                            return curCSS(elem, "marginRight");
                        }
                    });
                }
            };
        }
        // Webkit bug: https://bugs.webkit.org/show_bug.cgi?id=29084
        // getComputedStyle returns percent when specified for top/left/bottom/right
        // rather than make the css module depend on the offset module, we just check for it here
        if (!jQuery.support.pixelPosition && jQuery.fn.position) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.each([ "top", "left" ], function(i, prop) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.cssHooks[prop] = {
                    get: function(elem, computed) {
                        "dk.brics.tajs.directives.unreachable";
                        if (computed) {
                            "dk.brics.tajs.directives.unreachable";
                            var ret = curCSS(elem, prop);
                            // if curCSS returns percentage, fallback to offset
                            return rnumnonpx.test(ret) ? jQuery(elem).position()[prop] + "px" : ret;
                        }
                    }
                };
            });
        }
    });
    if (jQuery.expr && jQuery.expr.filters) {
        jQuery.expr.filters.hidden = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return elem.offsetWidth === 0 && elem.offsetHeight === 0 || !jQuery.support.reliableHiddenOffsets && (elem.style && elem.style.display || curCSS(elem, "display")) === "none";
        };
        jQuery.expr.filters.visible = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return !jQuery.expr.filters.hidden(elem);
        };
    }
    // These hooks are used by animate to expand properties
    jQuery.each({
        margin: "",
        padding: "",
        border: "Width"
    }, function(prefix, suffix) {
        jQuery.cssHooks[prefix + suffix] = {
            expand: function(value) {
                "dk.brics.tajs.directives.unreachable";
                var i, // assumes a single number if not a string
                parts = typeof value === "string" ? value.split(" ") : [ value ], expanded = {};
                for (i = 0; i < 4; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    expanded[prefix + cssExpand[i] + suffix] = parts[i] || parts[i - 2] || parts[0];
                }
                return expanded;
            }
        };
        if (!rmargin.test(prefix)) {
            jQuery.cssHooks[prefix + suffix].set = setPositiveNumber;
        }
    });
    var r20 = /%20/g, rbracket = /\[\]$/, rCRLF = /\r?\n/g, rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i, rselectTextarea = /^(?:select|textarea)/i;
    jQuery.fn.extend({
        serialize: function() {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.param(this.serializeArray());
        },
        serializeArray: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.map(function() {
                "dk.brics.tajs.directives.unreachable";
                return this.elements ? jQuery.makeArray(this.elements) : this;
            }).filter(function() {
                "dk.brics.tajs.directives.unreachable";
                return this.name && !this.disabled && (this.checked || rselectTextarea.test(this.nodeName) || rinput.test(this.type));
            }).map(function(i, elem) {
                "dk.brics.tajs.directives.unreachable";
                var val = jQuery(this).val();
                return val == null ? null : jQuery.isArray(val) ? jQuery.map(val, function(val, i) {
                    "dk.brics.tajs.directives.unreachable";
                    return {
                        name: elem.name,
                        value: val.replace(rCRLF, "\r\n")
                    };
                }) : {
                    name: elem.name,
                    value: val.replace(rCRLF, "\r\n")
                };
            }).get();
        }
    });
    //Serialize an array of form elements or a set of
    //key/values into a query string
    jQuery.param = function(a, traditional) {
        "dk.brics.tajs.directives.unreachable";
        var prefix, s = [], add = function(key, value) {
            "dk.brics.tajs.directives.unreachable";
            // If value is a function, invoke it and return its value
            value = jQuery.isFunction(value) ? value() : value == null ? "" : value;
            s[s.length] = encodeURIComponent(key) + "=" + encodeURIComponent(value);
        };
        // Set traditional to true for jQuery <= 1.3.2 behavior.
        if (traditional === undefined) {
            "dk.brics.tajs.directives.unreachable";
            traditional = jQuery.ajaxSettings && jQuery.ajaxSettings.traditional;
        }
        // If an array was passed in, assume that it is an array of form elements.
        if (jQuery.isArray(a) || a.jquery && !jQuery.isPlainObject(a)) {
            "dk.brics.tajs.directives.unreachable";
            // Serialize the form elements
            jQuery.each(a, function() {
                "dk.brics.tajs.directives.unreachable";
                add(this.name, this.value);
            });
        } else {
            "dk.brics.tajs.directives.unreachable";
            // If traditional, encode the "old" way (the way 1.3.2 or older
            // did it), otherwise encode params recursively.
            for (prefix in a) {
                "dk.brics.tajs.directives.unreachable";
                buildParams(prefix, a[prefix], traditional, add);
            }
        }
        // Return the resulting serialization
        return s.join("&").replace(r20, "+");
    };
    function buildParams(prefix, obj, traditional, add) {
        "dk.brics.tajs.directives.unreachable";
        var name;
        if (jQuery.isArray(obj)) {
            "dk.brics.tajs.directives.unreachable";
            // Serialize array item.
            jQuery.each(obj, function(i, v) {
                "dk.brics.tajs.directives.unreachable";
                if (traditional || rbracket.test(prefix)) {
                    "dk.brics.tajs.directives.unreachable";
                    // Treat each array item as a scalar.
                    add(prefix, v);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // If array item is non-scalar (array or object), encode its
                    // numeric index to resolve deserialization ambiguity issues.
                    // Note that rack (as of 1.0.0) can't currently deserialize
                    // nested arrays properly, and attempting to do so may cause
                    // a server error. Possible fixes are to modify rack's
                    // deserialization algorithm or to provide an option or flag
                    // to force array serialization to be shallow.
                    buildParams(prefix + "[" + (typeof v === "object" ? i : "") + "]", v, traditional, add);
                }
            });
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (!traditional && jQuery.type(obj) === "object") {
                "dk.brics.tajs.directives.unreachable";
                // Serialize object item.
                for (name in obj) {
                    "dk.brics.tajs.directives.unreachable";
                    buildParams(prefix + "[" + name + "]", obj[name], traditional, add);
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                // Serialize scalar item.
                add(prefix, obj);
            }
        }
    }
    var // Document location
    ajaxLocation, // Document location segments
    ajaxLocParts, rhash = /#.*$/, rheaders = /^(.*?):[ \t]*([^\r\n]*)\r?$/gm, // IE leaves an \r character at EOL
    // #7653, #8125, #8152: local protocol detection
    rlocalProtocol = /^(?:about|app|app\-storage|.+\-extension|file|res|widget):$/, rnoContent = /^(?:GET|HEAD)$/, rprotocol = /^\/\//, rquery = /\?/, rscript = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, rts = /([?&])_=[^&]*/, rurl = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+)|)|)/, // Keep a copy of the old load method
    _load = jQuery.fn.load, /* Prefilters
	 * 1) They are useful to introduce custom dataTypes (see ajax/jsonp.js for an example)
	 * 2) These are called:
	 *    - BEFORE asking for a transport
	 *    - AFTER param serialization (s.data is a string if s.processData is true)
	 * 3) key is the dataType
	 * 4) the catchall symbol "*" can be used
	 * 5) execution will start with transport dataType and THEN continue down to "*" if needed
	 */
    prefilters = {}, /* Transports bindings
	 * 1) key is the dataType
	 * 2) the catchall symbol "*" can be used
	 * 3) selection will start with transport dataType and THEN go to "*" if needed
	 */
    transports = {}, // Avoid comment-prolog char sequence (#10098); must appease lint and evade compression
    allTypes = [ "*/" ] + [ "*" ];
    // #8138, IE may throw an exception when accessing
    // a field from window.location if document.domain has been set
    try {
        ajaxLocation = location.href;
    } catch (e) {
        "dk.brics.tajs.directives.unreachable";
        // Use the href attribute of an A element
        // since IE will modify it given document.location
        ajaxLocation = document.createElement("a");
        ajaxLocation.href = "";
        ajaxLocation = ajaxLocation.href;
    }
    // Segment location into parts
    ajaxLocParts = rurl.exec(ajaxLocation.toLowerCase()) || [];
    // Base "constructor" for jQuery.ajaxPrefilter and jQuery.ajaxTransport
    function addToPrefiltersOrTransports(structure) {
        // dataTypeExpression is optional and defaults to "*"
        return function(dataTypeExpression, func) {
            if (typeof dataTypeExpression !== "string") {
                func = dataTypeExpression;
                dataTypeExpression = "*";
            }
            var dataType, list, placeBefore, dataTypes = dataTypeExpression.toLowerCase().split(core_rspace), i = 0, length = dataTypes.length;
            if (jQuery.isFunction(func)) {
                // For each dataType in the dataTypeExpression
                for (;i < length; i++) {
                    dataType = dataTypes[i];
                    // We control if we're asked to add before
                    // any existing element
                    placeBefore = /^\+/.test(dataType);
                    if (placeBefore) {
                        "dk.brics.tajs.directives.unreachable";
                        dataType = dataType.substr(1) || "*";
                    }
                    list = structure[dataType] = structure[dataType] || [];
                    // then we add to the structure accordingly
                    list[placeBefore ? "unshift" : "push"](func);
                }
            }
        };
    }
    // Base inspection function for prefilters and transports
    function inspectPrefiltersOrTransports(structure, options, originalOptions, jqXHR, dataType, inspected) {
        "dk.brics.tajs.directives.unreachable";
        dataType = dataType || options.dataTypes[0];
        inspected = inspected || {};
        inspected[dataType] = true;
        var selection, list = structure[dataType], i = 0, length = list ? list.length : 0, executeOnly = structure === prefilters;
        for (;i < length && (executeOnly || !selection); i++) {
            "dk.brics.tajs.directives.unreachable";
            selection = list[i](options, originalOptions, jqXHR);
            // If we got redirected to another dataType
            // we try there if executing only and not done already
            if (typeof selection === "string") {
                "dk.brics.tajs.directives.unreachable";
                if (!executeOnly || inspected[selection]) {
                    "dk.brics.tajs.directives.unreachable";
                    selection = undefined;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    options.dataTypes.unshift(selection);
                    selection = inspectPrefiltersOrTransports(structure, options, originalOptions, jqXHR, selection, inspected);
                }
            }
        }
        // If we're only executing or nothing was selected
        // we try the catchall dataType if not done already
        if ((executeOnly || !selection) && !inspected["*"]) {
            "dk.brics.tajs.directives.unreachable";
            selection = inspectPrefiltersOrTransports(structure, options, originalOptions, jqXHR, "*", inspected);
        }
        // unnecessary when only executing (prefilters)
        // but it'll be ignored by the caller in that case
        return selection;
    }
    // A special extend for ajax options
    // that takes "flat" options (not to be deep extended)
    // Fixes #9887
    function ajaxExtend(target, src) {
        var key, deep, flatOptions = jQuery.ajaxSettings.flatOptions || {};
        for (key in src) {
            if (src[key] !== undefined) {
                (flatOptions[key] ? target : deep || (deep = {}))[key] = src[key];
            }
        }
        if (deep) {
            jQuery.extend(true, target, deep);
        }
    }
    jQuery.fn.load = function(url, params, callback) {
        "dk.brics.tajs.directives.unreachable";
        if (typeof url !== "string" && _load) {
            "dk.brics.tajs.directives.unreachable";
            return _load.apply(this, arguments);
        }
        // Don't do a request if no elements are being requested
        if (!this.length) {
            "dk.brics.tajs.directives.unreachable";
            return this;
        }
        var selector, type, response, self = this, off = url.indexOf(" ");
        if (off >= 0) {
            "dk.brics.tajs.directives.unreachable";
            selector = url.slice(off, url.length);
            url = url.slice(0, off);
        }
        // If it's a function
        if (jQuery.isFunction(params)) {
            "dk.brics.tajs.directives.unreachable";
            // We assume that it's the callback
            callback = params;
            params = undefined;
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (typeof params === "object") {
                "dk.brics.tajs.directives.unreachable";
                type = "POST";
            }
        }
        // Request the remote document
        jQuery.ajax({
            url: url,
            // if "type" variable is undefined, then "GET" method will be used
            type: type,
            dataType: "html",
            data: params,
            complete: function(jqXHR, status) {
                "dk.brics.tajs.directives.unreachable";
                if (callback) {
                    "dk.brics.tajs.directives.unreachable";
                    self.each(callback, response || [ jqXHR.responseText, status, jqXHR ]);
                }
            }
        }).done(function(responseText) {
            "dk.brics.tajs.directives.unreachable";
            // Save response for use in complete callback
            response = arguments;
            // See if a selector was specified
            self.html(selector ? // Create a dummy div to hold the results
            jQuery("<div>").append(responseText.replace(rscript, "")).find(selector) : // If not, just inject the full result
            responseText);
        });
        return this;
    };
    // Attach a bunch of functions for handling common AJAX events
    jQuery.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "), function(i, o) {
        jQuery.fn[o] = function(f) {
            "dk.brics.tajs.directives.unreachable";
            return this.on(o, f);
        };
    });
    jQuery.each([ "get", "post" ], function(i, method) {
        jQuery[method] = function(url, data, callback, type) {
            "dk.brics.tajs.directives.unreachable";
            // shift arguments if data argument was omitted
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
                type = type || callback;
                callback = data;
                data = undefined;
            }
            return jQuery.ajax({
                type: method,
                url: url,
                data: data,
                success: callback,
                dataType: type
            });
        };
    });
    jQuery.extend({
        getScript: function(url, callback) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.get(url, undefined, callback, "script");
        },
        getJSON: function(url, data, callback) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.get(url, data, callback, "json");
        },
        // Creates a full fledged settings object into target
        // with both ajaxSettings and settings fields.
        // If target is omitted, writes into ajaxSettings.
        ajaxSetup: function(target, settings) {
            if (settings) {
                "dk.brics.tajs.directives.unreachable";
                // Building a settings object
                ajaxExtend(target, jQuery.ajaxSettings);
            } else {
                // Extending ajaxSettings
                settings = target;
                target = jQuery.ajaxSettings;
            }
            ajaxExtend(target, settings);
            return target;
        },
        ajaxSettings: {
            url: ajaxLocation,
            isLocal: rlocalProtocol.test(ajaxLocParts[1]),
            global: true,
            type: "GET",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            processData: true,
            async: true,
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
                xml: "application/xml, text/xml",
                html: "text/html",
                text: "text/plain",
                json: "application/json, text/javascript",
                "*": allTypes
            },
            contents: {
                xml: /xml/,
                html: /html/,
                json: /json/
            },
            responseFields: {
                xml: "responseXML",
                text: "responseText"
            },
            // List of data converters
            // 1) key format is "source_type destination_type" (a single space in-between)
            // 2) the catchall symbol "*" can be used for source_type
            converters: {
                // Convert anything to text
                "* text": window.String,
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
                context: true,
                url: true
            }
        },
        ajaxPrefilter: addToPrefiltersOrTransports(prefilters),
        ajaxTransport: addToPrefiltersOrTransports(transports),
        // Main method
        ajax: function(url, options) {
            "dk.brics.tajs.directives.unreachable";
            // If url is an object, simulate pre-1.5 signature
            if (typeof url === "object") {
                "dk.brics.tajs.directives.unreachable";
                options = url;
                url = undefined;
            }
            // Force options to be an object
            options = options || {};
            var // ifModified key
            ifModifiedKey, // Response headers
            responseHeadersString, responseHeaders, // transport
            transport, // timeout handle
            timeoutTimer, // Cross-domain detection vars
            parts, // To know if global events are to be dispatched
            fireGlobals, // Loop variable
            i, // Create the final options object
            s = jQuery.ajaxSetup({}, options), // Callbacks context
            callbackContext = s.context || s, // Context for global events
            // It's the callbackContext if one was provided in the options
            // and if it's a DOM node or a jQuery collection
            globalEventContext = callbackContext !== s && (callbackContext.nodeType || callbackContext instanceof jQuery) ? jQuery(callbackContext) : jQuery.event, // Deferreds
            deferred = jQuery.Deferred(), completeDeferred = jQuery.Callbacks("once memory"), // Status-dependent callbacks
            statusCode = s.statusCode || {}, // Headers (they are sent all at once)
            requestHeaders = {}, requestHeadersNames = {}, // The jqXHR state
            state = 0, // Default abort message
            strAbort = "canceled", // Fake xhr
            jqXHR = {
                readyState: 0,
                // Caches the header
                setRequestHeader: function(name, value) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!state) {
                        "dk.brics.tajs.directives.unreachable";
                        var lname = name.toLowerCase();
                        name = requestHeadersNames[lname] = requestHeadersNames[lname] || name;
                        requestHeaders[name] = value;
                    }
                    return this;
                },
                // Raw string
                getAllResponseHeaders: function() {
                    "dk.brics.tajs.directives.unreachable";
                    return state === 2 ? responseHeadersString : null;
                },
                // Builds headers hashtable if needed
                getResponseHeader: function(key) {
                    "dk.brics.tajs.directives.unreachable";
                    var match;
                    if (state === 2) {
                        "dk.brics.tajs.directives.unreachable";
                        if (!responseHeaders) {
                            "dk.brics.tajs.directives.unreachable";
                            responseHeaders = {};
                            while (match = rheaders.exec(responseHeadersString)) {
                                "dk.brics.tajs.directives.unreachable";
                                responseHeaders[match[1].toLowerCase()] = match[2];
                            }
                        }
                        match = responseHeaders[key.toLowerCase()];
                    }
                    return match === undefined ? null : match;
                },
                // Overrides response content-type header
                overrideMimeType: function(type) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!state) {
                        "dk.brics.tajs.directives.unreachable";
                        s.mimeType = type;
                    }
                    return this;
                },
                // Cancel the request
                abort: function(statusText) {
                    "dk.brics.tajs.directives.unreachable";
                    statusText = statusText || strAbort;
                    if (transport) {
                        "dk.brics.tajs.directives.unreachable";
                        transport.abort(statusText);
                    }
                    done(0, statusText);
                    return this;
                }
            };
            // Callback for when everything is done
            // It is defined here because jslint complains if it is declared
            // at the end of the function (which would be more logical and readable)
            function done(status, nativeStatusText, responses, headers) {
                "dk.brics.tajs.directives.unreachable";
                var isSuccess, success, error, response, modified, statusText = nativeStatusText;
                // Called once
                if (state === 2) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                // State is "done" now
                state = 2;
                // Clear timeout if it exists
                if (timeoutTimer) {
                    "dk.brics.tajs.directives.unreachable";
                    clearTimeout(timeoutTimer);
                }
                // Dereference transport for early garbage collection
                // (no matter how long the jqXHR object will be used)
                transport = undefined;
                // Cache response headers
                responseHeadersString = headers || "";
                // Set readyState
                jqXHR.readyState = status > 0 ? 4 : 0;
                // Get response data
                if (responses) {
                    "dk.brics.tajs.directives.unreachable";
                    response = ajaxHandleResponses(s, jqXHR, responses);
                }
                // If successful, handle type chaining
                if (status >= 200 && status < 300 || status === 304) {
                    "dk.brics.tajs.directives.unreachable";
                    // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
                    if (s.ifModified) {
                        "dk.brics.tajs.directives.unreachable";
                        modified = jqXHR.getResponseHeader("Last-Modified");
                        if (modified) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.lastModified[ifModifiedKey] = modified;
                        }
                        modified = jqXHR.getResponseHeader("Etag");
                        if (modified) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.etag[ifModifiedKey] = modified;
                        }
                    }
                    // If not modified
                    if (status === 304) {
                        "dk.brics.tajs.directives.unreachable";
                        statusText = "notmodified";
                        isSuccess = true;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        isSuccess = ajaxConvert(s, response);
                        statusText = isSuccess.state;
                        success = isSuccess.data;
                        error = isSuccess.error;
                        isSuccess = !error;
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // We extract error from statusText
                    // then normalize statusText and status for non-aborts
                    error = statusText;
                    if (!statusText || status) {
                        "dk.brics.tajs.directives.unreachable";
                        statusText = "error";
                        if (status < 0) {
                            "dk.brics.tajs.directives.unreachable";
                            status = 0;
                        }
                    }
                }
                // Set data for the fake xhr object
                jqXHR.status = status;
                jqXHR.statusText = "" + (nativeStatusText || statusText);
                // Success/Error
                if (isSuccess) {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.resolveWith(callbackContext, [ success, statusText, jqXHR ]);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.rejectWith(callbackContext, [ jqXHR, statusText, error ]);
                }
                // Status-dependent callbacks
                jqXHR.statusCode(statusCode);
                statusCode = undefined;
                if (fireGlobals) {
                    "dk.brics.tajs.directives.unreachable";
                    globalEventContext.trigger("ajax" + (isSuccess ? "Success" : "Error"), [ jqXHR, s, isSuccess ? success : error ]);
                }
                // Complete
                completeDeferred.fireWith(callbackContext, [ jqXHR, statusText ]);
                if (fireGlobals) {
                    "dk.brics.tajs.directives.unreachable";
                    globalEventContext.trigger("ajaxComplete", [ jqXHR, s ]);
                    // Handle the global AJAX counter
                    if (!--jQuery.active) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.trigger("ajaxStop");
                    }
                }
            }
            // Attach deferreds
            deferred.promise(jqXHR);
            jqXHR.success = jqXHR.done;
            jqXHR.error = jqXHR.fail;
            jqXHR.complete = completeDeferred.add;
            // Status-dependent callbacks
            jqXHR.statusCode = function(map) {
                "dk.brics.tajs.directives.unreachable";
                if (map) {
                    "dk.brics.tajs.directives.unreachable";
                    var tmp;
                    if (state < 2) {
                        "dk.brics.tajs.directives.unreachable";
                        for (tmp in map) {
                            "dk.brics.tajs.directives.unreachable";
                            statusCode[tmp] = [ statusCode[tmp], map[tmp] ];
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        tmp = map[jqXHR.status];
                        jqXHR.always(tmp);
                    }
                }
                return this;
            };
            // Remove hash character (#7531: and string promotion)
            // Add protocol if not provided (#5866: IE7 issue with protocol-less urls)
            // We also use the url parameter if available
            s.url = ((url || s.url) + "").replace(rhash, "").replace(rprotocol, ajaxLocParts[1] + "//");
            // Extract dataTypes list
            s.dataTypes = jQuery.trim(s.dataType || "*").toLowerCase().split(core_rspace);
            // Determine if a cross-domain request is in order
            if (s.crossDomain == null) {
                "dk.brics.tajs.directives.unreachable";
                parts = rurl.exec(s.url.toLowerCase());
                s.crossDomain = !!(parts && (parts[1] != ajaxLocParts[1] || parts[2] != ajaxLocParts[2] || (parts[3] || (parts[1] === "http:" ? 80 : 443)) != (ajaxLocParts[3] || (ajaxLocParts[1] === "http:" ? 80 : 443))));
            }
            // Convert data if not already a string
            if (s.data && s.processData && typeof s.data !== "string") {
                "dk.brics.tajs.directives.unreachable";
                s.data = jQuery.param(s.data, s.traditional);
            }
            // Apply prefilters
            inspectPrefiltersOrTransports(prefilters, s, options, jqXHR);
            // If request was aborted inside a prefilter, stop there
            if (state === 2) {
                "dk.brics.tajs.directives.unreachable";
                return jqXHR;
            }
            // We can fire global events as of now if asked to
            fireGlobals = s.global;
            // Uppercase the type
            s.type = s.type.toUpperCase();
            // Determine if request has content
            s.hasContent = !rnoContent.test(s.type);
            // Watch for a new set of requests
            if (fireGlobals && jQuery.active++ === 0) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger("ajaxStart");
            }
            // More options handling for requests with no content
            if (!s.hasContent) {
                "dk.brics.tajs.directives.unreachable";
                // If data is available, append data to url
                if (s.data) {
                    "dk.brics.tajs.directives.unreachable";
                    s.url += (rquery.test(s.url) ? "&" : "?") + s.data;
                    // #9682: remove data so that it's not used in an eventual retry
                    delete s.data;
                }
                // Get ifModifiedKey before adding the anti-cache parameter
                ifModifiedKey = s.url;
                // Add anti-cache in url if needed
                if (s.cache === false) {
                    "dk.brics.tajs.directives.unreachable";
                    var ts = jQuery.now(), // try replacing _= if it is there
                    ret = s.url.replace(rts, "$1_=" + ts);
                    // if nothing was replaced, add timestamp to the end
                    s.url = ret + (ret === s.url ? (rquery.test(s.url) ? "&" : "?") + "_=" + ts : "");
                }
            }
            // Set the correct header, if data is being sent
            if (s.data && s.hasContent && s.contentType !== false || options.contentType) {
                "dk.brics.tajs.directives.unreachable";
                jqXHR.setRequestHeader("Content-Type", s.contentType);
            }
            // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
            if (s.ifModified) {
                "dk.brics.tajs.directives.unreachable";
                ifModifiedKey = ifModifiedKey || s.url;
                if (jQuery.lastModified[ifModifiedKey]) {
                    "dk.brics.tajs.directives.unreachable";
                    jqXHR.setRequestHeader("If-Modified-Since", jQuery.lastModified[ifModifiedKey]);
                }
                if (jQuery.etag[ifModifiedKey]) {
                    "dk.brics.tajs.directives.unreachable";
                    jqXHR.setRequestHeader("If-None-Match", jQuery.etag[ifModifiedKey]);
                }
            }
            // Set the Accepts header for the server, depending on the dataType
            jqXHR.setRequestHeader("Accept", s.dataTypes[0] && s.accepts[s.dataTypes[0]] ? s.accepts[s.dataTypes[0]] + (s.dataTypes[0] !== "*" ? ", " + allTypes + "; q=0.01" : "") : s.accepts["*"]);
            // Check for headers option
            for (i in s.headers) {
                "dk.brics.tajs.directives.unreachable";
                jqXHR.setRequestHeader(i, s.headers[i]);
            }
            // Allow custom headers/mimetypes and early abort
            if (s.beforeSend && (s.beforeSend.call(callbackContext, jqXHR, s) === false || state === 2)) {
                "dk.brics.tajs.directives.unreachable";
                // Abort if not done already and return
                return jqXHR.abort();
            }
            // aborting is no longer a cancellation
            strAbort = "abort";
            // Install callbacks on deferreds
            for (i in {
                success: 1,
                error: 1,
                complete: 1
            }) {
                "dk.brics.tajs.directives.unreachable";
                jqXHR[i](s[i]);
            }
            // Get transport
            transport = inspectPrefiltersOrTransports(transports, s, options, jqXHR);
            // If no transport, we auto-abort
            if (!transport) {
                "dk.brics.tajs.directives.unreachable";
                done(-1, "No Transport");
            } else {
                "dk.brics.tajs.directives.unreachable";
                jqXHR.readyState = 1;
                // Send global event
                if (fireGlobals) {
                    "dk.brics.tajs.directives.unreachable";
                    globalEventContext.trigger("ajaxSend", [ jqXHR, s ]);
                }
                // Timeout
                if (s.async && s.timeout > 0) {
                    "dk.brics.tajs.directives.unreachable";
                    timeoutTimer = setTimeout(function() {
                        "dk.brics.tajs.directives.unreachable";
                        jqXHR.abort("timeout");
                    }, s.timeout);
                }
                try {
                    "dk.brics.tajs.directives.unreachable";
                    state = 1;
                    transport.send(requestHeaders, done);
                } catch (e) {
                    "dk.brics.tajs.directives.unreachable";
                    // Propagate exception as error if not done
                    if (state < 2) {
                        "dk.brics.tajs.directives.unreachable";
                        done(-1, e);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        throw e;
                    }
                }
            }
            return jqXHR;
        },
        // Counter for holding the number of active queries
        active: 0,
        // Last-Modified header cache for next request
        lastModified: {},
        etag: {}
    });
    /* Handles responses to an ajax request:
 * - sets all responseXXX fields accordingly
 * - finds the right dataType (mediates between content-type and expected dataType)
 * - returns the corresponding response
 */
    function ajaxHandleResponses(s, jqXHR, responses) {
        "dk.brics.tajs.directives.unreachable";
        var ct, type, finalDataType, firstDataType, contents = s.contents, dataTypes = s.dataTypes, responseFields = s.responseFields;
        // Fill responseXXX fields
        for (type in responseFields) {
            "dk.brics.tajs.directives.unreachable";
            if (type in responses) {
                "dk.brics.tajs.directives.unreachable";
                jqXHR[responseFields[type]] = responses[type];
            }
        }
        // Remove auto dataType and get content-type in the process
        while (dataTypes[0] === "*") {
            "dk.brics.tajs.directives.unreachable";
            dataTypes.shift();
            if (ct === undefined) {
                "dk.brics.tajs.directives.unreachable";
                ct = s.mimeType || jqXHR.getResponseHeader("content-type");
            }
        }
        // Check if we're dealing with a known content-type
        if (ct) {
            "dk.brics.tajs.directives.unreachable";
            for (type in contents) {
                "dk.brics.tajs.directives.unreachable";
                if (contents[type] && contents[type].test(ct)) {
                    "dk.brics.tajs.directives.unreachable";
                    dataTypes.unshift(type);
                    break;
                }
            }
        }
        // Check to see if we have a response for the expected dataType
        if (dataTypes[0] in responses) {
            "dk.brics.tajs.directives.unreachable";
            finalDataType = dataTypes[0];
        } else {
            "dk.brics.tajs.directives.unreachable";
            // Try convertible dataTypes
            for (type in responses) {
                "dk.brics.tajs.directives.unreachable";
                if (!dataTypes[0] || s.converters[type + " " + dataTypes[0]]) {
                    "dk.brics.tajs.directives.unreachable";
                    finalDataType = type;
                    break;
                }
                if (!firstDataType) {
                    "dk.brics.tajs.directives.unreachable";
                    firstDataType = type;
                }
            }
            // Or just use first one
            finalDataType = finalDataType || firstDataType;
        }
        // If we found a dataType
        // We add the dataType to the list if needed
        // and return the corresponding response
        if (finalDataType) {
            "dk.brics.tajs.directives.unreachable";
            if (finalDataType !== dataTypes[0]) {
                "dk.brics.tajs.directives.unreachable";
                dataTypes.unshift(finalDataType);
            }
            return responses[finalDataType];
        }
    }
    // Chain conversions given the request and the original response
    function ajaxConvert(s, response) {
        "dk.brics.tajs.directives.unreachable";
        var conv, conv2, current, tmp, // Work with a copy of dataTypes in case we need to modify it for conversion
        dataTypes = s.dataTypes.slice(), prev = dataTypes[0], converters = {}, i = 0;
        // Apply the dataFilter if provided
        if (s.dataFilter) {
            "dk.brics.tajs.directives.unreachable";
            response = s.dataFilter(response, s.dataType);
        }
        // Create converters map with lowercased keys
        if (dataTypes[1]) {
            "dk.brics.tajs.directives.unreachable";
            for (conv in s.converters) {
                "dk.brics.tajs.directives.unreachable";
                converters[conv.toLowerCase()] = s.converters[conv];
            }
        }
        // Convert to each sequential dataType, tolerating list modification
        for (;current = dataTypes[++i]; ) {
            "dk.brics.tajs.directives.unreachable";
            // There's only work to do if current dataType is non-auto
            if (current !== "*") {
                "dk.brics.tajs.directives.unreachable";
                // Convert response if prev dataType is non-auto and differs from current
                if (prev !== "*" && prev !== current) {
                    "dk.brics.tajs.directives.unreachable";
                    // Seek a direct converter
                    conv = converters[prev + " " + current] || converters["* " + current];
                    // If none found, seek a pair
                    if (!conv) {
                        "dk.brics.tajs.directives.unreachable";
                        for (conv2 in converters) {
                            "dk.brics.tajs.directives.unreachable";
                            // If conv2 outputs current
                            tmp = conv2.split(" ");
                            if (tmp[1] === current) {
                                "dk.brics.tajs.directives.unreachable";
                                // If prev can be converted to accepted input
                                conv = converters[prev + " " + tmp[0]] || converters["* " + tmp[0]];
                                if (conv) {
                                    "dk.brics.tajs.directives.unreachable";
                                    // Condense equivalence converters
                                    if (conv === true) {
                                        "dk.brics.tajs.directives.unreachable";
                                        conv = converters[conv2];
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (converters[conv2] !== true) {
                                            "dk.brics.tajs.directives.unreachable";
                                            current = tmp[0];
                                            dataTypes.splice(i--, 0, current);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    // Apply converter (if not an equivalence)
                    if (conv !== true) {
                        "dk.brics.tajs.directives.unreachable";
                        // Unless errors are allowed to bubble, catch and return them
                        if (conv && s["throws"]) {
                            "dk.brics.tajs.directives.unreachable";
                            response = conv(response);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            try {
                                "dk.brics.tajs.directives.unreachable";
                                response = conv(response);
                            } catch (e) {
                                "dk.brics.tajs.directives.unreachable";
                                return {
                                    state: "parsererror",
                                    error: conv ? e : "No conversion from " + prev + " to " + current
                                };
                            }
                        }
                    }
                }
                // Update prev for next iteration
                prev = current;
            }
        }
        return {
            state: "success",
            data: response
        };
    }
    var oldCallbacks = [], rquestion = /\?/, rjsonp = /(=)\?(?=&|$)|\?\?/, nonce = jQuery.now();
    // Default jsonp settings
    jQuery.ajaxSetup({
        jsonp: "callback",
        jsonpCallback: function() {
            "dk.brics.tajs.directives.unreachable";
            var callback = oldCallbacks.pop() || jQuery.expando + "_" + nonce++;
            this[callback] = true;
            return callback;
        }
    });
    // Detect, normalize options and install callbacks for jsonp requests
    jQuery.ajaxPrefilter("json jsonp", function(s, originalSettings, jqXHR) {
        "dk.brics.tajs.directives.unreachable";
        var callbackName, overwritten, responseContainer, data = s.data, url = s.url, hasCallback = s.jsonp !== false, replaceInUrl = hasCallback && rjsonp.test(url), replaceInData = hasCallback && !replaceInUrl && typeof data === "string" && !(s.contentType || "").indexOf("application/x-www-form-urlencoded") && rjsonp.test(data);
        // Handle iff the expected data type is "jsonp" or we have a parameter to set
        if (s.dataTypes[0] === "jsonp" || replaceInUrl || replaceInData) {
            "dk.brics.tajs.directives.unreachable";
            // Get callback name, remembering preexisting value associated with it
            callbackName = s.jsonpCallback = jQuery.isFunction(s.jsonpCallback) ? s.jsonpCallback() : s.jsonpCallback;
            overwritten = window[callbackName];
            // Insert callback into url or form data
            if (replaceInUrl) {
                "dk.brics.tajs.directives.unreachable";
                s.url = url.replace(rjsonp, "$1" + callbackName);
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (replaceInData) {
                    "dk.brics.tajs.directives.unreachable";
                    s.data = data.replace(rjsonp, "$1" + callbackName);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (hasCallback) {
                        "dk.brics.tajs.directives.unreachable";
                        s.url += (rquestion.test(url) ? "&" : "?") + s.jsonp + "=" + callbackName;
                    }
                }
            }
            // Use data converter to retrieve json after script execution
            s.converters["script json"] = function() {
                "dk.brics.tajs.directives.unreachable";
                if (!responseContainer) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.error(callbackName + " was not called");
                }
                return responseContainer[0];
            };
            // force json dataType
            s.dataTypes[0] = "json";
            // Install callback
            window[callbackName] = function() {
                "dk.brics.tajs.directives.unreachable";
                responseContainer = arguments;
            };
            // Clean-up function (fires after converters)
            jqXHR.always(function() {
                "dk.brics.tajs.directives.unreachable";
                // Restore preexisting value
                window[callbackName] = overwritten;
                // Save back as free
                if (s[callbackName]) {
                    "dk.brics.tajs.directives.unreachable";
                    // make sure that re-using the options doesn't screw things around
                    s.jsonpCallback = originalSettings.jsonpCallback;
                    // save the callback name for future use
                    oldCallbacks.push(callbackName);
                }
                // Call if it was a function and we have a response
                if (responseContainer && jQuery.isFunction(overwritten)) {
                    "dk.brics.tajs.directives.unreachable";
                    overwritten(responseContainer[0]);
                }
                responseContainer = overwritten = undefined;
            });
            // Delegate to script
            return "script";
        }
    });
    // Install script dataType
    jQuery.ajaxSetup({
        accepts: {
            script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
        },
        contents: {
            script: /javascript|ecmascript/
        },
        converters: {
            "text script": function(text) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.globalEval(text);
                return text;
            }
        }
    });
    // Handle cache's special case and global
    jQuery.ajaxPrefilter("script", function(s) {
        "dk.brics.tajs.directives.unreachable";
        if (s.cache === undefined) {
            "dk.brics.tajs.directives.unreachable";
            s.cache = false;
        }
        if (s.crossDomain) {
            "dk.brics.tajs.directives.unreachable";
            s.type = "GET";
            s.global = false;
        }
    });
    // Bind script tag hack transport
    jQuery.ajaxTransport("script", function(s) {
        "dk.brics.tajs.directives.unreachable";
        // This transport only deals with cross domain requests
        if (s.crossDomain) {
            "dk.brics.tajs.directives.unreachable";
            var script, head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
            return {
                send: function(_, callback) {
                    "dk.brics.tajs.directives.unreachable";
                    script = document.createElement("script");
                    script.async = "async";
                    if (s.scriptCharset) {
                        "dk.brics.tajs.directives.unreachable";
                        script.charset = s.scriptCharset;
                    }
                    script.src = s.url;
                    // Attach handlers for all browsers
                    script.onload = script.onreadystatechange = function(_, isAbort) {
                        "dk.brics.tajs.directives.unreachable";
                        if (isAbort || !script.readyState || /loaded|complete/.test(script.readyState)) {
                            "dk.brics.tajs.directives.unreachable";
                            // Handle memory leak in IE
                            script.onload = script.onreadystatechange = null;
                            // Remove the script
                            if (head && script.parentNode) {
                                "dk.brics.tajs.directives.unreachable";
                                head.removeChild(script);
                            }
                            // Dereference the script
                            script = undefined;
                            // Callback if not abort
                            if (!isAbort) {
                                "dk.brics.tajs.directives.unreachable";
                                callback(200, "success");
                            }
                        }
                    };
                    // Use insertBefore instead of appendChild  to circumvent an IE6 bug.
                    // This arises when a base node is used (#2709 and #4378).
                    head.insertBefore(script, head.firstChild);
                },
                abort: function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (script) {
                        "dk.brics.tajs.directives.unreachable";
                        script.onload(0, 1);
                    }
                }
            };
        }
    });
    var xhrCallbacks, // #5280: Internet Explorer will keep connections alive if we don't abort on unload
    xhrOnUnloadAbort = window.ActiveXObject ? function() {
        "dk.brics.tajs.directives.unreachable";
        // Abort all pending requests
        for (var key in xhrCallbacks) {
            "dk.brics.tajs.directives.unreachable";
            xhrCallbacks[key](0, 1);
        }
    } : false, xhrId = 0;
    // Functions to create xhrs
    function createStandardXHR() {
        try {
            return new window.XMLHttpRequest();
        } catch (e) {}
    }
    function createActiveXHR() {
        "dk.brics.tajs.directives.unreachable";
        try {
            "dk.brics.tajs.directives.unreachable";
            return new window.ActiveXObject("Microsoft.XMLHTTP");
        } catch (e) {}
    }
    // Create the request object
    // (This is still attached to ajaxSettings for backward compatibility)
    jQuery.ajaxSettings.xhr = window.ActiveXObject ? /* Microsoft failed to properly
	 * implement the XMLHttpRequest in IE7 (can't request local files),
	 * so we use the ActiveXObject when it is available
	 * Additionally XMLHttpRequest can be disabled in IE7/IE8 so
	 * we need a fallback.
	 */
    function() {
        "dk.brics.tajs.directives.unreachable";
        return !this.isLocal && createStandardXHR() || createActiveXHR();
    } : // For all other browsers, use the standard XMLHttpRequest object
    createStandardXHR;
    // Determine support properties
    (function(xhr) {
        jQuery.extend(jQuery.support, {
            ajax: !!xhr,
            cors: !!xhr && "withCredentials" in xhr
        });
    })(jQuery.ajaxSettings.xhr());
    // Create transport if the browser can provide an xhr
    if (jQuery.support.ajax) {
        jQuery.ajaxTransport(function(s) {
            "dk.brics.tajs.directives.unreachable";
            // Cross domain only allowed if supported through XMLHttpRequest
            if (!s.crossDomain || jQuery.support.cors) {
                "dk.brics.tajs.directives.unreachable";
                var callback;
                return {
                    send: function(headers, complete) {
                        "dk.brics.tajs.directives.unreachable";
                        // Get a new xhr
                        var handle, i, xhr = s.xhr();
                        // Open the socket
                        // Passing null username, generates a login popup on Opera (#2865)
                        if (s.username) {
                            "dk.brics.tajs.directives.unreachable";
                            xhr.open(s.type, s.url, s.async, s.username, s.password);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            xhr.open(s.type, s.url, s.async);
                        }
                        // Apply custom fields if provided
                        if (s.xhrFields) {
                            "dk.brics.tajs.directives.unreachable";
                            for (i in s.xhrFields) {
                                "dk.brics.tajs.directives.unreachable";
                                xhr[i] = s.xhrFields[i];
                            }
                        }
                        // Override mime type if needed
                        if (s.mimeType && xhr.overrideMimeType) {
                            "dk.brics.tajs.directives.unreachable";
                            xhr.overrideMimeType(s.mimeType);
                        }
                        // X-Requested-With header
                        // For cross-domain requests, seeing as conditions for a preflight are
                        // akin to a jigsaw puzzle, we simply never set it to be sure.
                        // (it can always be set on a per-request basis or even using ajaxSetup)
                        // For same-domain requests, won't change header if already provided.
                        if (!s.crossDomain && !headers["X-Requested-With"]) {
                            "dk.brics.tajs.directives.unreachable";
                            headers["X-Requested-With"] = "XMLHttpRequest";
                        }
                        // Need an extra try/catch for cross domain requests in Firefox 3
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            for (i in headers) {
                                "dk.brics.tajs.directives.unreachable";
                                xhr.setRequestHeader(i, headers[i]);
                            }
                        } catch (_) {}
                        // Do send the request
                        // This may raise an exception which is actually
                        // handled in jQuery.ajax (so no try/catch here)
                        xhr.send(s.hasContent && s.data || null);
                        // Listener
                        callback = function(_, isAbort) {
                            "dk.brics.tajs.directives.unreachable";
                            var status, statusText, responseHeaders, responses, xml;
                            // Firefox throws exceptions when accessing properties
                            // of an xhr when a network error occurred
                            // http://helpful.knobs-dials.com/index.php/Component_returned_failure_code:_0x80040111_(NS_ERROR_NOT_AVAILABLE)
                            try {
                                "dk.brics.tajs.directives.unreachable";
                                // Was never called and is aborted or complete
                                if (callback && (isAbort || xhr.readyState === 4)) {
                                    "dk.brics.tajs.directives.unreachable";
                                    // Only called once
                                    callback = undefined;
                                    // Do not keep as active anymore
                                    if (handle) {
                                        "dk.brics.tajs.directives.unreachable";
                                        xhr.onreadystatechange = jQuery.noop;
                                        if (xhrOnUnloadAbort) {
                                            "dk.brics.tajs.directives.unreachable";
                                            delete xhrCallbacks[handle];
                                        }
                                    }
                                    // If it's an abort
                                    if (isAbort) {
                                        "dk.brics.tajs.directives.unreachable";
                                        // Abort it manually if needed
                                        if (xhr.readyState !== 4) {
                                            "dk.brics.tajs.directives.unreachable";
                                            xhr.abort();
                                        }
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        status = xhr.status;
                                        responseHeaders = xhr.getAllResponseHeaders();
                                        responses = {};
                                        xml = xhr.responseXML;
                                        // Construct response list
                                        if (xml && xml.documentElement) {
                                            "dk.brics.tajs.directives.unreachable";
                                            responses.xml = xml;
                                        }
                                        // When requesting binary data, IE6-9 will throw an exception
                                        // on any attempt to access responseText (#11426)
                                        try {
                                            "dk.brics.tajs.directives.unreachable";
                                            responses.text = xhr.responseText;
                                        } catch (_) {}
                                        // Firefox throws an exception when accessing
                                        // statusText for faulty cross-domain requests
                                        try {
                                            "dk.brics.tajs.directives.unreachable";
                                            statusText = xhr.statusText;
                                        } catch (e) {
                                            "dk.brics.tajs.directives.unreachable";
                                            // We normalize with Webkit giving an empty statusText
                                            statusText = "";
                                        }
                                        // Filter status for non standard behaviors
                                        // If the request is local and we have data: assume a success
                                        // (success with no data won't get notified, that's the best we
                                        // can do given current implementations)
                                        if (!status && s.isLocal && !s.crossDomain) {
                                            "dk.brics.tajs.directives.unreachable";
                                            status = responses.text ? 200 : 404;
                                        } else {
                                            "dk.brics.tajs.directives.unreachable";
                                            if (status === 1223) {
                                                "dk.brics.tajs.directives.unreachable";
                                                status = 204;
                                            }
                                        }
                                    }
                                }
                            } catch (firefoxAccessException) {
                                "dk.brics.tajs.directives.unreachable";
                                if (!isAbort) {
                                    "dk.brics.tajs.directives.unreachable";
                                    complete(-1, firefoxAccessException);
                                }
                            }
                            // Call complete if needed
                            if (responses) {
                                "dk.brics.tajs.directives.unreachable";
                                complete(status, statusText, responses, responseHeaders);
                            }
                        };
                        if (!s.async) {
                            "dk.brics.tajs.directives.unreachable";
                            // if we're in sync mode we fire the callback
                            callback();
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (xhr.readyState === 4) {
                                "dk.brics.tajs.directives.unreachable";
                                // (IE6 & IE7) if it's in cache and has been
                                // retrieved directly we need to fire the callback
                                setTimeout(callback, 0);
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                handle = ++xhrId;
                                if (xhrOnUnloadAbort) {
                                    "dk.brics.tajs.directives.unreachable";
                                    // Create the active xhrs callbacks list if needed
                                    // and attach the unload handler
                                    if (!xhrCallbacks) {
                                        "dk.brics.tajs.directives.unreachable";
                                        xhrCallbacks = {};
                                        jQuery(window).unload(xhrOnUnloadAbort);
                                    }
                                    // Add to list of active xhrs callbacks
                                    xhrCallbacks[handle] = callback;
                                }
                                xhr.onreadystatechange = callback;
                            }
                        }
                    },
                    abort: function() {
                        "dk.brics.tajs.directives.unreachable";
                        if (callback) {
                            "dk.brics.tajs.directives.unreachable";
                            callback(0, 1);
                        }
                    }
                };
            }
        });
    }
    var fxNow, timerId, rfxtypes = /^(?:toggle|show|hide)$/, rfxnum = new RegExp("^(?:([-+])=|)(" + core_pnum + ")([a-z%]*)$", "i"), rrun = /queueHooks$/, animationPrefilters = [ defaultPrefilter ], tweeners = {
        "*": [ function(prop, value) {
            "dk.brics.tajs.directives.unreachable";
            var end, unit, prevScale, tween = this.createTween(prop, value), parts = rfxnum.exec(value), target = tween.cur(), start = +target || 0, scale = 1;
            if (parts) {
                "dk.brics.tajs.directives.unreachable";
                end = +parts[2];
                unit = parts[3] || (jQuery.cssNumber[prop] ? "" : "px");
                // We need to compute starting value
                if (unit !== "px" && start) {
                    "dk.brics.tajs.directives.unreachable";
                    // Iteratively approximate from a nonzero starting point
                    // Prefer the current property, because this process will be trivial if it uses the same units
                    // Fallback to end or a simple constant
                    start = jQuery.css(tween.elem, prop, true) || end || 1;
                    do {
                        "dk.brics.tajs.directives.unreachable";
                        // If previous iteration zeroed out, double until we get *something*
                        // Use a string for doubling factor so we don't accidentally see scale as unchanged below
                        prevScale = scale = scale || ".5";
                        // Adjust and apply
                        start = start / scale;
                        jQuery.style(tween.elem, prop, start + unit);
                        // Update scale, tolerating zeroes from tween.cur()
                        scale = tween.cur() / target;
                    } while (scale !== 1 && scale !== prevScale);
                }
                tween.unit = unit;
                tween.start = start;
                // If a +=/-= token was provided, we're doing a relative animation
                tween.end = parts[1] ? start + (parts[1] + 1) * end : end;
            }
            return tween;
        } ]
    };
    // Animations created synchronously will run synchronously
    function createFxNow() {
        "dk.brics.tajs.directives.unreachable";
        setTimeout(function() {
            "dk.brics.tajs.directives.unreachable";
            fxNow = undefined;
        }, 0);
        return fxNow = jQuery.now();
    }
    function createTweens(animation, props) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.each(props, function(prop, value) {
            "dk.brics.tajs.directives.unreachable";
            var collection = (tweeners[prop] || []).concat(tweeners["*"]), index = 0, length = collection.length;
            for (;index < length; index++) {
                "dk.brics.tajs.directives.unreachable";
                if (collection[index].call(animation, prop, value)) {
                    "dk.brics.tajs.directives.unreachable";
                    // we're done with this property
                    return;
                }
            }
        });
    }
    function Animation(elem, properties, options) {
        "dk.brics.tajs.directives.unreachable";
        var result, index = 0, tweenerIndex = 0, length = animationPrefilters.length, deferred = jQuery.Deferred().always(function() {
            "dk.brics.tajs.directives.unreachable";
            // don't match elem in the :animated selector
            delete tick.elem;
        }), tick = function() {
            "dk.brics.tajs.directives.unreachable";
            var currentTime = fxNow || createFxNow(), remaining = Math.max(0, animation.startTime + animation.duration - currentTime), percent = 1 - (remaining / animation.duration || 0), index = 0, length = animation.tweens.length;
            for (;index < length; index++) {
                "dk.brics.tajs.directives.unreachable";
                animation.tweens[index].run(percent);
            }
            deferred.notifyWith(elem, [ animation, percent, remaining ]);
            if (percent < 1 && length) {
                "dk.brics.tajs.directives.unreachable";
                return remaining;
            } else {
                "dk.brics.tajs.directives.unreachable";
                deferred.resolveWith(elem, [ animation ]);
                return false;
            }
        }, animation = deferred.promise({
            elem: elem,
            props: jQuery.extend({}, properties),
            opts: jQuery.extend(true, {
                specialEasing: {}
            }, options),
            originalProperties: properties,
            originalOptions: options,
            startTime: fxNow || createFxNow(),
            duration: options.duration,
            tweens: [],
            createTween: function(prop, end, easing) {
                "dk.brics.tajs.directives.unreachable";
                var tween = jQuery.Tween(elem, animation.opts, prop, end, animation.opts.specialEasing[prop] || animation.opts.easing);
                animation.tweens.push(tween);
                return tween;
            },
            stop: function(gotoEnd) {
                "dk.brics.tajs.directives.unreachable";
                var index = 0, // if we are going to the end, we want to run all the tweens
                // otherwise we skip this part
                length = gotoEnd ? animation.tweens.length : 0;
                for (;index < length; index++) {
                    "dk.brics.tajs.directives.unreachable";
                    animation.tweens[index].run(1);
                }
                // resolve when we played the last frame
                // otherwise, reject
                if (gotoEnd) {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.resolveWith(elem, [ animation, gotoEnd ]);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.rejectWith(elem, [ animation, gotoEnd ]);
                }
                return this;
            }
        }), props = animation.props;
        propFilter(props, animation.opts.specialEasing);
        for (;index < length; index++) {
            "dk.brics.tajs.directives.unreachable";
            result = animationPrefilters[index].call(animation, elem, props, animation.opts);
            if (result) {
                "dk.brics.tajs.directives.unreachable";
                return result;
            }
        }
        createTweens(animation, props);
        if (jQuery.isFunction(animation.opts.start)) {
            "dk.brics.tajs.directives.unreachable";
            animation.opts.start.call(elem, animation);
        }
        jQuery.fx.timer(jQuery.extend(tick, {
            anim: animation,
            queue: animation.opts.queue,
            elem: elem
        }));
        // attach callbacks from options
        return animation.progress(animation.opts.progress).done(animation.opts.done, animation.opts.complete).fail(animation.opts.fail).always(animation.opts.always);
    }
    function propFilter(props, specialEasing) {
        "dk.brics.tajs.directives.unreachable";
        var index, name, easing, value, hooks;
        // camelCase, specialEasing and expand cssHook pass
        for (index in props) {
            "dk.brics.tajs.directives.unreachable";
            name = jQuery.camelCase(index);
            easing = specialEasing[name];
            value = props[index];
            if (jQuery.isArray(value)) {
                "dk.brics.tajs.directives.unreachable";
                easing = value[1];
                value = props[index] = value[0];
            }
            if (index !== name) {
                "dk.brics.tajs.directives.unreachable";
                props[name] = value;
                delete props[index];
            }
            hooks = jQuery.cssHooks[name];
            if (hooks && "expand" in hooks) {
                "dk.brics.tajs.directives.unreachable";
                value = hooks.expand(value);
                delete props[name];
                // not quite $.extend, this wont overwrite keys already present.
                // also - reusing 'index' from above because we have the correct "name"
                for (index in value) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!(index in props)) {
                        "dk.brics.tajs.directives.unreachable";
                        props[index] = value[index];
                        specialEasing[index] = easing;
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                specialEasing[name] = easing;
            }
        }
    }
    jQuery.Animation = jQuery.extend(Animation, {
        tweener: function(props, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(props)) {
                "dk.brics.tajs.directives.unreachable";
                callback = props;
                props = [ "*" ];
            } else {
                "dk.brics.tajs.directives.unreachable";
                props = props.split(" ");
            }
            var prop, index = 0, length = props.length;
            for (;index < length; index++) {
                "dk.brics.tajs.directives.unreachable";
                prop = props[index];
                tweeners[prop] = tweeners[prop] || [];
                tweeners[prop].unshift(callback);
            }
        },
        prefilter: function(callback, prepend) {
            "dk.brics.tajs.directives.unreachable";
            if (prepend) {
                "dk.brics.tajs.directives.unreachable";
                animationPrefilters.unshift(callback);
            } else {
                "dk.brics.tajs.directives.unreachable";
                animationPrefilters.push(callback);
            }
        }
    });
    function defaultPrefilter(elem, props, opts) {
        "dk.brics.tajs.directives.unreachable";
        var index, prop, value, length, dataShow, tween, hooks, oldfire, anim = this, style = elem.style, orig = {}, handled = [], hidden = elem.nodeType && isHidden(elem);
        // handle queue: false promises
        if (!opts.queue) {
            "dk.brics.tajs.directives.unreachable";
            hooks = jQuery._queueHooks(elem, "fx");
            if (hooks.unqueued == null) {
                "dk.brics.tajs.directives.unreachable";
                hooks.unqueued = 0;
                oldfire = hooks.empty.fire;
                hooks.empty.fire = function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (!hooks.unqueued) {
                        "dk.brics.tajs.directives.unreachable";
                        oldfire();
                    }
                };
            }
            hooks.unqueued++;
            anim.always(function() {
                "dk.brics.tajs.directives.unreachable";
                // doing this makes sure that the complete handler will be called
                // before this completes
                anim.always(function() {
                    "dk.brics.tajs.directives.unreachable";
                    hooks.unqueued--;
                    if (!jQuery.queue(elem, "fx").length) {
                        "dk.brics.tajs.directives.unreachable";
                        hooks.empty.fire();
                    }
                });
            });
        }
        // height/width overflow pass
        if (elem.nodeType === 1 && ("height" in props || "width" in props)) {
            "dk.brics.tajs.directives.unreachable";
            // Make sure that nothing sneaks out
            // Record all 3 overflow attributes because IE does not
            // change the overflow attribute when overflowX and
            // overflowY are set to the same value
            opts.overflow = [ style.overflow, style.overflowX, style.overflowY ];
            // Set display property to inline-block for height/width
            // animations on inline elements that are having width/height animated
            if (jQuery.css(elem, "display") === "inline" && jQuery.css(elem, "float") === "none") {
                "dk.brics.tajs.directives.unreachable";
                // inline-level elements accept inline-block;
                // block-level elements need to be inline with layout
                if (!jQuery.support.inlineBlockNeedsLayout || css_defaultDisplay(elem.nodeName) === "inline") {
                    "dk.brics.tajs.directives.unreachable";
                    style.display = "inline-block";
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    style.zoom = 1;
                }
            }
        }
        if (opts.overflow) {
            "dk.brics.tajs.directives.unreachable";
            style.overflow = "hidden";
            if (!jQuery.support.shrinkWrapBlocks) {
                "dk.brics.tajs.directives.unreachable";
                anim.done(function() {
                    "dk.brics.tajs.directives.unreachable";
                    style.overflow = opts.overflow[0];
                    style.overflowX = opts.overflow[1];
                    style.overflowY = opts.overflow[2];
                });
            }
        }
        // show/hide pass
        for (index in props) {
            "dk.brics.tajs.directives.unreachable";
            value = props[index];
            if (rfxtypes.exec(value)) {
                "dk.brics.tajs.directives.unreachable";
                delete props[index];
                if (value === (hidden ? "hide" : "show")) {
                    "dk.brics.tajs.directives.unreachable";
                    continue;
                }
                handled.push(index);
            }
        }
        length = handled.length;
        if (length) {
            "dk.brics.tajs.directives.unreachable";
            dataShow = jQuery._data(elem, "fxshow") || jQuery._data(elem, "fxshow", {});
            if (hidden) {
                "dk.brics.tajs.directives.unreachable";
                jQuery(elem).show();
            } else {
                "dk.brics.tajs.directives.unreachable";
                anim.done(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(elem).hide();
                });
            }
            anim.done(function() {
                "dk.brics.tajs.directives.unreachable";
                var prop;
                jQuery.removeData(elem, "fxshow", true);
                for (prop in orig) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.style(elem, prop, orig[prop]);
                }
            });
            for (index = 0; index < length; index++) {
                "dk.brics.tajs.directives.unreachable";
                prop = handled[index];
                tween = anim.createTween(prop, hidden ? dataShow[prop] : 0);
                orig[prop] = dataShow[prop] || jQuery.style(elem, prop);
                if (!(prop in dataShow)) {
                    "dk.brics.tajs.directives.unreachable";
                    dataShow[prop] = tween.start;
                    if (hidden) {
                        "dk.brics.tajs.directives.unreachable";
                        tween.end = tween.start;
                        tween.start = prop === "width" || prop === "height" ? 1 : 0;
                    }
                }
            }
        }
    }
    function Tween(elem, options, prop, end, easing) {
        "dk.brics.tajs.directives.unreachable";
        return new Tween.prototype.init(elem, options, prop, end, easing);
    }
    jQuery.Tween = Tween;
    Tween.prototype = {
        constructor: Tween,
        init: function(elem, options, prop, end, easing, unit) {
            "dk.brics.tajs.directives.unreachable";
            this.elem = elem;
            this.prop = prop;
            this.easing = easing || "swing";
            this.options = options;
            this.start = this.now = this.cur();
            this.end = end;
            this.unit = unit || (jQuery.cssNumber[prop] ? "" : "px");
        },
        cur: function() {
            "dk.brics.tajs.directives.unreachable";
            var hooks = Tween.propHooks[this.prop];
            return hooks && hooks.get ? hooks.get(this) : Tween.propHooks._default.get(this);
        },
        run: function(percent) {
            "dk.brics.tajs.directives.unreachable";
            var eased, hooks = Tween.propHooks[this.prop];
            this.pos = eased = jQuery.easing[this.easing](percent, this.options.duration * percent, 0, 1, this.options.duration);
            this.now = (this.end - this.start) * eased + this.start;
            if (this.options.step) {
                "dk.brics.tajs.directives.unreachable";
                this.options.step.call(this.elem, this.now, this);
            }
            if (hooks && hooks.set) {
                "dk.brics.tajs.directives.unreachable";
                hooks.set(this);
            } else {
                "dk.brics.tajs.directives.unreachable";
                Tween.propHooks._default.set(this);
            }
            return this;
        }
    };
    Tween.prototype.init.prototype = Tween.prototype;
    Tween.propHooks = {
        _default: {
            get: function(tween) {
                "dk.brics.tajs.directives.unreachable";
                var result;
                if (tween.elem[tween.prop] != null && (!tween.elem.style || tween.elem.style[tween.prop] == null)) {
                    "dk.brics.tajs.directives.unreachable";
                    return tween.elem[tween.prop];
                }
                // passing any value as a 4th parameter to .css will automatically
                // attempt a parseFloat and fallback to a string if the parse fails
                // so, simple values such as "10px" are parsed to Float.
                // complex values such as "rotate(1rad)" are returned as is.
                result = jQuery.css(tween.elem, tween.prop, false, "");
                // Empty strings, null, undefined and "auto" are converted to 0.
                return !result || result === "auto" ? 0 : result;
            },
            set: function(tween) {
                "dk.brics.tajs.directives.unreachable";
                // use step hook for back compat - use cssHook if its there - use .style if its
                // available and use plain properties where available
                if (jQuery.fx.step[tween.prop]) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.fx.step[tween.prop](tween);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (tween.elem.style && (tween.elem.style[jQuery.cssProps[tween.prop]] != null || jQuery.cssHooks[tween.prop])) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.style(tween.elem, tween.prop, tween.now + tween.unit);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        tween.elem[tween.prop] = tween.now;
                    }
                }
            }
        }
    };
    // Remove in 2.0 - this supports IE8's panic based approach
    // to setting things on disconnected nodes
    Tween.propHooks.scrollTop = Tween.propHooks.scrollLeft = {
        set: function(tween) {
            "dk.brics.tajs.directives.unreachable";
            if (tween.elem.nodeType && tween.elem.parentNode) {
                "dk.brics.tajs.directives.unreachable";
                tween.elem[tween.prop] = tween.now;
            }
        }
    };
    jQuery.each([ "toggle", "show", "hide" ], function(i, name) {
        var cssFn = jQuery.fn[name];
        jQuery.fn[name] = function(speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            return speed == null || typeof speed === "boolean" || // special check for .toggle( handler, handler, ... )
            !i && jQuery.isFunction(speed) && jQuery.isFunction(easing) ? cssFn.apply(this, arguments) : this.animate(genFx(name, true), speed, easing, callback);
        };
    });
    jQuery.fn.extend({
        fadeTo: function(speed, to, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            // show any hidden elements after setting opacity to 0
            return this.filter(isHidden).css("opacity", 0).show().end().animate({
                opacity: to
            }, speed, easing, callback);
        },
        animate: function(prop, speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            var empty = jQuery.isEmptyObject(prop), optall = jQuery.speed(speed, easing, callback), doAnimation = function() {
                "dk.brics.tajs.directives.unreachable";
                // Operate on a copy of prop so per-property easing won't be lost
                var anim = Animation(this, jQuery.extend({}, prop), optall);
                // Empty animations resolve immediately
                if (empty) {
                    "dk.brics.tajs.directives.unreachable";
                    anim.stop(true);
                }
            };
            return empty || optall.queue === false ? this.each(doAnimation) : this.queue(optall.queue, doAnimation);
        },
        stop: function(type, clearQueue, gotoEnd) {
            "dk.brics.tajs.directives.unreachable";
            var stopQueue = function(hooks) {
                "dk.brics.tajs.directives.unreachable";
                var stop = hooks.stop;
                delete hooks.stop;
                stop(gotoEnd);
            };
            if (typeof type !== "string") {
                "dk.brics.tajs.directives.unreachable";
                gotoEnd = clearQueue;
                clearQueue = type;
                type = undefined;
            }
            if (clearQueue && type !== false) {
                "dk.brics.tajs.directives.unreachable";
                this.queue(type || "fx", []);
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                var dequeue = true, index = type != null && type + "queueHooks", timers = jQuery.timers, data = jQuery._data(this);
                if (index) {
                    "dk.brics.tajs.directives.unreachable";
                    if (data[index] && data[index].stop) {
                        "dk.brics.tajs.directives.unreachable";
                        stopQueue(data[index]);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    for (index in data) {
                        "dk.brics.tajs.directives.unreachable";
                        if (data[index] && data[index].stop && rrun.test(index)) {
                            "dk.brics.tajs.directives.unreachable";
                            stopQueue(data[index]);
                        }
                    }
                }
                for (index = timers.length; index--; ) {
                    "dk.brics.tajs.directives.unreachable";
                    if (timers[index].elem === this && (type == null || timers[index].queue === type)) {
                        "dk.brics.tajs.directives.unreachable";
                        timers[index].anim.stop(gotoEnd);
                        dequeue = false;
                        timers.splice(index, 1);
                    }
                }
                // start the next in the queue if the last step wasn't forced
                // timers currently will call their complete callbacks, which will dequeue
                // but only if they were gotoEnd
                if (dequeue || !gotoEnd) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.dequeue(this, type);
                }
            });
        }
    });
    // Generate parameters to create a standard animation
    function genFx(type, includeWidth) {
        var which, attrs = {
            height: type
        }, i = 0;
        // if we include width, step value is 1 to do all cssExpand values,
        // if we don't include width, step value is 2 to skip over Left and Right
        for (;i < 4; i += 2 - includeWidth) {
            which = cssExpand[i];
            attrs["margin" + which] = attrs["padding" + which] = type;
        }
        if (includeWidth) {
            "dk.brics.tajs.directives.unreachable";
            attrs.opacity = attrs.width = type;
        }
        return attrs;
    }
    // Generate shortcuts for custom animations
    jQuery.each({
        slideDown: genFx("show"),
        slideUp: genFx("hide"),
        slideToggle: genFx("toggle"),
        fadeIn: {
            opacity: "show"
        },
        fadeOut: {
            opacity: "hide"
        },
        fadeToggle: {
            opacity: "toggle"
        }
    }, function(name, props) {
        jQuery.fn[name] = function(speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate(props, speed, easing, callback);
        };
    });
    jQuery.speed = function(speed, easing, fn) {
        "dk.brics.tajs.directives.unreachable";
        var opt = speed && typeof speed === "object" ? jQuery.extend({}, speed) : {
            complete: fn || !fn && easing || jQuery.isFunction(speed) && speed,
            duration: speed,
            easing: fn && easing || easing && !jQuery.isFunction(easing) && easing
        };
        opt.duration = jQuery.fx.off ? 0 : typeof opt.duration === "number" ? opt.duration : opt.duration in jQuery.fx.speeds ? jQuery.fx.speeds[opt.duration] : jQuery.fx.speeds._default;
        // normalize opt.queue - true/undefined/null -> "fx"
        if (opt.queue == null || opt.queue === true) {
            "dk.brics.tajs.directives.unreachable";
            opt.queue = "fx";
        }
        // Queueing
        opt.old = opt.complete;
        opt.complete = function() {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(opt.old)) {
                "dk.brics.tajs.directives.unreachable";
                opt.old.call(this);
            }
            if (opt.queue) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.dequeue(this, opt.queue);
            }
        };
        return opt;
    };
    jQuery.easing = {
        linear: function(p) {
            "dk.brics.tajs.directives.unreachable";
            return p;
        },
        swing: function(p) {
            "dk.brics.tajs.directives.unreachable";
            return .5 - Math.cos(p * Math.PI) / 2;
        }
    };
    jQuery.timers = [];
    jQuery.fx = Tween.prototype.init;
    jQuery.fx.tick = function() {
        "dk.brics.tajs.directives.unreachable";
        var timer, timers = jQuery.timers, i = 0;
        for (;i < timers.length; i++) {
            "dk.brics.tajs.directives.unreachable";
            timer = timers[i];
            // Checks the timer has not already been removed
            if (!timer() && timers[i] === timer) {
                "dk.brics.tajs.directives.unreachable";
                timers.splice(i--, 1);
            }
        }
        if (!timers.length) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.fx.stop();
        }
    };
    jQuery.fx.timer = function(timer) {
        "dk.brics.tajs.directives.unreachable";
        if (timer() && jQuery.timers.push(timer) && !timerId) {
            "dk.brics.tajs.directives.unreachable";
            timerId = setInterval(jQuery.fx.tick, jQuery.fx.interval);
        }
    };
    jQuery.fx.interval = 13;
    jQuery.fx.stop = function() {
        "dk.brics.tajs.directives.unreachable";
        clearInterval(timerId);
        timerId = null;
    };
    jQuery.fx.speeds = {
        slow: 600,
        fast: 200,
        // Default speed
        _default: 400
    };
    // Back Compat <1.8 extension point
    jQuery.fx.step = {};
    if (jQuery.expr && jQuery.expr.filters) {
        jQuery.expr.filters.animated = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.grep(jQuery.timers, function(fn) {
                "dk.brics.tajs.directives.unreachable";
                return elem === fn.elem;
            }).length;
        };
    }
    var rroot = /^(?:body|html)$/i;
    jQuery.fn.offset = function(options) {
        "dk.brics.tajs.directives.unreachable";
        if (arguments.length) {
            "dk.brics.tajs.directives.unreachable";
            return options === undefined ? this : this.each(function(i) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.offset.setOffset(this, options, i);
            });
        }
        var box, docElem, body, win, clientTop, clientLeft, scrollTop, scrollLeft, top, left, elem = this[0], doc = elem && elem.ownerDocument;
        if (!doc) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        if ((body = doc.body) === elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.offset.bodyOffset(elem);
        }
        docElem = doc.documentElement;
        // Make sure we're not dealing with a disconnected DOM node
        if (!jQuery.contains(docElem, elem)) {
            "dk.brics.tajs.directives.unreachable";
            return {
                top: 0,
                left: 0
            };
        }
        box = elem.getBoundingClientRect();
        win = getWindow(doc);
        clientTop = docElem.clientTop || body.clientTop || 0;
        clientLeft = docElem.clientLeft || body.clientLeft || 0;
        scrollTop = win.pageYOffset || docElem.scrollTop;
        scrollLeft = win.pageXOffset || docElem.scrollLeft;
        top = box.top + scrollTop - clientTop;
        left = box.left + scrollLeft - clientLeft;
        return {
            top: top,
            left: left
        };
    };
    jQuery.offset = {
        bodyOffset: function(body) {
            "dk.brics.tajs.directives.unreachable";
            var top = body.offsetTop, left = body.offsetLeft;
            if (jQuery.support.doesNotIncludeMarginInBodyOffset) {
                "dk.brics.tajs.directives.unreachable";
                top += parseFloat(jQuery.css(body, "marginTop")) || 0;
                left += parseFloat(jQuery.css(body, "marginLeft")) || 0;
            }
            return {
                top: top,
                left: left
            };
        },
        setOffset: function(elem, options, i) {
            "dk.brics.tajs.directives.unreachable";
            var position = jQuery.css(elem, "position");
            // set position first, in-case top/left are set even on static elem
            if (position === "static") {
                "dk.brics.tajs.directives.unreachable";
                elem.style.position = "relative";
            }
            var curElem = jQuery(elem), curOffset = curElem.offset(), curCSSTop = jQuery.css(elem, "top"), curCSSLeft = jQuery.css(elem, "left"), calculatePosition = (position === "absolute" || position === "fixed") && jQuery.inArray("auto", [ curCSSTop, curCSSLeft ]) > -1, props = {}, curPosition = {}, curTop, curLeft;
            // need to be able to calculate position if either top or left is auto and position is either absolute or fixed
            if (calculatePosition) {
                "dk.brics.tajs.directives.unreachable";
                curPosition = curElem.position();
                curTop = curPosition.top;
                curLeft = curPosition.left;
            } else {
                "dk.brics.tajs.directives.unreachable";
                curTop = parseFloat(curCSSTop) || 0;
                curLeft = parseFloat(curCSSLeft) || 0;
            }
            if (jQuery.isFunction(options)) {
                "dk.brics.tajs.directives.unreachable";
                options = options.call(elem, i, curOffset);
            }
            if (options.top != null) {
                "dk.brics.tajs.directives.unreachable";
                props.top = options.top - curOffset.top + curTop;
            }
            if (options.left != null) {
                "dk.brics.tajs.directives.unreachable";
                props.left = options.left - curOffset.left + curLeft;
            }
            if ("using" in options) {
                "dk.brics.tajs.directives.unreachable";
                options.using.call(elem, props);
            } else {
                "dk.brics.tajs.directives.unreachable";
                curElem.css(props);
            }
        }
    };
    jQuery.fn.extend({
        position: function() {
            "dk.brics.tajs.directives.unreachable";
            if (!this[0]) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var elem = this[0], // Get *real* offsetParent
            offsetParent = this.offsetParent(), // Get correct offsets
            offset = this.offset(), parentOffset = rroot.test(offsetParent[0].nodeName) ? {
                top: 0,
                left: 0
            } : offsetParent.offset();
            // Subtract element margins
            // note: when an element has margin: auto the offsetLeft and marginLeft
            // are the same in Safari causing offset.left to incorrectly be 0
            offset.top -= parseFloat(jQuery.css(elem, "marginTop")) || 0;
            offset.left -= parseFloat(jQuery.css(elem, "marginLeft")) || 0;
            // Add offsetParent borders
            parentOffset.top += parseFloat(jQuery.css(offsetParent[0], "borderTopWidth")) || 0;
            parentOffset.left += parseFloat(jQuery.css(offsetParent[0], "borderLeftWidth")) || 0;
            // Subtract the two offsets
            return {
                top: offset.top - parentOffset.top,
                left: offset.left - parentOffset.left
            };
        },
        offsetParent: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.map(function() {
                "dk.brics.tajs.directives.unreachable";
                var offsetParent = this.offsetParent || document.body;
                while (offsetParent && !rroot.test(offsetParent.nodeName) && jQuery.css(offsetParent, "position") === "static") {
                    "dk.brics.tajs.directives.unreachable";
                    offsetParent = offsetParent.offsetParent;
                }
                return offsetParent || document.body;
            });
        }
    });
    // Create scrollLeft and scrollTop methods
    jQuery.each({
        scrollLeft: "pageXOffset",
        scrollTop: "pageYOffset"
    }, function(method, prop) {
        var top = /Y/.test(prop);
        jQuery.fn[method] = function(val) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, function(elem, method, val) {
                "dk.brics.tajs.directives.unreachable";
                var win = getWindow(elem);
                if (val === undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    return win ? prop in win ? win[prop] : win.document.documentElement[method] : elem[method];
                }
                if (win) {
                    "dk.brics.tajs.directives.unreachable";
                    win.scrollTo(!top ? val : jQuery(win).scrollLeft(), top ? val : jQuery(win).scrollTop());
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    elem[method] = val;
                }
            }, method, val, arguments.length, null);
        };
    });
    function getWindow(elem) {
        "dk.brics.tajs.directives.unreachable";
        return jQuery.isWindow(elem) ? elem : elem.nodeType === 9 ? elem.defaultView || elem.parentWindow : false;
    }
    // Create innerHeight, innerWidth, height, width, outerHeight and outerWidth methods
    jQuery.each({
        Height: "height",
        Width: "width"
    }, function(name, type) {
        jQuery.each({
            padding: "inner" + name,
            content: type,
            "": "outer" + name
        }, function(defaultExtra, funcName) {
            // margin is only for outerHeight, outerWidth
            jQuery.fn[funcName] = function(margin, value) {
                "dk.brics.tajs.directives.unreachable";
                var chainable = arguments.length && (defaultExtra || typeof margin !== "boolean"), extra = defaultExtra || (margin === true || value === true ? "margin" : "border");
                return jQuery.access(this, function(elem, type, value) {
                    "dk.brics.tajs.directives.unreachable";
                    var doc;
                    if (jQuery.isWindow(elem)) {
                        "dk.brics.tajs.directives.unreachable";
                        // As of 5/8/2012 this will yield incorrect results for Mobile Safari, but there
                        // isn't a whole lot we can do. See pull request at this URL for discussion:
                        // https://github.com/jquery/jquery/pull/764
                        return elem.document.documentElement["client" + name];
                    }
                    // Get document width or height
                    if (elem.nodeType === 9) {
                        "dk.brics.tajs.directives.unreachable";
                        doc = elem.documentElement;
                        // Either scroll[Width/Height] or offset[Width/Height] or client[Width/Height], whichever is greatest
                        // unfortunately, this causes bug #3838 in IE6/8 only, but there is currently no good, small way to fix it.
                        return Math.max(elem.body["scroll" + name], doc["scroll" + name], elem.body["offset" + name], doc["offset" + name], doc["client" + name]);
                    }
                    return value === undefined ? // Get width or height on the element, requesting but not forcing parseFloat
                    jQuery.css(elem, type, value, extra) : // Set width or height on the element
                    jQuery.style(elem, type, value, extra);
                }, type, chainable ? margin : undefined, chainable);
            };
        });
    });
    // Expose jQuery to the global object
    window.jQuery = window.$ = jQuery;
    // Expose jQuery as an AMD module, but only for AMD loaders that
    // understand the issues with loading multiple versions of jQuery
    // in a page that all might call define(). The loader will indicate
    // they have special allowances for multiple jQuery versions by
    // specifying define.amd.jQuery = true. Register as a named module,
    // since jQuery can be concatenated with other files that may use define,
    // but not use a proper concatenation script that understands anonymous
    // AMD modules. A named AMD is safest and most robust way to register.
    // Lowercase jquery is used because AMD module names are derived from
    // file names, and jQuery is normally delivered in a lowercase file name.
    // Do this after creating the global so that if an AMD module wants to call
    // noConflict to hide this version of jQuery, it will work.
    if (typeof define === "function" && define.amd && define.amd.jQuery) {
        "dk.brics.tajs.directives.unreachable";
        define("jquery", [], function() {
            "dk.brics.tajs.directives.unreachable";
            return jQuery;
        });
    }
})(window);
