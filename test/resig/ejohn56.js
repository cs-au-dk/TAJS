var a = 5; 
function runMe(a){ 
 assert( a == 6 ); 
 
 function innerRun(){ 
   assert( b == 7 ); 
   assert( c == undefined ); 
 } 
 
 var b = 7; 
 innerRun(); 
 var c = 8; 
} 
runMe(6); 
 
for ( var d = 0; d < 3; d++ ) { 
 setTimeout(function(){ 
   assert( d == 3 ); 
 }, 100); 
}
