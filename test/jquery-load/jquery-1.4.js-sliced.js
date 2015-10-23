/*!
 * jQuery JavaScript Library v1.4
 * http://jquery.com/
 *
 * Copyright 2010, John Resig
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://docs.jquery.com/License
 *
 * Includes Sizzle.js
 * http://sizzlejs.com/
 * Copyright 2010, The Dojo Foundation
 * Released under the MIT, BSD, and GPL Licenses.
 *
 * Date: Wed Jan 13 15:23:05 2010 -0500
 */
(function(window, undefined) {
    // Define a local copy of jQuery
    var jQuery = function(selector, context) {
        // The jQuery object is actually just the init constructor 'enhanced'
        return new jQuery.fn.init(selector, context);
    }, // Map over jQuery in case of overwrite
    _jQuery = window.jQuery, // Map over the $ in case of overwrite
    _$ = window.$, // Use the correct document accordingly with window argument (sandbox)
    document = window.document, // A central reference to the root jQuery(document)
    rootjQuery, // A simple way to check for HTML strings or ID strings
    // (both of which we optimize for)
    quickExpr = /^[^<]*(<[\w\W]+>)[^>]*$|^#([\w-]+)$/, // Is it a simple selector
    isSimple = /^.[^:#\[\.,]*$/, // Check if a string has a non-whitespace character in it
    rnotwhite = /\S/, // Used for trimming whitespace
    rtrim = /^(\s|\u00A0)+|(\s|\u00A0)+$/g, // Match a standalone tag
    rsingleTag = /^<(\w+)\s*\/?>(?:<\/\1>)?$/, // Keep a UserAgent string for use with jQuery.browser
    userAgent = navigator.userAgent, // For matching the engine and version of the browser
    browserMatch, // Has the ready events already been bound?
    readyBound = false, // The functions to execute on DOM ready
    readyList = [], // The ready event handler
    DOMContentLoaded, // Save a reference to some core methods
    toString = Object.prototype.toString, hasOwnProperty = Object.prototype.hasOwnProperty, push = Array.prototype.push, slice = Array.prototype.slice, indexOf = Array.prototype.indexOf;
    jQuery.fn = jQuery.prototype = {
        init: function(selector, context) {
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
                            ret = buildFragment([ match[1] ], [ doc ]);
                            selector = (ret.cacheable ? ret.fragment.cloneNode(true) : ret.fragment).childNodes;
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        elem = document.getElementById(match[2]);
                        if (elem) {
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
                    if (!context && /^\w+$/.test(selector)) {
                        "dk.brics.tajs.directives.unreachable";
                        this.selector = selector;
                        this.context = document;
                        selector = document.getElementsByTagName(selector);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (!context || context.jquery) {
                            "dk.brics.tajs.directives.unreachable";
                            return (context || rootjQuery).find(selector);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            return jQuery(context).find(selector);
                        }
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
            return jQuery.isArray(selector) ? this.setArray(selector) : jQuery.makeArray(selector, this);
        },
        // Start with an empty selector
        selector: "",
        // The current version of jQuery being used
        jquery: "1.4",
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
            num < 0 ? this.slice(num)[0] : this[num];
        },
        // Take an array of elements and push it onto the stack
        // (returning the new matched element set)
        pushStack: function(elems, name, selector) {
            "dk.brics.tajs.directives.unreachable";
            // Build a new jQuery matched element set
            var ret = jQuery(elems || null);
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
        // Force the current matched set of elements to become
        // the specified array of elements (destroying the stack in the process)
        // You should use pushStack() in order to do this, but maintain the stack
        setArray: function(elems) {
            "dk.brics.tajs.directives.unreachable";
            // Resetting the length to 0, then using the native Array push
            // is a super-fast way to populate an object with array-like properties
            this.length = 0;
            push.apply(this, elems);
            return this;
        },
        // Execute a callback for every element in the matched set.
        // (You can seed the arguments with an array of args, but this is
        // only used internally.)
        each: function(callback, args) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.each(this, callback, args);
        },
        ready: function(fn) {
            // Attach the listeners
            jQuery.bindReady();
            // If the DOM is already ready
            if (jQuery.isReady) {
                "dk.brics.tajs.directives.unreachable";
                // Execute the function immediately
                fn.call(document, jQuery);
            } else {
                if (readyList) {
                    // Add the function to the wait list
                    readyList.push(fn);
                }
            }
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
            return this.prevObject || jQuery(null);
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
        // copy reference to target object
        var target = arguments[0] || {}, i = 1, length = arguments.length, deep = false, options, name, src, copy;
        // Handle a deep copy situation
        if (typeof target === "boolean") {
            "dk.brics.tajs.directives.unreachable";
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
                    // Recurse if we're merging object literal values or arrays
                    if (deep && copy && (jQuery.isPlainObject(copy) || jQuery.isArray(copy))) {
                        "dk.brics.tajs.directives.unreachable";
                        var clone = src && (jQuery.isPlainObject(src) || jQuery.isArray(src)) ? src : jQuery.isArray(copy) ? [] : {};
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
        // Handle when the DOM is ready
        ready: function() {
            // Make sure that the DOM is not already loaded
            if (!jQuery.isReady) {
                // Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
                if (!document.body) {
                    "dk.brics.tajs.directives.unreachable";
                    return setTimeout(jQuery.ready, 13);
                }
                // Remember that the DOM is ready
                jQuery.isReady = true;
                // If there are functions bound, to execute
                if (readyList) {
                    // Execute all of them
                    var fn, i = 0;
                    while (fn = readyList[i++]) {
                        fn.call(document, jQuery);
                    }
                    // Reset the list of functions
                    readyList = null;
                }
                // Trigger any bound ready events
                if (jQuery.fn.triggerHandler) {
                    jQuery(document).triggerHandler("ready");
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
                return jQuery.ready();
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
            return toString.call(obj) === "[object Function]";
        },
        isArray: function(obj) {
            "dk.brics.tajs.directives.unreachable";
            return toString.call(obj) === "[object Array]";
        },
        isPlainObject: function(obj) {
            "dk.brics.tajs.directives.unreachable";
            // Must be an Object.
            // Because of IE, we also have to check the presence of the constructor property.
            // Make sure that DOM nodes and window objects don't pass through, as well
            if (!obj || toString.call(obj) !== "[object Object]" || obj.nodeType || obj.setInterval) {
                "dk.brics.tajs.directives.unreachable";
                return false;
            }
            // Not own constructor property must be Object
            if (obj.constructor && !hasOwnProperty.call(obj, "constructor") && !hasOwnProperty.call(obj.constructor.prototype, "isPrototypeOf")) {
                "dk.brics.tajs.directives.unreachable";
                return false;
            }
            // Own properties are enumerated firstly, so to speed up,
            // if last one is own, then all properties are own.
            var key;
            for (key in obj) {}
            return key === undefined || hasOwnProperty.call(obj, key);
        },
        isEmptyObject: function(obj) {
            "dk.brics.tajs.directives.unreachable";
            for (var name in obj) {
                "dk.brics.tajs.directives.unreachable";
                return false;
            }
            return true;
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
                if (jQuery.support.scriptEval) {
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
        trim: function(text) {
            "dk.brics.tajs.directives.unreachable";
            return (text || "").replace(rtrim, "");
        },
        // results is for internal usage only
        makeArray: function(array, results) {
            var ret = results || [];
            if (array != null) {
                "dk.brics.tajs.directives.unreachable";
                // The window, strings (and functions) also have 'length'
                // The extra typeof function check is to prevent crashes
                // in Safari 2 (See: #3039)
                if (array.length == null || typeof array === "string" || jQuery.isFunction(array) || typeof array !== "function" && array.setInterval) {
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
            var ret = [];
            // Go through the array, only saving the items
            // that pass the validator function
            for (var i = 0, length = elems.length; i < length; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (!inv !== !callback(elems[i], i)) {
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
        // Use of jQuery.browser is frowned upon.
        // More details: http://docs.jquery.com/Utilities/jQuery.browser
        uaMatch: function(ua) {
            var ret = {
                browser: ""
            };
            ua = ua.toLowerCase();
            if (/webkit/.test(ua)) {
                ret = {
                    browser: "webkit",
                    version: /webkit[\/ ]([\w.]+)/
                };
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (/opera/.test(ua)) {
                    "dk.brics.tajs.directives.unreachable";
                    ret = {
                        browser: "opera",
                        version: /version/.test(ua) ? /version[\/ ]([\w.]+)/ : /opera[\/ ]([\w.]+)/
                    };
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (/msie/.test(ua)) {
                        "dk.brics.tajs.directives.unreachable";
                        ret = {
                            browser: "msie",
                            version: /msie ([\w.]+)/
                        };
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (/mozilla/.test(ua) && !/compatible/.test(ua)) {
                            "dk.brics.tajs.directives.unreachable";
                            ret = {
                                browser: "mozilla",
                                version: /rv:([\w.]+)/
                            };
                        }
                    }
                }
            }
            ret.version = (ret.version && ret.version.exec(ua) || [ 0, "0" ])[1];
            return ret;
        },
        browser: {}
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
        } catch (error) {
            "dk.brics.tajs.directives.unreachable";
            setTimeout(doScrollCheck, 1);
            return;
        }
        // and execute any waiting functions
        jQuery.ready();
    }
    if (indexOf) {
        jQuery.inArray = function(elem, array) {
            "dk.brics.tajs.directives.unreachable";
            return indexOf.call(array, elem);
        };
    }
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
    // Mutifunctional method to get and set values to a collection
    // The value/s can be optionally by executed if its a function
    function access(elems, key, value, exec, fn, pass) {
        "dk.brics.tajs.directives.unreachable";
        var length = elems.length;
        // Setting many attributes
        if (typeof key === "object") {
            "dk.brics.tajs.directives.unreachable";
            for (var k in key) {
                "dk.brics.tajs.directives.unreachable";
                access(elems, k, key[k], exec, fn, value);
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
        return length ? fn(elems[0], key) : null;
    }
    function now() {
        return new Date().getTime();
    }
    (function() {
        jQuery.support = {};
        var root = document.documentElement, script = document.createElement("script"), div = document.createElement("div"), id = "script" + "TAJS_NOW";
        div.style.display = "none";
        div.innerHTML = "   <link/><table></table><a href='/a' style='color:red;float:left;opacity:.55;'>a</a><input type='checkbox'/>";
        var all = div.getElementsByTagName("*"), a = div.getElementsByTagName("a")[0];
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
            optSelected: document.createElement("select").appendChild(document.createElement("option")).selected,
            // Will be defined later
            scriptEval: false,
            noCloneEvent: true,
            boxModel: null
        };
        script.type = "text/javascript";
        try {
            script.appendChild(document.createTextNode("window." + id + "=1;"));
        } catch (e) {}
        root.insertBefore(script, root.firstChild);
        // Make sure that the execution of code works by injecting a script
        // tag with appendChild/createTextNode
        // (IE doesn't support this, fails, and uses .text instead)
        if (window[id]) {
            jQuery.support.scriptEval = true;
            delete window[id];
        }
        root.removeChild(script);
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
        // Figure out if the W3C box model works as expected
        // document.body must exist before we can do this
        // TODO: This timeout is temporary until I move ready into core.js.
        jQuery(function() {
            var div = document.createElement("div");
            div.style.width = div.style.paddingLeft = "1px";
            document.body.appendChild(div);
            jQuery.boxModel = jQuery.support.boxModel = div.offsetWidth === 2;
            document.body.removeChild(div).style.display = "none";
            div = null;
        });
        // Technique from Juriy Zaytsev
        // http://thinkweb2.com/projects/prototype/detecting-event-support-without-browser-sniffing/
        var eventSupported = function(eventName) {
            var el = document.createElement("div");
            eventName = "on" + eventName;
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
        root = script = div = all = a = null;
    })();
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
    var expando = "jQuery" + "TAJS_NOW", uuid = 0, windowData = {};
    var emptyObject = {};
    jQuery.extend({
        cache: {},
        expando: expando,
        // The following elements throw uncatchable exceptions if you
        // attempt to add expando properties to them.
        noData: {
            embed: true,
            object: true,
            applet: true
        },
        data: function(elem, name, data) {
            if (elem.nodeName && jQuery.noData[elem.nodeName.toLowerCase()]) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            elem = elem == window ? windowData : elem;
            var id = elem[expando], cache = jQuery.cache, thisCache;
            // Handle the case where there's no name immediately
            if (!name && !id) {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            // Compute a unique ID for the element
            if (!id) {
                id = ++uuid;
            }
            // Avoid generating a new cache unless none exists and we
            // want to manipulate it.
            if (typeof name === "object") {
                "dk.brics.tajs.directives.unreachable";
                elem[expando] = id;
                thisCache = cache[id] = jQuery.extend(true, {}, name);
            } else {
                if (cache[id]) {
                    "dk.brics.tajs.directives.unreachable";
                    thisCache = cache[id];
                } else {
                    if (typeof data === "undefined") {
                        thisCache = emptyObject;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        thisCache = cache[id] = {};
                    }
                }
            }
            // Prevent overriding the named cache with undefined values
            if (data !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                elem[expando] = id;
                thisCache[name] = data;
            }
            return typeof name === "string" ? thisCache[name] : thisCache;
        },
        removeData: function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            if (elem.nodeName && jQuery.noData[elem.nodeName.toLowerCase()]) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            elem = elem == window ? windowData : elem;
            var id = elem[expando], cache = jQuery.cache, thisCache = cache[id];
            // If we want to remove a specific section of the element's data
            if (name) {
                "dk.brics.tajs.directives.unreachable";
                if (thisCache) {
                    "dk.brics.tajs.directives.unreachable";
                    // Remove the section of cache data
                    delete thisCache[name];
                    // If we've removed all the data, remove the element's cache
                    if (jQuery.isEmptyObject(thisCache)) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.removeData(elem);
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                // Clean up the element expando
                try {
                    "dk.brics.tajs.directives.unreachable";
                    delete elem[expando];
                } catch (e) {
                    "dk.brics.tajs.directives.unreachable";
                    // IE has trouble directly removing the expando
                    // but it's ok with using removeAttribute
                    if (elem.removeAttribute) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.removeAttribute(expando);
                    }
                }
                // Completely remove the data cache
                delete cache[id];
            }
        }
    });
    jQuery.fn.extend({
        data: function(key, value) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof key === "undefined" && this.length) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.data(this[0]);
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
                var data = this.triggerHandler("getData" + parts[1] + "!", [ parts[0] ]);
                if (data === undefined && this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    data = jQuery.data(this[0], key);
                }
                return data === undefined && parts[1] ? this.data(parts[0]) : data;
            } else {
                "dk.brics.tajs.directives.unreachable";
                return this.trigger("setData" + parts[1] + "!", [ parts[0], value ]).each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.data(this, key, value);
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
    jQuery.extend({
        queue: function(elem, type, data) {
            "dk.brics.tajs.directives.unreachable";
            if (!elem) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            type = (type || "fx") + "queue";
            var q = jQuery.data(elem, type);
            // Speed up dequeue by getting out quickly if this is just a lookup
            if (!data) {
                "dk.brics.tajs.directives.unreachable";
                return q || [];
            }
            if (!q || jQuery.isArray(data)) {
                "dk.brics.tajs.directives.unreachable";
                q = jQuery.data(elem, type, jQuery.makeArray(data));
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
            return this.each(function(i, elem) {
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
    var rclass = /[\n\t]/g, rspace = /\s+/, rreturn = /\r/g, rspecialurl = /href|src|style/, rtype = /(button|input)/i, rfocusable = /(button|input|object|select|textarea)/i, rclickable = /^(a|area)$/i, rradiocheck = /radio|checkbox/;
    jQuery.fn.extend({
        attr: function(name, value) {
            "dk.brics.tajs.directives.unreachable";
            return access(this, name, value, true, jQuery.attr);
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
                var classNames = (value || "").split(rspace);
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
                            var className = " " + elem.className + " ";
                            for (var c = 0, cl = classNames.length; c < cl; c++) {
                                "dk.brics.tajs.directives.unreachable";
                                if (className.indexOf(" " + classNames[c] + " ") < 0) {
                                    "dk.brics.tajs.directives.unreachable";
                                    elem.className += " " + classNames[c];
                                }
                            }
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
                var classNames = (value || "").split(rspace);
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
                            elem.className = className.substring(1, className.length - 1);
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
                            jQuery.data(this, "__className__", this.className);
                        }
                        // toggle whole className
                        this.className = this.className || value === false ? "" : jQuery.data(this, "__className__") || "";
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
            if (value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                var elem = this[0];
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.nodeName(elem, "option")) {
                        "dk.brics.tajs.directives.unreachable";
                        return (elem.attributes.value || {}).specified ? elem.value : elem.text;
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
                            if (option.selected) {
                                "dk.brics.tajs.directives.unreachable";
                                // Get the specifc value for the option
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
                // Typecast each time if the value is a Function and the appended
                // value is therefore different each time.
                if (typeof val === "number") {
                    "dk.brics.tajs.directives.unreachable";
                    val += "";
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
            // don't set attributes on text and comment nodes
            if (!elem || elem.nodeType === 3 || elem.nodeType === 8) {
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
                if (name in elem && notxml && !special) {
                    "dk.brics.tajs.directives.unreachable";
                    if (set) {
                        "dk.brics.tajs.directives.unreachable";
                        // We can't allow the type property to be changed (since it causes problems in IE)
                        if (name === "type" && rtype.test(elem.nodeName) && elem.parentNode) {
                            "dk.brics.tajs.directives.unreachable";
                            throw "type property can't be changed";
                        }
                        elem[name] = value;
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
                var attr = !jQuery.support.hrefNormalized && notxml && special ? // Some attributes require a special call on IE
                elem.getAttribute(name, 2) : elem.getAttribute(name);
                // Non-existent attributes return null, we normalize to undefined
                return attr === null ? undefined : attr;
            }
            // elem is actually elem.style ... set the style
            // Using attr for specific style information is now deprecated. Use style insead.
            return jQuery.style(elem, name, value);
        }
    });
    var fcleanup = function(nm) {
        "dk.brics.tajs.directives.unreachable";
        return nm.replace(/[^\w\s\.\|`]/g, function(ch) {
            "dk.brics.tajs.directives.unreachable";
            return "\\" + ch;
        });
    };
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
            if (elem.setInterval && elem !== window && !elem.frameElement) {
                "dk.brics.tajs.directives.unreachable";
                elem = window;
            }
            // Make sure that the function being executed has a unique ID
            if (!handler.guid) {
                "dk.brics.tajs.directives.unreachable";
                handler.guid = jQuery.guid++;
            }
            // if data is passed, bind to handler
            if (data !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                // Create temporary function pointer to original handler
                var fn = handler;
                // Create unique handler function, wrapped around original handler
                handler = jQuery.proxy(fn);
                // Store data in unique handler
                handler.data = data;
            }
            // Init the element's event structure
            var events = jQuery.data(elem, "events") || jQuery.data(elem, "events", {}), handle = jQuery.data(elem, "handle"), eventHandle;
            if (!handle) {
                "dk.brics.tajs.directives.unreachable";
                eventHandle = function() {
                    "dk.brics.tajs.directives.unreachable";
                    // Handle the second event of a trigger and when
                    // an event is called after a page has unloaded
                    return typeof jQuery !== "undefined" && !jQuery.event.triggered ? jQuery.event.handle.apply(eventHandle.elem, arguments) : undefined;
                };
                handle = jQuery.data(elem, "handle", eventHandle);
            }
            // If no handle is found then we must be trying to bind to one of the
            // banned noData elements
            if (!handle) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Add elem as a property of the handle function
            // This is to prevent a memory leak with non-native
            // event in IE.
            handle.elem = elem;
            // Handle multiple events separated by a space
            // jQuery(...).bind("mouseover mouseout", fn);
            types = types.split(/\s+/);
            var type, i = 0;
            while (type = types[i++]) {
                "dk.brics.tajs.directives.unreachable";
                // Namespaced event handlers
                var namespaces = type.split(".");
                type = namespaces.shift();
                handler.type = namespaces.slice(0).sort().join(".");
                // Get the current list of functions bound to this event
                var handlers = events[type], special = this.special[type] || {};
                // Init the event handler queue
                if (!handlers) {
                    "dk.brics.tajs.directives.unreachable";
                    handlers = events[type] = {};
                    // Check for a special event handler
                    // Only use addEventListener/attachEvent if the special
                    // events handler returns false
                    if (!special.setup || special.setup.call(elem, data, namespaces, handler) === false) {
                        "dk.brics.tajs.directives.unreachable";
                        // Bind the global event handler to the element
                        if (elem.addEventListener) {
                            "dk.brics.tajs.directives.unreachable";
                            elem.addEventListener(type, handle, false);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (elem.attachEvent) {
                                "dk.brics.tajs.directives.unreachable";
                                elem.attachEvent("on" + type, handle);
                            }
                        }
                    }
                }
                if (special.add) {
                    "dk.brics.tajs.directives.unreachable";
                    var modifiedHandler = special.add.call(elem, handler, data, namespaces, handlers);
                    if (modifiedHandler && jQuery.isFunction(modifiedHandler)) {
                        "dk.brics.tajs.directives.unreachable";
                        modifiedHandler.guid = modifiedHandler.guid || handler.guid;
                        handler = modifiedHandler;
                    }
                }
                // Add the function to the element's handler list
                handlers[handler.guid] = handler;
                // Keep track of which events have been used, for global triggering
                this.global[type] = true;
            }
            // Nullify elem to prevent memory leaks in IE
            elem = null;
        },
        global: {},
        // Detach an event or set of events from an element
        remove: function(elem, types, handler) {
            "dk.brics.tajs.directives.unreachable";
            // don't do events on text and comment nodes
            if (elem.nodeType === 3 || elem.nodeType === 8) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var events = jQuery.data(elem, "events"), ret, type, fn;
            if (events) {
                "dk.brics.tajs.directives.unreachable";
                // Unbind all events for the element
                if (types === undefined || typeof types === "string" && types.charAt(0) === ".") {
                    "dk.brics.tajs.directives.unreachable";
                    for (type in events) {
                        "dk.brics.tajs.directives.unreachable";
                        this.remove(elem, type + (types || ""));
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // types is actually an event object here
                    if (types.type) {
                        "dk.brics.tajs.directives.unreachable";
                        handler = types.handler;
                        types = types.type;
                    }
                    // Handle multiple events separated by a space
                    // jQuery(...).unbind("mouseover mouseout", fn);
                    types = types.split(/\s+/);
                    var i = 0;
                    while (type = types[i++]) {
                        "dk.brics.tajs.directives.unreachable";
                        // Namespaced event handlers
                        var namespaces = type.split(".");
                        type = namespaces.shift();
                        var all = !namespaces.length, cleaned = jQuery.map(namespaces.slice(0).sort(), fcleanup), namespace = new RegExp("(^|\\.)" + cleaned.join("\\.(?:.*\\.)?") + "(\\.|$)"), special = this.special[type] || {};
                        if (events[type]) {
                            "dk.brics.tajs.directives.unreachable";
                            // remove the given handler for the given type
                            if (handler) {
                                "dk.brics.tajs.directives.unreachable";
                                fn = events[type][handler.guid];
                                delete events[type][handler.guid];
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                for (var handle in events[type]) {
                                    "dk.brics.tajs.directives.unreachable";
                                    // Handle the removal of namespaced events
                                    if (all || namespace.test(events[type][handle].type)) {
                                        "dk.brics.tajs.directives.unreachable";
                                        delete events[type][handle];
                                    }
                                }
                            }
                            if (special.remove) {
                                "dk.brics.tajs.directives.unreachable";
                                special.remove.call(elem, namespaces, fn);
                            }
                            // remove generic event handler if no more handlers exist
                            for (ret in events[type]) {
                                "dk.brics.tajs.directives.unreachable";
                                break;
                            }
                            if (!ret) {
                                "dk.brics.tajs.directives.unreachable";
                                if (!special.teardown || special.teardown.call(elem, namespaces) === false) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (elem.removeEventListener) {
                                        "dk.brics.tajs.directives.unreachable";
                                        elem.removeEventListener(type, jQuery.data(elem, "handle"), false);
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (elem.detachEvent) {
                                            "dk.brics.tajs.directives.unreachable";
                                            elem.detachEvent("on" + type, jQuery.data(elem, "handle"));
                                        }
                                    }
                                }
                                ret = null;
                                delete events[type];
                            }
                        }
                    }
                }
                // Remove the expando if it's no longer used
                for (ret in events) {
                    "dk.brics.tajs.directives.unreachable";
                    break;
                }
                if (!ret) {
                    "dk.brics.tajs.directives.unreachable";
                    var handle = jQuery.data(elem, "handle");
                    if (handle) {
                        "dk.brics.tajs.directives.unreachable";
                        handle.elem = null;
                    }
                    jQuery.removeData(elem, "events");
                    jQuery.removeData(elem, "handle");
                }
            }
        },
        // bubbling is internal
        trigger: function(event, data, elem) {
            // Event object or event type
            var type = event.type || event, bubbling = arguments[3];
            if (!bubbling) {
                event = typeof event === "object" ? // jQuery.Event object
                event[expando] ? event : // Object literal
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
                    if (this.global[type]) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.each(jQuery.cache, function() {
                            "dk.brics.tajs.directives.unreachable";
                            if (this.events && this.events[type]) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.event.trigger(event, data, this.handle.elem);
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
            var handle = jQuery.data(elem, "handle");
            if (handle) {
                "dk.brics.tajs.directives.unreachable";
                handle.apply(elem, data);
            }
            var nativeFn, nativeHandler;
            try {
                if (!(elem && elem.nodeName && jQuery.noData[elem.nodeName.toLowerCase()])) {
                    nativeFn = elem[type];
                    nativeHandler = elem["on" + type];
                }
            } catch (e) {}
            var isClick = jQuery.nodeName(elem, "a") && type === "click";
            // Trigger the native events (except for clicks on links)
            if (!bubbling && nativeFn && !event.isDefaultPrevented() && !isClick) {
                "dk.brics.tajs.directives.unreachable";
                this.triggered = true;
                try {
                    "dk.brics.tajs.directives.unreachable";
                    elem[type]();
                } catch (e) {}
            } else {
                if (nativeHandler && elem["on" + type].apply(elem, data) === false) {
                    "dk.brics.tajs.directives.unreachable";
                    event.result = false;
                }
            }
            this.triggered = false;
            if (!event.isPropagationStopped()) {
                "dk.brics.tajs.directives.unreachable";
                var parent = elem.parentNode || elem.ownerDocument;
                if (parent) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger(event, data, parent, true);
                }
            }
        },
        handle: function(event) {
            "dk.brics.tajs.directives.unreachable";
            // returned undefined or false
            var all, handlers;
            event = arguments[0] = jQuery.event.fix(event || window.event);
            event.currentTarget = this;
            // Namespaced event handlers
            var namespaces = event.type.split(".");
            event.type = namespaces.shift();
            // Cache this now, all = true means, any handler
            all = !namespaces.length && !event.exclusive;
            var namespace = new RegExp("(^|\\.)" + namespaces.slice(0).sort().join("\\.(?:.*\\.)?") + "(\\.|$)");
            handlers = (jQuery.data(this, "events") || {})[event.type];
            for (var j in handlers) {
                "dk.brics.tajs.directives.unreachable";
                var handler = handlers[j];
                // Filter the functions by class
                if (all || namespace.test(handler.type)) {
                    "dk.brics.tajs.directives.unreachable";
                    // Pass in a reference to the handler function itself
                    // So that we can later remove it
                    event.handler = handler;
                    event.data = handler.data;
                    var ret = handler.apply(this, arguments);
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
            return event.result;
        },
        props: "altKey attrChange attrName bubbles button cancelable charCode clientX clientY ctrlKey currentTarget data detail eventPhase fromElement handler keyCode layerX layerY metaKey newValue offsetX offsetY originalTarget pageX pageY prevValue relatedNode relatedTarget screenX screenY shiftKey srcElement target toElement view wheelDelta which".split(" "),
        fix: function(event) {
            "dk.brics.tajs.directives.unreachable";
            if (event[expando]) {
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
            if (!event.which && (event.charCode || event.charCode === 0 ? event.charCode : event.keyCode)) {
                "dk.brics.tajs.directives.unreachable";
                event.which = event.charCode || event.keyCode;
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
                add: function(proxy, data, namespaces, live) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.extend(proxy, data || {});
                    proxy.guid += data.selector + data.live;
                    jQuery.event.add(this, data.live, liveHandler, data);
                },
                remove: function(namespaces) {
                    "dk.brics.tajs.directives.unreachable";
                    if (namespaces.length) {
                        "dk.brics.tajs.directives.unreachable";
                        var remove = 0, name = new RegExp("(^|\\.)" + namespaces[0] + "(\\.|$)");
                        jQuery.each(jQuery.data(this, "events").live || {}, function() {
                            "dk.brics.tajs.directives.unreachable";
                            if (name.test(this.type)) {
                                "dk.brics.tajs.directives.unreachable";
                                remove++;
                            }
                        });
                        if (remove < 1) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.event.remove(this, namespaces[0], liveHandler);
                        }
                    }
                },
                special: {}
            },
            beforeunload: {
                setup: function(data, namespaces, fn) {
                    "dk.brics.tajs.directives.unreachable";
                    // We only want to do this special case on windows
                    if (this.setInterval) {
                        "dk.brics.tajs.directives.unreachable";
                        this.onbeforeunload = fn;
                    }
                    return false;
                },
                teardown: function(namespaces, fn) {
                    "dk.brics.tajs.directives.unreachable";
                    if (this.onbeforeunload === fn) {
                        "dk.brics.tajs.directives.unreachable";
                        this.onbeforeunload = null;
                    }
                }
            }
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
        } else {
            this.type = src;
        }
        // timeStamp is buggy for some events on Firefox(#3843)
        // So we won't rely on the native value
        this.timeStamp = now();
        // Mark it as fixed
        this[expando] = true;
    };
    function returnFalse() {
        "dk.brics.tajs.directives.unreachable";
        return false;
    }
    function returnTrue() {
        return true;
    }
    // jQuery.Event is based on DOM3 Events as specified by the ECMAScript Language Binding
    // http://www.w3.org/TR/2003/WD-DOM-Level-3-Events-20030331/ecma-script-binding.html
    jQuery.Event.prototype = {
        preventDefault: function() {
            this.isDefaultPrevented = returnTrue;
            var e = this.originalEvent;
            if (!e) {
                return;
            }
            "dk.brics.tajs.directives.unreachable";
            // if preventDefault exists run it on the original event
            if (e.preventDefault) {
                "dk.brics.tajs.directives.unreachable";
                e.preventDefault();
            }
            // otherwise set the returnValue property of the original event to false (IE)
            e.returnValue = false;
        },
        stopPropagation: function() {
            this.isPropagationStopped = returnTrue;
            var e = this.originalEvent;
            if (!e) {
                return;
            }
            "dk.brics.tajs.directives.unreachable";
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
        // Traverse up the tree
        while (parent && parent !== this) {
            "dk.brics.tajs.directives.unreachable";
            // Firefox sometimes assigns relatedTarget a XUL element
            // which we cannot access the parentNode property of
            try {
                "dk.brics.tajs.directives.unreachable";
                parent = parent.parentNode;
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                break;
            }
        }
        if (parent !== this) {
            "dk.brics.tajs.directives.unreachable";
            // set the correct event type
            event.type = event.data;
            // handle event if we actually just moused on to a non sub-element
            jQuery.event.handle.apply(this, arguments);
        }
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
            setup: function(data, namespaces, fn) {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeName.toLowerCase() !== "form") {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(this, "click.specialSubmit." + fn.guid, function(e) {
                        "dk.brics.tajs.directives.unreachable";
                        var elem = e.target, type = elem.type;
                        if ((type === "submit" || type === "image") && jQuery(elem).closest("form").length) {
                            "dk.brics.tajs.directives.unreachable";
                            return trigger("submit", this, arguments);
                        }
                    });
                    jQuery.event.add(this, "keypress.specialSubmit." + fn.guid, function(e) {
                        "dk.brics.tajs.directives.unreachable";
                        var elem = e.target, type = elem.type;
                        if ((type === "text" || type === "password") && jQuery(elem).closest("form").length && e.keyCode === 13) {
                            "dk.brics.tajs.directives.unreachable";
                            return trigger("submit", this, arguments);
                        }
                    });
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
            },
            remove: function(namespaces, fn) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, "click.specialSubmit" + (fn ? "." + fn.guid : ""));
                jQuery.event.remove(this, "keypress.specialSubmit" + (fn ? "." + fn.guid : ""));
            }
        };
    }
    // change delegation, happens here so we have bind.
    if (!jQuery.support.changeBubbles) {
        "dk.brics.tajs.directives.unreachable";
        var formElems = /textarea|input|select/i;
        function getVal(elem) {
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
        }
        function testChange(e) {
            "dk.brics.tajs.directives.unreachable";
            var elem = e.target, data, val;
            if (!formElems.test(elem.nodeName) || elem.readOnly) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            data = jQuery.data(elem, "_change_data");
            val = getVal(elem);
            if (val === data) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // the current data will be also retrieved by beforeactivate
            if (e.type !== "focusout" || elem.type !== "radio") {
                "dk.brics.tajs.directives.unreachable";
                jQuery.data(elem, "_change_data", val);
            }
            if (elem.type !== "select" && (data != null || val)) {
                "dk.brics.tajs.directives.unreachable";
                e.type = "change";
                return jQuery.event.trigger(e, arguments[1], this);
            }
        }
        jQuery.event.special.change = {
            filters: {
                focusout: testChange,
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
                // information/focus[in] is not needed anymore
                beforeactivate: function(e) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = e.target;
                    if (elem.nodeName.toLowerCase() === "input" && elem.type === "radio") {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.data(elem, "_change_data", getVal(elem));
                    }
                }
            },
            setup: function(data, namespaces, fn) {
                "dk.brics.tajs.directives.unreachable";
                for (var type in changeFilters) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(this, type + ".specialChange." + fn.guid, changeFilters[type]);
                }
                return formElems.test(this.nodeName);
            },
            remove: function(namespaces, fn) {
                "dk.brics.tajs.directives.unreachable";
                for (var type in changeFilters) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.remove(this, type + ".specialChange" + (fn ? "." + fn.guid : ""), changeFilters[type]);
                }
                return formElems.test(this.nodeName);
            }
        };
        var changeFilters = jQuery.event.special.change.filters;
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
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
                thisObject = fn;
                fn = data;
                data = undefined;
            }
            var handler = name === "one" ? jQuery.proxy(fn, function(event) {
                "dk.brics.tajs.directives.unreachable";
                jQuery(this).unbind(event, handler);
                return fn.apply(this, arguments);
            }) : fn;
            return type === "unload" && name !== "one" ? this.one(type, data, fn, thisObject) : this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.add(this, type, handler, data);
            });
        };
    });
    jQuery.fn.extend({
        unbind: function(type, fn) {
            "dk.brics.tajs.directives.unreachable";
            // Handle object literals
            if (typeof type === "object" && !type.preventDefault) {
                "dk.brics.tajs.directives.unreachable";
                for (var key in type) {
                    "dk.brics.tajs.directives.unreachable";
                    this.unbind(key, type[key]);
                }
                return this;
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, type, fn);
            });
        },
        trigger: function(type, data) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger(type, data, this);
            });
        },
        triggerHandler: function(type, data) {
            if (this[0]) {
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
                var lastToggle = (jQuery.data(this, "lastToggle" + fn.guid) || 0) % i;
                jQuery.data(this, "lastToggle" + fn.guid, lastToggle + 1);
                // Make sure that clicks stop
                event.preventDefault();
                // and execute the function
                return args[lastToggle].apply(this, arguments) || false;
            }));
        },
        hover: function(fnOver, fnOut) {
            "dk.brics.tajs.directives.unreachable";
            return this.mouseenter(fnOver).mouseleave(fnOut || fnOver);
        },
        live: function(type, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
                fn = data;
                data = undefined;
            }
            jQuery(this.context).bind(liveConvert(type, this.selector), {
                data: data,
                selector: this.selector,
                live: type
            }, fn);
            return this;
        },
        die: function(type, fn) {
            "dk.brics.tajs.directives.unreachable";
            jQuery(this.context).unbind(liveConvert(type, this.selector), fn ? {
                guid: fn.guid + this.selector + type
            } : null);
            return this;
        }
    });
    function liveHandler(event) {
        "dk.brics.tajs.directives.unreachable";
        var stop = true, elems = [], selectors = [], args = arguments, related, match, fn, elem, j, i, data, live = jQuery.extend({}, jQuery.data(this, "events").live);
        for (j in live) {
            "dk.brics.tajs.directives.unreachable";
            fn = live[j];
            if (fn.live === event.type || fn.altLive && jQuery.inArray(event.type, fn.altLive) > -1) {
                "dk.brics.tajs.directives.unreachable";
                data = fn.data;
                if (!(data.beforeFilter && data.beforeFilter[event.type] && !data.beforeFilter[event.type](event))) {
                    "dk.brics.tajs.directives.unreachable";
                    selectors.push(fn.selector);
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                delete live[j];
            }
        }
        match = jQuery(event.target).closest(selectors, event.currentTarget);
        for (i = 0, l = match.length; i < l; i++) {
            "dk.brics.tajs.directives.unreachable";
            for (j in live) {
                "dk.brics.tajs.directives.unreachable";
                fn = live[j];
                elem = match[i].elem;
                related = null;
                if (match[i].selector === fn.selector) {
                    "dk.brics.tajs.directives.unreachable";
                    // Those two events require additional checking
                    if (fn.live === "mouseenter" || fn.live === "mouseleave") {
                        "dk.brics.tajs.directives.unreachable";
                        related = jQuery(event.relatedTarget).closest(fn.selector)[0];
                    }
                    if (!related || related !== elem) {
                        "dk.brics.tajs.directives.unreachable";
                        elems.push({
                            elem: elem,
                            fn: fn
                        });
                    }
                }
            }
        }
        for (i = 0, l = elems.length; i < l; i++) {
            "dk.brics.tajs.directives.unreachable";
            match = elems[i];
            event.currentTarget = match.elem;
            event.data = match.fn.data;
            if (match.fn.apply(match.elem, args) === false) {
                "dk.brics.tajs.directives.unreachable";
                stop = false;
                break;
            }
        }
        return stop;
    }
    function liveConvert(type, selector) {
        "dk.brics.tajs.directives.unreachable";
        return [ "live", type, selector.replace(/\./g, "`").replace(/ /g, "&") ].join(".");
    }
    jQuery.each(("blur focus focusin focusout load resize scroll unload click dblclick " + "mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " + "change select submit keydown keypress keyup error").split(" "), function(i, name) {
        // Handle event binding
        jQuery.fn[name] = function(fn) {
            "dk.brics.tajs.directives.unreachable";
            return fn ? this.bind(name, fn) : this.trigger(name);
        };
        if (jQuery.attrFn) {
            jQuery.attrFn[name] = true;
        }
    });
    // Prevent memory leaks in IE
    // Window isn't included so as not to unbind existing unload events
    // More info:
    //  - http://isaacschlueter.com/2006/10/msie-memory-leaks/
    if (window.attachEvent && !window.addEventListener) {
        "dk.brics.tajs.directives.unreachable";
        window.attachEvent("onunload", function() {
            "dk.brics.tajs.directives.unreachable";
            for (var id in jQuery.cache) {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.cache[id].handle) {
                    "dk.brics.tajs.directives.unreachable";
                    // Try/Catch is to handle iframes being unloaded, see #4280
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.remove(jQuery.cache[id].handle.elem);
                    } catch (e) {}
                }
            }
        });
    }
    /*!
 * Sizzle CSS Selector Engine - v1.0
 *  Copyright 2009, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
    (function() {
        var chunker = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^[\]]*\]|['"][^'"]*['"]|[^[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g, done = 0, toString = Object.prototype.toString, hasDuplicate = false, baseHasDuplicate = true;
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
            var origContext = context = context || document;
            if (context.nodeType !== 1 && context.nodeType !== 9) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            if (!selector || typeof selector !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return results;
            }
            var parts = [], m, set, checkSet, extra, prune = true, contextXML = isXML(context), soFar = selector;
            // Reset the position of the chunker regexp (start from head)
            while ((chunker.exec(""), m = chunker.exec(soFar)) !== null) {
                "dk.brics.tajs.directives.unreachable";
                soFar = m[3];
                parts.push(m[1]);
                if (m[2]) {
                    "dk.brics.tajs.directives.unreachable";
                    extra = m[3];
                    break;
                }
            }
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
                    var ret = Sizzle.find(parts.shift(), context, contextXML);
                    context = ret.expr ? Sizzle.filter(ret.expr, ret.set)[0] : ret.set[0];
                }
                if (context) {
                    "dk.brics.tajs.directives.unreachable";
                    var ret = seed ? {
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
                        var cur = parts.pop(), pop = cur;
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
                throw "Syntax error, unrecognized expression: " + (cur || selector);
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
                        for (var i = 0; checkSet[i] != null; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (checkSet[i] && (checkSet[i] === true || checkSet[i].nodeType === 1 && contains(context, checkSet[i]))) {
                                "dk.brics.tajs.directives.unreachable";
                                results.push(set[i]);
                            }
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0; checkSet[i] != null; i++) {
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
        Sizzle.find = function(expr, context, isXML) {
            "dk.brics.tajs.directives.unreachable";
            var set, match;
            if (!expr) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            for (var i = 0, l = Expr.order.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var type = Expr.order[i], match;
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
                set = context.getElementsByTagName("*");
            }
            return {
                set: set,
                expr: expr
            };
        };
        Sizzle.filter = function(expr, set, inplace, not) {
            "dk.brics.tajs.directives.unreachable";
            var old = expr, result = [], curLoop = set, match, anyFound, isXMLFilter = set && set[0] && isXML(set[0]);
            while (expr && set.length) {
                "dk.brics.tajs.directives.unreachable";
                for (var type in Expr.filter) {
                    "dk.brics.tajs.directives.unreachable";
                    if ((match = Expr.leftMatch[type].exec(expr)) != null && match[2]) {
                        "dk.brics.tajs.directives.unreachable";
                        var filter = Expr.filter[type], found, item, left = match[1];
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
                        throw "Syntax error, unrecognized expression: " + expr;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        break;
                    }
                }
                old = expr;
            }
            return curLoop;
        };
        var Expr = Sizzle.selectors = {
            order: [ "ID", "NAME", "TAG" ],
            match: {
                ID: /#((?:[\w\u00c0-\uFFFF-]|\\.)+)/,
                CLASS: /\.((?:[\w\u00c0-\uFFFF-]|\\.)+)/,
                NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF-]|\\.)+)['"]*\]/,
                ATTR: /\[\s*((?:[\w\u00c0-\uFFFF-]|\\.)+)\s*(?:(\S?=)\s*(['"]*)(.*?)\3|)\s*\]/,
                TAG: /^((?:[\w\u00c0-\uFFFF\*-]|\\.)+)/,
                CHILD: /:(only|nth|last|first)-child(?:\((even|odd|[\dn+-]*)\))?/,
                POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^-]|$)/,
                PSEUDO: /:((?:[\w\u00c0-\uFFFF-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/
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
                    var isPartStr = typeof part === "string";
                    if (isPartStr && !/\W/.test(part)) {
                        "dk.brics.tajs.directives.unreachable";
                        part = part.toLowerCase();
                        for (var i = 0, l = checkSet.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            var elem = checkSet[i];
                            if (elem) {
                                "dk.brics.tajs.directives.unreachable";
                                var parent = elem.parentNode;
                                checkSet[i] = parent.nodeName.toLowerCase() === part ? parent : false;
                            }
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0, l = checkSet.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            var elem = checkSet[i];
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
                    var doneName = done++, checkFn = dirCheck;
                    if (typeof part === "string" && !/\W/.test(part)) {
                        "dk.brics.tajs.directives.unreachable";
                        var nodeCheck = part = part.toLowerCase();
                        checkFn = dirNodeCheck;
                    }
                    checkFn("parentNode", part, doneName, checkSet, nodeCheck, isXML);
                },
                "~": function(checkSet, part, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    var doneName = done++, checkFn = dirCheck;
                    if (typeof part === "string" && !/\W/.test(part)) {
                        "dk.brics.tajs.directives.unreachable";
                        var nodeCheck = part = part.toLowerCase();
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
                        return m ? [ m ] : [];
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
                    return context.getElementsByTagName(match[1]);
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
                            if (not ^ (elem.className && (" " + elem.className + " ").replace(/[\t\n]/g, " ").indexOf(match) >= 0)) {
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
                        // parse equations like 'even', 'odd', '5', '2n', '3n+2', '4n-1', '-n+6'
                        var test = /(-?)(\d*)n((?:\+|-)?\d*)/.exec(match[2] === "even" && "2n" || match[2] === "odd" && "2n+1" || !/\D/.test(match[2]) && "0n+" + match[2] || match[2]);
                        // calculate the numbers (first)n+(last) including if they are negative
                        match[2] = test[1] + (test[2] || 1) - 0;
                        match[3] = test[3] - 0;
                    }
                    // TODO: Move to normal caching system
                    match[0] = done++;
                    return match;
                },
                ATTR: function(match, curLoop, inplace, result, not, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = match[1].replace(/\\/g, "");
                    if (!isXML && Expr.attrMap[name]) {
                        "dk.brics.tajs.directives.unreachable";
                        match[1] = Expr.attrMap[name];
                    }
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
                            return (elem.textContent || elem.innerText || getText([ elem ]) || "").indexOf(match[3]) >= 0;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (name === "not") {
                                "dk.brics.tajs.directives.unreachable";
                                var not = match[3];
                                for (var i = 0, l = not.length; i < l; i++) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (not[i] === elem) {
                                        "dk.brics.tajs.directives.unreachable";
                                        return false;
                                    }
                                }
                                return true;
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                throw "Syntax error, unrecognized expression: " + name;
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
        var origPOS = Expr.match.POS;
        for (var type in Expr.match) {
            Expr.match[type] = new RegExp(Expr.match[type].source + /(?![^\[]*\])(?![^\(]*\))/.source);
            Expr.leftMatch[type] = new RegExp(/(^(?:.|\r|\n)*?)/.source + Expr.match[type].source.replace(/\\(\d+)/g, function(all, num) {
                return "\\" + (num - 0 + 1);
            }));
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
        try {
            Array.prototype.slice.call(document.documentElement.childNodes, 0);
        } catch (e) {
            "dk.brics.tajs.directives.unreachable";
            makeArray = function(array, results) {
                "dk.brics.tajs.directives.unreachable";
                var ret = results || [];
                if (toString.call(array) === "[object Array]") {
                    "dk.brics.tajs.directives.unreachable";
                    Array.prototype.push.apply(ret, array);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof array.length === "number") {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0, l = array.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            ret.push(array[i]);
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0; array[i]; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            ret.push(array[i]);
                        }
                    }
                }
                return ret;
            };
        }
        var sortOrder;
        if (document.documentElement.compareDocumentPosition) {
            sortOrder = function(a, b) {
                "dk.brics.tajs.directives.unreachable";
                if (!a.compareDocumentPosition || !b.compareDocumentPosition) {
                    "dk.brics.tajs.directives.unreachable";
                    if (a == b) {
                        "dk.brics.tajs.directives.unreachable";
                        hasDuplicate = true;
                    }
                    return a.compareDocumentPosition ? -1 : 1;
                }
                var ret = a.compareDocumentPosition(b) & 4 ? -1 : a === b ? 0 : 1;
                if (ret === 0) {
                    "dk.brics.tajs.directives.unreachable";
                    hasDuplicate = true;
                }
                return ret;
            };
        } else {
            "dk.brics.tajs.directives.unreachable";
            if ("sourceIndex" in document.documentElement) {
                "dk.brics.tajs.directives.unreachable";
                sortOrder = function(a, b) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!a.sourceIndex || !b.sourceIndex) {
                        "dk.brics.tajs.directives.unreachable";
                        if (a == b) {
                            "dk.brics.tajs.directives.unreachable";
                            hasDuplicate = true;
                        }
                        return a.sourceIndex ? -1 : 1;
                    }
                    var ret = a.sourceIndex - b.sourceIndex;
                    if (ret === 0) {
                        "dk.brics.tajs.directives.unreachable";
                        hasDuplicate = true;
                    }
                    return ret;
                };
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (document.createRange) {
                    "dk.brics.tajs.directives.unreachable";
                    sortOrder = function(a, b) {
                        "dk.brics.tajs.directives.unreachable";
                        if (!a.ownerDocument || !b.ownerDocument) {
                            "dk.brics.tajs.directives.unreachable";
                            if (a == b) {
                                "dk.brics.tajs.directives.unreachable";
                                hasDuplicate = true;
                            }
                            return a.ownerDocument ? -1 : 1;
                        }
                        var aRange = a.ownerDocument.createRange(), bRange = b.ownerDocument.createRange();
                        aRange.setStart(a, 0);
                        aRange.setEnd(a, 0);
                        bRange.setStart(b, 0);
                        bRange.setEnd(b, 0);
                        var ret = aRange.compareBoundaryPoints(Range.START_TO_END, bRange);
                        if (ret === 0) {
                            "dk.brics.tajs.directives.unreachable";
                            hasDuplicate = true;
                        }
                        return ret;
                    };
                }
            }
        }
        // Utility function for retreiving the text value of an array of DOM nodes
        function getText(elems) {
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
                        ret += getText(elem.childNodes);
                    }
                }
            }
            return ret;
        }
        // Check to see if the browser returns elements by name when
        // querying by getElementById (and provide a workaround)
        (function() {
            // We're going to inject a fake input element with a specified name
            var form = document.createElement("div"), id = "script" + "TAJS_UUID";
            form.innerHTML = "<a name='" + id + "'/>";
            // Inject it into the root element, check its status, and remove it quickly
            var root = document.documentElement;
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
            div = null;
        })();
        if (document.querySelectorAll) {
            (function() {
                var oldSizzle = Sizzle, div = document.createElement("div");
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
                    if (!seed && context.nodeType === 9 && !isXML(context)) {
                        "dk.brics.tajs.directives.unreachable";
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            return makeArray(context.querySelectorAll(query), extra);
                        } catch (e) {}
                    }
                    return oldSizzle(query, context, extra, seed);
                };
                for (var prop in oldSizzle) {
                    Sizzle[prop] = oldSizzle[prop];
                }
                div = null;
            })();
        }
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
            div = null;
        })();
        function dirNodeCheck(dir, cur, doneName, checkSet, nodeCheck, isXML) {
            "dk.brics.tajs.directives.unreachable";
            for (var i = 0, l = checkSet.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var elem = checkSet[i];
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = elem[dir];
                    var match = false;
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
                    elem = elem[dir];
                    var match = false;
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
        var contains = document.compareDocumentPosition ? function(a, b) {
            "dk.brics.tajs.directives.unreachable";
            return a.compareDocumentPosition(b) & 16;
        } : function(a, b) {
            "dk.brics.tajs.directives.unreachable";
            return a !== b && (a.contains ? a.contains(b) : true);
        };
        var isXML = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            // documentElement is verified for cases where it doesn't yet exist
            // (such as loading iframes in IE - #4833) 
            var documentElement = (elem ? elem.ownerDocument || elem : 0).documentElement;
            return documentElement ? documentElement.nodeName !== "HTML" : false;
        };
        var posProcess = function(selector, context) {
            "dk.brics.tajs.directives.unreachable";
            var tmpSet = [], later = "", match, root = context.nodeType ? [ context ] : context;
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
        jQuery.getText = getText;
        jQuery.isXMLDoc = isXML;
        jQuery.contains = contains;
        return;
        "dk.brics.tajs.directives.unreachable";
        window.Sizzle = Sizzle;
    })();
    var runtil = /Until$/, rparentsprev = /^(?:parents|prevUntil|prevAll)/, // Note: This RegExp should be improved, or likely pulled from Sizzle
    rmultiselector = /,/, slice = Array.prototype.slice;
    // Implement the identical functionality for filter and not
    var winnow = function(elements, qualifier, keep) {
        "dk.brics.tajs.directives.unreachable";
        if (jQuery.isFunction(qualifier)) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.grep(elements, function(elem, i) {
                "dk.brics.tajs.directives.unreachable";
                return !!qualifier.call(elem, i, elem) === keep;
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
                        qualifier = jQuery.filter(qualifier, elements);
                    }
                }
            }
        }
        return jQuery.grep(elements, function(elem, i) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.inArray(elem, qualifier) >= 0 === keep;
        });
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
            if (jQuery.isArray(selectors)) {
                "dk.brics.tajs.directives.unreachable";
                var ret = [], cur = this[0], match, matches = {}, selector;
                if (cur && selectors.length) {
                    "dk.brics.tajs.directives.unreachable";
                    for (var i = 0, l = selectors.length; i < l; i++) {
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
                                    elem: cur
                                });
                                delete matches[selector];
                            }
                        }
                        cur = cur.parentNode;
                    }
                }
                return ret;
            }
            var pos = jQuery.expr.match.POS.test(selectors) ? jQuery(selectors, context || this.context) : null;
            return this.map(function(i, cur) {
                "dk.brics.tajs.directives.unreachable";
                while (cur && cur.ownerDocument && cur !== context) {
                    "dk.brics.tajs.directives.unreachable";
                    if (pos ? pos.index(cur) > -1 : jQuery(cur).is(selectors)) {
                        "dk.brics.tajs.directives.unreachable";
                        return cur;
                    }
                    cur = cur.parentNode;
                }
                return null;
            });
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
            var set = typeof selector === "string" ? jQuery(selector, context || this.context) : jQuery.makeArray(selector), all = jQuery.merge(this.get(), set);
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
            var ret = jQuery.map(this, fn, until);
            if (!runtil.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                selector = until;
            }
            if (selector && typeof selector === "string") {
                "dk.brics.tajs.directives.unreachable";
                ret = jQuery.filter(selector, ret);
            }
            ret = this.length > 1 ? jQuery.unique(ret) : ret;
            if ((this.length > 1 || rmultiselector.test(selector)) && rparentsprev.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                ret = ret.reverse();
            }
            return this.pushStack(ret, name, slice.call(arguments).join(","));
        };
    });
    jQuery.extend({
        filter: function(expr, elems, not) {
            "dk.brics.tajs.directives.unreachable";
            if (not) {
                "dk.brics.tajs.directives.unreachable";
                expr = ":not(" + expr + ")";
            }
            return jQuery.find.matches(expr, elems);
        },
        dir: function(elem, dir, until) {
            "dk.brics.tajs.directives.unreachable";
            var matched = [], cur = elem[dir];
            while (cur && cur.nodeType !== 9 && (until === undefined || !jQuery(cur).is(until))) {
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
    var rinlinejQuery = / jQuery\d+="(?:\d+|null)"/g, rleadingWhitespace = /^\s+/, rxhtmlTag = /(<([\w:]+)[^>]*?)\/>/g, rselfClosing = /^(?:area|br|col|embed|hr|img|input|link|meta|param)$/i, rtagName = /<([\w:]+)/, rtbody = /<tbody/i, rhtml = /<|&\w+;/, fcloseTag = function(all, front, tag) {
        "dk.brics.tajs.directives.unreachable";
        return rselfClosing.test(tag) ? all : front + "></" + tag + ">";
    }, wrapMap = {
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
                    return self.text(text.call(this, i, self.text()));
                });
            }
            if (typeof text !== "object" && text !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                return this.empty().append((this[0] && this[0].ownerDocument || document).createTextNode(text));
            }
            return jQuery.getText(this);
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
        clone: function(events) {
            "dk.brics.tajs.directives.unreachable";
            // Do the clone
            var ret = this.map(function() {
                "dk.brics.tajs.directives.unreachable";
                if (!jQuery.support.noCloneEvent && !jQuery.isXMLDoc(this)) {
                    "dk.brics.tajs.directives.unreachable";
                    // IE copies events bound via attachEvent when
                    // using cloneNode. Calling detachEvent on the
                    // clone will also remove the events from the orignal
                    // In order to get around this, we use innerHTML.
                    // Unfortunately, this means some modifications to
                    // attributes in IE that are actually only stored
                    // as properties will not be copied (such as the
                    // the name attribute on an input).
                    var html = this.outerHTML, ownerDocument = this.ownerDocument;
                    if (!html) {
                        "dk.brics.tajs.directives.unreachable";
                        var div = ownerDocument.createElement("div");
                        div.appendChild(this.cloneNode(true));
                        html = div.innerHTML;
                    }
                    return jQuery.clean([ html.replace(rinlinejQuery, "").replace(rleadingWhitespace, "") ], ownerDocument)[0];
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return this.cloneNode(true);
                }
            });
            // Copy the events from the original to the clone
            if (events === true) {
                "dk.brics.tajs.directives.unreachable";
                cloneCopyEvent(this, ret);
                cloneCopyEvent(this.find("*"), ret.find("*"));
            }
            // Return the cloned set
            return ret;
        },
        html: function(value) {
            "dk.brics.tajs.directives.unreachable";
            if (value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                return this[0] && this[0].nodeType === 1 ? this[0].innerHTML.replace(rinlinejQuery, "") : null;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (typeof value === "string" && !/<script/i.test(value) && (jQuery.support.leadingWhitespace || !rleadingWhitespace.test(value)) && !wrapMap[(rtagName.exec(value) || [ "", "" ])[1].toLowerCase()]) {
                    "dk.brics.tajs.directives.unreachable";
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0, l = this.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            // Remove element nodes and prevent memory leaks
                            if (this[i].nodeType === 1) {
                                "dk.brics.tajs.directives.unreachable";
                                cleanData(this[i].getElementsByTagName("*"));
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
                            var self = jQuery(this), old = self.html();
                            self.empty().append(function() {
                                "dk.brics.tajs.directives.unreachable";
                                return value.call(this, i, old);
                            });
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
                if (!jQuery.isFunction(value)) {
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
            var results, first, value = args[0], scripts = [];
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    var self = jQuery(this);
                    args[0] = value.call(this, i, table ? self.html() : undefined);
                    return self.domManip(args, table, callback);
                });
            }
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                // If we're in a fragment, just use that instead of building a new one
                if (args[0] && args[0].parentNode && args[0].parentNode.nodeType === 11) {
                    "dk.brics.tajs.directives.unreachable";
                    results = {
                        fragment: args[0].parentNode
                    };
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    results = buildFragment(args, this, scripts);
                }
                first = results.fragment.firstChild;
                if (first) {
                    "dk.brics.tajs.directives.unreachable";
                    table = table && jQuery.nodeName(first, "tr");
                    for (var i = 0, l = this.length; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        callback.call(table ? root(this[i], first) : this[i], results.cacheable || this.length > 1 || i > 0 ? results.fragment.cloneNode(true) : results.fragment);
                    }
                }
                if (scripts) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.each(scripts, evalScript);
                }
            }
            return this;
            function root(elem, cur) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.nodeName(elem, "table") ? elem.getElementsByTagName("tbody")[0] || elem.appendChild(elem.ownerDocument.createElement("tbody")) : elem;
            }
        }
    });
    function cloneCopyEvent(orig, ret) {
        "dk.brics.tajs.directives.unreachable";
        var i = 0;
        ret.each(function() {
            "dk.brics.tajs.directives.unreachable";
            if (this.nodeName !== (orig[i] && orig[i].nodeName)) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var oldData = jQuery.data(orig[i++]), curData = jQuery.data(this, oldData), events = oldData && oldData.events;
            if (events) {
                "dk.brics.tajs.directives.unreachable";
                delete curData.handle;
                curData.events = {};
                for (var type in events) {
                    "dk.brics.tajs.directives.unreachable";
                    for (var handler in events[type]) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.event.add(this, type, events[type][handler], events[type][handler].data);
                    }
                }
            }
        });
    }
    function buildFragment(args, nodes, scripts) {
        "dk.brics.tajs.directives.unreachable";
        var fragment, cacheable, cached, cacheresults, doc;
        if (args.length === 1 && typeof args[0] === "string" && args[0].length < 512 && args[0].indexOf("<option") < 0) {
            "dk.brics.tajs.directives.unreachable";
            cacheable = true;
            cacheresults = jQuery.fragments[args[0]];
            if (cacheresults) {
                "dk.brics.tajs.directives.unreachable";
                if (cacheresults !== 1) {
                    "dk.brics.tajs.directives.unreachable";
                    fragment = cacheresults;
                }
                cached = true;
            }
        }
        if (!fragment) {
            "dk.brics.tajs.directives.unreachable";
            doc = nodes && nodes[0] ? nodes[0].ownerDocument || nodes[0] : document;
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
    }
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
            var ret = [], insert = jQuery(selector);
            for (var i = 0, l = insert.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var elems = (i > 0 ? this.clone(true) : this).get();
                jQuery.fn[original].apply(jQuery(insert[i]), elems);
                ret = ret.concat(elems);
            }
            return this.pushStack(ret, name, insert.selector);
        };
    });
    jQuery.each({
        // keepData is for internal use only--do not document
        remove: function(selector, keepData) {
            "dk.brics.tajs.directives.unreachable";
            if (!selector || jQuery.filter(selector, [ this ]).length) {
                "dk.brics.tajs.directives.unreachable";
                if (!keepData && this.nodeType === 1) {
                    "dk.brics.tajs.directives.unreachable";
                    cleanData(this.getElementsByTagName("*"));
                    cleanData([ this ]);
                }
                if (this.parentNode) {
                    "dk.brics.tajs.directives.unreachable";
                    this.parentNode.removeChild(this);
                }
            }
        },
        empty: function() {
            "dk.brics.tajs.directives.unreachable";
            // Remove element nodes and prevent memory leaks
            if (this.nodeType === 1) {
                "dk.brics.tajs.directives.unreachable";
                cleanData(this.getElementsByTagName("*"));
            }
            // Remove any remaining nodes
            while (this.firstChild) {
                "dk.brics.tajs.directives.unreachable";
                this.removeChild(this.firstChild);
            }
        }
    }, function(name, fn) {
        jQuery.fn[name] = function() {
            "dk.brics.tajs.directives.unreachable";
            return this.each(fn, arguments);
        };
    });
    jQuery.extend({
        clean: function(elems, context, fragment, scripts) {
            "dk.brics.tajs.directives.unreachable";
            context = context || document;
            // !context.createElement fails in IE with an error but returns typeof 'object'
            if (typeof context.createElement === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                context = context.ownerDocument || context[0] && context[0].ownerDocument || document;
            }
            var ret = [];
            jQuery.each(elems, function(i, elem) {
                "dk.brics.tajs.directives.unreachable";
                if (typeof elem === "number") {
                    "dk.brics.tajs.directives.unreachable";
                    elem += "";
                }
                if (!elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
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
                        elem = elem.replace(rxhtmlTag, fcloseTag);
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
                        elem = jQuery.makeArray(div.childNodes);
                    }
                }
                if (elem.nodeType) {
                    "dk.brics.tajs.directives.unreachable";
                    ret.push(elem);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    ret = jQuery.merge(ret, elem);
                }
            });
            if (fragment) {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0; ret[i]; i++) {
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
        }
    });
    function cleanData(elems) {
        "dk.brics.tajs.directives.unreachable";
        for (var i = 0, elem, id; (elem = elems[i]) != null; i++) {
            "dk.brics.tajs.directives.unreachable";
            if (!jQuery.noData[elem.nodeName.toLowerCase()] && (id = elem[expando])) {
                "dk.brics.tajs.directives.unreachable";
                delete jQuery.cache[id];
            }
        }
    }
    // exclude the following css properties to add px
    var rexclude = /z-?index|font-?weight|opacity|zoom|line-?height/i, ralpha = /alpha\([^)]*\)/, ropacity = /opacity=([^)]*)/, rfloat = /float/i, rdashAlpha = /-([a-z])/gi, rupper = /([A-Z])/g, rnumpx = /^-?\d+(?:px)?$/i, rnum = /^-?\d/, cssShow = {
        position: "absolute",
        visibility: "hidden",
        display: "block"
    }, cssWidth = [ "Left", "Right" ], cssHeight = [ "Top", "Bottom" ], // cache check for defaultView.getComputedStyle
    getComputedStyle = document.defaultView && document.defaultView.getComputedStyle, // normalize float css property
    styleFloat = jQuery.support.cssFloat ? "cssFloat" : "styleFloat", fcamelCase = function(all, letter) {
        "dk.brics.tajs.directives.unreachable";
        return letter.toUpperCase();
    };
    jQuery.fn.css = function(name, value) {
        "dk.brics.tajs.directives.unreachable";
        return access(this, name, value, true, function(elem, name, value) {
            "dk.brics.tajs.directives.unreachable";
            if (value === undefined) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.curCSS(elem, name);
            }
            if (typeof value === "number" && !rexclude.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                value += "px";
            }
            jQuery.style(elem, name, value);
        });
    };
    jQuery.extend({
        style: function(elem, name, value) {
            "dk.brics.tajs.directives.unreachable";
            // don't set styles on text and comment nodes
            if (!elem || elem.nodeType === 3 || elem.nodeType === 8) {
                "dk.brics.tajs.directives.unreachable";
                return undefined;
            }
            // ignore negative width and height values #1599
            if ((name === "width" || name === "height") && parseFloat(value) < 0) {
                "dk.brics.tajs.directives.unreachable";
                value = undefined;
            }
            var style = elem.style || elem, set = value !== undefined;
            // IE uses filters for opacity
            if (!jQuery.support.opacity && name === "opacity") {
                "dk.brics.tajs.directives.unreachable";
                if (set) {
                    "dk.brics.tajs.directives.unreachable";
                    // IE has trouble with opacity if it does not have layout
                    // Force it by setting the zoom level
                    style.zoom = 1;
                    // Set the alpha filter to set the opacity
                    var opacity = parseInt(value, 10) + "" === "NaN" ? "" : "alpha(opacity=" + value * 100 + ")";
                    var filter = style.filter || jQuery.curCSS(elem, "filter") || "";
                    style.filter = ralpha.test(filter) ? filter.replace(ralpha, opacity) : opacity;
                }
                return style.filter && style.filter.indexOf("opacity=") >= 0 ? parseFloat(ropacity.exec(style.filter)[1]) / 100 + "" : "";
            }
            // Make sure we're using the right name for getting the float value
            if (rfloat.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                name = styleFloat;
            }
            name = name.replace(rdashAlpha, fcamelCase);
            if (set) {
                "dk.brics.tajs.directives.unreachable";
                style[name] = value;
            }
            return style[name];
        },
        css: function(elem, name, force, extra) {
            "dk.brics.tajs.directives.unreachable";
            if (name === "width" || name === "height") {
                "dk.brics.tajs.directives.unreachable";
                var val, props = cssShow, which = name === "width" ? cssWidth : cssHeight;
                function getWH() {
                    "dk.brics.tajs.directives.unreachable";
                    val = name === "width" ? elem.offsetWidth : elem.offsetHeight;
                    if (extra === "border") {
                        "dk.brics.tajs.directives.unreachable";
                        return;
                    }
                    jQuery.each(which, function() {
                        "dk.brics.tajs.directives.unreachable";
                        if (!extra) {
                            "dk.brics.tajs.directives.unreachable";
                            val -= parseFloat(jQuery.curCSS(elem, "padding" + this, true)) || 0;
                        }
                        if (extra === "margin") {
                            "dk.brics.tajs.directives.unreachable";
                            val += parseFloat(jQuery.curCSS(elem, "margin" + this, true)) || 0;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            val -= parseFloat(jQuery.curCSS(elem, "border" + this + "Width", true)) || 0;
                        }
                    });
                }
                if (elem.offsetWidth !== 0) {
                    "dk.brics.tajs.directives.unreachable";
                    getWH();
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.swap(elem, props, getWH);
                }
                return Math.max(0, Math.round(val));
            }
            return jQuery.curCSS(elem, name, force);
        },
        curCSS: function(elem, name, force) {
            "dk.brics.tajs.directives.unreachable";
            var ret, style = elem.style, filter;
            // IE uses filters for opacity
            if (!jQuery.support.opacity && name === "opacity" && elem.currentStyle) {
                "dk.brics.tajs.directives.unreachable";
                ret = ropacity.test(elem.currentStyle.filter || "") ? parseFloat(RegExp.$1) / 100 + "" : "";
                return ret === "" ? "1" : ret;
            }
            // Make sure we're using the right name for getting the float value
            if (rfloat.test(name)) {
                "dk.brics.tajs.directives.unreachable";
                name = styleFloat;
            }
            if (!force && style && style[name]) {
                "dk.brics.tajs.directives.unreachable";
                ret = style[name];
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (getComputedStyle) {
                    "dk.brics.tajs.directives.unreachable";
                    // Only "float" is needed here
                    if (rfloat.test(name)) {
                        "dk.brics.tajs.directives.unreachable";
                        name = "float";
                    }
                    name = name.replace(rupper, "-$1").toLowerCase();
                    var defaultView = elem.ownerDocument.defaultView;
                    if (!defaultView) {
                        "dk.brics.tajs.directives.unreachable";
                        return null;
                    }
                    var computedStyle = defaultView.getComputedStyle(elem, null);
                    if (computedStyle) {
                        "dk.brics.tajs.directives.unreachable";
                        ret = computedStyle.getPropertyValue(name);
                    }
                    // We should always get a number back from opacity
                    if (name === "opacity" && ret === "") {
                        "dk.brics.tajs.directives.unreachable";
                        ret = "1";
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.currentStyle) {
                        "dk.brics.tajs.directives.unreachable";
                        var camelCase = name.replace(rdashAlpha, fcamelCase);
                        ret = elem.currentStyle[name] || elem.currentStyle[camelCase];
                        // From the awesome hack by Dean Edwards
                        // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
                        // If we're not dealing with a regular pixel number
                        // but a number that has a weird ending, we need to convert it to pixels
                        if (!rnumpx.test(ret) && rnum.test(ret)) {
                            "dk.brics.tajs.directives.unreachable";
                            // Remember the original values
                            var left = style.left, rsLeft = elem.runtimeStyle.left;
                            // Put in the new values to get a computed value out
                            elem.runtimeStyle.left = elem.currentStyle.left;
                            style.left = camelCase === "fontSize" ? "1em" : ret || 0;
                            ret = style.pixelLeft + "px";
                            // Revert the changed values
                            style.left = left;
                            elem.runtimeStyle.left = rsLeft;
                        }
                    }
                }
            }
            return ret;
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
            for (var name in options) {
                "dk.brics.tajs.directives.unreachable";
                elem.style[name] = old[name];
            }
        }
    });
    if (jQuery.expr && jQuery.expr.filters) {
        jQuery.expr.filters.hidden = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            var width = elem.offsetWidth, height = elem.offsetHeight, skip = elem.nodeName.toLowerCase() === "tr";
            return width === 0 && height === 0 && !skip ? true : width > 0 && height > 0 && !skip ? false : jQuery.curCSS(elem, "display") === "none";
        };
        jQuery.expr.filters.visible = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return !jQuery.expr.filters.hidden(elem);
        };
    }
    var jsc = now(), rscript = /<script(.|\s)*?\/script>/gi, rselectTextarea = /select|textarea/i, rinput = /color|date|datetime|email|hidden|month|number|password|range|search|tel|text|time|url|week/i, jsre = /=\?(&|$)/, rquery = /\?/, rts = /(\?|&)_=.*?(&|$)/, rurl = /^(\w+:)?\/\/([^\/?#]+)/, r20 = /%20/g;
    jQuery.fn.extend({
        // Keep a copy of the old load
        _load: jQuery.fn.load,
        load: function(url, params, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof url !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return this._load(url);
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
            // Request the remote document
            jQuery.ajax({
                url: url,
                type: type,
                dataType: "html",
                data: params,
                context: this,
                complete: function(res, status) {
                    "dk.brics.tajs.directives.unreachable";
                    // If successful, inject the HTML into all the matched elements
                    if (status === "success" || status === "notmodified") {
                        "dk.brics.tajs.directives.unreachable";
                        // See if a selector was specified
                        this.html(selector ? // Create a dummy div to hold the results
                        jQuery("<div />").append(res.responseText.replace(rscript, "")).find(selector) : // If not, just inject the full result
                        res.responseText);
                    }
                    if (callback) {
                        "dk.brics.tajs.directives.unreachable";
                        this.each(callback, [ res.responseText, status, res ]);
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
                        value: val
                    };
                }) : {
                    name: elem.name,
                    value: val
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
    jQuery.extend({
        get: function(url, data, callback, type) {
            "dk.brics.tajs.directives.unreachable";
            // shift arguments if data argument was omited
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
                type = type || callback;
                callback = data;
                data = null;
            }
            return jQuery.ajax({
                type: "GET",
                url: url,
                data: data,
                success: callback,
                dataType: type
            });
        },
        getScript: function(url, callback) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.get(url, null, callback, "script");
        },
        getJSON: function(url, data, callback) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.get(url, data, callback, "json");
        },
        post: function(url, data, callback, type) {
            "dk.brics.tajs.directives.unreachable";
            // shift arguments if data argument was omited
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
                type = type || callback;
                callback = data;
                data = {};
            }
            return jQuery.ajax({
                type: "POST",
                url: url,
                data: data,
                success: callback,
                dataType: type
            });
        },
        ajaxSetup: function(settings) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.extend(jQuery.ajaxSettings, settings);
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
		username: null,
		password: null,
		traditional: false,
		*/
            // Create the request object; Microsoft failed to properly
            // implement the XMLHttpRequest in IE7 (can't request local files),
            // so we use the ActiveXObject when it is available
            // This function can be overriden by calling jQuery.ajaxSetup
            xhr: window.XMLHttpRequest && (window.location.protocol !== "file:" || !window.ActiveXObject) ? function() {
                "dk.brics.tajs.directives.unreachable";
                return new window.XMLHttpRequest();
            } : function() {
                "dk.brics.tajs.directives.unreachable";
                try {
                    "dk.brics.tajs.directives.unreachable";
                    return new window.ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {}
            },
            accepts: {
                xml: "application/xml, text/xml",
                html: "text/html",
                script: "text/javascript, application/javascript",
                json: "application/json, text/javascript",
                text: "text/plain",
                _default: "*/*"
            }
        },
        // Last-Modified header cache for next request
        lastModified: {},
        etag: {},
        ajax: function(origSettings) {
            "dk.brics.tajs.directives.unreachable";
            var s = jQuery.extend(true, {}, jQuery.ajaxSettings, origSettings);
            var jsonp, status, data, callbackContext = s.context || s, type = s.type.toUpperCase();
            // convert data if not already a string
            if (s.data && s.processData && typeof s.data !== "string") {
                "dk.brics.tajs.directives.unreachable";
                s.data = jQuery.param(s.data, s.traditional);
            }
            // Handle JSONP Parameter Callbacks
            if (s.dataType === "jsonp") {
                "dk.brics.tajs.directives.unreachable";
                if (type === "GET") {
                    "dk.brics.tajs.directives.unreachable";
                    if (!jsre.test(s.url)) {
                        "dk.brics.tajs.directives.unreachable";
                        s.url += (rquery.test(s.url) ? "&" : "?") + (s.jsonp || "callback") + "=?";
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (!s.data || !jsre.test(s.data)) {
                        "dk.brics.tajs.directives.unreachable";
                        s.data = (s.data ? s.data + "&" : "") + (s.jsonp || "callback") + "=?";
                    }
                }
                s.dataType = "json";
            }
            // Build temporary JSONP function
            if (s.dataType === "json" && (s.data && jsre.test(s.data) || jsre.test(s.url))) {
                "dk.brics.tajs.directives.unreachable";
                jsonp = s.jsonpCallback || "jsonp" + jsc++;
                // Replace the =? sequence both in the query string and the data
                if (s.data) {
                    "dk.brics.tajs.directives.unreachable";
                    s.data = (s.data + "").replace(jsre, "=" + jsonp + "$1");
                }
                s.url = s.url.replace(jsre, "=" + jsonp + "$1");
                // We need to make sure
                // that a JSONP style response is executed properly
                s.dataType = "script";
                // Handle JSONP-style loading
                window[jsonp] = window[jsonp] || function(tmp) {
                    "dk.brics.tajs.directives.unreachable";
                    data = tmp;
                    success();
                    complete();
                    // Garbage collect
                    window[jsonp] = undefined;
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        delete window[jsonp];
                    } catch (e) {}
                    if (head) {
                        "dk.brics.tajs.directives.unreachable";
                        head.removeChild(script);
                    }
                };
            }
            if (s.dataType === "script" && s.cache === null) {
                "dk.brics.tajs.directives.unreachable";
                s.cache = false;
            }
            if (s.cache === false && type === "GET") {
                "dk.brics.tajs.directives.unreachable";
                var ts = now();
                // try replacing _= if it is there
                var ret = s.url.replace(rts, "$1_=" + ts + "$2");
                // if nothing was replaced, add timestamp to the end
                s.url = ret + (ret === s.url ? (rquery.test(s.url) ? "&" : "?") + "_=" + ts : "");
            }
            // If data is available, append data to url for get requests
            if (s.data && type === "GET") {
                "dk.brics.tajs.directives.unreachable";
                s.url += (rquery.test(s.url) ? "&" : "?") + s.data;
            }
            // Watch for a new set of requests
            if (s.global && !jQuery.active++) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger("ajaxStart");
            }
            // Matches an absolute URL, and saves the domain
            var parts = rurl.exec(s.url), remote = parts && (parts[1] && parts[1] !== location.protocol || parts[2] !== location.host);
            // If we're requesting a remote document
            // and trying to load JSON or Script with a GET
            if (s.dataType === "script" && type === "GET" && remote) {
                "dk.brics.tajs.directives.unreachable";
                var head = document.getElementsByTagName("head")[0] || document.documentElement;
                var script = document.createElement("script");
                script.src = s.url;
                if (s.scriptCharset) {
                    "dk.brics.tajs.directives.unreachable";
                    script.charset = s.scriptCharset;
                }
                // Handle Script loading
                if (!jsonp) {
                    "dk.brics.tajs.directives.unreachable";
                    var done = false;
                    // Attach handlers for all browsers
                    script.onload = script.onreadystatechange = function() {
                        "dk.brics.tajs.directives.unreachable";
                        if (!done && (!this.readyState || this.readyState === "loaded" || this.readyState === "complete")) {
                            "dk.brics.tajs.directives.unreachable";
                            done = true;
                            success();
                            complete();
                            // Handle memory leak in IE
                            script.onload = script.onreadystatechange = null;
                            if (head && script.parentNode) {
                                "dk.brics.tajs.directives.unreachable";
                                head.removeChild(script);
                            }
                        }
                    };
                }
                // Use insertBefore instead of appendChild  to circumvent an IE6 bug.
                // This arises when a base node is used (#2709 and #4378).
                head.insertBefore(script, head.firstChild);
                // We handle everything using the script element injection
                return undefined;
            }
            var requestDone = false;
            // Create the request object
            var xhr = s.xhr();
            if (!xhr) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // Open the socket
            // Passing null username, generates a login popup on Opera (#2865)
            if (s.username) {
                "dk.brics.tajs.directives.unreachable";
                xhr.open(type, s.url, s.async, s.username, s.password);
            } else {
                "dk.brics.tajs.directives.unreachable";
                xhr.open(type, s.url, s.async);
            }
            // Need an extra try/catch for cross domain requests in Firefox 3
            try {
                "dk.brics.tajs.directives.unreachable";
                // Set the correct header, if data is being sent
                if (s.data || origSettings && origSettings.contentType) {
                    "dk.brics.tajs.directives.unreachable";
                    xhr.setRequestHeader("Content-Type", s.contentType);
                }
                // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
                if (s.ifModified) {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.lastModified[s.url]) {
                        "dk.brics.tajs.directives.unreachable";
                        xhr.setRequestHeader("If-Modified-Since", jQuery.lastModified[s.url]);
                    }
                    if (jQuery.etag[s.url]) {
                        "dk.brics.tajs.directives.unreachable";
                        xhr.setRequestHeader("If-None-Match", jQuery.etag[s.url]);
                    }
                }
                // Set header so the called script knows that it's an XMLHttpRequest
                // Only send the header if it's not a remote XHR
                if (!remote) {
                    "dk.brics.tajs.directives.unreachable";
                    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                }
                // Set the Accepts header for the server, depending on the dataType
                xhr.setRequestHeader("Accept", s.dataType && s.accepts[s.dataType] ? s.accepts[s.dataType] + ", */*" : s.accepts._default);
            } catch (e) {}
            // Allow custom headers/mimetypes and early abort
            if (s.beforeSend && s.beforeSend.call(callbackContext, xhr, s) === false) {
                "dk.brics.tajs.directives.unreachable";
                // Handle the global AJAX counter
                if (s.global && !--jQuery.active) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger("ajaxStop");
                }
                // close opended socket
                xhr.abort();
                return false;
            }
            if (s.global) {
                "dk.brics.tajs.directives.unreachable";
                trigger("ajaxSend", [ xhr, s ]);
            }
            // Wait for a response to come back
            var onreadystatechange = xhr.onreadystatechange = function(isTimeout) {
                "dk.brics.tajs.directives.unreachable";
                // The request was aborted
                if (!xhr || xhr.readyState === 0) {
                    "dk.brics.tajs.directives.unreachable";
                    // Opera doesn't call onreadystatechange before this point
                    // so we simulate the call
                    if (!requestDone) {
                        "dk.brics.tajs.directives.unreachable";
                        complete();
                    }
                    requestDone = true;
                    if (xhr) {
                        "dk.brics.tajs.directives.unreachable";
                        xhr.onreadystatechange = jQuery.noop;
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (!requestDone && xhr && (xhr.readyState === 4 || isTimeout === "timeout")) {
                        "dk.brics.tajs.directives.unreachable";
                        requestDone = true;
                        xhr.onreadystatechange = jQuery.noop;
                        status = isTimeout === "timeout" ? "timeout" : !jQuery.httpSuccess(xhr) ? "error" : s.ifModified && jQuery.httpNotModified(xhr, s.url) ? "notmodified" : "success";
                        if (status === "success") {
                            "dk.brics.tajs.directives.unreachable";
                            // Watch for, and catch, XML document parse errors
                            try {
                                "dk.brics.tajs.directives.unreachable";
                                // process the data (runs the xml through httpData regardless of callback)
                                data = jQuery.httpData(xhr, s.dataType, s);
                            } catch (e) {
                                "dk.brics.tajs.directives.unreachable";
                                status = "parsererror";
                            }
                        }
                        // Make sure that the request was successful or notmodified
                        if (status === "success" || status === "notmodified") {
                            "dk.brics.tajs.directives.unreachable";
                            // JSONP handles its own success callback
                            if (!jsonp) {
                                "dk.brics.tajs.directives.unreachable";
                                success();
                            }
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.handleError(s, xhr, status);
                        }
                        // Fire the complete handlers
                        complete();
                        if (isTimeout === "timeout") {
                            "dk.brics.tajs.directives.unreachable";
                            xhr.abort();
                        }
                        // Stop memory leaks
                        if (s.async) {
                            "dk.brics.tajs.directives.unreachable";
                            xhr = null;
                        }
                    }
                }
            };
            // Override the abort handler, if we can (IE doesn't allow it, but that's OK)
            // Opera doesn't fire onreadystatechange at all on abort
            try {
                "dk.brics.tajs.directives.unreachable";
                var oldAbort = xhr.abort;
                xhr.abort = function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (xhr) {
                        "dk.brics.tajs.directives.unreachable";
                        oldAbort.call(xhr);
                        if (xhr) {
                            "dk.brics.tajs.directives.unreachable";
                            xhr.readyState = 0;
                        }
                    }
                    onreadystatechange();
                };
            } catch (e) {}
            // Timeout checker
            if (s.async && s.timeout > 0) {
                "dk.brics.tajs.directives.unreachable";
                setTimeout(function() {
                    "dk.brics.tajs.directives.unreachable";
                    // Check to see if the request is still happening
                    if (xhr && !requestDone) {
                        "dk.brics.tajs.directives.unreachable";
                        onreadystatechange("timeout");
                    }
                }, s.timeout);
            }
            // Send the data
            try {
                "dk.brics.tajs.directives.unreachable";
                xhr.send(type === "POST" || type === "PUT" || type === "DELETE" ? s.data : null);
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.handleError(s, xhr, null, e);
                // Fire the complete handlers
                complete();
            }
            // firefox 1.5 doesn't fire statechange for sync requests
            if (!s.async) {
                "dk.brics.tajs.directives.unreachable";
                onreadystatechange();
            }
            function success() {
                "dk.brics.tajs.directives.unreachable";
                // If a local callback was specified, fire it and pass it the data
                if (s.success) {
                    "dk.brics.tajs.directives.unreachable";
                    s.success.call(callbackContext, data, status, xhr);
                }
                // Fire the global callback
                if (s.global) {
                    "dk.brics.tajs.directives.unreachable";
                    trigger("ajaxSuccess", [ xhr, s ]);
                }
            }
            function complete() {
                "dk.brics.tajs.directives.unreachable";
                // Process result
                if (s.complete) {
                    "dk.brics.tajs.directives.unreachable";
                    s.complete.call(callbackContext, xhr, status);
                }
                // The request was completed
                if (s.global) {
                    "dk.brics.tajs.directives.unreachable";
                    trigger("ajaxComplete", [ xhr, s ]);
                }
                // Handle the global AJAX counter
                if (s.global && !--jQuery.active) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger("ajaxStop");
                }
            }
            function trigger(type, args) {
                "dk.brics.tajs.directives.unreachable";
                (s.context ? jQuery(s.context) : jQuery.event).trigger(type, args);
            }
            // return XMLHttpRequest to allow aborting the request etc.
            return xhr;
        },
        handleError: function(s, xhr, status, e) {
            "dk.brics.tajs.directives.unreachable";
            // If a local callback was specified, fire it
            if (s.error) {
                "dk.brics.tajs.directives.unreachable";
                s.error.call(s.context || window, xhr, status, e);
            }
            // Fire the global callback
            if (s.global) {
                "dk.brics.tajs.directives.unreachable";
                (s.context ? jQuery(s.context) : jQuery.event).trigger("ajaxError", [ xhr, s, e ]);
            }
        },
        // Counter for holding the number of active queries
        active: 0,
        // Determines if an XMLHttpRequest was successful or not
        httpSuccess: function(xhr) {
            "dk.brics.tajs.directives.unreachable";
            try {
                "dk.brics.tajs.directives.unreachable";
                // IE error sometimes returns 1223 when it should be 204 so treat it as success, see #1450
                return !xhr.status && location.protocol === "file:" || // Opera returns 0 when status is 304
                xhr.status >= 200 && xhr.status < 300 || xhr.status === 304 || xhr.status === 1223 || xhr.status === 0;
            } catch (e) {}
            return false;
        },
        // Determines if an XMLHttpRequest returns NotModified
        httpNotModified: function(xhr, url) {
            "dk.brics.tajs.directives.unreachable";
            var lastModified = xhr.getResponseHeader("Last-Modified"), etag = xhr.getResponseHeader("Etag");
            if (lastModified) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.lastModified[url] = lastModified;
            }
            if (etag) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.etag[url] = etag;
            }
            // Opera returns 0 when status is 304
            return xhr.status === 304 || xhr.status === 0;
        },
        httpData: function(xhr, type, s) {
            "dk.brics.tajs.directives.unreachable";
            var ct = xhr.getResponseHeader("content-type") || "", xml = type === "xml" || !type && ct.indexOf("xml") >= 0, data = xml ? xhr.responseXML : xhr.responseText;
            if (xml && data.documentElement.nodeName === "parsererror") {
                "dk.brics.tajs.directives.unreachable";
                throw "parsererror";
            }
            // Allow a pre-filtering function to sanitize the response
            // s is checked to keep backwards compatibility
            if (s && s.dataFilter) {
                "dk.brics.tajs.directives.unreachable";
                data = s.dataFilter(data, type);
            }
            // The filter can actually parse the response
            if (typeof data === "string") {
                "dk.brics.tajs.directives.unreachable";
                // Get the JavaScript object, if JSON is used.
                if (type === "json" || !type && ct.indexOf("json") >= 0) {
                    "dk.brics.tajs.directives.unreachable";
                    // Make sure the incoming data is actual JSON
                    // Logic borrowed from http://json.org/json2.js
                    if (/^[\],:{}\s]*$/.test(data.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, ""))) {
                        "dk.brics.tajs.directives.unreachable";
                        // Try to use the native JSON parser first
                        if (window.JSON && window.JSON.parse) {
                            "dk.brics.tajs.directives.unreachable";
                            data = window.JSON.parse(data);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            data = new Function("return " + data)();
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        throw "Invalid JSON: " + data;
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (type === "script" || !type && ct.indexOf("javascript") >= 0) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.globalEval(data);
                    }
                }
            }
            return data;
        },
        // Serialize an array of form elements or a set of
        // key/values into a query string
        param: function(a, traditional) {
            "dk.brics.tajs.directives.unreachable";
            var s = [];
            // Set traditional to true for jQuery <= 1.3.2 behavior.
            if (traditional === undefined) {
                "dk.brics.tajs.directives.unreachable";
                traditional = jQuery.ajaxSettings.traditional;
            }
            function add(key, value) {
                "dk.brics.tajs.directives.unreachable";
                // If value is a function, invoke it and return its value
                value = jQuery.isFunction(value) ? value() : value;
                s[s.length] = encodeURIComponent(key) + "=" + encodeURIComponent(value);
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
                jQuery.each(a, function buildParams(prefix, obj) {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.isArray(obj)) {
                        "dk.brics.tajs.directives.unreachable";
                        // Serialize array item.
                        jQuery.each(obj, function(i, v) {
                            "dk.brics.tajs.directives.unreachable";
                            if (traditional) {
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
                                buildParams(prefix + "[" + (typeof v === "object" || jQuery.isArray(v) ? i : "") + "]", v);
                            }
                        });
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (!traditional && obj != null && typeof obj === "object") {
                            "dk.brics.tajs.directives.unreachable";
                            // Serialize object item.
                            jQuery.each(obj, function(k, v) {
                                "dk.brics.tajs.directives.unreachable";
                                buildParams(prefix + "[" + k + "]", v);
                            });
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // Serialize scalar item.
                            add(prefix, obj);
                        }
                    }
                });
            }
            // Return the resulting serialization
            return s.join("&").replace(r20, "+");
        }
    });
    var elemdisplay = {}, rfxtypes = /toggle|show|hide/, rfxnum = /^([+-]=)?([\d+-.]+)(.*)$/, timerId, fxAttrs = [ // height animations
    [ "height", "marginTop", "marginBottom", "paddingTop", "paddingBottom" ], // width animations
    [ "width", "marginLeft", "marginRight", "paddingLeft", "paddingRight" ], // opacity animations
    [ "opacity" ] ];
    jQuery.fn.extend({
        show: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (speed != null) {
                "dk.brics.tajs.directives.unreachable";
                return this.animate(genFx("show", 3), speed, callback);
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var old = jQuery.data(this[i], "olddisplay");
                    this[i].style.display = old || "";
                    if (jQuery.css(this[i], "display") === "none") {
                        "dk.brics.tajs.directives.unreachable";
                        var nodeName = this[i].nodeName, display;
                        if (elemdisplay[nodeName]) {
                            "dk.brics.tajs.directives.unreachable";
                            display = elemdisplay[nodeName];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            var elem = jQuery("<" + nodeName + " />").appendTo("body");
                            display = elem.css("display");
                            if (display === "none") {
                                "dk.brics.tajs.directives.unreachable";
                                display = "block";
                            }
                            elem.remove();
                            elemdisplay[nodeName] = display;
                        }
                        jQuery.data(this[i], "olddisplay", display);
                    }
                }
                // Set the display of the elements in a second loop
                // to avoid the constant reflow
                for (var j = 0, k = this.length; j < k; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    this[j].style.display = jQuery.data(this[j], "olddisplay") || "";
                }
                return this;
            }
        },
        hide: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (speed != null) {
                "dk.brics.tajs.directives.unreachable";
                return this.animate(genFx("hide", 3), speed, callback);
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, l = this.length; i < l; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var old = jQuery.data(this[i], "olddisplay");
                    if (!old && old !== "none") {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.data(this[i], "olddisplay", jQuery.css(this[i], "display"));
                    }
                }
                // Set the display of the elements in a second loop
                // to avoid the constant reflow
                for (var j = 0, k = this.length; j < k; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    this[j].style.display = "none";
                }
                return this;
            }
        },
        // Save the old toggle function
        _toggle: jQuery.fn.toggle,
        toggle: function(fn, fn2) {
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
                    this.animate(genFx("toggle", 3), fn, fn2);
                }
            }
            return this;
        },
        fadeTo: function(speed, to, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.filter(":hidden").css("opacity", 0).show().end().animate({
                opacity: to
            }, speed, callback);
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
                var opt = jQuery.extend({}, optall), p, hidden = this.nodeType === 1 && jQuery(this).is(":hidden"), self = this;
                for (p in prop) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = p.replace(rdashAlpha, fcamelCase);
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
                    if ((p === "height" || p === "width") && this.style) {
                        "dk.brics.tajs.directives.unreachable";
                        // Store display property
                        opt.display = jQuery.css(this, "display");
                        // Make sure that nothing sneaks out
                        opt.overflow = this.style.overflow;
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
                        var parts = rfxnum.exec(val), start = e.cur(true) || 0;
                        if (parts) {
                            "dk.brics.tajs.directives.unreachable";
                            var end = parseFloat(parts[2]), unit = parts[3] || "px";
                            // We need to compute starting value
                            if (unit !== "px") {
                                "dk.brics.tajs.directives.unreachable";
                                self.style[name] = (end || 1) + unit;
                                start = (end || 1) / e.cur(true) * start;
                                self.style[name] = start + unit;
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
        }
    }, function(name, props) {
        jQuery.fn[name] = function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate(props, speed, callback);
        };
    });
    jQuery.extend({
        speed: function(speed, easing, fn) {
            "dk.brics.tajs.directives.unreachable";
            var opt = speed && typeof speed === "object" ? speed : {
                complete: fn || !fn && easing || jQuery.isFunction(speed) && speed,
                duration: speed,
                easing: fn && easing || easing && !jQuery.isFunction(easing) && easing
            };
            opt.duration = jQuery.fx.off ? 0 : typeof opt.duration === "number" ? opt.duration : jQuery.fx.speeds[opt.duration] || jQuery.fx.speeds._default;
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
            // Set display property to block for height/width animations
            if ((this.prop === "height" || this.prop === "width") && this.elem.style) {
                "dk.brics.tajs.directives.unreachable";
                this.elem.style.display = "block";
            }
        },
        // Get the current size
        cur: function(force) {
            "dk.brics.tajs.directives.unreachable";
            if (this.elem[this.prop] != null && (!this.elem.style || this.elem.style[this.prop] == null)) {
                "dk.brics.tajs.directives.unreachable";
                return this.elem[this.prop];
            }
            var r = parseFloat(jQuery.css(this.elem, this.prop, force));
            return r && r > -1e4 ? r : parseFloat(jQuery.curCSS(this.elem, this.prop)) || 0;
        },
        // Start an animation from one number to another
        custom: function(from, to, unit) {
            "dk.brics.tajs.directives.unreachable";
            this.startTime = now();
            this.start = from;
            this.end = to;
            this.unit = unit || this.unit || "px";
            this.now = this.start;
            this.pos = this.state = 0;
            var self = this;
            function t(gotoEnd) {
                "dk.brics.tajs.directives.unreachable";
                return self.step(gotoEnd);
            }
            t.elem = this.elem;
            if (t() && jQuery.timers.push(t) && !timerId) {
                "dk.brics.tajs.directives.unreachable";
                timerId = setInterval(jQuery.fx.tick, 13);
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
            var t = now(), done = true;
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
                    if (this.options.display != null) {
                        "dk.brics.tajs.directives.unreachable";
                        // Reset the overflow
                        this.elem.style.overflow = this.options.overflow;
                        // Reset the display
                        var old = jQuery.data(this.elem, "olddisplay");
                        this.elem.style.display = old ? old : this.options.display;
                        if (jQuery.css(this.elem, "display") === "none") {
                            "dk.brics.tajs.directives.unreachable";
                            this.elem.style.display = "block";
                        }
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
    function genFx(type, num) {
        var obj = {};
        jQuery.each(fxAttrs.concat.apply([], fxAttrs.slice(0, num)), function() {
            obj[this] = type;
        });
        return obj;
    }
    if ("getBoundingClientRect" in document.documentElement) {
        jQuery.fn.offset = function(options) {
            "dk.brics.tajs.directives.unreachable";
            var elem = this[0];
            if (!elem || !elem.ownerDocument) {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            if (options) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.offset.setOffset(this, options, i);
                });
            }
            if (elem === elem.ownerDocument.body) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.offset.bodyOffset(elem);
            }
            var box = elem.getBoundingClientRect(), doc = elem.ownerDocument, body = doc.body, docElem = doc.documentElement, clientTop = docElem.clientTop || body.clientTop || 0, clientLeft = docElem.clientLeft || body.clientLeft || 0, top = box.top + (self.pageYOffset || jQuery.support.boxModel && docElem.scrollTop || body.scrollTop) - clientTop, left = box.left + (self.pageXOffset || jQuery.support.boxModel && docElem.scrollLeft || body.scrollLeft) - clientLeft;
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
            if (!elem || !elem.ownerDocument) {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            if (options) {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.offset.setOffset(this, options, i);
                });
            }
            if (elem === elem.ownerDocument.body) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.offset.bodyOffset(elem);
            }
            jQuery.offset.initialize();
            var offsetParent = elem.offsetParent, prevOffsetParent = elem, doc = elem.ownerDocument, computedStyle, docElem = doc.documentElement, body = doc.body, defaultView = doc.defaultView, prevComputedStyle = defaultView ? defaultView.getComputedStyle(elem, null) : elem.currentStyle, top = elem.offsetTop, left = elem.offsetLeft;
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
                    if (jQuery.offset.doesNotAddBorder && !(jQuery.offset.doesAddBorderForTableAndCells && /^t(able|d|h)$/i.test(elem.nodeName))) {
                        "dk.brics.tajs.directives.unreachable";
                        top += parseFloat(computedStyle.borderTopWidth) || 0;
                        left += parseFloat(computedStyle.borderLeftWidth) || 0;
                    }
                    prevOffsetParent = offsetParent, offsetParent = elem.offsetParent;
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
            var body = document.body, container = document.createElement("div"), innerDiv, checkDiv, table, td, bodyMarginTop = parseFloat(jQuery.curCSS(body, "marginTop", true)) || 0, html = "<div style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;'><div></div></div><table style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;' cellpadding='0' cellspacing='0'><tr><td></td></tr></table>";
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
            checkDiv.style.position = "fixed", checkDiv.style.top = "20px";
            // safari subtracts parent border width here which is 5px
            this.supportsFixedPosition = checkDiv.offsetTop === 20 || checkDiv.offsetTop === 15;
            checkDiv.style.position = checkDiv.style.top = "";
            innerDiv.style.overflow = "hidden", innerDiv.style.position = "relative";
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
                top += parseFloat(jQuery.curCSS(body, "marginTop", true)) || 0;
                left += parseFloat(jQuery.curCSS(body, "marginLeft", true)) || 0;
            }
            return {
                top: top,
                left: left
            };
        },
        setOffset: function(elem, options, i) {
            "dk.brics.tajs.directives.unreachable";
            // set position first, in-case top/left are set even on static elem
            if (/static/.test(jQuery.curCSS(elem, "position"))) {
                "dk.brics.tajs.directives.unreachable";
                elem.style.position = "relative";
            }
            var curElem = jQuery(elem), curOffset = curElem.offset(), curTop = parseInt(jQuery.curCSS(elem, "top", true), 10) || 0, curLeft = parseInt(jQuery.curCSS(elem, "left", true), 10) || 0;
            if (jQuery.isFunction(options)) {
                "dk.brics.tajs.directives.unreachable";
                options = options.call(elem, i, curOffset);
            }
            var props = {
                top: options.top - curOffset.top + curTop,
                left: options.left - curOffset.left + curLeft
            };
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
            offset = this.offset(), parentOffset = /^body|html$/i.test(offsetParent[0].nodeName) ? {
                top: 0,
                left: 0
            } : offsetParent.offset();
            // Subtract element margins
            // note: when an element has margin: auto the offsetLeft and marginLeft
            // are the same in Safari causing offset.left to incorrectly be 0
            offset.top -= parseFloat(jQuery.curCSS(elem, "marginTop", true)) || 0;
            offset.left -= parseFloat(jQuery.curCSS(elem, "marginLeft", true)) || 0;
            // Add offsetParent borders
            parentOffset.top += parseFloat(jQuery.curCSS(offsetParent[0], "borderTopWidth", true)) || 0;
            parentOffset.left += parseFloat(jQuery.curCSS(offsetParent[0], "borderLeftWidth", true)) || 0;
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
                while (offsetParent && !/^body|html$/i.test(offsetParent.nodeName) && jQuery.css(offsetParent, "position") === "static") {
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
        return "scrollTo" in elem && elem.document ? elem : elem.nodeType === 9 ? elem.defaultView || elem.parentWindow : false;
    }
    // Create innerHeight, innerWidth, outerHeight and outerWidth methods
    jQuery.each([ "Height", "Width" ], function(i, name) {
        var type = name.toLowerCase();
        // innerHeight and innerWidth
        jQuery.fn["inner" + name] = function() {
            "dk.brics.tajs.directives.unreachable";
            return this[0] ? jQuery.css(this[0], type, false, "padding") : null;
        };
        // outerHeight and outerWidth
        jQuery.fn["outer" + name] = function(margin) {
            "dk.brics.tajs.directives.unreachable";
            return this[0] ? jQuery.css(this[0], type, false, margin ? "margin" : "border") : null;
        };
        jQuery.fn[type] = function(size) {
            "dk.brics.tajs.directives.unreachable";
            // Get window width or height
            var elem = this[0];
            if (!elem) {
                "dk.brics.tajs.directives.unreachable";
                return size == null ? null : this;
            }
            return "scrollTo" in elem && elem.document ? // does it walk and quack like a window?
            // Everyone else use document.documentElement or document.body depending on Quirks vs Standards mode
            elem.document.compatMode === "CSS1Compat" && elem.document.documentElement["client" + name] || elem.document.body["client" + name] : // Get document width or height
            elem.nodeType === 9 ? // is it a document
            // Either scroll[Width/Height] or offset[Width/Height], whichever is greater
            Math.max(elem.documentElement["client" + name], elem.body["scroll" + name], elem.documentElement["scroll" + name], elem.body["offset" + name], elem.documentElement["offset" + name]) : // Get or set width or height on the element
            size === undefined ? // Get width or height on the element
            jQuery.css(elem, type) : // Set the width or height on the element (default to pixels if value is unitless)
            this.css(type, typeof size === "string" ? size : size + "px");
        };
    });
    // Expose jQuery to the global object
    window.jQuery = window.$ = jQuery;
})(window);
