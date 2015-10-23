(function() {
    /*
 * jQuery 1.2 - New Wave Javascript
 *
 * Copyright (c) 2007 John Resig (jquery.com)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * $Date$
 * $Rev$
 */
    // Map over jQuery in case of overwrite
    if (typeof jQuery != "undefined") {
        "dk.brics.tajs.directives.unreachable";
        var _jQuery = jQuery;
    }
    var jQuery = window.jQuery = function(a, c) {
        "dk.brics.tajs.directives.unreachable";
        // If the context is global, return a new object
        if (window == this || !this.init) {
            "dk.brics.tajs.directives.unreachable";
            return new jQuery(a, c);
        }
        return this.init(a, c);
    };
    // Map over the $ in case of overwrite
    if (typeof $ != "undefined") {
        "dk.brics.tajs.directives.unreachable";
        var _$ = $;
    }
    // Map the jQuery namespace to the '$' one
    window.$ = jQuery;
    var quickExpr = /^[^<]*(<(.|\s)+>)[^>]*$|^#(\w+)$/;
    jQuery.fn = jQuery.prototype = {
        init: function(a, c) {
            "dk.brics.tajs.directives.unreachable";
            // Make sure that a selection was provided
            a = a || document;
            // Handle HTML strings
            if (typeof a == "string") {
                "dk.brics.tajs.directives.unreachable";
                var m = quickExpr.exec(a);
                if (m && (m[1] || !c)) {
                    "dk.brics.tajs.directives.unreachable";
                    // HANDLE: $(html) -> $(array)
                    if (m[1]) {
                        "dk.brics.tajs.directives.unreachable";
                        a = jQuery.clean([ m[1] ], c);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        var tmp = document.getElementById(m[3]);
                        if (tmp) // Handle the case where IE and Opera return items
                        // by name instead of ID
                        {
                            "dk.brics.tajs.directives.unreachable";
                            if (tmp.id != m[3]) {
                                "dk.brics.tajs.directives.unreachable";
                                return jQuery().find(a);
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                this[0] = tmp;
                                this.length = 1;
                                return this;
                            }
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            a = [];
                        }
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    return new jQuery(c).find(a);
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.isFunction(a)) {
                    "dk.brics.tajs.directives.unreachable";
                    if(jQuery.fn.ready){
                        return new jQuery(document).ready(a);
                    }else{
                        return new jQuery(document).load(a);
                    }
                }
            }
            return this.setArray(// HANDLE: $(array)
            a.constructor == Array && a || // HANDLE: $(arraylike)
            // Watch for when an array-like object is passed as the selector
            (a.jquery || a.length && a != window && !a.nodeType && a[0] != undefined && a[0].nodeType) && jQuery.makeArray(a) || // HANDLE: $(*)
            [ a ]);
        },
        jquery: "1.2",
        size: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.length;
        },
        length: 0,
        get: function(num) {
            "dk.brics.tajs.directives.unreachable";
            return num == undefined ? // Return a 'clean' array
            jQuery.makeArray(this) : // Return just the object
            this[num];
        },
        pushStack: function(a) {
            "dk.brics.tajs.directives.unreachable";
            var ret = jQuery(a);
            ret.prevObject = this;
            return ret;
        },
        setArray: function(a) {
            "dk.brics.tajs.directives.unreachable";
            this.length = 0;
            Array.prototype.push.apply(this, a);
            return this;
        },
        each: function(fn, args) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.each(this, fn, args);
        },
        index: function(obj) {
            "dk.brics.tajs.directives.unreachable";
            var pos = -1;
            this.each(function(i) {
                "dk.brics.tajs.directives.unreachable";
                if (this == obj) {
                    "dk.brics.tajs.directives.unreachable";
                    pos = i;
                }
            });
            return pos;
        },
        attr: function(key, value, type) {
            "dk.brics.tajs.directives.unreachable";
            var obj = key;
            // Look for the case where we're accessing a style value
            if (key.constructor == String) {
                "dk.brics.tajs.directives.unreachable";
                if (value == undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    return this.length && jQuery[type || "attr"](this[0], key) || undefined;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    obj = {};
                    obj[key] = value;
                }
            }
            // Check to see if we're setting style values
            return this.each(function(index) {
                "dk.brics.tajs.directives.unreachable";
                // Set all the styles
                for (var prop in obj) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.attr(type ? this.style : this, prop, jQuery.prop(this, obj[prop], type, index, prop));
                }
            });
        },
        css: function(key, value) {
            "dk.brics.tajs.directives.unreachable";
            return this.attr(key, value, "curCSS");
        },
        text: function(e) {
            "dk.brics.tajs.directives.unreachable";
            if (typeof e != "object" && e != null) {
                "dk.brics.tajs.directives.unreachable";
                return this.empty().append(document.createTextNode(e));
            }
            var t = "";
            jQuery.each(e || this, function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.each(this.childNodes, function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (this.nodeType != 8) {
                        "dk.brics.tajs.directives.unreachable";
                        t += this.nodeType != 1 ? this.nodeValue : jQuery.fn.text([ this ]);
                    }
                });
            });
            return t;
        },
        wrapAll: function(html) {
            "dk.brics.tajs.directives.unreachable";
            if (this[0]) // The elements to wrap the target around
            {
                "dk.brics.tajs.directives.unreachable";
                jQuery(html, this[0].ownerDocument).clone().insertBefore(this[0]).map(function() {
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
            return this.domManip(arguments, true, 1, function(a) {
                "dk.brics.tajs.directives.unreachable";
                this.appendChild(a);
            });
        },
        prepend: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, true, -1, function(a) {
                "dk.brics.tajs.directives.unreachable";
                this.insertBefore(a, this.firstChild);
            });
        },
        before: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, false, 1, function(a) {
                "dk.brics.tajs.directives.unreachable";
                this.parentNode.insertBefore(a, this);
            });
        },
        after: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.domManip(arguments, false, -1, function(a) {
                "dk.brics.tajs.directives.unreachable";
                this.parentNode.insertBefore(a, this.nextSibling);
            });
        },
        end: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.prevObject || jQuery([]);
        },
        find: function(t) {
            "dk.brics.tajs.directives.unreachable";
            var data = jQuery.map(this, function(a) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.find(t, a);
            });
            return this.pushStack(/[^+>] [^+>]/.test(t) || t.indexOf("..") > -1 ? jQuery.unique(data) : data);
        },
        clone: function(events) {
            "dk.brics.tajs.directives.unreachable";
            // Do the clone
            var ret = this.map(function() {
                "dk.brics.tajs.directives.unreachable";
                return this.outerHTML ? jQuery(this.outerHTML)[0] : this.cloneNode(true);
            });
            if (events === true) {
                "dk.brics.tajs.directives.unreachable";
                var clone = ret.find("*").andSelf();
                this.find("*").andSelf().each(function(i) {
                    "dk.brics.tajs.directives.unreachable";
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
        filter: function(t) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(jQuery.isFunction(t) && jQuery.grep(this, function(el, index) {
                "dk.brics.tajs.directives.unreachable";
                return t.apply(el, [ index ]);
            }) || jQuery.multiFilter(t, this));
        },
        not: function(t) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(t.constructor == String && jQuery.multiFilter(t, this, true) || jQuery.grep(this, function(a) {
                "dk.brics.tajs.directives.unreachable";
                return t.constructor == Array || t.jquery ? jQuery.inArray(a, t) < 0 : a != t;
            }));
        },
        add: function(t) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(jQuery.merge(this.get(), t.constructor == String ? jQuery(t).get() : t.length != undefined && (!t.nodeName || t.nodeName == "FORM") ? t : [ t ]));
        },
        is: function(expr) {
            "dk.brics.tajs.directives.unreachable";
            return expr ? jQuery.multiFilter(expr, this).length > 0 : false;
        },
        hasClass: function(expr) {
            "dk.brics.tajs.directives.unreachable";
            return this.is("." + expr);
        },
        val: function(val) {
            "dk.brics.tajs.directives.unreachable";
            if (val == undefined) {
                "dk.brics.tajs.directives.unreachable";
                if (this.length) {
                    "dk.brics.tajs.directives.unreachable";
                    var elem = this[0];
                    // We need to handle select boxes special
                    if (jQuery.nodeName(elem, "select")) {
                        "dk.brics.tajs.directives.unreachable";
                        var index = elem.selectedIndex, a = [], options = elem.options, one = elem.type == "select-one";
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
                                var val = jQuery.browser.msie && !option.attributes["value"].specified ? option.text : option.value;
                                // We don't need an array for one selects
                                if (one) {
                                    "dk.brics.tajs.directives.unreachable";
                                    return val;
                                }
                                // Multi-Selects return an array
                                a.push(val);
                            }
                        }
                        return a;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        return this[0].value.replace(/\r/g, "");
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                return this.each(function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (val.constructor == Array && /radio|checkbox/.test(this.type)) {
                        "dk.brics.tajs.directives.unreachable";
                        this.checked = jQuery.inArray(this.value, val) >= 0 || jQuery.inArray(this.name, val) >= 0;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (jQuery.nodeName(this, "select")) {
                            "dk.brics.tajs.directives.unreachable";
                            var tmp = val.constructor == Array ? val : [ val ];
                            jQuery("option", this).each(function() {
                                "dk.brics.tajs.directives.unreachable";
                                this.selected = jQuery.inArray(this.value, tmp) >= 0 || jQuery.inArray(this.text, tmp) >= 0;
                            });
                            if (!tmp.length) {
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
        },
        html: function(val) {
            "dk.brics.tajs.directives.unreachable";
            return val == undefined ? this.length ? this[0].innerHTML : null : this.empty().append(val);
        },
        replaceWith: function(val) {
            "dk.brics.tajs.directives.unreachable";
            return this.after(val).remove();
        },
        slice: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(Array.prototype.slice.apply(this, arguments));
        },
        map: function(fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.pushStack(jQuery.map(this, function(elem, i) {
                "dk.brics.tajs.directives.unreachable";
                return fn.call(elem, i, elem);
            }));
        },
        andSelf: function() {
            "dk.brics.tajs.directives.unreachable";
            return this.add(this.prevObject);
        },
        domManip: function(args, table, dir, fn) {
            "dk.brics.tajs.directives.unreachable";
            var clone = this.length > 1, a;
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (!a) {
                    "dk.brics.tajs.directives.unreachable";
                    a = jQuery.clean(args, this.ownerDocument);
                    if (dir < 0) {
                        "dk.brics.tajs.directives.unreachable";
                        a.reverse();
                    }
                }
                var obj = this;
                if (table && jQuery.nodeName(this, "table") && jQuery.nodeName(a[0], "tr")) {
                    "dk.brics.tajs.directives.unreachable";
                    obj = this.getElementsByTagName("tbody")[0] || this.appendChild(document.createElement("tbody"));
                }
                jQuery.each(a, function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.nodeName(this, "script")) {
                        "dk.brics.tajs.directives.unreachable";
                        if (this.src) {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.ajax({
                                url: this.src,
                                async: false,
                                dataType: "script"
                            });
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            jQuery.globalEval(this.text || this.textContent || this.innerHTML || "");
                        }
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        fn.apply(obj, [ clone ? this.cloneNode(true) : this ]);
                    }
                });
            });
        }
    };
    jQuery.extend = jQuery.fn.extend = function(ARG1, ARG2, ARG3) {
        // copy reference to target object
        var target = arguments[0] || {}, a = 1, al = arguments.length, deep = false;
        // Handle a deep copy situation
        if (target.constructor == Boolean) {
            "dk.brics.tajs.directives.unreachable";
            deep = target;
            target = arguments[1] || {};
        }
        // extend jQuery itself if only one argument is passed
        if (al == 1) {
            target = this;
            a = 0;
        }
        var prop;
        for (;a < al; a++) // Only deal with non-null/undefined values
        {
            if ((prop = arguments[a]) != null) // Extend the base object
            {
                for (var i in prop) {
                    // Prevent never-ending loop
                    if (target == prop[i]) {
                        "dk.brics.tajs.directives.unreachable";
                        continue;
                    }
                    // Recurse if we're merging object values
                    if (deep && typeof prop[i] == "object" && target[i]) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.extend(target[i], prop[i]);
                    } else {
                        if (prop[i] != undefined) {
                            target[i] = prop[i];
                        }
                    }
                }
            }
        }
        // Return the modified object
        return target;
    };
    var expando = "jQuery" + "TAJS_UUID", uuid = 0, win = {};
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
        // This may seem like some crazy code, but trust me when I say that this
        // is the only cross-browser way to do this. --John
        isFunction: function(fn) {
            "dk.brics.tajs.directives.unreachable";
            return !!fn && typeof fn != "string" && !fn.nodeName && fn.constructor != Array && /function/i.test(fn + "");
        },
        // check if an element is in a XML document
        isXMLDoc: function(elem) {
            "dk.brics.tajs.directives.unreachable";
            return elem.documentElement && !elem.body || elem.tagName && elem.ownerDocument && !elem.ownerDocument.body;
        },
        // Evalulates a script in a global context
        // Evaluates Async. in Safari 2 :-(
        globalEval: function(data) {
            "dk.brics.tajs.directives.unreachable";
            data = jQuery.trim(data);
            if (data) {
                "dk.brics.tajs.directives.unreachable";
                if (window.execScript) {
                    "dk.brics.tajs.directives.unreachable";
                    window.execScript(data);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery.browser.safari) // safari doesn't provide a synchronous global eval
                    {
                        "dk.brics.tajs.directives.unreachable";
                        window.setTimeout(data, 0);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        eval.call(window, data);
                    }
                }
            }
        },
        nodeName: function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            return elem.nodeName && elem.nodeName.toUpperCase() == name.toUpperCase();
        },
        cache: {},
        data: function(elem, name, data) {
            "dk.brics.tajs.directives.unreachable";
            elem = elem == window ? win : elem;
            var id = elem[expando];
            // Compute a unique ID for the element
            if (!id) {
                "dk.brics.tajs.directives.unreachable";
                id = elem[expando] = ++uuid;
            }
            // Only generate the data cache if we're
            // trying to access or manipulate it
            if (name && !jQuery.cache[id]) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.cache[id] = {};
            }
            // Prevent overriding the named cache with undefined values
            if (data != undefined) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.cache[id][name] = data;
            }
            // Return the named cache data, or the ID for the element	
            return name ? jQuery.cache[id][name] : id;
        },
        removeData: function(elem, name) {
            "dk.brics.tajs.directives.unreachable";
            elem = elem == window ? win : elem;
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
        // args is for internal usage only
        each: function(obj, fn, args) {
            if (args) {
                "dk.brics.tajs.directives.unreachable";
                if (obj.length == undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    for (var i in obj) {
                        "dk.brics.tajs.directives.unreachable";
                        fn.apply(obj[i], args);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    for (var i = 0, ol = obj.length; i < ol; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        if (fn.apply(obj[i], args) === false) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                    }
                }
            } else {
                if (obj.length == undefined) {
                    for (var i in obj) {
                        fn.call(obj[i], i, obj[i]);
                    }
                } else {
                    for (var i = 0, ol = obj.length, val = obj[0]; i < ol && fn.call(val, i, val) !== false; val = obj[++i]) {}
                }
            }
            return obj;
        },
        prop: function(elem, value, type, index, prop) {
            "dk.brics.tajs.directives.unreachable";
            // Handle executable functions
            if (jQuery.isFunction(value)) {
                "dk.brics.tajs.directives.unreachable";
                value = value.call(elem, [ index ]);
            }
            // exclude the following css properties to add px
            var exclude = /z-?index|font-?weight|opacity|zoom|line-?height/i;
            // Handle passing in a number to a CSS property
            return value && value.constructor == Number && type == "curCSS" && !exclude.test(prop) ? value + "px" : value;
        },
        className: {
            // internal only, use addClass("class")
            add: function(elem, c) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.each((c || "").split(/\s+/), function(i, cur) {
                    "dk.brics.tajs.directives.unreachable";
                    if (!jQuery.className.has(elem.className, cur)) {
                        "dk.brics.tajs.directives.unreachable";
                        elem.className += (elem.className ? " " : "") + cur;
                    }
                });
            },
            // internal only, use removeClass("class")
            remove: function(elem, c) {
                "dk.brics.tajs.directives.unreachable";
                elem.className = c != undefined ? jQuery.grep(elem.className.split(/\s+/), function(cur) {
                    "dk.brics.tajs.directives.unreachable";
                    return !jQuery.className.has(c, cur);
                }).join(" ") : "";
            },
            // internal only, use is(".class")
            has: function(t, c) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.inArray(c, (t.className || t).toString().split(/\s+/)) > -1;
            }
        },
        swap: function(e, o, f) {
            "dk.brics.tajs.directives.unreachable";
            for (var i in o) {
                "dk.brics.tajs.directives.unreachable";
                e.style["old" + i] = e.style[i];
                e.style[i] = o[i];
            }
            f.apply(e, []);
            for (var i in o) {
                "dk.brics.tajs.directives.unreachable";
                e.style[i] = e.style["old" + i];
            }
        },
        css: function(e, p) {
            "dk.brics.tajs.directives.unreachable";
            if (p == "height" || p == "width") {
                "dk.brics.tajs.directives.unreachable";
                var old = {}, oHeight, oWidth, d = [ "Top", "Bottom", "Right", "Left" ];
                jQuery.each(d, function() {
                    "dk.brics.tajs.directives.unreachable";
                    old["padding" + this] = 0;
                    old["border" + this + "Width"] = 0;
                });
                jQuery.swap(e, old, function() {
                    "dk.brics.tajs.directives.unreachable";
                    if (jQuery(e).is(":visible")) {
                        "dk.brics.tajs.directives.unreachable";
                        oHeight = e.offsetHeight;
                        oWidth = e.offsetWidth;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        e = jQuery(e.cloneNode(true)).find(":radio").removeAttr("checked").end().css({
                            visibility: "hidden",
                            position: "absolute",
                            display: "block",
                            right: "0",
                            left: "0"
                        }).appendTo(e.parentNode)[0];
                        var parPos = jQuery.css(e.parentNode, "position") || "static";
                        if (parPos == "static") {
                            "dk.brics.tajs.directives.unreachable";
                            e.parentNode.style.position = "relative";
                        }
                        oHeight = e.clientHeight;
                        oWidth = e.clientWidth;
                        if (parPos == "static") {
                            "dk.brics.tajs.directives.unreachable";
                            e.parentNode.style.position = "static";
                        }
                        e.parentNode.removeChild(e);
                    }
                });
                return p == "height" ? oHeight : oWidth;
            }
            return jQuery.curCSS(e, p);
        },
        curCSS: function(elem, prop, force) {
            "dk.brics.tajs.directives.unreachable";
            var ret, stack = [], swap = [];
            // A helper method for determining if an element's values are broken
            function color(a) {
                "dk.brics.tajs.directives.unreachable";
                if (!jQuery.browser.safari) {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
                var ret = document.defaultView.getComputedStyle(a, null);
                return !ret || ret.getPropertyValue("color") == "";
            }
            if (prop == "opacity" && jQuery.browser.msie) {
                "dk.brics.tajs.directives.unreachable";
                ret = jQuery.attr(elem.style, "opacity");
                return ret == "" ? "1" : ret;
            }
            if (prop.match(/float/i)) {
                "dk.brics.tajs.directives.unreachable";
                prop = styleFloat;
            }
            if (!force && elem.style[prop]) {
                "dk.brics.tajs.directives.unreachable";
                ret = elem.style[prop];
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (document.defaultView && document.defaultView.getComputedStyle) {
                    "dk.brics.tajs.directives.unreachable";
                    if (prop.match(/float/i)) {
                        "dk.brics.tajs.directives.unreachable";
                        prop = "float";
                    }
                    prop = prop.replace(/([A-Z])/g, "-$1").toLowerCase();
                    var cur = document.defaultView.getComputedStyle(elem, null);
                    if (cur && !color(elem)) {
                        "dk.brics.tajs.directives.unreachable";
                        ret = cur.getPropertyValue(prop);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        // Locate all of the parent display: none elements
                        for (var a = elem; a && color(a); a = a.parentNode) {
                            "dk.brics.tajs.directives.unreachable";
                            stack.unshift(a);
                        }
                        // Go through and make them visible, but in reverse
                        // (It would be better if we knew the exact display type that they had)
                        for (a = 0; a < stack.length; a++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (color(stack[a])) {
                                "dk.brics.tajs.directives.unreachable";
                                swap[a] = stack[a].style.display;
                                stack[a].style.display = "block";
                            }
                        }
                        // Since we flip the display style, we have to handle that
                        // one special, otherwise get the value
                        ret = prop == "display" && swap[stack.length - 1] != null ? "none" : document.defaultView.getComputedStyle(elem, null).getPropertyValue(prop) || "";
                        // Finally, revert the display styles back
                        for (a = 0; a < swap.length; a++) {
                            "dk.brics.tajs.directives.unreachable";
                            if (swap[a] != null) {
                                "dk.brics.tajs.directives.unreachable";
                                stack[a].style.display = swap[a];
                            }
                        }
                    }
                    if (prop == "opacity" && ret == "") {
                        "dk.brics.tajs.directives.unreachable";
                        ret = "1";
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (elem.currentStyle) {
                        "dk.brics.tajs.directives.unreachable";
                        var newProp = prop.replace(/\-(\w)/g, function(m, c) {
                            "dk.brics.tajs.directives.unreachable";
                            return c.toUpperCase();
                        });
                        ret = elem.currentStyle[prop] || elem.currentStyle[newProp];
                        // From the awesome hack by Dean Edwards
                        // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
                        // If we're not dealing with a regular pixel number
                        // but a number that has a weird ending, we need to convert it to pixels
                        if (!/^\d+(px)?$/i.test(ret) && /^\d/.test(ret)) {
                            "dk.brics.tajs.directives.unreachable";
                            var style = elem.style.left;
                            var runtimeStyle = elem.runtimeStyle.left;
                            elem.runtimeStyle.left = elem.currentStyle.left;
                            elem.style.left = ret || 0;
                            ret = elem.style.pixelLeft + "px";
                            elem.style.left = style;
                            elem.runtimeStyle.left = runtimeStyle;
                        }
                    }
                }
            }
            return ret;
        },
        clean: function(a, doc) {
            "dk.brics.tajs.directives.unreachable";
            var r = [];
            doc = doc || document;
            jQuery.each(a, function(i, arg) {
                "dk.brics.tajs.directives.unreachable";
                if (!arg) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                if (arg.constructor == Number) {
                    "dk.brics.tajs.directives.unreachable";
                    arg = arg.toString();
                }
                // Convert html string into DOM nodes
                if (typeof arg == "string") {
                    "dk.brics.tajs.directives.unreachable";
                    // Fix "XHTML"-style tags in all browsers
                    arg = arg.replace(/(<(\w+)[^>]*?)\/>/g, function(m, all, tag) {
                        "dk.brics.tajs.directives.unreachable";
                        return tag.match(/^(abbr|br|col|img|input|link|meta|param|hr|area)$/i) ? m : all + "></" + tag + ">";
                    });
                    // Trim whitespace, otherwise indexOf won't work as expected
                    var s = jQuery.trim(arg).toLowerCase(), div = doc.createElement("div"), tb = [];
                    var wrap = // option or optgroup
                    !s.indexOf("<opt") && [ 1, "<select>", "</select>" ] || !s.indexOf("<leg") && [ 1, "<fieldset>", "</fieldset>" ] || s.match(/^<(thead|tbody|tfoot|colg|cap)/) && [ 1, "<table>", "</table>" ] || !s.indexOf("<tr") && [ 2, "<table><tbody>", "</tbody></table>" ] || // <thead> matched above
                    (!s.indexOf("<td") || !s.indexOf("<th")) && [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ] || !s.indexOf("<col") && [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ] || // IE can't serialize <link> and <script> tags normally
                    jQuery.browser.msie && [ 1, "div<div>", "</div>" ] || [ 0, "", "" ];
                    // Go to html and back, then peel off extra wrappers
                    div.innerHTML = wrap[1] + arg + wrap[2];
                    // Move to the right depth
                    while (wrap[0]--) {
                        "dk.brics.tajs.directives.unreachable";
                        div = div.lastChild;
                    }
                    // Remove IE's autoinserted <tbody> from table fragments
                    if (jQuery.browser.msie) {
                        "dk.brics.tajs.directives.unreachable";
                        // String was a <table>, *may* have spurious <tbody>
                        if (!s.indexOf("<table") && s.indexOf("<tbody") < 0) {
                            "dk.brics.tajs.directives.unreachable";
                            tb = div.firstChild && div.firstChild.childNodes;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (wrap[1] == "<table>" && s.indexOf("<tbody") < 0) {
                                "dk.brics.tajs.directives.unreachable";
                                tb = div.childNodes;
                            }
                        }
                        for (var n = tb.length - 1; n >= 0; --n) {
                            "dk.brics.tajs.directives.unreachable";
                            if (jQuery.nodeName(tb[n], "tbody") && !tb[n].childNodes.length) {
                                "dk.brics.tajs.directives.unreachable";
                                tb[n].parentNode.removeChild(tb[n]);
                            }
                        }
                        // IE completely kills leading whitespace when innerHTML is used	
                        if (/^\s/.test(arg)) {
                            "dk.brics.tajs.directives.unreachable";
                            div.insertBefore(doc.createTextNode(arg.match(/^\s*/)[0]), div.firstChild);
                        }
                    }
                    arg = jQuery.makeArray(div.childNodes);
                }
                if (0 === arg.length && !jQuery.nodeName(arg, "form") && !jQuery.nodeName(arg, "select")) {
                    "dk.brics.tajs.directives.unreachable";
                    return;
                }
                if (arg[0] == undefined || jQuery.nodeName(arg, "form") || arg.options) {
                    "dk.brics.tajs.directives.unreachable";
                    r.push(arg);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    r = jQuery.merge(r, arg);
                }
            });
            return r;
        },
        attr: function(elem, name, value) {
            "dk.brics.tajs.directives.unreachable";
            var fix = jQuery.isXMLDoc(elem) ? {} : jQuery.props;
            // Safari mis-reports the default selected property of a hidden option
            // Accessing the parent's selectedIndex property fixes it
            if (name == "selected" && jQuery.browser.safari) {
                "dk.brics.tajs.directives.unreachable";
                elem.parentNode.selectedIndex;
            }
            // Certain attributes only work when accessed via the old DOM 0 way
            if (fix[name]) {
                "dk.brics.tajs.directives.unreachable";
                if (value != undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    elem[fix[name]] = value;
                }
                return elem[fix[name]];
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.browser.msie && name == "style") {
                    "dk.brics.tajs.directives.unreachable";
                    return jQuery.attr(elem.style, "cssText", value);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (value == undefined && jQuery.browser.msie && jQuery.nodeName(elem, "form") && (name == "action" || name == "method")) {
                        "dk.brics.tajs.directives.unreachable";
                        return elem.getAttributeNode(name).nodeValue;
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (elem.tagName) {
                            "dk.brics.tajs.directives.unreachable";
                            if (value != undefined) {
                                "dk.brics.tajs.directives.unreachable";
                                if (name == "type" && jQuery.nodeName(elem, "input") && elem.parentNode) {
                                    "dk.brics.tajs.directives.unreachable";
                                    throw "type property can't be changed";
                                }
                                elem.setAttribute(name, value);
                            }
                            if (jQuery.browser.msie && /href|src/.test(name) && !jQuery.isXMLDoc(elem)) {
                                "dk.brics.tajs.directives.unreachable";
                                return elem.getAttribute(name, 2);
                            }
                            return elem.getAttribute(name);
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // IE actually uses filters for opacity
                            if (name == "opacity" && jQuery.browser.msie) {
                                "dk.brics.tajs.directives.unreachable";
                                if (value != undefined) {
                                    "dk.brics.tajs.directives.unreachable";
                                    // IE has trouble with opacity if it does not have layout
                                    // Force it by setting the zoom level
                                    elem.zoom = 1;
                                    // Set the alpha filter to set the opacity
                                    elem.filter = (elem.filter || "").replace(/alpha\([^)]*\)/, "") + (parseFloat(value).toString() == "NaN" ? "" : "alpha(opacity=" + value * 100 + ")");
                                }
                                return elem.filter ? (parseFloat(elem.filter.match(/opacity=([^)]*)/)[1]) / 100).toString() : "";
                            }
                            name = name.replace(/-([a-z])/gi, function(z, b) {
                                "dk.brics.tajs.directives.unreachable";
                                return b.toUpperCase();
                            });
                            if (value != undefined) {
                                "dk.brics.tajs.directives.unreachable";
                                elem[name] = value;
                            }
                            return elem[name];
                        }
                    }
                }
            }
        },
        trim: function(t) {
            "dk.brics.tajs.directives.unreachable";
            return (t || "").replace(/^\s+|\s+$/g, "");
        },
        makeArray: function(a) {
            "dk.brics.tajs.directives.unreachable";
            var r = [];
            // Need to use typeof to fight Safari childNodes crashes
            if (typeof a != "array") {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, al = a.length; i < al; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    r.push(a[i]);
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                r = a.slice(0);
            }
            return r;
        },
        inArray: function(b, a) {
            "dk.brics.tajs.directives.unreachable";
            for (var i = 0, al = a.length; i < al; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (a[i] == b) {
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
            // Also, we need to make sure that the correct elements are being returned
            // (IE returns comment nodes in a '*' query)
            if (jQuery.browser.msie) {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0; second[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (second[i].nodeType != 8) {
                        "dk.brics.tajs.directives.unreachable";
                        first.push(second[i]);
                    }
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0; second[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    first.push(second[i]);
                }
            }
            return first;
        },
        unique: function(first) {
            "dk.brics.tajs.directives.unreachable";
            var r = [], done = {};
            try {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0, fl = first.length; i < fl; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    var id = jQuery.data(first[i]);
                    if (!done[id]) {
                        "dk.brics.tajs.directives.unreachable";
                        done[id] = true;
                        r.push(first[i]);
                    }
                }
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                r = first;
            }
            return r;
        },
        grep: function(elems, fn, inv) {
            "dk.brics.tajs.directives.unreachable";
            // If a string is passed in for the function, make a function
            // for it (a handy shortcut)
            if (typeof fn == "string") {
                "dk.brics.tajs.directives.unreachable";
                fn = eval("false||function(a,i){return " + fn + "}");
            }
            var result = [];
            // Go through the array, only saving the items
            // that pass the validator function
            for (var i = 0, el = elems.length; i < el; i++) {
                "dk.brics.tajs.directives.unreachable";
                if (!inv && fn(elems[i], i) || inv && !fn(elems[i], i)) {
                    "dk.brics.tajs.directives.unreachable";
                    result.push(elems[i]);
                }
            }
            return result;
        },
        map: function(elems, fn) {
            "dk.brics.tajs.directives.unreachable";
            // If a string is passed in for the function, make a function
            // for it (a handy shortcut)
            if (typeof fn == "string") {
                "dk.brics.tajs.directives.unreachable";
                fn = eval("false||function(a){return " + fn + "}");
            }
            var result = [];
            // Go through the array, translating each of the items to their
            // new value (or values).
            for (var i = 0, el = elems.length; i < el; i++) {
                "dk.brics.tajs.directives.unreachable";
                var val = fn(elems[i], i);
                if (val !== null && val != undefined) {
                    "dk.brics.tajs.directives.unreachable";
                    if (val.constructor != Array) {
                        "dk.brics.tajs.directives.unreachable";
                        val = [ val ];
                    }
                    result = result.concat(val);
                }
            }
            return result;
        }
    });
    var userAgent = navigator.userAgent.toLowerCase();
    // Figure out what browser is being used
    jQuery.browser = {
        version: (userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1],
	safari: true || /webkit/.test(userAgent),
	opera: false && /opera/.test(userAgent),
	msie: false && /msie/.test(userAgent) && !/opera/.test(userAgent),
	mozilla: false && /mozilla/.test(userAgent) && !/(compatible|webkit)/.test(userAgent)
    };
    var styleFloat = jQuery.browser.msie ? "styleFloat" : "cssFloat";
    jQuery.extend({
        // Check to see if the W3C box model is being used
        boxModel: !jQuery.browser.msie || document.compatMode == "CSS1Compat",
        styleFloat: jQuery.browser.msie ? "styleFloat" : "cssFloat",
        props: {
            "for": "htmlFor",
            "class": "className",
            "float": styleFloat,
            cssFloat: styleFloat,
            styleFloat: styleFloat,
            innerHTML: "innerHTML",
            className: "className",
            value: "value",
            disabled: "disabled",
            checked: "checked",
            readonly: "readOnly",
            selected: "selected",
            maxlength: "maxLength"
        }
    });
    jQuery.each({
        parent: "a.parentNode",
        parents: "jQuery.dir(a,'parentNode')",
        next: "jQuery.nth(a,2,'nextSibling')",
        prev: "jQuery.nth(a,2,'previousSibling')",
        nextAll: "jQuery.dir(a,'nextSibling')",
        prevAll: "jQuery.dir(a,'previousSibling')",
        siblings: "jQuery.sibling(a.parentNode.firstChild,a)",
        children: "jQuery.sibling(a.firstChild)",
        contents: "jQuery.nodeName(a,'iframe')?a.contentDocument||a.contentWindow.document:jQuery.makeArray(a.childNodes)"
    }, function(i, n) {
        jQuery.fn[i] = function(a) {
            "dk.brics.tajs.directives.unreachable";
            var ret = jQuery.map(this, n);
            if (a && typeof a == "string") {
                "dk.brics.tajs.directives.unreachable";
                ret = jQuery.multiFilter(a, ret);
            }
            return this.pushStack(jQuery.unique(ret));
        };
    });
    jQuery.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
    }, function(i, n) {
        jQuery.fn[i] = function() {
            "dk.brics.tajs.directives.unreachable";
            var a = arguments;
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                for (var j = 0, al = a.length; j < al; j++) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(a[j])[n](this);
                }
            });
        };
    });
    jQuery.each({
        removeAttr: function(key) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.attr(this, key, "");
            this.removeAttribute(key);
        },
        addClass: function(c) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.className.add(this, c);
        },
        removeClass: function(c) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.className.remove(this, c);
        },
        toggleClass: function(c) {
            "dk.brics.tajs.directives.unreachable";
            if(jQuery.className.has(this, c)){
                jQuery.className.remove(this, c);
            }else{
                jQuery.className.add(this, c);
            }
        },
        remove: function(a) {
            "dk.brics.tajs.directives.unreachable";
            if (!a || jQuery.filter(a, [ this ]).r.length) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.removeData(this);
                this.parentNode.removeChild(this);
            }
        },
        empty: function() {
            "dk.brics.tajs.directives.unreachable";
            // Clean up the cache
            jQuery("*", this).each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.removeData(this);
            });
            while (this.firstChild) {
                "dk.brics.tajs.directives.unreachable";
                this.removeChild(this.firstChild);
            }
        }
    }, function(i, n) {
        jQuery.fn[i] = function() {
            "dk.brics.tajs.directives.unreachable";
            return this.each(n, arguments);
        };
    });
    jQuery.each([ "Height", "Width" ], function(i, name) {
        var n = name.toLowerCase();
        jQuery.fn[n] = function(h) {
            "dk.brics.tajs.directives.unreachable";
            return this[0] == window ? jQuery.browser.safari && self["inner" + name] || jQuery.boxModel && Math.max(document.documentElement["client" + name], document.body["client" + name]) || document.body["client" + name] : this[0] == document ? Math.max(document.body["scroll" + name], document.body["offset" + name]) : h == undefined ? this.length ? jQuery.css(this[0], n) : null : this.css(n, h.constructor == String ? h : h + "px");
        };
    });
    var chars = jQuery.browser.safari && parseInt(jQuery.browser.version) < 417 ? "(?:[\\w*_-]|\\\\.)" : "(?:[\\w\u0128-\uFFFF*_-]|\\\\.)", quickChild = new RegExp("^>\\s*(" + chars + "+)"), quickID = new RegExp("^(" + chars + "+)(#)(" + chars + "+)"), quickClass = new RegExp("^([#.]?)(" + chars + "*)");
    jQuery.extend({
        expr: {
            "": "m[2]=='*'||jQuery.nodeName(a,m[2])",
            "#": "a.getAttribute('id')==m[2]",
            ":": {
                // Position Checks
                lt: "i<m[3]-0",
                gt: "i>m[3]-0",
                nth: "m[3]-0==i",
                eq: "m[3]-0==i",
                first: "i==0",
                last: "i==r.length-1",
                even: "i%2==0",
                odd: "i%2",
                // Child Checks
                "first-child": "a.parentNode.getElementsByTagName('*')[0]==a",
                "last-child": "jQuery.nth(a.parentNode.lastChild,1,'previousSibling')==a",
                "only-child": "!jQuery.nth(a.parentNode.lastChild,2,'previousSibling')",
                // Parent Checks
                parent: "a.firstChild",
                empty: "!a.firstChild",
                // Text Check
                contains: "(a.textContent||a.innerText||'').indexOf(m[3])>=0",
                // Visibility
                visible: '"hidden"!=a.type&&jQuery.css(a,"display")!="none"&&jQuery.css(a,"visibility")!="hidden"',
                hidden: '"hidden"==a.type||jQuery.css(a,"display")=="none"||jQuery.css(a,"visibility")=="hidden"',
                // Form attributes
                enabled: "!a.disabled",
                disabled: "a.disabled",
                checked: "a.checked",
                selected: "a.selected||jQuery.attr(a,'selected')",
                // Form elements
                text: "'text'==a.type",
                radio: "'radio'==a.type",
                checkbox: "'checkbox'==a.type",
                file: "'file'==a.type",
                password: "'password'==a.type",
                submit: "'submit'==a.type",
                image: "'image'==a.type",
                reset: "'reset'==a.type",
                button: '"button"==a.type||jQuery.nodeName(a,"button")',
                input: "/input|select|textarea|button/i.test(a.nodeName)",
                // :has()
                has: "jQuery.find(m[3],a).length",
                // :header
                header: "/h\\d/i.test(a.nodeName)",
                // :animated
                animated: "jQuery.grep(jQuery.timers,function(fn){return a==fn.elem;}).length"
            }
        },
        // The regular expressions that power the parsing engine
        parse: [ // Match: [@value='test'], [@foo]
        /^(\[) *@?([\w-]+) *([!*$^~=]*) *('?"?)(.*?)\4 *\]/, // Match: :contains('foo')
        /^(:)([\w-]+)\("?'?(.*?(\(.*?\))?[^(]*?)"?'?\)/, // Match: :even, :last-chlid, #id, .class
        new RegExp("^([:.#]*)(" + chars + "+)") ],
        multiFilter: function(expr, elems, not) {
            "dk.brics.tajs.directives.unreachable";
            var old, cur = [];
            while (expr && expr != old) {
                "dk.brics.tajs.directives.unreachable";
                old = expr;
                var f = jQuery.filter(expr, elems, not);
                expr = f.t.replace(/^\s*,\s*/, "");
                cur = not ? elems = f.r : jQuery.merge(cur, f.r);
            }
            return cur;
        },
        find: function(t, context) {
            "dk.brics.tajs.directives.unreachable";
            // Quickly handle non-string expressions
            if (typeof t != "string") {
                "dk.brics.tajs.directives.unreachable";
                return [ t ];
            }
            // Make sure that the context is a DOM Element
            if (context && !context.nodeType) {
                "dk.brics.tajs.directives.unreachable";
                context = null;
            }
            // Set the correct context (if none is provided)
            context = context || document;
            // Initialize the search
            var ret = [ context ], done = [], last;
            // Continue while a selector expression exists, and while
            // we're no longer looping upon ourselves
            while (t && last != t) {
                "dk.brics.tajs.directives.unreachable";
                var r = [];
                last = t;
                t = jQuery.trim(t);
                var foundToken = false;
                // An attempt at speeding up child selectors that
                // point to a specific element tag
                var re = quickChild;
                var m = re.exec(t);
                if (m) {
                    "dk.brics.tajs.directives.unreachable";
                    var nodeName = m[1].toUpperCase();
                    // Perform our own iteration and filter
                    for (var i = 0; ret[i]; i++) {
                        "dk.brics.tajs.directives.unreachable";
                        for (var c = ret[i].firstChild; c; c = c.nextSibling) {
                            "dk.brics.tajs.directives.unreachable";
                            if (c.nodeType == 1 && (nodeName == "*" || c.nodeName.toUpperCase() == nodeName.toUpperCase())) {
                                "dk.brics.tajs.directives.unreachable";
                                r.push(c);
                            }
                        }
                    }
                    ret = r;
                    t = t.replace(re, "");
                    if (t.indexOf(" ") == 0) {
                        "dk.brics.tajs.directives.unreachable";
                        continue;
                    }
                    foundToken = true;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    re = /^([>+~])\s*(\w*)/i;
                    if ((m = re.exec(t)) != null) {
                        "dk.brics.tajs.directives.unreachable";
                        r = [];
                        var nodeName = m[2], merge = {};
                        m = m[1];
                        for (var j = 0, rl = ret.length; j < rl; j++) {
                            "dk.brics.tajs.directives.unreachable";
                            var n = m == "~" || m == "+" ? ret[j].nextSibling : ret[j].firstChild;
                            for (;n; n = n.nextSibling) {
                                "dk.brics.tajs.directives.unreachable";
                                if (n.nodeType == 1) {
                                    "dk.brics.tajs.directives.unreachable";
                                    var id = jQuery.data(n);
                                    if (m == "~" && merge[id]) {
                                        "dk.brics.tajs.directives.unreachable";
                                        break;
                                    }
                                    if (!nodeName || n.nodeName.toUpperCase() == nodeName.toUpperCase()) {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (m == "~") {
                                            "dk.brics.tajs.directives.unreachable";
                                            merge[id] = true;
                                        }
                                        r.push(n);
                                    }
                                    if (m == "+") {
                                        "dk.brics.tajs.directives.unreachable";
                                        break;
                                    }
                                }
                            }
                        }
                        ret = r;
                        // And remove the token
                        t = jQuery.trim(t.replace(re, ""));
                        foundToken = true;
                    }
                }
                // See if there's still an expression, and that we haven't already
                // matched a token
                if (t && !foundToken) {
                    "dk.brics.tajs.directives.unreachable";
                    // Handle multiple expressions
                    if (!t.indexOf(",")) {
                        "dk.brics.tajs.directives.unreachable";
                        // Clean the result set
                        if (context == ret[0]) {
                            "dk.brics.tajs.directives.unreachable";
                            ret.shift();
                        }
                        // Merge the result sets
                        done = jQuery.merge(done, ret);
                        // Reset the context
                        r = ret = [ context ];
                        // Touch up the selector string
                        t = " " + t.substr(1, t.length);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        // Optimize for the case nodeName#idName
                        var re2 = quickID;
                        var m = re2.exec(t);
                        // Re-organize the results, so that they're consistent
                        if (m) {
                            "dk.brics.tajs.directives.unreachable";
                            m = [ 0, m[2], m[3], m[1] ];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // Otherwise, do a traditional filter check for
                            // ID, class, and element selectors
                            re2 = quickClass;
                            m = re2.exec(t);
                        }
                        m[2] = m[2].replace(/\\/g, "");
                        var elem = ret[ret.length - 1];
                        // Try to do a global search by ID, where we can
                        if (m[1] == "#" && elem && elem.getElementById && !jQuery.isXMLDoc(elem)) {
                            "dk.brics.tajs.directives.unreachable";
                            // Optimization for HTML document case
                            var oid = elem.getElementById(m[2]);
                            // Do a quick check for the existence of the actual ID attribute
                            // to avoid selecting by the name attribute in IE
                            // also check to insure id is a string to avoid selecting an element with the name of 'id' inside a form
                            if ((jQuery.browser.msie || jQuery.browser.opera) && oid && typeof oid.id == "string" && oid.id != m[2]) {
                                "dk.brics.tajs.directives.unreachable";
                                oid = jQuery('[@id="' + m[2] + '"]', elem)[0];
                            }
                            // Do a quick check for node name (where applicable) so
                            // that div#foo searches will be really fast
                            ret = r = oid && (!m[3] || jQuery.nodeName(oid, m[3])) ? [ oid ] : [];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            // We need to find all descendant elements
                            for (var i = 0; ret[i]; i++) {
                                "dk.brics.tajs.directives.unreachable";
                                // Grab the tag name being searched for
                                var tag = m[1] == "#" && m[3] ? m[3] : m[1] != "" || m[0] == "" ? "*" : m[2];
                                // Handle IE7 being really dumb about <object>s
                                if (tag == "*" && ret[i].nodeName.toLowerCase() == "object") {
                                    "dk.brics.tajs.directives.unreachable";
                                    tag = "param";
                                }
                                r = jQuery.merge(r, ret[i].getElementsByTagName(tag));
                            }
                            // It's faster to filter by class and be done with it
                            if (m[1] == ".") {
                                "dk.brics.tajs.directives.unreachable";
                                r = jQuery.classFilter(r, m[2]);
                            }
                            // Same with ID filtering
                            if (m[1] == "#") {
                                "dk.brics.tajs.directives.unreachable";
                                var tmp = [];
                                // Try to find the element with the ID
                                for (var i = 0; r[i]; i++) {
                                    "dk.brics.tajs.directives.unreachable";
                                    if (r[i].getAttribute("id") == m[2]) {
                                        "dk.brics.tajs.directives.unreachable";
                                        tmp = [ r[i] ];
                                        break;
                                    }
                                }
                                r = tmp;
                            }
                            ret = r;
                        }
                        t = t.replace(re2, "");
                    }
                }
                // If a selector string still exists
                if (t) {
                    "dk.brics.tajs.directives.unreachable";
                    // Attempt to filter it
                    var val = jQuery.filter(t, r);
                    ret = r = val.r;
                    t = jQuery.trim(val.t);
                }
            }
            // An error occurred with the selector;
            // just return an empty set instead
            if (t) {
                "dk.brics.tajs.directives.unreachable";
                ret = [];
            }
            // Remove the root context
            if (ret && context == ret[0]) {
                "dk.brics.tajs.directives.unreachable";
                ret.shift();
            }
            // And combine the results
            done = jQuery.merge(done, ret);
            return done;
        },
        classFilter: function(r, m, not) {
            "dk.brics.tajs.directives.unreachable";
            m = " " + m + " ";
            var tmp = [];
            for (var i = 0; r[i]; i++) {
                "dk.brics.tajs.directives.unreachable";
                var pass = (" " + r[i].className + " ").indexOf(m) >= 0;
                if (!not && pass || not && !pass) {
                    "dk.brics.tajs.directives.unreachable";
                    tmp.push(r[i]);
                }
            }
            return tmp;
        },
        filter: function(t, r, not) {
            "dk.brics.tajs.directives.unreachable";
            var last;
            // Look for common filter expressions
            while (t && t != last) {
                "dk.brics.tajs.directives.unreachable";
                last = t;
                var p = jQuery.parse, m;
                for (var i = 0; p[i]; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    m = p[i].exec(t);
                    if (m) {
                        "dk.brics.tajs.directives.unreachable";
                        // Remove what we just matched
                        t = t.substring(m[0].length);
                        m[2] = m[2].replace(/\\/g, "");
                        break;
                    }
                }
                if (!m) {
                    "dk.brics.tajs.directives.unreachable";
                    break;
                }
                // :not() is a special case that can be optimized by
                // keeping it out of the expression list
                if (m[1] == ":" && m[2] == "not") {
                    "dk.brics.tajs.directives.unreachable";
                    r = jQuery.filter(m[3], r, true).r;
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (m[1] == ".") {
                        "dk.brics.tajs.directives.unreachable";
                        r = jQuery.classFilter(r, m[2], not);
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        if (m[1] == "[") {
                            "dk.brics.tajs.directives.unreachable";
                            var tmp = [], type = m[3];
                            for (var i = 0, rl = r.length; i < rl; i++) {
                                "dk.brics.tajs.directives.unreachable";
                                var a = r[i], z = a[jQuery.props[m[2]] || m[2]];
                                if (z == null || /href|src|selected/.test(m[2])) {
                                    "dk.brics.tajs.directives.unreachable";
                                    z = jQuery.attr(a, m[2]) || "";
                                }
                                if ((type == "" && !!z || type == "=" && z == m[5] || type == "!=" && z != m[5] || type == "^=" && z && !z.indexOf(m[5]) || type == "$=" && z.substr(z.length - m[5].length) == m[5] || (type == "*=" || type == "~=") && z.indexOf(m[5]) >= 0) ^ not) {
                                    "dk.brics.tajs.directives.unreachable";
                                    tmp.push(a);
                                }
                            }
                            r = tmp;
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            if (m[1] == ":" && m[2] == "nth-child") {
                                "dk.brics.tajs.directives.unreachable";
                                var merge = {}, tmp = [], test = /(\d*)n\+?(\d*)/.exec(m[3] == "even" && "2n" || m[3] == "odd" && "2n+1" || !/\D/.test(m[3]) && "n+" + m[3] || m[3]), first = (test[1] || 1) - 0, last = test[2] - 0;
                                for (var i = 0, rl = r.length; i < rl; i++) {
                                    "dk.brics.tajs.directives.unreachable";
                                    var node = r[i], parentNode = node.parentNode, id = jQuery.data(parentNode);
                                    if (!merge[id]) {
                                        "dk.brics.tajs.directives.unreachable";
                                        var c = 1;
                                        for (var n = parentNode.firstChild; n; n = n.nextSibling) {
                                            "dk.brics.tajs.directives.unreachable";
                                            if (n.nodeType == 1) {
                                                "dk.brics.tajs.directives.unreachable";
                                                n.nodeIndex = c++;
                                            }
                                        }
                                        merge[id] = true;
                                    }
                                    var add = false;
                                    if (first == 1) {
                                        "dk.brics.tajs.directives.unreachable";
                                        if (last == 0 || node.nodeIndex == last) {
                                            "dk.brics.tajs.directives.unreachable";
                                            add = true;
                                        }
                                    } else {
                                        "dk.brics.tajs.directives.unreachable";
                                        if ((node.nodeIndex + last) % first == 0) {
                                            "dk.brics.tajs.directives.unreachable";
                                            add = true;
                                        }
                                    }
                                    if (add ^ not) {
                                        "dk.brics.tajs.directives.unreachable";
                                        tmp.push(node);
                                    }
                                }
                                r = tmp;
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                var f = jQuery.expr[m[1]];
                                if (typeof f != "string") {
                                    "dk.brics.tajs.directives.unreachable";
                                    f = jQuery.expr[m[1]][m[2]];
                                }
                                // Build a custom macro to enclose it
                                f = eval("false||function(a,i){return " + f + "}");
                                // Execute it against the current filter
                                r = jQuery.grep(r, f, not);
                            }
                        }
                    }
                }
            }
            // Return an array of filtered elements (r)
            // and the modified expression string (t)
            return {
                r: r,
                t: t
            };
        },
        dir: function(elem, dir) {
            "dk.brics.tajs.directives.unreachable";
            var matched = [];
            var cur = elem[dir];
            while (cur && cur != document) {
                "dk.brics.tajs.directives.unreachable";
                if (cur.nodeType == 1) {
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
                if (cur.nodeType == 1 && ++num == result) {
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
                if (n.nodeType == 1 && (!elem || n != elem)) {
                    "dk.brics.tajs.directives.unreachable";
                    r.push(n);
                }
            }
            return r;
        }
    });
    /*
 * A number of helper functions used for managing events.
 * Many of the ideas behind this code orignated from 
 * Dean Edwards' addEvent library.
 */
    jQuery.event = {
        // Bind an event to an element
        // Original by Dean Edwards
        add: function(element, type, handler, data) {
            "dk.brics.tajs.directives.unreachable";
            // For whatever reason, IE has trouble passing the window object
            // around, causing it to be cloned in the process
            if (jQuery.browser.msie && element.setInterval != undefined) {
                "dk.brics.tajs.directives.unreachable";
                element = window;
            }
            // Make sure that the function being executed has a unique ID
            if (!handler.guid) {
                "dk.brics.tajs.directives.unreachable";
                handler.guid = this.guid++;
            }
            // if data is passed, bind to handler 
            if (data != undefined) {
                "dk.brics.tajs.directives.unreachable";
                // Create temporary function pointer to original handler 
                var fn = handler;
                // Create unique handler function, wrapped around original handler 
                handler = function() {
                    "dk.brics.tajs.directives.unreachable";
                    // Pass arguments and context to original handler 
                    return fn.apply(this, arguments);
                };
                // Store data in unique handler 
                handler.data = data;
                // Set the guid of unique handler to the same of original handler, so it can be removed 
                handler.guid = fn.guid;
            }
            // Namespaced event handlers
            var parts = type.split(".");
            type = parts[0];
            handler.type = parts[1];
            // Init the element's event structure
            var events = jQuery.data(element, "events") || jQuery.data(element, "events", {});
            var handle = jQuery.data(element, "handle", function() {
                "dk.brics.tajs.directives.unreachable";
                // returned undefined or false
                var val;
                // Handle the second event of a trigger and when
                // an event is called after a page has unloaded
                if (typeof jQuery == "undefined" || jQuery.event.triggered) {
                    "dk.brics.tajs.directives.unreachable";
                    return val;
                }
                val = jQuery.event.handle.apply(element, arguments);
                return val;
            });
            // Get the current list of functions bound to this event
            var handlers = events[type];
            // Init the event handler queue
            if (!handlers) {
                "dk.brics.tajs.directives.unreachable";
                handlers = events[type] = {};
                // And bind the global event handler to the element
                if (element.addEventListener) {
                    "dk.brics.tajs.directives.unreachable";
                    element.addEventListener(type, handle, false);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    element.attachEvent("on" + type, handle);
                }
            }
            // Add the function to the element's handler list
            handlers[handler.guid] = handler;
            // Keep track of which events have been used, for global triggering
            this.global[type] = true;
        },
        guid: 1,
        global: {},
        // Detach an event or set of events from an element
        remove: function(element, type, handler) {
            "dk.brics.tajs.directives.unreachable";
            var events = jQuery.data(element, "events"), ret, index;
            // Namespaced event handlers
            if (typeof type == "string") {
                "dk.brics.tajs.directives.unreachable";
                var parts = type.split(".");
                type = parts[0];
            }
            if (events) {
                "dk.brics.tajs.directives.unreachable";
                // type is actually an event object here
                if (type && type.type) {
                    "dk.brics.tajs.directives.unreachable";
                    handler = type.handler;
                    type = type.type;
                }
                if (!type) {
                    "dk.brics.tajs.directives.unreachable";
                    for (type in events) {
                        "dk.brics.tajs.directives.unreachable";
                        this.remove(element, type);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    if (events[type]) {
                        "dk.brics.tajs.directives.unreachable";
                        // remove the given handler for the given type
                        if (handler) {
                            "dk.brics.tajs.directives.unreachable";
                            delete events[type][handler.guid];
                        } else {
                            "dk.brics.tajs.directives.unreachable";
                            for (handler in events[type]) // Handle the removal of namespaced events
                            {
                                "dk.brics.tajs.directives.unreachable";
                                if (!parts[1] || events[type][handler].type == parts[1]) {
                                    "dk.brics.tajs.directives.unreachable";
                                    delete events[type][handler];
                                }
                            }
                        }
                        // remove generic event handler if no more handlers exist
                        for (ret in events[type]) {
                            "dk.brics.tajs.directives.unreachable";
                            break;
                        }
                        if (!ret) {
                            "dk.brics.tajs.directives.unreachable";
                            if (element.removeEventListener) {
                                "dk.brics.tajs.directives.unreachable";
                                element.removeEventListener(type, jQuery.data(element, "handle"), false);
                            } else {
                                "dk.brics.tajs.directives.unreachable";
                                element.detachEvent("on" + type, jQuery.data(element, "handle"));
                            }
                            ret = null;
                            delete events[type];
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
                    jQuery.removeData(element, "events");
                    jQuery.removeData(element, "handle");
                }
            }
        },
        trigger: function(type, data, element, donative, extra) {
            "dk.brics.tajs.directives.unreachable";
            // Clone the incoming data, if any
            data = jQuery.makeArray(data || []);
            // Handle a global trigger
            if (!element) {
                "dk.brics.tajs.directives.unreachable";
                // Only trigger if we've ever bound an event for it
                if (this.global[type]) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery("*").add([ window, document ]).trigger(type, data);
                }
            } else {
                "dk.brics.tajs.directives.unreachable";
                var val, ret, fn = jQuery.isFunction(element[type] || null), // Check to see if we need to provide a fake event, or not
                evt = !data[0] || !data[0].preventDefault;
                // Pass along a fake event
                if (evt) {
                    "dk.brics.tajs.directives.unreachable";
                    data.unshift(this.fix({
                        type: type,
                        target: element
                    }));
                }
                // Trigger the event
                if (jQuery.isFunction(jQuery.data(element, "handle"))) {
                    "dk.brics.tajs.directives.unreachable";
                    val = jQuery.data(element, "handle").apply(element, data);
                }
                // Handle triggering native .onfoo handlers
                if (!fn && element["on" + type] && element["on" + type].apply(element, data) === false) {
                    "dk.brics.tajs.directives.unreachable";
                    val = false;
                }
                // Extra functions don't get the custom event object
                if (evt) {
                    "dk.brics.tajs.directives.unreachable";
                    data.shift();
                }
                // Handle triggering of extra function
                if (extra && extra.apply(element, data) === false) {
                    "dk.brics.tajs.directives.unreachable";
                    val = false;
                }
                // Trigger the native events (except for clicks on links)
                if (fn && donative !== false && val !== false && !(jQuery.nodeName(element, "a") && type == "click")) {
                    "dk.brics.tajs.directives.unreachable";
                    this.triggered = true;
                    element[type]();
                }
                this.triggered = false;
            }
            return val;
        },
        handle: function(event) {
            "dk.brics.tajs.directives.unreachable";
            // returned undefined or false
            var val;
            // Empty object is for triggered events with no data
            event = jQuery.event.fix(event || window.event || {});
            // Namespaced event handlers
            var parts = event.type.split(".");
            event.type = parts[0];
            var c = jQuery.data(this, "events") && jQuery.data(this, "events")[event.type], args = Array.prototype.slice.call(arguments, 1);
            args.unshift(event);
            for (var j in c) {
                "dk.brics.tajs.directives.unreachable";
                // Pass in a reference to the handler function itself
                // So that we can later remove it
                args[0].handler = c[j];
                args[0].data = c[j].data;
                // Filter the functions by class
                if (!parts[1] || c[j].type == parts[1]) {
                    "dk.brics.tajs.directives.unreachable";
                    var tmp = c[j].apply(this, args);
                    if (val !== false) {
                        "dk.brics.tajs.directives.unreachable";
                        val = tmp;
                    }
                    if (tmp === false) {
                        "dk.brics.tajs.directives.unreachable";
                        event.preventDefault();
                        event.stopPropagation();
                    }
                }
            }
            // Clean up added properties in IE to prevent memory leak
            if (jQuery.browser.msie) {
                "dk.brics.tajs.directives.unreachable";
                event.target = event.preventDefault = event.stopPropagation = event.handler = event.data = null;
            }
            return val;
        },
        fix: function(event) {
            "dk.brics.tajs.directives.unreachable";
            // store a copy of the original event object 
            // and clone to set read-only properties
            var originalEvent = event;
            event = jQuery.extend({}, originalEvent);
            // add preventDefault and stopPropagation since 
            // they will not work on the clone
            event.preventDefault = function() {
                "dk.brics.tajs.directives.unreachable";
                // if preventDefault exists run it on the original event
                if (originalEvent.preventDefault) {
                    "dk.brics.tajs.directives.unreachable";
                    originalEvent.preventDefault();
                }
                // otherwise set the returnValue property of the original event to false (IE)
                originalEvent.returnValue = false;
            };
            event.stopPropagation = function() {
                "dk.brics.tajs.directives.unreachable";
                // if stopPropagation exists run it on the original event
                if (originalEvent.stopPropagation) {
                    "dk.brics.tajs.directives.unreachable";
                    originalEvent.stopPropagation();
                }
                // otherwise set the cancelBubble property of the original event to true (IE)
                originalEvent.cancelBubble = true;
            };
            // Fix target property, if necessary
            if (!event.target && event.srcElement) {
                "dk.brics.tajs.directives.unreachable";
                event.target = event.srcElement;
            }
            // check if target is a textnode (safari)
            if (jQuery.browser.safari && event.target.nodeType == 3) {
                "dk.brics.tajs.directives.unreachable";
                event.target = originalEvent.target.parentNode;
            }
            // Add relatedTarget, if necessary
            if (!event.relatedTarget && event.fromElement) {
                "dk.brics.tajs.directives.unreachable";
                event.relatedTarget = event.fromElement == event.target ? event.toElement : event.fromElement;
            }
            // Calculate pageX/Y if missing and clientX/Y available
            if (event.pageX == null && event.clientX != null) {
                "dk.brics.tajs.directives.unreachable";
                var e = document.documentElement, b = document.body;
                event.pageX = event.clientX + (e && e.scrollLeft || b.scrollLeft || 0);
                event.pageY = event.clientY + (e && e.scrollTop || b.scrollTop || 0);
            }
            // Add which for key events
            if (!event.which && (event.charCode || event.keyCode)) {
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
        }
    };
    jQuery.fn.extend({
        bind: function(type, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            return type == "unload" ? this.one(type, data, fn) : this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.add(this, type, fn || data, fn && data);
            });
        },
        one: function(type, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.add(this, type, function(event) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(this).unbind(event);
                    return (fn || data).apply(this, arguments);
                }, fn && data);
            });
        },
        unbind: function(type, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.remove(this, type, fn);
            });
        },
        trigger: function(type, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger(type, data, this, true, fn);
            });
        },
        triggerHandler: function(type, data, fn) {
            "dk.brics.tajs.directives.unreachable";
            if (this[0]) {
                "dk.brics.tajs.directives.unreachable";
                return jQuery.event.trigger(type, data, this[0], false, fn);
            }
        },
        toggle: function() {
            "dk.brics.tajs.directives.unreachable";
            // Save reference to arguments for access in closure
            var a = arguments;
            return this.click(function(e) {
                "dk.brics.tajs.directives.unreachable";
                // Figure out which function to execute
                this.lastToggle = 0 == this.lastToggle ? 1 : 0;
                // Make sure that clicks stop
                e.preventDefault();
                // and execute the function
                return a[this.lastToggle].apply(this, [ e ]) || false;
            });
        },
        hover: function(f, g) {
            "dk.brics.tajs.directives.unreachable";
            // A private function for handling mouse 'hovering'
            function handleHover(e) {
                "dk.brics.tajs.directives.unreachable";
                // Check if mouse(over|out) are still within the same parent element
                var p = e.relatedTarget;
                // Traverse up the tree
                while (p && p != this) {
                    "dk.brics.tajs.directives.unreachable";
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        p = p.parentNode;
                    } catch (e) {
                        "dk.brics.tajs.directives.unreachable";
                        p = this;
                    }
                }
                // If we actually just moused on to a sub-element, ignore it
                if (p == this) {
                    "dk.brics.tajs.directives.unreachable";
                    return false;
                }
                // Execute the right function
                return (e.type == "mouseover" ? f : g).apply(this, [ e ]);
            }
            // Bind the function to the two event listeners
            return this.mouseover(handleHover).mouseout(handleHover);
        },
        ready: function(f) {
            "dk.brics.tajs.directives.unreachable";
            // Attach the listeners
            bindReady();
            // If the DOM is already ready
            if (jQuery.isReady) // Execute the function immediately
            {
                "dk.brics.tajs.directives.unreachable";
                f.apply(document, [ jQuery ]);
            } else // Add the function to the wait list
            {
                "dk.brics.tajs.directives.unreachable";
                jQuery.readyList.push(function() {
                    "dk.brics.tajs.directives.unreachable";
                    return f.apply(this, [ jQuery ]);
                });
            }
            return this;
        }
    });
    jQuery.extend({
        /*
	 * All the code that makes DOM Ready work nicely.
	 */
        isReady: false,
        readyList: [],
        // Handle when the DOM is ready
        ready: function() {
            "dk.brics.tajs.directives.unreachable";
            // Make sure that the DOM is not already loaded
            if (!jQuery.isReady) {
                "dk.brics.tajs.directives.unreachable";
                // Remember that the DOM is ready
                jQuery.isReady = true;
                // If there are functions bound, to execute
                if (jQuery.readyList) {
                    "dk.brics.tajs.directives.unreachable";
                    // Execute all of them
                    jQuery.each(jQuery.readyList, function() {
                        "dk.brics.tajs.directives.unreachable";
                        this.apply(document);
                    });
                    // Reset the list of functions
                    jQuery.readyList = null;
                }
                // Remove event listener to avoid memory leak
                if (jQuery.browser.mozilla || jQuery.browser.opera) {
                    "dk.brics.tajs.directives.unreachable";
                    document.removeEventListener("DOMContentLoaded", jQuery.ready, false);
                }
                // Remove script element used by IE hack
                if (!window.frames.length) // don't remove if frames are present (#1187)
                {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery(window).load(function() {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery("#__ie_init").remove();
                    });
                }
            }
        }
    });
    jQuery.each(("blur,focus,load,resize,scroll,unload,click,dblclick," + "mousedown,mouseup,mousemove,mouseover,mouseout,change,select," + "submit,keydown,keypress,keyup,error").split(","), function(i, o) {
        // Handle event binding
        jQuery.fn[o] = function(f) {
            "dk.brics.tajs.directives.unreachable";
            return f ? this.bind(o, f) : this.trigger(o);
        };
    });
    var readyBound = false;
    function bindReady() {
        "dk.brics.tajs.directives.unreachable";
        if (readyBound) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        readyBound = true;
        // If Mozilla is used
        if (jQuery.browser.mozilla || jQuery.browser.opera) // Use the handy event callback
        {
            "dk.brics.tajs.directives.unreachable";
            document.addEventListener("DOMContentLoaded", jQuery.ready, false);
        } else {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.browser.msie) {
                "dk.brics.tajs.directives.unreachable";
                // Only works if you document.write() it
                document.write("<scr" + "ipt id=__ie_init defer=true " + "src=//:></script>");
                // Use the defer script hack
                var script = document.getElementById("__ie_init");
                // script does not exist if jQuery is loaded dynamically
                if (script) {
                    "dk.brics.tajs.directives.unreachable";
                    script.onreadystatechange = function() {
                        "dk.brics.tajs.directives.unreachable";
                        if (this.readyState != "complete") {
                            "dk.brics.tajs.directives.unreachable";
                            return;
                        }
                        jQuery.ready();
                    };
                }
                // Clear from memory
                script = null;
            } else {
                "dk.brics.tajs.directives.unreachable";
                if (jQuery.browser.safari) // Continually check to see if the document.readyState is valid
                {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.safariTimer = setInterval(function() {
                        "dk.brics.tajs.directives.unreachable";
                        // loaded and complete are both valid states
                        if (document.readyState == "loaded" || document.readyState == "complete") {
                            "dk.brics.tajs.directives.unreachable";
                            // If either one are found, remove the timer
                            clearInterval(jQuery.safariTimer);
                            jQuery.safariTimer = null;
                            // and execute any waiting functions
                            jQuery.ready();
                        }
                    }, 10);
                }
            }
        }
        // A fallback to window.onload, that will always work
        jQuery.event.add(window, "load", jQuery.ready);
    }
    jQuery.fn.extend({
        load: function(url, params, callback) {
            "dk.brics.tajs.directives.unreachable";
            if (jQuery.isFunction(url)) {
                "dk.brics.tajs.directives.unreachable";
                return this.bind("load", url);
            }
            var off = url.indexOf(" ");
            if (off >= 0) {
                "dk.brics.tajs.directives.unreachable";
                var selector = url.slice(off, url.length);
                url = url.slice(0, off);
            }
            callback = callback || function() {};
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
                    params = jQuery.param(params);
                    type = "POST";
                }
            }
            var self = this;
            // Request the remote document
            jQuery.ajax({
                url: url,
                type: type,
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
                    // Add delay to account for Safari's delay in globalEval
                    setTimeout(function() {
                        "dk.brics.tajs.directives.unreachable";
                        self.each(callback, [ res.responseText, status, res ]);
                    }, 13);
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
                return jQuery.nodeName(this, "form") ? jQuery.makeArray(this.elements) : this;
            }).filter(function() {
                "dk.brics.tajs.directives.unreachable";
                return this.name && !this.disabled && (this.checked || /select|textarea/i.test(this.nodeName) || /text|hidden|password/i.test(this.type));
            }).map(function(i, elem) {
                "dk.brics.tajs.directives.unreachable";
                var val = jQuery(this).val();
                return val == null ? null : val.constructor == Array ? jQuery.map(val, function(i, val) {
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
    var jsc = new Date().getTime();
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
            global: true,
            type: "GET",
            timeout: 0,
            contentType: "application/x-www-form-urlencoded",
            processData: true,
            async: true,
            data: null
        },
        // Last-Modified header cache for next request
        lastModified: {},
        ajax: function(s) {
            "dk.brics.tajs.directives.unreachable";
            var jsonp, jsre = /=(\?|%3F)/g, status, data;
            // Extend the settings, but re-extend 's' so that it can be
            // checked again later (in the test suite, specifically)
            s = jQuery.extend(true, s, jQuery.extend(true, {}, jQuery.ajaxSettings, s));
            // convert data if not already a string
            if (s.data && s.processData && typeof s.data != "string") {
                "dk.brics.tajs.directives.unreachable";
                s.data = jQuery.param(s.data);
            }
            // Break the data into one single string
            var q = s.url.indexOf("?");
            if (q > -1) {
                "dk.brics.tajs.directives.unreachable";
                s.data = (s.data ? s.data + "&" : "") + s.url.slice(q + 1);
                s.url = s.url.slice(0, q);
            }
            // Handle JSONP Parameter Callbacks
            if (s.dataType == "jsonp") {
                "dk.brics.tajs.directives.unreachable";
                if (!s.data || !s.data.match(jsre)) {
                    "dk.brics.tajs.directives.unreachable";
                    s.data = (s.data ? s.data + "&" : "") + (s.jsonp || "callback") + "=?";
                }
                s.dataType = "json";
            }
            // Build temporary JSONP function
            if (s.dataType == "json" && s.data && s.data.match(jsre)) {
                "dk.brics.tajs.directives.unreachable";
                jsonp = "jsonp" + jsc++;
                s.data = s.data.replace(jsre, "=" + jsonp);
                // We need to make sure
                // that a JSONP style response is executed properly
                s.dataType = "script";
                // Handle JSONP-style loading
                window[jsonp] = function(tmp) {
                    "dk.brics.tajs.directives.unreachable";
                    data = tmp;
                    success();
                    // Garbage collect
                    window[jsonp] = undefined;
                    try {
                        "dk.brics.tajs.directives.unreachable";
                        delete window[jsonp];
                    } catch (e) {}
                };
            }
            if (s.dataType == "script" && s.cache == null) {
                "dk.brics.tajs.directives.unreachable";
                s.cache = false;
            }
            if (s.cache === false && s.type.toLowerCase() == "get") {
                "dk.brics.tajs.directives.unreachable";
                s.data = (s.data ? s.data + "&" : "") + "_=" + "TAJS_UUID";
            }
            // If data is available, append data to url for get requests
            if (s.data && s.type.toLowerCase() == "get") {
                "dk.brics.tajs.directives.unreachable";
                s.url += "?" + s.data;
                // IE likes to send both get and post data, prevent this
                s.data = null;
            }
            // Watch for a new set of requests
            if (s.global && !jQuery.active++) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger("ajaxStart");
            }
            // If we're requesting a remote document
            // and trying to load JSON or Script
            if (!s.url.indexOf("http") && s.dataType == "script") {
                "dk.brics.tajs.directives.unreachable";
                var head = document.getElementsByTagName("head")[0];
                var script = document.createElement("script");
                script.src = s.url;
                // Handle Script loading
                if (!jsonp && (s.success || s.complete)) {
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
                return;
            }
            var requestDone = false;
            // Create the request object; Microsoft failed to properly
            // implement the XMLHttpRequest in IE7, so we use the ActiveXObject when it is available
            var xml = window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest();
            // Open the socket
            xml.open(s.type, s.url, s.async);
            // Set the correct header, if data is being sent
            if (s.data) {
                "dk.brics.tajs.directives.unreachable";
                xml.setRequestHeader("Content-Type", s.contentType);
            }
            // Set the If-Modified-Since header, if ifModified mode.
            if (s.ifModified) {
                "dk.brics.tajs.directives.unreachable";
                xml.setRequestHeader("If-Modified-Since", jQuery.lastModified[s.url] || "Thu, 01 Jan 1970 00:00:00 GMT");
            }
            // Set header so the called script knows that it's an XMLHttpRequest
            xml.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            // Allow custom headers/mimetypes
            if (s.beforeSend) {
                "dk.brics.tajs.directives.unreachable";
                s.beforeSend(xml);
            }
            if (s.global) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger("ajaxSend", [ xml, s ]);
            }
            // Wait for a response to come back
            var onreadystatechange = function(isTimeout) {
                "dk.brics.tajs.directives.unreachable";
                // The transfer is complete and the data is available, or the request timed out
                if (!requestDone && xml && (xml.readyState == 4 || isTimeout == "timeout")) {
                    "dk.brics.tajs.directives.unreachable";
                    requestDone = true;
                    // clear poll interval
                    if (ival) {
                        "dk.brics.tajs.directives.unreachable";
                        clearInterval(ival);
                        ival = null;
                    }
                    status = isTimeout == "timeout" && "timeout" || !jQuery.httpSuccess(xml) && "error" || s.ifModified && jQuery.httpNotModified(xml, s.url) && "notmodified" || "success";
                    if (status == "success") {
                        "dk.brics.tajs.directives.unreachable";
                        // Watch for, and catch, XML document parse errors
                        try {
                            "dk.brics.tajs.directives.unreachable";
                            // process the data (runs the xml through httpData regardless of callback)
                            data = jQuery.httpData(xml, s.dataType);
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
                            modRes = xml.getResponseHeader("Last-Modified");
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
                        jQuery.handleError(s, xml, status);
                    }
                    // Fire the complete handlers
                    complete();
                    // Stop memory leaks
                    if (s.async) {
                        "dk.brics.tajs.directives.unreachable";
                        xml = null;
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
                        if (xml) {
                            "dk.brics.tajs.directives.unreachable";
                            // Cancel the request
                            xml.abort();
                            if (!requestDone) {
                                "dk.brics.tajs.directives.unreachable";
                                onreadystatechange("timeout");
                            }
                        }
                    }, s.timeout);
                }
            }
            // Send the data
            try {
                "dk.brics.tajs.directives.unreachable";
                xml.send(s.data);
            } catch (e) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.handleError(s, xml, null, e);
            }
            // firefox 1.5 doesn't fire statechange for sync requests
            if (!s.async) {
                "dk.brics.tajs.directives.unreachable";
                onreadystatechange();
            }
            // return XMLHttpRequest to allow aborting the request etc.
            return xml;
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
                    jQuery.event.trigger("ajaxSuccess", [ xml, s ]);
                }
            }
            function complete() {
                "dk.brics.tajs.directives.unreachable";
                // Process result
                if (s.complete) {
                    "dk.brics.tajs.directives.unreachable";
                    s.complete(xml, status);
                }
                // The request was completed
                if (s.global) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger("ajaxComplete", [ xml, s ]);
                }
                // Handle the global AJAX counter
                if (s.global && !--jQuery.active) {
                    "dk.brics.tajs.directives.unreachable";
                    jQuery.event.trigger("ajaxStop");
                }
            }
        },
        handleError: function(s, xml, status, e) {
            "dk.brics.tajs.directives.unreachable";
            // If a local callback was specified, fire it
            if (s.error) {
                "dk.brics.tajs.directives.unreachable";
                s.error(xml, status, e);
            }
            // Fire the global callback
            if (s.global) {
                "dk.brics.tajs.directives.unreachable";
                jQuery.event.trigger("ajaxError", [ xml, s, e ]);
            }
        },
        // Counter for holding the number of active queries
        active: 0,
        // Determines if an XMLHttpRequest was successful or not
        httpSuccess: function(r) {
            "dk.brics.tajs.directives.unreachable";
            try {
                "dk.brics.tajs.directives.unreachable";
                return !r.status && location.protocol == "file:" || r.status >= 200 && r.status < 300 || r.status == 304 || jQuery.browser.safari && r.status == undefined;
            } catch (e) {}
            return false;
        },
        // Determines if an XMLHttpRequest returns NotModified
        httpNotModified: function(xml, url) {
            "dk.brics.tajs.directives.unreachable";
            try {
                "dk.brics.tajs.directives.unreachable";
                var xmlRes = xml.getResponseHeader("Last-Modified");
                // Firefox always returns 200. check Last-Modified date
                return xml.status == 304 || xmlRes == jQuery.lastModified[url] || jQuery.browser.safari && xml.status == undefined;
            } catch (e) {}
            return false;
        },
        httpData: function(r, type) {
            "dk.brics.tajs.directives.unreachable";
            var ct = r.getResponseHeader("content-type");
            var xml = type == "xml" || !type && ct && ct.indexOf("xml") >= 0;
            var data = xml ? r.responseXML : r.responseText;
            if (xml && data.documentElement.tagName == "parsererror") {
                "dk.brics.tajs.directives.unreachable";
                throw "parsererror";
            }
            // If the type is "script", eval it in global context
            if (type == "script") {
                "dk.brics.tajs.directives.unreachable";
                jQuery.globalEval(data);
            }
            // Get the JavaScript object, if JSON is used.
            if (type == "json") {
                "dk.brics.tajs.directives.unreachable";
                data = eval("(" + data + ")");
            }
            return data;
        },
        // Serialize an array of form elements or a set of
        // key/values into a query string
        param: function(a) {
            "dk.brics.tajs.directives.unreachable";
            var s = [];
            // If an array was passed in, assume that it is an array
            // of form elements
            if (a.constructor == Array || a.jquery) // Serialize the form elements
            {
                "dk.brics.tajs.directives.unreachable";
                jQuery.each(a, function() {
                    "dk.brics.tajs.directives.unreachable";
                    s.push(encodeURIComponent(this.name) + "=" + encodeURIComponent(this.value));
                });
            } else // Serialize the key/values
            {
                "dk.brics.tajs.directives.unreachable";
                for (var j in a) // If the value is an array then the key names need to be repeated
                {
                    "dk.brics.tajs.directives.unreachable";
                    if (a[j] && a[j].constructor == Array) {
                        "dk.brics.tajs.directives.unreachable";
                        jQuery.each(a[j], function() {
                            "dk.brics.tajs.directives.unreachable";
                            s.push(encodeURIComponent(j) + "=" + encodeURIComponent(this));
                        });
                    } else {
                        "dk.brics.tajs.directives.unreachable";
                        s.push(encodeURIComponent(j) + "=" + encodeURIComponent(a[j]));
                    }
                }
            }
            // Return the resulting serialization
            return s.join("&").replace(/%20/g, "+");
        }
    });
    jQuery.fn.extend({
        show: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return speed ? this.animate({
                height: "show",
                width: "show",
                opacity: "show"
            }, speed, callback) : this.filter(":hidden").each(function() {
                "dk.brics.tajs.directives.unreachable";
                this.style.display = this.oldblock ? this.oldblock : "";
                if (jQuery.css(this, "display") == "none") {
                    "dk.brics.tajs.directives.unreachable";
                    this.style.display = "block";
                }
            }).end();
        },
        hide: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return speed ? this.animate({
                height: "hide",
                width: "hide",
                opacity: "hide"
            }, speed, callback) : this.filter(":visible").each(function() {
                "dk.brics.tajs.directives.unreachable";
                this.oldblock = this.oldblock || jQuery.css(this, "display");
                if (this.oldblock == "none") {
                    "dk.brics.tajs.directives.unreachable";
                    this.oldblock = "block";
                }
                this.style.display = "none";
            }).end();
        },
        // Save the old toggle function
        _toggle: jQuery.fn.toggle,
        toggle: function(fn, fn2) {
            "dk.brics.tajs.directives.unreachable";
            return jQuery.isFunction(fn) && jQuery.isFunction(fn2) ? this._toggle(fn, fn2) : fn ? this.animate({
                height: "toggle",
                width: "toggle",
                opacity: "toggle"
            }, fn, fn2) : this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                if(jQuery(this).is(":hidden")){
                    jQuery(this).show();
                }else{
                    jQuery(this).hide();
                }
            });
        },
        slideDown: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate({
                height: "show"
            }, speed, callback);
        },
        slideUp: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate({
                height: "hide"
            }, speed, callback);
        },
        slideToggle: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate({
                height: "toggle"
            }, speed, callback);
        },
        fadeIn: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate({
                opacity: "show"
            }, speed, callback);
        },
        fadeOut: function(speed, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate({
                opacity: "hide"
            }, speed, callback);
        },
        fadeTo: function(speed, to, callback) {
            "dk.brics.tajs.directives.unreachable";
            return this.animate({
                opacity: to
            }, speed, callback);
        },
        animate: function(prop, speed, easing, callback) {
            "dk.brics.tajs.directives.unreachable";
            var opt = jQuery.speed(speed, easing, callback);
            var f = function() {
                "dk.brics.tajs.directives.unreachable";
                opt = jQuery.extend({}, opt);
                var hidden = jQuery(this).is(":hidden"), self = this;
                for (var p in prop) {
                    "dk.brics.tajs.directives.unreachable";
                    if (prop[p] == "hide" && hidden || prop[p] == "show" && !hidden) {
                        "dk.brics.tajs.directives.unreachable";
                        return jQuery.isFunction(opt.complete) && opt.complete.apply(this);
                    }
                    if (p == "height" || p == "width") {
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
                        var parts = val.toString().match(/^([+-]?)([\d.]+)(.*)$/), start = e.cur(true) || 0;
                        if (parts) {
                            "dk.brics.tajs.directives.unreachable";
                            end = parseFloat(parts[2]), unit = parts[3] || "px";
                            // We need to compute starting value
                            if (unit != "px") {
                                "dk.brics.tajs.directives.unreachable";
                                self.style[name] = end + unit;
                                start = end / e.cur(true) * start;
                                self.style[name] = start + unit;
                            }
                            // If a +/- token was provided, we're doing a relative animation
                            if (parts[1]) {
                                "dk.brics.tajs.directives.unreachable";
                                end = (parts[1] == "-" ? -1 : 1) * end + start;
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
            if(opt.queue === false){
                return this.each(f);
            }else{
                return this.queue(f);
            } 
        },
        queue: function(type, fn) {
            "dk.brics.tajs.directives.unreachable";
            if (!fn) {
                "dk.brics.tajs.directives.unreachable";
                fn = type;
                type = "fx";
            }
            if (!arguments.length) {
                "dk.brics.tajs.directives.unreachable";
                return queue(this[0], type);
            }
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                if (fn.constructor == Array) {
                    "dk.brics.tajs.directives.unreachable";
                    queue(this, type, fn);
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    queue(this, type).push(fn);
                    if (queue(this, type).length == 1) {
                        "dk.brics.tajs.directives.unreachable";
                        fn.apply(this);
                    }
                }
            });
        },
        dequeue: function(type) {
            "dk.brics.tajs.directives.unreachable";
            type = type || "fx";
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                var q = queue(this, type);
                q.shift();
                if (q.length) {
                    "dk.brics.tajs.directives.unreachable";
                    q[0].apply(this);
                }
            });
        },
        stop: function() {
            "dk.brics.tajs.directives.unreachable";
            var timers = jQuery.timers;
            return this.each(function() {
                "dk.brics.tajs.directives.unreachable";
                for (var i = 0; i < timers.length; i++) {
                    "dk.brics.tajs.directives.unreachable";
                    if (timers[i].elem == this) {
                        "dk.brics.tajs.directives.unreachable";
                        timers.splice(i--, 1);
                    }
                }
            }).dequeue();
        }
    });
    function queue(elem, type, array) {
        "dk.brics.tajs.directives.unreachable";
        if (!elem) {
            "dk.brics.tajs.directives.unreachable";
            return;
        }
        var queue = jQuery.data(elem, type + "queue");
        if (!queue || array) {
            "dk.brics.tajs.directives.unreachable";
            queue = jQuery.data(elem, type + "queue", array ? jQuery.makeArray(array) : []);
        }
        return queue;
    }
    jQuery.extend({
        speed: function(speed, easing, fn) {
            "dk.brics.tajs.directives.unreachable";
            var opt = speed && speed.constructor == Object ? speed : {
                complete: fn || !fn && easing || jQuery.isFunction(speed) && speed,
                duration: speed,
                easing: fn && easing || easing && easing.constructor != Function && easing
            };
            opt.duration = (opt.duration && opt.duration.constructor == Number ? opt.duration : {
                slow: 600,
                fast: 200
            }[opt.duration]) || 400;
            // Queueing
            opt.old = opt.complete;
            opt.complete = function() {
                "dk.brics.tajs.directives.unreachable";
                jQuery(this).dequeue();
                if (jQuery.isFunction(opt.old)) {
                    "dk.brics.tajs.directives.unreachable";
                    opt.old.apply(this);
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
                this.options.step.apply(this.elem, [ this.now, this ]);
            }
            (jQuery.fx.step[this.prop] || jQuery.fx.step._default)(this);
            // Set display property to block for height/width animations
            if (this.prop == "height" || this.prop == "width") {
                "dk.brics.tajs.directives.unreachable";
                this.elem.style.display = "block";
            }
        },
        // Get the current size
        cur: function(force) {
            "dk.brics.tajs.directives.unreachable";
            if (this.elem[this.prop] != null && this.elem.style[this.prop] == null) {
                "dk.brics.tajs.directives.unreachable";
                return this.elem[this.prop];
            }
            var r = parseFloat(jQuery.curCSS(this.elem, this.prop, force));
            return r && r > -1e4 ? r : parseFloat(jQuery.css(this.elem, this.prop)) || 0;
        },
        // Start an animation from one number to another
        custom: function(from, to, unit) {
            "dk.brics.tajs.directives.unreachable";
            this.startTime = new Date().getTime();
            this.start = from;
            this.end = to;
            this.unit = unit || this.unit || "px";
            this.now = this.start;
            this.pos = this.state = 0;
            this.update();
            var self = this;
            function t() {
                "dk.brics.tajs.directives.unreachable";
                return self.step();
            }
            t.elem = this.elem;
            jQuery.timers.push(t);
            if (jQuery.timers.length == 1) {
                "dk.brics.tajs.directives.unreachable";
                var timer = setInterval(function() {
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
                        clearInterval(timer);
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
            this.custom(0, this.cur());
            // Make sure that we start at a small width/height to avoid any
            // flash of content
            if (this.prop == "width" || this.prop == "height") {
                "dk.brics.tajs.directives.unreachable";
                this.elem.style[this.prop] = "1px";
            }
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
        step: function() {
            "dk.brics.tajs.directives.unreachable";
            var t = new Date().getTime();
            if (t > this.options.duration + this.startTime) {
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
                        this.elem.style.display = "none";
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
                // If a callback was provided, execute it
                if (done && jQuery.isFunction(this.options.complete)) // Execute the complete function
                {
                    "dk.brics.tajs.directives.unreachable";
                    this.options.complete.apply(this.elem);
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
    jQuery.fx.step = {
        scrollLeft: function(fx) {
            "dk.brics.tajs.directives.unreachable";
            fx.elem.scrollLeft = fx.now;
        },
        scrollTop: function(fx) {
            "dk.brics.tajs.directives.unreachable";
            fx.elem.scrollTop = fx.now;
        },
        opacity: function(fx) {
            "dk.brics.tajs.directives.unreachable";
            jQuery.attr(fx.elem.style, "opacity", fx.now);
        },
        _default: function(fx) {
            "dk.brics.tajs.directives.unreachable";
            fx.elem.style[fx.prop] = fx.now + fx.unit;
        }
    };
    // The Offset Method
    // Originally By Brandon Aaron, part of the Dimension Plugin
    // http://jquery.com/plugins/project/dimensions
    jQuery.fn.offset = function() {
        "dk.brics.tajs.directives.unreachable";
        var left = 0, top = 0, elem = this[0], results;
        if (elem) {
            "dk.brics.tajs.directives.unreachable";
            with (jQuery.browser) {
                "dk.brics.tajs.directives.unreachable";
                var absolute = jQuery.css(elem, "position") == "absolute", parent = elem.parentNode, offsetParent = elem.offsetParent, doc = elem.ownerDocument, safari2 = safari && !absolute && parseInt(version) < 522;
                // Use getBoundingClientRect if available
                if (elem.getBoundingClientRect) {
                    "dk.brics.tajs.directives.unreachable";
                    box = elem.getBoundingClientRect();
                    // Add the document scroll offsets
                    add(box.left + Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft), box.top + Math.max(doc.documentElement.scrollTop, doc.body.scrollTop));
                    // IE adds the HTML element's border, by default it is medium which is 2px
                    // IE 6 and IE 7 quirks mode the border width is overwritable by the following css html { border: 0; }
                    // IE 7 standards mode, the border is always 2px
                    if (msie) {
                        "dk.brics.tajs.directives.unreachable";
                        var border = jQuery("html").css("borderWidth");
                        border = (border == "medium" || jQuery.boxModel && parseInt(version) >= 7) && 2 || border;
                        add(-border, -border);
                    }
                } else {
                    "dk.brics.tajs.directives.unreachable";
                    // Initial element offsets
                    add(elem.offsetLeft, elem.offsetTop);
                    // Get parent offsets
                    while (offsetParent) {
                        "dk.brics.tajs.directives.unreachable";
                        // Add offsetParent offsets
                        add(offsetParent.offsetLeft, offsetParent.offsetTop);
                        // Mozilla and Safari > 2 does not include the border on offset parents
                        // However Mozilla adds the border for table cells
                        if (mozilla && /^t[d|h]$/i.test(parent.tagName) || !safari2) {
                            "dk.brics.tajs.directives.unreachable";
                            border(offsetParent);
                        }
                        // Safari <= 2 doubles body offsets with an absolutely positioned element or parent
                        if (safari2 && !absolute && jQuery.css(offsetParent, "position") == "absolute") {
                            "dk.brics.tajs.directives.unreachable";
                            absolute = true;
                        }
                        // Get next offsetParent
                        offsetParent = offsetParent.offsetParent;
                    }
                    // Get parent scroll offsets
                    while (parent.tagName && /^body|html$/i.test(parent.tagName)) {
                        "dk.brics.tajs.directives.unreachable";
                        // Work around opera inline/table scrollLeft/Top bug
                        if (/^inline|table-row.*$/i.test(jQuery.css(parent, "display"))) // Subtract parent scroll offsets
                        {
                            "dk.brics.tajs.directives.unreachable";
                            add(-parent.scrollLeft, -parent.scrollTop);
                        }
                        // Mozilla does not add the border for a parent that has overflow != visible
                        if (mozilla && jQuery.css(parent, "overflow") != "visible") {
                            "dk.brics.tajs.directives.unreachable";
                            border(parent);
                        }
                        // Get next parent
                        parent = parent.parentNode;
                    }
                    // Safari doubles body offsets with an absolutely positioned element or parent
                    if (safari && absolute) {
                        "dk.brics.tajs.directives.unreachable";
                        add(-doc.body.offsetLeft, -doc.body.offsetTop);
                    }
                }
                // Return an object with top and left properties
                results = {
                    top: top,
                    left: left
                };
            }
        }
        return results;
        function border(elem) {
            "dk.brics.tajs.directives.unreachable";
            add(jQuery.css(elem, "borderLeftWidth"), jQuery.css(elem, "borderTopWidth"));
        }
        function add(l, t) {
            "dk.brics.tajs.directives.unreachable";
            left += parseInt(l) || 0;
            top += parseInt(t) || 0;
        }
    };
})();
