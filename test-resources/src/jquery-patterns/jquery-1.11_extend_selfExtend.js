// TAJS ::: $.extend test #1: extension of jQuery.fn
jQuery.fn.extend({
    // TAJS: modified body for testing
    find: "find",
    filter: "filter",
    not: "not"
});

var findResult = jQuery.fn.find;
var filterResult = jQuery.fn.filter;
var notResult = jQuery.fn.not;

// TAJS ::: remove spurious undefinedness
findResult.KILL_UNDEFINED;
filterResult.KILL_UNDEFINED;
notResult.KILL_UNDEFINED;

TAJS_assert(findResult === "find");
TAJS_assert(filterResult === "filter");
TAJS_assert(notResult === "not");
