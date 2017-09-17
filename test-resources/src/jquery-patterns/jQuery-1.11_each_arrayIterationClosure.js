// TAJS ::: $.each test #2: Callback creates a closure over parameters

// Attach a bunch of functions for handling common AJAX events
jQuery.each([ "ajaxStart", "ajaxStop", "ajaxComplete", "ajaxError", "ajaxSuccess", "ajaxSend" ], function (i, type) {
    jQuery.fn[ type ] = function () {
        // TAJS: modified function body for testing
        return type;
    };
});

var ajaxStartResult = jQuery.fn.ajaxStart();
var ajaxStopResult = jQuery.fn.ajaxStop();
var ajaxCompleteResult = jQuery.fn.ajaxComplete();

TAJS_assert(ajaxStartResult === "ajaxStart");
TAJS_assert(ajaxStopResult === "ajaxStop");
TAJS_assert(ajaxCompleteResult === "ajaxComplete");
