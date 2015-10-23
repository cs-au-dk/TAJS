// extracted $.each and dependencies from jQuery-1.8.2.js
var core_toString = Object.prototype.toString;
var class2type = {};
// Populate the class2type map
// TAJS mocked to keep .each state simple to look at (it is inlined, so it has no semantic effects)
class2type["[object Boolean]"] = "boolean";
class2type["[object Number]"] = "number";
class2type["[object String]"] = "string";
class2type["[object Function]"] = "function";
class2type["[object Array]"] = "array";
class2type["[object Date]"] = "date";
class2type["[object Regexp]"] = "regexp";
class2type["[object Object]"] = "object";

var jQuery = {
    type: function(obj) {
        return obj == null ? String(obj) : class2type[core_toString.call(obj)] || "object";
    },

    isFunction: function(obj) {
        return jQuery.type(obj) === "function";
    },

    each: function(obj, callback, args) {
	TAJS_addContextSensitivity(callback, 0);
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
                "dk.brics.tajs.directives.unreachable";
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
    }
};
TAJS_addContextSensitivity(jQuery.each, 0);
var o1 = {};
jQuery.each(["x"], function(i, e){
    o1.v = e;
});
TAJS_assert(o1.v, "isMaybeSingleStr");

var o2 = {};
jQuery.each(["x", "y"], function(i, e){
    o2.v = e;
});
TAJS_assert(o2.v, "isMaybeSingleStr ");

var o3 = {};
jQuery.each(["x", "y", 2], function(i, e){
    o3.v = e;
});
TAJS_assert(o3.v, "isMaybeSingleNum ");

var o4 = {};
jQuery.each([2, "x", "y"], function(i, e){
    o4.v = e;
});
TAJS_assert(o4.v, "isMaybeSingleStr ");
