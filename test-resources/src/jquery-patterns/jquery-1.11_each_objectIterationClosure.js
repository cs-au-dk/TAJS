// TAJS ::: $.each test #3: Iteration over object, creates a closure over parameters

jQuery.each({
    parent: "parentFun",
    parents: "parentsFun",
    parentsUntil: "parentsUntilFun",
    next: "nextFun"
}, function (name, fn) {
    jQuery.fn[ name ] = function () {
        // TAJS: modified function body for testing
        return name + "_" + fn
    };
});

var parentResult = jQuery.fn.parent();
var parentsResult = jQuery.fn.parents();
var parentsUntilResult = jQuery.fn.parentsUntil();
var nextResult = jQuery.fn.next();

TAJS_assert(parentResult === "parent_parentFun");
TAJS_assert(parentsResult === "parents_parentsFun");
TAJS_assert(parentsUntilResult === "parentsUntil_parentsUntilFun");
TAJS_assert(nextResult === "next_nextFun");
