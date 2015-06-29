f = function() {
    var local_var = 0;
    this[ Math.random()? "foo":"bar" ] = {};
    while ( Math.random() ) {
    	TAJS_dumpValue(local_var);  // <--- 0.0 | absent | <@ and * of '{}' above>
        (function() {
         f();
         })();
    }
};
f();
