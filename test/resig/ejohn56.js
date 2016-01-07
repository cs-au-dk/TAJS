var a = 5; 
function runMe(a){
// TAJS_dumpValue( a );
 TAJS_assert( a == 6 );
 
 function innerRun(){ 
//   TAJS_dumpValue( b );
   TAJS_assert( b == 7 );
//   TAJS_dumpValue( c );
   TAJS_assert( c == undefined );
 } 
 
 var b = 7; 
 innerRun(); 
 var c = 8; 
} 
runMe(6); 
 
for ( var d = 0; d < 3; d++ ) { 
 setTimeout(function(){ 
//   TAJS_dumpValue( d );
   TAJS_assert( d == 3 );
 }, 100); 
}
