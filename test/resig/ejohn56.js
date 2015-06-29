var a = 5; 
function runMe(a){ 
 TAJS_assert( a == 6 ); 
 
 function innerRun(){ 
   TAJS_assert( b == 7 ); 
   TAJS_assert( c == undefined ); 
 } 
 
 var b = 7; 
 innerRun(); 
 var c = 8; 
} 
runMe(6); 
 
for ( var d = 0; d < 3; d++ ) { 
 setTimeout(function(){ 
   TAJS_assert( d == 3 ); 
 }, 100); 
}
