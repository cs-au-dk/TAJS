if (typeof console === "undefined") {
    console = {};
    console.memory = {};
    console.assert = function () {
    };
    console.clear = function () {
    };
    console.count = function () {
        TAJS_assert(false);
    };
    console.debug = function () {
    };
    console.dir = function () {
        TAJS_assert(false);
    };
    console.dirxml = function () {
        TAJS_assert(false);
    };
    console.error = function () {
    };
    console.exception = function () {
        TAJS_assert(false);
    };
    console.group = function () {
        TAJS_assert(false);
    };
    console.groupCollapsed = function () {
        TAJS_assert(false);
    };
    console.groupEnd = function () {
        TAJS_assert(false);
    };
    console.info = function () {
    };
    console.log = function () {
    };
    console.markTimeline = function () {
        TAJS_assert(false);
    };
    console.profile = function () {
        TAJS_assert(false);
    };
    console.profiles = function () {
        TAJS_assert(false);
    };
    console.profileEnd = function () {
        TAJS_assert(false);
    };
    console.show = function () {
        TAJS_assert(false);
    };
    console.table = function () {
        TAJS_assert(false);
    };
    console.time = function () {
        TAJS_assert(false);
    };
    console.timeEnd = function () {
        TAJS_assert(false);
    };
    console.timeline = function () {
        TAJS_assert(false);
    };
    console.timelineEnd = function () {
        TAJS_assert(false);
    };
    console.timeStamp = function () {
        TAJS_assert(false);
    };
    console.trace = function () {
    };
    console.warn = function () {
    };
}