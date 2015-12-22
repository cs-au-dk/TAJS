var count = 0; 
 
var timer = setInterval(function(){ 
  if ( count < 5 ) { 
    TAJS_dumpValue( count );
    count++; 
  } else { 
    TAJS_dumpValue( count );
    TAJS_dumpValue( timer );
    clearInterval( timer ); 
  } 
}, 100);
