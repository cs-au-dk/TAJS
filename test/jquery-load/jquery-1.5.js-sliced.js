/*!
 * jQuery JavaScript Library v1.5
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
 * Date: Mon Jan 31 08:31:29 2011 -0500
 */
(function(window, undefined) {
    // Use the correct document accordingly with window argument (sandbox)
    var document = window.document;
    var jQuery = function() {
        // Define a local copy of jQuery
        var jQuery = function(selector, context) {
            // The jQuery object is actually just the init constructor 'enhanced'
            return new jQuery.fn.init(selector, context, rootjQuery);
        }, // Map over jQuery in case of overwrite
        _jQuery = window.jQuery, // Map over the $ in case of overwrite
        _$ = window.$, // A central reference to the root jQuery(document)
        rootjQuery, // A simple way to check for HTML strings or ID strings
        // (both of which we optimize for)
        quickExpr = /^(?:[^<]*(<[\w\W]+>)[^>]*$|#([\w\-]+)$)/, // Check if a string has a non-whitespace character in it
        rnotwhite = /\S/, // Used for trimming whitespace
        trimLeft = /^\s+/, trimRight = /\s+$/, // Check for digits
        rdigit = /\d/, // Match a standalone tag
        rsingleTag = /^<(\w+)\s*\/?>(?:<\/\1>)?$/, // JSON RegExp
        rvalidchars = /^[\],:{}\s]*$/, rvalidescape = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, rvalidtokens = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g, // Useragent RegExp
        rwebkit = /(webkit)[ \/]([\w.]+)/, ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/, rmsie = /(msie) ([\w.]+)/, rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/, // Keep a UserAgent string for use with jQuery.browser
        userAgent = navigator.userAgent, // For matching the engine and version of the browser
        browserMatch, // Has the ready events already been bound?
        readyBound = false, // The deferred used on DOM ready
        readyList, // Promise methods
        promiseMethods = "then done fail isResolved isRejected promise".split(" "), // The ready event handler
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
                    this.selector = "body";
                    this.length = 1;
                    return this;
                }
                // Handle HTML strings
                if (typeof selector === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    // Are we dealing with HTML string or an ID?
                    match = quickExpr.exec(selector);
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
            jquery: "1.5",
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
                readyList.done(fn);
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
                window.$ = _$;
                if (deep) {
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
            // Handle when the DOM is ready
            ready: function(wait) {
                // A third-party is pushing the ready event forwards
                if (wait === true) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.readyWait--;
                }
                // Make sure that the DOM is not already loaded
                if (!jQuery.readyWait || wait !== true && !jQuery.isReady) {
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
                        jQuery(document).trigger("ready").unbind("ready");
                    }
                }
            },
            bindReady: function() {
                if (readyBound) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                readyBound = true;
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
            isNaN: function(obj) {
                "dk.brics.tajs.directives.unreachable";
                return obj == null || !rdigit.test(obj) || isNaN(obj);
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
                // Not own constructor property must be Object
                if (obj.constructor && !hasOwn.call(obj, "constructor") && !hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
                // Own properties are enumerated firstly, so to speed up,
                // if last one is own, then all properties are own.
                var key;
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
                // Make sure the incoming data is actual JSON
                // Logic borrowed from http://json.org/json2.js
                if (rvalidchars.test(data.replace(rvalidescape, "@").replace(rvalidtokens, "]").replace(rvalidbraces, ""))) {
                    "dk.brics.tajs.directives.unreachable";
                    // Try to use the native JSON parser first
                    return window.JSON && window.JSON.parse ? window.JSON.parse(data) : new Function("return " + data)();
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.error("Invalid JSON: " + data);
                }
            },
            // Cross-browser xml parsing
            // (xml & tmp used internally)
            parseXML: function(data, xml, tmp) {
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
                tmp = xml.documentElement;
                if (!tmp || !tmp.nodeName || tmp.nodeName === "parsererror") {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.error("Invalid XML: " + data);
                }
                return xml;
            },
            noop: function() {},
            // Evalulates a script in a global context
            globalEval: function(data) {
                "dk.brics.tajs.directives.unreachable";
                if (data && rnotwhite.test(data)) {
                    "dk.brics.tajs.directives.unreachable";
                    // Inspired by code by Andrea Giammarchi
                    // http://webreflection.blogspot.com/2007/08/global-scope-evaluation-and-dom.html
                    var head = document.getElementsByTagName("head")[0] || document.documentElement, script = document.createElement("script");
                    script.type = "text/javascript";
                    if (jQuery.support.scriptEval()) {
                        "dk.brics.tajs.directives.unreachable";
                        script.appendChild(document.createTextNode(data));
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        script.text = data;
                    }
                    // Use insertBefore instead of appendChild to circumvent an IE6 bug.
                    // This arises when a base node is used (#2709).
                    head.insertBefore(script, head.firstChild);
                    head.removeChild(script);
                }
            },
            nodeName: function(elem, name) {
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
                        for (var value = object[0]; i < length && callback.call(value, i, value) !== false; value = object[++i]) {}
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
            inArray: function(elem, array) {
                "dk.brics.tajs.directives.unreachable";
                if (array.indexOf) {
                    "dk.brics.tajs.directives.unreachable";
                    return array.indexOf(elem);
                }
                for (var i = 0, length = array.length; i < length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (array[i] === elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return i;
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
                var ret = [], value;
                // Go through the array, translating each of the items to their
                // new value (or values).
                for (var i = 0, length = elems.length; i < length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    value = callback(elems[i], i, arg);
                    if (value != null) {
                        "dk.brics.tajs.directives.unreachable";
                        ret[ret.length] = value;
                    }
                }
                // Flatten any nested arrays
                return ret.concat.apply([], ret);
            },
            // A global GUID counter for objects
            guid: 1,
            proxy: function(fn, proxy, thisObject) {
                "dk.brics.tajs.directives.unreachable";
                if (arguments.length === 2) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof proxy === "string") {
                        "dk.brics.tajs.directives.unreachable";
                        thisObject = fn;
                        fn = thisObject[proxy];
                        proxy = undefined;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (proxy && !jQuery.isFunction(proxy)) {
                            "dk.brics.tajs.directives.unreachable";
                            thisObject = proxy;
                            proxy = undefined;
                        }
                    }
                }
                if (!proxy && fn) {
                    "dk.brics.tajs.directives.unreachable";
                    proxy = function() {
                        "dk.brics.tajs.directives.unreachable";
                        return fn.apply(thisObject || this, arguments);
                    };
                }
                // Set the guid of unique handler to the same of original handler, so it can be removed
                if (fn) {
                    "dk.brics.tajs.directives.unreachable";
                    proxy.guid = fn.guid = fn.guid || proxy.guid || jQuery.guid++;
                }
                // So proxy can be declared as an argument
                return proxy;
            },
            // Mutifunctional method to get and set values to a collection
            // The value/s can be optionally by executed if its a function
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
            // Create a simple deferred (one callbacks list)
            _Deferred: function() {
                var // callbacks list
                callbacks = [], // stored [ context , args ]
                fired, // to avoid firing when already doing so
                firing, // flag to know if the deferred has been cancelled
                cancelled, // the deferred itself
                deferred = {
                    // done( f1, f2, ...)
                    done: function() {
                        if (!cancelled) {
                            var args = arguments, i, length, elem, type, _fired;
                            if (fired) {
                                "dk.brics.tajs.directives.unreachable";
                                _fired = fired;
                                fired = 0;
                            }
                            for (i = 0, length = args.length; i < length; i++) {
                                elem = args[i];
                                type = jQuery.type(elem);
                                if (type === "array") {
                                    "dk.brics.tajs.directives.unreachable";
                                    deferred.done.apply(deferred, elem);
                                } else {
                                    if (type === "function") {
                                        callbacks.push(elem);
                                    }
                                }
                            }
                            if (_fired) {
                                "dk.brics.tajs.directives.unreachable";
                                deferred.resolveWith(_fired[0], _fired[1]);
                            }
                        }
                        return this;
                    },
                    // resolve with given context and args
                    resolveWith: function(context, args) {
                        if (!cancelled && !fired && !firing) {
                            firing = 1;
                            try {
                                while (callbacks[0]) {
                                    callbacks.shift().apply(context, args);
                                }
                            } catch(e){} finally {
                                fired = [ context, args ];
                                firing = 0;
                            }
                        }
                        return this;
                    },
                    // resolve with this as context and given arguments
                    resolve: function() {
                        "dk.brics.tajs.directives.unreachable";
                        deferred.resolveWith(jQuery.isFunction(this.promise) ? this.promise() : this, arguments);
                        return this;
                    },
                    // Has this deferred been resolved?
                    isResolved: function() {
                        "dk.brics.tajs.directives.unreachable";
                        return !!(firing || fired);
                    },
                    // Cancel
                    cancel: function() {
                        "dk.brics.tajs.directives.unreachable";
                        cancelled = 1;
                        callbacks = [];
                        return this;
                    }
                };
                return deferred;
            },
            // Full fledged deferred (two callbacks list)
            Deferred: function(func) {
                "dk.brics.tajs.directives.unreachable";
                var deferred = jQuery._Deferred(), failDeferred = jQuery._Deferred(), promise;
                // Add errorDeferred methods, then and promise
                jQuery.extend(deferred, {
                    then: function(doneCallbacks, failCallbacks) {
                        "dk.brics.tajs.directives.unreachable";
                        deferred.done(doneCallbacks).fail(failCallbacks);
                        return this;
                    },
                    fail: failDeferred.done,
                    rejectWith: failDeferred.resolveWith,
                    reject: failDeferred.resolve,
                    isRejected: failDeferred.isResolved,
                    // Get a promise for this deferred
                    // If obj is provided, the promise aspect is added to the object
                    promise: function(obj, i) {
                        "dk.brics.tajs.directives.unreachable";
                        if (obj == null) {
                            "dk.brics.tajs.directives.unreachable";
                            if (promise) {
                                "dk.brics.tajs.directives.unreachable";
                                return promise;
                            }
                            promise = obj = {};
                        }
                        i = promiseMethods.length;
                        while (i--) {
                            "dk.brics.tajs.directives.unreachable";
                            obj[promiseMethods[i]] = deferred[promiseMethods[i]];
                        }
                        return obj;
                    }
                });
                // Make sure only one callback list will be used
                deferred.then(failDeferred.cancel, deferred.cancel);
                // Unexpose cancel
                delete deferred.cancel;
                // Call given func if any
                if (func) {
                    "dk.brics.tajs.directives.unreachable";
                    func.call(deferred, deferred);
                }
                return deferred;
            },
            // Deferred helper
            when: function(object) {
                "dk.brics.tajs.directives.unreachable";
                var args = arguments, length = args.length, deferred = length <= 1 && object && jQuery.isFunction(object.promise) ? object : jQuery.Deferred(), promise = deferred.promise(), resolveArray;
                if (length > 1) {
                    "dk.brics.tajs.directives.unreachable";
                    resolveArray = new Array(length);
                    jQuery.each(args, function(index, element) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.when(element).then(function(value) {
                            "dk.brics.tajs.directives.unreachable";
                            resolveArray[index] = arguments.length > 1 ? slice.call(arguments, 0) : value;
                            if (!--length) {
                                "dk.brics.tajs.directives.unreachable";
                                deferred.resolveWith(promise, resolveArray);
                            }
                        }, deferred.reject);
                    });
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (deferred !== object) {
                        "dk.brics.tajs.directives.unreachable";
                        deferred.resolve(object);
                    }
                }
                return promise;
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
                function jQuerySubclass(selector, context) {
                    "dk.brics.tajs.directives.unreachable";
                    return new jQuerySubclass.fn.init(selector, context);
                }
                jQuery.extend(true, jQuerySubclass, this);
                jQuerySubclass.superclass = this;
                jQuerySubclass.fn = jQuerySubclass.prototype = this();
                jQuerySubclass.fn.constructor = jQuerySubclass;
                jQuerySubclass.subclass = this.subclass;
                jQuerySubclass.fn.init = function init(selector, context) {
                    "dk.brics.tajs.directives.unreachable";
                    if (context && context instanceof jQuery && !(context instanceof jQuerySubclass)) {
                        "dk.brics.tajs.directives.unreachable";
                        context = jQuerySubclass(context);
                    }
                    return jQuery.fn.init.call(this, selector, context, rootjQuerySubclass);
                };
                jQuerySubclass.fn.init.prototype = jQuerySubclass.fn;
                var rootjQuerySubclass = jQuerySubclass(document);
                return jQuerySubclass;
            },
            browser: {}
        });
        // Create readyList deferred
        readyList = jQuery._Deferred();
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
        if (indexOf) {
            jQuery.inArray = function(elem, array) {
                "dk.brics.tajs.directives.unreachable";
                return indexOf.call(array, elem);
            };
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
        // Expose jQuery to the global object
        return window.jQuery = window.$ = jQuery;
    }();
    (function() {
        jQuery.support = {};
        var div = document.createElement("div");
        div.style.display = "none";
        div.innerHTML = "   <link/><table></table><a href='/a' style='color:red;float:left;opacity:.55;'>a</a><input type='checkbox'/>";
        var all = div.getElementsByTagName("*"), a = div.getElementsByTagName("a")[0], select = document.createElement("select"), opt = select.appendChild(document.createElement("option"));
        // Can't get basic test support
        if (!all || !all.length || !a) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        jQuery.support = {
            // IE strips leading whitespace when .innerHTML is used
            leadingWhitespace: div.firstChild.nodeType === 3,
            // Make sure that tbody elements aren't automatically inserted
            // IE will insert them into empty tables
            tbody: !div.getElementsByTagName("tbody").length,
            // Make sure that link elements get serialized correctly by innerHTML
            // This requires a wrapper element in IE
            htmlSerialize: !!div.getElementsByTagName("link").length,
            // Get the style information from getAttribute
            // (IE uses .cssText insted)
            style: /red/.test(a.getAttribute("style")),
            // Make sure that URLs aren't manipulated
            // (IE normalizes it by default)
            hrefNormalized: a.getAttribute("href") === "/a",
            // Make sure that element opacity exists
            // (IE uses filter instead)
            // Use a regex to work around a WebKit issue. See #5145
            opacity: /^0.55$/.test(a.style.opacity),
            // Verify style float existence
            // (IE uses styleFloat instead of cssFloat)
            cssFloat: !!a.style.cssFloat,
            // Make sure that if no value is specified for a checkbox
            // that it defaults to "on".
            // (WebKit defaults to "" instead)
            checkOn: div.getElementsByTagName("input")[0].value === "on",
            // Make sure that a selected-by-default option has a working selected property.
            // (WebKit defaults to false instead of true, IE too, if it's in an optgroup)
            optSelected: opt.selected,
            // Will be defined later
            deleteExpando: true,
            optDisabled: false,
            checkClone: false,
            _scriptEval: null,
            noCloneEvent: true,
            boxModel: null,
            inlineBlockNeedsLayout: false,
            shrinkWrapBlocks: false,
            reliableHiddenOffsets: true
        };
        // Make sure that the options inside disabled selects aren't marked as disabled
        // (WebKit marks them as diabled)
        select.disabled = true;
        jQuery.support.optDisabled = !opt.disabled;
        jQuery.support.scriptEval = function() {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.support._scriptEval === null) {
                "dk.brics.tajs.directives.unreachable";
                var root = document.documentElement, script = document.createElement("script"), id = "script" + "TAJS_NOW";
                script.type = "text/javascript";
                try {
                    "dk.brics.tajs.directives.unreachable";
                    script.appendChild(document.createTextNode("window." + id + "=1;"));
                } catch (e) {}
                root.insertBefore(script, root.firstChild);
                // Make sure that the execution of code works by injecting a script
                // tag with appendChild/createTextNode
                // (IE doesn't support this, fails, and uses .text instead)
                if (window[id]) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.support._scriptEval = true;
                    delete window[id];
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.support._scriptEval = false;
                }
                root.removeChild(script);
                // release memory in IE
                root = script = id = null;
            }
            return jQuery.support._scriptEval;
        };
        // Test to see if it's possible to delete an expando from an element
        // Fails in Internet Explorer
        try {
            delete div.test;
        } catch (e) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.support.deleteExpando = false;
        }
        if (div.attachEvent && div.fireEvent) {
            "dk.brics.tajs.directives.unreachable";
            div.attachEvent("onclick", function click() {
                "dk.brics.tajs.directives.unreachable";
                // Cloning a node shouldn't copy over any
                // bound event handlers (IE does this)
                jQuery.support.noCloneEvent = false;
                div.detachEvent("onclick", click);
            });
            div.cloneNode(true).fireEvent("onclick");
        }
        div = document.createElement("div");
        div.innerHTML = "<input type='radio' name='radiotest' checked='checked'/>";
        var fragment = document.createDocumentFragment();
        fragment.appendChild(div.firstChild);
        // WebKit doesn't clone checked state correctly in fragments
        jQuery.support.checkClone = fragment.cloneNode(true).cloneNode(true).lastChild.checked;
        // Figure out if the W3C box model works as expected
        // document.body must exist before we can do this
        jQuery(function() {
            var div = document.createElement("div"), body = document.getElementsByTagName("body")[0];
            // Frameset documents with no body should not run this code
            if (!body) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            div.style.width = div.style.paddingLeft = "1px";
            body.appendChild(div);
            jQuery.boxModel = jQuery.support.boxModel = div.offsetWidth === 2;
            if ("zoom" in div.style) {
                // Check if natively block-level elements act like inline-block
                // elements when setting their display to 'inline' and giving
                // them layout
                // (IE < 8 does this)
                div.style.display = "inline";
                div.style.zoom = 1;
                jQuery.support.inlineBlockNeedsLayout = div.offsetWidth === 2;
                // Check if elements with layout shrink-wrap their children
                // (IE 6 does this)
                div.style.display = "";
                div.innerHTML = "<div style='width:4px;'></div>";
                jQuery.support.shrinkWrapBlocks = div.offsetWidth !== 2;
            }
            div.innerHTML = "<table><tr><td style='padding:0;border:0;display:none'></td><td>t</td></tr></table>";
            var tds = div.getElementsByTagName("td");
            // Check if table cells still have offsetWidth/Height when they are set
            // to display:none and there are still other visible table cells in a
            // table row; if so, offsetWidth/Height are not reliable for use when
            // determining if an element has been hidden directly using
            // display:none (it is still safe to use offsets if a parent element is
            // hidden; don safety goggles and see bug #4512 for more information).
            // (only IE 8 fails this test)
            jQuery.support.reliableHiddenOffsets = tds[0].offsetHeight === 0;
            tds[0].style.display = "";
            tds[1].style.display = "none";
            // Check if empty table cells still have offsetWidth/Height
            // (IE < 8 fail this test)
            jQuery.support.reliableHiddenOffsets = jQuery.support.reliableHiddenOffsets && tds[0].offsetHeight === 0;
            div.innerHTML = "";
            body.removeChild(div).style.display = "none";
            div = tds = null;
        });
        // Technique from Juriy Zaytsev
        // http://thinkweb2.com/projects/prototype/detecting-event-support-without-browser-sniffing/
        var eventSupported = function(eventName) {
            var el = document.createElement("div");
            eventName = "on" + eventName;
            // We only care about the case where non-standard event systems
            // are used, namely in IE. Short-circuiting here helps us to
            // avoid an eval call (in setAttribute) which can cause CSP
            // to go haywire. See: https://developer.mozilla.org/en/Security/CSP
            if (!el.attachEvent) {
                return true;
            }
            "dk.brics.tajs.directives.unreachable";
            var isSupported = eventName in el;
            if (!isSupported) {
                "dk.brics.tajs.directives.unreachable";
                el.setAttribute(eventName, "return;");
                isSupported = typeof el[eventName] === "function";
            }
            el = null;
            return isSupported;
        };
        jQuery.support.submitBubbles = eventSupported("submit");
        jQuery.support.changeBubbles = eventSupported("change");
        // release memory in IE
        div = all = a = null;
    })();
    var rbrace = /^(?:\{.*\}|\[.*\])$/;
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
            return !!elem && !jQuery.isEmptyObject(elem);
        },
        data: function(elem, name, data, pvt) {
            if (!jQuery.acceptData(elem)) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var internalKey = jQuery.expando, getByName = typeof name === "string", thisCache, // We have to handle DOM nodes and JS objects differently because IE6-7
            // can't GC object references properly across the DOM-JS boundary
            isNode = elem.nodeType, // Only DOM nodes need the global jQuery cache; JS object data is
            // attached directly to the object so GC can occur automatically
            cache = isNode ? jQuery.cache : elem, // Only defining an ID for JS objects if its cache already exists allows
            // the code to shortcut on the same path as a DOM node with no cache
            id = isNode ? elem[jQuery.expando] : elem[jQuery.expando] && jQuery.expando;
            // Avoid doing any more work than we need to when trying to get data on an
            // object that has no data at all
            if ((!id || pvt && id && !cache[id][internalKey]) && getByName && data === undefined) {
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
            }
            // An object can be passed to jQuery.data instead of a key/value pair; this gets
            // shallow copied over onto the existing cache
            if (typeof name === "object") {
                "dk.brics.tajs.directives.unreachable";
                if (pvt) {
                    "dk.brics.tajs.directives.unreachable";
                    cache[id][internalKey] = jQuery.extend(cache[id][internalKey], name);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    cache[id] = jQuery.extend(cache[id], name);
                }
            }
            thisCache = cache[id];
            // Internal jQuery data is stored in a separate object inside the object's data
            // cache in order to avoid key collisions between internal data and user-defined
            // data
            if (pvt) {
                "dk.brics.tajs.directives.unreachable";
                if (!thisCache[internalKey]) {
                    "dk.brics.tajs.directives.unreachable";
                    thisCache[internalKey] = {};
                }
                thisCache = thisCache[internalKey];
            }
            if (data !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                thisCache[name] = data;
            }
            // TODO: This is a hack for 1.5 ONLY. It will be removed in 1.6. Users should
            // not attempt to inspect the internal events object using jQuery.data, as this
            // internal data object is undocumented and subject to change.
            if (name === "events" && !thisCache[name]) {
                "dk.brics.tajs.directives.unreachable";
                return thisCache[internalKey] && thisCache[internalKey].events;
            }
            return getByName ? thisCache[name] : thisCache;
        },
        removeData: function(elem, name, pvt) {
            "dk.brics.tajs.directives.unreachable";
            if (!jQuery.acceptData(elem)) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var internalKey = jQuery.expando, isNode = elem.nodeType, // See jQuery.data for more information
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
                var thisCache = pvt ? cache[id][internalKey] : cache[id];
                if (thisCache) {
                    "dk.brics.tajs.directives.unreachable";
                    delete thisCache[name];
                    // If there is no data left in the cache, we want to continue
                    // and let the cache object itself get destroyed
                    if (!jQuery.isEmptyObject(thisCache)) {
                        "dk.brics.tajs.directives.unreachable";
                        return;
                    }
                }
            }
            // See jQuery.data for more information
            if (pvt) {
                "dk.brics.tajs.directives.unreachable";
                delete cache[id][internalKey];
                // Don't destroy the parent cache unless the internal data object
                // had been the only thing left in it
                if (!jQuery.isEmptyObject(cache[id])) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
            }
            var internalCache = cache[id][internalKey];
            // Browsers that fail expando deletion also refuse to delete expandos on
            // the window, but it will allow it on all other JS objects; other browsers
            // don't care
            if (jQuery.support.deleteExpando || cache != window) {
                "dk.brics.tajs.directives.unreachable";
                delete cache[id];
            } else {
                "dk.brics.tajs.directives.unreachable";
                cache[id] = null;
            }
            // We destroyed the entire user cache at once because it's faster than
            // iterating through each key, but we need to continue to persist internal
            // data if it existed
            if (internalCache) {
                "dk.brics.tajs.directives.unreachable";
                cache[id] = {};
                cache[id][internalKey] = internalCache;
            } else {
                "dk.brics.tajs.directives.unreachable";
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
            var data = null;
            if (typeof key === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                if (this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    data = jQuery.data(this[0]);
                    if (this[0].nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        var attr = this[0].attributes, name;
                        for (var i = 0, l = attr.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            name = attr[i].name;
                            if (name.indexOf("data-") === 0) {
                                "dk.brics.tajs.directives.unreachable";
                                name = name.substr(5);
                                dataAttr(this[0], name, data[name]);
                            }
                        }
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
            var parts = key.split(".");
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
            data = elem.getAttribute("data-" + key);
            if (typeof data === "string") {
                "dk.brics.tajs.directives.unreachable";
                try {
                    "dk.brics.tajs.directives.unreachable";
                    data = data === "true" ? true : data === "false" ? false : data === "null" ? null : !jQuery.isNaN(data) ? parseFloat(data) : rbrace.test(data) ? jQuery.parseJSON(data) : data;
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
    jQuery.extend({
        queue: function(elem, type, data) {
            "dk.brics.tajs.directives.unreachable";
            if (!elem) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            type = (type || "fx") + "queue";
            var q = jQuery._data(elem, type);
            // Speed up dequeue by getting out quickly if this is just a lookup
            if (!data) {
                "dk.brics.tajs.directives.unreachable";
                return q || [];
            }
            if (!q || jQuery.isArray(data)) {
                "dk.brics.tajs.directives.unreachable";
                q = jQuery._data(elem, type, jQuery.makeArray(data));
            } else {
                "dk.brics.tajs.directives.unreachable";
                q.push(data);
            }
            return q;
        },
        dequeue: function(elem, type) {
            "dk.brics.tajs.directives.unreachable";
            type = type || "fx";
            var queue = jQuery.queue(elem, type), fn = queue.shift();
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
                fn.call(elem, function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.dequeue(elem, type);
                });
            }
            if (!queue.length) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.removeData(elem, type + "queue", true);
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
            return this.each(function(i) {
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
            return this.queue(type, function() {
                "dk.brics.tajs.directives.unreachable";
                var elem = this;
                setTimeout(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.dequeue(elem, type);
                }, time);
            });
        },
        clearQueue: function(type) {
            "dk.brics.tajs.directives.unreachable";
            return this.queue(type || "fx", []);
        }
    });
    var rclass = /[\n\t\r]/g, rspaces = " ", rreturn = /\r/g, rspecialurl = /^(?:href|src|style)$/, rtype = /^(?:button|input)$/i, rfocusable = /^(?:button|input|object|select|textarea)$/i, rclickable = /^a(?:rea)?$/i, rradiocheck = /^(?:radio|checkbox)$/i;
    jQuery.props = {
        "for": "htmlFor",
        "class": "className",
        readonly: "readOnly",
        maxlength: "maxLength",
        cellspacing: "cellSpacing",
        rowspan: "rowSpan",
        colspan: "colSpan",
        tabindex: "tabIndex",
        usemap: "useMap",
        frameborder: "frameBorder"
    };
    jQuery.fn.extend({
        attr: function(name, value) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.access(this, name, value, true, jQuery.attr);
        },
        removeAttr: function(name, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.attr(this, name, "");
                if (this.nodeType === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    this.removeAttribute(name);
                }
            });
        },
        addClass: function(value) {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    var self = jQuery(this);
                    self.addClass(value.call(this, i, self.attr("class")));
                });
            }
            if (value && typeof value === "string") {
                "dk.brics.tajs.directives.unreachable";
                var classNames = (value || "").split(rspaces);
                for (var i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = this[i];
                    if (elem.nodeType === 1) {
                        "dk.brics.tajs.directives.unreachable";
                        if (!elem.className) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.className = value;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            var className = " " + elem.className + " ", setClass = elem.className;
                            for (var c = 0, cl = classNames.length; c < cl; c++) {
                                "dk.brics.tajs.directives.unreachable";
                                if (className.indexOf(" " + classNames[c] + " ") < 0) {
                                    "dk.brics.tajs.directives.unreachable";
                                    setClass += " " + classNames[c];
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
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    var self = jQuery(this);
                    self.removeClass(value.call(this, i, self.attr("class")));
                });
            }
            if (value && typeof value === "string" || value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                var classNames = (value || "").split(rspaces);
                for (var i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = this[i];
                    if (elem.nodeType === 1 && elem.className) {
                        "dk.brics.tajs.directives.unreachable";
                        if (value) {
                            "dk.brics.tajs.directives.unreachable";
                            var className = (" " + elem.className + " ").replace(rclass, " ");
                            for (var c = 0, cl = classNames.length; c < cl; c++) {
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
                    var self = jQuery(this);
                    self.toggleClass(value.call(this, i, self.attr("class"), stateVal), stateVal);
                });
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (type === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    // toggle individual class names
                    var className, i = 0, self = jQuery(this), state = stateVal, classNames = value.split(rspaces);
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
            var className = " " + selector + " ";
            for (var i = 0, l = this.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                if ((" " + this[i].className + " ").replace(rclass, " ").indexOf(className) > -1) {
                    "dk.brics.tajs.directives.unreachable";
                    return true;
                }
            }
            return false;
        },
        val: function(value) {
            "dk.brics.tajs.directives.unreachable";
            if (!arguments.length) {
                "dk.brics.tajs.directives.unreachable";
                var elem = this[0];
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.nodeName(elem, "option")) {
                        "dk.brics.tajs.directives.unreachable";
                        // attributes.value is undefined in Blackberry 4.7 but
                        // uses .value. See #6932
                        var val = elem.attributes.value;
                        return !val || val.specified ? elem.value : elem.text;
                    }
                    // We need to handle select boxes special
                    if (jQuery.nodeName(elem, "select")) {
                        "dk.brics.tajs.directives.unreachable";
                        var index = elem.selectedIndex, values = [], options = elem.options, one = elem.type === "select-one";
                        // Nothing was selected
                        if (index < 0) {
                            "dk.brics.tajs.directives.unreachable";
                            return null;
                        }
                        // Loop through all the selected options
                        for (var i = one ? index : 0, max = one ? index + 1 : options.length; i < max; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            var option = options[i];
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
                        return values;
                    }
                    // Handle the case where in Webkit "" is returned instead of "on" if a value isn't specified
                    if (rradiocheck.test(elem.type) && !jQuery.support.checkOn) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.getAttribute("value") === null ? "on" : elem.value;
                    }
                    // Everything else, we just grab the value
                    return (elem.value || "").replace(rreturn, "");
                }
                return undefined;
            }
            var isFunction = jQuery.isFunction(value);
            return this.each(function(i) {
                "dk.brics.tajs.directives.unreachable";
                var self = jQuery(this), val = value;
                if (this.nodeType !== 1) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                if (isFunction) {
                    "dk.brics.tajs.directives.unreachable";
                    val = value.call(this, i, self.val());
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
                if (jQuery.isArray(val) && rradiocheck.test(this.type)) {
                    "dk.brics.tajs.directives.unreachable";
                    this.checked = jQuery.inArray(self.val(), val) >= 0;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.nodeName(this, "select")) {
                        "dk.brics.tajs.directives.unreachable";
                        var values = jQuery.makeArray(val);
                        jQuery("option", this).each(function() {
                            "dk.brics.tajs.directives.unreachable";
                            this.selected = jQuery.inArray(jQuery(this).val(), values) >= 0;
                        });
                        if (!values.length) {
                            "dk.brics.tajs.directives.unreachable";
                            this.selectedIndex = -1;
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        this.value = val;
                    }
                }
            });
        }
    });
    jQuery.extend({
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
            // don't get/set attributes on text, comment and attribute nodes
            if (!elem || elem.nodeType === 3 || elem.nodeType === 8 || elem.nodeType === 2) {
                "dk.brics.tajs.directives.unreachable";
                return undefined;
            }
            if (pass && name in jQuery.attrFn) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery(elem)[name](value);
            }
            var notxml = elem.nodeType !== 1 || !jQuery.isXMLDoc(elem), // Whether we are setting (or getting)
            set = value !== undefined;
            // Try to normalize/fix the name
            name = notxml && jQuery.props[name] || name;
            // Only do all the following if this is a node (faster for style)
            if (elem.nodeType === 1) {
                "dk.brics.tajs.directives.unreachable";
                // These attributes require special treatment
                var special = rspecialurl.test(name);
                // Safari mis-reports the default selected property of an option
                // Accessing the parent's selectedIndex property fixes it
                if (name === "selected" && !jQuery.support.optSelected) {
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
                }
                // If applicable, access the attribute via the DOM 0 way
                // 'in' checks fail in Blackberry 4.7 #6931
                if ((name in elem || elem[name] !== undefined) && notxml && !special) {
                    "dk.brics.tajs.directives.unreachable";
                    if (set) {
                        "dk.brics.tajs.directives.unreachable";
                        // We can't allow the type property to be changed (since it causes problems in IE)
                        if (name === "type" && rtype.test(elem.nodeName) && elem.parentNode) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.error("type property can't be changed");
                        }
                        if (value === null) {
                            "dk.brics.tajs.directives.unreachable";
                            if (elem.nodeType === 1) {
                                "dk.brics.tajs.directives.unreachable";
                                elem.removeAttribute(name);
                            }
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            elem[name] = value;
                        }
                    }
                    // browsers index elements by id/name on forms, give priority to attributes.
                    if (jQuery.nodeName(elem, "form") && elem.getAttributeNode(name)) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.getAttributeNode(name).nodeValue;
                    }
                    // elem.tabIndex doesn't always return the correct value when it hasn't been explicitly set
                    // http://fluidproject.org/blog/2008/01/09/getting-setting-and-removing-tabindex-values-with-javascript/
                    if (name === "tabIndex") {
                        "dk.brics.tajs.directives.unreachable";
                        var attributeNode = elem.getAttributeNode("tabIndex");
                        return attributeNode && attributeNode.specified ? attributeNode.value : rfocusable.test(elem.nodeName) || rclickable.test(elem.nodeName) && elem.href ? 0 : undefined;
                    }
                    return elem[name];
                }
                if (!jQuery.support.style && notxml && name === "style") {
                    "dk.brics.tajs.directives.unreachable";
                    if (set) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.style.cssText = "" + value;
                    }
                    return elem.style.cssText;
                }
                if (set) {
                    "dk.brics.tajs.directives.unreachable";
                    // convert the value to a string (all browsers do this but IE) see #1070
                    elem.setAttribute(name, "" + value);
                }
                // Ensure that missing attributes return undefined
                // Blackberry 4.7 returns "" from getAttribute #6938
                if (!elem.attributes[name] && elem.hasAttribute && !elem.hasAttribute(name)) {
                    "dk.brics.tajs.directives.unreachable";
                    return undefined;
                }
                var attr = !jQuery.support.hrefNormalized && notxml && special ? // Some attributes require a special call on IE
                elem.getAttribute(name, 2) : elem.getAttribute(name);
                // Non-existent attributes return null, we normalize to undefined
                return attr === null ? undefined : attr;
            }
            // Handle everything which isn't a DOM element node
            if (set) {
                "dk.brics.tajs.directives.unreachable";
                elem[name] = value;
            }
            return elem[name];
        }
    });
    var rnamespaces = /\.(.*)$/, rformElems = /^(?:textarea|input|select)$/i, rperiod = /\./g, rspace = / /g, rescape = /[^\w\s.|`]/g, fcleanup = function(nm) {
        "dk.brics.tajs.directives.unreachable";
        return nm.replace(rescape, "\\$&");
    }, eventKey = "events";
    /*
 * A number of helper functions used for managing events.
 * Many of the ideas behind this code originated from
 * Dean Edwards' addEvent library.
 */
    jQuery.event = {
        // Bind an event to an element
        // Original by Dean Edwards
        add: function(elem, types, handler, data) {
            "dk.brics.tajs.directives.unreachable";
            if (elem.nodeType === 3 || elem.nodeType === 8) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // For whatever reason, IE has trouble passing the window object
            // around, causing it to be cloned in the process
            if (jQuery.isWindow(elem) && elem !== window && !elem.frameElement) {
                "dk.brics.tajs.directives.unreachable";
                elem = window;
            }
            if (handler === false) {
                "dk.brics.tajs.directives.unreachable";
                handler = returnFalse;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (!handler) {
                    "dk.brics.tajs.directives.unreachable";
                    // Fixes bug #7229. Fix recommended by jdalton
                    return;
                }
            }
            var handleObjIn, handleObj;
            if (handler.handler) {
                "dk.brics.tajs.directives.unreachable";
                handleObjIn = handler;
                handler = handleObjIn.handler;
            }
            // Make sure that the function being executed has a unique ID
            if (!handler.guid) {
                "dk.brics.tajs.directives.unreachable";
                handler.guid = jQuery.guid++;
            }
            // Init the element's event structure
            var elemData = jQuery._data(elem);
            // If no elemData is found then we must be trying to bind to one of the
            // banned noData elements
            if (!elemData) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var events = elemData[eventKey], eventHandle = elemData.handle;
            if (typeof events === "function") {
                "dk.brics.tajs.directives.unreachable";
                // On plain objects events is a fn that holds the the data
                // which prevents this data from being JSON serialized
                // the function does not need to be called, it just contains the data
                eventHandle = events.handle;
                events = events.events;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (!events) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!elem.nodeType) {
                        "dk.brics.tajs.directives.unreachable";
                        // On plain objects, create a fn that acts as the holder
                        // of the values to avoid JSON serialization of event data
                        elemData[eventKey] = elemData = function() {};
                    }
                    elemData.events = events = {};
                }
            }
            if (!eventHandle) {
                "dk.brics.tajs.directives.unreachable";
                elemData.handle = eventHandle = function() {
                    "dk.brics.tajs.directives.unreachable";
                    // Handle the second event of a trigger and when
                    // an event is called after a page has unloaded
                    return typeof jQuery !== "undefined" && !jQuery.event.triggered ? jQuery.event.handle.apply(eventHandle.elem, arguments) : undefined;
                };
            }
            // Add elem as a property of the handle function
            // This is to prevent a memory leak with non-native events in IE.
            eventHandle.elem = elem;
            // Handle multiple events separated by a space
            // jQuery(...).bind("mouseover mouseout", fn);
            types = types.split(" ");
            var type, i = 0, namespaces;
            while (type = types[i++]) {
                "dk.brics.tajs.directives.unreachable";
                handleObj = handleObjIn ? jQuery.extend({}, handleObjIn) : {
                    handler: handler,
                    data: data
                };
                // Namespaced event handlers
                if (type.indexOf(".") > -1) {
                    "dk.brics.tajs.directives.unreachable";
                    namespaces = type.split(".");
                    type = namespaces.shift();
                    handleObj.namespace = namespaces.slice(0).sort().join(".");
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    namespaces = [];
                    handleObj.namespace = "";
                }
                handleObj.type = type;
                if (!handleObj.guid) {
                    "dk.brics.tajs.directives.unreachable";
                    handleObj.guid = handler.guid;
                }
                // Get the current list of functions bound to this event
                var handlers = events[type], special = jQuery.event.special[type] || {};
                // Init the event handler queue
                if (!handlers) {
                    "dk.brics.tajs.directives.unreachable";
                    handlers = events[type] = [];
                    // Check for a special event handler
                    // Only use addEventListener/attachEvent if the special
                    // events handler returns false
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
                // Add the function to the element's handler list
                handlers.push(handleObj);
                // Keep track of which events have been used, for global triggering
                jQuery.event.global[type] = true;
            }
            // Nullify elem to prevent memory leaks in IE
            elem = null;
        },
        global: {},
        // Detach an event or set of events from an element
        remove: function(elem, types, handler, pos) {
            // don't do events on text and comment nodes
            if (elem.nodeType === 3 || elem.nodeType === 8) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            if (handler === false) {
                "dk.brics.tajs.directives.unreachable";
                handler = returnFalse;
            }
            var ret, type, fn, j, i = 0, all, namespaces, namespace, special, eventType, handleObj, origType, elemData = jQuery.hasData(elem) && jQuery._data(elem), events = elemData && elemData[eventKey];
            if (!elemData || !events) {
                return;
            }
            "dk.brics.tajs.directives.unreachable";
            if (typeof events === "function") {
                "dk.brics.tajs.directives.unreachable";
                elemData = events;
                events = events.events;
            }
            // types is actually an event object here
            if (types && types.type) {
                "dk.brics.tajs.directives.unreachable";
                handler = types.handler;
                types = types.type;
            }
            // Unbind all events for the element
            if (!types || typeof types === "string" && types.charAt(0) === ".") {
                "dk.brics.tajs.directives.unreachable";
                types = types || "";
                for (type in events) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.remove(elem, type + types);
                }
                return;
            }
            // Handle multiple events separated by a space
            // jQuery(...).unbind("mouseover mouseout", fn);
            types = types.split(" ");
            while (type = types[i++]) {
                "dk.brics.tajs.directives.unreachable";
                origType = type;
                handleObj = null;
                all = type.indexOf(".") < 0;
                namespaces = [];
                if (!all) {
                    "dk.brics.tajs.directives.unreachable";
                    // Namespaced event handlers
                    namespaces = type.split(".");
                    type = namespaces.shift();
                    namespace = new RegExp("(^|\\.)" + jQuery.map(namespaces.slice(0).sort(), fcleanup).join("\\.(?:.*\\.)?") + "(\\.|$)");
                }
                eventType = events[type];
                if (!eventType) {
                    "dk.brics.tajs.directives.unreachable";
                    continue;
                }
                if (!handler) {
                    "dk.brics.tajs.directives.unreachable";
                    for (j = 0; j < eventType.length; j++) {
                        "dk.brics.tajs.directives.unreachable";
                        handleObj = eventType[j];
                        if (all || namespace.test(handleObj.namespace)) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.event.remove(elem, origType, handleObj.handler, j);
                            eventType.splice(j--, 1);
                        }
                    }
                    continue;
                }
                special = jQuery.event.special[type] || {};
                for (j = pos || 0; j < eventType.length; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    handleObj = eventType[j];
                    if (handler.guid === handleObj.guid) {
                        "dk.brics.tajs.directives.unreachable";
                        // remove the given handler for the given type
                        if (all || namespace.test(handleObj.namespace)) {
                            "dk.brics.tajs.directives.unreachable";
                            if (pos == null) {
                                "dk.brics.tajs.directives.unreachable";
                                eventType.splice(j--, 1);
                            }
                            if (special.remove) {
                                "dk.brics.tajs.directives.unreachable";
                                special.remove.call(elem, handleObj);
                            }
                        }
                        if (pos != null) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
                }
                // remove generic event handler if no more handlers exist
                if (eventType.length === 0 || pos != null && eventType.length === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!special.teardown || special.teardown.call(elem, namespaces) === false) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.removeEvent(elem, type, elemData.handle);
                    }
                    ret = null;
                    delete events[type];
                }
            }
            // Remove the expando if it's no longer used
            if (jQuery.isEmptyObject(events)) {
                "dk.brics.tajs.directives.unreachable";
                var handle = elemData.handle;
                if (handle) {
                    "dk.brics.tajs.directives.unreachable";
                    handle.elem = null;
                }
                delete elemData.events;
                delete elemData.handle;
                if (typeof elemData === "function") {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.removeData(elem, eventKey, true);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.isEmptyObject(elemData)) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.removeData(elem, undefined, true);
                    }
                }
            }
        },
        // bubbling is internal
        trigger: function(event, data, elem) {
            // Event object or event type
            var type = event.type || event, bubbling = arguments[3];
            if (!bubbling) {
                event = typeof event === "object" ? // jQuery.Event object
                event[jQuery.expando] ? event : // Object literal
                jQuery.extend(jQuery.Event(type), event) : // Just the event type (string)
                jQuery.Event(type);
                if (type.indexOf("!") >= 0) {
                    "dk.brics.tajs.directives.unreachable";
                    event.type = type = type.slice(0, -1);
                    event.exclusive = true;
                }
                // Handle a global trigger
                if (!elem) {
                    "dk.brics.tajs.directives.unreachable";
                    // Don't bubble custom events when global (to avoid too much overhead)
                    event.stopPropagation();
                    // Only trigger if we've ever bound an event for it
                    if (jQuery.event.global[type]) {
                        "dk.brics.tajs.directives.unreachable";
                        // XXX This code smells terrible. event.js should not be directly
                        // inspecting the data cache
                        jQuery.each(jQuery.cache, function() {
                            "dk.brics.tajs.directives.unreachable";
                            // internalKey variable is just used to make it easier to find
                            // and potentially change this stuff later; currently it just
                            // points to jQuery.expando
                            var internalKey = jQuery.expando, internalCache = this[internalKey];
                            if (internalCache && internalCache.events && internalCache.events[type]) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.event.trigger(event, data, internalCache.handle.elem);
                            }
                        });
                    }
                }
                // Handle triggering a single element
                // don't do events on text and comment nodes
                if (!elem || elem.nodeType === 3 || elem.nodeType === 8) {
                    "dk.brics.tajs.directives.unreachable";
                    return undefined;
                }
                // Clean up in case it is reused
                event.result = undefined;
                event.target = elem;
                // Clone the incoming data, if any
                data = jQuery.makeArray(data);
                data.unshift(event);
            }
            event.currentTarget = elem;
            // Trigger the event, it is assumed that "handle" is a function
            var handle = elem.nodeType ? jQuery._data(elem, "handle") : (jQuery._data(elem, eventKey) || {}).handle;
            if (handle) {
                "dk.brics.tajs.directives.unreachable";
                handle.apply(elem, data);
            }
            var parent = elem.parentNode || elem.ownerDocument;
            // Trigger an inline bound script
            try {
                if (!(elem && elem.nodeName && jQuery.noData[elem.nodeName.toLowerCase()])) {
                    if (elem["on" + type] && elem["on" + type].apply(elem, data) === false) {
                        "dk.brics.tajs.directives.unreachable";
                        event.result = false;
                        event.preventDefault();
                    }
                }
            } catch (inlineError) {}
            if (!event.isPropagationStopped() && parent) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger(event, data, parent, true);
            } else {
                if (!event.isDefaultPrevented()) {
                    var old, target = event.target, targetType = type, isClick = jQuery.nodeName(target, "a") && targetType === "click", special = jQuery.event.special[targetType] || {};
                    if ((!special._default || special._default.call(elem, event) === false) && !isClick && !(target && target.nodeName && jQuery.noData[target.nodeName.toLowerCase()])) {
                        try {
                            if (target[targetType]) {
                                "dk.brics.tajs.directives.unreachable";
                                // Make sure that we don't accidentally re-trigger the onFOO events
                                old = target["on" + targetType];
                                if (old) {
                                    "dk.brics.tajs.directives.unreachable";
                                    target["on" + targetType] = null;
                                }
                                jQuery.event.triggered = true;
                                target[targetType]();
                            }
                        } catch (triggerError) {}
                        if (old) {
                            "dk.brics.tajs.directives.unreachable";
                            target["on" + targetType] = old;
                        }
                        jQuery.event.triggered = false;
                    }
                }
            }
        },
        handle: function(event) {
            "dk.brics.tajs.directives.unreachable";
            var all, handlers, namespaces, namespace_re, events, namespace_sort = [], args = jQuery.makeArray(arguments);
            event = args[0] = jQuery.event.fix(event || window.event);
            event.currentTarget = this;
            // Namespaced event handlers
            all = event.type.indexOf(".") < 0 && !event.exclusive;
            if (!all) {
                "dk.brics.tajs.directives.unreachable";
                namespaces = event.type.split(".");
                event.type = namespaces.shift();
                namespace_sort = namespaces.slice(0).sort();
                namespace_re = new RegExp("(^|\\.)" + namespace_sort.join("\\.(?:.*\\.)?") + "(\\.|$)");
            }
            event.namespace = event.namespace || namespace_sort.join(".");
            events = jQuery._data(this, eventKey);
            if (typeof events === "function") {
                "dk.brics.tajs.directives.unreachable";
                events = events.events;
            }
            handlers = (events || {})[event.type];
            if (events && handlers) {
                "dk.brics.tajs.directives.unreachable";
                // Clone the handlers to prevent manipulation
                handlers = handlers.slice(0);
                for (var j = 0, l = handlers.length; j < l; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    var handleObj = handlers[j];
                    // Filter the functions by class
                    if (all || namespace_re.test(handleObj.namespace)) {
                        "dk.brics.tajs.directives.unreachable";
                        // Pass in a reference to the handler function itself
                        // So that we can later remove it
                        event.handler = handleObj.handler;
                        event.data = handleObj.data;
                        event.handleObj = handleObj;
                        var ret = handleObj.handler.apply(this, args);
                        if (ret !== undefined) {
                            "dk.brics.tajs.directives.unreachable";
                            event.result = ret;
                            if (ret === false) {
                                "dk.brics.tajs.directives.unreachable";
                                event.preventDefault();
                                event.stopPropagation();
                            }
                        }
                        if (event.isImmediatePropagationStopped()) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
                }
            }
            return event.result;
        },
        props: "altKey attrChange attrName bubbles button cancelable charCode clientX clientY ctrlKey currentTarget data detail eventPhase fromElement handler keyCode layerX layerY metaKey newValue offsetX offsetY pageX pageY prevValue relatedNode relatedTarget screenX screenY shiftKey srcElement target toElement view wheelDelta which".split(" "),
        fix: function(event) {
            "dk.brics.tajs.directives.unreachable";
            if (event[jQuery.expando]) {
                "dk.brics.tajs.directives.unreachable";
                return event;
            }
            // store a copy of the original event object
            // and "clone" to set read-only properties
            var originalEvent = event;
            event = jQuery.Event(originalEvent);
            for (var i = this.props.length, prop; i; ) {
                "dk.brics.tajs.directives.unreachable";
                prop = this.props[--i];
                event[prop] = originalEvent[prop];
            }
            // Fix target property, if necessary
            if (!event.target) {
                "dk.brics.tajs.directives.unreachable";
                // Fixes #1925 where srcElement might not be defined either
                event.target = event.srcElement || document;
            }
            // check if target is a textnode (safari)
            if (event.target.nodeType === 3) {
                "dk.brics.tajs.directives.unreachable";
                event.target = event.target.parentNode;
            }
            // Add relatedTarget, if necessary
            if (!event.relatedTarget && event.fromElement) {
                "dk.brics.tajs.directives.unreachable";
                event.relatedTarget = event.fromElement === event.target ? event.toElement : event.fromElement;
            }
            // Calculate pageX/Y if missing and clientX/Y available
            if (event.pageX == null && event.clientX != null) {
                "dk.brics.tajs.directives.unreachable";
                var doc = document.documentElement, body = document.body;
                event.pageX = event.clientX + (doc && doc.scrollLeft || body && body.scrollLeft || 0) - (doc && doc.clientLeft || body && body.clientLeft || 0);
                event.pageY = event.clientY + (doc && doc.scrollTop || body && body.scrollTop || 0) - (doc && doc.clientTop || body && body.clientTop || 0);
            }
            // Add which for key events
            if (event.which == null && (event.charCode != null || event.keyCode != null)) {
                "dk.brics.tajs.directives.unreachable";
                event.which = event.charCode != null ? event.charCode : event.keyCode;
            }
            // Add metaKey to non-Mac browsers (use ctrl for PC's and Meta for Macs)
            if (!event.metaKey && event.ctrlKey) {
                "dk.brics.tajs.directives.unreachable";
                event.metaKey = event.ctrlKey;
            }
            // Add which for click: 1 === left; 2 === middle; 3 === right
            // Note: button is not normalized, so don't use it
            if (!event.which && event.button !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                event.which = event.button & 1 ? 1 : event.button & 2 ? 3 : event.button & 4 ? 2 : 0;
            }
            return event;
        },
        // Deprecated, use jQuery.guid instead
        guid: 1e8,
        // Deprecated, use jQuery.proxy instead
        proxy: jQuery.proxy,
        special: {
            ready: {
                // Make sure the ready event is setup
                setup: jQuery.bindReady,
                teardown: jQuery.noop
            },
            live: {
                add: function(handleObj) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(this, liveConvert(handleObj.origType, handleObj.selector), jQuery.extend({}, handleObj, {
                        handler: liveHandler,
                        guid: handleObj.handler.guid
                    }));
                },
                remove: function(handleObj) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.remove(this, liveConvert(handleObj.origType, handleObj.selector), handleObj);
                }
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
        }
    };
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
    jQuery.Event = function(src) {
        // Allow instantiation without the 'new' keyword
        if (!this.preventDefault) {
            return new jQuery.Event(src);
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
        // timeStamp is buggy for some events on Firefox(#3843)
        // So we won't rely on the native value
        this.timeStamp = jQuery.now();
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
    // Checks if an event happened on an element within another element
    // Used in jQuery.event.special.mouseenter and mouseleave handlers
    var withinElement = function(event) {
        "dk.brics.tajs.directives.unreachable";
        // Check if mouse(over|out) are still within the same parent element
        var parent = event.relatedTarget;
        // Firefox sometimes assigns relatedTarget a XUL element
        // which we cannot access the parentNode property of
        try {
            "dk.brics.tajs.directives.unreachable";
            // Traverse up the tree
            while (parent && parent !== this) {
                "dk.brics.tajs.directives.unreachable";
                parent = parent.parentNode;
            }
            if (parent !== this) {
                "dk.brics.tajs.directives.unreachable";
                // set the correct event type
                event.type = event.data;
                // handle event if we actually just moused on to a non sub-element
                jQuery.event.handle.apply(this, arguments);
            }
        } catch (e) {}
    }, // In case of event delegation, we only need to rename the event.type,
    // liveHandler will take care of the rest.
    delegate = function(event) {
        "dk.brics.tajs.directives.unreachable";
        event.type = event.data;
        jQuery.event.handle.apply(this, arguments);
    };
    // Create mouseenter and mouseleave events
    jQuery.each({
        mouseenter: "mouseover",
        mouseleave: "mouseout"
    }, function(orig, fix) {
        jQuery.event.special[orig] = {
            setup: function(data) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.add(this, fix, data && data.selector ? delegate : withinElement, orig);
            },
            teardown: function(data) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, fix, data && data.selector ? delegate : withinElement);
            }
        };
    });
    // submit delegation
    if (!jQuery.support.submitBubbles) {
        "dk.brics.tajs.directives.unreachable";
        jQuery.event.special.submit = {
            setup: function(data, namespaces) {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeName && this.nodeName.toLowerCase() !== "form") {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(this, "click.specialSubmit", function(e) {
                        "dk.brics.tajs.directives.unreachable";
                        var elem = e.target, type = elem.type;
                        if ((type === "submit" || type === "image") && jQuery(elem).closest("form").length) {
                            "dk.brics.tajs.directives.unreachable";
                            e.liveFired = undefined;
                            return trigger("submit", this, arguments);
                        }
                    });
                    jQuery.event.add(this, "keypress.specialSubmit", function(e) {
                        "dk.brics.tajs.directives.unreachable";
                        var elem = e.target, type = elem.type;
                        if ((type === "text" || type === "password") && jQuery(elem).closest("form").length && e.keyCode === 13) {
                            "dk.brics.tajs.directives.unreachable";
                            e.liveFired = undefined;
                            return trigger("submit", this, arguments);
                        }
                    });
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
            },
            teardown: function(namespaces) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, ".specialSubmit");
            }
        };
    }
    // change delegation, happens here so we have bind.
    if (!jQuery.support.changeBubbles) {
        "dk.brics.tajs.directives.unreachable";
        var changeFilters, getVal = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            var type = elem.type, val = elem.value;
            if (type === "radio" || type === "checkbox") {
                "dk.brics.tajs.directives.unreachable";
                val = elem.checked;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (type === "select-multiple") {
                    "dk.brics.tajs.directives.unreachable";
                    val = elem.selectedIndex > -1 ? jQuery.map(elem.options, function(elem) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.selected;
                    }).join("-") : "";
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.nodeName.toLowerCase() === "select") {
                        "dk.brics.tajs.directives.unreachable";
                        val = elem.selectedIndex;
                    }
                }
            }
            return val;
        }, testChange = function testChange(e) {
            "dk.brics.tajs.directives.unreachable";
            var elem = e.target, data, val;
            if (!rformElems.test(elem.nodeName) || elem.readOnly) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            data = jQuery._data(elem, "_change_data");
            val = getVal(elem);
            // the current data will be also retrieved by beforeactivate
            if (e.type !== "focusout" || elem.type !== "radio") {
                "dk.brics.tajs.directives.unreachable";
                jQuery._data(elem, "_change_data", val);
            }
            if (data === undefined || val === data) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            if (data != null || val) {
                "dk.brics.tajs.directives.unreachable";
                e.type = "change";
                e.liveFired = undefined;
                return jQuery.event.trigger(e, arguments[1], elem);
            }
        };
        jQuery.event.special.change = {
            filters: {
                focusout: testChange,
                beforedeactivate: testChange,
                click: function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = e.target, type = elem.type;
                    if (type === "radio" || type === "checkbox" || elem.nodeName.toLowerCase() === "select") {
                        "dk.brics.tajs.directives.unreachable";
                        return testChange.call(this, e);
                    }
                },
                // Change has to be called before submit
                // Keydown will be called before keypress, which is used in submit-event delegation
                keydown: function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = e.target, type = elem.type;
                    if (e.keyCode === 13 && elem.nodeName.toLowerCase() !== "textarea" || e.keyCode === 32 && (type === "checkbox" || type === "radio") || type === "select-multiple") {
                        "dk.brics.tajs.directives.unreachable";
                        return testChange.call(this, e);
                    }
                },
                // Beforeactivate happens also before the previous element is blurred
                // with this event you can't trigger a change event, but you can store
                // information
                beforeactivate: function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = e.target;
                    jQuery._data(elem, "_change_data", getVal(elem));
                }
            },
            setup: function(data, namespaces) {
                "dk.brics.tajs.directives.unreachable";
                if (this.type === "file") {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
                for (var type in changeFilters) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(this, type + ".specialChange", changeFilters[type]);
                }
                return rformElems.test(this.nodeName);
            },
            teardown: function(namespaces) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, ".specialChange");
                return rformElems.test(this.nodeName);
            }
        };
        changeFilters = jQuery.event.special.change.filters;
        // Handle when the input is .focus()'d
        changeFilters.focus = changeFilters.beforeactivate;
    }
    function trigger(type, elem, args) {
        "dk.brics.tajs.directives.unreachable";
        args[0].type = type;
        return jQuery.event.handle.apply(elem, args);
    }
    // Create "bubbling" focus and blur events
    if (document.addEventListener) {
        jQuery.each({
            focus: "focusin",
            blur: "focusout"
        }, function(orig, fix) {
            jQuery.event.special[fix] = {
                setup: function() {
                    "dk.brics.tajs.directives.unreachable";
                    this.addEventListener(orig, handler, true);
                },
                teardown: function() {
                    "dk.brics.tajs.directives.unreachable";
                    this.removeEventListener(orig, handler, true);
                }
            };
            function handler(e) {
                "dk.brics.tajs.directives.unreachable";
                e = jQuery.event.fix(e);
                e.type = fix;
                return jQuery.event.handle.call(this, e);
            }
        });
    }
    jQuery.each([ "bind", "one" ], function(i, name) {
        jQuery.fn[name] = function(type, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            // Handle object literals
            if (typeof type === "object") {
                "dk.brics.tajs.directives.unreachable";
                for (var key in type) {
                    "dk.brics.tajs.directives.unreachable";
                    this[name](key, data, type[key], fn);
                }
                return this;
            }
            if (jQuery.isFunction(data) || data === false) {
                "dk.brics.tajs.directives.unreachable";
                fn = data;
                data = undefined;
            }
            var handler = name === "one" ? jQuery.proxy(fn, function(event) {
                "dk.brics.tajs.directives.unreachable";
                jQuery(this).unbind(event, handler);
                return fn.apply(this, arguments);
            }) : fn;
            if (type === "unload" && name !== "one") {
                "dk.brics.tajs.directives.unreachable";
                this.one(type, data, fn);
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(this[i], type, handler, data);
                }
            }
            return this;
        };
    });
    jQuery.fn.extend({
        unbind: function(type, fn) {
            // Handle object literals
            if (typeof type === "object" && !type.preventDefault) {
                "dk.brics.tajs.directives.unreachable";
                for (var key in type) {
                    "dk.brics.tajs.directives.unreachable";
                    this.unbind(key, type[key]);
                }
            } else {
                for (var i = 0, l = this.length; i < l; i++) {
                    jQuery.event.remove(this[i], type, fn);
                }
            }
            return this;
        },
        delegate: function(selector, types, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.live(types, data, fn, selector);
        },
        undelegate: function(selector, types, fn) {
            "dk.brics.tajs.directives.unreachable";
            if (arguments.length === 0) {
                "dk.brics.tajs.directives.unreachable";
                return this.unbind("live");
            } else {
                "dk.brics.tajs.directives.unreachable";
                return this.die(types, null, fn, selector);
            }
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
                var event = jQuery.Event(type);
                event.preventDefault();
                event.stopPropagation();
                jQuery.event.trigger(event, data, this[0]);
                return event.result;
            }
        },
        toggle: function(fn) {
            "dk.brics.tajs.directives.unreachable";
            // Save reference to arguments for access in closure
            var args = arguments, i = 1;
            // link all the functions, so any of them can unbind this click handler
            while (i < args.length) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.proxy(fn, args[i++]);
            }
            return this.click(jQuery.proxy(fn, function(event) {
                "dk.brics.tajs.directives.unreachable";
                // Figure out which function to execute
                var lastToggle = (jQuery._data(this, "lastToggle" + fn.guid) || 0) % i;
                jQuery._data(this, "lastToggle" + fn.guid, lastToggle + 1);
                // Make sure that clicks stop
                event.preventDefault();
                // and execute the function
                return args[lastToggle].apply(this, arguments) || false;
            }));
        },
        hover: function(fnOver, fnOut) {
            "dk.brics.tajs.directives.unreachable";
            return this.mouseenter(fnOver).mouseleave(fnOut || fnOver);
        }
    });
    var liveMap = {
        focus: "focusin",
        blur: "focusout",
        mouseenter: "mouseover",
        mouseleave: "mouseout"
    };
    jQuery.each([ "live", "die" ], function(i, name) {
        jQuery.fn[name] = function(types, data, fn, origSelector) {
            "dk.brics.tajs.directives.unreachable";
            var type, i = 0, match, namespaces, preType, selector = origSelector || this.selector, context = origSelector ? this : jQuery(this.context);
            if (typeof types === "object" && !types.preventDefault) {
                "dk.brics.tajs.directives.unreachable";
                for (var key in types) {
                    "dk.brics.tajs.directives.unreachable";
                    context[name](key, data, types[key], selector);
                }
                return this;
            }
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
                fn = data;
                data = undefined;
            }
            types = (types || "").split(" ");
            while ((type = types[i++]) != null) {
                "dk.brics.tajs.directives.unreachable";
                match = rnamespaces.exec(type);
                namespaces = "";
                if (match) {
                    "dk.brics.tajs.directives.unreachable";
                    namespaces = match[0];
                    type = type.replace(rnamespaces, "");
                }
                if (type === "hover") {
                    "dk.brics.tajs.directives.unreachable";
                    types.push("mouseenter" + namespaces, "mouseleave" + namespaces);
                    continue;
                }
                preType = type;
                if (type === "focus" || type === "blur") {
                    "dk.brics.tajs.directives.unreachable";
                    types.push(liveMap[type] + namespaces);
                    type = type + namespaces;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    type = (liveMap[type] || type) + namespaces;
                }
                if (name === "live") {
                    "dk.brics.tajs.directives.unreachable";
                    // bind live handler
                    for (var j = 0, l = context.length; j < l; j++) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(context[j], "live." + liveConvert(type, selector), {
                            data: data,
                            selector: selector,
                            handler: fn,
                            origType: type,
                            origHandler: fn,
                            preType: preType
                        });
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // unbind live handler
                    context.unbind("live." + liveConvert(type, selector), fn);
                }
            }
            return this;
        };
    });
    function liveHandler(event) {
        "dk.brics.tajs.directives.unreachable";
        var stop, maxLevel, related, match, handleObj, elem, j, i, l, data, close, namespace, ret, elems = [], selectors = [], events = jQuery._data(this, eventKey);
        if (typeof events === "function") {
            "dk.brics.tajs.directives.unreachable";
            events = events.events;
        }
        // Make sure we avoid non-left-click bubbling in Firefox (#3861) and disabled elements in IE (#6911)
        if (event.liveFired === this || !events || !events.live || event.target.disabled || event.button && event.type === "click") {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        if (event.namespace) {
            "dk.brics.tajs.directives.unreachable";
            namespace = new RegExp("(^|\\.)" + event.namespace.split(".").join("\\.(?:.*\\.)?") + "(\\.|$)");
        }
        event.liveFired = this;
        var live = events.live.slice(0);
        for (j = 0; j < live.length; j++) {
            "dk.brics.tajs.directives.unreachable";
            handleObj = live[j];
            if (handleObj.origType.replace(rnamespaces, "") === event.type) {
                "dk.brics.tajs.directives.unreachable";
                selectors.push(handleObj.selector);
            } else {
                "dk.brics.tajs.directives.unreachable";
                live.splice(j--, 1);
            }
        }
        match = jQuery(event.target).closest(selectors, event.currentTarget);
        for (i = 0, l = match.length; i < l; i++) {
            "dk.brics.tajs.directives.unreachable";
            close = match[i];
            for (j = 0; j < live.length; j++) {
                "dk.brics.tajs.directives.unreachable";
                handleObj = live[j];
                if (close.selector === handleObj.selector && (!namespace || namespace.test(handleObj.namespace))) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = close.elem;
                    related = null;
                    // Those two events require additional checking
                    if (handleObj.preType === "mouseenter" || handleObj.preType === "mouseleave") {
                        "dk.brics.tajs.directives.unreachable";
                        event.type = handleObj.preType;
                        related = jQuery(event.relatedTarget).closest(handleObj.selector)[0];
                    }
                    if (!related || related !== elem) {
                        "dk.brics.tajs.directives.unreachable";
                        elems.push({
                            elem: elem,
                            handleObj: handleObj,
                            level: close.level
                        });
                    }
                }
            }
        }
        for (i = 0, l = elems.length; i < l; i++) {
            "dk.brics.tajs.directives.unreachable";
            match = elems[i];
            if (maxLevel && match.level > maxLevel) {
                "dk.brics.tajs.directives.unreachable";
                break;
            }
            event.currentTarget = match.elem;
            event.data = match.handleObj.data;
            event.handleObj = match.handleObj;
            ret = match.handleObj.origHandler.apply(match.elem, arguments);
            if (ret === false || event.isPropagationStopped()) {
                "dk.brics.tajs.directives.unreachable";
                maxLevel = match.level;
                if (ret === false) {
                    "dk.brics.tajs.directives.unreachable";
                    stop = false;
                }
                if (event.isImmediatePropagationStopped()) {
                    "dk.brics.tajs.directives.unreachable";
                    break;
                }
            }
        }
        return stop;
    }
    function liveConvert(type, selector) {
        "dk.brics.tajs.directives.unreachable";
        return (type && type !== "*" ? type + "." : "") + selector.replace(rperiod, "`").replace(rspace, "&");
    }
    jQuery.each(("blur focus focusin focusout load resize scroll unload click dblclick " + "mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " + "change select submit keydown keypress keyup error").split(" "), function(i, name) {
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
    });
    /*!
 * Sizzle CSS Selector Engine
 *  Copyright 2011, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
    (function() {
        var chunker = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g, done = 0, toString = Object.prototype.toString, hasDuplicate = false, baseHasDuplicate = true;
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
                    set = posProcess(parts[0] + parts[1], context);
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
                        set = posProcess(selector, set);
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
            var set;
            if (!expr) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            for (var i = 0, l = Expr.order.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var match, type = Expr.order[i];
                if (match = Expr.leftMatch[type].exec(expr)) {
                    "dk.brics.tajs.directives.unreachable";
                    var left = match[1];
                    match.splice(1, 1);
                    if (left.substr(left.length - 1) !== "\\") {
                        "dk.brics.tajs.directives.unreachable";
                        match[1] = (match[1] || "").replace(/\\/g, "");
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
            var match, anyFound, old = expr, result = [], curLoop = set, isXMLFilter = set && set[0] && Sizzle.isXML(set[0]);
            while (expr && set.length) {
                "dk.brics.tajs.directives.unreachable";
                for (var type in Expr.filter) {
                    "dk.brics.tajs.directives.unreachable";
                    if ((match = Expr.leftMatch[type].exec(expr)) != null && match[2]) {
                        "dk.brics.tajs.directives.unreachable";
                        var found, item, filter = Expr.filter[type], left = match[1];
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
                            for (var i = 0; (item = curLoop[i]) != null; i++) {
                                "dk.brics.tajs.directives.unreachable";
                                if (item) {
                                    "dk.brics.tajs.directives.unreachable";
                                    found = filter(item, match, i, curLoop);
                                    var pass = not ^ !!found;
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
                }
            },
            relative: {
                "+": function(checkSet, part) {
                    "dk.brics.tajs.directives.unreachable";
                    var isPartStr = typeof part === "string", isTag = isPartStr && !/\W/.test(part), isPartStrNotTag = isPartStr && !isTag;
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
                    if (isPartStr && !/\W/.test(part)) {
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
                    if (typeof part === "string" && !/\W/.test(part)) {
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
                    if (typeof part === "string" && !/\W/.test(part)) {
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
                    match = " " + match[1].replace(/\\/g, "") + " ";
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
                    return match[1].replace(/\\/g, "");
                },
                TAG: function(match, curLoop) {
                    "dk.brics.tajs.directives.unreachable";
                    return match[1].toLowerCase();
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
                    var name = match[1] = match[1].replace(/\\/g, "");
                    if (!isXML && Expr.attrMap[name]) {
                        "dk.brics.tajs.directives.unreachable";
                        match[1] = Expr.attrMap[name];
                    }
                    // Handle if an un-quoted value was used
                    match[4] = (match[4] || match[5] || "").replace(/\\/g, "");
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
                    elem.parentNode.selectedIndex;
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
                    return "text" === elem.type;
                },
                radio: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "radio" === elem.type;
                },
                checkbox: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "checkbox" === elem.type;
                },
                file: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "file" === elem.type;
                },
                password: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "password" === elem.type;
                },
                submit: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "submit" === elem.type;
                },
                image: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "image" === elem.type;
                },
                reset: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "reset" === elem.type;
                },
                button: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return "button" === elem.type || elem.nodeName.toLowerCase() === "button";
                },
                input: function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return /input|select|textarea|button/i.test(elem.nodeName);
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
                            return (elem.textContent || elem.innerText || Sizzle.getText([ elem ]) || "").indexOf(match[3]) >= 0;
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
                    var type = match[1], node = elem;
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
                        var first = match[2], last = match[3];
                        if (first === 1 && last === 0) {
                            "dk.brics.tajs.directives.unreachable";
                            return true;
                        }
                        var doneName = match[0], parent = elem.parentNode;
                        if (parent && (parent.sizcache !== doneName || !elem.nodeIndex)) {
                            "dk.brics.tajs.directives.unreachable";
                            var count = 0;
                            for (node = parent.firstChild; node; node = node.nextSibling) {
                                "dk.brics.tajs.directives.unreachable";
                                if (node.nodeType === 1) {
                                    "dk.brics.tajs.directives.unreachable";
                                    node.nodeIndex = ++count;
                                }
                            }
                            parent.sizcache = doneName;
                        }
                        var diff = elem.nodeIndex - last;
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
                    return match === "*" && elem.nodeType === 1 || elem.nodeName.toLowerCase() === match;
                },
                CLASS: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return (" " + (elem.className || elem.getAttribute("class")) + " ").indexOf(match) > -1;
                },
                ATTR: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = match[1], result = Expr.attrHandle[name] ? Expr.attrHandle[name](elem) : elem[name] != null ? elem[name] : elem.getAttribute(name), value = result + "", type = match[2], check = match[4];
                    return result == null ? type === "!=" : type === "=" ? value === check : type === "*=" ? value.indexOf(check) >= 0 : type === "~=" ? (" " + value + " ").indexOf(check) >= 0 : !check ? value && result !== false : type === "!=" ? value !== check : type === "^=" ? value.indexOf(check) === 0 : type === "$=" ? value.substr(value.length - check.length) === check : type === "|=" ? value === check || value.substr(0, check.length + 1) === check + "-" : false;
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
                var al, bl, ap = [], bp = [], aup = a.parentNode, bup = b.parentNode, cur = aup;
                // The nodes are identical, we can exit early
                if (a === b) {
                    "dk.brics.tajs.directives.unreachable";
                    hasDuplicate = true;
                    return 0;
                } else {
                    "dk.brics.tajs.directives.unreachable";
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
        // Utility function for retreiving the text value of an array of DOM nodes
        Sizzle.getText = function(elems) {
            "dk.brics.tajs.directives.unreachable";
            var ret = "", elem;
            for (var i = 0; elems[i]; i++) {
                "dk.brics.tajs.directives.unreachable";
                elem = elems[i];
                // Get the text from text nodes and CDATA nodes
                if (elem.nodeType === 3 || elem.nodeType === 4) {
                    "dk.brics.tajs.directives.unreachable";
                    ret += elem.nodeValue;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.nodeType !== 8) {
                        "dk.brics.tajs.directives.unreachable";
                        ret += Sizzle.getText(elem.childNodes);
                    }
                }
            }
            return ret;
        };
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
                                var old = context.getAttribute("id"), nid = old || id, hasParent = context.parentNode, relativeHierarchySelector = /^\s*[+~]/.test(query);
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
                                        context.removeAttribute("id");
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
            var html = document.documentElement, matches = html.matchesSelector || html.mozMatchesSelector || html.webkitMatchesSelector || html.msMatchesSelector, pseudoWorks = false;
            try {
                // This should fail with an exception
                // Gecko does not error, returns false instead
                matches.call(document.documentElement, "[test!='']:sizzle");
            } catch (pseudoError) {
                pseudoWorks = true;
            }
            if (matches) {
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
                                return matches.call(node, expr);
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
                        if (elem.sizcache === doneName) {
                            "dk.brics.tajs.directives.unreachable";
                            match = checkSet[elem.sizset];
                            break;
                        }
                        if (elem.nodeType === 1 && !isXML) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.sizcache = doneName;
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
                        if (elem.sizcache === doneName) {
                            "dk.brics.tajs.directives.unreachable";
                            match = checkSet[elem.sizset];
                            break;
                        }
                        if (elem.nodeType === 1) {
                            "dk.brics.tajs.directives.unreachable";
                            if (!isXML) {
                                "dk.brics.tajs.directives.unreachable";
                                elem.sizcache = doneName;
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
        var posProcess = function(selector, context) {
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
                Sizzle(selector, root[i], tmpSet);
            }
            return Sizzle.filter(later, tmpSet);
        };
        // EXPOSE
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
            var ret = this.pushStack("", "find", selector), length = 0;
            for (var i = 0, l = this.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                length = ret.length;
                jQuery.find(selector, this[i], ret);
                if (i > 0) {
                    "dk.brics.tajs.directives.unreachable";
                    // Make sure that the results are unique
                    for (var n = length; n < ret.length; n++) {
                        "dk.brics.tajs.directives.unreachable";
                        for (var r = 0; r < length; r++) {
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
            return !!selector && jQuery.filter(selector, this).length > 0;
        },
        closest: function(selectors, context) {
            "dk.brics.tajs.directives.unreachable";
            var ret = [], i, l, cur = this[0];
            if (jQuery.isArray(selectors)) {
                "dk.brics.tajs.directives.unreachable";
                var match, selector, matches = {}, level = 1;
                if (cur && selectors.length) {
                    "dk.brics.tajs.directives.unreachable";
                    for (i = 0, l = selectors.length; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        selector = selectors[i];
                        if (!matches[selector]) {
                            "dk.brics.tajs.directives.unreachable";
                            matches[selector] = jQuery.expr.match.POS.test(selector) ? jQuery(selector, context || this.context) : selector;
                        }
                    }
                    while (cur && cur.ownerDocument && cur !== context) {
                        "dk.brics.tajs.directives.unreachable";
                        for (selector in matches) {
                            "dk.brics.tajs.directives.unreachable";
                            match = matches[selector];
                            if (match.jquery ? match.index(cur) > -1 : jQuery(cur).is(match)) {
                                "dk.brics.tajs.directives.unreachable";
                                ret.push({
                                    selector: selector,
                                    elem: cur,
                                    level: level
                                });
                            }
                        }
                        cur = cur.parentNode;
                        level++;
                    }
                }
                return ret;
            }
            var pos = POS.test(selectors) ? jQuery(selectors, context || this.context) : null;
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
                        if (!cur || !cur.ownerDocument || cur === context) {
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
            if (!elem || typeof elem === "string") {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.inArray(this[0], // If it receives a string, the selector is used
                // If it receives nothing, the siblings are used
                elem ? jQuery(elem) : this.parent().children());
            }
            // Locate the position of the desired element
            return jQuery.inArray(// If it receives a jQuery object, the first element is used
            elem.jquery ? elem[0] : elem, this);
        },
        add: function(selector, context) {
            "dk.brics.tajs.directives.unreachable";
            var set = typeof selector === "string" ? jQuery(selector, context) : jQuery.makeArray(selector), all = jQuery.merge(this.get(), set);
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
    var rinlinejQuery = / jQuery\d+="(?:\d+|null)"/g, rleadingWhitespace = /^\s+/, rxhtmlTag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi, rtagName = /<([\w:]+)/, rtbody = /<tbody/i, rhtml = /<|&#?\w+;/, rnocache = /<(?:script|object|embed|option|style)/i, // checked="checked" or checked (html5)
    rchecked = /checked\s*(?:[^=]|=\s*.checked.)/i, wrapMap = {
        option: [ 1, "<select multiple='multiple'>", "</select>" ],
        legend: [ 1, "<fieldset>", "</fieldset>" ],
        thead: [ 1, "<table>", "</table>" ],
        tr: [ 2, "<table><tbody>", "</tbody></table>" ],
        td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],
        col: [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ],
        area: [ 1, "<map>", "</map>" ],
        _default: [ 0, "", "" ]
    };
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
            dataAndEvents = dataAndEvents == null ? true : dataAndEvents;
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
                if (typeof value === "string" && !rnocache.test(value) && (jQuery.support.leadingWhitespace || !rleadingWhitespace.test(value)) && !wrapMap[(rtagName.exec(value) || [ "", "" ])[1].toLowerCase()]) {
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
                return this.pushStack(jQuery(jQuery.isFunction(value) ? value() : value), "replaceWith", value);
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
        var internalKey = jQuery.expando, oldData = jQuery.data(src), curData = jQuery.data(dest, oldData);
        // Switch to use the internal data object, if it exists, for the next
        // stage of data copying
        if (oldData = oldData[internalKey]) {
            "dk.brics.tajs.directives.unreachable";
            var events = oldData.events;
            curData = curData[internalKey] = jQuery.extend({}, oldData);
            if (events) {
                "dk.brics.tajs.directives.unreachable";
                delete curData.handle;
                curData.events = {};
                for (var type in events) {
                    "dk.brics.tajs.directives.unreachable";
                    for (var i = 0, l = events[type].length; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(dest, type, events[type][i], events[type][i].data);
                    }
                }
            }
        }
    }
    function cloneFixAttributes(src, dest) {
        "dk.brics.tajs.directives.unreachable";
        // We do not need to do anything for non-Elements
        if (dest.nodeType !== 1) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        var nodeName = dest.nodeName.toLowerCase();
        // clearAttributes removes the attributes, which we don't want,
        // but also removes the attachEvent events, which we *do* want
        dest.clearAttributes();
        // mergeAttributes, in contrast, only merges back on the
        // original attributes, not the events
        dest.mergeAttributes(src);
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
        var fragment, cacheable, cacheresults, doc = nodes && nodes[0] ? nodes[0].ownerDocument || nodes[0] : document;
        // Only cache "small" (1/2 KB) HTML strings that are associated with the main document
        // Cloning options loses the selected state, so don't cache them
        // IE 6 doesn't like it when you put <object> or <embed> elements in a fragment
        // Also, WebKit does not clone 'checked' attributes on cloneNode, so don't cache
        if (args.length === 1 && typeof args[0] === "string" && args[0].length < 512 && doc === document && args[0].charAt(0) === "<" && !rnocache.test(args[0]) && (jQuery.support.checkClone || !rchecked.test(args[0]))) {
            "dk.brics.tajs.directives.unreachable";
            cacheable = true;
            cacheresults = jQuery.fragments[args[0]];
            if (cacheresults) {
                "dk.brics.tajs.directives.unreachable";
                if (cacheresults !== 1) {
                    "dk.brics.tajs.directives.unreachable";
                    fragment = cacheresults;
                }
            }
        }
        if (!fragment) {
            "dk.brics.tajs.directives.unreachable";
            fragment = doc.createDocumentFragment();
            jQuery.clean(args, doc, fragment, scripts);
        }
        if (cacheable) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.fragments[args[0]] = cacheresults ? fragment : 1;
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
    jQuery.extend({
        clone: function(elem, dataAndEvents, deepDataAndEvents) {
            "dk.brics.tajs.directives.unreachable";
            var clone = elem.cloneNode(true), srcElements, destElements, i;
            if (!jQuery.support.noCloneEvent && (elem.nodeType === 1 || elem.nodeType === 11) && !jQuery.isXMLDoc(elem)) {
                "dk.brics.tajs.directives.unreachable";
                // IE copies events bound via attachEvent when using cloneNode.
                // Calling detachEvent on the clone will also remove the events
                // from the original. In order to get around this, we use some
                // proprietary methods to clear the events. Thanks to MooTools
                // guys for this hotness.
                // Using Sizzle here is crazy slow, so we use getElementsByTagName
                // instead
                srcElements = elem.getElementsByTagName("*");
                destElements = clone.getElementsByTagName("*");
                // Weird iteration because IE will replace the length property
                // with an element if you are cloning the body and one of the
                // elements on the page has a name or id of "length"
                for (i = 0; srcElements[i]; ++i) {
                    "dk.brics.tajs.directives.unreachable";
                    cloneFixAttributes(srcElements[i], destElements[i]);
                }
                cloneFixAttributes(elem, clone);
            }
            // Copy the events from the original to the clone
            if (dataAndEvents) {
                "dk.brics.tajs.directives.unreachable";
                cloneCopyEvent(elem, clone);
                if (deepDataAndEvents && "getElementsByTagName" in elem) {
                    "dk.brics.tajs.directives.unreachable";
                    srcElements = elem.getElementsByTagName("*");
                    destElements = clone.getElementsByTagName("*");
                    if (srcElements.length) {
                        "dk.brics.tajs.directives.unreachable";
                        for (i = 0; srcElements[i]; ++i) {
                            "dk.brics.tajs.directives.unreachable";
                            cloneCopyEvent(srcElements[i], destElements[i]);
                        }
                    }
                }
            }
            // Return the cloned set
            return clone;
        },
        clean: function(elems, context, fragment, scripts) {
            "dk.brics.tajs.directives.unreachable";
            context = context || document;
            // !context.createElement fails in IE with an error but returns typeof 'object'
            if (typeof context.createElement === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                context = context.ownerDocument || context[0] && context[0].ownerDocument || document;
            }
            var ret = [];
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
                if (typeof elem === "string" && !rhtml.test(elem)) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = context.createTextNode(elem);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof elem === "string") {
                        "dk.brics.tajs.directives.unreachable";
                        // Fix "XHTML"-style tags in all browsers
                        elem = elem.replace(rxhtmlTag, "<$1></$2>");
                        // Trim whitespace, otherwise indexOf won't work as expected
                        var tag = (rtagName.exec(elem) || [ "", "" ])[1].toLowerCase(), wrap = wrapMap[tag] || wrapMap._default, depth = wrap[0], div = context.createElement("div");
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
                            for (var j = tbody.length - 1; j >= 0; --j) {
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
                for (i = 0; ret[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (scripts && jQuery.nodeName(ret[i], "script") && (!ret[i].type || ret[i].type.toLowerCase() === "text/javascript")) {
                        "dk.brics.tajs.directives.unreachable";
                        scripts.push(ret[i].parentNode ? ret[i].parentNode.removeChild(ret[i]) : ret[i]);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (ret[i].nodeType === 1) {
                            "dk.brics.tajs.directives.unreachable";
                            ret.splice.apply(ret, [ i + 1, 0 ].concat(jQuery.makeArray(ret[i].getElementsByTagName("script"))));
                        }
                        fragment.appendChild(ret[i]);
                    }
                }
            }
            return ret;
        },
        cleanData: function(elems) {
            "dk.brics.tajs.directives.unreachable";
            var data, id, cache = jQuery.cache, internalKey = jQuery.expando, special = jQuery.event.special, deleteExpando = jQuery.support.deleteExpando;
            for (var i = 0, elem; (elem = elems[i]) != null; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (elem.nodeName && jQuery.noData[elem.nodeName.toLowerCase()]) {
                    "dk.brics.tajs.directives.unreachable";
                    continue;
                }
                id = elem[jQuery.expando];
                if (id) {
                    "dk.brics.tajs.directives.unreachable";
                    data = cache[id] && cache[id][internalKey];
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
            jQuery.globalEval(elem.text || elem.textContent || elem.innerHTML || "");
        }
        if (elem.parentNode) {
            "dk.brics.tajs.directives.unreachable";
            elem.parentNode.removeChild(elem);
        }
    }
    var ralpha = /alpha\([^)]*\)/i, ropacity = /opacity=([^)]*)/, rdashAlpha = /-([a-z])/gi, rupper = /([A-Z])/g, rnumpx = /^-?\d+(?:px)?$/i, rnum = /^-?\d/, cssShow = {
        position: "absolute",
        visibility: "hidden",
        display: "block"
    }, cssWidth = [ "Left", "Right" ], cssHeight = [ "Top", "Bottom" ], curCSS, getComputedStyle, currentStyle, fcamelCase = function(all, letter) {
        "dk.brics.tajs.directives.unreachable";
        return letter.toUpperCase();
    };
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
            zIndex: true,
            fontWeight: true,
            opacity: true,
            zoom: true,
            lineHeight: true
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
            var ret, origName = jQuery.camelCase(name), style = elem.style, hooks = jQuery.cssHooks[origName];
            name = jQuery.cssProps[origName] || origName;
            // Check if we're setting a value
            if (value !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                // Make sure that NaN and null values aren't set. See: #7116
                if (typeof value === "number" && isNaN(value) || value == null) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                // If a number was passed in, add 'px' to the (except for certain CSS properties)
                if (typeof value === "number" && !jQuery.cssNumber[origName]) {
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
            // Make sure that we're working with the right name
            var ret, origName = jQuery.camelCase(name), hooks = jQuery.cssHooks[origName];
            name = jQuery.cssProps[origName] || origName;
            // If a hook was provided get the computed value from there
            if (hooks && "get" in hooks && (ret = hooks.get(elem, true, extra)) !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                return ret;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (curCSS) {
                    "dk.brics.tajs.directives.unreachable";
                    return curCSS(elem, name, origName);
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
        },
        camelCase: function(string) {
            "dk.brics.tajs.directives.unreachable";
            return string.replace(rdashAlpha, fcamelCase);
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
                        val = getWH(elem, name, extra);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.swap(elem, cssShow, function() {
                            "dk.brics.tajs.directives.unreachable";
                            val = getWH(elem, name, extra);
                        });
                    }
                    if (val <= 0) {
                        "dk.brics.tajs.directives.unreachable";
                        val = curCSS(elem, name, name);
                        if (val === "0px" && currentStyle) {
                            "dk.brics.tajs.directives.unreachable";
                            val = currentStyle(elem, name, name);
                        }
                        if (val != null) {
                            "dk.brics.tajs.directives.unreachable";
                            // Should return "auto" instead of 0, use 0 for
                            // temporary backwards-compat
                            return val === "" || val === "auto" ? "0px" : val;
                        }
                    }
                    if (val < 0 || val == null) {
                        "dk.brics.tajs.directives.unreachable";
                        val = elem.style[name];
                        // Should return "auto" instead of 0, use 0 for
                        // temporary backwards-compat
                        return val === "" || val === "auto" ? "0px" : val;
                    }
                    return typeof val === "string" ? val : val + "px";
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
                var style = elem.style;
                // IE has trouble with opacity if it does not have layout
                // Force it by setting the zoom level
                style.zoom = 1;
                // Set the alpha filter to set the opacity
                var opacity = jQuery.isNaN(value) ? "" : "alpha(opacity=" + value * 100 + ")", filter = style.filter || "";
                style.filter = ralpha.test(filter) ? filter.replace(ralpha, opacity) : style.filter + " " + opacity;
            }
        };
    }
    if (document.defaultView && document.defaultView.getComputedStyle) {
        getComputedStyle = function(elem, newName, name) {
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
            var left, ret = elem.currentStyle && elem.currentStyle[name], rsLeft = elem.runtimeStyle && elem.runtimeStyle[name], style = elem.style;
            // From the awesome hack by Dean Edwards
            // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
            // If we're not dealing with a regular pixel number
            // but a number that has a weird ending, we need to convert it to pixels
            if (!rnumpx.test(ret) && rnum.test(ret)) {
                "dk.brics.tajs.directives.unreachable";
                // Remember the original values
                left = style.left;
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
        var which = name === "width" ? cssWidth : cssHeight, val = name === "width" ? elem.offsetWidth : elem.offsetHeight;
        if (extra === "border") {
            "dk.brics.tajs.directives.unreachable";
            return val;
        }
        jQuery.each(which, function() {
            "dk.brics.tajs.directives.unreachable";
            if (!extra) {
                "dk.brics.tajs.directives.unreachable";
                val -= parseFloat(jQuery.css(elem, "padding" + this)) || 0;
            }
            if (extra === "margin") {
                "dk.brics.tajs.directives.unreachable";
                val += parseFloat(jQuery.css(elem, "margin" + this)) || 0;
            } else {
                "dk.brics.tajs.directives.unreachable";
                val -= parseFloat(jQuery.css(elem, "border" + this + "Width")) || 0;
            }
        });
        return val;
    }
    if (jQuery.expr && jQuery.expr.filters) {
        jQuery.expr.filters.hidden = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            var width = elem.offsetWidth, height = elem.offsetHeight;
            return width === 0 && height === 0 || !jQuery.support.reliableHiddenOffsets && (elem.style.display || jQuery.css(elem, "display")) === "none";
        };
        jQuery.expr.filters.visible = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return !jQuery.expr.filters.hidden(elem);
        };
    }
    var r20 = /%20/g, rbracket = /\[\]$/, rCRLF = /\r?\n/g, rhash = /#.*$/, rheaders = /^(.*?):\s*(.*?)\r?$/gm, // IE leaves an \r character at EOL
    rinput = /^(?:color|date|datetime|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i, rnoContent = /^(?:GET|HEAD)$/, rprotocol = /^\/\//, rquery = /\?/, rscript = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, rselectTextarea = /^(?:select|textarea)/i, rspacesAjax = /\s+/, rts = /([?&])_=[^&]*/, rurl = /^(\w+:)\/\/([^\/?#:]+)(?::(\d+))?/, // Keep a copy of the old load method
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
    transports = {};
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
    //Base inspection function for prefilters and transports
    function inspectPrefiltersOrTransports(structure, options, originalOptions, jXHR, dataType, inspected) {
        "dk.brics.tajs.directives.unreachable";
        dataType = dataType || options.dataTypes[0];
        inspected = inspected || {};
        inspected[dataType] = true;
        var list = structure[dataType], i = 0, length = list ? list.length : 0, executeOnly = structure === prefilters, selection;
        for (;i < length && (executeOnly || !selection); i++) {
            "dk.brics.tajs.directives.unreachable";
            selection = list[i](options, originalOptions, jXHR);
            // If we got redirected to another dataType
            // we try there if not done already
            if (typeof selection === "string") {
                "dk.brics.tajs.directives.unreachable";
                if (inspected[selection]) {
                    "dk.brics.tajs.directives.unreachable";
                    selection = undefined;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    options.dataTypes.unshift(selection);
                    selection = inspectPrefiltersOrTransports(structure, options, originalOptions, jXHR, selection, inspected);
                }
            }
        }
        // If we're only executing or nothing was selected
        // we try the catchall dataType if not done already
        if ((executeOnly || !selection) && !inspected["*"]) {
            "dk.brics.tajs.directives.unreachable";
            selection = inspectPrefiltersOrTransports(structure, options, originalOptions, jXHR, "*", inspected);
        }
        // unnecessary when only executing (prefilters)
        // but it'll be ignored by the caller in that case
        return selection;
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
                    params = null;
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
                complete: function(jXHR, status, responseText) {
                    "dk.brics.tajs.directives.unreachable";
                    // Store the response as specified by the jXHR object
                    responseText = jXHR.responseText;
                    // If successful, inject the HTML into all the matched elements
                    if (jXHR.isResolved()) {
                        "dk.brics.tajs.directives.unreachable";
                        // #4825: Get the actual response in case
                        // a dataFilter is present in ajaxSettings
                        jXHR.done(function(r) {
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
                        self.each(callback, [ responseText, status, jXHR ]);
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
                data = null;
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
            return jQuery.get(url, null, callback, "script");
        },
        getJSON: function(url, data, callback) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.get(url, data, callback, "json");
        },
        ajaxSetup: function(settings) {
            jQuery.extend(true, jQuery.ajaxSettings, settings);
            if (settings.context) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.ajaxSettings.context = settings.context;
            }
        },
        ajaxSettings: {
            url: location.href,
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
		crossDomain: null,
		*/
            accepts: {
                xml: "application/xml, text/xml",
                html: "text/html",
                text: "text/plain",
                json: "application/json, text/javascript",
                "*": "*/*"
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
            }
        },
        ajaxPrefilter: addToPrefiltersOrTransports(prefilters),
        ajaxTransport: addToPrefiltersOrTransports(transports),
        // Main method
        ajax: function(url, options) {
            "dk.brics.tajs.directives.unreachable";
            // If options is not an object,
            // we simulate pre-1.5 signature
            if (typeof options !== "object") {
                "dk.brics.tajs.directives.unreachable";
                options = url;
                url = undefined;
            }
            // Force options to be an object
            options = options || {};
            var // Create the final options object
            s = jQuery.extend(true, {}, jQuery.ajaxSettings, options), // Callbacks contexts
            // We force the original context if it exists
            // or take it from jQuery.ajaxSettings otherwise
            // (plain objects used as context get extended)
            callbackContext = (s.context = ("context" in options ? options : jQuery.ajaxSettings).context) || s, globalEventContext = callbackContext === s ? jQuery.event : jQuery(callbackContext), // Deferreds
            deferred = jQuery.Deferred(), completeDeferred = jQuery._Deferred(), // Status-dependent callbacks
            statusCode = s.statusCode || {}, // Headers (they are sent all at once)
            requestHeaders = {}, // Response headers
            responseHeadersString, responseHeaders, // transport
            transport, // timeout handle
            timeoutTimer, // Cross-domain detection vars
            loc = document.location, protocol = loc.protocol || "http:", parts, // The jXHR state
            state = 0, // Loop variable
            i, // Fake xhr
            jXHR = {
                readyState: 0,
                // Caches the header
                setRequestHeader: function(name, value) {
                    "dk.brics.tajs.directives.unreachable";
                    if (state === 0) {
                        "dk.brics.tajs.directives.unreachable";
                        requestHeaders[name.toLowerCase()] = value;
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
                    return match || null;
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
            function done(status, statusText, responses, headers) {
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
                // (no matter how long the jXHR object will be used)
                transport = undefined;
                // Cache response headers
                responseHeadersString = headers || "";
                // Set readyState
                jXHR.readyState = status ? 4 : 0;
                var isSuccess, success, error, response = responses ? ajaxHandleResponses(s, jXHR, responses) : undefined, lastModified, etag;
                // If successful, handle type chaining
                if (status >= 200 && status < 300 || status === 304) {
                    "dk.brics.tajs.directives.unreachable";
                    // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
                    if (s.ifModified) {
                        "dk.brics.tajs.directives.unreachable";
                        if (lastModified = jXHR.getResponseHeader("Last-Modified")) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.lastModified[s.url] = lastModified;
                        }
                        if (etag = jXHR.getResponseHeader("Etag")) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.etag[s.url] = etag;
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
                    if (status) {
                        "dk.brics.tajs.directives.unreachable";
                        statusText = "error";
                        if (status < 0) {
                            "dk.brics.tajs.directives.unreachable";
                            status = 0;
                        }
                    }
                }
                // Set data for the fake xhr object
                jXHR.status = status;
                jXHR.statusText = statusText;
                // Success/Error
                if (isSuccess) {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.resolveWith(callbackContext, [ success, statusText, jXHR ]);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    deferred.rejectWith(callbackContext, [ jXHR, statusText, error ]);
                }
                // Status-dependent callbacks
                jXHR.statusCode(statusCode);
                statusCode = undefined;
                if (s.global) {
                    "dk.brics.tajs.directives.unreachable";
                    globalEventContext.trigger("ajax" + (isSuccess ? "Success" : "Error"), [ jXHR, s, isSuccess ? success : error ]);
                }
                // Complete
                completeDeferred.resolveWith(callbackContext, [ jXHR, statusText ]);
                if (s.global) {
                    "dk.brics.tajs.directives.unreachable";
                    globalEventContext.trigger("ajaxComplete", [ jXHR, s ]);
                    // Handle the global AJAX counter
                    if (!--jQuery.active) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.trigger("ajaxStop");
                    }
                }
            }
            // Attach deferreds
            deferred.promise(jXHR);
            jXHR.success = jXHR.done;
            jXHR.error = jXHR.fail;
            jXHR.complete = completeDeferred.done;
            // Status-dependent callbacks
            jXHR.statusCode = function(map) {
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
                        tmp = map[jXHR.status];
                        jXHR.then(tmp, tmp);
                    }
                }
                return this;
            };
            // Remove hash character (#7531: and string promotion)
            // Add protocol if not provided (#5866: IE7 issue with protocol-less urls)
            // We also use the url parameter if available
            s.url = ("" + (url || s.url)).replace(rhash, "").replace(rprotocol, protocol + "//");
            // Extract dataTypes list
            s.dataTypes = jQuery.trim(s.dataType || "*").toLowerCase().split(rspacesAjax);
            // Determine if a cross-domain request is in order
            if (!s.crossDomain) {
                "dk.brics.tajs.directives.unreachable";
                parts = rurl.exec(s.url.toLowerCase());
                s.crossDomain = !!(parts && (parts[1] != protocol || parts[2] != loc.hostname || (parts[3] || (parts[1] === "http:" ? 80 : 443)) != (loc.port || (protocol === "http:" ? 80 : 443))));
            }
            // Convert data if not already a string
            if (s.data && s.processData && typeof s.data !== "string") {
                "dk.brics.tajs.directives.unreachable";
                s.data = jQuery.param(s.data, s.traditional);
            }
            // Apply prefilters
            inspectPrefiltersOrTransports(prefilters, s, options, jXHR);
            // Uppercase the type
            s.type = s.type.toUpperCase();
            // Determine if request has content
            s.hasContent = !rnoContent.test(s.type);
            // Watch for a new set of requests
            if (s.global && jQuery.active++ === 0) {
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
                }
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
                requestHeaders["content-type"] = s.contentType;
            }
            // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
            if (s.ifModified) {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.lastModified[s.url]) {
                    "dk.brics.tajs.directives.unreachable";
                    requestHeaders["if-modified-since"] = jQuery.lastModified[s.url];
                }
                if (jQuery.etag[s.url]) {
                    "dk.brics.tajs.directives.unreachable";
                    requestHeaders["if-none-match"] = jQuery.etag[s.url];
                }
            }
            // Set the Accepts header for the server, depending on the dataType
            requestHeaders.accept = s.dataTypes[0] && s.accepts[s.dataTypes[0]] ? s.accepts[s.dataTypes[0]] + (s.dataTypes[0] !== "*" ? ", */*; q=0.01" : "") : s.accepts["*"];
            // Check for headers option
            for (i in s.headers) {
                "dk.brics.tajs.directives.unreachable";
                requestHeaders[i.toLowerCase()] = s.headers[i];
            }
            // Allow custom headers/mimetypes and early abort
            if (s.beforeSend && (s.beforeSend.call(callbackContext, jXHR, s) === false || state === 2)) {
                "dk.brics.tajs.directives.unreachable";
                // Abort if not done already
                done(0, "abort");
                // Return false
                jXHR = false;
            } else {
                "dk.brics.tajs.directives.unreachable";
                // Install callbacks on deferreds
                for (i in {
                    success: 1,
                    error: 1,
                    complete: 1
                }) {
                    "dk.brics.tajs.directives.unreachable";
                    jXHR[i](s[i]);
                }
                // Get transport
                transport = inspectPrefiltersOrTransports(transports, s, options, jXHR);
                // If no transport, we auto-abort
                if (!transport) {
                    "dk.brics.tajs.directives.unreachable";
                    done(-1, "No Transport");
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // Set state as sending
                    state = jXHR.readyState = 1;
                    // Send global event
                    if (s.global) {
                        "dk.brics.tajs.directives.unreachable";
                        globalEventContext.trigger("ajaxSend", [ jXHR, s ]);
                    }
                    // Timeout
                    if (s.async && s.timeout > 0) {
                        "dk.brics.tajs.directives.unreachable";
                        timeoutTimer = setTimeout(function() {
                            "dk.brics.tajs.directives.unreachable";
                            jXHR.abort("timeout");
                        }, s.timeout);
                    }
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        transport.send(requestHeaders, done);
                    } catch (e) {
                        "dk.brics.tajs.directives.unreachable";
                        // Propagate exception as error if not done
                        if (status < 2) {
                            "dk.brics.tajs.directives.unreachable";
                            done(-1, e);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.error(e);
                        }
                    }
                }
            }
            return jXHR;
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
            if (jQuery.isArray(a) || a.jquery) {
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
        if (jQuery.isArray(obj) && obj.length) {
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
                // If we see an array here, it is empty and should be treated as an empty
                // object
                if (jQuery.isArray(obj) || jQuery.isEmptyObject(obj)) {
                    "dk.brics.tajs.directives.unreachable";
                    add(prefix, "");
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.each(obj, function(k, v) {
                        "dk.brics.tajs.directives.unreachable";
                        buildParams(prefix + "[" + k + "]", v, traditional, add);
                    });
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
    function ajaxHandleResponses(s, jXHR, responses) {
        "dk.brics.tajs.directives.unreachable";
        var contents = s.contents, dataTypes = s.dataTypes, responseFields = s.responseFields, ct, type, finalDataType, firstDataType;
        // Fill responseXXX fields
        for (type in responseFields) {
            "dk.brics.tajs.directives.unreachable";
            if (type in responses) {
                "dk.brics.tajs.directives.unreachable";
                jXHR[responseFields[type]] = responses[type];
            }
        }
        // Remove auto dataType and get content-type in the process
        while (dataTypes[0] === "*") {
            "dk.brics.tajs.directives.unreachable";
            dataTypes.shift();
            if (ct === undefined) {
                "dk.brics.tajs.directives.unreachable";
                ct = jXHR.getResponseHeader("content-type");
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
        var dataTypes = s.dataTypes, converters = s.converters, i, length = dataTypes.length, tmp, // Current and previous dataTypes
        current = dataTypes[0], prev, // Conversion expression
        conversion, // Conversion function
        conv, // Conversion functions (transitive conversion)
        conv1, conv2;
        // For each dataType in the chain
        for (i = 1; i < length; i++) {
            "dk.brics.tajs.directives.unreachable";
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
    var jsc = jQuery.now(), jsre = /(\=)\?(&|$)|()\?\?()/i;
    // Default jsonp settings
    jQuery.ajaxSetup({
        jsonp: "callback",
        jsonpCallback: function() {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.expando + "_" + jsc++;
        }
    });
    // Detect, normalize options and install callbacks for jsonp requests
    jQuery.ajaxPrefilter("json jsonp", function(s, originalSettings, dataIsString) {
        "dk.brics.tajs.directives.unreachable";
        dataIsString = typeof s.data === "string";
        if (s.dataTypes[0] === "jsonp" || originalSettings.jsonpCallback || originalSettings.jsonp != null || s.jsonp !== false && (jsre.test(s.url) || dataIsString && jsre.test(s.data))) {
            "dk.brics.tajs.directives.unreachable";
            var responseContainer, jsonpCallback = s.jsonpCallback = jQuery.isFunction(s.jsonpCallback) ? s.jsonpCallback() : s.jsonpCallback, previous = window[jsonpCallback], url = s.url, data = s.data, replace = "$1" + jsonpCallback + "$2";
            if (s.jsonp !== false) {
                "dk.brics.tajs.directives.unreachable";
                url = url.replace(jsre, replace);
                if (s.url === url) {
                    "dk.brics.tajs.directives.unreachable";
                    if (dataIsString) {
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
            window[jsonpCallback] = function(response) {
                "dk.brics.tajs.directives.unreachable";
                responseContainer = [ response ];
            };
            s.complete = [ function() {
                "dk.brics.tajs.directives.unreachable";
                // Set callback back to previous value
                window[jsonpCallback] = previous;
                // Call if it was a function and we have a response
                if (previous) {
                    "dk.brics.tajs.directives.unreachable";
                    if (responseContainer && jQuery.isFunction(previous)) {
                        "dk.brics.tajs.directives.unreachable";
                        window[jsonpCallback](responseContainer[0]);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // else, more memory leak avoidance
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        delete window[jsonpCallback];
                    } catch (e) {}
                }
            }, s.complete ];
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
            script: "text/javascript, application/javascript"
        },
        contents: {
            script: /javascript/
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
            var script, head = document.getElementsByTagName("head")[0] || document.documentElement;
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
                        if (!script.readyState || /loaded|complete/.test(script.readyState)) {
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
    var // Next active xhr id
    xhrId = jQuery.now(), // active xhrs
    xhrs = {}, // #5280: see below
    xhrUnloadAbortInstalled, // XHR used to determine supports properties
    testXHR;
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
        if (window.location.protocol !== "file:") {
            "dk.brics.tajs.directives.unreachable";
            try {
                "dk.brics.tajs.directives.unreachable";
                return new window.XMLHttpRequest();
            } catch (xhrError) {}
        }
        try {
            "dk.brics.tajs.directives.unreachable";
            return new window.ActiveXObject("Microsoft.XMLHTTP");
        } catch (activeError) {}
    } : // For all other browsers, use the standard XMLHttpRequest object
    function() {
        return new window.XMLHttpRequest();
    };
    // Test if we can create an xhr object
    try {
        testXHR = jQuery.ajaxSettings.xhr();
    } catch (xhrCreationException) {}
    //Does this browser support XHR requests?
    jQuery.support.ajax = !!testXHR;
    // Does this browser support crossDomain XHR requests
    jQuery.support.cors = testXHR && "withCredentials" in testXHR;
    // No need for the temporary xhr anymore
    testXHR = undefined;
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
                        // #5280: we need to abort on unload or IE will keep connections alive
                        if (!xhrUnloadAbortInstalled) {
                            "dk.brics.tajs.directives.unreachable";
                            xhrUnloadAbortInstalled = 1;
                            jQuery(window).bind("unload", function() {
                                "dk.brics.tajs.directives.unreachable";
                                // Abort all pending requests
                                jQuery.each(xhrs, function(_, xhr) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (xhr.onreadystatechange) {
                                        "dk.brics.tajs.directives.unreachable";
                                        xhr.onreadystatechange(1);
                                    }
                                });
                            });
                        }
                        // Get a new xhr
                        var xhr = s.xhr(), handle;
                        // Open the socket
                        // Passing null username, generates a login popup on Opera (#2865)
                        if (s.username) {
                            "dk.brics.tajs.directives.unreachable";
                            xhr.open(s.type, s.url, s.async, s.username, s.password);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            xhr.open(s.type, s.url, s.async);
                        }
                        // Requested-With header
                        // Not set for crossDomain requests with no content
                        // (see why at http://trac.dojotoolkit.org/ticket/9486)
                        // Won't change header if already provided
                        if (!(s.crossDomain && !s.hasContent) && !headers["x-requested-with"]) {
                            "dk.brics.tajs.directives.unreachable";
                            headers["x-requested-with"] = "XMLHttpRequest";
                        }
                        // Need an extra try/catch for cross domain requests in Firefox 3
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.each(headers, function(key, value) {
                                "dk.brics.tajs.directives.unreachable";
                                xhr.setRequestHeader(key, value);
                            });
                        } catch (_) {}
                        // Do send the request
                        // This may raise an exception which is actually
                        // handled in jQuery.ajax (so no try/catch here)
                        xhr.send(s.hasContent && s.data || null);
                        // Listener
                        callback = function(_, isAbort) {
                            "dk.brics.tajs.directives.unreachable";
                            // Was never called and is aborted or complete
                            if (callback && (isAbort || xhr.readyState === 4)) {
                                "dk.brics.tajs.directives.unreachable";
                                // Only called once
                                callback = 0;
                                // Do not keep as active anymore
                                if (handle) {
                                    "dk.brics.tajs.directives.unreachable";
                                    xhr.onreadystatechange = jQuery.noop;
                                    delete xhrs[handle];
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
                                    // Get info
                                    var status = xhr.status, statusText, responseHeaders = xhr.getAllResponseHeaders(), responses = {}, xml = xhr.responseXML;
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
                                    // Filter status for non standard behaviours
                                    status = // Opera returns 0 when it should be 304
                                    // Webkit returns 0 for failing cross-domain no matter the real status
                                    status === 0 ? // Webkit, Firefox: filter out faulty cross-domain requests
                                    !s.crossDomain || statusText ? // Opera: filter out real aborts #6060
                                    responseHeaders ? 304 : 0 : // We assume 302 but could be anything cross-domain related
                                    302 : // IE sometimes returns 1223 when it should be 204 (see #1450)
                                    status == 1223 ? 204 : status;
                                    // Call complete
                                    complete(status, statusText, responses, responseHeaders);
                                }
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
                            // Add to list of active xhrs
                            handle = xhrId++;
                            xhrs[handle] = xhr;
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
    var elemdisplay = {}, rfxtypes = /^(?:toggle|show|hide)$/, rfxnum = /^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i, timerId, fxAttrs = [ // height animations
    [ "height", "marginTop", "marginBottom", "paddingTop", "paddingBottom" ], // width animations
    [ "width", "marginLeft", "marginRight", "paddingLeft", "paddingRight" ], // opacity animations
    [ "opacity" ] ];
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
                // Set the display of most of the elements in a second loop
                // to avoid the constant reflow
                for (i = 0; i < j; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = this[i];
                    display = elem.style.display;
                    if (display === "" || display === "none") {
                        "dk.brics.tajs.directives.unreachable";
                        elem.style.display = jQuery._data(elem, "olddisplay") || "";
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
                for (var i = 0, j = this.length; i < j; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var display = jQuery.css(this[i], "display");
                    if (display !== "none" && !jQuery._data(this[i], "olddisplay")) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery._data(this[i], "olddisplay", display);
                    }
                }
                // Set the display of the elements in a second loop
                // to avoid the constant reflow
                for (i = 0; i < j; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    this[i].style.display = "none";
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
                return this.each(optall.complete);
            }
            var f = function() {
                "dk.brics.tajs.directives.unreachable";
                // XXX 'this' does not always have a nodeName when running the
                // test suite
                var opt = jQuery.extend({}, optall), p, isElement = this.nodeType === 1, hidden = isElement && jQuery(this).is(":hidden"), self = this;
                for (p in prop) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = jQuery.camelCase(p);
                    if (p !== name) {
                        "dk.brics.tajs.directives.unreachable";
                        prop[name] = prop[p];
                        delete prop[p];
                        p = name;
                    }
                    if (prop[p] === "hide" && hidden || prop[p] === "show" && !hidden) {
                        "dk.brics.tajs.directives.unreachable";
                        return opt.complete.call(this);
                    }
                    if (isElement && (p === "height" || p === "width")) {
                        "dk.brics.tajs.directives.unreachable";
                        // Make sure that nothing sneaks out
                        // Record all 3 overflow attributes because IE does not
                        // change the overflow attribute when overflowX and
                        // overflowY are set to the same value
                        opt.overflow = [ this.style.overflow, this.style.overflowX, this.style.overflowY ];
                        // Set display property to inline-block for height/width
                        // animations on inline elements that are having width/height
                        // animated
                        if (jQuery.css(this, "display") === "inline" && jQuery.css(this, "float") === "none") {
                            "dk.brics.tajs.directives.unreachable";
                            if (!jQuery.support.inlineBlockNeedsLayout) {
                                "dk.brics.tajs.directives.unreachable";
                                this.style.display = "inline-block";
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                var display = defaultDisplay(this.nodeName);
                                // inline-level elements accept inline-block;
                                // block-level elements need to be inline with layout
                                if (display === "inline") {
                                    "dk.brics.tajs.directives.unreachable";
                                    this.style.display = "inline-block";
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    this.style.display = "inline";
                                    this.style.zoom = 1;
                                }
                            }
                        }
                    }
                    if (jQuery.isArray(prop[p])) {
                        "dk.brics.tajs.directives.unreachable";
                        // Create (if needed) and add to specialEasing
                        (opt.specialEasing = opt.specialEasing || {})[p] = prop[p][1];
                        prop[p] = prop[p][0];
                    }
                }
                if (opt.overflow != null) {
                    "dk.brics.tajs.directives.unreachable";
                    this.style.overflow = "hidden";
                }
                opt.curAnim = jQuery.extend({}, prop);
                jQuery.each(prop, function(name, val) {
                    "dk.brics.tajs.directives.unreachable";
                    var e = new jQuery.fx(self, opt, name);
                    if (rfxtypes.test(val)) {
                        "dk.brics.tajs.directives.unreachable";
                        if(val === "toggle"){
                            if(hidden){
                                e.show();
                            }else{
                                e.hide();
                            }
                        }else{
                            e[val]();
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        var parts = rfxnum.exec(val), start = e.cur() || 0;
                        if (parts) {
                            "dk.brics.tajs.directives.unreachable";
                            var end = parseFloat(parts[2]), unit = parts[3] || "px";
                            // We need to compute starting value
                            if (unit !== "px") {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.style(self, name, (end || 1) + unit);
                                start = (end || 1) / e.cur() * start;
                                jQuery.style(self, name, start + unit);
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
                });
                // For JS strict compliance
                return true;
            }
            if(optall.queue === false){
                return this.each(f);
            }else{
                return this.queue(f);
            } 
        },
        stop: function(clearQueue, gotoEnd) {
            "dk.brics.tajs.directives.unreachable";
            var timers = jQuery.timers;
            if (clearQueue) {
                "dk.brics.tajs.directives.unreachable";
                this.queue([]);
            }
            this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                // go in reverse order so anything added to the queue during the loop is ignored
                for (var i = timers.length - 1; i >= 0; i--) {
                    "dk.brics.tajs.directives.unreachable";
                    if (timers[i].elem === this) {
                        "dk.brics.tajs.directives.unreachable";
                        if (gotoEnd) {
                            "dk.brics.tajs.directives.unreachable";
                            // force the next step to be the last
                            timers[i](true);
                        }
                        timers.splice(i, 1);
                    }
                }
            });
            // start the next in the queue if the last step wasn't forced
            if (!gotoEnd) {
                "dk.brics.tajs.directives.unreachable";
                this.dequeue();
            }
            return this;
        }
    });
    function genFx(type, num) {
        var obj = {};
        for(var i = 0; i < num; i++){
            jQuery.each(fxAttrs[i], function() {
                obj[this] = type;
            });
        }
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
            // Queueing
            opt.old = opt.complete;
            opt.complete = function() {
                "dk.brics.tajs.directives.unreachable";
                if (opt.queue !== false) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).dequeue();
                }
                if (jQuery.isFunction(opt.old)) {
                    "dk.brics.tajs.directives.unreachable";
                    opt.old.call(this);
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
            if (!options.orig) {
                "dk.brics.tajs.directives.unreachable";
                options.orig = {};
            }
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
            var r = parseFloat(jQuery.css(this.elem, this.prop));
            return r || 0;
        },
        // Start an animation from one number to another
        custom: function(from, to, unit) {
            "dk.brics.tajs.directives.unreachable";
            var self = this, fx = jQuery.fx;
            this.startTime = jQuery.now();
            this.start = from;
            this.end = to;
            this.unit = unit || this.unit || "px";
            this.now = this.start;
            this.pos = this.state = 0;
            function t(gotoEnd) {
                "dk.brics.tajs.directives.unreachable";
                return self.step(gotoEnd);
            }
            t.elem = this.elem;
            if (t() && jQuery.timers.push(t) && !timerId) {
                "dk.brics.tajs.directives.unreachable";
                timerId = setInterval(fx.tick, fx.interval);
            }
        },
        // Simple 'show' function
        show: function() {
            "dk.brics.tajs.directives.unreachable";
            // Remember where we started, so that we can go back to it later
            this.options.orig[this.prop] = jQuery.style(this.elem, this.prop);
            this.options.show = true;
            // Begin the animation
            // Make sure that we start at a small width/height to avoid any
            // flash of content
            this.custom(this.prop === "width" || this.prop === "height" ? 1 : 0, this.cur());
            // Start by showing the element
            jQuery(this.elem).show();
        },
        // Simple 'hide' function
        hide: function() {
            "dk.brics.tajs.directives.unreachable";
            // Remember where we started, so that we can go back to it later
            this.options.orig[this.prop] = jQuery.style(this.elem, this.prop);
            this.options.hide = true;
            // Begin the animation
            this.custom(this.cur(), 0);
        },
        // Each step of an animation
        step: function(gotoEnd) {
            "dk.brics.tajs.directives.unreachable";
            var t = jQuery.now(), done = true;
            if (gotoEnd || t >= this.options.duration + this.startTime) {
                "dk.brics.tajs.directives.unreachable";
                this.now = this.end;
                this.pos = this.state = 1;
                this.update();
                this.options.curAnim[this.prop] = true;
                for (var i in this.options.curAnim) {
                    "dk.brics.tajs.directives.unreachable";
                    if (this.options.curAnim[i] !== true) {
                        "dk.brics.tajs.directives.unreachable";
                        done = false;
                    }
                }
                if (done) {
                    "dk.brics.tajs.directives.unreachable";
                    // Reset the overflow
                    if (this.options.overflow != null && !jQuery.support.shrinkWrapBlocks) {
                        "dk.brics.tajs.directives.unreachable";
                        var elem = this.elem, options = this.options;
                        jQuery.each([ "", "X", "Y" ], function(index, value) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.style["overflow" + value] = options.overflow[index];
                        });
                    }
                    // Hide the element if the "hide" operation was done
                    if (this.options.hide) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery(this.elem).hide();
                    }
                    // Reset the properties, if the item has been hidden or shown
                    if (this.options.hide || this.options.show) {
                        "dk.brics.tajs.directives.unreachable";
                        for (var p in this.options.curAnim) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.style(this.elem, p, this.options.orig[p]);
                        }
                    }
                    // Execute the complete function
                    this.options.complete.call(this.elem);
                }
                return false;
            } else {
                "dk.brics.tajs.directives.unreachable";
                var n = t - this.startTime;
                this.state = n / this.options.duration;
                // Perform the easing function, defaults to swing
                var specialEasing = this.options.specialEasing && this.options.specialEasing[this.prop];
                var defaultEasing = this.options.easing || (jQuery.easing.swing ? "swing" : "linear");
                this.pos = jQuery.easing[specialEasing || defaultEasing](this.state, n, 0, 1, this.options.duration);
                this.now = this.start + (this.end - this.start) * this.pos;
                // Perform the next step of the animation
                this.update();
            }
            return true;
        }
    };
    jQuery.extend(jQuery.fx, {
        tick: function() {
            "dk.brics.tajs.directives.unreachable";
            var timers = jQuery.timers;
            for (var i = 0; i < timers.length; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (!timers[i]()) {
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
                    fx.elem.style[fx.prop] = (fx.prop === "width" || fx.prop === "height" ? Math.max(0, fx.now) : fx.now) + fx.unit;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    fx.elem[fx.prop] = fx.now;
                }
            }
        }
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
    function defaultDisplay(nodeName) {
        "dk.brics.tajs.directives.unreachable";
        if (!elemdisplay[nodeName]) {
            "dk.brics.tajs.directives.unreachable";
            var elem = jQuery("<" + nodeName + ">").appendTo("body"), display = elem.css("display");
            elem.remove();
            if (display === "none" || display === "") {
                "dk.brics.tajs.directives.unreachable";
                display = "block";
            }
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
            jQuery.offset.initialize();
            var computedStyle, offsetParent = elem.offsetParent, prevOffsetParent = elem, doc = elem.ownerDocument, docElem = doc.documentElement, body = doc.body, defaultView = doc.defaultView, prevComputedStyle = defaultView ? defaultView.getComputedStyle(elem, null) : elem.currentStyle, top = elem.offsetTop, left = elem.offsetLeft;
            while ((elem = elem.parentNode) && elem !== body && elem !== docElem) {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.offset.supportsFixedPosition && prevComputedStyle.position === "fixed") {
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
                    if (jQuery.offset.doesNotAddBorder && !(jQuery.offset.doesAddBorderForTableAndCells && rtable.test(elem.nodeName))) {
                        "dk.brics.tajs.directives.unreachable";
                        top += parseFloat(computedStyle.borderTopWidth) || 0;
                        left += parseFloat(computedStyle.borderLeftWidth) || 0;
                    }
                    prevOffsetParent = offsetParent;
                    offsetParent = elem.offsetParent;
                }
                if (jQuery.offset.subtractsBorderForOverflowNotVisible && computedStyle.overflow !== "visible") {
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
            if (jQuery.offset.supportsFixedPosition && prevComputedStyle.position === "fixed") {
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
        initialize: function() {
            "dk.brics.tajs.directives.unreachable";
            var body = document.body, container = document.createElement("div"), innerDiv, checkDiv, table, td, bodyMarginTop = parseFloat(jQuery.css(body, "marginTop")) || 0, html = "<div style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;'><div></div></div><table style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;' cellpadding='0' cellspacing='0'><tr><td></td></tr></table>";
            jQuery.extend(container.style, {
                position: "absolute",
                top: 0,
                left: 0,
                margin: 0,
                border: 0,
                width: "1px",
                height: "1px",
                visibility: "hidden"
            });
            container.innerHTML = html;
            body.insertBefore(container, body.firstChild);
            innerDiv = container.firstChild;
            checkDiv = innerDiv.firstChild;
            td = innerDiv.nextSibling.firstChild.firstChild;
            this.doesNotAddBorder = checkDiv.offsetTop !== 5;
            this.doesAddBorderForTableAndCells = td.offsetTop === 5;
            checkDiv.style.position = "fixed";
            checkDiv.style.top = "20px";
            // safari subtracts parent border width here which is 5px
            this.supportsFixedPosition = checkDiv.offsetTop === 20 || checkDiv.offsetTop === 15;
            checkDiv.style.position = checkDiv.style.top = "";
            innerDiv.style.overflow = "hidden";
            innerDiv.style.position = "relative";
            this.subtractsBorderForOverflowNotVisible = checkDiv.offsetTop === -5;
            this.doesNotIncludeMarginInBodyOffset = body.offsetTop !== bodyMarginTop;
            body.removeChild(container);
            body = container = innerDiv = checkDiv = table = td = null;
            jQuery.offset.initialize = jQuery.noop;
        },
        bodyOffset: function(body) {
            "dk.brics.tajs.directives.unreachable";
            var top = body.offsetTop, left = body.offsetLeft;
            jQuery.offset.initialize();
            if (jQuery.offset.doesNotIncludeMarginInBodyOffset) {
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
            var curElem = jQuery(elem), curOffset = curElem.offset(), curCSSTop = jQuery.css(elem, "top"), curCSSLeft = jQuery.css(elem, "left"), calculatePosition = position === "absolute" && jQuery.inArray("auto", [ curCSSTop, curCSSLeft ]) > -1, props = {}, curPosition = {}, curTop, curLeft;
            // need to be able to calculate position if either top or left is auto and position is absolute
            if (calculatePosition) {
                "dk.brics.tajs.directives.unreachable";
                curPosition = curElem.position();
            }
            curTop = calculatePosition ? curPosition.top : parseInt(curCSSTop, 10) || 0;
            curLeft = calculatePosition ? curPosition.left : parseInt(curCSSLeft, 10) || 0;
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
            var elem = this[0], win;
            if (!elem) {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            if (val !== undefined) {
                "dk.brics.tajs.directives.unreachable";
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
            } else {
                "dk.brics.tajs.directives.unreachable";
                win = getWindow(elem);
                // Return the scroll offset
                return win ? "pageXOffset" in win ? win[i ? "pageYOffset" : "pageXOffset"] : jQuery.support.boxModel && win.document.documentElement[method] || win.document.body[method] : elem[method];
            }
        };
    });
    function getWindow(elem) {
        "dk.brics.tajs.directives.unreachable";
        return jQuery.isWindow(elem) ? elem : elem.nodeType === 9 ? elem.defaultView || elem.parentWindow : false;
    }
    // Create innerHeight, innerWidth, outerHeight and outerWidth methods
    jQuery.each([ "Height", "Width" ], function(i, name) {
        var type = name.toLowerCase();
        // innerHeight and innerWidth
        jQuery.fn["inner" + name] = function() {
            "dk.brics.tajs.directives.unreachable";
            return this[0] ? parseFloat(jQuery.css(this[0], type, "padding")) : null;
        };
        // outerHeight and outerWidth
        jQuery.fn["outer" + name] = function(margin) {
            "dk.brics.tajs.directives.unreachable";
            return this[0] ? parseFloat(jQuery.css(this[0], type, margin ? "margin" : "border")) : null;
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
                var docElemProp = elem.document.documentElement["client" + name];
                return elem.document.compatMode === "CSS1Compat" && docElemProp || elem.document.body["client" + name] || docElemProp;
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
                        return jQuery.isNaN(ret) ? orig : ret;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        return this.css(type, typeof size === "string" ? size : size + "px");
                    }
                }
            }
        };
    });
})(window);
