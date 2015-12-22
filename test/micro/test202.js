var jQuery = { fn: {} };

function W() {}
W.prototype = jQuery.fn;

jQuery.fn.on = function on(o, f) {
	return f(o);
}
TAJS_addContextSensitivity(jQuery.fn.on, 0);

function callback( i, o ) {
	jQuery.fn[ o ] = function( f ){
		return this.on( o, f );
	};
}
TAJS_addContextSensitivity(callback, 0);

var obj = {
	    ajaxStart: "ajaxStart",
	    ajaxStop: "ajaxStop",
	    ajaxComplete: "ajaxComplete",
	    ajaxError: "ajaxError",
	    ajaxSuccess: "ajaxSuccess",
	    ajaxSend: "ajaxSend"
	};
for (var name in obj) {
  callback.call(obj[name], name, obj[name]);
} 

//TAJS_dumpState()
//TAJS_dumpValue(jQuery.fn.ajaxError)

var x = new W();
var t1 = x.ajaxError(function(y) {return y;})
var t2 = x.ajaxComplete(function(y) {return y;})
var t3 = x.ajaxSend(function(y) {return y;})
TAJS_dumpValue(t1)
TAJS_dumpValue(t2)
TAJS_dumpValue(t3)
