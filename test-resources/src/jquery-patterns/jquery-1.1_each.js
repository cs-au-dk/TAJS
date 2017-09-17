var jQuery = {
    each: function (obj, fn, args) {
        if (obj.length == undefined)
            for (var i in obj)
                fn.apply(obj[i], args || [i, obj[i]]);
        else
            for (var i = 0, ol = obj.length; i < ol; i++)
                if (fn.apply(obj[i], args || [i, obj[i]]) === false) break;
        return obj;
    },
    fn: {}
};

jQuery.each({
    parent: "a.parentNode",
    parents: "jQuery.parents(a)",
    next: "jQuery.nth(a,2,'nextSibling')",
    prev: "jQuery.nth(a,2,'previousSibling')"
}, function (i, n) {
    jQuery.fn[ i ] = function (a) {
        // TAJS: modified function body for testing
        return n;
    };
});
var parentResult = jQuery.fn.parent();
var parentsResult = jQuery.fn.parents();
var nextResult = jQuery.fn.next();
var prevResult = jQuery.fn.prev();

TAJS_assert(parentResult === "a.parentNode");
TAJS_assert(parentsResult === "jQuery.parents(a)");
TAJS_assert(nextResult === "jQuery.nth(a,2,'nextSibling')");
TAJS_assert(prevResult === "jQuery.nth(a,2,'previousSibling')");
