// TAJS ::: $.each test #4: Nested uses of each, outer callback creates objects to iterate iterate over, inner callback creates a closure over parameters

// Create innerHeight, innerWidth, height, width, outerHeight and outerWidth methods
jQuery.each({ Height: "height", Width: "width" }, function (name, type) {
    jQuery.each({ padding: "inner" + name, content: type, "": "outer" + name },
        function (defaultExtra, funcName) {
            // margin is only for outerHeight, outerWidth
            jQuery.fn[ funcName ] = function () {
                // TAJS: modified function body for testing
                return defaultExtra + "_" + name + "_" + type;
            }
        });
});

var innerHeightResult = jQuery.fn.innerHeight();
var innerWidthResult = jQuery.fn.innerWidth();
var heightResult = jQuery.fn.height();
var widthResult = jQuery.fn.width();
var outerHeightResult = jQuery.fn.outerHeight();
var outerWidthResult = jQuery.fn.outerWidth();

TAJS_assert(innerHeightResult === "padding_Height_height");
TAJS_assert(innerWidthResult === "padding_Width_width");
TAJS_assert(heightResult === "content_Height_height");
TAJS_assert(widthResult === "content_Width_width");
TAJS_assert(outerHeightResult === "_Height_height");
TAJS_assert(outerWidthResult === "_Width_width");
