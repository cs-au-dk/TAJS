for ( var d = 0; d < 3; d++ ) (function(d){ 
 setTimeout(function(){ 
   log( "Value of d: ", d ); 
   assert( d == d ); 
 }, d * 200); 
})(d);
