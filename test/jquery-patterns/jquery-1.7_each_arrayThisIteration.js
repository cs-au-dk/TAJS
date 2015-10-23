// TAJS ::: $.each test #5: Callback use 'this' instead of parameters (and iteration over a complex array)

var fxAttrs = [ // height animations
    [ "height", "marginTop", "marginBottom", "paddingTop", "paddingBottom" ], // width animations
    [ "width", "marginLeft", "marginRight", "paddingLeft", "paddingRight" ], // opacity animations
    [ "opacity" ]
];

// Generate parameters to create a standard animation
function genFx(type, num) {
    var obj = {};

    // TAJS: modified variables for testing
    var extracted = fxAttrs.concat.apply([], fxAttrs.slice(0, num));

    TAJS_assert(extracted[0] === fxAttrs[0][0]);
    TAJS_assert(extracted[1] === fxAttrs[0][1]);
    TAJS_assert(extracted[2] === fxAttrs[0][2]);

    jQuery.each(extracted, function () {
        obj[this] = type;
    });
    return obj;
}

// Generate shortcuts for custom animations
jQuery.each({
    slideDown: genFx("show", 1),
    slideUp: genFx("hide", 1),
    slideToggle: genFx("toggle", 1)
}, function (name, props) {
    jQuery.fn[name] = function (speed, easing, callback) {
        // TAJS: modified function body for testing
        return name + "_" + props.height;
    };
});

TAJS_assert(jQuery.fn.slideToggle() === 'slideToggle_toggle');
