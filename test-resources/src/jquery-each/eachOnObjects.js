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

var o1 = {};
jQuery.each({x: "xv"}, function(n, e){
    o1[n] = e;
});
TAJS_assert(o1.x, "isMaybeSingleStr");

var o2 = {};
jQuery.each({x: "xv", y: "yv"}, function(n, e){
    TAJS_addContextSensitivity("n");
    o2[n] = e;
});
TAJS_assert(o2.x, "isMaybeSingleStr");
TAJS_assert(o2.y, "isMaybeSingleStr");


var o3 = {};
jQuery.each({x: "xv", y: "yv", z: "zv"}, function(n, e){
    TAJS_addContextSensitivity("n");
    o3[n] = e;
});
TAJS_assert(o3.x, "isMaybeSingleStr");
TAJS_assert(o3.y, "isMaybeSingleStr");
TAJS_assert(o3.z, "isMaybeSingleStr");
