/*!
 * jQuery JavaScript Library v1.3
 * http://jquery.com/
 *
 * Copyright (c) 2009 John Resig
 * Dual licensed under the MIT and GPL licenses.
 * http://docs.jquery.com/License
 *
 * Date: 
 * Revision: 
 */
(function() {
    var // Will speed up references to window, and allows munging its name.
    window = this, // Will speed up references to undefined, and allows munging its name.
    undefined, // Map over jQuery in case of overwrite
    _jQuery = window.jQuery, // Map over the $ in case of overwrite
    _$ = window.$, jQuery = window.jQuery = window.$ = function(selector, context) {
        // The jQuery object is actually just the init constructor 'enhanced'
        return new jQuery.fn.init(selector, context);
    }, // A simple way to check for HTML strings or ID strings
    // (both of which we optimize for)
    quickExpr = /^[^<]*(<(.|\s)+>)[^>]*$|^#([\w-]+)$/, // Is it a simple selector
    isSimple = /^.[^:#\[\.,]*$/;
    jQuery.fn = jQuery.prototype = {
        init: function(selector, context) {
            // Make sure that a selection was provided
            selector = selector || document;
            // Handle $(DOMElement)
            if (selector.nodeType) {
                this[0] = selector;
                this.length = 1;
                this.context = selector;
                return this;
            }
            // Handle HTML strings
            if (typeof selector === "string") {
                "dk.brics.tajs.directives.unreachable";
                // Are we dealing with HTML string or an ID?
                var match = quickExpr.exec(selector);
                // Verify a match, and that no context was specified for #id
                if (match && (match[1] || !context)) {
                    "dk.brics.tajs.directives.unreachable";
                    // HANDLE: $(html) -> $(array)
                    if (match[1]) {
                        "dk.brics.tajs.directives.unreachable";
                        selector = jQuery.clean([ match[1] ], context);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        var elem = document.getElementById(match[3]);
                        // Make sure an element was located
                        if (elem) {
                            "dk.brics.tajs.directives.unreachable";
                            // Handle the case where IE and Opera return items
                            // by name instead of ID
                            if (elem.id != match[3]) {
                                "dk.brics.tajs.directives.unreachable";
                                return jQuery().find(selector);
                            }
                            // Otherwise, we inject the element directly into the jQuery object
                            var ret = jQuery(elem);
                            ret.context = document;
                            ret.selector = selector;
                            return ret;
                        }
                        selector = [];
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return jQuery(context).find(selector);
                }
            } else {
                if (jQuery.isFunction(selector)) {
                    return jQuery(document).ready(selector);
                }
            }
            // Make sure that old selector state is passed along
            if (selector.selector && selector.context) {
                "dk.brics.tajs.directives.unreachable";
                this.selector = selector.selector;
                this.context = selector.context;
            }
            return this.setArray(jQuery.makeArray(selector));
        },
        // Start with an empty selector
        selector: "",
        // The current version of jQuery being used
        jquery: "1.3",
        // The number of elements contained in the matched element set
        size: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.length;
        },
        // Get the Nth element in the matched element set OR
        // Get the whole matched element set as a clean array
        get: function(num) {
            "dk.brics.tajs.directives.unreachable";
            return num === undefined ? // Return a 'clean' array
            jQuery.makeArray(this) : // Return just the object
            this[num];
        },
        // Take an array of elements and push it onto the stack
        // (returning the new matched element set)
        pushStack: function(elems, name, selector) {
            "dk.brics.tajs.directives.unreachable";
            // Build a new jQuery matched element set
            var ret = jQuery(elems);
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
            // Resetting the length to 0, then using the native Array push
            // is a super-fast way to populate an object with array-like properties
            this.length = 0;
            Array.prototype.push.apply(this, elems);
            return this;
        },
        // Execute a callback for every element in the matched set.
        // (You can seed the arguments with an array of args, but this is
        // only used internally.)
        each: function(callback, args) {
            return jQuery.each(this, callback, args);
        },
        // Determine the position of an element within
        // the matched set of elements
        index: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            // Locate the position of the desired element
            return jQuery.inArray(// If it receives a jQuery object, the first element is used
            elem && elem.jquery ? elem[0] : elem, this);
        },
        attr: function(name, value, type) {
            "dk.brics.tajs.directives.unreachable";
            var options = name;
            // Look for the case where we're accessing a style value
            if (typeof name === "string") {
                "dk.brics.tajs.directives.unreachable";
                if (value === undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    return this[0] && jQuery[type || "attr"](this[0], name);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    options = {};
                    options[name] = value;
                }
            }
            // Check to see if we're setting style values
            return this.each(function(i) {
                "dk.brics.tajs.directives.unreachable";
                // Set all the styles
                for (name in options) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.attr(type ? this.style : this, name, jQuery.prop(this, options[name], type, i, name));
                }
            });
        },
        css: function(key, value) {
            "dk.brics.tajs.directives.unreachable";
            // ignore negative width and height values
            if ((key == "width" || key == "height") && parseFloat(value) < 0) {
                "dk.brics.tajs.directives.unreachable";
                value = undefined;
            }
            return this.attr(key, value, "curCSS");
        },
        text: function(text) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof text !== "object" && text != null) {
                "dk.brics.tajs.directives.unreachable";
                return this.empty().append((this[0] && this[0].ownerDocument || document).createTextNode(text));
            }
            var ret = "";
            jQuery.each(text || this, function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.each(this.childNodes, function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (this.nodeType != 8) {
                        "dk.brics.tajs.directives.unreachable";
                        ret += this.nodeType != 1 ? this.nodeValue : jQuery.fn.text([ this ]);
                    }
                });
            });
            return ret;
        },
        wrapAll: function(html) {
            "dk.brics.tajs.directives.unreachable";
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                // The elements to wrap the target around
                var wrap = jQuery(html, this[0].ownerDocument).clone();
                if (this[0].parentNode) {
                    "dk.brics.tajs.directives.unreachable";
                    wrap.insertBefore(this[0]);
                }
                wrap.map(function() {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = this;
                    while (elem.firstChild) {
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
                jQuery(this).contents().wrapAll(html);
            });
        },
        wrap: function(html) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery(this).wrapAll(html);
            });
        },
        append: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, true, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeType == 1) {
                    "dk.brics.tajs.directives.unreachable";
                    this.appendChild(elem);
                }
            });
        },
        prepend: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, true, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeType == 1) {
                    "dk.brics.tajs.directives.unreachable";
                    this.insertBefore(elem, this.firstChild);
                }
            });
        },
        before: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, false, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                this.parentNode.insertBefore(elem, this);
            });
        },
        after: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, false, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                this.parentNode.insertBefore(elem, this.nextSibling);
            });
        },
        end: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.prevObject || jQuery([]);
        },
        // For internal use only.
        // Behaves like an Array's .push method, not like a jQuery method.
        push: [].push,
        find: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            if (this.length === 1 && !/,/.test(selector)) {
                "dk.brics.tajs.directives.unreachable";
                var ret = this.pushStack([], "find", selector);
                ret.length = 0;
                jQuery.find(selector, this[0], ret);
                return ret;
            } else {
                "dk.brics.tajs.directives.unreachable";
                var elems = jQuery.map(this, function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return jQuery.find(selector, elem);
                });
                return this.pushStack(/[^+>] [^+>]/.test(selector) ? jQuery.unique(elems) : elems, "find", selector);
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
                    var clone = this.cloneNode(true), container = document.createElement("div");
                    container.appendChild(clone);
                    return jQuery.clean([ container.innerHTML ])[0];
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return this.cloneNode(true);
                }
            });
            // Need to set the expando to null on the cloned set if it exists
            // removeData doesn't work here, IE removes it from the original as well
            // this is primarily for IE but the data expando shouldn't be copied over in any browser
            var clone = ret.find("*").andSelf().each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (this[expando] !== undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    this[expando] = null;
                }
            });
            // Copy the events from the original to the clone
            if (events === true) {
                "dk.brics.tajs.directives.unreachable";
                this.find("*").andSelf().each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
                    if (this.nodeType == 3) {
                        "dk.brics.tajs.directives.unreachable";
                        return;
                    }
                    var events = jQuery.data(this, "events");
                    for (var type in events) {
                        "dk.brics.tajs.directives.unreachable";
                        for (var handler in events[type]) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.event.add(clone[i], type, events[type][handler], events[type][handler].data);
                        }
                    }
                });
            }
            // Return the cloned set
            return ret;
        },
        filter: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(jQuery.isFunction(selector) && jQuery.grep(this, function(elem, i) {
                "dk.brics.tajs.directives.unreachable";
                return selector.call(elem, i);
            }) || jQuery.multiFilter(selector, jQuery.grep(this, function(elem) {
                "dk.brics.tajs.directives.unreachable";
                return elem.nodeType === 1;
            })), "filter", selector);
        },
        closest: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            var pos = jQuery.expr.match.POS.test(selector) ? jQuery(selector) : null;
            return this.map(function() {
                "dk.brics.tajs.directives.unreachable";
                var cur = this;
                while (cur && cur.ownerDocument) {
                    "dk.brics.tajs.directives.unreachable";
                    if (pos ? pos.index(cur) > -1 : jQuery(cur).is(selector)) {
                        "dk.brics.tajs.directives.unreachable";
                        return cur;
                    }
                    cur = cur.parentNode;
                }
            });
        },
        not: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof selector === "string") // test special case where just one selector is passed in
            {
                "dk.brics.tajs.directives.unreachable";
                if (isSimple.test(selector)) {
                    "dk.brics.tajs.directives.unreachable";
                    return this.pushStack(jQuery.multiFilter(selector, this, true), "not", selector);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    selector = jQuery.multiFilter(selector, this);
                }
            }
            var isArrayLike = selector.length && selector[selector.length - 1] !== undefined && !selector.nodeType;
            return this.filter(function() {
                "dk.brics.tajs.directives.unreachable";
                return isArrayLike ? jQuery.inArray(this, selector) < 0 : this != selector;
            });
        },
        add: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(jQuery.unique(jQuery.merge(this.get(), typeof selector === "string" ? jQuery(selector) : jQuery.makeArray(selector))));
        },
        is: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return !!selector && jQuery.multiFilter(selector, this).length > 0;
        },
        hasClass: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            return !!selector && this.is("." + selector);
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
                        var index = elem.selectedIndex, values = [], options = elem.options, one = elem.type == "select-one";
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
                    // Everything else, we just grab the value
                    return (elem.value || "").replace(/\r/g, "");
                }
                return undefined;
            }
            if (typeof value === "number") {
                "dk.brics.tajs.directives.unreachable";
                value += "";
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (this.nodeType != 1) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                if (jQuery.isArray(value) && /radio|checkbox/.test(this.type)) {
                    "dk.brics.tajs.directives.unreachable";
                    this.checked = jQuery.inArray(this.value, value) >= 0 || jQuery.inArray(this.name, value) >= 0;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.nodeName(this, "select")) {
                        "dk.brics.tajs.directives.unreachable";
                        var values = jQuery.makeArray(value);
                        jQuery("option", this).each(function() {
                            "dk.brics.tajs.directives.unreachable";
                            this.selected = jQuery.inArray(this.value, values) >= 0 || jQuery.inArray(this.text, values) >= 0;
                        });
                        if (!values.length) {
                            "dk.brics.tajs.directives.unreachable";
                            this.selectedIndex = -1;
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        this.value = value;
                    }
                }
            });
        },
        html: function(value) {
            "dk.brics.tajs.directives.unreachable";
            return value === undefined ? this[0] ? this[0].innerHTML : null : this.empty().append(value);
        },
        replaceWith: function(value) {
            "dk.brics.tajs.directives.unreachable";
            return this.after(value).remove();
        },
        eq: function(i) {
            "dk.brics.tajs.directives.unreachable";
            return this.slice(i, +i + 1);
        },
        slice: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(Array.prototype.slice.apply(this, arguments), "slice", Array.prototype.slice.call(arguments).join(","));
        },
        map: function(callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(jQuery.map(this, function(elem, i) {
                "dk.brics.tajs.directives.unreachable";
                return callback.call(elem, i, elem);
            }));
        },
        andSelf: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.add(this.prevObject);
        },
        domManip: function(args, table, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                var fragment = (this[0].ownerDocument || this[0]).createDocumentFragment(), scripts = jQuery.clean(args, this[0].ownerDocument || this[0], fragment), first = fragment.firstChild, extra = this.length > 1 ? fragment.cloneNode(true) : fragment;
                if (first) {
                    "dk.brics.tajs.directives.unreachable";
                    for (var i = 0, l = this.length; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        callback.call(root(this[i], first), i > 0 ? extra.cloneNode(true) : fragment);
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
                return table && jQuery.nodeName(elem, "table") && jQuery.nodeName(cur, "tr") ? elem.getElementsByTagName("tbody")[0] || elem.appendChild(elem.ownerDocument.createElement("tbody")) : elem;
            }
        }
    };
    // Give the init function the jQuery prototype for later instantiation
    jQuery.fn.init.prototype = jQuery.fn;
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
    function now() {
        return +new Date();
    }
    jQuery.extend = jQuery.fn.extend = function(ARG1, ARG2, ARG3) {
        // copy reference to target object
        var target = arguments[0] || {}, i = 1, length = arguments.length, deep = false, options;
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
        if (length == i) {
            target = this;
            --i;
        }
        for (;i < length; i++) // Only deal with non-null/undefined values
        {
            if ((options = arguments[i]) != null) // Extend the base object
            {
                for (var name in options) {
                    var src = target[name], copy = options[name];
                    // Prevent never-ending loop
                    if (target === copy) {
                        "dk.brics.tajs.directives.unreachable";
                        continue;
                    }
                    // Recurse if we're merging object values
                    if (deep && copy && typeof copy === "object" && !copy.nodeType) {
                        "dk.brics.tajs.directives.unreachable";
                        target[name] = jQuery.extend(deep, // Never move original objects, clone them
                        src || (copy.length != null ? [] : {}), copy);
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
    // exclude the following css properties to add px
    var exclude = /z-?index|font-?weight|opacity|zoom|line-?height/i, // cache defaultView
    defaultView = document.defaultView || {}, toString = Object.prototype.toString;
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
        // check if an element is in a (or is an) XML document
        isXMLDoc: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return elem.documentElement && !elem.body || elem.tagName && elem.ownerDocument && !elem.ownerDocument.body;
        },
        // Evalulates a script in a global context
        globalEval: function(data) {
            "dk.brics.tajs.directives.unreachable";
            data = jQuery.trim(data);
            if (data) {
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
                // Use insertBefore instead of appendChild  to circumvent an IE6 bug.
                // This arises when a base node is used (#2709).
                head.insertBefore(script, head.firstChild);
                head.removeChild(script);
            }
        },
        nodeName: function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            return elem.nodeName && elem.nodeName.toUpperCase() == name.toUpperCase();
        },
        // args is for internal usage only
        each: function(object, callback, args) {
            var name, i = 0, length = object.length;
            if (args) {
                "dk.brics.tajs.directives.unreachable";
                if (length === undefined) {
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
                if (length === undefined) {
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
        prop: function(elem, value, type, i, name) {
            "dk.brics.tajs.directives.unreachable";
            // Handle executable functions
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                value = value.call(elem, i);
            }
            // Handle passing in a number to a CSS property
            return typeof value === "number" && type == "curCSS" && !exclude.test(name) ? value + "px" : value;
        },
        className: {
            // internal only, use addClass("class")
            add: function(elem, classNames) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.each((classNames || "").split(/\s+/), function(i, className) {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.nodeType == 1 && !jQuery.className.has(elem.className, className)) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.className += (elem.className ? " " : "") + className;
                    }
                });
            },
            // internal only, use removeClass("class")
            remove: function(elem, classNames) {
                "dk.brics.tajs.directives.unreachable";
                if (elem.nodeType == 1) {
                    "dk.brics.tajs.directives.unreachable";
                    elem.className = classNames !== undefined ? jQuery.grep(elem.className.split(/\s+/), function(className) {
                        "dk.brics.tajs.directives.unreachable";
                        return !jQuery.className.has(classNames, className);
                    }).join(" ") : "";
                }
            },
            // internal only, use hasClass("class")
            has: function(elem, className) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.inArray(className, (elem.className || elem).toString().split(/\s+/)) > -1;
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
            for (var name in options) {
                "dk.brics.tajs.directives.unreachable";
                elem.style[name] = old[name];
            }
        },
        css: function(elem, name, force) {
            "dk.brics.tajs.directives.unreachable";
            if (name == "width" || name == "height") {
                "dk.brics.tajs.directives.unreachable";
                var val, props = {
                    position: "absolute",
                    visibility: "hidden",
                    display: "block"
                }, which = name == "width" ? [ "Left", "Right" ] : [ "Top", "Bottom" ];
                function getWH() {
                    "dk.brics.tajs.directives.unreachable";
                    val = name == "width" ? elem.offsetWidth : elem.offsetHeight;
                    var padding = 0, border = 0;
                    jQuery.each(which, function() {
                        "dk.brics.tajs.directives.unreachable";
                        padding += parseFloat(jQuery.curCSS(elem, "padding" + this, true)) || 0;
                        border += parseFloat(jQuery.curCSS(elem, "border" + this + "Width", true)) || 0;
                    });
                    val -= Math.round(padding + border);
                }
                if (jQuery(elem).is(":visible")) {
                    "dk.brics.tajs.directives.unreachable";
                    getWH();
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.swap(elem, props, getWH);
                }
                return Math.max(0, val);
            }
            return jQuery.curCSS(elem, name, force);
        },
        curCSS: function(elem, name, force) {
            "dk.brics.tajs.directives.unreachable";
            var ret, style = elem.style;
            // We need to handle opacity special in IE
            if (name == "opacity" && !jQuery.support.opacity) {
                "dk.brics.tajs.directives.unreachable";
                ret = jQuery.attr(style, "opacity");
                return ret == "" ? "1" : ret;
            }
            // Make sure we're using the right name for getting the float value
            if (name.match(/float/i)) {
                "dk.brics.tajs.directives.unreachable";
                name = styleFloat;
            }
            if (!force && style && style[name]) {
                "dk.brics.tajs.directives.unreachable";
                ret = style[name];
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (defaultView.getComputedStyle) {
                    "dk.brics.tajs.directives.unreachable";
                    // Only "float" is needed here
                    if (name.match(/float/i)) {
                        "dk.brics.tajs.directives.unreachable";
                        name = "float";
                    }
                    name = name.replace(/([A-Z])/g, "-$1").toLowerCase();
                    var computedStyle = defaultView.getComputedStyle(elem, null);
                    if (computedStyle) {
                        "dk.brics.tajs.directives.unreachable";
                        ret = computedStyle.getPropertyValue(name);
                    }
                    // We should always get a number back from opacity
                    if (name == "opacity" && ret == "") {
                        "dk.brics.tajs.directives.unreachable";
                        ret = "1";
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.currentStyle) {
                        "dk.brics.tajs.directives.unreachable";
                        var camelCase = name.replace(/\-(\w)/g, function(all, letter) {
                            "dk.brics.tajs.directives.unreachable";
                            return letter.toUpperCase();
                        });
                        ret = elem.currentStyle[name] || elem.currentStyle[camelCase];
                        // From the awesome hack by Dean Edwards
                        // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
                        // If we're not dealing with a regular pixel number
                        // but a number that has a weird ending, we need to convert it to pixels
                        if (!/^\d+(px)?$/i.test(ret) && /^\d/.test(ret)) {
                            "dk.brics.tajs.directives.unreachable";
                            // Remember the original values
                            var left = style.left, rsLeft = elem.runtimeStyle.left;
                            // Put in the new values to get a computed value out
                            elem.runtimeStyle.left = elem.currentStyle.left;
                            style.left = ret || 0;
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
        clean: function(elems, context, fragment) {
            "dk.brics.tajs.directives.unreachable";
            context = context || document;
            // !context.createElement fails in IE with an error but returns typeof 'object'
            if (typeof context.createElement === "undefined") {
                "dk.brics.tajs.directives.unreachable";
                context = context.ownerDocument || context[0] && context[0].ownerDocument || document;
            }
            // If a single string is passed in and it's a single tag
            // just do a createElement and skip the rest
            if (!fragment && elems.length === 1 && typeof elems[0] === "string") {
                "dk.brics.tajs.directives.unreachable";
                var match = /^<(\w+)\s*\/?>$/.exec(elems[0]);
                if (match) {
                    "dk.brics.tajs.directives.unreachable";
                    return [ context.createElement(match[1]) ];
                }
            }
            var ret = [], scripts = [], div = context.createElement("div");
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
                if (typeof elem === "string") {
                    "dk.brics.tajs.directives.unreachable";
                    // Fix "XHTML"-style tags in all browsers
                    elem = elem.replace(/(<(\w+)[^>]*?)\/>/g, function(all, front, tag) {
                        "dk.brics.tajs.directives.unreachable";
                        return tag.match(/^(abbr|br|col|img|input|link|meta|param|hr|area|embed)$/i) ? all : front + "></" + tag + ">";
                    });
                    // Trim whitespace, otherwise indexOf won't work as expected
                    var tags = jQuery.trim(elem).toLowerCase();
                    var wrap = // option or optgroup
                    !tags.indexOf("<opt") && [ 1, "<select multiple='multiple'>", "</select>" ] || !tags.indexOf("<leg") && [ 1, "<fieldset>", "</fieldset>" ] || tags.match(/^<(thead|tbody|tfoot|colg|cap)/) && [ 1, "<table>", "</table>" ] || !tags.indexOf("<tr") && [ 2, "<table><tbody>", "</tbody></table>" ] || // <thead> matched above
                    (!tags.indexOf("<td") || !tags.indexOf("<th")) && [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ] || !tags.indexOf("<col") && [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ] || // IE can't serialize <link> and <script> tags normally
                    !jQuery.support.htmlSerialize && [ 1, "div<div>", "</div>" ] || [ 0, "", "" ];
                    // Go to html and back, then peel off extra wrappers
                    div.innerHTML = wrap[1] + elem + wrap[2];
                    // Move to the right depth
                    while (wrap[0]--) {
                        "dk.brics.tajs.directives.unreachable";
                        div = div.lastChild;
                    }
                    // Remove IE's autoinserted <tbody> from table fragments
                    if (!jQuery.support.tbody) {
                        "dk.brics.tajs.directives.unreachable";
                        // String was a <table>, *may* have spurious <tbody>
                        var tbody = !tags.indexOf("<table") && tags.indexOf("<tbody") < 0 ? div.firstChild && div.firstChild.childNodes : // String was a bare <thead> or <tfoot>
                        wrap[1] == "<table>" && tags.indexOf("<tbody") < 0 ? div.childNodes : [];
                        for (var j = tbody.length - 1; j >= 0; --j) {
                            "dk.brics.tajs.directives.unreachable";
                            if (jQuery.nodeName(tbody[j], "tbody") && !tbody[j].childNodes.length) {
                                "dk.brics.tajs.directives.unreachable";
                                tbody[j].parentNode.removeChild(tbody[j]);
                            }
                        }
                    }
                    // IE completely kills leading whitespace when innerHTML is used
                    if (!jQuery.support.leadingWhitespace && /^\s/.test(elem)) {
                        "dk.brics.tajs.directives.unreachable";
                        div.insertBefore(context.createTextNode(elem.match(/^\s*/)[0]), div.firstChild);
                    }
                    elem = jQuery.makeArray(div.childNodes);
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
                    if (jQuery.nodeName(ret[i], "script") && (!ret[i].type || ret[i].type.toLowerCase() === "text/javascript")) {
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
                return scripts;
            }
            return ret;
        },
        attr: function(elem, name, value) {
            "dk.brics.tajs.directives.unreachable";
            // don't set attributes on text and comment nodes
            if (!elem || elem.nodeType == 3 || elem.nodeType == 8) {
                "dk.brics.tajs.directives.unreachable";
                return undefined;
            }
            var notxml = !jQuery.isXMLDoc(elem), // Whether we are setting (or getting)
            set = value !== undefined;
            // Try to normalize/fix the name
            name = notxml && jQuery.props[name] || name;
            // Only do all the following if this is a node (faster for style)
            // IE elem.getAttribute passes even for style
            if (elem.tagName) {
                "dk.brics.tajs.directives.unreachable";
                // These attributes require special treatment
                var special = /href|src|style/.test(name);
                // Safari mis-reports the default selected property of a hidden option
                // Accessing the parent's selectedIndex property fixes it
                if (name == "selected" && elem.parentNode) {
                    "dk.brics.tajs.directives.unreachable";
                    elem.parentNode.selectedIndex;
                }
                // If applicable, access the attribute via the DOM 0 way
                if (name in elem && notxml && !special) {
                    "dk.brics.tajs.directives.unreachable";
                    if (set) {
                        "dk.brics.tajs.directives.unreachable";
                        // We can't allow the type property to be changed (since it causes problems in IE)
                        if (name == "type" && jQuery.nodeName(elem, "input") && elem.parentNode) {
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
                    if (name == "tabIndex") {
                        "dk.brics.tajs.directives.unreachable";
                        var attributeNode = elem.getAttributeNode("tabIndex");
                        return attributeNode && attributeNode.specified ? attributeNode.value : elem.nodeName.match(/^(a|area|button|input|object|select|textarea)$/i) ? 0 : undefined;
                    }
                    return elem[name];
                }
                if (!jQuery.support.style && notxml && name == "style") {
                    "dk.brics.tajs.directives.unreachable";
                    return jQuery.attr(elem.style, "cssText", value);
                }
                if (set) // convert the value to a string (all browsers do this but IE) see #1070
                {
                    "dk.brics.tajs.directives.unreachable";
                    elem.setAttribute(name, "" + value);
                }
                var attr = !jQuery.support.hrefNormalized && notxml && special ? elem.getAttribute(name, 2) : elem.getAttribute(name);
                // Non-existent attributes return null, we normalize to undefined
                return attr === null ? undefined : attr;
            }
            // elem is actually elem.style ... set the style
            // IE uses filters for opacity
            if (!jQuery.support.opacity && name == "opacity") {
                "dk.brics.tajs.directives.unreachable";
                if (set) {
                    "dk.brics.tajs.directives.unreachable";
                    // IE has trouble with opacity if it does not have layout
                    // Force it by setting the zoom level
                    elem.zoom = 1;
                    // Set the alpha filter to set the opacity
                    elem.filter = (elem.filter || "").replace(/alpha\([^)]*\)/, "") + (parseInt(value) + "" == "NaN" ? "" : "alpha(opacity=" + value * 100 + ")");
                }
                return elem.filter && elem.filter.indexOf("opacity=") >= 0 ? parseFloat(elem.filter.match(/opacity=([^)]*)/)[1]) / 100 + "" : "";
            }
            name = name.replace(/-([a-z])/gi, function(all, letter) {
                "dk.brics.tajs.directives.unreachable";
                return letter.toUpperCase();
            });
            if (set) {
                "dk.brics.tajs.directives.unreachable";
                elem[name] = value;
            }
            return elem[name];
        },
        trim: function(text) {
            "dk.brics.tajs.directives.unreachable";
            return (text || "").replace(/^\s+|\s+$/g, "");
        },
        makeArray: function(array) {
            var ret = [];
            if (array != null) {
                var i = array.length;
                // The window, strings (and functions) also have 'length'
                if (i == null || typeof array === "string" || jQuery.isFunction(array) || array.setInterval) {
                    ret[0] = array;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    while (i) {
                        "dk.brics.tajs.directives.unreachable";
                        ret[--i] = array[i];
                    }
                }
            }
            return ret;
        },
        inArray: function(elem, array) {
            "dk.brics.tajs.directives.unreachable";
            for (var i = 0, length = array.length; i < length; i++) // Use === because on IE, window == document
            {
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
            // We have to loop this way because IE & Opera overwrite the length
            // expando of getElementsByTagName
            var i = 0, elem, pos = first.length;
            // Also, we need to make sure that the correct elements are being returned
            // (IE returns comment nodes in a '*' query)
            if (!jQuery.support.getAll) {
                "dk.brics.tajs.directives.unreachable";
                while ((elem = second[i++]) != null) {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.nodeType != 8) {
                        "dk.brics.tajs.directives.unreachable";
                        first[pos++] = elem;
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                while ((elem = second[i++]) != null) {
                    "dk.brics.tajs.directives.unreachable";
                    first[pos++] = elem;
                }
            }
            return first;
        },
        unique: function(array) {
            "dk.brics.tajs.directives.unreachable";
            var ret = [], done = {};
            try {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, length = array.length; i < length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var id = jQuery.data(array[i]);
                    if (!done[id]) {
                        "dk.brics.tajs.directives.unreachable";
                        done[id] = true;
                        ret.push(array[i]);
                    }
                }
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                ret = array;
            }
            return ret;
        },
        grep: function(elems, callback, inv) {
            "dk.brics.tajs.directives.unreachable";
            var ret = [];
            // Go through the array, only saving the items
            // that pass the validator function
            for (var i = 0, length = elems.length; i < length; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (!inv != !callback(elems[i], i)) {
                    "dk.brics.tajs.directives.unreachable";
                    ret.push(elems[i]);
                }
            }
            return ret;
        },
        map: function(elems, callback) {
            "dk.brics.tajs.directives.unreachable";
            var ret = [];
            // Go through the array, translating each of the items to their
            // new value (or values).
            for (var i = 0, length = elems.length; i < length; i++) {
                "dk.brics.tajs.directives.unreachable";
                var value = callback(elems[i], i);
                if (value != null) {
                    "dk.brics.tajs.directives.unreachable";
                    ret[ret.length] = value;
                }
            }
            return ret.concat.apply([], ret);
        }
    });
    // Use of jQuery.browser is deprecated.
    // It's included for backwards compatibility and plugins,
    // although they should work to migrate away.
    var userAgent = navigator.userAgent.toLowerCase();
    // Figure out what browser is being used
    jQuery.browser = {
        version: (userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [ 0, "0" ])[1],
        safari: true || /webkit/.test(userAgent),
        opera: false && /opera/.test(userAgent),
        msie: false && /msie/.test(userAgent) && !/opera/.test(userAgent),
        mozilla: false && /mozilla/.test(userAgent) && !/(compatible|webkit)/.test(userAgent)
    };
    jQuery.each({
        parent: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return elem.parentNode;
        },
        parents: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.dir(elem, "parentNode");
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
        jQuery.fn[name] = function(selector) {
            "dk.brics.tajs.directives.unreachable";
            var ret = jQuery.map(this, fn);
            if (selector && typeof selector == "string") {
                "dk.brics.tajs.directives.unreachable";
                ret = jQuery.multiFilter(selector, ret);
            }
            return this.pushStack(jQuery.unique(ret), name, selector);
        };
    });
    jQuery.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
    }, function(name, original) {
        jQuery.fn[name] = function() {
            "dk.brics.tajs.directives.unreachable";
            var args = arguments;
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, length = args.length; i < length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(args[i])[original](this);
                }
            });
        };
    });
    jQuery.each({
        removeAttr: function(name) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.attr(this, name, "");
            if (this.nodeType == 1) {
                "dk.brics.tajs.directives.unreachable";
                this.removeAttribute(name);
            }
        },
        addClass: function(classNames) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.className.add(this, classNames);
        },
        removeClass: function(classNames) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.className.remove(this, classNames);
        },
        toggleClass: function(classNames, state) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof state !== "boolean") {
                "dk.brics.tajs.directives.unreachable";
                state = !jQuery.className.has(this, classNames);
            }
            if(state){
                jQuery.className.add(this, classNames);
            }else{
                jQuery.className.remove(this, classNames);
            } 
        },
        remove: function(selector) {
            "dk.brics.tajs.directives.unreachable";
            if (!selector || jQuery.filter(selector, [ this ]).length) {
                "dk.brics.tajs.directives.unreachable";
                // Prevent memory leaks
                jQuery("*", this).add([ this ]).each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.remove(this);
                    jQuery.removeData(this);
                });
                if (this.parentNode) {
                    "dk.brics.tajs.directives.unreachable";
                    this.parentNode.removeChild(this);
                }
            }
        },
        empty: function() {
            "dk.brics.tajs.directives.unreachable";
            // Remove element nodes and prevent memory leaks
            jQuery(">*", this).remove();
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
    // Helper function used by the dimensions and offset modules
    function num(elem, prop) {
        "dk.brics.tajs.directives.unreachable";
        return elem[0] && parseInt(jQuery.curCSS(elem[0], prop, true), 10) || 0;
    }
    var expando = "jQuery" + "TAJS_NOW", uuid = 0, windowData = {};
    jQuery.extend({
        cache: {},
        data: function(elem, name, data) {
            elem = elem == window ? windowData : elem;
            var id = elem[expando];
            // Compute a unique ID for the element
            if (!id) {
                id = elem[expando] = ++uuid;
            }
            // Only generate the data cache if we're
            // trying to access or manipulate it
            if (name && !jQuery.cache[id]) {
                jQuery.cache[id] = {};
            }
            // Prevent overriding the named cache with undefined values
            if (data !== undefined) {
                jQuery.cache[id][name] = data;
            }
            // Return the named cache data, or the ID for the element
            return name ? jQuery.cache[id][name] : id;
        },
        removeData: function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            elem = elem == window ? windowData : elem;
            var id = elem[expando];
            // If we want to remove a specific section of the element's data
            if (name) {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.cache[id]) {
                    "dk.brics.tajs.directives.unreachable";
                    // Remove the section of cache data
                    delete jQuery.cache[id][name];
                    // If we've removed all the data, remove the element's cache
                    name = "";
                    for (name in jQuery.cache[id]) {
                        "dk.brics.tajs.directives.unreachable";
                        break;
                    }
                    if (!name) {
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
                delete jQuery.cache[id];
            }
        },
        queue: function(elem, type, data) {
            "dk.brics.tajs.directives.unreachable";
            if (elem) {
                "dk.brics.tajs.directives.unreachable";
                type = (type || "fx") + "queue";
                var q = jQuery.data(elem, type);
                if (!q || jQuery.isArray(data)) {
                    "dk.brics.tajs.directives.unreachable";
                    q = jQuery.data(elem, type, jQuery.makeArray(data));
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (data) {
                        "dk.brics.tajs.directives.unreachable";
                        q.push(data);
                    }
                }
            }
            return q;
        },
        dequeue: function(elem, type) {
            "dk.brics.tajs.directives.unreachable";
            var queue = jQuery.queue(elem, type), fn = queue.shift();
            if (!type || type === "fx") {
                "dk.brics.tajs.directives.unreachable";
                fn = queue[0];
            }
            if (fn !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                fn.call(elem);
            }
        }
    });
    jQuery.fn.extend({
        data: function(key, value) {
            "dk.brics.tajs.directives.unreachable";
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
        },
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
                if (type == "fx" && queue.length == 1) {
                    "dk.brics.tajs.directives.unreachable";
                    queue[0].call(this);
                }
            });
        },
        dequeue: function(type) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.dequeue(this, type);
            });
        }
    });
    /*!
 * Sizzle CSS Selector Engine - v0.9.1
 *  Copyright 2009, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
    (function() {
        var chunker = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^[\]]*\]|[^[\]]+)+\]|\\.|[^ >+~,(\[]+)+|[>+~])(\s*,\s*)?/g, done = 0, toString = Object.prototype.toString;
        var Sizzle = function(selector, context, results, seed) {
            "dk.brics.tajs.directives.unreachable";
            results = results || [];
            context = context || document;
            if (context.nodeType !== 1 && context.nodeType !== 9) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            if (!selector || typeof selector !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return results;
            }
            var parts = [], m, set, checkSet, check, mode, extra, prune = true;
            // Reset the position of the chunker regexp (start from head)
            chunker.lastIndex = 0;
            while ((m = chunker.exec(selector)) !== null) {
                "dk.brics.tajs.directives.unreachable";
                parts.push(m[1]);
                if (m[2]) {
                    "dk.brics.tajs.directives.unreachable";
                    extra = RegExp.rightContext;
                    break;
                }
            }
            if (parts.length > 1 && Expr.match.POS.exec(selector)) {
                "dk.brics.tajs.directives.unreachable";
                if (parts.length === 2 && Expr.relative[parts[0]]) {
                    "dk.brics.tajs.directives.unreachable";
                    var later = "", match;
                    // Position selectors must be done after the filter
                    while (match = Expr.match.POS.exec(selector)) {
                        "dk.brics.tajs.directives.unreachable";
                        later += match[0];
                        selector = selector.replace(Expr.match.POS, "");
                    }
                    set = Sizzle.filter(later, Sizzle(/\s$/.test(selector) ? selector + "*" : selector, context));
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    set = Expr.relative[parts[0]] ? [ context ] : Sizzle(parts.shift(), context);
                    while (parts.length) {
                        "dk.brics.tajs.directives.unreachable";
                        var tmpSet = [];
                        selector = parts.shift();
                        if (Expr.relative[selector]) {
                            "dk.brics.tajs.directives.unreachable";
                            selector += parts.shift();
                        }
                        for (var i = 0, l = set.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle(selector, set[i], tmpSet);
                        }
                        set = tmpSet;
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                var ret = seed ? {
                    expr: parts.pop(),
                    set: makeArray(seed)
                } : Sizzle.find(parts.pop(), parts.length === 1 && context.parentNode ? context.parentNode : context);
                set = Sizzle.filter(ret.expr, ret.set);
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
                    Expr.relative[cur](checkSet, pop, isXML(context));
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
                    if (context.nodeType === 1) {
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
                Sizzle(extra, context, results, seed);
            }
            return results;
        };
        Sizzle.matches = function(expr, set) {
            "dk.brics.tajs.directives.unreachable";
            return Sizzle(expr, null, null, set);
        };
        Sizzle.find = function(expr, context) {
            "dk.brics.tajs.directives.unreachable";
            var set, match;
            if (!expr) {
                "dk.brics.tajs.directives.unreachable";
                return [];
            }
            for (var i = 0, l = Expr.order.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var type = Expr.order[i], match;
                if (match = Expr.match[type].exec(expr)) {
                    "dk.brics.tajs.directives.unreachable";
                    var left = RegExp.leftContext;
                    if (left.substr(left.length - 1) !== "\\") {
                        "dk.brics.tajs.directives.unreachable";
                        match[1] = (match[1] || "").replace(/\\/g, "");
                        set = Expr.find[type](match, context);
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
            var old = expr, result = [], curLoop = set, match, anyFound;
            while (expr && set.length) {
                "dk.brics.tajs.directives.unreachable";
                for (var type in Expr.filter) {
                    "dk.brics.tajs.directives.unreachable";
                    if ((match = Expr.match[type].exec(expr)) != null) {
                        "dk.brics.tajs.directives.unreachable";
                        var filter = Expr.filter[type], goodArray = null, goodPos = 0, found, item;
                        anyFound = false;
                        if (curLoop == result) {
                            "dk.brics.tajs.directives.unreachable";
                            result = [];
                        }
                        if (Expr.preFilter[type]) {
                            "dk.brics.tajs.directives.unreachable";
                            match = Expr.preFilter[type](match, curLoop, inplace, result, not);
                            if (!match) {
                                "dk.brics.tajs.directives.unreachable";
                                anyFound = found = true;
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (match === true) {
                                    "dk.brics.tajs.directives.unreachable";
                                    continue;
                                } else {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (match[0] === true) {
                                        "dk.brics.tajs.directives.unreachable";
                                        goodArray = [];
                                        var last = null, elem;
                                        for (var i = 0; (elem = curLoop[i]) !== undefined; i++) {
                                            "dk.brics.tajs.directives.unreachable";
                                            if (elem && last !== elem) {
                                                "dk.brics.tajs.directives.unreachable";
                                                goodArray.push(elem);
                                                last = elem;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (match) {
                            "dk.brics.tajs.directives.unreachable";
                            for (var i = 0; (item = curLoop[i]) !== undefined; i++) {
                                "dk.brics.tajs.directives.unreachable";
                                if (item) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (goodArray && item != goodArray[goodPos]) {
                                        "dk.brics.tajs.directives.unreachable";
                                        goodPos++;
                                    }
                                    found = filter(item, match, goodPos, goodArray);
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
                expr = expr.replace(/\s*,\s*/, "");
                // Improper expression
                if (expr == old) {
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
                ID: /#((?:[\w\u00c0-\uFFFF_-]|\\.)+)/,
                CLASS: /\.((?:[\w\u00c0-\uFFFF_-]|\\.)+)/,
                NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF_-]|\\.)+)['"]*\]/,
                ATTR: /\[\s*((?:[\w\u00c0-\uFFFF_-]|\\.)+)\s*(?:(\S?=)\s*(['"]*)(.*?)\3|)\s*\]/,
                TAG: /^((?:[\w\u00c0-\uFFFF\*_-]|\\.)+)/,
                CHILD: /:(only|nth|last|first)-child(?:\((even|odd|[\dn+-]*)\))?/,
                POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^-]|$)/,
                PSEUDO: /:((?:[\w\u00c0-\uFFFF_-]|\\.)+)(?:\((['"]*)((?:\([^\)]+\)|[^\2\(\)]*)+)\2\))?/
            },
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
                    for (var i = 0, l = checkSet.length; i < l; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        var elem = checkSet[i];
                        if (elem) {
                            "dk.brics.tajs.directives.unreachable";
                            var cur = elem.previousSibling;
                            while (cur && cur.nodeType !== 1) {
                                "dk.brics.tajs.directives.unreachable";
                                cur = cur.previousSibling;
                            }
                            checkSet[i] = typeof part === "string" ? cur || false : cur === part;
                        }
                    }
                    if (typeof part === "string") {
                        "dk.brics.tajs.directives.unreachable";
                        Sizzle.filter(part, checkSet, true);
                    }
                },
                ">": function(checkSet, part, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof part === "string" && !/\W/.test(part)) {
                        "dk.brics.tajs.directives.unreachable";
                        part = isXML ? part : part.toUpperCase();
                        for (var i = 0, l = checkSet.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            var elem = checkSet[i];
                            if (elem) {
                                "dk.brics.tajs.directives.unreachable";
                                var parent = elem.parentNode;
                                checkSet[i] = parent.nodeName === part ? parent : false;
                            }
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        for (var i = 0, l = checkSet.length; i < l; i++) {
                            "dk.brics.tajs.directives.unreachable";
                            var elem = checkSet[i];
                            if (elem) {
                                "dk.brics.tajs.directives.unreachable";
                                checkSet[i] = typeof part === "string" ? elem.parentNode : elem.parentNode === part;
                            }
                        }
                        if (typeof part === "string") {
                            "dk.brics.tajs.directives.unreachable";
                            Sizzle.filter(part, checkSet, true);
                        }
                    }
                },
                "": function(checkSet, part, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    var doneName = "done" + done++, checkFn = dirCheck;
                    if (!part.match(/\W/)) {
                        "dk.brics.tajs.directives.unreachable";
                        var nodeCheck = part = isXML ? part : part.toUpperCase();
                        checkFn = dirNodeCheck;
                    }
                    checkFn("parentNode", part, doneName, checkSet, nodeCheck, isXML);
                },
                "~": function(checkSet, part, isXML) {
                    "dk.brics.tajs.directives.unreachable";
                    var doneName = "done" + done++, checkFn = dirCheck;
                    if (typeof part === "string" && !part.match(/\W/)) {
                        "dk.brics.tajs.directives.unreachable";
                        var nodeCheck = part = isXML ? part : part.toUpperCase();
                        checkFn = dirNodeCheck;
                    }
                    checkFn("previousSibling", part, doneName, checkSet, nodeCheck, isXML);
                }
            },
            find: {
                ID: function(match, context) {
                    "dk.brics.tajs.directives.unreachable";
                    if (context.getElementById) {
                        "dk.brics.tajs.directives.unreachable";
                        var m = context.getElementById(match[1]);
                        return m ? [ m ] : [];
                    }
                },
                NAME: function(match, context) {
                    "dk.brics.tajs.directives.unreachable";
                    return context.getElementsByName ? context.getElementsByName(match[1]) : null;
                },
                TAG: function(match, context) {
                    "dk.brics.tajs.directives.unreachable";
                    return context.getElementsByTagName(match[1]);
                }
            },
            preFilter: {
                CLASS: function(match, curLoop, inplace, result, not) {
                    "dk.brics.tajs.directives.unreachable";
                    match = " " + match[1].replace(/\\/g, "") + " ";
                    for (var i = 0; curLoop[i]; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (not ^ (" " + curLoop[i].className + " ").indexOf(match) >= 0) {
                            "dk.brics.tajs.directives.unreachable";
                            if (!inplace) {
                                "dk.brics.tajs.directives.unreachable";
                                result.push(curLoop[i]);
                            }
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (inplace) {
                                "dk.brics.tajs.directives.unreachable";
                                curLoop[i] = false;
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
                    for (var i = 0; !curLoop[i]; i++) {}
                    return isXML(curLoop[i]) ? match[1] : match[1].toUpperCase();
                },
                CHILD: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    if (match[1] == "nth") {
                        "dk.brics.tajs.directives.unreachable";
                        // parse equations like 'even', 'odd', '5', '2n', '3n+2', '4n-1', '-n+6'
                        var test = /(-?)(\d*)n((?:\+|-)?\d*)/.exec(match[2] == "even" && "2n" || match[2] == "odd" && "2n+1" || !/\D/.test(match[2]) && "0n+" + match[2] || match[2]);
                        // calculate the numbers (first)n+(last) including if they are negative
                        match[2] = test[1] + (test[2] || 1) - 0;
                        match[3] = test[3] - 0;
                    }
                    // TODO: Move to normal caching system
                    match[0] = "done" + done++;
                    return match;
                },
                ATTR: function(match) {
                    "dk.brics.tajs.directives.unreachable";
                    var name = match[1];
                    if (Expr.attrMap[name]) {
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
                        if (match[3].match(chunker).length > 1) {
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
                        if (Expr.match.POS.test(match[0])) {
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
                    return "button" === elem.type || elem.nodeName.toUpperCase() === "BUTTON";
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
                    return match[3] - 0 == i;
                },
                eq: function(elem, i, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return match[3] - 0 == i;
                }
            },
            filter: {
                CHILD: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    var type = match[1], parent = elem.parentNode;
                    var doneName = "child" + parent.childNodes.length;
                    if (parent && (!parent[doneName] || !elem.nodeIndex)) {
                        "dk.brics.tajs.directives.unreachable";
                        var count = 1;
                        for (var node = parent.firstChild; node; node = node.nextSibling) {
                            "dk.brics.tajs.directives.unreachable";
                            if (node.nodeType == 1) {
                                "dk.brics.tajs.directives.unreachable";
                                node.nodeIndex = count++;
                            }
                        }
                        parent[doneName] = count - 1;
                    }
                    if (type == "first") {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.nodeIndex == 1;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (type == "last") {
                            "dk.brics.tajs.directives.unreachable";
                            return elem.nodeIndex == parent[doneName];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (type == "only") {
                                "dk.brics.tajs.directives.unreachable";
                                return parent[doneName] == 1;
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                if (type == "nth") {
                                    "dk.brics.tajs.directives.unreachable";
                                    var add = false, first = match[2], last = match[3];
                                    if (first == 1 && last == 0) {
                                        "dk.brics.tajs.directives.unreachable";
                                        return true;
                                    }
                                    if (first == 0) {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (elem.nodeIndex == last) {
                                            "dk.brics.tajs.directives.unreachable";
                                            add = true;
                                        }
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        if ((elem.nodeIndex - last) % first == 0 && (elem.nodeIndex - last) / first >= 0) {
                                            "dk.brics.tajs.directives.unreachable";
                                            add = true;
                                        }
                                    }
                                    return add;
                                }
                            }
                        }
                    }
                },
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
                            return (elem.textContent || elem.innerText || "").indexOf(match[3]) >= 0;
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
                            }
                        }
                    }
                },
                ID: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.nodeType === 1 && elem.getAttribute("id") === match;
                },
                TAG: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return match === "*" && elem.nodeType === 1 || elem.nodeName === match;
                },
                CLASS: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    return match.test(elem.className);
                },
                ATTR: function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    var result = Expr.attrHandle[match[1]] ? Expr.attrHandle[match[1]](elem) : elem[match[1]] || elem.getAttribute(match[1]), value = result + "", type = match[2], check = match[4];
                    return result == null ? false : type === "=" ? value === check : type === "*=" ? value.indexOf(check) >= 0 : type === "~=" ? (" " + value + " ").indexOf(check) >= 0 : !match[4] ? result : type === "!=" ? value != check : type === "^=" ? value.indexOf(check) === 0 : type === "$=" ? value.substr(value.length - check.length) === check : type === "|=" ? value === check || value.substr(0, check.length + 1) === check + "-" : false;
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
        for (var type in Expr.match) {
            Expr.match[type] = RegExp(Expr.match[type].source + /(?![^\[]*\])(?![^\(]*\))/.source);
        }
        var makeArray = function(array, results) {
            "dk.brics.tajs.directives.unreachable";
            array = Array.prototype.slice.call(array);
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
            Array.prototype.slice.call(document.documentElement.childNodes);
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
        // Check to see if the browser returns elements by name when
        // querying by getElementById (and provide a workaround)
        (function() {
            // We're going to inject a fake input element with a specified name
            var form = document.createElement("form"), id = "script" + "TAJS_TIME";
            form.innerHTML = "<input name='" + id + "'/>";
            // Inject it into the root element, check its status, and remove it quickly
            var root = document.documentElement;
            root.insertBefore(form, root.firstChild);
            // The workaround has to do additional checks after a getElementById
            // Which slows things down for other browsers (hence the branching)
            if (!!document.getElementById(id)) {
                "dk.brics.tajs.directives.unreachable";
                Expr.find.ID = function(match, context) {
                    "dk.brics.tajs.directives.unreachable";
                    if (context.getElementById) {
                        "dk.brics.tajs.directives.unreachable";
                        var m = context.getElementById(match[1]);
                        return m ? m.id === match[1] || m.getAttributeNode && m.getAttributeNode("id").nodeValue === match[1] ? [ m ] : undefined : [];
                    }
                };
                Expr.filter.ID = function(elem, match) {
                    "dk.brics.tajs.directives.unreachable";
                    var node = elem.getAttributeNode && elem.getAttributeNode("id");
                    return elem.nodeType === 1 && node && node.nodeValue === match;
                };
            }
            root.removeChild(form);
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
            if (div.firstChild.getAttribute("href") !== "#") {
                "dk.brics.tajs.directives.unreachable";
                Expr.attrHandle.href = function(elem) {
                    "dk.brics.tajs.directives.unreachable";
                    return elem.getAttribute("href", 2);
                };
            }
        })();
        if (document.querySelectorAll) {
            (function() {
                var oldSizzle = Sizzle;
                Sizzle = function(query, context, extra, seed) {
                    "dk.brics.tajs.directives.unreachable";
                    context = context || document;
                    if (!seed && context.nodeType === 9) {
                        "dk.brics.tajs.directives.unreachable";
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            return makeArray(context.querySelectorAll(query), extra);
                        } catch (e) {}
                    }
                    return oldSizzle(query, context, extra, seed);
                };
                Sizzle.find = oldSizzle.find;
                Sizzle.filter = oldSizzle.filter;
                Sizzle.selectors = oldSizzle.selectors;
                Sizzle.matches = oldSizzle.matches;
            })();
        }
        if (document.documentElement.getElementsByClassName) {
            Expr.order.splice(1, 0, "CLASS");
            Expr.find.CLASS = function(match, context) {
                "dk.brics.tajs.directives.unreachable";
                return context.getElementsByClassName(match[1]);
            };
        }
        function dirNodeCheck(dir, cur, doneName, checkSet, nodeCheck, isXML) {
            "dk.brics.tajs.directives.unreachable";
            for (var i = 0, l = checkSet.length; i < l; i++) {
                "dk.brics.tajs.directives.unreachable";
                var elem = checkSet[i];
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    elem = elem[dir];
                    var match = false;
                    while (elem && elem.nodeType) {
                        "dk.brics.tajs.directives.unreachable";
                        var done = elem[doneName];
                        if (done) {
                            "dk.brics.tajs.directives.unreachable";
                            match = checkSet[done];
                            break;
                        }
                        if (elem.nodeType === 1 && !isXML) {
                            "dk.brics.tajs.directives.unreachable";
                            elem[doneName] = i;
                        }
                        if (elem.nodeName === cur) {
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
                    while (elem && elem.nodeType) {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem[doneName]) {
                            "dk.brics.tajs.directives.unreachable";
                            match = checkSet[elem[doneName]];
                            break;
                        }
                        if (elem.nodeType === 1) {
                            "dk.brics.tajs.directives.unreachable";
                            if (!isXML) {
                                "dk.brics.tajs.directives.unreachable";
                                elem[doneName] = i;
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
            return elem.documentElement && !elem.body || elem.tagName && elem.ownerDocument && !elem.ownerDocument.body;
        };
        // EXPOSE
        jQuery.find = Sizzle;
        jQuery.filter = Sizzle.filter;
        jQuery.expr = Sizzle.selectors;
        jQuery.expr[":"] = jQuery.expr.filters;
        Sizzle.selectors.filters.hidden = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return "hidden" === elem.type || jQuery.css(elem, "display") === "none" || jQuery.css(elem, "visibility") === "hidden";
        };
        Sizzle.selectors.filters.visible = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return "hidden" !== elem.type && jQuery.css(elem, "display") !== "none" && jQuery.css(elem, "visibility") !== "hidden";
        };
        Sizzle.selectors.filters.animated = function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.grep(jQuery.timers, function(fn) {
                "dk.brics.tajs.directives.unreachable";
                return elem === fn.elem;
            }).length;
        };
        jQuery.multiFilter = function(expr, elems, not) {
            "dk.brics.tajs.directives.unreachable";
            if (not) {
                "dk.brics.tajs.directives.unreachable";
                expr = ":not(" + expr + ")";
            }
            return Sizzle.matches(expr, elems);
        };
        jQuery.dir = function(elem, dir) {
            "dk.brics.tajs.directives.unreachable";
            var matched = [], cur = elem[dir];
            while (cur && cur != document) {
                "dk.brics.tajs.directives.unreachable";
                if (cur.nodeType == 1) {
                    "dk.brics.tajs.directives.unreachable";
                    matched.push(cur);
                }
                cur = cur[dir];
            }
            return matched;
        };
        jQuery.nth = function(cur, result, dir, elem) {
            "dk.brics.tajs.directives.unreachable";
            result = result || 1;
            var num = 0;
            for (;cur; cur = cur[dir]) {
                "dk.brics.tajs.directives.unreachable";
                if (cur.nodeType == 1 && ++num == result) {
                    "dk.brics.tajs.directives.unreachable";
                    break;
                }
            }
            return cur;
        };
        jQuery.sibling = function(n, elem) {
            "dk.brics.tajs.directives.unreachable";
            var r = [];
            for (;n; n = n.nextSibling) {
                "dk.brics.tajs.directives.unreachable";
                if (n.nodeType == 1 && n != elem) {
                    "dk.brics.tajs.directives.unreachable";
                    r.push(n);
                }
            }
            return r;
        };
        return;
        "dk.brics.tajs.directives.unreachable";
        window.Sizzle = Sizzle;
    })();
    /*
 * A number of helper functions used for managing events.
 * Many of the ideas behind this code originated from
 * Dean Edwards' addEvent library.
 */
    jQuery.event = {
        // Bind an event to an element
        // Original by Dean Edwards
        add: function(elem, types, handler, data) {
            if (elem.nodeType == 3 || elem.nodeType == 8) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            // For whatever reason, IE has trouble passing the window object
            // around, causing it to be cloned in the process
            if (elem.setInterval && elem != window) {
                "dk.brics.tajs.directives.unreachable";
                elem = window;
            }
            // Make sure that the function being executed has a unique ID
            if (!handler.guid) {
                handler.guid = this.guid++;
            }
            // if data is passed, bind to handler
            if (data !== undefined) {
                "dk.brics.tajs.directives.unreachable";
                // Create temporary function pointer to original handler
                var fn = handler;
                // Create unique handler function, wrapped around original handler
                handler = this.proxy(fn);
                // Store data in unique handler
                handler.data = data;
            }
            // Init the element's event structure
            var events = jQuery.data(elem, "events") || jQuery.data(elem, "events", {}), handle = jQuery.data(elem, "handle") || jQuery.data(elem, "handle", function() {
                // Handle the second event of a trigger and when
                // an event is called after a page has unloaded
                return typeof jQuery !== "undefined" && !jQuery.event.triggered ? jQuery.event.handle.apply(arguments.callee.elem, arguments) : undefined;
            });
            // Add elem as a property of the handle function
            // This is to prevent a memory leak with non-native
            // event in IE.
            handle.elem = elem;
            // Handle multiple events separated by a space
            // jQuery(...).bind("mouseover mouseout", fn);
            jQuery.each(types.split(" "), function(index, type) {
                // Namespaced event handlers
                var namespaces = type.split(".");
                type = namespaces.shift();
                handler.type = namespaces.slice().sort().join(".");
                // Get the current list of functions bound to this event
                var handlers = events[type];
                if (jQuery.event.specialAll[type]) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.specialAll[type].setup.call(elem, data, namespaces);
                }
                // Init the event handler queue
                if (!handlers) {
                    handlers = events[type] = {};
                    // Check for a special event handler
                    // Only use addEventListener/attachEvent if the special
                    // events handler returns false
                    if (!jQuery.event.special[type] || jQuery.event.special[type].setup.call(elem, data, namespaces) === false) {
                        // Bind the global event handler to the element
                        if (elem.addEventListener) {
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
                // Add the function to the element's handler list
                handlers[handler.guid] = handler;
                // Keep track of which events have been used, for global triggering
                jQuery.event.global[type] = true;
            });
            // Nullify elem to prevent memory leaks in IE
            elem = null;
        },
        guid: 1,
        global: {},
        // Detach an event or set of events from an element
        remove: function(elem, types, handler) {
            "dk.brics.tajs.directives.unreachable";
            // don't do events on text and comment nodes
            if (elem.nodeType == 3 || elem.nodeType == 8) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var events = jQuery.data(elem, "events"), ret, index;
            if (events) {
                "dk.brics.tajs.directives.unreachable";
                // Unbind all events for the element
                if (types === undefined || typeof types === "string" && types.charAt(0) == ".") {
                    "dk.brics.tajs.directives.unreachable";
                    for (var type in events) {
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
                    // Handle multiple events seperated by a space
                    // jQuery(...).unbind("mouseover mouseout", fn);
                    jQuery.each(types.split(/\s+/), function(index, type) {
                        "dk.brics.tajs.directives.unreachable";
                        // Namespaced event handlers
                        var namespaces = type.split(".");
                        type = namespaces.shift();
                        var namespace = RegExp("(^|\\.)" + namespaces.slice().sort().join(".*\\.") + "(\\.|$)");
                        if (events[type]) {
                            "dk.brics.tajs.directives.unreachable";
                            // remove the given handler for the given type
                            if (handler) {
                                "dk.brics.tajs.directives.unreachable";
                                delete events[type][handler.guid];
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                for (var handle in events[type]) // Handle the removal of namespaced events
                                {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (namespace.test(events[type][handle].type)) {
                                        "dk.brics.tajs.directives.unreachable";
                                        delete events[type][handle];
                                    }
                                }
                            }
                            if (jQuery.event.specialAll[type]) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.event.specialAll[type].teardown.call(elem, namespaces);
                            }
                            // remove generic event handler if no more handlers exist
                            for (ret in events[type]) {
                                "dk.brics.tajs.directives.unreachable";
                                break;
                            }
                            if (!ret) {
                                "dk.brics.tajs.directives.unreachable";
                                if (!jQuery.event.special[type] || jQuery.event.special[type].teardown.call(elem, namespaces) === false) {
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
                    });
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
        trigger: function(event, data, elem, bubbling) {
            // Event object or event type
            var type = event.type || event;
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
                if (!elem || elem.nodeType == 3 || elem.nodeType == 8) {
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
            // Handle triggering native .onfoo handlers (and on links since we don't call .click() for links)
            if ((!elem[type] || jQuery.nodeName(elem, "a") && type == "click") && elem["on" + type] && elem["on" + type].apply(elem, data) === false) {
                "dk.brics.tajs.directives.unreachable";
                event.result = false;
            }
            // Trigger the native events (except for clicks on links)
            if (!bubbling && elem[type] && !event.isDefaultPrevented() && !(jQuery.nodeName(elem, "a") && type == "click")) {
                "dk.brics.tajs.directives.unreachable";
                this.triggered = true;
                try {
                    "dk.brics.tajs.directives.unreachable";
                    elem[type]();
                } catch (e) {}
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
            // returned undefined or false
            var all, handlers;
            event = arguments[0] = jQuery.event.fix(event || window.event);
            // Namespaced event handlers
            var namespaces = event.type.split(".");
            event.type = namespaces.shift();
            // Cache this now, all = true means, any handler
            all = !namespaces.length && !event.exclusive;
            var namespace = RegExp("(^|\\.)" + namespaces.slice().sort().join(".*\\.") + "(\\.|$)");
            handlers = (jQuery.data(this, "events") || {})[event.type];
            for (var j in handlers) {
                var handler = handlers[j];
                // Filter the functions by class
                if (all || namespace.test(handler.type)) {
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
        },
        props: "altKey attrChange attrName bubbles button cancelable charCode clientX clientY ctrlKey currentTarget data detail eventPhase fromElement handler keyCode metaKey newValue originalTarget pageX pageY prevValue relatedNode relatedTarget screenX screenY shiftKey srcElement target toElement view wheelDelta which".split(" "),
        fix: function(event) {
            if (event[expando]) {
                "dk.brics.tajs.directives.unreachable";
                return event;
            }
            // store a copy of the original event object
            // and "clone" to set read-only properties
            var originalEvent = event;
            event = jQuery.Event(originalEvent);
            for (var i = this.props.length, prop; i; ) {
                prop = this.props[--i];
                event[prop] = originalEvent[prop];
            }
            // Fix target property, if necessary
            if (!event.target) {
                "dk.brics.tajs.directives.unreachable";
                event.target = event.srcElement || document;
            }
            // Fixes #1925 where srcElement might not be defined either
            // check if target is a textnode (safari)
            if (event.target.nodeType == 3) {
                "dk.brics.tajs.directives.unreachable";
                event.target = event.target.parentNode;
            }
            // Add relatedTarget, if necessary
            if (!event.relatedTarget && event.fromElement) {
                "dk.brics.tajs.directives.unreachable";
                event.relatedTarget = event.fromElement == event.target ? event.toElement : event.fromElement;
            }
            // Calculate pageX/Y if missing and clientX/Y available
            if (event.pageX == null && event.clientX != null) {
                "dk.brics.tajs.directives.unreachable";
                var doc = document.documentElement, body = document.body;
                event.pageX = event.clientX + (doc && doc.scrollLeft || body && body.scrollLeft || 0) - (doc.clientLeft || 0);
                event.pageY = event.clientY + (doc && doc.scrollTop || body && body.scrollTop || 0) - (doc.clientTop || 0);
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
            // Add which for click: 1 == left; 2 == middle; 3 == right
            // Note: button is not normalized, so don't use it
            if (!event.which && event.button) {
                "dk.brics.tajs.directives.unreachable";
                event.which = event.button & 1 ? 1 : event.button & 2 ? 3 : event.button & 4 ? 2 : 0;
            }
            return event;
        },
        proxy: function(fn, proxy) {
            proxy = proxy || function() {
                "dk.brics.tajs.directives.unreachable";
                return fn.apply(this, arguments);
            };
            // Set the guid of unique handler to the same of original handler, so it can be removed
            proxy.guid = fn.guid = fn.guid || proxy.guid || this.guid++;
            // So proxy can be declared as an argument
            return proxy;
        },
        special: {
            ready: {
                // Make sure the ready event is setup
                setup: bindReady,
                teardown: function() {}
            }
        },
        specialAll: {
            live: {
                setup: function(selector, namespaces) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.add(this, namespaces[0], liveHandler);
                },
                teardown: function(namespaces) {
                    "dk.brics.tajs.directives.unreachable";
                    if (namespaces.length) {
                        "dk.brics.tajs.directives.unreachable";
                        var remove = 0, name = RegExp("(^|\\.)" + namespaces[0] + "(\\.|$)");
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
            this.originalEvent = src;
            this.type = src.type;
            this.timeStamp = src.timeStamp;
        } else {
            this.type = src;
        }
        if (!this.timeStamp) {
            this.timeStamp = now();
        }
        // Mark it as fixed
        this[expando] = true;
    };
    function returnFalse() {
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
        while (parent && parent != this) {
            "dk.brics.tajs.directives.unreachable";
            try {
                "dk.brics.tajs.directives.unreachable";
                parent = parent.parentNode;
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                parent = this;
            }
        }
        if (parent != this) {
            "dk.brics.tajs.directives.unreachable";
            // set the correct event type
            event.type = event.data;
            // handle event if we actually just moused on to a non sub-element
            jQuery.event.handle.apply(this, arguments);
        }
    };
    jQuery.each({
        mouseover: "mouseenter",
        mouseout: "mouseleave"
    }, function(orig, fix) {
        jQuery.event.special[fix] = {
            setup: function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.add(this, orig, withinElement, fix);
            },
            teardown: function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, orig, withinElement);
            }
        };
    });
    jQuery.fn.extend({
        bind: function(type, data, fn) {
            return type == "unload" ? this.one(type, data, fn) : this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.add(this, type, fn || data, fn && data);
            });
        },
        one: function(type, data, fn) {
            var one = jQuery.event.proxy(fn || data, function(event) {
                "dk.brics.tajs.directives.unreachable";
                jQuery(this).unbind(event, one);
                return (fn || data).apply(this, arguments);
            });
            return this.each(function() {
                jQuery.event.add(this, type, one, fn && data);
            });
        },
        unbind: function(type, fn) {
            "dk.brics.tajs.directives.unreachable";
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
                jQuery.event.proxy(fn, args[i++]);
            }
            return this.click(jQuery.event.proxy(fn, function(event) {
                "dk.brics.tajs.directives.unreachable";
                // Figure out which function to execute
                this.lastToggle = (this.lastToggle || 0) % i;
                // Make sure that clicks stop
                event.preventDefault();
                // and execute the function
                return args[this.lastToggle++].apply(this, arguments) || false;
            }));
        },
        hover: function(fnOver, fnOut) {
            "dk.brics.tajs.directives.unreachable";
            return this.mouseenter(fnOver).mouseleave(fnOut);
        },
        ready: function(fn) {
            // Attach the listeners
            bindReady();
            // If the DOM is already ready
            if (jQuery.isReady) // Execute the function immediately
            {
                "dk.brics.tajs.directives.unreachable";
                fn.call(document, jQuery);
            } else // Add the function to the wait list
            {
                jQuery.readyList.push(fn);
            }
            return this;
        },
        live: function(type, fn) {
            "dk.brics.tajs.directives.unreachable";
            var proxy = jQuery.event.proxy(fn);
            proxy.guid += this.selector + type;
            jQuery(document).bind(liveConvert(type, this.selector), this.selector, proxy);
            return this;
        },
        die: function(type, fn) {
            "dk.brics.tajs.directives.unreachable";
            jQuery(document).unbind(liveConvert(type, this.selector), fn ? {
                guid: fn.guid + this.selector + type
            } : null);
            return this;
        }
    });
    function liveHandler(event) {
        "dk.brics.tajs.directives.unreachable";
        var check = RegExp("(^|\\.)" + event.type + "(\\.|$)"), stop = true, elems = [];
        jQuery.each(jQuery.data(this, "events").live || [], function(i, fn) {
            "dk.brics.tajs.directives.unreachable";
            if (check.test(fn.type)) {
                "dk.brics.tajs.directives.unreachable";
                var elem = jQuery(event.target).closest(fn.data)[0];
                if (elem) {
                    "dk.brics.tajs.directives.unreachable";
                    elems.push({
                        elem: elem,
                        fn: fn
                    });
                }
            }
        });
        jQuery.each(elems, function() {
            "dk.brics.tajs.directives.unreachable";
            if (!event.isImmediatePropagationStopped() && this.fn.call(this.elem, event, this.fn.data) === false) {
                "dk.brics.tajs.directives.unreachable";
                stop = false;
            }
        });
        return stop;
    }
    function liveConvert(type, selector) {
        "dk.brics.tajs.directives.unreachable";
        return [ "live", type, selector.replace(/\./g, "`").replace(/ /g, "|") ].join(".");
    }
    jQuery.extend({
        isReady: false,
        readyList: [],
        // Handle when the DOM is ready
        ready: function() {
            // Make sure that the DOM is not already loaded
            if (!jQuery.isReady) {
                // Remember that the DOM is ready
                jQuery.isReady = true;
                // If there are functions bound, to execute
                if (jQuery.readyList) {
                    // Execute all of them
                    jQuery.each(jQuery.readyList, function() {
                        this.call(document, jQuery);
                    });
                    // Reset the list of functions
                    jQuery.readyList = null;
                }
                // Trigger any bound ready events
                jQuery(document).triggerHandler("ready");
            }
        }
    });
    var readyBound = false;
    function bindReady() {
        if (readyBound) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        readyBound = true;
        // Mozilla, Opera and webkit nightlies currently support this event
        if (document.addEventListener) {
            // Use the handy event callback
            document.addEventListener("DOMContentLoaded", function() {
                document.removeEventListener("DOMContentLoaded", arguments.callee, false);
                jQuery.ready();
            }, false);
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (document.attachEvent) {
                "dk.brics.tajs.directives.unreachable";
                // ensure firing before onload,
                // maybe late but safe also for iframes
                document.attachEvent("onreadystatechange", function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (document.readyState === "complete") {
                        "dk.brics.tajs.directives.unreachable";
                        document.detachEvent("onreadystatechange", arguments.callee);
                        jQuery.ready();
                    }
                });
                // If IE and not an iframe
                // continually check to see if the document is ready
                if (document.documentElement.doScroll && !window.frameElement) {
                    "dk.brics.tajs.directives.unreachable";
                    (function() {
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
                            setTimeout(arguments.callee, 0);
                            return;
                        }
                        // and execute any waiting functions
                        jQuery.ready();
                    })();
                }
            }
        }
        // A fallback to window.onload, that will always work
        jQuery.event.add(window, "load", jQuery.ready);
    }
    jQuery.each(("blur,focus,load,resize,scroll,unload,click,dblclick," + "mousedown,mouseup,mousemove,mouseover,mouseout,mouseenter,mouseleave," + "change,select,submit,keydown,keypress,keyup,error").split(","), function(i, name) {
        // Handle event binding
        jQuery.fn[name] = function(fn) {
            "dk.brics.tajs.directives.unreachable";
            return fn ? this.bind(name, fn) : this.trigger(name);
        };
    });
    // Prevent memory leaks in IE
    // And prevent errors on refresh with events like mouseover in other browsers
    // Window isn't included so as not to unbind existing unload events
    jQuery(window).bind("unload", function() {
        "dk.brics.tajs.directives.unreachable";
        for (var id in jQuery.cache) // Skip the window
        {
            "dk.brics.tajs.directives.unreachable";
            if (id != 1 && jQuery.cache[id].handle) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(jQuery.cache[id].handle.elem);
            }
        }
    });
    (function() {
        jQuery.support = {};
        var root = document.documentElement, script = document.createElement("script"), div = document.createElement("div"), id = "script" + "TAJS_UUID";
        div.style.display = "none";
        div.innerHTML = '   <link/><table></table><a href="/a" style="color:red;float:left;opacity:.5;">a</a><select><option>text</option></select><object><param/></object>';
        var all = div.getElementsByTagName("*"), a = div.getElementsByTagName("a")[0];
        // Can't get basic test support
        if (!all || !all.length || !a) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        jQuery.support = {
            // IE strips leading whitespace when .innerHTML is used
            leadingWhitespace: div.firstChild.nodeType == 3,
            // Make sure that tbody elements aren't automatically inserted
            // IE will insert them into empty tables
            tbody: !div.getElementsByTagName("tbody").length,
            // Make sure that you can get all elements in an <object> element
            // IE 7 always returns no results
            objectAll: !!div.getElementsByTagName("object")[0].getElementsByTagName("*").length,
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
            opacity: a.style.opacity === "0.5",
            // Verify style float existence
            // (IE uses styleFloat instead of cssFloat)
            cssFloat: !!a.style.cssFloat,
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
            div.attachEvent("onclick", function() {
                "dk.brics.tajs.directives.unreachable";
                // Cloning a node shouldn't copy over any
                // bound event handlers (IE does this)
                jQuery.support.noCloneEvent = false;
                div.detachEvent("onclick", arguments.callee);
            });
            div.cloneNode(true).fireEvent("onclick");
        }
        // Figure out if the W3C box model works as expected
        // document.body must exist before we can do this
        jQuery(function() {
            var div = document.createElement("div");
            div.style.width = "1px";
            div.style.paddingLeft = "1px";
            document.body.appendChild(div);
            jQuery.boxModel = jQuery.support.boxModel = div.offsetWidth === 2;
            document.body.removeChild(div);
        });
    })();
    var styleFloat = jQuery.support.cssFloat ? "cssFloat" : "styleFloat";
    jQuery.props = {
        "for": "htmlFor",
        "class": "className",
        "float": styleFloat,
        cssFloat: styleFloat,
        styleFloat: styleFloat,
        readonly: "readOnly",
        maxlength: "maxLength",
        cellspacing: "cellSpacing",
        rowspan: "rowSpan",
        tabindex: "tabIndex"
    };
    jQuery.fn.extend({
        // Keep a copy of the old load
        _load: jQuery.fn.load,
        load: function(url, params, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof url !== "string") {
                "dk.brics.tajs.directives.unreachable";
                return this._load(url);
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
            if (params) // If it's a function
            {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.isFunction(params)) {
                    "dk.brics.tajs.directives.unreachable";
                    // We assume that it's the callback
                    callback = params;
                    params = null;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (typeof params === "object") {
                        "dk.brics.tajs.directives.unreachable";
                        params = jQuery.param(params);
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
                complete: function(res, status) {
                    "dk.brics.tajs.directives.unreachable";
                    // If successful, inject the HTML into all the matched elements
                    if (status == "success" || status == "notmodified") // See if a selector was specified
                    {
                        "dk.brics.tajs.directives.unreachable";
                        self.html(selector ? // Create a dummy div to hold the results
                        jQuery("<div/>").append(res.responseText.replace(/<script(.|\s)*?\/script>/g, "")).find(selector) : // If not, just inject the full result
                        res.responseText);
                    }
                    if (callback) {
                        "dk.brics.tajs.directives.unreachable";
                        self.each(callback, [ res.responseText, status, res ]);
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
                return this.name && !this.disabled && (this.checked || /select|textarea/i.test(this.nodeName) || /text|hidden|password/i.test(this.type));
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
    jQuery.each("ajaxStart,ajaxStop,ajaxComplete,ajaxError,ajaxSuccess,ajaxSend".split(","), function(i, o) {
        jQuery.fn[o] = function(f) {
            "dk.brics.tajs.directives.unreachable";
            return this.bind(o, f);
        };
    });
    var jsc = now();
    jQuery.extend({
        get: function(url, data, callback, type) {
            "dk.brics.tajs.directives.unreachable";
            // shift arguments if data argument was ommited
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
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
            if (jQuery.isFunction(data)) {
                "dk.brics.tajs.directives.unreachable";
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
		*/
            // Create the request object; Microsoft failed to properly
            // implement the XMLHttpRequest in IE7, so we use the ActiveXObject when it is available
            // This function can be overriden by calling jQuery.ajaxSetup
            xhr: function() {
                "dk.brics.tajs.directives.unreachable";
                return window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest();
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
        ajax: function(s) {
            "dk.brics.tajs.directives.unreachable";
            // Extend the settings, but re-extend 's' so that it can be
            // checked again later (in the test suite, specifically)
            s = jQuery.extend(true, s, jQuery.extend(true, {}, jQuery.ajaxSettings, s));
            var jsonp, jsre = /=\?(&|$)/g, status, data, type = s.type.toUpperCase();
            // convert data if not already a string
            if (s.data && s.processData && typeof s.data !== "string") {
                "dk.brics.tajs.directives.unreachable";
                s.data = jQuery.param(s.data);
            }
            // Handle JSONP Parameter Callbacks
            if (s.dataType == "jsonp") {
                "dk.brics.tajs.directives.unreachable";
                if (type == "GET") {
                    "dk.brics.tajs.directives.unreachable";
                    if (!s.url.match(jsre)) {
                        "dk.brics.tajs.directives.unreachable";
                        s.url += (s.url.match(/\?/) ? "&" : "?") + (s.jsonp || "callback") + "=?";
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (!s.data || !s.data.match(jsre)) {
                        "dk.brics.tajs.directives.unreachable";
                        s.data = (s.data ? s.data + "&" : "") + (s.jsonp || "callback") + "=?";
                    }
                }
                s.dataType = "json";
            }
            // Build temporary JSONP function
            if (s.dataType == "json" && (s.data && s.data.match(jsre) || s.url.match(jsre))) {
                "dk.brics.tajs.directives.unreachable";
                jsonp = "jsonp" + jsc++;
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
                window[jsonp] = function(tmp) {
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
            if (s.dataType == "script" && s.cache == null) {
                "dk.brics.tajs.directives.unreachable";
                s.cache = false;
            }
            if (s.cache === false && type == "GET") {
                "dk.brics.tajs.directives.unreachable";
                var ts = now();
                // try replacing _= if it is there
                var ret = s.url.replace(/(\?|&)_=.*?(&|$)/, "$1_=" + ts + "$2");
                // if nothing was replaced, add timestamp to the end
                s.url = ret + (ret == s.url ? (s.url.match(/\?/) ? "&" : "?") + "_=" + ts : "");
            }
            // If data is available, append data to url for get requests
            if (s.data && type == "GET") {
                "dk.brics.tajs.directives.unreachable";
                s.url += (s.url.match(/\?/) ? "&" : "?") + s.data;
                // IE likes to send both get and post data, prevent this
                s.data = null;
            }
            // Watch for a new set of requests
            if (s.global && !jQuery.active++) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger("ajaxStart");
            }
            // Matches an absolute URL, and saves the domain
            var parts = /^(\w+:)?\/\/([^\/?#]+)/.exec(s.url);
            // If we're requesting a remote document
            // and trying to load JSON or Script with a GET
            if (s.dataType == "script" && type == "GET" && parts && (parts[1] && parts[1] != location.protocol || parts[2] != location.host)) {
                "dk.brics.tajs.directives.unreachable";
                var head = document.getElementsByTagName("head")[0];
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
                        if (!done && (!this.readyState || this.readyState == "loaded" || this.readyState == "complete")) {
                            "dk.brics.tajs.directives.unreachable";
                            done = true;
                            success();
                            complete();
                            head.removeChild(script);
                        }
                    };
                }
                head.appendChild(script);
                // We handle everything using the script element injection
                return undefined;
            }
            var requestDone = false;
            // Create the request object
            var xhr = s.xhr();
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
                if (s.data) {
                    "dk.brics.tajs.directives.unreachable";
                    xhr.setRequestHeader("Content-Type", s.contentType);
                }
                // Set the If-Modified-Since header, if ifModified mode.
                if (s.ifModified) {
                    "dk.brics.tajs.directives.unreachable";
                    xhr.setRequestHeader("If-Modified-Since", jQuery.lastModified[s.url] || "Thu, 01 Jan 1970 00:00:00 GMT");
                }
                // Set header so the called script knows that it's an XMLHttpRequest
                xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                // Set the Accepts header for the server, depending on the dataType
                xhr.setRequestHeader("Accept", s.dataType && s.accepts[s.dataType] ? s.accepts[s.dataType] + ", */*" : s.accepts._default);
            } catch (e) {}
            // Allow custom headers/mimetypes and early abort
            if (s.beforeSend && s.beforeSend(xhr, s) === false) {
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
                jQuery.event.trigger("ajaxSend", [ xhr, s ]);
            }
            // Wait for a response to come back
            var onreadystatechange = function(isTimeout) {
                "dk.brics.tajs.directives.unreachable";
                // The request was aborted, clear the interval and decrement jQuery.active
                if (xhr.readyState == 0) {
                    "dk.brics.tajs.directives.unreachable";
                    if (ival) {
                        "dk.brics.tajs.directives.unreachable";
                        // clear poll interval
                        clearInterval(ival);
                        ival = null;
                        // Handle the global AJAX counter
                        if (s.global && !--jQuery.active) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.event.trigger("ajaxStop");
                        }
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (!requestDone && xhr && (xhr.readyState == 4 || isTimeout == "timeout")) {
                        "dk.brics.tajs.directives.unreachable";
                        requestDone = true;
                        // clear poll interval
                        if (ival) {
                            "dk.brics.tajs.directives.unreachable";
                            clearInterval(ival);
                            ival = null;
                        }
                        status = isTimeout == "timeout" ? "timeout" : !jQuery.httpSuccess(xhr) ? "error" : s.ifModified && jQuery.httpNotModified(xhr, s.url) ? "notmodified" : "success";
                        if (status == "success") {
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
                        if (status == "success") {
                            "dk.brics.tajs.directives.unreachable";
                            // Cache Last-Modified header, if ifModified mode.
                            var modRes;
                            try {
                                "dk.brics.tajs.directives.unreachable";
                                modRes = xhr.getResponseHeader("Last-Modified");
                            } catch (e) {}
                            // swallow exception thrown by FF if header is not available
                            if (s.ifModified && modRes) {
                                "dk.brics.tajs.directives.unreachable";
                                jQuery.lastModified[s.url] = modRes;
                            }
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
                        // Stop memory leaks
                        if (s.async) {
                            "dk.brics.tajs.directives.unreachable";
                            xhr = null;
                        }
                    }
                }
            };
            if (s.async) {
                "dk.brics.tajs.directives.unreachable";
                // don't attach the handler to the request, just poll it instead
                var ival = setInterval(onreadystatechange, 13);
                // Timeout checker
                if (s.timeout > 0) {
                    "dk.brics.tajs.directives.unreachable";
                    setTimeout(function() {
                        "dk.brics.tajs.directives.unreachable";
                        // Check to see if the request is still happening
                        if (xhr) {
                            "dk.brics.tajs.directives.unreachable";
                            if (!requestDone) {
                                "dk.brics.tajs.directives.unreachable";
                                onreadystatechange("timeout");
                            }
                            // Cancel the request
                            if (xhr) {
                                "dk.brics.tajs.directives.unreachable";
                                xhr.abort();
                            }
                        }
                    }, s.timeout);
                }
            }
            // Send the data
            try {
                "dk.brics.tajs.directives.unreachable";
                xhr.send(s.data);
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.handleError(s, xhr, null, e);
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
                    s.success(data, status);
                }
                // Fire the global callback
                if (s.global) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger("ajaxSuccess", [ xhr, s ]);
                }
            }
            function complete() {
                "dk.brics.tajs.directives.unreachable";
                // Process result
                if (s.complete) {
                    "dk.brics.tajs.directives.unreachable";
                    s.complete(xhr, status);
                }
                // The request was completed
                if (s.global) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger("ajaxComplete", [ xhr, s ]);
                }
                // Handle the global AJAX counter
                if (s.global && !--jQuery.active) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger("ajaxStop");
                }
            }
            // return XMLHttpRequest to allow aborting the request etc.
            return xhr;
        },
        handleError: function(s, xhr, status, e) {
            "dk.brics.tajs.directives.unreachable";
            // If a local callback was specified, fire it
            if (s.error) {
                "dk.brics.tajs.directives.unreachable";
                s.error(xhr, status, e);
            }
            // Fire the global callback
            if (s.global) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger("ajaxError", [ xhr, s, e ]);
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
                return !xhr.status && location.protocol == "file:" || xhr.status >= 200 && xhr.status < 300 || xhr.status == 304 || xhr.status == 1223;
            } catch (e) {}
            return false;
        },
        // Determines if an XMLHttpRequest returns NotModified
        httpNotModified: function(xhr, url) {
            "dk.brics.tajs.directives.unreachable";
            try {
                "dk.brics.tajs.directives.unreachable";
                var xhrRes = xhr.getResponseHeader("Last-Modified");
                // Firefox always returns 200. check Last-Modified date
                return xhr.status == 304 || xhrRes == jQuery.lastModified[url];
            } catch (e) {}
            return false;
        },
        httpData: function(xhr, type, s) {
            "dk.brics.tajs.directives.unreachable";
            var ct = xhr.getResponseHeader("content-type"), xml = type == "xml" || !type && ct && ct.indexOf("xml") >= 0, data = xml ? xhr.responseXML : xhr.responseText;
            if (xml && data.documentElement.tagName == "parsererror") {
                "dk.brics.tajs.directives.unreachable";
                throw "parsererror";
            }
            // Allow a pre-filtering function to sanitize the response
            // s != null is checked to keep backwards compatibility
            if (s && s.dataFilter) {
                "dk.brics.tajs.directives.unreachable";
                data = s.dataFilter(data, type);
            }
            // The filter can actually parse the response
            if (typeof data === "string") {
                "dk.brics.tajs.directives.unreachable";
                // If the type is "script", eval it in global context
                if (type == "script") {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.globalEval(data);
                }
                // Get the JavaScript object, if JSON is used.
                if (type == "json") {
                    "dk.brics.tajs.directives.unreachable";
                    data = window["eval"]("(" + data + ")");
                }
            }
            return data;
        },
        // Serialize an array of form elements or a set of
        // key/values into a query string
        param: function(a) {
            "dk.brics.tajs.directives.unreachable";
            var s = [];
            function add(key, value) {
                "dk.brics.tajs.directives.unreachable";
                s[s.length] = encodeURIComponent(key) + "=" + encodeURIComponent(value);
            }
            // If an array was passed in, assume that it is an array
            // of form elements
            if (jQuery.isArray(a) || a.jquery) // Serialize the form elements
            {
                "dk.brics.tajs.directives.unreachable";
                jQuery.each(a, function() {
                    "dk.brics.tajs.directives.unreachable";
                    add(this.name, this.value);
                });
            } else // Serialize the key/values
            {
                "dk.brics.tajs.directives.unreachable";
                for (var j in a) // If the value is an array then the key names need to be repeated
                {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.isArray(a[j])) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.each(a[j], function() {
                            "dk.brics.tajs.directives.unreachable";
                            add(j, this);
                        });
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        add(j, jQuery.isFunction(a[j]) ? a[j]() : a[j]);
                    }
                }
            }
            // Return the resulting serialization
            return s.join("&").replace(/%20/g, "+");
        }
    });
    var elemdisplay = {}, fxAttrs = [ // height animations
    [ "height", "marginTop", "marginBottom", "paddingTop", "paddingBottom" ], // width animations
    [ "width", "marginLeft", "marginRight", "paddingLeft", "paddingRight" ], // opacity animations
    [ "opacity" ] ];
    function genFx(type, num) {
        var obj = {};
        for(var i = 0; i < num; i++){
            jQuery.each(fxAttrs[i], function() {
                obj[this] = type;
            });
        }
        return obj;
    }
    jQuery.fn.extend({
        show: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (speed) {
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
                        var tagName = this[i].tagName, display;
                        if (elemdisplay[tagName]) {
                            "dk.brics.tajs.directives.unreachable";
                            display = elemdisplay[tagName];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            var elem = jQuery("<" + tagName + " />").appendTo("body");
                            display = elem.css("display");
                            if (display === "none") {
                                "dk.brics.tajs.directives.unreachable";
                                display = "block";
                            }
                            elem.remove();
                            elemdisplay[tagName] = display;
                        }
                        this[i].style.display = jQuery.data(this[i], "olddisplay", display);
                    }
                }
                return this;
            }
        },
        hide: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (speed) {
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
                    this[i].style.display = "none";
                }
                return this;
            }
        },
        // Save the old toggle function
        _toggle: jQuery.fn.toggle,
        toggle: function(fn, fn2) {
            "dk.brics.tajs.directives.unreachable";
            var bool = typeof fn === "boolean";
            return jQuery.isFunction(fn) && jQuery.isFunction(fn2) ? this._toggle.apply(this, arguments) : fn == null || bool ? this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                var state = bool ? fn : jQuery(this).is(":hidden");
                if(state){
                    jQuery(this).show();                            
                }else{
                    jQuery(this).hide();
                }
            }) : this.animate(genFx("toggle", 3), fn, fn2);
        },
        fadeTo: function(speed, to, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate({
                opacity: to
            }, speed, callback);
        },
        animate: function(prop, speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            var optall = jQuery.speed(speed, easing, callback);
            var f = function() {
                "dk.brics.tajs.directives.unreachable";
                var opt = jQuery.extend({}, optall), p, hidden = this.nodeType == 1 && jQuery(this).is(":hidden"), self = this;
                for (p in prop) {
                    "dk.brics.tajs.directives.unreachable";
                    if (prop[p] == "hide" && hidden || prop[p] == "show" && !hidden) {
                        "dk.brics.tajs.directives.unreachable";
                        return opt.complete.call(this);
                    }
                    if ((p == "height" || p == "width") && this.style) {
                        "dk.brics.tajs.directives.unreachable";
                        // Store display property
                        opt.display = jQuery.css(this, "display");
                        // Make sure that nothing sneaks out
                        opt.overflow = this.style.overflow;
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
                    if (/toggle|show|hide/.test(val)) {
                        "dk.brics.tajs.directives.unreachable";
                        if(val === "toggle"){
                            if(hidden){
                                e.show(prop);
                            }else{
                                e.hide(prop);
                            }
                        }else{
                            e[val](prop);
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        var parts = val.toString().match(/^([+-]=)?([\d+-.]+)(.*)$/), start = e.cur(true) || 0;
                        if (parts) {
                            "dk.brics.tajs.directives.unreachable";
                            var end = parseFloat(parts[2]), unit = parts[3] || "px";
                            // We need to compute starting value
                            if (unit != "px") {
                                "dk.brics.tajs.directives.unreachable";
                                self.style[name] = (end || 1) + unit;
                                start = (end || 1) / e.cur(true) * start;
                                self.style[name] = start + unit;
                            }
                            // If a +=/-= token was provided, we're doing a relative animation
                            if (parts[1]) {
                                "dk.brics.tajs.directives.unreachable";
                                end = (parts[1] == "-=" ? -1 : 1) * end + start;
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
            };
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
                    if (timers[i].elem == this) {
                        "dk.brics.tajs.directives.unreachable";
                        if (gotoEnd) // force the next step to be the last
                        {
                            "dk.brics.tajs.directives.unreachable";
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
            var opt = typeof speed === "object" ? speed : {
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
        timerId: null,
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
            if ((this.prop == "height" || this.prop == "width") && this.elem.style) {
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
            jQuery.timers.push(t);
            if (t() && jQuery.timerId == null) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.timerId = setInterval(function() {
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
                        clearInterval(jQuery.timerId);
                        jQuery.timerId = null;
                    }
                }, 13);
            }
        },
        // Simple 'show' function
        show: function() {
            "dk.brics.tajs.directives.unreachable";
            // Remember where we started, so that we can go back to it later
            this.options.orig[this.prop] = jQuery.attr(this.elem.style, this.prop);
            this.options.show = true;
            // Begin the animation
            // Make sure that we start at a small width/height to avoid any
            // flash of content
            this.custom(this.prop == "width" || this.prop == "height" ? 1 : 0, this.cur());
            // Start by showing the element
            jQuery(this.elem).show();
        },
        // Simple 'hide' function
        hide: function() {
            "dk.brics.tajs.directives.unreachable";
            // Remember where we started, so that we can go back to it later
            this.options.orig[this.prop] = jQuery.attr(this.elem.style, this.prop);
            this.options.hide = true;
            // Begin the animation
            this.custom(this.cur(), 0);
        },
        // Each step of an animation
        step: function(gotoEnd) {
            "dk.brics.tajs.directives.unreachable";
            var t = now();
            if (gotoEnd || t >= this.options.duration + this.startTime) {
                "dk.brics.tajs.directives.unreachable";
                this.now = this.end;
                this.pos = this.state = 1;
                this.update();
                this.options.curAnim[this.prop] = true;
                var done = true;
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
                        this.elem.style.display = this.options.display;
                        if (jQuery.css(this.elem, "display") == "none") {
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
                            jQuery.attr(this.elem.style, p, this.options.orig[p]);
                        }
                    }
                }
                if (done) // Execute the complete function
                {
                    "dk.brics.tajs.directives.unreachable";
                    this.options.complete.call(this.elem);
                }
                return false;
            } else {
                "dk.brics.tajs.directives.unreachable";
                var n = t - this.startTime;
                this.state = n / this.options.duration;
                // Perform the easing function, defaults to swing
                this.pos = jQuery.easing[this.options.easing || (jQuery.easing.swing ? "swing" : "linear")](this.state, n, 0, 1, this.options.duration);
                this.now = this.start + (this.end - this.start) * this.pos;
                // Perform the next step of the animation
                this.update();
            }
            return true;
        }
    };
    jQuery.extend(jQuery.fx, {
        speeds: {
            slow: 600,
            fast: 200,
            // Default speed
            _default: 400
        },
        step: {
            opacity: function(fx) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.attr(fx.elem.style, "opacity", fx.now);
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
    if (document.documentElement["getBoundingClientRect"]) {
        jQuery.fn.offset = function() {
            "dk.brics.tajs.directives.unreachable";
            if (!this[0]) {
                "dk.brics.tajs.directives.unreachable";
                return {
                    top: 0,
                    left: 0
                };
            }
            if (this[0] === this[0].ownerDocument.body) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.offset.bodyOffset(this[0]);
            }
            var box = this[0].getBoundingClientRect(), doc = this[0].ownerDocument, body = doc.body, docElem = doc.documentElement, clientTop = docElem.clientTop || body.clientTop || 0, clientLeft = docElem.clientLeft || body.clientLeft || 0, top = box.top + (self.pageYOffset || jQuery.boxModel && docElem.scrollTop || body.scrollTop) - clientTop, left = box.left + (self.pageXOffset || jQuery.boxModel && docElem.scrollLeft || body.scrollLeft) - clientLeft;
            return {
                top: top,
                left: left
            };
        };
    } else {
        "dk.brics.tajs.directives.unreachable";
        jQuery.fn.offset = function() {
            "dk.brics.tajs.directives.unreachable";
            if (!this[0]) {
                "dk.brics.tajs.directives.unreachable";
                return {
                    top: 0,
                    left: 0
                };
            }
            if (this[0] === this[0].ownerDocument.body) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.offset.bodyOffset(this[0]);
            }
            jQuery.offset.initialized || jQuery.offset.initialize();
            var elem = this[0], offsetParent = elem.offsetParent, prevOffsetParent = elem, doc = elem.ownerDocument, computedStyle, docElem = doc.documentElement, body = doc.body, defaultView = doc.defaultView, prevComputedStyle = defaultView.getComputedStyle(elem, null), top = elem.offsetTop, left = elem.offsetLeft;
            while ((elem = elem.parentNode) && elem !== body && elem !== docElem) {
                "dk.brics.tajs.directives.unreachable";
                computedStyle = defaultView.getComputedStyle(elem, null);
                top -= elem.scrollTop, left -= elem.scrollLeft;
                if (elem === offsetParent) {
                    "dk.brics.tajs.directives.unreachable";
                    top += elem.offsetTop, left += elem.offsetLeft;
                    if (jQuery.offset.doesNotAddBorder && !(jQuery.offset.doesAddBorderForTableAndCells && /^t(able|d|h)$/i.test(elem.tagName))) {
                        "dk.brics.tajs.directives.unreachable";
                        top += parseInt(computedStyle.borderTopWidth, 10) || 0, left += parseInt(computedStyle.borderLeftWidth, 10) || 0;
                    }
                    prevOffsetParent = offsetParent, offsetParent = elem.offsetParent;
                }
                if (jQuery.offset.subtractsBorderForOverflowNotVisible && computedStyle.overflow !== "visible") {
                    "dk.brics.tajs.directives.unreachable";
                    top += parseInt(computedStyle.borderTopWidth, 10) || 0, left += parseInt(computedStyle.borderLeftWidth, 10) || 0;
                }
                prevComputedStyle = computedStyle;
            }
            if (prevComputedStyle.position === "relative" || prevComputedStyle.position === "static") {
                "dk.brics.tajs.directives.unreachable";
                top += body.offsetTop, left += body.offsetLeft;
            }
            if (prevComputedStyle.position === "fixed") {
                "dk.brics.tajs.directives.unreachable";
                top += Math.max(docElem.scrollTop, body.scrollTop), left += Math.max(docElem.scrollLeft, body.scrollLeft);
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
            if (this.initialized) {
                "dk.brics.tajs.directives.unreachable";
                return;
            }
            var body = document.body, container = document.createElement("div"), innerDiv, checkDiv, table, td, rules, prop, bodyMarginTop = body.style.marginTop, html = '<div style="position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;"><div></div></div><table style="position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;"cellpadding="0"cellspacing="0"><tr><td></td></tr></table>';
            rules = {
                position: "absolute",
                top: 0,
                left: 0,
                margin: 0,
                border: 0,
                width: "1px",
                height: "1px",
                visibility: "hidden"
            };
            for (prop in rules) {
                "dk.brics.tajs.directives.unreachable";
                container.style[prop] = rules[prop];
            }
            container.innerHTML = html;
            body.insertBefore(container, body.firstChild);
            innerDiv = container.firstChild, checkDiv = innerDiv.firstChild, td = innerDiv.nextSibling.firstChild.firstChild;
            this.doesNotAddBorder = checkDiv.offsetTop !== 5;
            this.doesAddBorderForTableAndCells = td.offsetTop === 5;
            innerDiv.style.overflow = "hidden", innerDiv.style.position = "relative";
            this.subtractsBorderForOverflowNotVisible = checkDiv.offsetTop === -5;
            body.style.marginTop = "1px";
            this.doesNotIncludeMarginInBodyOffset = body.offsetTop === 0;
            body.style.marginTop = bodyMarginTop;
            body.removeChild(container);
            this.initialized = true;
        },
        bodyOffset: function(body) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.offset.initialized || jQuery.offset.initialize();
            var top = body.offsetTop, left = body.offsetLeft;
            if (jQuery.offset.doesNotIncludeMarginInBodyOffset) {
                "dk.brics.tajs.directives.unreachable";
                top += parseInt(jQuery.curCSS(body, "marginTop", true), 10) || 0, left += parseInt(jQuery.curCSS(body, "marginLeft", true), 10) || 0;
            }
            return {
                top: top,
                left: left
            };
        }
    };
    jQuery.fn.extend({
        position: function() {
            "dk.brics.tajs.directives.unreachable";
            var left = 0, top = 0, results;
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                // Get *real* offsetParent
                var offsetParent = this.offsetParent(), // Get correct offsets
                offset = this.offset(), parentOffset = /^body|html$/i.test(offsetParent[0].tagName) ? {
                    top: 0,
                    left: 0
                } : offsetParent.offset();
                // Subtract element margins
                // note: when an element has margin: auto the offsetLeft and marginLeft 
                // are the same in Safari causing offset.left to incorrectly be 0
                offset.top -= num(this, "marginTop");
                offset.left -= num(this, "marginLeft");
                // Add offsetParent borders
                parentOffset.top += num(offsetParent, "borderTopWidth");
                parentOffset.left += num(offsetParent, "borderLeftWidth");
                // Subtract the two offsets
                results = {
                    top: offset.top - parentOffset.top,
                    left: offset.left - parentOffset.left
                };
            }
            return results;
        },
        offsetParent: function() {
            "dk.brics.tajs.directives.unreachable";
            var offsetParent = this[0].offsetParent || document.body;
            while (offsetParent && !/^body|html$/i.test(offsetParent.tagName) && jQuery.css(offsetParent, "position") == "static") {
                "dk.brics.tajs.directives.unreachable";
                offsetParent = offsetParent.offsetParent;
            }
            return jQuery(offsetParent);
        }
    });
    // Create scrollLeft and scrollTop methods
    jQuery.each([ "Left", "Top" ], function(i, name) {
        var method = "scroll" + name;
        jQuery.fn[method] = function(val) {
            "dk.brics.tajs.directives.unreachable";
            if (!this[0]) {
                "dk.brics.tajs.directives.unreachable";
                return null;
            }
            return val !== undefined ? // Set the scroll offset
            this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                this == window || this == document ? window.scrollTo(!i ? val : jQuery(window).scrollLeft(), i ? val : jQuery(window).scrollTop()) : this[method] = val;
            }) : // Return the scroll offset
            this[0] == window || this[0] == document ? self[i ? "pageYOffset" : "pageXOffset"] || jQuery.boxModel && document.documentElement[method] || document.body[method] : this[0][method];
        };
    });
    // Create innerHeight, innerWidth, outerHeight and outerWidth methods
    jQuery.each([ "Height", "Width" ], function(i, name) {
        var tl = i ? "Left" : "Top", // top or left
        br = i ? "Right" : "Bottom";
        // bottom or right
        // innerHeight and innerWidth
        jQuery.fn["inner" + name] = function() {
            "dk.brics.tajs.directives.unreachable";
            return this[name.toLowerCase()]() + num(this, "padding" + tl) + num(this, "padding" + br);
        };
        // outerHeight and outerWidth
        jQuery.fn["outer" + name] = function(margin) {
            "dk.brics.tajs.directives.unreachable";
            return this["inner" + name]() + num(this, "border" + tl + "Width") + num(this, "border" + br + "Width") + (margin ? num(this, "margin" + tl) + num(this, "margin" + br) : 0);
        };
        var type = name.toLowerCase();
        jQuery.fn[type] = function(size) {
            "dk.brics.tajs.directives.unreachable";
            // Get window width or height
            return this[0] == window ? // Everyone else use document.documentElement or document.body depending on Quirks vs Standards mode
            document.compatMode == "CSS1Compat" && document.documentElement["client" + name] || document.body["client" + name] : // Get document width or height
            this[0] == document ? // Either scroll[Width/Height] or offset[Width/Height], whichever is greater
            Math.max(document.documentElement["client" + name], document.body["scroll" + name], document.documentElement["scroll" + name], document.body["offset" + name], document.documentElement["offset" + name]) : // Get or set width or height on the element
            size === undefined ? // Get width or height on the element
            this.length ? jQuery.css(this[0], type) : null : // Set the width or height on the element (default to pixels if value is unitless)
            this.css(type, typeof size === "string" ? size : size + "px");
        };
    });
})();
