function Ninja(){} 
 
var ninja = new Ninja(); 
 
assert( typeof ninja == "object" );   
assert( ninja instanceof Ninja ); 
assert( ninja.constructor == Ninja );
