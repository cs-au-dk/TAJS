(function(){ 
  var count = 0; 
 
  var timer = setInterval(function(){ 
    if ( count < 5 ) { 
      log( "Timer call: ", count ); 
      count++; 
    } else { 
      assert( count == 5 ); 
      assert( timer ); 
      clearInterval( timer ); 
    } 
  }, 100); 
})(); 
 
assert( typeof count == "undefined" ); 
assert( typeof timer == "undefined" );
