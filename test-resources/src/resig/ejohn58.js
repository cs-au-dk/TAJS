(function(){ 
  var count = 0; 
 
  var timer = setInterval(function(){ 
    if ( count < 5 ) { 
      TAJS_dumpValue( count );
      count++; 
    } else { 
      TAJS_dumpValue( count );
//      TAJS_assert( count == 5 );
      TAJS_dumpValue( timer );
//      TAJS_assert( timer );
      clearInterval( timer ); 
    } 
  }, 100); 
})(); 
 
TAJS_assert( typeof count == "undefined" ); 
TAJS_assert( typeof timer == "undefined" );
