function Ninja(){} 
 
var ninja = new Ninja(); 
 
TAJS_assert( typeof ninja == "object" );   
TAJS_assert( ninja instanceof Ninja ); 
TAJS_assert( ninja.constructor == Ninja );
