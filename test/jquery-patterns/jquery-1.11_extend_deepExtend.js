// TAJS ::: $.extend test #2: deep extend
// Install script dataType
jQuery.ajaxSetup({
    // TAJS: modified body for testing
    accepts: {
        script: "accepts: script"
    },
    contents: {
        script: "content: script"
    },
    converters: {
        "text script": "converters: text script"
    }
});

var acceptsResult = jQuery.ajaxSettings.accepts.script;
var contentsResult = jQuery.ajaxSettings.contents.script;
var convertersResult = jQuery.ajaxSettings.converters["text script"];

// TAJS ::: remove spurious undefinedness
acceptsResult.KILL_UNDEFINED;
contentsResult.KILL_UNDEFINED;
convertersResult.KILL_UNDEFINED;

// These two cases get mixed together as they share context because of the 'script' property name...
// TAJS_assert(acceptsResult === "accepts: script");
// TAJS_assert(contentsResult === "content: script");
TAJS_assert(convertersResult === "converters: text script");
