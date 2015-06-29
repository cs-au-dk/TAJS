jQuery = function() {}

jQuery.extend = function() {
    for (i = 0; i < 1; i++ ) {
	    options = arguments[ 0 ]
	    TAJS_dumpObject(options)
	    for ( name in options ) {
	      this[name] = options[name]
	    }
    }
}

jQuery.extend({
    each: function( ) {},
    browser: {}
});

TAJS_dumpObject(jQuery)
TAJS_dumpValue(jQuery.browser)
TAJS_dumpValue(jQuery.each)