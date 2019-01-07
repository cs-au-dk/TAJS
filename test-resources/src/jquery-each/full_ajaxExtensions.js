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
        TAJS_addContextSensitivity(callback, 0);
        var name, i = 0, length = obj.length, isObj = length === undefined || jQuery.isFunction(obj);
        if (args) {
            if (isObj) {
                for (name in obj) {
                    if (callback.apply(obj[name], args) === false) {
                        break;
                    }
                }
            } else {
                for (;i < length; ) {
                    if (callback.apply(obj[i++], args) === false) {
                        break;
                    }
                }
            }
        } else {
            if (isObj) {
                for (name in obj) {
                    if (callback.call(obj[name], name, obj[name]) === false) {
                        break;
                    }
                }
            } else {
                for (;i < length; ) {
                    if (callback.call(obj[i], i, obj[i++]) === false) {
                        break;
                    }
                }
            }
        }
        return obj;
    }
};
TAJS_addContextSensitivity(jQuery.each, 0);
jQuery.fn = {};
jQuery.each(({ajaxStart: "ajaxStart", ajaxStop: "ajaxStop", ajaxComplete: "ajaxComplete",
              ajaxError: "ajaxError", ajaxSuccess: "ajaxSuccess", ajaxSend: "ajaxSend"}), function(i, o) {
                  jQuery.fn[o] = function(f) {
                      return this.on(o, f);
                  };
              });
TAJS_assert(jQuery.fn.ajaxStop, "isMaybeObject");
jQuery.fn.ajaxStop.KILL_UNDEFINED;
TAJS_assert(jQuery.fn.ajaxStop, "isMaybePrimitiveOrSymbol", false);
