for ( var d = 0; d < 3; d++ ) (function(d){ 
 setTimeout(function(){ 
   TAJS_dumpValue( d );
 }, d * 200); 
})(d);
