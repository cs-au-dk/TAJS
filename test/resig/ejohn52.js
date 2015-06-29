var count = 0; 
 
var timer = setInterval(function(){ 
  if ( count < 5 ) { 
    log( "Timer call: ", count ); 
    count++; 
  } else { 
    TAJS_assert( count == 5 ); 
    TAJS_assert( timer ); 
    clearInterval( timer ); 
  } 
}, 100);
