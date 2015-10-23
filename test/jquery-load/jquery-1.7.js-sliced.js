/*!
 * jQuery JavaScript Library v1.7
 * http://jquery.com/
 *
 * Copyright 2011, John Resig
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * Includes Sizzle.js
 * http://sizzlejs.com/
 * Copyright 2011, The Dojo Foundation
 * Released under the MIT, BSD, and GPL Licenses.
 *
 * Date: Thu Nov 3 16:18:21 2011 -0400
 */
(function(window, undefined) {
    // Use the correct document accordingly with window argument (sandbox)
    var document = window.document, navigator = window.navigator, location = window.location;
    var jQuery = function() {
        // Define a local copy of jQuery
        var jQuery = function(selector, context) {
            // The jQuery object is actually just the init constructor 'enhanced'
            return new jQuery.fn.init(selector, context, rootjQuery);
        }, // Map over jQuery in case of overwrite
        _jQuery = window.jQuery, // Map over the $ in case of overwrite
        _$ = window.$, // A central reference to the root jQuery(document)
        rootjQuery, // A simple way to check for HTML strings or ID strings
        // Prioritize #id over <tag> to avoid XSS via location.hash (#9521)
        quickExpr = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/, // Check if a string has a non-whitespace character in it
        rnotwhite = /\S/, // Used for trimming whitespace
        trimLeft = /^\s+/, trimRight = /\s+$/, // Check for digits
        rdigit = /\d/, // Match a standalone tag
        rsingleTag = /^<(\w+)\s*\/?>(?:<\/\1>)?$/, // JSON RegExp
        rvalidchars = /^[\],:{}\s]*$/, rvalidescape = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, rvalidtokens = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g, // Useragent RegExp
        rwebkit = /(webkit)[ \/]([\w.]+)/, ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/, rmsie = /(msie) ([\w.]+)/, rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/, // Matches dashed string for camelizing
        rdashAlpha = /-([a-z]|[0-9])/gi, rmsPrefix = /^-ms-/, // Used by jQuery.camelCase as callback to replace()
        fcamelCase = function(all, letter) {
            "dk.brics.tajs.directives.unreachable";
            return (letter + "").toUpperCase();
        }, // Keep a UserAgent string for use with jQuery.browser
        userAgent = navigator.userAgent, // For matching the engine and version of the browser
        browserMatch, // The deferred used on DOM ready
        readyList, // The ready event handler
        DOMContentLoaded, // Save a reference to some core methods
        toString = Object.prototype.toString, hasOwn = Object.prototype.hasOwnProperty, push = Array.prototype.push, slice = Array.prototype.slice, trim = String.prototype.trim, indexOf = Array.prototype.indexOf, // [[Class]] -> type pairs
        class2type = {};
        jQuery.fn = jQuery.prototype = {
            constructor: jQuery,
            init: function(selector, context, rootjQuery) {
                var match, elem, ret, doc;
                // Handle $(""), $(null), or $(undefined)
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
                // The body element only exists once, optimize finding it
                if (selector === "body" && !context && document.body) {
                    "dk.brics.tajs.directives.unreachable";
                    this.context = document;
                    this[0] = document.body;
                    this.selector = selector;
                    this.length = 1;
                    return this;
                }
                // Handle HTML strings
                if (typeof selector === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    // Are we dealing with HTML string or an ID?
                    if (selector.charAt(0) === "<" && selector.charAt(selector.length - 1) === ">" && selector.length >= 3) {
                        "dk.brics.tajs.directives.unreachable";
                        // Assume that strings that start and end with <> are HTML and skip the regex check
                        match = [ null, selector, null ];
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        match = quickExpr.exec(selector);
                    }
                    // Verify a match, and that no context was specified for #id
                    if (match && (match[1] || !context)) {
                        "dk.brics.tajs.directives.unreachable";
                        // HANDLE: $(html) -> $(array)
                        if (match[1]) {
                            "dk.brics.tajs.directives.unreachable";
                            context = context instanceof jQuery ? context[0] : context;
                            doc = context ? context.ownerDocument || context : document;
                            // If a single string is passed in and it's a single tag
                            // just do a createElement and skip the rest
                            ret = rsingleTag.exec(selector);
                            if (ret) {
                                "dk.brics.tajs.directives.unreachable";
                                if (jQuery.isPlainObject(context)) {
                                    "dk.brics.tajs.directives.unreachable";
                                    selector = [ document.createElement(ret[1]) ];
                                    jQuery.fn.attr.call(selector, context, true);
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    selector = [ doc.createElement(ret[1]) ];
                                }
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                ret = jQuery.buildFragment([ match[1] ], [ doc ]);
                                selector = (ret.cacheable ? jQuery.clone(ret.fragment) : ret.fragment).childNodes;
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
            jquery: "1.7",
            // The default length of a jQuery object is 0
            length: 0,
            // The number of elements contained in the matched element set
            size: function() {
                "dk.brics.tajs.directives.unreachable";
                return this.length;
            },
            toArray: function() {
                "dk.brics.tajs.directives.unreachable";
                return slice.call(this, 0);
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
                var ret = this.constructor();
                if (jQuery.isArray(elems)) {
                    "dk.brics.tajs.directives.unreachable";
                    push.apply(ret, elems);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.merge(ret, elems);
                }
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
                // Attach the listeners
                jQuery.bindReady();
                // Add the callback
                readyList.add(fn);
                return this;
            },
            eq: function(i) {
                "dk.brics.tajs.directives.unreachable";
                return i === -1 ? this.slice(i) : this.slice(i, +i + 1);
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
                return this.pushStack(slice.apply(this, arguments), "slice", slice.call(arguments).join(","));
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
            push: push,
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
                // Either a released hold or an DOMready/load event and not yet ready
                if (wait === true && !--jQuery.readyWait || wait !== true && !jQuery.isReady) {
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
                    readyList.fireWith(document, [ jQuery ]);
                    // Trigger any bound ready events
                    if (jQuery.fn.trigger) {
                        jQuery(document).trigger("ready").unbind("ready");
                    }
                }
            },
            bindReady: function() {
                if (readyList) {
                    return;
                }
                readyList = jQuery.Callbacks("once memory");
                // Catch cases where $(document).ready() is called after the
                // browser event has already occurred.
                if (document.readyState === "complete") {
                    "dk.brics.tajs.directives.unreachable";
                    // Handle it asynchronously to allow scripts the opportunity to delay ready
                    return setTimeout(jQuery.ready, 1);
                }
                // Mozilla, Opera and webkit nightlies currently support this event
                if (document.addEventListener) {
                    // Use the handy event callback
                    document.addEventListener("DOMContentLoaded", DOMContentLoaded, false);
                    // A fallback to window.onload, that will always work
                    window.addEventListener("load", jQuery.ready, false);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (document.attachEvent) {
                        "dk.brics.tajs.directives.unreachable";
                        // ensure firing before onload,
                        // maybe late but safe also for iframes
                        document.attachEvent("onreadystatechange", DOMContentLoaded);
                        // A fallback to window.onload, that will always work
                        window.attachEvent("onload", jQuery.ready);
                        // If IE and not a frame
                        // continually check to see if the document is ready
                        var toplevel = false;
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            toplevel = window.frameElement == null;
                        } catch (e) {}
                        if (document.documentElement.doScroll && toplevel) {
                            "dk.brics.tajs.directives.unreachable";
                            doScrollCheck();
                        }
                    }
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
            // A crude way of determining if an object is a window
            isWindow: function(obj) {
                return obj && typeof obj === "object" && "setInterval" in obj;
            },
            isNumeric: function(obj) {
                "dk.brics.tajs.directives.unreachable";
                return obj != null && rdigit.test(obj) && !isNaN(obj);
            },
            type: function(obj) {
                return obj == null ? "null" : class2type[toString.call(obj)] || "object";
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
                    if (obj.constructor && !hasOwn.call(obj, "constructor") && !hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
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
                    if(!hasOwn.call(obj, key)){
                        return false;
                    }
                }
                return key === undefined || true;
                for (key in obj) {}
                return key === undefined || hasOwn.call(obj, key);
            },
            isEmptyObject: function(obj) {
                "dk.brics.tajs.directives.unreachable";
                for (var name in obj) {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
                return true;
            },
            error: function(msg) {
                "dk.brics.tajs.directives.unreachable";
                throw msg;
            },
            parseJSON: function(data) {
                "dk.brics.tajs.directives.unreachable";
                if (typeof data !== "string" || !data) {
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
                if (data && rnotwhite.test(data)) {
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
            each: function(object, callback, args) {
                var name, i = 0, length = object.length, isObj = length === undefined || jQuery.isFunction(object);
                if (args) {
                    "dk.brics.tajs.directives.unreachable";
                    if (isObj) {
                        "dk.brics.tajs.directives.unreachable";
                        for (name in object) {
                            "dk.brics.tajs.directives.unreachable";
                            if (callback.apply(object[name], args) === false) {
                                "dk.brics.tajs.directives.unreachable";
                                break;
                            }
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (;i < length; ) {
                            "dk.brics.tajs.directives.unreachable";
                            if (callback.apply(object[i++], args) === false) {
                                "dk.brics.tajs.directives.unreachable";
                                break;
                            }
                        }
                    }
                } else {
                    if (isObj) {
                        for (name in object) {
                            if (callback.call(object[name], name, object[name]) === false) {
                                "dk.brics.tajs.directives.unreachable";
                                break;
                            }
                        }
                    } else {
                        for (;i < length; ) {
                            if (callback.call(object[i], i, object[i++]) === false) {
                                "dk.brics.tajs.directives.unreachable";
                                break;
                            }
                        }
                    }
                }
                return object;
            },
            // Use native String.trim function wherever possible
            trim: trim ? function(text) {
                "dk.brics.tajs.directives.unreachable";
                return text == null ? "" : trim.call(text);
            } : // Otherwise use our own trimming functionality
            function(text) {
                "dk.brics.tajs.directives.unreachable";
                return text == null ? "" : text.toString().replace(trimLeft, "").replace(trimRight, "");
            },
            // results is for internal usage only
            makeArray: function(array, results) {
                "dk.brics.tajs.directives.unreachable";
                var ret = results || [];
                if (array != null) {
                    "dk.brics.tajs.directives.unreachable";
                    // The window, strings (and functions) also have 'length'
                    // The extra typeof function check is to prevent crashes
                    // in Safari 2 (See: #3039)
                    // Tweaked logic slightly to handle Blackberry 4.7 RegExp issues #6930
                    var type = jQuery.type(array);
                    if (array.length == null || type === "string" || type === "function" || type === "regexp" || jQuery.isWindow(array)) {
                        "dk.brics.tajs.directives.unreachable";
                        push.call(ret, array);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.merge(ret, array);
                    }
                }
                return ret;
            },
            inArray: function(elem, array, i) {
                "dk.brics.tajs.directives.unreachable";
                var len;
                if (array) {
                    "dk.brics.tajs.directives.unreachable";
                    if (indexOf) {
                        "dk.brics.tajs.directives.unreachable";
                        return indexOf.call(array, elem, i);
                    }
                    len = array.length;
                    i = i ? i < 0 ? Math.max(0, len + i) : i : 0;
                    for (;i < len; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        // Skip accessing in sparse arrays
                        if (i in array && array[i] === elem) {
                            "dk.brics.tajs.directives.unreachable";
                            return i;
                        }
                    }
                }
                return -1;
            },
            merge: function(first, second) {
                "dk.brics.tajs.directives.unreachable";
                var i = first.length, j = 0;
                if (typeof second.length === "number") {
                    "dk.brics.tajs.directives.unreachable";
                    for (var l = second.length; j < l; j++) {
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
                var ret = [], retVal;
                inv = !!inv;
                // Go through the array, only saving the items
                // that pass the validator function
                for (var i = 0, length = elems.length; i < length; i++) {
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
                if (typeof context === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    var tmp = fn[context];
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
                var args = slice.call(arguments, 2), proxy = function() {
                    "dk.brics.tajs.directives.unreachable";
                    return fn.apply(context, args.concat(slice.call(arguments)));
                };
                // Set the guid of unique handler to the same of original handler, so it can be removed
                proxy.guid = fn.guid = fn.guid || proxy.guid || jQuery.guid++;
                return proxy;
            },
            // Mutifunctional method to get and set values to a collection
            // The value/s can optionally be executed if it's a function
            access: function(elems, key, value, exec, fn, pass) {
                "dk.brics.tajs.directives.unreachable";
                var length = elems.length;
                // Setting many attributes
                if (typeof key === "object") {
                    "dk.brics.tajs.directives.unreachable";
                    for (var k in key) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.access(elems, k, key[k], exec, fn, value);
                    }
                    return elems;
                }
                // Setting one attribute
                if (value !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    // Optionally, function values get executed if exec is true
                    exec = !pass && exec && jQuery.isFunction(value);
                    for (var i = 0; i < length; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        fn(elems[i], key, exec ? value.call(elems[i], i, fn(elems[i], key)) : value, pass);
                    }
                    return elems;
                }
                // Getting an attribute
                return length ? fn(elems[0], key) : undefined;
            },
            now: function() {
                return new Date().getTime();
            },
            // Use of jQuery.browser is frowned upon.
            // More details: http://docs.jquery.com/Utilities/jQuery.browser
            uaMatch: function(ua) {
                ua = ua.toLowerCase();
                var match = rwebkit.exec(ua) || ropera.exec(ua) || rmsie.exec(ua) || ua.indexOf("compatible") < 0 && rmozilla.exec(ua) || [];
                return {
                    browser: match[1] || "",
                    version: match[2] || "0"
                };
            },
            sub: function() {
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
            },
            browser: {}
        });
        // Populate the class2type map
        jQuery.each("Boolean Number String Function Array Date RegExp Object".split(" "), function(i, name) {
            class2type["[object " + name + "]"] = name.toLowerCase();
        });
        browserMatch = jQuery.uaMatch(userAgent);
        if (browserMatch.browser) {
            jQuery.browser[browserMatch.browser] = true;
            jQuery.browser.version = browserMatch.version;
        }
        // Deprecated, use jQuery.browser.webkit instead
        if (jQuery.browser.webkit) {
            jQuery.browser.safari = true;
        }
        // IE doesn't match non-breaking spaces with \s
        if (rnotwhite.test(" ")) {
            "dk.brics.tajs.directives.unreachable";
            trimLeft = /^[\s\xA0]+/;
            trimRight = /[\s\xA0]+$/;
        }
        // All jQuery objects should point back to these
        rootjQuery = jQuery(document);
        // Cleanup functions for the document ready method
        if (document.addEventListener) {
            DOMContentLoaded = function() {
                document.removeEventListener("DOMContentLoaded", DOMContentLoaded, false);
                jQuery.ready();
            };
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (document.attachEvent) {
                "dk.brics.tajs.directives.unreachable";
                DOMContentLoaded = function() {
                    "dk.brics.tajs.directives.unreachable";
                    // Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
                    if (document.readyState === "complete") {
                        "dk.brics.tajs.directives.unreachable";
                        document.detachEvent("onreadystatechange", DOMContentLoaded);
                        jQuery.ready();
                    }
                };
            }
        }
        // The DOM ready check for Internet Explorer
        function doScrollCheck() {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isReady) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            try {
                "dk.brics.tajs.directives.unreachable";
                // If IE is used, use the trick by Diego Perini
                // http://javascript.nwbox.com/IEContentLoaded/
                document.documentElement.doScroll("left");
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                setTimeout(doScrollCheck, 1);
                return;
            }
            // and execute any waiting functions
            jQuery.ready();
        }
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
        if (typeof define === "function" && define.amd && define.amd.jQuery) {
            "dk.brics.tajs.directives.unreachable";
            define("jquery", [], function() {
                "dk.brics.tajs.directives.unreachable";
                return jQuery;
            });
        }
        return jQuery;
    }();
    // String to Object flags format cache
    var flagsCache = {};
    // Convert String-formatted flags into Object-formatted ones and store in cache
    function createFlags(flags) {
        var object = flagsCache[flags] = {}, i, length;
        flags = flags.split(" ");
        for (i = 0, length = flags.length; i < length; i++) {
            object[flags[i]] = true;
        }
        return object;
    }
    /*
 * Create a callback list using the following parameters:
 *
 *	flags:	an optional list of space-separated flags that will change how
 *			the callback list behaves
 *
 * By default a callback list will act like an event callback list and can be
 * "fired" multiple times.
 *
 * Possible flags:
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
    jQuery.Callbacks = function(flags) {
        // Convert flags from String-formatted to Object-formatted
        // (we check in cache first)
        flags = flags ? flagsCache[flags] || createFlags(flags) : {};
        var // Actual callback list
        list = [], // Stack of fire calls for repeatable lists
        stack = [], // Last fire value (for non-forgettable lists)
        memory, // Flag to know if list is currently firing
        firing, // First callback to fire (used internally by add and fireWith)
        firingStart, // End of the loop when firing
        firingLength, // Index of currently firing callback (modified by remove if needed)
        firingIndex, // Add one or several callbacks to the list
        add = function(args) {
            var i, length, elem, type, actual;
            for (i = 0, length = args.length; i < length; i++) {
                elem = args[i];
                type = jQuery.type(elem);
                if (type === "array") {
                    "dk.brics.tajs.directives.unreachable";
                    // Inspect recursively
                    add(elem);
                } else {
                    if (type === "function") {
                        // Add if not in unique mode and callback is not in
                        if (!flags.unique || !self.has(elem)) {
                            list.push(elem);
                        }
                    }
                }
            }
        }, // Fire callbacks
        fire = function(context, args) {
            args = args || [];
            memory = !flags.memory || [ context, args ];
            firing = true;
            firingIndex = firingStart || 0;
            firingStart = 0;
            firingLength = list.length;
            for (;list && firingIndex < firingLength; firingIndex++) {
                if (list[firingIndex].apply(context, args) === false && flags.stopOnFalse) {
                    "dk.brics.tajs.directives.unreachable";
                    memory = true;
                    // Mark as halted
                    break;
                }
            }
            firing = false;
            if (list) {
                if (!flags.once) {
                    "dk.brics.tajs.directives.unreachable";
                    if (stack && stack.length) {
                        "dk.brics.tajs.directives.unreachable";
                        memory = stack.shift();
                        self.fireWith(memory[0], memory[1]);
                    }
                } else {
                    if (memory === true) {
                        "dk.brics.tajs.directives.unreachable";
                        self.disable();
                    } else {
                        list = [];
                    }
                }
            }
        }, // Actual Callbacks object
        self = {
            // Add a callback or a collection of callbacks to the list
            add: function() {
                if (list) {
                    var length = list.length;
                    add(arguments);
                    // Do we need to add the callbacks to the
                    // current firing batch?
                    if (firing) {
                        "dk.brics.tajs.directives.unreachable";
                        firingLength = list.length;
                    } else {
                        if (memory && memory !== true) {
                            "dk.brics.tajs.directives.unreachable";
                            firingStart = length;
                            fire(memory[0], memory[1]);
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
                    var args = arguments, argIndex = 0, argLength = args.length;
                    for (;argIndex < argLength; argIndex++) {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0; i < list.length; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (args[argIndex] === list[i]) {
                                "dk.brics.tajs.directives.unreachable";
                                // Handle firingIndex and firingLength
                                if (firing) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (i <= firingLength) {
                                        "dk.brics.tajs.directives.unreachable";
                                        firingLength--;
                                        if (i <= firingIndex) {
                                            "dk.brics.tajs.directives.unreachable";
                                            firingIndex--;
                                        }
                                    }
                                }
                                // Remove the element
                                list.splice(i--, 1);
                                // If we have some unicity property then
                                // we only need to do this once
                                if (flags.unique) {
                                    "dk.brics.tajs.directives.unreachable";
                                    break;
                                }
                            }
                        }
                    }
                }
                return this;
            },
            // Control if a given callback is in the list
            has: function(fn) {
                "dk.brics.tajs.directives.unreachable";
                if (list) {
                    "dk.brics.tajs.directives.unreachable";
                    var i = 0, length = list.length;
                    for (;i < length; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (fn === list[i]) {
                            "dk.brics.tajs.directives.unreachable";
                            return true;
                        }
                    }
                }
                return false;
            },
            // Remove all callbacks from the list
            empty: function() {
                "dk.brics.tajs.directives.unreachable";
                list = [];
                return this;
            },
            // Have the list do nothing anymore
            disable: function() {
                "dk.brics.tajs.directives.unreachable";
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
                "dk.brics.tajs.directives.unreachable";
                stack = undefined;
                if (!memory || memory === true) {
                    "dk.brics.tajs.directives.unreachable";
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
                if (stack) {
                    if (firing) {
                        "dk.brics.tajs.directives.unreachable";
                        if (!flags.once) {
                            "dk.brics.tajs.directives.unreachable";
                            stack.push([ context, args ]);
                        }
                    } else {
                        if (!(flags.once && memory)) {
                            fire(context, args);
                        }
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
                return !!memory;
            }
        };
        return self;
    };
    var // Static reference to slice
    sliceDeferred = [].slice;
    jQuery.extend({
        Deferred: function(func) {
            "dk.brics.tajs.directives.unreachable";
            var doneList = jQuery.Callbacks("once memory"), failList = jQuery.Callbacks("once memory"), progressList = jQuery.Callbacks("memory"), state = "pending", lists = {
                resolve: doneList,
                reject: failList,
                notify: progressList
            }, promise = {
                done: doneList.add,
                fail: failList.add,
                progress: progressList.add,
                state: function() {
                    "dk.brics.tajs.directives.unreachable";
                    return state;
                },
                // Deprecated
                isResolved: doneList.fired,
                isRejected: failList.fired,
                then: function(doneCallbacks, failCallbacks, progressCallbacks) {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.done(doneCallbacks).fail(failCallbacks).progress(progressCallbacks);
                    return this;
                },
                always: function() {
                    "dk.brics.tajs.directives.unreachable";
                    return deferred.done.apply(deferred, arguments).fail.apply(deferred, arguments);
                },
                pipe: function(fnDone, fnFail, fnProgress) {
                    "dk.brics.tajs.directives.unreachable";
                    return jQuery.Deferred(function(newDefer) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.each({
                            done: [ fnDone, "resolve" ],
                            fail: [ fnFail, "reject" ],
                            progress: [ fnProgress, "notify" ]
                        }, function(handler, data) {
                            "dk.brics.tajs.directives.unreachable";
                            var fn = data[0], action = data[1], returned;
                            if (jQuery.isFunction(fn)) {
                                "dk.brics.tajs.directives.unreachable";
                                deferred[handler](function() {
                                    "dk.brics.tajs.directives.unreachable";
                                    returned = fn.apply(this, arguments);
                                    if (returned && jQuery.isFunction(returned.promise)) {
                                        "dk.brics.tajs.directives.unreachable";
                                        returned.promise().then(newDefer.resolve, newDefer.reject, newDefer.notify);
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        newDefer[action + "With"](this === deferred ? newDefer : this, [ returned ]);
                                    }
                                });
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                deferred[handler](newDefer[action]);
                            }
                        });
                    }).promise();
                },
                // Get a promise for this deferred
                // If obj is provided, the promise aspect is added to the object
                promise: function(obj) {
                    "dk.brics.tajs.directives.unreachable";
                    if (obj == null) {
                        "dk.brics.tajs.directives.unreachable";
                        obj = promise;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (var key in promise) {
                            "dk.brics.tajs.directives.unreachable";
                            obj[key] = promise[key];
                        }
                    }
                    return obj;
                }
            }, deferred = promise.promise({}), key;
            for (key in lists) {
                "dk.brics.tajs.directives.unreachable";
                deferred[key] = lists[key].fire;
                deferred[key + "With"] = lists[key].fireWith;
            }
            // Handle state
            deferred.done(function() {
                "dk.brics.tajs.directives.unreachable";
                state = "resolved";
            }, failList.disable, progressList.lock).fail(function() {
                "dk.brics.tajs.directives.unreachable";
                state = "rejected";
            }, doneList.disable, progressList.lock);
            // Call given func if any
            if (func) {
                "dk.brics.tajs.directives.unreachable";
                func.call(deferred, deferred);
            }
            // All done!
            return deferred;
        },
        // Deferred helper
        when: function(firstParam) {
            "dk.brics.tajs.directives.unreachable";
            var args = sliceDeferred.call(arguments, 0), i = 0, length = args.length, pValues = new Array(length), count = length, pCount = length, deferred = length <= 1 && firstParam && jQuery.isFunction(firstParam.promise) ? firstParam : jQuery.Deferred(), promise = deferred.promise();
            function resolveFunc(i) {
                "dk.brics.tajs.directives.unreachable";
                return function(value) {
                    "dk.brics.tajs.directives.unreachable";
                    args[i] = arguments.length > 1 ? sliceDeferred.call(arguments, 0) : value;
                    if (!--count) {
                        "dk.brics.tajs.directives.unreachable";
                        deferred.resolveWith(deferred, args);
                    }
                };
            }
            function progressFunc(i) {
                "dk.brics.tajs.directives.unreachable";
                return function(value) {
                    "dk.brics.tajs.directives.unreachable";
                    pValues[i] = arguments.length > 1 ? sliceDeferred.call(arguments, 0) : value;
                    deferred.notifyWith(promise, pValues);
                };
            }
            if (length > 1) {
                "dk.brics.tajs.directives.unreachable";
                for (;i < length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (args[i] && args[i].promise && jQuery.isFunction(args[i].promise)) {
                        "dk.brics.tajs.directives.unreachable";
                        args[i].promise().then(resolveFunc(i), deferred.reject, progressFunc(i));
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        --count;
                    }
                }
                if (!count) {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.resolveWith(deferred, args);
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (deferred !== firstParam) {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.resolveWith(deferred, length ? [ firstParam ] : []);
                }
            }
            return promise;
        }
    });
    jQuery.support = function() {
        var div = document.createElement("div"), documentElement = document.documentElement, all, a, select, opt, input, marginDiv, support, fragment, body, testElementParent, testElement, testElementStyle, tds, events, eventName, i, isSupported;
        // Preliminary tests
        div.setAttribute("className", "t");
        div.innerHTML = "   <link/><table></table><a href='/a' style='top:1px;float:left;opacity:.55;'>a</a><input type='checkbox'/><nav></nav>";
        all = div.getElementsByTagName("*");
        a = div.getElementsByTagName("a")[0];
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
            opacity: /^0.55/.test(a.style.opacity),
            // Verify style float existence
            // (IE uses styleFloat instead of cssFloat)
            cssFloat: !!a.style.cssFloat,
            // Make sure unknown elements (like HTML5 elems) are handled appropriately
            unknownElems: !!div.getElementsByTagName("nav").length,
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
            // Will be defined later
            submitBubbles: true,
            changeBubbles: true,
            focusinBubbles: false,
            deleteExpando: true,
            noCloneEvent: true,
            inlineBlockNeedsLayout: false,
            shrinkWrapBlocks: false,
            reliableMarginRight: true
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
            div.attachEvent("onclick", function() {
                "dk.brics.tajs.directives.unreachable";
                // Cloning a node shouldn't copy over any
                // bound event handlers (IE does this)
                support.noCloneEvent = false;
            });
            div.cloneNode(true).fireEvent("onclick");
        }
        // Check if a radio maintains its value
        // after being appended to the DOM
        input = document.createElement("input");
        input.value = "t";
        input.setAttribute("type", "radio");
        support.radioValue = input.value === "t";
        input.setAttribute("checked", "checked");
        div.appendChild(input);
        fragment = document.createDocumentFragment();
        fragment.appendChild(div.lastChild);
        // WebKit doesn't clone checked state correctly in fragments
        support.checkClone = fragment.cloneNode(true).cloneNode(true).lastChild.checked;
        div.innerHTML = "";
        // Figure out if the W3C box model works as expected
        div.style.width = div.style.paddingLeft = "1px";
        // We don't want to do body-related feature tests on frameset
        // documents, which lack a body. So we use
        // document.getElementsByTagName("body")[0], which is undefined in
        // frameset documents, while document.body isn’t. (7398)
        body = document.getElementsByTagName("body")[0];
        // We use our own, invisible, body unless the body is already present
        // in which case we use a div (#9239)
        testElement = document.createElement(body ? "div" : "body");
        testElementStyle = {
            visibility: "hidden",
            width: 0,
            height: 0,
            border: 0,
            margin: 0,
            background: "none"
        };
        if (body) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.extend(testElementStyle, {
                position: "absolute",
                left: "-999px",
                top: "-999px"
            });
        }
        for (i in testElementStyle) {
            testElement.style[i] = testElementStyle[i];
        }
        testElement.appendChild(div);
        testElementParent = body || documentElement;
        testElementParent.insertBefore(testElement, testElementParent.firstChild);
        // Check if a disconnected checkbox will retain its checked
        // value of true after appended to the DOM (IE6/7)
        support.appendChecked = input.checked;
        support.boxModel = div.offsetWidth === 2;
        if ("zoom" in div.style) {
            // Check if natively block-level elements act like inline-block
            // elements when setting their display to 'inline' and giving
            // them layout
            // (IE < 8 does this)
            div.style.display = "inline";
            div.style.zoom = 1;
            support.inlineBlockNeedsLayout = div.offsetWidth === 2;
            // Check if elements with layout shrink-wrap their children
            // (IE 6 does this)
            div.style.display = "";
            div.innerHTML = "<div style='width:4px;'></div>";
            support.shrinkWrapBlocks = div.offsetWidth !== 2;
        }
        div.innerHTML = "<table><tr><td style='padding:0;border:0;display:none'></td><td>t</td></tr></table>";
        tds = div.getElementsByTagName("td");
        // Check if table cells still have offsetWidth/Height when they are set
        // to display:none and there are still other visible table cells in a
        // table row; if so, offsetWidth/Height are not reliable for use when
        // determining if an element has been hidden directly using
        // display:none (it is still safe to use offsets if a parent element is
        // hidden; don safety goggles and see bug #4512 for more information).
        // (only IE 8 fails this test)
        isSupported = tds[0].offsetHeight === 0;
        tds[0].style.display = "";
        tds[1].style.display = "none";
        // Check if empty table cells still have offsetWidth/Height
        // (IE < 8 fail this test)
        support.reliableHiddenOffsets = isSupported && tds[0].offsetHeight === 0;
        div.innerHTML = "";
        // Check if div with explicit width and no margin-right incorrectly
        // gets computed margin-right based on width of container. For more
        // info see bug #3333
        // Fails in WebKit before Feb 2011 nightlies
        // WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
        if (document.defaultView && document.defaultView.getComputedStyle) {
            marginDiv = document.createElement("div");
            marginDiv.style.width = "0";
            marginDiv.style.marginRight = "0";
            div.appendChild(marginDiv);
            support.reliableMarginRight = (parseInt((document.defaultView.getComputedStyle(marginDiv, null) || {
                marginRight: 0
            }).marginRight, 10) || 0) === 0;
        }
        // Technique from Juriy Zaytsev
        // http://perfectionkills.com/detecting-event-support-without-browser-sniffing/
        // We only care about the case where non-standard event systems
        // are used, namely in IE. Short-circuiting here helps us to
        // avoid an eval call (in setAttribute) which can cause CSP
        // to go haywire. See: https://developer.mozilla.org/en/Security/CSP
        if (div.attachEvent) {
            "dk.brics.tajs.directives.unreachable";
            for (i in {
                submit: 1,
                change: 1,
                focusin: 1
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
        // Run fixed position tests at doc ready to avoid a crash
        // related to the invisible body in IE8
        jQuery(function() {
            var container, outer, inner, table, td, offsetSupport, conMarginTop = 1, ptlm = "position:absolute;top:0;left:0;width:1px;height:1px;margin:0;", vb = "visibility:hidden;border:0;", style = "style='" + ptlm + "border:5px solid #000;padding:0;'", html = "<div " + style + "><div></div></div>" + "<table " + style + " cellpadding='0' cellspacing='0'>" + "<tr><td></td></tr></table>";
            // Reconstruct a container
            body = document.getElementsByTagName("body")[0];
            if (!body) {
                "dk.brics.tajs.directives.unreachable";
                // Return for frameset docs that don't have a body
                // These tests cannot be done
                return;
            }
            container = document.createElement("div");
            container.style.cssText = vb + "width:0;height:0;position:static;top:0;margin-top:" + conMarginTop + "px";
            body.insertBefore(container, body.firstChild);
            // Construct a test element
            testElement = document.createElement("div");
            testElement.style.cssText = ptlm + vb;
            testElement.innerHTML = html;
            container.appendChild(testElement);
            outer = testElement.firstChild;
            inner = outer.firstChild;
            td = outer.nextSibling.firstChild.firstChild;
            offsetSupport = {
                doesNotAddBorder: inner.offsetTop !== 5,
                doesAddBorderForTableAndCells: td.offsetTop === 5
            };
            inner.style.position = "fixed";
            inner.style.top = "20px";
            // safari subtracts parent border width here which is 5px
            offsetSupport.fixedPosition = inner.offsetTop === 20 || inner.offsetTop === 15;
            inner.style.position = inner.style.top = "";
            outer.style.overflow = "hidden";
            outer.style.position = "relative";
            offsetSupport.subtractsBorderForOverflowNotVisible = inner.offsetTop === -5;
            offsetSupport.doesNotIncludeMarginInBodyOffset = body.offsetTop !== conMarginTop;
            body.removeChild(container);
            testElement = container = null;
            jQuery.extend(support, offsetSupport);
        });
        testElement.innerHTML = "";
        testElementParent.removeChild(testElement);
        // Null connected elements to avoid leaks in IE
        testElement = fragment = select = opt = body = marginDiv = div = input = null;
        return support;
    }();
    // Keep track of boxModel
    jQuery.boxModel = jQuery.support.boxModel;
    var rbrace = /^(?:\{.*\}|\[.*\])$/, rmultiDash = /([A-Z])/g;
    jQuery.extend({
        cache: {},
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
            var privateCache, thisCache, ret, internalKey = jQuery.expando, getByName = typeof name === "string", // We have to handle DOM nodes and JS objects differently because IE6-7
            // can't GC object references properly across the DOM-JS boundary
            isNode = elem.nodeType, // Only DOM nodes need the global jQuery cache; JS object data is
            // attached directly to the object so GC can occur automatically
            cache = isNode ? jQuery.cache : elem, // Only defining an ID for JS objects if its cache already exists allows
            // the code to shortcut on the same path as a DOM node with no cache
            id = isNode ? elem[jQuery.expando] : elem[jQuery.expando] && jQuery.expando, isEvents = name === "events";
            // Avoid doing any more work than we need to when trying to get data on an
            // object that has no data at all
            if ((!id || !cache[id] || !isEvents && !pvt && !cache[id].data) && getByName && data === undefined) {
                return;
            }
            "dk.brics.tajs.directives.unreachable";
            if (!id) {
                "dk.brics.tajs.directives.unreachable";
                // Only DOM nodes need a new unique ID for each element since their data
                // ends up in the global cache
                if (isNode) {
                    "dk.brics.tajs.directives.unreachable";
                    elem[jQuery.expando] = id = ++jQuery.uuid;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    id = jQuery.expando;
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
            privateCache = thisCache = cache[id];
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
            // Users should not attempt to inspect the internal events object using jQuery.data,
            // it is undocumented and subject to change. But does anyone listen? No.
            if (isEvents && !thisCache[name]) {
                "dk.brics.tajs.directives.unreachable";
                return privateCache.events;
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
            var thisCache, i, l, // Reference to internal data cache key
            internalKey = jQuery.expando, isNode = elem.nodeType, // See jQuery.data for more information
            cache = isNode ? jQuery.cache : elem, // See jQuery.data for more information
            id = isNode ? elem[jQuery.expando] : jQuery.expando;
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
                    // Support space separated names
                    if (jQuery.isArray(name)) {
                        "dk.brics.tajs.directives.unreachable";
                        name = name;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (name in thisCache) {
                            "dk.brics.tajs.directives.unreachable";
                            name = [ name ];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // split the camel cased version by spaces
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
            // Browsers that fail expando deletion also refuse to delete expandos on
            // the window, but it will allow it on all other JS objects; other browsers
            // don't care
            // Ensure that `cache` is not a window object #10080
            if (jQuery.support.deleteExpando || !cache.setInterval) {
                "dk.brics.tajs.directives.unreachable";
                delete cache[id];
            } else {
                "dk.brics.tajs.directives.unreachable";
                cache[id] = null;
            }
            // We destroyed the cache and need to eliminate the expando on the node to avoid
            // false lookups in the cache for entries that no longer exist
            if (isNode) {
                "dk.brics.tajs.directives.unreachable";
                // IE does not allow us to delete expando properties from nodes,
                // nor does it have a removeAttribute function on Document nodes;
                // we must handle all of these cases
                if (jQuery.support.deleteExpando) {
                    "dk.brics.tajs.directives.unreachable";
                    delete elem[jQuery.expando];
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.removeAttribute) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.removeAttribute(jQuery.expando);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        elem[jQuery.expando] = null;
                    }
                }
            }
        },
        // For internal use only.
        _data: function(elem, name, data) {
            return jQuery.data(elem, name, data, true);
        },
        // A method for determining if a DOM node can handle the data expando
        acceptData: function(elem) {
            if (elem.nodeName) {
                var match = jQuery.noData[elem.nodeName.toLowerCase()];
                if (match) {
                    "dk.brics.tajs.directives.unreachable";
                    return !(match === true || elem.getAttribute("classid") !== match);
                }
            }
            return true;
        }
    });
    jQuery.fn.extend({
        data: function(key, value) {
            "dk.brics.tajs.directives.unreachable";
            var parts, attr, name, data = null;
            if (typeof key === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                if (this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    data = jQuery.data(this[0]);
                    if (this[0].nodeType === 1 && !jQuery._data(this[0], "parsedAttrs")) {
                        "dk.brics.tajs.directives.unreachable";
                        attr = this[0].attributes;
                        for (var i = 0, l = attr.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            name = attr[i].name;
                            if (name.indexOf("data-") === 0) {
                                "dk.brics.tajs.directives.unreachable";
                                name = jQuery.camelCase(name.substring(5));
                                dataAttr(this[0], name, data[name]);
                            }
                        }
                        jQuery._data(this[0], "parsedAttrs", true);
                    }
                }
                return data;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (typeof key === "object") {
                    "dk.brics.tajs.directives.unreachable";
                    return this.each(function() {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.data(this, key);
                    });
                }
            }
            parts = key.split(".");
            parts[1] = parts[1] ? "." + parts[1] : "";
            if (value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                data = this.triggerHandler("getData" + parts[1] + "!", [ parts[0] ]);
                // Try to fetch any internally stored data first
                if (data === undefined && this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    data = jQuery.data(this[0], key);
                    data = dataAttr(this[0], key, data);
                }
                return data === undefined && parts[1] ? this.data(parts[0]) : data;
            } else {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    var $this = jQuery(this), args = [ parts[0], value ];
                    $this.triggerHandler("setData" + parts[1] + "!", args);
                    jQuery.data(this, key, value);
                    $this.triggerHandler("changeData" + parts[1] + "!", args);
                });
            }
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
                    data = data === "true" ? true : data === "false" ? false : data === "null" ? null : jQuery.isNumeric(data) ? parseFloat(data) : rbrace.test(data) ? jQuery.parseJSON(data) : data;
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
        for (var name in obj) {
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
    function handleQueueMarkDefer(elem, type, src) {
        "dk.brics.tajs.directives.unreachable";
        var deferDataKey = type + "defer", queueDataKey = type + "queue", markDataKey = type + "mark", defer = jQuery._data(elem, deferDataKey);
        if (defer && (src === "queue" || !jQuery._data(elem, queueDataKey)) && (src === "mark" || !jQuery._data(elem, markDataKey))) {
            "dk.brics.tajs.directives.unreachable";
            // Give room for hard-coded callbacks to fire first
            // and eventually mark/queue something else on the element
            setTimeout(function() {
                "dk.brics.tajs.directives.unreachable";
                if (!jQuery._data(elem, queueDataKey) && !jQuery._data(elem, markDataKey)) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.removeData(elem, deferDataKey, true);
                    defer.fire();
                }
            }, 0);
        }
    }
    jQuery.extend({
        _mark: function(elem, type) {
            "dk.brics.tajs.directives.unreachable";
            if (elem) {
                "dk.brics.tajs.directives.unreachable";
                type = (type || "fx") + "mark";
                jQuery._data(elem, type, (jQuery._data(elem, type) || 0) + 1);
            }
        },
        _unmark: function(force, elem, type) {
            "dk.brics.tajs.directives.unreachable";
            if (force !== true) {
                "dk.brics.tajs.directives.unreachable";
                type = elem;
                elem = force;
                force = false;
            }
            if (elem) {
                "dk.brics.tajs.directives.unreachable";
                type = type || "fx";
                var key = type + "mark", count = force ? 0 : (jQuery._data(elem, key) || 1) - 1;
                if (count) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery._data(elem, key, count);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.removeData(elem, key, true);
                    handleQueueMarkDefer(elem, type, "mark");
                }
            }
        },
        queue: function(elem, type, data) {
            "dk.brics.tajs.directives.unreachable";
            var q;
            if (elem) {
                "dk.brics.tajs.directives.unreachable";
                type = (type || "fx") + "queue";
                q = jQuery._data(elem, type);
                // Speed up dequeue by getting out quickly if this is just a lookup
                if (data) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!q || jQuery.isArray(data)) {
                        "dk.brics.tajs.directives.unreachable";
                        q = jQuery._data(elem, type, jQuery.makeArray(data));
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        q.push(data);
                    }
                }
                return q || [];
            }
        },
        dequeue: function(elem, type) {
            "dk.brics.tajs.directives.unreachable";
            type = type || "fx";
            var queue = jQuery.queue(elem, type), fn = queue.shift(), hooks = {};
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
                jQuery._data(elem, type + ".run", hooks);
                fn.call(elem, function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.dequeue(elem, type);
                }, hooks);
            }
            if (!queue.length) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.removeData(elem, type + "queue " + type + ".run", true);
                handleQueueMarkDefer(elem, type, "queue");
            }
        }
    });
    jQuery.fn.extend({
        queue: function(type, data) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof type !== "string") {
                "dk.brics.tajs.directives.unreachable";
                data = type;
                type = "fx";
            }
            if (data === undefined) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.queue(this[0], type);
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                var queue = jQuery.queue(this, type, data);
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
        promise: function(type, object) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof type !== "string") {
                "dk.brics.tajs.directives.unreachable";
                object = type;
                type = undefined;
            }
            type = type || "fx";
            var defer = jQuery.Deferred(), elements = this, i = elements.length, count = 1, deferDataKey = type + "defer", queueDataKey = type + "queue", markDataKey = type + "mark", tmp;
            function resolve() {
                "dk.brics.tajs.directives.unreachable";
                if (!--count) {
                    "dk.brics.tajs.directives.unreachable";
                    defer.resolveWith(elements, [ elements ]);
                }
            }
            while (i--) {
                "dk.brics.tajs.directives.unreachable";
                if (tmp = jQuery.data(elements[i], deferDataKey, undefined, true) || (jQuery.data(elements[i], queueDataKey, undefined, true) || jQuery.data(elements[i], markDataKey, undefined, true)) && jQuery.data(elements[i], deferDataKey, jQuery.Callbacks("once memory"), true)) {
                    "dk.brics.tajs.directives.unreachable";
                    count++;
                    tmp.add(resolve);
                }
            }
            resolve();
            return defer.promise();
        }
    });
    var rclass = /[\n\t\r]/g, rspace = " ", rreturn = /\r/g, rtype = /^(?:button|input)$/i, rfocusable = /^(?:button|input|object|select|textarea)$/i, rclickable = /^a(?:rea)?$/i, rboolean = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i, getSetAttribute = jQuery.support.getSetAttribute, nodeHook, boolHook, fixSpecified;
    jQuery.fn.extend({
        attr: function(name, value) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, name, value, true, jQuery.attr);
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
            return jQuery.access(this, name, value, true, jQuery.prop);
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
                classNames = value.split(rspace);
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
            var classNames, i, l, elem, className, c, cl;
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(j) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).removeClass(value.call(this, j, this.className));
                });
            }
            if (value && typeof value === "string" || value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                classNames = (value || "").split(rspace);
                for (i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = this[i];
                    if (elem.nodeType === 1 && elem.className) {
                        "dk.brics.tajs.directives.unreachable";
                        if (value) {
                            "dk.brics.tajs.directives.unreachable";
                            className = (" " + elem.className + " ").replace(rclass, " ");
                            for (c = 0, cl = classNames.length; c < cl; c++) {
                                "dk.brics.tajs.directives.unreachable";
                                className = className.replace(" " + classNames[c] + " ", " ");
                            }
                            elem.className = jQuery.trim(className);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            elem.className = "";
                        }
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
                    var className, i = 0, self = jQuery(this), state = stateVal, classNames = value.split(rspace);
                    while (className = classNames[i++]) {
                        "dk.brics.tajs.directives.unreachable";
                        // check each className given, space seperated list
                        state = isBool ? state : !self.hasClass(className);
                        if(state){
                            self.addClass(className);
                        }else{
                            self.removeClass(className);
                        }
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
                    hooks = jQuery.valHooks[elem.nodeName.toLowerCase()] || jQuery.valHooks[elem.type];
                    if (hooks && "get" in hooks && (ret = hooks.get(elem, "value")) !== undefined) {
                        "dk.brics.tajs.directives.unreachable";
                        return ret;
                    }
                    ret = elem.value;
                    return typeof ret === "string" ? // handle most common string cases
                    ret.replace(rreturn, "") : // handle cases where value is null/undef or number
                    ret == null ? "" : ret;
                }
                return undefined;
            }
            isFunction = jQuery.isFunction(value);
            return this.each(function(i) {
                "dk.brics.tajs.directives.unreachable";
                var self = jQuery(this), val;
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
                hooks = jQuery.valHooks[this.nodeName.toLowerCase()] || jQuery.valHooks[this.type];
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
        attrFn: {
            val: true,
            css: true,
            html: true,
            text: true,
            data: true,
            width: true,
            height: true,
            offset: true
        },
        attr: function(elem, name, value, pass) {
            "dk.brics.tajs.directives.unreachable";
            var ret, hooks, notxml, nType = elem.nodeType;
            // don't get/set attributes on text, comment and attribute nodes
            if (!elem || nType === 3 || nType === 8 || nType === 2) {
                "dk.brics.tajs.directives.unreachable";
                return undefined;
            }
            if (pass && name in jQuery.attrFn) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery(elem)[name](value);
            }
            // Fallback to prop when attributes are not supported
            if (!("getAttribute" in elem)) {
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
                    return undefined;
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
            var propName, attrNames, name, l, i = 0;
            if (elem.nodeType === 1) {
                "dk.brics.tajs.directives.unreachable";
                attrNames = (value || "").split(rspace);
                l = attrNames.length;
                for (;i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    name = attrNames[i].toLowerCase();
                    propName = jQuery.propFix[name] || name;
                    // See #9699 for explanation of this approach (setting first, then removal)
                    jQuery.attr(elem, name, "");
                    elem.removeAttribute(getSetAttribute ? name : propName);
                    // Set corresponding property to false for boolean attributes
                    if (rboolean.test(name) && propName in elem) {
                        "dk.brics.tajs.directives.unreachable";
                        elem[propName] = false;
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
                return undefined;
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
    // Add the tabIndex propHook to attrHooks for back-compat (different case is intentional)
    jQuery.attrHooks.tabindex = jQuery.propHooks.tabIndex;
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
            id: true
        };
        // Use this for any attribute in IE6/7
        // This fixes almost every IE6/7 issue
        nodeHook = jQuery.valHooks.button = {
            get: function(elem, name) {
                "dk.brics.tajs.directives.unreachable";
                var ret;
                ret = elem.getAttributeNode(name);
                return ret && (fixSpecified[name] ? ret.nodeValue !== "" : ret.specified) ? ret.nodeValue : undefined;
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
                return ret.nodeValue = value + "";
            }
        };
        // Apply the nodeHook to tabindex
        jQuery.attrHooks.tabindex.set = nodeHook.set;
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
    var rnamespaces = /\.(.*)$/, rformElems = /^(?:textarea|input|select)$/i, rperiod = /\./g, rspaces = / /g, rescape = /[^\w\s.|`]/g, rtypenamespace = /^([^\.]*)?(?:\.(.+))?$/, rhoverHack = /\bhover(\.\S+)?/, rkeyEvent = /^key/, rmouseEvent = /^(?:mouse|contextmenu)|click/, rquickIs = /^(\w*)(?:#([\w\-]+))?(?:\.([\w\-]+))?$/, quickParse = function(selector) {
        "dk.brics.tajs.directives.unreachable";
        var quick = rquickIs.exec(selector);
        if (quick) {
            "dk.brics.tajs.directives.unreachable";
            //   0  1    2   3
            // [ _, tag, id, class ]
            quick[1] = (quick[1] || "").toLowerCase();
            quick[3] = quick[3] && new RegExp("(?:^|\\s)" + quick[3] + "(?:\\s|$)");
        }
        return quick;
    }, quickIs = function(elem, m) {
        "dk.brics.tajs.directives.unreachable";
        return (!m[1] || elem.nodeName.toLowerCase() === m[1]) && (!m[2] || elem.id === m[2]) && (!m[3] || m[3].test(elem.className));
    }, hoverHack = function(events) {
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
            var elemData, eventHandle, events, t, tns, type, namespaces, handleObj, handleObjIn, quick, handlers, special;
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
            types = hoverHack(types).split(" ");
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
                // Delegated event; pre-analyze selector so it's processed quickly on event dispatch
                if (selector) {
                    "dk.brics.tajs.directives.unreachable";
                    handleObj.quick = quickParse(selector);
                    if (!handleObj.quick && jQuery.expr.match.POS.test(selector)) {
                        "dk.brics.tajs.directives.unreachable";
                        handleObj.isPositional = true;
                    }
                }
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
        remove: function(elem, types, handler, selector) {
            var elemData = jQuery.hasData(elem) && jQuery._data(elem), t, tns, type, namespaces, origCount, j, events, special, handle, eventType, handleObj;
            if (!elemData || !(events = elemData.events)) {
                return;
            }
            "dk.brics.tajs.directives.unreachable";
            // Once for each type.namespace in types; type may be omitted
            types = hoverHack(types || "").split(" ");
            for (t = 0; t < types.length; t++) {
                "dk.brics.tajs.directives.unreachable";
                tns = rtypenamespace.exec(types[t]) || [];
                type = tns[1];
                namespaces = tns[2];
                // Unbind all events (on this namespace, if provided) for the element
                if (!type) {
                    "dk.brics.tajs.directives.unreachable";
                    namespaces = namespaces ? "." + namespaces : "";
                    for (j in events) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.remove(elem, j + namespaces, handler, selector);
                    }
                    return;
                }
                special = jQuery.event.special[type] || {};
                type = (selector ? special.delegateType : special.bindType) || type;
                eventType = events[type] || [];
                origCount = eventType.length;
                namespaces = namespaces ? new RegExp("(^|\\.)" + namespaces.split(".").sort().join("\\.(?:.*\\.)?") + "(\\.|$)") : null;
                // Only need to loop for special events or selective removal
                if (handler || namespaces || selector || special.remove) {
                    "dk.brics.tajs.directives.unreachable";
                    for (j = 0; j < eventType.length; j++) {
                        "dk.brics.tajs.directives.unreachable";
                        handleObj = eventType[j];
                        if (!handler || handler.guid === handleObj.guid) {
                            "dk.brics.tajs.directives.unreachable";
                            if (!namespaces || namespaces.test(handleObj.namespace)) {
                                "dk.brics.tajs.directives.unreachable";
                                if (!selector || selector === handleObj.selector || selector === "**" && handleObj.selector) {
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
                        }
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // Removing all events
                    eventType.length = 0;
                }
                // Remove generic event handler if we removed something and no more handlers exist
                // (avoids potential for endless recursion during removal of special event handlers)
                if (eventType.length === 0 && origCount !== eventType.length) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!special.teardown || special.teardown.call(elem, namespaces) === false) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.removeEvent(elem, type, elemData.handle);
                    }
                    delete events[type];
                }
            }
            // Remove the expando if it's no longer used
            if (jQuery.isEmptyObject(events)) {
                "dk.brics.tajs.directives.unreachable";
                handle = elemData.handle;
                if (handle) {
                    "dk.brics.tajs.directives.unreachable";
                    handle.elem = null;
                }
                // removeData also checks for emptiness and clears the expando if empty
                // so use it instead of delete
                jQuery.removeData(elem, [ "events", "handle" ], true);
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
            var type = event.type || event, namespaces = [], cache, exclusive, i, cur, old, ontype, special, handle, eventPath, bubbleType;
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
            event.namespace_re = event.namespace ? new RegExp("(^|\\.)" + namespaces.join("\\.(?:.*\\.)?") + "(\\.|$)") : null;
            ontype = type.indexOf(":") < 0 ? "on" + type : "";
            // triggerHandler() and global events don't bubble or run the default action
            if (onlyHandlers || !elem) {
                "dk.brics.tajs.directives.unreachable";
                event.preventDefault();
            }
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
                old = null;
                for (cur = elem.parentNode; cur; cur = cur.parentNode) {
                    "dk.brics.tajs.directives.unreachable";
                    eventPath.push([ cur, bubbleType ]);
                    old = cur;
                }
                // Only add window if we got to document (e.g., not plain obj or detached DOM)
                if (old && old === elem.ownerDocument) {
                    "dk.brics.tajs.directives.unreachable";
                    eventPath.push([ old.defaultView || old.parentWindow || window, bubbleType ]);
                }
            }
            // Fire handlers on the event path
            for (i = 0; i < eventPath.length; i++) {
                cur = eventPath[i][0];
                event.type = eventPath[i][1];
                handle = (jQuery._data(cur, "events") || {})[event.type] && jQuery._data(cur, "handle");
                if (handle) {
                    "dk.brics.tajs.directives.unreachable";
                    handle.apply(cur, data);
                }
                handle = ontype && cur[ontype];
                if (handle && jQuery.acceptData(cur)) {
                    "dk.brics.tajs.directives.unreachable";
                    handle.apply(cur, data);
                }
                if (event.isPropagationStopped()) {
                    "dk.brics.tajs.directives.unreachable";
                    break;
                }
            }
            event.type = type;
            // If nobody prevented the default action, do it now
            if (!event.isDefaultPrevented()) {
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
            var handlers = (jQuery._data(this, "events") || {})[event.type] || [], delegateCount = handlers.delegateCount, args = [].slice.call(arguments, 0), run_all = !event.exclusive && !event.namespace, specialHandle = (jQuery.event.special[event.type] || {}).handle, handlerQueue = [], i, j, cur, ret, selMatch, matched, matches, handleObj, sel, hit, related;
            // Use the fix-ed jQuery.Event rather than the (read-only) native event
            args[0] = event;
            event.delegateTarget = this;
            // Determine handlers that should run if there are delegated events
            // Avoid disabled elements in IE (#6911) and non-left-click bubbling in Firefox (#3861)
            if (delegateCount && !event.target.disabled && !(event.button && event.type === "click")) {
                "dk.brics.tajs.directives.unreachable";
                for (cur = event.target; cur != this; cur = cur.parentNode || this) {
                    "dk.brics.tajs.directives.unreachable";
                    selMatch = {};
                    matches = [];
                    for (i = 0; i < delegateCount; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        handleObj = handlers[i];
                        sel = handleObj.selector;
                        hit = selMatch[sel];
                        if (handleObj.isPositional) {
                            "dk.brics.tajs.directives.unreachable";
                            // Since .is() does not work for positionals; see http://jsfiddle.net/eJ4yd/3/
                            hit = (hit || (selMatch[sel] = jQuery(sel))).index(cur) >= 0;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (hit === undefined) {
                                "dk.brics.tajs.directives.unreachable";
                                hit = selMatch[sel] = handleObj.quick ? quickIs(cur, handleObj.quick) : jQuery(cur).is(sel);
                            }
                        }
                        if (hit) {
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
                        ret = (specialHandle || handleObj.handler).apply(matched.elem, args);
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
            props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement wheelDelta".split(" "),
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
            // For mouse/key events; add metaKey if it's not there (#3368, IE6/7/8)
            if (event.metaKey === undefined) {
                "dk.brics.tajs.directives.unreachable";
                event.metaKey = event.ctrlKey;
            }
            return fixHook.filter ? fixHook.filter(event, originalEvent) : event;
        },
        special: {
            ready: {
                // Make sure the ready event is setup
                setup: jQuery.bindReady
            },
            focus: {
                delegateType: "focusin",
                noBubble: true
            },
            blur: {
                delegateType: "focusout",
                noBubble: true
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
        if (elem.detachEvent) {
            "dk.brics.tajs.directives.unreachable";
            elem.detachEvent("on" + type, handle);
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
        jQuery.event.special[orig] = jQuery.event.special[fix] = {
            delegateType: fix,
            bindType: fix,
            handle: function(event) {
                "dk.brics.tajs.directives.unreachable";
                var target = this, related = event.relatedTarget, handleObj = event.handleObj, selector = handleObj.selector, oldType, ret;
                // For a real mouseover/out, always call the handler; for
                // mousenter/leave call the handler if related is outside the target.
                // NB: No relatedTarget if the mouse left/entered the browser window
                if (!related || handleObj.origType === event.type || related !== target && !jQuery.contains(target, related)) {
                    "dk.brics.tajs.directives.unreachable";
                    oldType = event.type;
                    event.type = handleObj.origType;
                    ret = handleObj.handler.apply(this, arguments);
                    event.type = oldType;
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
                    if (form && !form._submit_attached) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(form, "submit._submit", function(event) {
                            "dk.brics.tajs.directives.unreachable";
                            // Form was submitted, bubble the event up the tree
                            if (this.parentNode) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.event.simulate("submit", this.parentNode, event, true);
                            }
                        });
                        form._submit_attached = true;
                    }
                });
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
                            if (this._just_changed) {
                                "dk.brics.tajs.directives.unreachable";
                                this._just_changed = false;
                                jQuery.event.simulate("change", this, event, true);
                            }
                        });
                    }
                    return false;
                }
                // Delegated event; lazy-add a change handler on descendant inputs
                jQuery.event.add(this, "beforeactivate._change", function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = e.target;
                    if (rformElems.test(elem.nodeName) && !elem._change_attached) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(elem, "change._change", function(event) {
                            "dk.brics.tajs.directives.unreachable";
                            if (this.parentNode && !event.isSimulated) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.event.simulate("change", this.parentNode, event, true);
                            }
                        });
                        elem._change_attached = true;
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
                    // ( types-Object, data )
                    data = selector;
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
            return this.on.call(this, types, selector, data, fn, 1);
        },
        off: function(types, selector, fn) {
            if (types && types.preventDefault && types.handleObj) {
                "dk.brics.tajs.directives.unreachable";
                // ( event )  dispatched jQuery.Event
                var handleObj = types.handleObj;
                jQuery(types.delegateTarget).off(handleObj.namespace ? handleObj.type + "." + handleObj.namespace : handleObj.type, handleObj.selector, handleObj.handler);
                return this;
            }
            if (typeof types === "object") {
                "dk.brics.tajs.directives.unreachable";
                // ( types-object [, selector] )
                for (var type in types) {
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
            return arguments.length == 1 ? this.off(selector, "**") : this.off(types, selector, fn);
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
            return arguments.length > 0 ? this.bind(name, data, fn) : this.trigger(name);
        };
        if (jQuery.attrFn) {
            jQuery.attrFn[name] = true;
        }
        if (rkeyEvent.test(name)) {
            jQuery.event.fixHooks[name] = jQuery.event.keyHooks;
        }
        if (rmouseEvent.test(name)) {
            jQuery.event.fixHooks[name] = jQuery.event.mouseHooks;
        }
    });
    /*!
 * Sizzle CSS Selector Engine
 *  Copyright 2011, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
    (function() {
        var chunker = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g, expando = "sizcache" + "TAJS_UUID", done = 0, toString = Object.prototype.toString, hasDuplicate = false, baseHasDuplicate = true, rBackslash = /\\/g, rReturn = /\r\n/g, rNonWord = /\W/;
        // Here we check if the JavaScript engine is using some sort of
        // optimization where it does not always call our comparision
        // function. If that is the case, discard the hasDuplicate value.
        //   Thus far that includes Google Chrome.
        [ 0, 0 ].sort(function() {
            baseHasDuplicate = false;
            return 0;
        });
        var Sizzle = function(selector, context, results, seed) {
            "dk.brics.tajs.directives.unreachable";
            results = results || [];
            context = context || document;
            var origContext = context;
            if (context.nodeType !== 1 && context.nodeType !== 9) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            if (!selector || typeof selector !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return results;
            }
            var m, set, checkSet, extra, ret, cur, pop, i, prune = true, contextXML = Sizzle.isXML(context), parts = [], soFar = selector;
            // Reset the position of the chunker regexp (start from head)
            do {
                "dk.brics.tajs.directives.unreachable";
                chunker.exec("");
                m = chunker.exec(soFar);
                if (m) {
                    "dk.brics.tajs.directives.unreachable";
                    soFar = m[3];
                    parts.push(m[1]);
                    if (m[2]) {
                        "dk.brics.tajs.directives.unreachable";
                        extra = m[3];
                        break;
                    }
                }
            } while (m);
            if (parts.length > 1 && origPOS.exec(selector)) {
                "dk.brics.tajs.directives.unreachable";
                if (parts.length === 2 && Expr.relative[parts[0]]) {
                    "dk.brics.tajs.directives.unreachable";
                    set = posProcess(parts[0] + parts[1], context, seed);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    set = Expr.relative[parts[0]] ? [ context ] : Sizzle(parts.shift(), context);
                    while (parts.length) {
                        "dk.brics.tajs.directives.unreachable";
                        selector = parts.shift();
                        if (Expr.relative[selector]) {
                            "dk.brics.tajs.directives.unreachable";
                            selector += parts.shift();
                        }
                        set = posProcess(selector, set, seed);
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                // Take a shortcut and set the context if the root selector is an ID
                // (but not if it'll be faster if the inner selector is an ID)
                if (!seed && parts.length > 1 && context.nodeType === 9 && !contextXML && Expr.match.ID.test(parts[0]) && !Expr.match.ID.test(parts[parts.length - 1])) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = Sizzle.find(parts.shift(), context, contextXML);
                    context = ret.expr ? Sizzle.filter(ret.expr, ret.set)[0] : ret.set[0];
                }
                if (context) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = seed ? {
                        expr: parts.pop(),
                        set: makeArray(seed)
                    } : Sizzle.find(parts.pop(), parts.length === 1 && (parts[0] === "~" || parts[0] === "+") && context.parentNode ? context.parentNode : context, contextXML);
                    set = ret.expr ? Sizzle.filter(ret.expr, ret.set) : ret.set;
                    if (parts.length > 0) {
                        "dk.brics.tajs.directives.unreachable";
                        checkSet = makeArray(set);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        prune = false;
                    }
                    while (parts.length) {
                        "dk.brics.tajs.directives.unreachable";
                        cur = parts.pop();
                        pop = cur;
                        if (!Expr.relative[cur]) {
                            "dk.brics.tajs.directives.unreachable";
                            cur = "";
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            pop = parts.pop();
                        }
                        if (pop == null) {
                            "dk.brics.tajs.directives.unreachable";
                            pop = context;
                        }
                        Expr.relative[cur](checkSet, pop, contextXML);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    checkSet = parts = [];
                }
            }
            if (!checkSet) {
                "dk.brics.tajs.directives.unreachable";
                checkSet = set;
            }
            if (!checkSet) {
                "dk.brics.tajs.directives.unreachable";
                Sizzle.error(cur || selector);
            }
            if (toString.call(checkSet) === "[object Array]") {
                "dk.brics.tajs.directives.unreachable";
                if (!prune) {
                    "dk.brics.tajs.directives.unreachable";
                    results.push.apply(results, checkSet);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (context && context.nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        for (i = 0; checkSet[i] != null; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (checkSet[i] && (checkSet[i] === true || checkSet[i].nodeType === 1 && Sizzle.contains(context, checkSet[i]))) {
                                "dk.brics.tajs.directives.unreachable";
                                results.push(set[i]);
                            }
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (i = 0; checkSet[i] != null; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (checkSet[i] && checkSet[i].nodeType === 1) {
                                "dk.brics.tajs.directives.unreachable";
                                results.push(set[i]);
                            }
                        }
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                makeArray(checkSet, results);
            }
            if (extra) {
                "dk.brics.tajs.directives.unreachable";
                Sizzle(extra, origContext, results, seed);
                Sizzle.uniqueSort(results);
            }
            return results;
        };
        Sizzle.uniqueSort = function(results) {
            "dk.brics.tajs.directives.unreachable";
            if (sortOrder) {
                "dk.brics.tajs.directives.unreachable";
                hasDuplicate = baseHasDuplicate;
                results.sort(sortOrder);
                if (hasDuplicate) {
                    "dk.brics.tajs.directives.unreachable";
                    for (var i = 1; i < results.length; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (results[i] === results[i - 1]) {
                            "dk.brics.tajs.directives.unreachable";
                            results.splice(i--, 1);
                        }
                    }
                }
            }
            return results;
        };
        Sizzle.matches = function(expr, set) {
            "dk.brics.tajs.directives.unreachable";
            return Sizzle(expr, null, null, set);
        };
        Sizzle.matchesSelector = function(node, expr) {
            "dk.brics.tajs.directives.unreachable";
            return Sizzle(expr, null, null, [ node ]).length > 0;
        };
        Sizzle.find = function(expr, context, isXML) {
            "dk.brics.tajs.directives.unreachable";
            var set, i, len, match, type, left;
            if (!expr) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            for (i = 0, len = Expr.order.length; i < len; i++) {
                "dk.brics.tajs.directives.unreachable";
                type = Expr.order[i];
                if (match = Expr.leftMatch[type].exec(expr)) {
                    "dk.brics.tajs.directives.unreachable";
                    left = match[1];
                    match.splice(1, 1);
                    if (left.substr(left.length - 1) !== "\\") {
                        "dk.brics.tajs.directives.unreachable";
                        match[1] = (match[1] || "").replace(rBackslash, "");
                        set = Expr.find[type](match, context, isXML);
                        if (set != null) {
                            "dk.brics.tajs.directives.unreachable";
                            expr = expr.replace(Expr.match[type], "");
                            break;
                        }
                    }
                }
            }
            if (!set) {
                "dk.brics.tajs.directives.unreachable";
                set = typeof context.getElementsByTagName !== "undefined" ? context.getElementsByTagName("*") : [];
            }
            return {
                set: set,
                expr: expr
            };
        };
        Sizzle.filter = function(expr, set, inplace, not) {
            "dk.brics.tajs.directives.unreachable";
            var match, anyFound, type, found, item, filter, left, i, pass, old = expr, result = [], curLoop = set, isXMLFilter = set && set[0] && Sizzle.isXML(set[0]);
            while (expr && set.length) {
                "dk.brics.tajs.directives.unreachable";
                for (type in Expr.filter) {
                    "dk.brics.tajs.directives.unreachable";
                    if ((match = Expr.leftMatch[type].exec(expr)) != null && match[2]) {
                        "dk.brics.tajs.directives.unreachable";
                        filter = Expr.filter[type];
                        left = match[1];
                        anyFound = false;
                        match.splice(1, 1);
                        if (left.substr(left.length - 1) === "\\") {
                            "dk.brics.tajs.directives.unreachable";
                            continue;
                        }
                        if (curLoop === result) {
                            "dk.brics.tajs.directives.unreachable";
                            result = [];
                        }
                        if (Expr.preFilter[type]) {
                            "dk.brics.tajs.directives.unreachable";
                            match = Expr.preFilter[type](match, curLoop, inplace, result, not, isXMLFilter);
                            if (!match) {
                                "dk.brics.tajs.directives.unreachable";
                                anyFound = found = true;
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (match === true) {
                                    "dk.brics.tajs.directives.unreachable";
                                    continue;
                                }
                            }
                        }
                        if (match) {
                            "dk.brics.tajs.directives.unreachable";
                            for (i = 0; (item = curLoop[i]) != null; i++) {
                                "dk.brics.tajs.directives.unreachable";
                                if (item) {
                                    "dk.brics.tajs.directives.unreachable";
                                    found = filter(item, match, i, curLoop);
                                    pass = not ^ found;
                                    if (inplace && found != null) {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (pass) {
                                            "dk.brics.tajs.directives.unreachable";
                                            anyFound = true;
                                        } else {
                                            "dk.brics.tajs.directives.unreachable";
                                            curLoop[i] = false;
                                        }
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (pass) {
                                            "dk.brics.tajs.directives.unreachable";
                                            result.push(item);
                                            anyFound = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (found !== undefined) {
                            "dk.brics.tajs.directives.unreachable";
                            if (!inplace) {
                                "dk.brics.tajs.directives.unreachable";
                                curLoop = result;
                            }
                            expr = expr.replace(Expr.match[type], "");
                            if (!anyFound) {
                                "dk.brics.tajs.directives.unreachable";
                                return [];
                            }
                            break;
                        }
                    }
                }
                // Improper expression
                if (expr === old) {
                    "dk.brics.tajs.directives.unreachable";
                    if (anyFound == null) {
                        "dk.brics.tajs.directives.unreachable";
                        Sizzle.error(expr);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        break;
                    }
                }
                old = expr;
            }
            return curLoop;
        };
        Sizzle.error = function(msg) {
            "dk.brics.tajs.directives.unreachable";
            throw "Syntax error, unrecognized expression: " + msg;
        };
        /**
 * Utility function for retreiving the text value of an array of DOM nodes
 * @param {Array|Element} elem
 */
        var getText = Sizzle.getText = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            var i, node, nodeType = elem.nodeType, ret = "";
            if (nodeType) {
                "dk.brics.tajs.directives.unreachable";
                if (nodeType === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    // Use textContent || innerText for elements
                    if (typeof elem.textContent === "string") {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.textContent;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (typeof elem.innerText === "string") {
                            "dk.brics.tajs.directives.unreachable";
                            // Replace IE's carriage returns
                            return elem.innerText.replace(rReturn, "");
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // Traverse it's children
                            for (elem = elem.firstChild; elem; elem = elem.nextSibling) {
                                "dk.brics.tajs.directives.unreachable";
                                ret += getText(elem);
                            }
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
                for (i = 0; node = elem[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    // Do not traverse comment nodes
                    if (node.nodeType !== 8) {
                        "dk.brics.tajs.directives.unreachable";
                        ret += getText(node);
                    }
                }
            }
            return ret;
        };
        var Expr = Sizzle.selectors = {
            order: [ "ID", "NAME", "TAG" ],
            match: {
                ID: /#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
                CLASS: /\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
                NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,
                ATTR: /\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(?:(['"])(.*?)\3|(#?(?:[\w\u00c0-\uFFFF\-]|\\.)*)|)|)\s*\]/,
                TAG: /^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,
                CHILD: /:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/,
                POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,
                PSEUDO: /:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/
            },
            leftMatch: {},
            attrMap: {
                "class": "className",
                "for": "htmlFor"
            },
            attrHandle: {
                href: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.getAttribute("href");
                },
                type: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.getAttribute("type");
                }
            },
            relative: {
                "+": function(checkSet, part) {
                    "dk.brics.tajs.directives.unreachable";
                    var isPartStr = typeof part === "string", isTag = isPartStr && !rNonWord.test(part), isPartStrNotTag = isPartStr && !isTag;
                    if (isTag) {
                        "dk.brics.tajs.directives.unreachable";
                        part = part.toLowerCase();
                    }
                    for (var i = 0, l = checkSet.length, elem; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem = checkSet[i]) {
                            "dk.brics.tajs.directives.unreachable";
                            while ((elem = elem.previousSibling) && elem.nodeType !== 1) {}
                            checkSet[i] = isPartStrNotTag || elem && elem.nodeName.toLowerCase() === part ? elem || false : elem === part;
                        }
                    }
                    if (isPartStrNotTag) {
                        "dk.brics.tajs.directives.unreachable";
                        Sizzle.filter(part, checkSet, true);
                    }
                },
                ">": function(checkSet, part) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem, isPartStr = typeof part === "string", i = 0, l = checkSet.length;
                    if (isPartStr && !rNonWord.test(part)) {
                        "dk.brics.tajs.directives.unreachable";
                        part = part.toLowerCase();
                        for (;i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            elem = checkSet[i];
                            if (elem) {
                                "dk.brics.tajs.directives.unreachable";
                                var parent = elem.parentNode;
                                checkSet[i] = parent.nodeName.toLowerCase() === part ? parent : false;
                            }
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (;i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            elem = checkSet[i];
                            if (elem) {
                                "dk.brics.tajs.directives.unreachable";
                                checkSet[i] = isPartStr ? elem.parentNode : elem.parentNode === part;
                            }
                        }
                        if (isPartStr) {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle.filter(part, checkSet, true);
                        }
                    }
                },
                "": function(checkSet, part, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    var nodeCheck, doneName = done++, checkFn = dirCheck;
                    if (typeof part === "string" && !rNonWord.test(part)) {
                        "dk.brics.tajs.directives.unreachable";
                        part = part.toLowerCase();
                        nodeCheck = part;
                        checkFn = dirNodeCheck;
                    }
                    checkFn("parentNode", part, doneName, checkSet, nodeCheck, isXML);
                },
                "~": function(checkSet, part, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    var nodeCheck, doneName = done++, checkFn = dirCheck;
                    if (typeof part === "string" && !rNonWord.test(part)) {
                        "dk.brics.tajs.directives.unreachable";
                        part = part.toLowerCase();
                        nodeCheck = part;
                        checkFn = dirNodeCheck;
                    }
                    checkFn("previousSibling", part, doneName, checkSet, nodeCheck, isXML);
                }
            },
            find: {
                ID: function(match, context, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof context.getElementById !== "undefined" && !isXML) {
                        "dk.brics.tajs.directives.unreachable";
                        var m = context.getElementById(match[1]);
                        // Check parentNode to catch when Blackberry 4.6 returns
                        // nodes that are no longer in the document #6963
                        return m && m.parentNode ? [ m ] : [];
                    }
                },
                NAME: function(match, context) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof context.getElementsByName !== "undefined") {
                        "dk.brics.tajs.directives.unreachable";
                        var ret = [], results = context.getElementsByName(match[1]);
                        for (var i = 0, l = results.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (results[i].getAttribute("name") === match[1]) {
                                "dk.brics.tajs.directives.unreachable";
                                ret.push(results[i]);
                            }
                        }
                        return ret.length === 0 ? null : ret;
                    }
                },
                TAG: function(match, context) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof context.getElementsByTagName !== "undefined") {
                        "dk.brics.tajs.directives.unreachable";
                        return context.getElementsByTagName(match[1]);
                    }
                }
            },
            preFilter: {
                CLASS: function(match, curLoop, inplace, result, not, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    match = " " + match[1].replace(rBackslash, "") + " ";
                    if (isXML) {
                        "dk.brics.tajs.directives.unreachable";
                        return match;
                    }
                    for (var i = 0, elem; (elem = curLoop[i]) != null; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem) {
                            "dk.brics.tajs.directives.unreachable";
                            if (not ^ (elem.className && (" " + elem.className + " ").replace(/[\t\n\r]/g, " ").indexOf(match) >= 0)) {
                                "dk.brics.tajs.directives.unreachable";
                                if (!inplace) {
                                    "dk.brics.tajs.directives.unreachable";
                                    result.push(elem);
                                }
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (inplace) {
                                    "dk.brics.tajs.directives.unreachable";
                                    curLoop[i] = false;
                                }
                            }
                        }
                    }
                    return false;
                },
                ID: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    return match[1].replace(rBackslash, "");
                },
                TAG: function(match, curLoop) {
                    "dk.brics.tajs.directives.unreachable";
                    return match[1].replace(rBackslash, "").toLowerCase();
                },
                CHILD: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    if (match[1] === "nth") {
                        "dk.brics.tajs.directives.unreachable";
                        if (!match[2]) {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle.error(match[0]);
                        }
                        match[2] = match[2].replace(/^\+|\s*/g, "");
                        // parse equations like 'even', 'odd', '5', '2n', '3n+2', '4n-1', '-n+6'
                        var test = /(-?)(\d*)(?:n([+\-]?\d*))?/.exec(match[2] === "even" && "2n" || match[2] === "odd" && "2n+1" || !/\D/.test(match[2]) && "0n+" + match[2] || match[2]);
                        // calculate the numbers (first)n+(last) including if they are negative
                        match[2] = test[1] + (test[2] || 1) - 0;
                        match[3] = test[3] - 0;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (match[2]) {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle.error(match[0]);
                        }
                    }
                    // TODO: Move to normal caching system
                    match[0] = done++;
                    return match;
                },
                ATTR: function(match, curLoop, inplace, result, not, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = match[1] = match[1].replace(rBackslash, "");
                    if (!isXML && Expr.attrMap[name]) {
                        "dk.brics.tajs.directives.unreachable";
                        match[1] = Expr.attrMap[name];
                    }
                    // Handle if an un-quoted value was used
                    match[4] = (match[4] || match[5] || "").replace(rBackslash, "");
                    if (match[2] === "~=") {
                        "dk.brics.tajs.directives.unreachable";
                        match[4] = " " + match[4] + " ";
                    }
                    return match;
                },
                PSEUDO: function(match, curLoop, inplace, result, not) {
                    "dk.brics.tajs.directives.unreachable";
                    if (match[1] === "not") {
                        "dk.brics.tajs.directives.unreachable";
                        // If we're dealing with a complex expression, or a simple one
                        if ((chunker.exec(match[3]) || "").length > 1 || /^\w/.test(match[3])) {
                            "dk.brics.tajs.directives.unreachable";
                            match[3] = Sizzle(match[3], null, null, curLoop);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            var ret = Sizzle.filter(match[3], curLoop, inplace, true ^ not);
                            if (!inplace) {
                                "dk.brics.tajs.directives.unreachable";
                                result.push.apply(result, ret);
                            }
                            return false;
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (Expr.match.POS.test(match[0]) || Expr.match.CHILD.test(match[0])) {
                            "dk.brics.tajs.directives.unreachable";
                            return true;
                        }
                    }
                    return match;
                },
                POS: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    match.unshift(true);
                    return match;
                }
            },
            filters: {
                enabled: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.disabled === false && elem.type !== "hidden";
                },
                disabled: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.disabled === true;
                },
                checked: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.checked === true;
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
                    return !!elem.firstChild;
                },
                empty: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return !elem.firstChild;
                },
                has: function(elem, i, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return !!Sizzle(match[3], elem).length;
                },
                header: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return /h\d/i.test(elem.nodeName);
                },
                text: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var attr = elem.getAttribute("type"), type = elem.type;
                    // IE6 and 7 will map elem.type to 'text' for new HTML5 types (search, etc) 
                    // use getAttribute instead to test this case
                    return elem.nodeName.toLowerCase() === "input" && "text" === type && (attr === type || attr === null);
                },
                radio: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeName.toLowerCase() === "input" && "radio" === elem.type;
                },
                checkbox: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeName.toLowerCase() === "input" && "checkbox" === elem.type;
                },
                file: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeName.toLowerCase() === "input" && "file" === elem.type;
                },
                password: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeName.toLowerCase() === "input" && "password" === elem.type;
                },
                submit: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = elem.nodeName.toLowerCase();
                    return (name === "input" || name === "button") && "submit" === elem.type;
                },
                image: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeName.toLowerCase() === "input" && "image" === elem.type;
                },
                reset: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = elem.nodeName.toLowerCase();
                    return (name === "input" || name === "button") && "reset" === elem.type;
                },
                button: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = elem.nodeName.toLowerCase();
                    return name === "input" && "button" === elem.type || name === "button";
                },
                input: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return /input|select|textarea|button/i.test(elem.nodeName);
                },
                focus: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem === elem.ownerDocument.activeElement;
                }
            },
            setFilters: {
                first: function(elem, i) {
                    "dk.brics.tajs.directives.unreachable";
                    return i === 0;
                },
                last: function(elem, i, match, array) {
                    "dk.brics.tajs.directives.unreachable";
                    return i === array.length - 1;
                },
                even: function(elem, i) {
                    "dk.brics.tajs.directives.unreachable";
                    return i % 2 === 0;
                },
                odd: function(elem, i) {
                    "dk.brics.tajs.directives.unreachable";
                    return i % 2 === 1;
                },
                lt: function(elem, i, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return i < match[3] - 0;
                },
                gt: function(elem, i, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return i > match[3] - 0;
                },
                nth: function(elem, i, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return match[3] - 0 === i;
                },
                eq: function(elem, i, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return match[3] - 0 === i;
                }
            },
            filter: {
                PSEUDO: function(elem, match, i, array) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = match[1], filter = Expr.filters[name];
                    if (filter) {
                        "dk.brics.tajs.directives.unreachable";
                        return filter(elem, i, match, array);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (name === "contains") {
                            "dk.brics.tajs.directives.unreachable";
                            return (elem.textContent || elem.innerText || getText([ elem ]) || "").indexOf(match[3]) >= 0;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (name === "not") {
                                "dk.brics.tajs.directives.unreachable";
                                var not = match[3];
                                for (var j = 0, l = not.length; j < l; j++) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (not[j] === elem) {
                                        "dk.brics.tajs.directives.unreachable";
                                        return false;
                                    }
                                }
                                return true;
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                Sizzle.error(name);
                            }
                        }
                    }
                },
                CHILD: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    var first, last, doneName, parent, cache, count, diff, type = match[1], node = elem;
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

                      case "nth":
                        "dk.brics.tajs.directives.unreachable";
                        first = match[2];
                        last = match[3];
                        if (first === 1 && last === 0) {
                            "dk.brics.tajs.directives.unreachable";
                            return true;
                        }
                        doneName = match[0];
                        parent = elem.parentNode;
                        if (parent && (parent[expando] !== doneName || !elem.nodeIndex)) {
                            "dk.brics.tajs.directives.unreachable";
                            count = 0;
                            for (node = parent.firstChild; node; node = node.nextSibling) {
                                "dk.brics.tajs.directives.unreachable";
                                if (node.nodeType === 1) {
                                    "dk.brics.tajs.directives.unreachable";
                                    node.nodeIndex = ++count;
                                }
                            }
                            parent[expando] = doneName;
                        }
                        diff = elem.nodeIndex - last;
                        if (first === 0) {
                            "dk.brics.tajs.directives.unreachable";
                            return diff === 0;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            return diff % first === 0 && diff / first >= 0;
                        }
                    }
                },
                ID: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeType === 1 && elem.getAttribute("id") === match;
                },
                TAG: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return match === "*" && elem.nodeType === 1 || !!elem.nodeName && elem.nodeName.toLowerCase() === match;
                },
                CLASS: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return (" " + (elem.className || elem.getAttribute("class")) + " ").indexOf(match) > -1;
                },
                ATTR: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = match[1], result = Sizzle.attr ? Sizzle.attr(elem, name) : Expr.attrHandle[name] ? Expr.attrHandle[name](elem) : elem[name] != null ? elem[name] : elem.getAttribute(name), value = result + "", type = match[2], check = match[4];
                    return result == null ? type === "!=" : !type && Sizzle.attr ? result != null : type === "=" ? value === check : type === "*=" ? value.indexOf(check) >= 0 : type === "~=" ? (" " + value + " ").indexOf(check) >= 0 : !check ? value && result !== false : type === "!=" ? value !== check : type === "^=" ? value.indexOf(check) === 0 : type === "$=" ? value.substr(value.length - check.length) === check : type === "|=" ? value === check || value.substr(0, check.length + 1) === check + "-" : false;
                },
                POS: function(elem, match, i, array) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = match[2], filter = Expr.setFilters[name];
                    if (filter) {
                        "dk.brics.tajs.directives.unreachable";
                        return filter(elem, i, match, array);
                    }
                }
            }
        };
        var origPOS = Expr.match.POS, fescape = function(all, num) {
            return "\\" + (num - 0 + 1);
        };
        for (var type in Expr.match) {
            Expr.match[type] = new RegExp(Expr.match[type].source + /(?![^\[]*\])(?![^\(]*\))/.source);
            Expr.leftMatch[type] = new RegExp(/(^(?:.|\r|\n)*?)/.source + Expr.match[type].source.replace(/\\(\d+)/g, fescape));
        }
        var makeArray = function(array, results) {
            "dk.brics.tajs.directives.unreachable";
            array = Array.prototype.slice.call(array, 0);
            if (results) {
                "dk.brics.tajs.directives.unreachable";
                results.push.apply(results, array);
                return results;
            }
            return array;
        };
        // Perform a simple check to determine if the browser is capable of
        // converting a NodeList to an array using builtin methods.
        // Also verifies that the returned array holds DOM nodes
        // (which is not the case in the Blackberry browser)
        try {
            Array.prototype.slice.call(document.documentElement.childNodes, 0)[0].nodeType;
        } catch (e) {
            "dk.brics.tajs.directives.unreachable";
            makeArray = function(array, results) {
                "dk.brics.tajs.directives.unreachable";
                var i = 0, ret = results || [];
                if (toString.call(array) === "[object Array]") {
                    "dk.brics.tajs.directives.unreachable";
                    Array.prototype.push.apply(ret, array);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof array.length === "number") {
                        "dk.brics.tajs.directives.unreachable";
                        for (var l = array.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            ret.push(array[i]);
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (;array[i]; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            ret.push(array[i]);
                        }
                    }
                }
                return ret;
            };
        }
        var sortOrder, siblingCheck;
        if (document.documentElement.compareDocumentPosition) {
            sortOrder = function(a, b) {
                "dk.brics.tajs.directives.unreachable";
                if (a === b) {
                    "dk.brics.tajs.directives.unreachable";
                    hasDuplicate = true;
                    return 0;
                }
                if (!a.compareDocumentPosition || !b.compareDocumentPosition) {
                    "dk.brics.tajs.directives.unreachable";
                    return a.compareDocumentPosition ? -1 : 1;
                }
                return a.compareDocumentPosition(b) & 4 ? -1 : 1;
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
        // Check to see if the browser returns elements by name when
        // querying by getElementById (and provide a workaround)
        (function() {
            // We're going to inject a fake input element with a specified name
            var form = document.createElement("div"), id = "script" + "TAJS_UUID", root = document.documentElement;
            form.innerHTML = "<a name='" + id + "'/>";
            // Inject it into the root element, check its status, and remove it quickly
            root.insertBefore(form, root.firstChild);
            // The workaround has to do additional checks after a getElementById
            // Which slows things down for other browsers (hence the branching)
            if (document.getElementById(id)) {
                "dk.brics.tajs.directives.unreachable";
                Expr.find.ID = function(match, context, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof context.getElementById !== "undefined" && !isXML) {
                        "dk.brics.tajs.directives.unreachable";
                        var m = context.getElementById(match[1]);
                        return m ? m.id === match[1] || typeof m.getAttributeNode !== "undefined" && m.getAttributeNode("id").nodeValue === match[1] ? [ m ] : undefined : [];
                    }
                };
                Expr.filter.ID = function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    var node = typeof elem.getAttributeNode !== "undefined" && elem.getAttributeNode("id");
                    return elem.nodeType === 1 && node && node.nodeValue === match;
                };
            }
            root.removeChild(form);
            // release memory in IE
            root = form = null;
        })();
        (function() {
            // Check to see if the browser returns only elements
            // when doing getElementsByTagName("*")
            // Create a fake element
            var div = document.createElement("div");
            div.appendChild(document.createComment(""));
            // Make sure no comments are found
            if (div.getElementsByTagName("*").length > 0) {
                "dk.brics.tajs.directives.unreachable";
                Expr.find.TAG = function(match, context) {
                    "dk.brics.tajs.directives.unreachable";
                    var results = context.getElementsByTagName(match[1]);
                    // Filter out possible comments
                    if (match[1] === "*") {
                        "dk.brics.tajs.directives.unreachable";
                        var tmp = [];
                        for (var i = 0; results[i]; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (results[i].nodeType === 1) {
                                "dk.brics.tajs.directives.unreachable";
                                tmp.push(results[i]);
                            }
                        }
                        results = tmp;
                    }
                    return results;
                };
            }
            // Check to see if an attribute returns normalized href attributes
            div.innerHTML = "<a href='#'></a>";
            if (div.firstChild && typeof div.firstChild.getAttribute !== "undefined" && div.firstChild.getAttribute("href") !== "#") {
                "dk.brics.tajs.directives.unreachable";
                Expr.attrHandle.href = function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.getAttribute("href", 2);
                };
            }
            // release memory in IE
            div = null;
        })();
        if (document.querySelectorAll) {
            (function() {
                var oldSizzle = Sizzle, div = document.createElement("div"), id = "__sizzle__";
                div.innerHTML = "<p class='TEST'></p>";
                // Safari can't handle uppercase or unicode characters when
                // in quirks mode.
                if (div.querySelectorAll && div.querySelectorAll(".TEST").length === 0) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                Sizzle = function(query, context, extra, seed) {
                    "dk.brics.tajs.directives.unreachable";
                    context = context || document;
                    // Only use querySelectorAll on non-XML documents
                    // (ID selectors don't work in non-HTML documents)
                    if (!seed && !Sizzle.isXML(context)) {
                        "dk.brics.tajs.directives.unreachable";
                        // See if we find a selector to speed up
                        var match = /^(\w+$)|^\.([\w\-]+$)|^#([\w\-]+$)/.exec(query);
                        if (match && (context.nodeType === 1 || context.nodeType === 9)) {
                            "dk.brics.tajs.directives.unreachable";
                            // Speed-up: Sizzle("TAG")
                            if (match[1]) {
                                "dk.brics.tajs.directives.unreachable";
                                return makeArray(context.getElementsByTagName(query), extra);
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (match[2] && Expr.find.CLASS && context.getElementsByClassName) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return makeArray(context.getElementsByClassName(match[2]), extra);
                                }
                            }
                        }
                        if (context.nodeType === 9) {
                            "dk.brics.tajs.directives.unreachable";
                            // Speed-up: Sizzle("body")
                            // The body element only exists once, optimize finding it
                            if (query === "body" && context.body) {
                                "dk.brics.tajs.directives.unreachable";
                                return makeArray([ context.body ], extra);
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (match && match[3]) {
                                    "dk.brics.tajs.directives.unreachable";
                                    var elem = context.getElementById(match[3]);
                                    // Check parentNode to catch when Blackberry 4.6 returns
                                    // nodes that are no longer in the document #6963
                                    if (elem && elem.parentNode) {
                                        "dk.brics.tajs.directives.unreachable";
                                        // Handle the case where IE and Opera return items
                                        // by name instead of ID
                                        if (elem.id === match[3]) {
                                            "dk.brics.tajs.directives.unreachable";
                                            return makeArray([ elem ], extra);
                                        }
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        return makeArray([], extra);
                                    }
                                }
                            }
                            try {
                                "dk.brics.tajs.directives.unreachable";
                                return makeArray(context.querySelectorAll(query), extra);
                            } catch (qsaError) {}
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (context.nodeType === 1 && context.nodeName.toLowerCase() !== "object") {
                                "dk.brics.tajs.directives.unreachable";
                                var oldContext = context, old = context.getAttribute("id"), nid = old || id, hasParent = context.parentNode, relativeHierarchySelector = /^\s*[+~]/.test(query);
                                if (!old) {
                                    "dk.brics.tajs.directives.unreachable";
                                    context.setAttribute("id", nid);
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    nid = nid.replace(/'/g, "\\$&");
                                }
                                if (relativeHierarchySelector && hasParent) {
                                    "dk.brics.tajs.directives.unreachable";
                                    context = context.parentNode;
                                }
                                try {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (!relativeHierarchySelector || hasParent) {
                                        "dk.brics.tajs.directives.unreachable";
                                        return makeArray(context.querySelectorAll("[id='" + nid + "'] " + query), extra);
                                    }
                                } catch (pseudoError) {} finally {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (!old) {
                                        "dk.brics.tajs.directives.unreachable";
                                        oldContext.removeAttribute("id");
                                    }
                                }
                            }
                        }
                    }
                    return oldSizzle(query, context, extra, seed);
                };
                for (var prop in oldSizzle) {
                    Sizzle[prop] = oldSizzle[prop];
                }
                // release memory in IE
                div = null;
            })();
        }
        (function() {
            var html = document.documentElement, matches = html.matchesSelector || html.mozMatchesSelector || html.webkitMatchesSelector || html.msMatchesSelector;
            if (matches) {
                // Check to see if it's possible to do matchesSelector
                // on a disconnected node (IE 9 fails this)
                var disconnectedMatch = !matches.call(document.createElement("div"), "div"), pseudoWorks = false;
                try {
                    // This should fail with an exception
                    // Gecko does not error, returns false instead
                    matches.call(document.documentElement, "[test!='']:sizzle");
                } catch (pseudoError) {
                    pseudoWorks = true;
                }
                Sizzle.matchesSelector = function(node, expr) {
                    "dk.brics.tajs.directives.unreachable";
                    // Make sure that attribute selectors are quoted
                    expr = expr.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");
                    if (!Sizzle.isXML(node)) {
                        "dk.brics.tajs.directives.unreachable";
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            if (pseudoWorks || !Expr.match.PSEUDO.test(expr) && !/!=/.test(expr)) {
                                "dk.brics.tajs.directives.unreachable";
                                var ret = matches.call(node, expr);
                                // IE 9's matchesSelector returns false on disconnected nodes
                                if (ret || !disconnectedMatch || // As well, disconnected nodes are said to be in a document
                                // fragment in IE 9, so check for that
                                node.document && node.document.nodeType !== 11) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return ret;
                                }
                            }
                        } catch (e) {}
                    }
                    return Sizzle(expr, null, null, [ node ]).length > 0;
                };
            }
        })();
        (function() {
            var div = document.createElement("div");
            div.innerHTML = "<div class='test e'></div><div class='test'></div>";
            // Opera can't find a second classname (in 9.6)
            // Also, make sure that getElementsByClassName actually exists
            if (!div.getElementsByClassName || div.getElementsByClassName("e").length === 0) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Safari caches class attributes, doesn't catch changes (in 3.2)
            div.lastChild.className = "e";
            if (div.getElementsByClassName("e").length === 1) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            Expr.order.splice(1, 0, "CLASS");
            Expr.find.CLASS = function(match, context, isXML) {
                "dk.brics.tajs.directives.unreachable";
                if (typeof context.getElementsByClassName !== "undefined" && !isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    return context.getElementsByClassName(match[1]);
                }
            };
            // release memory in IE
            div = null;
        })();
        function dirNodeCheck(dir, cur, doneName, checkSet, nodeCheck, isXML) {
            "dk.brics.tajs.directives.unreachable";
            for (var i = 0, l = checkSet.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var elem = checkSet[i];
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var match = false;
                    elem = elem[dir];
                    while (elem) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem[expando] === doneName) {
                            "dk.brics.tajs.directives.unreachable";
                            match = checkSet[elem.sizset];
                            break;
                        }
                        if (elem.nodeType === 1 && !isXML) {
                            "dk.brics.tajs.directives.unreachable";
                            elem[expando] = doneName;
                            elem.sizset = i;
                        }
                        if (elem.nodeName.toLowerCase() === cur) {
                            "dk.brics.tajs.directives.unreachable";
                            match = elem;
                            break;
                        }
                        elem = elem[dir];
                    }
                    checkSet[i] = match;
                }
            }
        }
        function dirCheck(dir, cur, doneName, checkSet, nodeCheck, isXML) {
            "dk.brics.tajs.directives.unreachable";
            for (var i = 0, l = checkSet.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var elem = checkSet[i];
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    var match = false;
                    elem = elem[dir];
                    while (elem) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem[expando] === doneName) {
                            "dk.brics.tajs.directives.unreachable";
                            match = checkSet[elem.sizset];
                            break;
                        }
                        if (elem.nodeType === 1) {
                            "dk.brics.tajs.directives.unreachable";
                            if (!isXML) {
                                "dk.brics.tajs.directives.unreachable";
                                elem[expando] = doneName;
                                elem.sizset = i;
                            }
                            if (typeof cur !== "string") {
                                "dk.brics.tajs.directives.unreachable";
                                if (elem === cur) {
                                    "dk.brics.tajs.directives.unreachable";
                                    match = true;
                                    break;
                                }
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (Sizzle.filter(cur, [ elem ]).length > 0) {
                                    "dk.brics.tajs.directives.unreachable";
                                    match = elem;
                                    break;
                                }
                            }
                        }
                        elem = elem[dir];
                    }
                    checkSet[i] = match;
                }
            }
        }
        if (document.documentElement.contains) {
            Sizzle.contains = function(a, b) {
                "dk.brics.tajs.directives.unreachable";
                return a !== b && (a.contains ? a.contains(b) : true);
            };
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (document.documentElement.compareDocumentPosition) {
                "dk.brics.tajs.directives.unreachable";
                Sizzle.contains = function(a, b) {
                    "dk.brics.tajs.directives.unreachable";
                    return !!(a.compareDocumentPosition(b) & 16);
                };
            } else {
                "dk.brics.tajs.directives.unreachable";
                Sizzle.contains = function() {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                };
            }
        }
        Sizzle.isXML = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            // documentElement is verified for cases where it doesn't yet exist
            // (such as loading iframes in IE - #4833) 
            var documentElement = (elem ? elem.ownerDocument || elem : 0).documentElement;
            return documentElement ? documentElement.nodeName !== "HTML" : false;
        };
        var posProcess = function(selector, context, seed) {
            "dk.brics.tajs.directives.unreachable";
            var match, tmpSet = [], later = "", root = context.nodeType ? [ context ] : context;
            // Position selectors must be done after the filter
            // And so must :not(positional) so we move all PSEUDOs to the end
            while (match = Expr.match.PSEUDO.exec(selector)) {
                "dk.brics.tajs.directives.unreachable";
                later += match[0];
                selector = selector.replace(Expr.match.PSEUDO, "");
            }
            selector = Expr.relative[selector] ? selector + "*" : selector;
            for (var i = 0, l = root.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                Sizzle(selector, root[i], tmpSet, seed);
            }
            return Sizzle.filter(later, tmpSet);
        };
        // EXPOSE
        // Override sizzle attribute retrieval
        Sizzle.attr = jQuery.attr;
        Sizzle.selectors.attrMap = {};
        jQuery.find = Sizzle;
        jQuery.expr = Sizzle.selectors;
        jQuery.expr[":"] = jQuery.expr.filters;
        jQuery.unique = Sizzle.uniqueSort;
        jQuery.text = Sizzle.getText;
        jQuery.isXMLDoc = Sizzle.isXML;
        jQuery.contains = Sizzle.contains;
    })();
    var runtil = /Until$/, rparentsprev = /^(?:parents|prevUntil|prevAll)/, // Note: This RegExp should be improved, or likely pulled from Sizzle
    rmultiselector = /,/, isSimple = /^.[^:#\[\.,]*$/, slice = Array.prototype.slice, POS = jQuery.expr.match.POS, // methods guaranteed to produce a unique set when starting from a unique set
    guaranteedUnique = {
        children: true,
        contents: true,
        next: true,
        prev: true
    };
    jQuery.fn.extend({
        find: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            var self = this, i, l;
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
            var ret = this.pushStack("", "find", selector), length, n, r;
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
            var targets = jQuery(target);
            return this.filter(function() {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, l = targets.length; i < l; i++) {
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
            return !!selector && (typeof selector === "string" ? // If this is a positional selector, check membership in the returned set
            // so $("p:first").is("p:last") won't return true for a doc with two "p".
            POS.test(selector) ? jQuery(selector, this.context).index(this[0]) >= 0 : jQuery.filter(selector, this).length > 0 : this.filter(selector).length > 0);
        },
        closest: function(selectors, context) {
            "dk.brics.tajs.directives.unreachable";
            var ret = [], i, l, cur = this[0];
            // Array (deprecated as of jQuery 1.7)
            if (jQuery.isArray(selectors)) {
                "dk.brics.tajs.directives.unreachable";
                var level = 1;
                while (cur && cur.ownerDocument && cur !== context) {
                    "dk.brics.tajs.directives.unreachable";
                    for (i = 0; i < selectors.length; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (jQuery(cur).is(selectors[i])) {
                            "dk.brics.tajs.directives.unreachable";
                            ret.push({
                                selector: selectors[i],
                                elem: cur,
                                level: level
                            });
                        }
                    }
                    cur = cur.parentNode;
                    level++;
                }
                return ret;
            }
            // String
            var pos = POS.test(selectors) || typeof selectors !== "string" ? jQuery(selectors, context || this.context) : 0;
            for (i = 0, l = this.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                cur = this[i];
                while (cur) {
                    "dk.brics.tajs.directives.unreachable";
                    if (pos ? pos.index(cur) > -1 : jQuery.find.matchesSelector(cur, selectors)) {
                        "dk.brics.tajs.directives.unreachable";
                        ret.push(cur);
                        break;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        cur = cur.parentNode;
                        if (!cur || !cur.ownerDocument || cur === context || cur.nodeType === 11) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
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
        andSelf: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.add(this.prevObject);
        }
    });
    // A painfully simple check to see if an element is disconnected
    // from a document (should be improved, where feasible).
    function isDisconnected(node) {
        "dk.brics.tajs.directives.unreachable";
        return !node || !node.parentNode || node.parentNode.nodeType === 11;
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
            return jQuery.nth(elem, 2, "nextSibling");
        },
        prev: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.nth(elem, 2, "previousSibling");
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
            return jQuery.sibling(elem.parentNode.firstChild, elem);
        },
        children: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.sibling(elem.firstChild);
        },
        contents: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.nodeName(elem, "iframe") ? elem.contentDocument || elem.contentWindow.document : jQuery.makeArray(elem.childNodes);
        }
    }, function(name, fn) {
        jQuery.fn[name] = function(until, selector) {
            "dk.brics.tajs.directives.unreachable";
            var ret = jQuery.map(this, fn, until), // The variable 'args' was introduced in
            // https://github.com/jquery/jquery/commit/52a0238
            // to work around a bug in Chrome 10 (Dev) and should be removed when the bug is fixed.
            // http://code.google.com/p/v8/issues/detail?id=1050
            args = slice.call(arguments);
            if (!runtil.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                selector = until;
            }
            if (selector && typeof selector === "string") {
                "dk.brics.tajs.directives.unreachable";
                ret = jQuery.filter(selector, ret);
            }
            ret = this.length > 1 && !guaranteedUnique[name] ? jQuery.unique(ret) : ret;
            if ((this.length > 1 || rmultiselector.test(selector)) && rparentsprev.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                ret = ret.reverse();
            }
            return this.pushStack(ret, name, args.join(","));
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
        nth: function(cur, result, dir, elem) {
            "dk.brics.tajs.directives.unreachable";
            result = result || 1;
            var num = 0;
            for (;cur; cur = cur[dir]) {
                "dk.brics.tajs.directives.unreachable";
                if (cur.nodeType === 1 && ++num === result) {
                    "dk.brics.tajs.directives.unreachable";
                    break;
                }
            }
            return cur;
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
        var list = nodeNames.split(" "), safeFrag = document.createDocumentFragment();
        if (safeFrag.createElement) {
            "dk.brics.tajs.directives.unreachable";
            while (list.length) {
                "dk.brics.tajs.directives.unreachable";
                safeFrag.createElement(list.pop());
            }
        }
        return safeFrag;
    }
    var nodeNames = "abbr article aside audio canvas datalist details figcaption figure footer " + "header hgroup mark meter nav output progress section summary time video", rinlinejQuery = / jQuery\d+="(?:\d+|null)"/g, rleadingWhitespace = /^\s+/, rxhtmlTag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi, rtagName = /<([\w:]+)/, rtbody = /<tbody/i, rhtml = /<|&#?\w+;/, rnoInnerhtml = /<(?:script|style)/i, rnocache = /<(?:script|object|embed|option|style)/i, rnoshimcache = new RegExp("<(?:" + nodeNames.replace(" ", "|") + ")", "i"), // checked="checked" or checked
    rchecked = /checked\s*(?:[^=]|=\s*.checked.)/i, rscriptType = /\/(java|ecma)script/i, rcleanScript = /^\s*<!(?:\[CDATA\[|\-\-)/, wrapMap = {
        option: [ 1, "<select multiple='multiple'>", "</select>" ],
        legend: [ 1, "<fieldset>", "</fieldset>" ],
        thead: [ 1, "<table>", "</table>" ],
        tr: [ 2, "<table><tbody>", "</tbody></table>" ],
        td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],
        col: [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ],
        area: [ 1, "<map>", "</map>" ],
        _default: [ 0, "", "" ]
    }, safeFragment = createSafeFragment(document);
    wrapMap.optgroup = wrapMap.option;
    wrapMap.tbody = wrapMap.tfoot = wrapMap.colgroup = wrapMap.caption = wrapMap.thead;
    wrapMap.th = wrapMap.td;
    // IE can't serialize <link> and <script> tags normally
    if (!jQuery.support.htmlSerialize) {
        "dk.brics.tajs.directives.unreachable";
        wrapMap._default = [ 1, "div<div>", "</div>" ];
    }
    jQuery.fn.extend({
        text: function(text) {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(text)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    var self = jQuery(this);
                    self.text(text.call(this, i, self.text()));
                });
            }
            if (typeof text !== "object" && text !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                return this.empty().append((this[0] && this[0].ownerDocument || document).createTextNode(text));
            }
            return jQuery.text(this);
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
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery(this).wrapAll(html);
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
                if (this.nodeType === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    this.appendChild(elem);
                }
            });
        },
        prepend: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, true, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeType === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    this.insertBefore(elem, this.firstChild);
                }
            });
        },
        before: function() {
            "dk.brics.tajs.directives.unreachable";
            if (this[0] && this[0].parentNode) {
                "dk.brics.tajs.directives.unreachable";
                return this.domManip(arguments, false, function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    this.parentNode.insertBefore(elem, this);
                });
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (arguments.length) {
                    "dk.brics.tajs.directives.unreachable";
                    var set = jQuery(arguments[0]);
                    set.push.apply(set, this.toArray());
                    return this.pushStack(set, "before", arguments);
                }
            }
        },
        after: function() {
            "dk.brics.tajs.directives.unreachable";
            if (this[0] && this[0].parentNode) {
                "dk.brics.tajs.directives.unreachable";
                return this.domManip(arguments, false, function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    this.parentNode.insertBefore(elem, this.nextSibling);
                });
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (arguments.length) {
                    "dk.brics.tajs.directives.unreachable";
                    var set = this.pushStack(this, "after", arguments);
                    set.push.apply(set, jQuery(arguments[0]).toArray());
                    return set;
                }
            }
        },
        // keepData is for internal use only--do not document
        remove: function(selector, keepData) {
            "dk.brics.tajs.directives.unreachable";
            for (var i = 0, elem; (elem = this[i]) != null; i++) {
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
            for (var i = 0, elem; (elem = this[i]) != null; i++) {
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
            if (value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                return this[0] && this[0].nodeType === 1 ? this[0].innerHTML.replace(rinlinejQuery, "") : null;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (typeof value === "string" && !rnoInnerhtml.test(value) && (jQuery.support.leadingWhitespace || !rleadingWhitespace.test(value)) && !wrapMap[(rtagName.exec(value) || [ "", "" ])[1].toLowerCase()]) {
                    "dk.brics.tajs.directives.unreachable";
                    value = value.replace(rxhtmlTag, "<$1></$2>");
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0, l = this.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            // Remove element nodes and prevent memory leaks
                            if (this[i].nodeType === 1) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.cleanData(this[i].getElementsByTagName("*"));
                                this[i].innerHTML = value;
                            }
                        }
                    } catch (e) {
                        "dk.brics.tajs.directives.unreachable";
                        this.empty().append(value);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.isFunction(value)) {
                        "dk.brics.tajs.directives.unreachable";
                        this.each(function(i) {
                            "dk.brics.tajs.directives.unreachable";
                            var self = jQuery(this);
                            self.html(value.call(this, i, self.html()));
                        });
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        this.empty().append(value);
                    }
                }
            }
            return this;
        },
        replaceWith: function(value) {
            "dk.brics.tajs.directives.unreachable";
            if (this[0] && this[0].parentNode) {
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
            } else {
                "dk.brics.tajs.directives.unreachable";
                return this.length ? this.pushStack(jQuery(jQuery.isFunction(value) ? value() : value), "replaceWith", value) : this;
            }
        },
        detach: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return this.remove(selector, true);
        },
        domManip: function(args, table, callback) {
            "dk.brics.tajs.directives.unreachable";
            var results, first, fragment, parent, value = args[0], scripts = [];
            // We can't cloneNode fragments that contain checked, in WebKit
            if (!jQuery.support.checkClone && arguments.length === 3 && typeof value === "string" && rchecked.test(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).domManip(args, table, callback, true);
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
                parent = value && value.parentNode;
                // If we're in a fragment, just use that instead of building a new one
                if (jQuery.support.parentNode && parent && parent.nodeType === 11 && parent.childNodes.length === this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    results = {
                        fragment: parent
                    };
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    results = jQuery.buildFragment(args, this, scripts);
                }
                fragment = results.fragment;
                if (fragment.childNodes.length === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    first = fragment = fragment.firstChild;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    first = fragment.firstChild;
                }
                if (first) {
                    "dk.brics.tajs.directives.unreachable";
                    table = table && jQuery.nodeName(first, "tr");
                    for (var i = 0, l = this.length, lastIndex = l - 1; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        callback.call(table ? root(this[i], first) : this[i], // Make sure that we do not leak memory by inadvertently discarding
                        // the original fragment (which might have attached data) instead of
                        // using it; in addition, use the original fragment object for the last
                        // item instead of first because it can end up being emptied incorrectly
                        // in certain situations (Bug #8070).
                        // Fragments from the fragment cache must always be cloned and never used
                        // in place.
                        results.cacheable || l > 1 && i < lastIndex ? jQuery.clone(fragment, true, true) : fragment);
                    }
                }
                if (scripts.length) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.each(scripts, evalScript);
                }
            }
            return this;
        }
    });
    function root(elem, cur) {
        "dk.brics.tajs.directives.unreachable";
        return jQuery.nodeName(elem, "table") ? elem.getElementsByTagName("tbody")[0] || elem.appendChild(elem.ownerDocument.createElement("tbody")) : elem;
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
                    jQuery.event.add(dest, type + (events[type][i].namespace ? "." : "") + events[type][i].namespace, events[type][i], events[type][i].data);
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
        // IE6-8 fail to clone children inside object elements that use
        // the proprietary classid attribute value (rather than the type
        // attribute) to identify the type of content to display
        if (nodeName === "object") {
            "dk.brics.tajs.directives.unreachable";
            dest.outerHTML = src.outerHTML;
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (nodeName === "input" && (src.type === "checkbox" || src.type === "radio")) {
                "dk.brics.tajs.directives.unreachable";
                // IE6-8 fails to persist the checked state of a cloned checkbox
                // or radio button. Worse, IE6-7 fail to give the cloned element
                // a checked appearance if the defaultChecked value isn't also set
                if (src.checked) {
                    "dk.brics.tajs.directives.unreachable";
                    dest.defaultChecked = dest.checked = src.checked;
                }
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
                    }
                }
            }
        }
        // Event data gets referenced instead of copied if the expando
        // gets copied too
        dest.removeAttribute(jQuery.expando);
    }
    jQuery.buildFragment = function(args, nodes, scripts) {
        "dk.brics.tajs.directives.unreachable";
        var fragment, cacheable, cacheresults, doc, first = args[0];
        // nodes may contain either an explicit document object,
        // a jQuery collection or context object.
        // If nodes[0] contains a valid object to assign to doc
        if (nodes && nodes[0]) {
            "dk.brics.tajs.directives.unreachable";
            doc = nodes[0].ownerDocument || nodes[0];
        }
        // Ensure that an attr object doesn't incorrectly stand in as a document object
        // Chrome and Firefox seem to allow this to occur and will throw exception
        // Fixes #8950
        if (!doc.createDocumentFragment) {
            "dk.brics.tajs.directives.unreachable";
            doc = document;
        }
        // Only cache "small" (1/2 KB) HTML strings that are associated with the main document
        // Cloning options loses the selected state, so don't cache them
        // IE 6 doesn't like it when you put <object> or <embed> elements in a fragment
        // Also, WebKit does not clone 'checked' attributes on cloneNode, so don't cache
        // Lastly, IE6,7,8 will not correctly reuse cached fragments that were created from unknown elems #10501
        if (args.length === 1 && typeof first === "string" && first.length < 512 && doc === document && first.charAt(0) === "<" && !rnocache.test(first) && (jQuery.support.checkClone || !rchecked.test(first)) && !jQuery.support.unknownElems && rnoshimcache.test(first)) {
            "dk.brics.tajs.directives.unreachable";
            cacheable = true;
            cacheresults = jQuery.fragments[first];
            if (cacheresults && cacheresults !== 1) {
                "dk.brics.tajs.directives.unreachable";
                fragment = cacheresults;
            }
        }
        if (!fragment) {
            "dk.brics.tajs.directives.unreachable";
            fragment = doc.createDocumentFragment();
            jQuery.clean(args, doc, fragment, scripts);
        }
        if (cacheable) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.fragments[first] = cacheresults ? fragment : 1;
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
            var ret = [], insert = jQuery(selector), parent = this.length === 1 && this[0].parentNode;
            if (parent && parent.nodeType === 11 && parent.childNodes.length === 1 && insert.length === 1) {
                "dk.brics.tajs.directives.unreachable";
                insert[original](this[0]);
                return this;
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, l = insert.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var elems = (i > 0 ? this.clone(true) : this).get();
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
        if (elem.type === "checkbox" || elem.type === "radio") {
            "dk.brics.tajs.directives.unreachable";
            elem.defaultChecked = elem.checked;
        }
    }
    // Finds all inputs and passes them to fixDefaultChecked
    function findInputs(elem) {
        "dk.brics.tajs.directives.unreachable";
        var nodeName = (elem.nodeName || "").toLowerCase();
        if (nodeName === "input") {
            "dk.brics.tajs.directives.unreachable";
            fixDefaultChecked(elem);
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (nodeName !== "script" && typeof elem.getElementsByTagName !== "undefined") {
                "dk.brics.tajs.directives.unreachable";
                jQuery.grep(elem.getElementsByTagName("input"), fixDefaultChecked);
            }
        }
    }
    jQuery.extend({
        clone: function(elem, dataAndEvents, deepDataAndEvents) {
            "dk.brics.tajs.directives.unreachable";
            var clone = elem.cloneNode(true), srcElements, destElements, i;
            if ((!jQuery.support.noCloneEvent || !jQuery.support.noCloneChecked) && (elem.nodeType === 1 || elem.nodeType === 11) && !jQuery.isXMLDoc(elem)) {
                "dk.brics.tajs.directives.unreachable";
                // IE copies events bound via attachEvent when using cloneNode.
                // Calling detachEvent on the clone will also remove the events
                // from the original. In order to get around this, we use some
                // proprietary methods to clear the events. Thanks to MooTools
                // guys for this hotness.
                cloneFixAttributes(elem, clone);
                // Using Sizzle here is crazy slow, so we use getElementsByTagName
                // instead
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
            var checkScriptType;
            context = context || document;
            // !context.createElement fails in IE with an error but returns typeof 'object'
            if (typeof context.createElement === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                context = context.ownerDocument || context[0] && context[0].ownerDocument || document;
            }
            var ret = [], j;
            for (var i = 0, elem; (elem = elems[i]) != null; i++) {
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
                        // Fix "XHTML"-style tags in all browsers
                        elem = elem.replace(rxhtmlTag, "<$1></$2>");
                        // Trim whitespace, otherwise indexOf won't work as expected
                        var tag = (rtagName.exec(elem) || [ "", "" ])[1].toLowerCase(), wrap = wrapMap[tag] || wrapMap._default, depth = wrap[0], div = context.createElement("div");
                        // Append wrapper element to unknown element safe doc fragment
                        if (context === document) {
                            "dk.brics.tajs.directives.unreachable";
                            // Use the fragment we've already created for this document
                            safeFragment.appendChild(div);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // Use a fragment created with the owner document
                            createSafeFragment(context).appendChild(div);
                        }
                        // Go to html and back, then peel off extra wrappers
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
                            var hasBody = rtbody.test(elem), tbody = tag === "table" && !hasBody ? div.firstChild && div.firstChild.childNodes : // String was a bare <thead> or <tfoot>
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
                    }
                }
                // Resets defaultChecked for any radios and checkboxes
                // about to be appended to the DOM in IE 6/7 (#8060)
                var len;
                if (!jQuery.support.appendChecked) {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem[0] && typeof (len = elem.length) === "number") {
                        "dk.brics.tajs.directives.unreachable";
                        for (j = 0; j < len; j++) {
                            "dk.brics.tajs.directives.unreachable";
                            findInputs(elem[j]);
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        findInputs(elem);
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
            if (fragment) {
                "dk.brics.tajs.directives.unreachable";
                checkScriptType = function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return !elem.type || rscriptType.test(elem.type);
                };
                for (i = 0; ret[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (scripts && jQuery.nodeName(ret[i], "script") && (!ret[i].type || ret[i].type.toLowerCase() === "text/javascript")) {
                        "dk.brics.tajs.directives.unreachable";
                        scripts.push(ret[i].parentNode ? ret[i].parentNode.removeChild(ret[i]) : ret[i]);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (ret[i].nodeType === 1) {
                            "dk.brics.tajs.directives.unreachable";
                            var jsTags = jQuery.grep(ret[i].getElementsByTagName("script"), checkScriptType);
                            ret.splice.apply(ret, [ i + 1, 0 ].concat(jsTags));
                        }
                        fragment.appendChild(ret[i]);
                    }
                }
            }
            return ret;
        },
        cleanData: function(elems) {
            "dk.brics.tajs.directives.unreachable";
            var data, id, cache = jQuery.cache, special = jQuery.event.special, deleteExpando = jQuery.support.deleteExpando;
            for (var i = 0, elem; (elem = elems[i]) != null; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (elem.nodeName && jQuery.noData[elem.nodeName.toLowerCase()]) {
                    "dk.brics.tajs.directives.unreachable";
                    continue;
                }
                id = elem[jQuery.expando];
                if (id) {
                    "dk.brics.tajs.directives.unreachable";
                    data = cache[id];
                    if (data && data.events) {
                        "dk.brics.tajs.directives.unreachable";
                        for (var type in data.events) {
                            "dk.brics.tajs.directives.unreachable";
                            if (special[type]) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.event.remove(elem, type);
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.removeEvent(elem, type, data.handle);
                            }
                        }
                        // Null the DOM reference to avoid IE6/7/8 leak (#7054)
                        if (data.handle) {
                            "dk.brics.tajs.directives.unreachable";
                            data.handle.elem = null;
                        }
                    }
                    if (deleteExpando) {
                        "dk.brics.tajs.directives.unreachable";
                        delete elem[jQuery.expando];
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem.removeAttribute) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.removeAttribute(jQuery.expando);
                        }
                    }
                    delete cache[id];
                }
            }
        }
    });
    function evalScript(i, elem) {
        "dk.brics.tajs.directives.unreachable";
        if (elem.src) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.ajax({
                url: elem.src,
                async: false,
                dataType: "script"
            });
        } else {
            "dk.brics.tajs.directives.unreachable";
            jQuery.globalEval((elem.text || elem.textContent || elem.innerHTML || "").replace(rcleanScript, "/*$0*/"));
        }
        if (elem.parentNode) {
            "dk.brics.tajs.directives.unreachable";
            elem.parentNode.removeChild(elem);
        }
    }
    var ralpha = /alpha\([^)]*\)/i, ropacity = /opacity=([^)]*)/, // fixed for IE9, see #8346
    rupper = /([A-Z]|^ms)/g, rnumpx = /^-?\d+(?:px)?$/i, rnum = /^-?\d/, rrelNum = /^([\-+])=([\-+.\de]+)/, cssShow = {
        position: "absolute",
        visibility: "hidden",
        display: "block"
    }, cssWidth = [ "Left", "Right" ], cssHeight = [ "Top", "Bottom" ], curCSS, getComputedStyle, currentStyle;
    jQuery.fn.css = function(name, value) {
        "dk.brics.tajs.directives.unreachable";
        // Setting 'undefined' is a no-op
        if (arguments.length === 2 && value === undefined) {
            "dk.brics.tajs.directives.unreachable";
            return this;
        }
        return jQuery.access(this, name, value, true, function(elem, name, value) {
            "dk.brics.tajs.directives.unreachable";
            return value !== undefined ? jQuery.style(elem, name, value) : jQuery.css(elem, name);
        });
    };
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
                        var ret = curCSS(elem, "opacity", "opacity");
                        return ret === "" ? "1" : ret;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.style.opacity;
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
            var ret, type, origName = jQuery.camelCase(name), style = elem.style, hooks = jQuery.cssHooks[origName];
            name = jQuery.cssProps[origName] || origName;
            // Check if we're setting a value
            if (value !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                type = typeof value;
                // convert relative number strings (+= or -=) to relative numbers. #7345
                if (type === "string" && (ret = rrelNum.exec(value))) {
                    "dk.brics.tajs.directives.unreachable";
                    value = +(ret[1] + 1) * +ret[2] + parseFloat(jQuery.css(elem, name));
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
                if (!hooks || !("set" in hooks) || (value = hooks.set(elem, value)) !== undefined) {
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
        css: function(elem, name, extra) {
            "dk.brics.tajs.directives.unreachable";
            var ret, hooks;
            // Make sure that we're working with the right name
            name = jQuery.camelCase(name);
            hooks = jQuery.cssHooks[name];
            name = jQuery.cssProps[name] || name;
            // cssFloat needs a special treatment
            if (name === "cssFloat") {
                "dk.brics.tajs.directives.unreachable";
                name = "float";
            }
            // If a hook was provided get the computed value from there
            if (hooks && "get" in hooks && (ret = hooks.get(elem, true, extra)) !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                return ret;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (curCSS) {
                    "dk.brics.tajs.directives.unreachable";
                    return curCSS(elem, name);
                }
            }
        },
        // A method for quickly swapping in/out CSS properties to get correct calculations
        swap: function(elem, options, callback) {
            "dk.brics.tajs.directives.unreachable";
            var old = {};
            // Remember the old values, and insert the new ones
            for (var name in options) {
                "dk.brics.tajs.directives.unreachable";
                old[name] = elem.style[name];
                elem.style[name] = options[name];
            }
            callback.call(elem);
            // Revert the old values
            for (name in options) {
                "dk.brics.tajs.directives.unreachable";
                elem.style[name] = old[name];
            }
        }
    });
    // DEPRECATED, Use jQuery.css() instead
    jQuery.curCSS = jQuery.css;
    jQuery.each([ "height", "width" ], function(i, name) {
        jQuery.cssHooks[name] = {
            get: function(elem, computed, extra) {
                "dk.brics.tajs.directives.unreachable";
                var val;
                if (computed) {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.offsetWidth !== 0) {
                        "dk.brics.tajs.directives.unreachable";
                        return getWH(elem, name, extra);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.swap(elem, cssShow, function() {
                            "dk.brics.tajs.directives.unreachable";
                            val = getWH(elem, name, extra);
                        });
                    }
                    return val;
                }
            },
            set: function(elem, value) {
                "dk.brics.tajs.directives.unreachable";
                if (rnumpx.test(value)) {
                    "dk.brics.tajs.directives.unreachable";
                    // ignore negative width and height values #1599
                    value = parseFloat(value);
                    if (value >= 0) {
                        "dk.brics.tajs.directives.unreachable";
                        return value + "px";
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return value;
                }
            }
        };
    });
    if (!jQuery.support.opacity) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.cssHooks.opacity = {
            get: function(elem, computed) {
                "dk.brics.tajs.directives.unreachable";
                // IE uses filters for opacity
                return ropacity.test((computed && elem.currentStyle ? elem.currentStyle.filter : elem.style.filter) || "") ? parseFloat(RegExp.$1) / 100 + "" : computed ? "1" : "";
            },
            set: function(elem, value) {
                "dk.brics.tajs.directives.unreachable";
                var style = elem.style, currentStyle = elem.currentStyle, opacity = jQuery.isNumeric(value) ? "alpha(opacity=" + value * 100 + ")" : "", filter = currentStyle && currentStyle.filter || style.filter || "";
                // IE has trouble with opacity if it does not have layout
                // Force it by setting the zoom level
                style.zoom = 1;
                // if setting opacity to 1, and no other filters exist - attempt to remove filter attribute #6652
                if (value >= 1 && jQuery.trim(filter.replace(ralpha, "")) === "") {
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
    jQuery(function() {
        // This hook cannot be added until DOM ready because the support test
        // for it is not run until after DOM ready
        if (!jQuery.support.reliableMarginRight) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.cssHooks.marginRight = {
                get: function(elem, computed) {
                    "dk.brics.tajs.directives.unreachable";
                    // WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
                    // Work around by temporarily setting element display to inline-block
                    var ret;
                    jQuery.swap(elem, {
                        display: "inline-block"
                    }, function() {
                        "dk.brics.tajs.directives.unreachable";
                        if (computed) {
                            "dk.brics.tajs.directives.unreachable";
                            ret = curCSS(elem, "margin-right", "marginRight");
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            ret = elem.style.marginRight;
                        }
                    });
                    return ret;
                }
            };
        }
    });
    if (document.defaultView && document.defaultView.getComputedStyle) {
        getComputedStyle = function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            var ret, defaultView, computedStyle;
            name = name.replace(rupper, "-$1").toLowerCase();
            if (!(defaultView = elem.ownerDocument.defaultView)) {
                "dk.brics.tajs.directives.unreachable";
                return undefined;
            }
            if (computedStyle = defaultView.getComputedStyle(elem, null)) {
                "dk.brics.tajs.directives.unreachable";
                ret = computedStyle.getPropertyValue(name);
                if (ret === "" && !jQuery.contains(elem.ownerDocument.documentElement, elem)) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = jQuery.style(elem, name);
                }
            }
            return ret;
        };
    }
    if (document.documentElement.currentStyle) {
        "dk.brics.tajs.directives.unreachable";
        currentStyle = function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            var left, rsLeft, uncomputed, ret = elem.currentStyle && elem.currentStyle[name], style = elem.style;
            // Avoid setting ret to empty string here
            // so we don't default to auto
            if (ret === null && style && (uncomputed = style[name])) {
                "dk.brics.tajs.directives.unreachable";
                ret = uncomputed;
            }
            // From the awesome hack by Dean Edwards
            // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
            // If we're not dealing with a regular pixel number
            // but a number that has a weird ending, we need to convert it to pixels
            if (!rnumpx.test(ret) && rnum.test(ret)) {
                "dk.brics.tajs.directives.unreachable";
                // Remember the original values
                left = style.left;
                rsLeft = elem.runtimeStyle && elem.runtimeStyle.left;
                // Put in the new values to get a computed value out
                if (rsLeft) {
                    "dk.brics.tajs.directives.unreachable";
                    elem.runtimeStyle.left = elem.currentStyle.left;
                }
                style.left = name === "fontSize" ? "1em" : ret || 0;
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
    curCSS = getComputedStyle || currentStyle;
    function getWH(elem, name, extra) {
        "dk.brics.tajs.directives.unreachable";
        // Start with offset property
        var val = name === "width" ? elem.offsetWidth : elem.offsetHeight, which = name === "width" ? cssWidth : cssHeight;
        if (val > 0) {
            "dk.brics.tajs.directives.unreachable";
            if (extra !== "border") {
                "dk.brics.tajs.directives.unreachable";
                jQuery.each(which, function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (!extra) {
                        "dk.brics.tajs.directives.unreachable";
                        val -= parseFloat(jQuery.css(elem, "padding" + this)) || 0;
                    }
                    if (extra === "margin") {
                        "dk.brics.tajs.directives.unreachable";
                        val += parseFloat(jQuery.css(elem, extra + this)) || 0;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        val -= parseFloat(jQuery.css(elem, "border" + this + "Width")) || 0;
                    }
                });
            }
            return val + "px";
        }
        // Fall back to computed then uncomputed css if necessary
        val = curCSS(elem, name, name);
        if (val < 0 || val == null) {
            "dk.brics.tajs.directives.unreachable";
            val = elem.style[name] || 0;
        }
        // Normalize "", auto, and prepare for extra
        val = parseFloat(val) || 0;
        // Add padding, border, margin
        if (extra) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.each(which, function() {
                "dk.brics.tajs.directives.unreachable";
                val += parseFloat(jQuery.css(elem, "padding" + this)) || 0;
                if (extra !== "padding") {
                    "dk.brics.tajs.directives.unreachable";
                    val += parseFloat(jQuery.css(elem, "border" + this + "Width")) || 0;
                }
                if (extra === "margin") {
                    "dk.brics.tajs.directives.unreachable";
                    val += parseFloat(jQuery.css(elem, extra + this)) || 0;
                }
            });
        }
        return val + "px";
    }
    if (jQuery.expr && jQuery.expr.filters) {
        jQuery.expr.filters.hidden = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            var width = elem.offsetWidth, height = elem.offsetHeight;
            return width === 0 && height === 0 || !jQuery.support.reliableHiddenOffsets && (elem.style && elem.style.display || jQuery.css(elem, "display")) === "none";
        };
        jQuery.expr.filters.visible = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return !jQuery.expr.filters.hidden(elem);
        };
    }
    var r20 = /%20/g, rbracket = /\[\]$/, rCRLF = /\r?\n/g, rhash = /#.*$/, rheaders = /^(.*?):[ \t]*([^\r\n]*)\r?$/gm, // IE leaves an \r character at EOL
    rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i, // #7653, #8125, #8152: local protocol detection
    rlocalProtocol = /^(?:about|app|app\-storage|.+\-extension|file|res|widget):$/, rnoContent = /^(?:GET|HEAD)$/, rprotocol = /^\/\//, rquery = /\?/, rscript = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, rselectTextarea = /^(?:select|textarea)/i, rspacesAjax = /\s+/, rts = /([?&])_=[^&]*/, rurl = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/, // Keep a copy of the old load method
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
    transports = {}, // Document location
    ajaxLocation, // Document location segments
    ajaxLocParts, // Avoid comment-prolog char sequence (#10098); must appease lint and evade compression
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
            if (jQuery.isFunction(func)) {
                var dataTypes = dataTypeExpression.toLowerCase().split(rspacesAjax), i = 0, length = dataTypes.length, dataType, list, placeBefore;
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
                    if(placeBefore){
                        list.unshift(func);
                    }else{
                        list.push(func);
                    }
                    
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
        var list = structure[dataType], i = 0, length = list ? list.length : 0, executeOnly = structure === prefilters, selection;
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
    jQuery.fn.extend({
        load: function(url, params, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof url !== "string" && _load) {
                "dk.brics.tajs.directives.unreachable";
                return _load.apply(this, arguments);
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (!this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    return this;
                }
            }
            var off = url.indexOf(" ");
            if (off >= 0) {
                "dk.brics.tajs.directives.unreachable";
                var selector = url.slice(off, url.length);
                url = url.slice(0, off);
            }
            // Default to a GET request
            var type = "GET";
            // If the second parameter was provided
            if (params) {
                "dk.brics.tajs.directives.unreachable";
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
                        params = jQuery.param(params, jQuery.ajaxSettings.traditional);
                        type = "POST";
                    }
                }
            }
            var self = this;
            // Request the remote document
            jQuery.ajax({
                url: url,
                type: type,
                dataType: "html",
                data: params,
                // Complete callback (responseText is used internally)
                complete: function(jqXHR, status, responseText) {
                    "dk.brics.tajs.directives.unreachable";
                    // Store the response as specified by the jqXHR object
                    responseText = jqXHR.responseText;
                    // If successful, inject the HTML into all the matched elements
                    if (jqXHR.isResolved()) {
                        "dk.brics.tajs.directives.unreachable";
                        // #4825: Get the actual response in case
                        // a dataFilter is present in ajaxSettings
                        jqXHR.done(function(r) {
                            "dk.brics.tajs.directives.unreachable";
                            responseText = r;
                        });
                        // See if a selector was specified
                        self.html(selector ? // Create a dummy div to hold the results
                        jQuery("<div>").append(responseText.replace(rscript, "")).find(selector) : // If not, just inject the full result
                        responseText);
                    }
                    if (callback) {
                        "dk.brics.tajs.directives.unreachable";
                        self.each(callback, [ responseText, status, jqXHR ]);
                    }
                }
            });
            return this;
        },
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
    // Attach a bunch of functions for handling common AJAX events
    jQuery.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "), function(i, o) {
        jQuery.fn[o] = function(f) {
            "dk.brics.tajs.directives.unreachable";
            return this.bind(o, f);
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
            contentType: "application/x-www-form-urlencoded",
            processData: true,
            async: true,
            /*
		timeout: 0,
		data: null,
		dataType: null,
		username: null,
		password: null,
		cache: null,
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
            var // Create the final options object
            s = jQuery.ajaxSetup({}, options), // Callbacks context
            callbackContext = s.context || s, // Context for global events
            // It's the callbackContext if one was provided in the options
            // and if it's a DOM node or a jQuery collection
            globalEventContext = callbackContext !== s && (callbackContext.nodeType || callbackContext instanceof jQuery) ? jQuery(callbackContext) : jQuery.event, // Deferreds
            deferred = jQuery.Deferred(), completeDeferred = jQuery.Callbacks("once memory"), // Status-dependent callbacks
            statusCode = s.statusCode || {}, // ifModified key
            ifModifiedKey, // Headers (they are sent all at once)
            requestHeaders = {}, requestHeadersNames = {}, // Response headers
            responseHeadersString, responseHeaders, // transport
            transport, // timeout handle
            timeoutTimer, // Cross-domain detection vars
            parts, // The jqXHR state
            state = 0, // To know if global events are to be dispatched
            fireGlobals, // Loop variable
            i, // Fake xhr
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
                    statusText = statusText || "abort";
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
                var isSuccess, success, error, statusText = nativeStatusText, response = responses ? ajaxHandleResponses(s, jqXHR, responses) : undefined, lastModified, etag;
                // If successful, handle type chaining
                if (status >= 200 && status < 300 || status === 304) {
                    "dk.brics.tajs.directives.unreachable";
                    // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
                    if (s.ifModified) {
                        "dk.brics.tajs.directives.unreachable";
                        if (lastModified = jqXHR.getResponseHeader("Last-Modified")) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.lastModified[ifModifiedKey] = lastModified;
                        }
                        if (etag = jqXHR.getResponseHeader("Etag")) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.etag[ifModifiedKey] = etag;
                        }
                    }
                    // If not modified
                    if (status === 304) {
                        "dk.brics.tajs.directives.unreachable";
                        statusText = "notmodified";
                        isSuccess = true;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            success = ajaxConvert(s, response);
                            statusText = "success";
                            isSuccess = true;
                        } catch (e) {
                            "dk.brics.tajs.directives.unreachable";
                            // We have a parsererror
                            statusText = "parsererror";
                            error = e;
                        }
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
                        jqXHR.then(tmp, tmp);
                    }
                }
                return this;
            };
            // Remove hash character (#7531: and string promotion)
            // Add protocol if not provided (#5866: IE7 issue with protocol-less urls)
            // We also use the url parameter if available
            s.url = ((url || s.url) + "").replace(rhash, "").replace(rprotocol, ajaxLocParts[1] + "//");
            // Extract dataTypes list
            s.dataTypes = jQuery.trim(s.dataType || "*").toLowerCase().split(rspacesAjax);
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
            // If request was aborted inside a prefiler, stop there
            if (state === 2) {
                "dk.brics.tajs.directives.unreachable";
                return false;
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
                // Abort if not done already
                jqXHR.abort();
                return false;
            }
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
                        jQuery.error(e);
                    }
                }
            }
            return jqXHR;
        },
        // Serialize an array of form elements or a set of
        // key/values into a query string
        param: function(a, traditional) {
            "dk.brics.tajs.directives.unreachable";
            var s = [], add = function(key, value) {
                "dk.brics.tajs.directives.unreachable";
                // If value is a function, invoke it and return its value
                value = jQuery.isFunction(value) ? value() : value;
                s[s.length] = encodeURIComponent(key) + "=" + encodeURIComponent(value);
            };
            // Set traditional to true for jQuery <= 1.3.2 behavior.
            if (traditional === undefined) {
                "dk.brics.tajs.directives.unreachable";
                traditional = jQuery.ajaxSettings.traditional;
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
                for (var prefix in a) {
                    "dk.brics.tajs.directives.unreachable";
                    buildParams(prefix, a[prefix], traditional, add);
                }
            }
            // Return the resulting serialization
            return s.join("&").replace(r20, "+");
        }
    });
    function buildParams(prefix, obj, traditional, add) {
        "dk.brics.tajs.directives.unreachable";
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
                    buildParams(prefix + "[" + (typeof v === "object" || jQuery.isArray(v) ? i : "") + "]", v, traditional, add);
                }
            });
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (!traditional && obj != null && typeof obj === "object") {
                "dk.brics.tajs.directives.unreachable";
                // Serialize object item.
                for (var name in obj) {
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
    // This is still on the jQuery object... for now
    // Want to move this to jQuery.ajax some day
    jQuery.extend({
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
        var contents = s.contents, dataTypes = s.dataTypes, responseFields = s.responseFields, ct, type, finalDataType, firstDataType;
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
        // Apply the dataFilter if provided
        if (s.dataFilter) {
            "dk.brics.tajs.directives.unreachable";
            response = s.dataFilter(response, s.dataType);
        }
        var dataTypes = s.dataTypes, converters = {}, i, key, length = dataTypes.length, tmp, // Current and previous dataTypes
        current = dataTypes[0], prev, // Conversion expression
        conversion, // Conversion function
        conv, // Conversion functions (transitive conversion)
        conv1, conv2;
        // For each dataType in the chain
        for (i = 1; i < length; i++) {
            "dk.brics.tajs.directives.unreachable";
            // Create converters map
            // with lowercased keys
            if (i === 1) {
                "dk.brics.tajs.directives.unreachable";
                for (key in s.converters) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof key === "string") {
                        "dk.brics.tajs.directives.unreachable";
                        converters[key.toLowerCase()] = s.converters[key];
                    }
                }
            }
            // Get the dataTypes
            prev = current;
            current = dataTypes[i];
            // If current is auto dataType, update it to prev
            if (current === "*") {
                "dk.brics.tajs.directives.unreachable";
                current = prev;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (prev !== "*" && prev !== current) {
                    "dk.brics.tajs.directives.unreachable";
                    // Get the converter
                    conversion = prev + " " + current;
                    conv = converters[conversion] || converters["* " + current];
                    // If there is no direct converter, search transitively
                    if (!conv) {
                        "dk.brics.tajs.directives.unreachable";
                        conv2 = undefined;
                        for (conv1 in converters) {
                            "dk.brics.tajs.directives.unreachable";
                            tmp = conv1.split(" ");
                            if (tmp[0] === prev || tmp[0] === "*") {
                                "dk.brics.tajs.directives.unreachable";
                                conv2 = converters[tmp[1] + " " + current];
                                if (conv2) {
                                    "dk.brics.tajs.directives.unreachable";
                                    conv1 = converters[conv1];
                                    if (conv1 === true) {
                                        "dk.brics.tajs.directives.unreachable";
                                        conv = conv2;
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (conv2 === true) {
                                            "dk.brics.tajs.directives.unreachable";
                                            conv = conv1;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    // If we found no converter, dispatch an error
                    if (!(conv || conv2)) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.error("No conversion from " + conversion.replace(" ", " to "));
                    }
                    // If found converter is not an equivalence
                    if (conv !== true) {
                        "dk.brics.tajs.directives.unreachable";
                        // Convert with 1 or 2 converters accordingly
                        response = conv ? conv(response) : conv2(conv1(response));
                    }
                }
            }
        }
        return response;
    }
    var jsc = jQuery.now(), jsre = /(\=)\?(&|$)|\?\?/i;
    // Default jsonp settings
    jQuery.ajaxSetup({
        jsonp: "callback",
        jsonpCallback: function() {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.expando + "_" + jsc++;
        }
    });
    // Detect, normalize options and install callbacks for jsonp requests
    jQuery.ajaxPrefilter("json jsonp", function(s, originalSettings, jqXHR) {
        "dk.brics.tajs.directives.unreachable";
        var inspectData = s.contentType === "application/x-www-form-urlencoded" && typeof s.data === "string";
        if (s.dataTypes[0] === "jsonp" || s.jsonp !== false && (jsre.test(s.url) || inspectData && jsre.test(s.data))) {
            "dk.brics.tajs.directives.unreachable";
            var responseContainer, jsonpCallback = s.jsonpCallback = jQuery.isFunction(s.jsonpCallback) ? s.jsonpCallback() : s.jsonpCallback, previous = window[jsonpCallback], url = s.url, data = s.data, replace = "$1" + jsonpCallback + "$2";
            if (s.jsonp !== false) {
                "dk.brics.tajs.directives.unreachable";
                url = url.replace(jsre, replace);
                if (s.url === url) {
                    "dk.brics.tajs.directives.unreachable";
                    if (inspectData) {
                        "dk.brics.tajs.directives.unreachable";
                        data = data.replace(jsre, replace);
                    }
                    if (s.data === data) {
                        "dk.brics.tajs.directives.unreachable";
                        // Add callback manually
                        url += (/\?/.test(url) ? "&" : "?") + s.jsonp + "=" + jsonpCallback;
                    }
                }
            }
            s.url = url;
            s.data = data;
            // Install callback
            window[jsonpCallback] = function(response) {
                "dk.brics.tajs.directives.unreachable";
                responseContainer = [ response ];
            };
            // Clean-up function
            jqXHR.always(function() {
                "dk.brics.tajs.directives.unreachable";
                // Set callback back to previous value
                window[jsonpCallback] = previous;
                // Call if it was a function and we have a response
                if (responseContainer && jQuery.isFunction(previous)) {
                    "dk.brics.tajs.directives.unreachable";
                    window[jsonpCallback](responseContainer[0]);
                }
            });
            // Use data converter to retrieve json after script execution
            s.converters["script json"] = function() {
                "dk.brics.tajs.directives.unreachable";
                if (!responseContainer) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.error(jsonpCallback + " was not called");
                }
                return responseContainer[0];
            };
            // force json dataType
            s.dataTypes[0] = "json";
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
    var // #5280: Internet Explorer will keep connections alive if we don't abort on unload
    xhrOnUnloadAbort = window.ActiveXObject ? function() {
        "dk.brics.tajs.directives.unreachable";
        // Abort all pending requests
        for (var key in xhrCallbacks) {
            "dk.brics.tajs.directives.unreachable";
            xhrCallbacks[key](0, 1);
        }
    } : false, xhrId = 0, xhrCallbacks;
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
                        var xhr = s.xhr(), handle, i;
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
                            // of an xhr when a network error occured
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
                                        responses.text = xhr.responseText;
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
                        // if we're in sync mode or it's in cache
                        // and has been retrieved directly (IE6 & IE7)
                        // we need to manually fire the callback
                        if (!s.async || xhr.readyState === 4) {
                            "dk.brics.tajs.directives.unreachable";
                            callback();
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
    var elemdisplay = {}, iframe, iframeDoc, rfxtypes = /^(?:toggle|show|hide)$/, rfxnum = /^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i, timerId, fxAttrs = [ // height animations
    [ "height", "marginTop", "marginBottom", "paddingTop", "paddingBottom" ], // width animations
    [ "width", "marginLeft", "marginRight", "paddingLeft", "paddingRight" ], // opacity animations
    [ "opacity" ] ], fxNow;
    jQuery.fn.extend({
        show: function(speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            var elem, display;
            if (speed || speed === 0) {
                "dk.brics.tajs.directives.unreachable";
                return this.animate(genFx("show", 3), speed, easing, callback);
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, j = this.length; i < j; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = this[i];
                    if (elem.style) {
                        "dk.brics.tajs.directives.unreachable";
                        display = elem.style.display;
                        // Reset the inline display of this element to learn if it is
                        // being hidden by cascaded rules or not
                        if (!jQuery._data(elem, "olddisplay") && display === "none") {
                            "dk.brics.tajs.directives.unreachable";
                            display = elem.style.display = "";
                        }
                        // Set elements which have been overridden with display: none
                        // in a stylesheet to whatever the default browser style is
                        // for such an element
                        if (display === "" && jQuery.css(elem, "display") === "none") {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery._data(elem, "olddisplay", defaultDisplay(elem.nodeName));
                        }
                    }
                }
                // Set the display of most of the elements in a second loop
                // to avoid the constant reflow
                for (i = 0; i < j; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = this[i];
                    if (elem.style) {
                        "dk.brics.tajs.directives.unreachable";
                        display = elem.style.display;
                        if (display === "" || display === "none") {
                            "dk.brics.tajs.directives.unreachable";
                            elem.style.display = jQuery._data(elem, "olddisplay") || "";
                        }
                    }
                }
                return this;
            }
        },
        hide: function(speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (speed || speed === 0) {
                "dk.brics.tajs.directives.unreachable";
                return this.animate(genFx("hide", 3), speed, easing, callback);
            } else {
                "dk.brics.tajs.directives.unreachable";
                var elem, display, i = 0, j = this.length;
                for (;i < j; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = this[i];
                    if (elem.style) {
                        "dk.brics.tajs.directives.unreachable";
                        display = jQuery.css(elem, "display");
                        if (display !== "none" && !jQuery._data(elem, "olddisplay")) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery._data(elem, "olddisplay", display);
                        }
                    }
                }
                // Set the display of the elements in a second loop
                // to avoid the constant reflow
                for (i = 0; i < j; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (this[i].style) {
                        "dk.brics.tajs.directives.unreachable";
                        this[i].style.display = "none";
                    }
                }
                return this;
            }
        },
        // Save the old toggle function
        _toggle: jQuery.fn.toggle,
        toggle: function(fn, fn2, callback) {
            "dk.brics.tajs.directives.unreachable";
            var bool = typeof fn === "boolean";
            if (jQuery.isFunction(fn) && jQuery.isFunction(fn2)) {
                "dk.brics.tajs.directives.unreachable";
                this._toggle.apply(this, arguments);
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (fn == null || bool) {
                    "dk.brics.tajs.directives.unreachable";
                    this.each(function() {
                        "dk.brics.tajs.directives.unreachable";
                        var state = bool ? fn : jQuery(this).is(":hidden");
                        if(state){
                            jQuery(this).show();                            
                        }else{
                            jQuery(this).hide();
                        }

                    });
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    this.animate(genFx("toggle", 3), fn, fn2, callback);
                }
            }
            return this;
        },
        fadeTo: function(speed, to, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.filter(":hidden").css("opacity", 0).show().end().animate({
                opacity: to
            }, speed, easing, callback);
        },
        animate: function(prop, speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            var optall = jQuery.speed(speed, easing, callback);
            if (jQuery.isEmptyObject(prop)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(optall.complete, [ false ]);
            }
            // Do not change referenced properties as per-property easing will be lost
            prop = jQuery.extend({}, prop);
            function doAnimation() {
                "dk.brics.tajs.directives.unreachable";
                // XXX 'this' does not always have a nodeName when running the
                // test suite
                if (optall.queue === false) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery._mark(this);
                }
                var opt = jQuery.extend({}, optall), isElement = this.nodeType === 1, hidden = isElement && jQuery(this).is(":hidden"), name, val, p, e, parts, start, end, unit, method;
                // will store per property easing and be used to determine when an animation is complete
                opt.animatedProperties = {};
                for (p in prop) {
                    "dk.brics.tajs.directives.unreachable";
                    // property name normalization
                    name = jQuery.camelCase(p);
                    if (p !== name) {
                        "dk.brics.tajs.directives.unreachable";
                        prop[name] = prop[p];
                        delete prop[p];
                    }
                    val = prop[name];
                    // easing resolution: per property > opt.specialEasing > opt.easing > 'swing' (default)
                    if (jQuery.isArray(val)) {
                        "dk.brics.tajs.directives.unreachable";
                        opt.animatedProperties[name] = val[1];
                        val = prop[name] = val[0];
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        opt.animatedProperties[name] = opt.specialEasing && opt.specialEasing[name] || opt.easing || "swing";
                    }
                    if (val === "hide" && hidden || val === "show" && !hidden) {
                        "dk.brics.tajs.directives.unreachable";
                        return opt.complete.call(this);
                    }
                    if (isElement && (name === "height" || name === "width")) {
                        "dk.brics.tajs.directives.unreachable";
                        // Make sure that nothing sneaks out
                        // Record all 3 overflow attributes because IE does not
                        // change the overflow attribute when overflowX and
                        // overflowY are set to the same value
                        opt.overflow = [ this.style.overflow, this.style.overflowX, this.style.overflowY ];
                        // Set display property to inline-block for height/width
                        // animations on inline elements that are having width/height animated
                        if (jQuery.css(this, "display") === "inline" && jQuery.css(this, "float") === "none") {
                            "dk.brics.tajs.directives.unreachable";
                            // inline-level elements accept inline-block;
                            // block-level elements need to be inline with layout
                            if (!jQuery.support.inlineBlockNeedsLayout || defaultDisplay(this.nodeName) === "inline") {
                                "dk.brics.tajs.directives.unreachable";
                                this.style.display = "inline-block";
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                this.style.zoom = 1;
                            }
                        }
                    }
                }
                if (opt.overflow != null) {
                    "dk.brics.tajs.directives.unreachable";
                    this.style.overflow = "hidden";
                }
                for (p in prop) {
                    "dk.brics.tajs.directives.unreachable";
                    e = new jQuery.fx(this, opt, p);
                    val = prop[p];
                    if (rfxtypes.test(val)) {
                        "dk.brics.tajs.directives.unreachable";
                        // Tracks whether to show or hide based on private
                        // data attached to the element
                        method = jQuery._data(this, "toggle" + p) || (val === "toggle" ? hidden ? "show" : "hide" : 0);
                        if (method) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery._data(this, "toggle" + p, method === "show" ? "hide" : "show");
                            e[method]();
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            e[val]();
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        parts = rfxnum.exec(val);
                        start = e.cur();
                        if (parts) {
                            "dk.brics.tajs.directives.unreachable";
                            end = parseFloat(parts[2]);
                            unit = parts[3] || (jQuery.cssNumber[p] ? "" : "px");
                            // We need to compute starting value
                            if (unit !== "px") {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.style(this, p, (end || 1) + unit);
                                start = (end || 1) / e.cur() * start;
                                jQuery.style(this, p, start + unit);
                            }
                            // If a +=/-= token was provided, we're doing a relative animation
                            if (parts[1]) {
                                "dk.brics.tajs.directives.unreachable";
                                end = (parts[1] === "-=" ? -1 : 1) * end + start;
                            }
                            e.custom(start, end, unit);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            e.custom(start, val, "");
                        }
                    }
                }
                // For JS strict compliance
                return true;
            }
            return optall.queue === false ? this.each(doAnimation) : this.queue(optall.queue, doAnimation);
        },
        stop: function(type, clearQueue, gotoEnd) {
            "dk.brics.tajs.directives.unreachable";
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
                var i, hadTimers = false, timers = jQuery.timers, data = jQuery._data(this);
                // clear marker counters if we know they won't be
                if (!gotoEnd) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery._unmark(true, this);
                }
                function stopQueue(elem, data, i) {
                    "dk.brics.tajs.directives.unreachable";
                    var hooks = data[i];
                    jQuery.removeData(elem, i, true);
                    hooks.stop(gotoEnd);
                }
                if (type == null) {
                    "dk.brics.tajs.directives.unreachable";
                    for (i in data) {
                        "dk.brics.tajs.directives.unreachable";
                        if (data[i].stop && i.indexOf(".run") === i.length - 4) {
                            "dk.brics.tajs.directives.unreachable";
                            stopQueue(this, data, i);
                        }
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (data[i = type + ".run"] && data[i].stop) {
                        "dk.brics.tajs.directives.unreachable";
                        stopQueue(this, data, i);
                    }
                }
                for (i = timers.length; i--; ) {
                    "dk.brics.tajs.directives.unreachable";
                    if (timers[i].elem === this && (type == null || timers[i].queue === type)) {
                        "dk.brics.tajs.directives.unreachable";
                        if (gotoEnd) {
                            "dk.brics.tajs.directives.unreachable";
                            // force the next step to be the last
                            timers[i](true);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            timers[i].saveState();
                        }
                        hadTimers = true;
                        timers.splice(i, 1);
                    }
                }
                // start the next in the queue if the last step wasn't forced
                // timers currently will call their complete callbacks, which will dequeue
                // but only if they were gotoEnd
                if (!(gotoEnd && hadTimers)) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.dequeue(this, type);
                }
            });
        }
    });
    // Animations created synchronously will run synchronously
    function createFxNow() {
        "dk.brics.tajs.directives.unreachable";
        setTimeout(clearFxNow, 0);
        return fxNow = jQuery.now();
    }
    function clearFxNow() {
        "dk.brics.tajs.directives.unreachable";
        fxNow = undefined;
    }
    // Generate parameters to create a standard animation
    function genFx(type, num) {
        var obj = {};
        jQuery.each(fxAttrs.concat.apply([], fxAttrs.slice(0, num)), function() {
            obj[this] = type;
        });
        return obj;
    }
    // Generate shortcuts for custom animations
    jQuery.each({
        slideDown: genFx("show", 1),
        slideUp: genFx("hide", 1),
        slideToggle: genFx("toggle", 1),
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
    jQuery.extend({
        speed: function(speed, easing, fn) {
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
            opt.complete = function(noUnmark) {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.isFunction(opt.old)) {
                    "dk.brics.tajs.directives.unreachable";
                    opt.old.call(this);
                }
                if (opt.queue) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.dequeue(this, opt.queue);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (noUnmark !== false) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery._unmark(this);
                    }
                }
            };
            return opt;
        },
        easing: {
            linear: function(p, n, firstNum, diff) {
                "dk.brics.tajs.directives.unreachable";
                return firstNum + diff * p;
            },
            swing: function(p, n, firstNum, diff) {
                "dk.brics.tajs.directives.unreachable";
                return (-Math.cos(p * Math.PI) / 2 + .5) * diff + firstNum;
            }
        },
        timers: [],
        fx: function(elem, options, prop) {
            "dk.brics.tajs.directives.unreachable";
            this.options = options;
            this.elem = elem;
            this.prop = prop;
            options.orig = options.orig || {};
        }
    });
    jQuery.fx.prototype = {
        // Simple function for setting a style value
        update: function() {
            "dk.brics.tajs.directives.unreachable";
            if (this.options.step) {
                "dk.brics.tajs.directives.unreachable";
                this.options.step.call(this.elem, this.now, this);
            }
            (jQuery.fx.step[this.prop] || jQuery.fx.step._default)(this);
        },
        // Get the current size
        cur: function() {
            "dk.brics.tajs.directives.unreachable";
            if (this.elem[this.prop] != null && (!this.elem.style || this.elem.style[this.prop] == null)) {
                "dk.brics.tajs.directives.unreachable";
                return this.elem[this.prop];
            }
            var parsed, r = jQuery.css(this.elem, this.prop);
            // Empty strings, null, undefined and "auto" are converted to 0,
            // complex values such as "rotate(1rad)" are returned as is,
            // simple values such as "10px" are parsed to Float.
            return isNaN(parsed = parseFloat(r)) ? !r || r === "auto" ? 0 : r : parsed;
        },
        // Start an animation from one number to another
        custom: function(from, to, unit) {
            "dk.brics.tajs.directives.unreachable";
            var self = this, fx = jQuery.fx;
            this.startTime = fxNow || createFxNow();
            this.end = to;
            this.now = this.start = from;
            this.pos = this.state = 0;
            this.unit = unit || this.unit || (jQuery.cssNumber[this.prop] ? "" : "px");
            function t(gotoEnd) {
                "dk.brics.tajs.directives.unreachable";
                return self.step(gotoEnd);
            }
            t.queue = this.options.queue;
            t.elem = this.elem;
            t.saveState = function() {
                "dk.brics.tajs.directives.unreachable";
                if (self.options.hide && jQuery._data(self.elem, "fxshow" + self.prop) === undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery._data(self.elem, "fxshow" + self.prop, self.start);
                }
            };
            if (t() && jQuery.timers.push(t) && !timerId) {
                "dk.brics.tajs.directives.unreachable";
                timerId = setInterval(fx.tick, fx.interval);
            }
        },
        // Simple 'show' function
        show: function() {
            "dk.brics.tajs.directives.unreachable";
            var dataShow = jQuery._data(this.elem, "fxshow" + this.prop);
            // Remember where we started, so that we can go back to it later
            this.options.orig[this.prop] = dataShow || jQuery.style(this.elem, this.prop);
            this.options.show = true;
            // Begin the animation
            // Make sure that we start at a small width/height to avoid any flash of content
            if (dataShow !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                // This show is picking up where a previous hide or show left off
                this.custom(this.cur(), dataShow);
            } else {
                "dk.brics.tajs.directives.unreachable";
                this.custom(this.prop === "width" || this.prop === "height" ? 1 : 0, this.cur());
            }
            // Start by showing the element
            jQuery(this.elem).show();
        },
        // Simple 'hide' function
        hide: function() {
            "dk.brics.tajs.directives.unreachable";
            // Remember where we started, so that we can go back to it later
            this.options.orig[this.prop] = jQuery._data(this.elem, "fxshow" + this.prop) || jQuery.style(this.elem, this.prop);
            this.options.hide = true;
            // Begin the animation
            this.custom(this.cur(), 0);
        },
        // Each step of an animation
        step: function(gotoEnd) {
            "dk.brics.tajs.directives.unreachable";
            var p, n, complete, t = fxNow || createFxNow(), done = true, elem = this.elem, options = this.options;
            if (gotoEnd || t >= options.duration + this.startTime) {
                "dk.brics.tajs.directives.unreachable";
                this.now = this.end;
                this.pos = this.state = 1;
                this.update();
                options.animatedProperties[this.prop] = true;
                for (p in options.animatedProperties) {
                    "dk.brics.tajs.directives.unreachable";
                    if (options.animatedProperties[p] !== true) {
                        "dk.brics.tajs.directives.unreachable";
                        done = false;
                    }
                }
                if (done) {
                    "dk.brics.tajs.directives.unreachable";
                    // Reset the overflow
                    if (options.overflow != null && !jQuery.support.shrinkWrapBlocks) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.each([ "", "X", "Y" ], function(index, value) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.style["overflow" + value] = options.overflow[index];
                        });
                    }
                    // Hide the element if the "hide" operation was done
                    if (options.hide) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery(elem).hide();
                    }
                    // Reset the properties, if the item has been hidden or shown
                    if (options.hide || options.show) {
                        "dk.brics.tajs.directives.unreachable";
                        for (p in options.animatedProperties) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.style(elem, p, options.orig[p]);
                            jQuery.removeData(elem, "fxshow" + p, true);
                            // Toggle data is no longer needed
                            jQuery.removeData(elem, "toggle" + p, true);
                        }
                    }
                    // Execute the complete function
                    // in the event that the complete function throws an exception
                    // we must ensure it won't be called twice. #5684
                    complete = options.complete;
                    if (complete) {
                        "dk.brics.tajs.directives.unreachable";
                        options.complete = false;
                        complete.call(elem);
                    }
                }
                return false;
            } else {
                "dk.brics.tajs.directives.unreachable";
                // classical easing cannot be used with an Infinity duration
                if (options.duration == Infinity) {
                    "dk.brics.tajs.directives.unreachable";
                    this.now = t;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    n = t - this.startTime;
                    this.state = n / options.duration;
                    // Perform the easing function, defaults to swing
                    this.pos = jQuery.easing[options.animatedProperties[this.prop]](this.state, n, 0, 1, options.duration);
                    this.now = this.start + (this.end - this.start) * this.pos;
                }
                // Perform the next step of the animation
                this.update();
            }
            return true;
        }
    };
    jQuery.extend(jQuery.fx, {
        tick: function() {
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
        },
        interval: 13,
        stop: function() {
            "dk.brics.tajs.directives.unreachable";
            clearInterval(timerId);
            timerId = null;
        },
        speeds: {
            slow: 600,
            fast: 200,
            // Default speed
            _default: 400
        },
        step: {
            opacity: function(fx) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.style(fx.elem, "opacity", fx.now);
            },
            _default: function(fx) {
                "dk.brics.tajs.directives.unreachable";
                if (fx.elem.style && fx.elem.style[fx.prop] != null) {
                    "dk.brics.tajs.directives.unreachable";
                    fx.elem.style[fx.prop] = fx.now + fx.unit;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    fx.elem[fx.prop] = fx.now;
                }
            }
        }
    });
    // Adds width/height step functions
    // Do not set anything below 0
    jQuery.each([ "width", "height" ], function(i, prop) {
        jQuery.fx.step[prop] = function(fx) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.style(fx.elem, prop, Math.max(0, fx.now));
        };
    });
    if (jQuery.expr && jQuery.expr.filters) {
        jQuery.expr.filters.animated = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.grep(jQuery.timers, function(fn) {
                "dk.brics.tajs.directives.unreachable";
                return elem === fn.elem;
            }).length;
        };
    }
    // Try to restore the default display value of an element
    function defaultDisplay(nodeName) {
        "dk.brics.tajs.directives.unreachable";
        if (!elemdisplay[nodeName]) {
            "dk.brics.tajs.directives.unreachable";
            var body = document.body, elem = jQuery("<" + nodeName + ">").appendTo(body), display = elem.css("display");
            elem.remove();
            // If the simple way fails,
            // get element's real default display by attaching it to a temp iframe
            if (display === "none" || display === "") {
                "dk.brics.tajs.directives.unreachable";
                // No iframe to use yet, so create it
                if (!iframe) {
                    "dk.brics.tajs.directives.unreachable";
                    iframe = document.createElement("iframe");
                    iframe.frameBorder = iframe.width = iframe.height = 0;
                }
                body.appendChild(iframe);
                // Create a cacheable copy of the iframe document on first call.
                // IE and Opera will allow us to reuse the iframeDoc without re-writing the fake HTML
                // document to it; WebKit & Firefox won't allow reusing the iframe document.
                if (!iframeDoc || !iframe.createElement) {
                    "dk.brics.tajs.directives.unreachable";
                    iframeDoc = (iframe.contentWindow || iframe.contentDocument).document;
                    iframeDoc.write((document.compatMode === "CSS1Compat" ? "<!doctype html>" : "") + "<html><body>");
                    iframeDoc.close();
                }
                elem = iframeDoc.createElement(nodeName);
                iframeDoc.body.appendChild(elem);
                display = jQuery.css(elem, "display");
                body.removeChild(iframe);
            }
            // Store the correct default display
            elemdisplay[nodeName] = display;
        }
        return elemdisplay[nodeName];
    }
    var rtable = /^t(?:able|d|h)$/i, rroot = /^(?:body|html)$/i;
    if ("getBoundingClientRect" in document.documentElement) {
        jQuery.fn.offset = function(options) {
            "dk.brics.tajs.directives.unreachable";
            var elem = this[0], box;
            if (options) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.offset.setOffset(this, options, i);
                });
            }
            if (!elem || !elem.ownerDocument) {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            if (elem === elem.ownerDocument.body) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.offset.bodyOffset(elem);
            }
            try {
                "dk.brics.tajs.directives.unreachable";
                box = elem.getBoundingClientRect();
            } catch (e) {}
            var doc = elem.ownerDocument, docElem = doc.documentElement;
            // Make sure we're not dealing with a disconnected DOM node
            if (!box || !jQuery.contains(docElem, elem)) {
                "dk.brics.tajs.directives.unreachable";
                return box ? {
                    top: box.top,
                    left: box.left
                } : {
                    top: 0,
                    left: 0
                };
            }
            var body = doc.body, win = getWindow(doc), clientTop = docElem.clientTop || body.clientTop || 0, clientLeft = docElem.clientLeft || body.clientLeft || 0, scrollTop = win.pageYOffset || jQuery.support.boxModel && docElem.scrollTop || body.scrollTop, scrollLeft = win.pageXOffset || jQuery.support.boxModel && docElem.scrollLeft || body.scrollLeft, top = box.top + scrollTop - clientTop, left = box.left + scrollLeft - clientLeft;
            return {
                top: top,
                left: left
            };
        };
    } else {
        "dk.brics.tajs.directives.unreachable";
        jQuery.fn.offset = function(options) {
            "dk.brics.tajs.directives.unreachable";
            var elem = this[0];
            if (options) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.offset.setOffset(this, options, i);
                });
            }
            if (!elem || !elem.ownerDocument) {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            if (elem === elem.ownerDocument.body) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.offset.bodyOffset(elem);
            }
            var computedStyle, offsetParent = elem.offsetParent, prevOffsetParent = elem, doc = elem.ownerDocument, docElem = doc.documentElement, body = doc.body, defaultView = doc.defaultView, prevComputedStyle = defaultView ? defaultView.getComputedStyle(elem, null) : elem.currentStyle, top = elem.offsetTop, left = elem.offsetLeft;
            while ((elem = elem.parentNode) && elem !== body && elem !== docElem) {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.support.fixedPosition && prevComputedStyle.position === "fixed") {
                    "dk.brics.tajs.directives.unreachable";
                    break;
                }
                computedStyle = defaultView ? defaultView.getComputedStyle(elem, null) : elem.currentStyle;
                top -= elem.scrollTop;
                left -= elem.scrollLeft;
                if (elem === offsetParent) {
                    "dk.brics.tajs.directives.unreachable";
                    top += elem.offsetTop;
                    left += elem.offsetLeft;
                    if (jQuery.support.doesNotAddBorder && !(jQuery.support.doesAddBorderForTableAndCells && rtable.test(elem.nodeName))) {
                        "dk.brics.tajs.directives.unreachable";
                        top += parseFloat(computedStyle.borderTopWidth) || 0;
                        left += parseFloat(computedStyle.borderLeftWidth) || 0;
                    }
                    prevOffsetParent = offsetParent;
                    offsetParent = elem.offsetParent;
                }
                if (jQuery.support.subtractsBorderForOverflowNotVisible && computedStyle.overflow !== "visible") {
                    "dk.brics.tajs.directives.unreachable";
                    top += parseFloat(computedStyle.borderTopWidth) || 0;
                    left += parseFloat(computedStyle.borderLeftWidth) || 0;
                }
                prevComputedStyle = computedStyle;
            }
            if (prevComputedStyle.position === "relative" || prevComputedStyle.position === "static") {
                "dk.brics.tajs.directives.unreachable";
                top += body.offsetTop;
                left += body.offsetLeft;
            }
            if (jQuery.support.fixedPosition && prevComputedStyle.position === "fixed") {
                "dk.brics.tajs.directives.unreachable";
                top += Math.max(docElem.scrollTop, body.scrollTop);
                left += Math.max(docElem.scrollLeft, body.scrollLeft);
            }
            return {
                top: top,
                left: left
            };
        };
    }
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
                return null;
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
                return offsetParent;
            });
        }
    });
    // Create scrollLeft and scrollTop methods
    jQuery.each([ "Left", "Top" ], function(i, name) {
        var method = "scroll" + name;
        jQuery.fn[method] = function(val) {
            "dk.brics.tajs.directives.unreachable";
            var elem, win;
            if (val === undefined) {
                "dk.brics.tajs.directives.unreachable";
                elem = this[0];
                if (!elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return null;
                }
                win = getWindow(elem);
                // Return the scroll offset
                return win ? "pageXOffset" in win ? win[i ? "pageYOffset" : "pageXOffset"] : jQuery.support.boxModel && win.document.documentElement[method] || win.document.body[method] : elem[method];
            }
            // Set the scroll offset
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                win = getWindow(this);
                if (win) {
                    "dk.brics.tajs.directives.unreachable";
                    win.scrollTo(!i ? val : jQuery(win).scrollLeft(), i ? val : jQuery(win).scrollTop());
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    this[method] = val;
                }
            });
        };
    });
    function getWindow(elem) {
        "dk.brics.tajs.directives.unreachable";
        return jQuery.isWindow(elem) ? elem : elem.nodeType === 9 ? elem.defaultView || elem.parentWindow : false;
    }
    // Create width, height, innerHeight, innerWidth, outerHeight and outerWidth methods
    jQuery.each([ "Height", "Width" ], function(i, name) {
        var type = name.toLowerCase();
        // innerHeight and innerWidth
        jQuery.fn["inner" + name] = function() {
            "dk.brics.tajs.directives.unreachable";
            var elem = this[0];
            return elem ? elem.style ? parseFloat(jQuery.css(elem, type, "padding")) : this[type]() : null;
        };
        // outerHeight and outerWidth
        jQuery.fn["outer" + name] = function(margin) {
            "dk.brics.tajs.directives.unreachable";
            var elem = this[0];
            return elem ? elem.style ? parseFloat(jQuery.css(elem, type, margin ? "margin" : "border")) : this[type]() : null;
        };
        jQuery.fn[type] = function(size) {
            "dk.brics.tajs.directives.unreachable";
            // Get window width or height
            var elem = this[0];
            if (!elem) {
                "dk.brics.tajs.directives.unreachable";
                return size == null ? null : this;
            }
            if (jQuery.isFunction(size)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    var self = jQuery(this);
                    self[type](size.call(this, i, self[type]()));
                });
            }
            if (jQuery.isWindow(elem)) {
                "dk.brics.tajs.directives.unreachable";
                // Everyone else use document.documentElement or document.body depending on Quirks vs Standards mode
                // 3rd condition allows Nokia support, as it supports the docElem prop but not CSS1Compat
                var docElemProp = elem.document.documentElement["client" + name], body = elem.document.body;
                return elem.document.compatMode === "CSS1Compat" && docElemProp || body && body["client" + name] || docElemProp;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (elem.nodeType === 9) {
                    "dk.brics.tajs.directives.unreachable";
                    // Either scroll[Width/Height] or offset[Width/Height], whichever is greater
                    return Math.max(elem.documentElement["client" + name], elem.body["scroll" + name], elem.documentElement["scroll" + name], elem.body["offset" + name], elem.documentElement["offset" + name]);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (size === undefined) {
                        "dk.brics.tajs.directives.unreachable";
                        var orig = jQuery.css(elem, type), ret = parseFloat(orig);
                        return jQuery.isNumeric(ret) ? ret : orig;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        return this.css(type, typeof size === "string" ? size : size + "px");
                    }
                }
            }
        };
    });
    // Expose jQuery to the global object
    window.jQuery = window.$ = jQuery;
})(window);
