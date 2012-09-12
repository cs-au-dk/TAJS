jQuery = function() {}

jQuery.extend = function() {
    for (i = 0; i < 1; i++ ) {
	    options = arguments[ 0 ]
	    dumpObject(options)
	    for ( name in options ) {
	      this[name] = options[name]
	    }
    }
}

jQuery.extend({
    each: function( ) {},
    browser: {}
});

dumpObject(jQuery)
dumpValue(jQuery.browser)
dumpValue(jQuery.each)