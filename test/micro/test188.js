jQuery = function() {}

jQuery.extend = function() {
    for (i = 0; i < 1; i++ ) {
	    options = arguments[ 0 ]
	    //options = {a:2, b:9 } // Works!
	    dumpObject(options)
	    for ( name in options ) {
	      dumpValue("Inside loop")
	    }
    }
}

jQuery.extend({
    each: function( ) {},
    browser: {}
});

