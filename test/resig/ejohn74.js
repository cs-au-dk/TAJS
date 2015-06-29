var ninja = (function(){ 
 function Ninja(){} 
 return new Ninja(); 
})(); 
 
// Make another instance of Ninja 
var ninjaB = new ninja.constructor(); 
 
TAJS_assert( ninja.constructor == ninjaB.constructor );
TAJS_dumpValue(ninja.constructor);
TAJS_dumpValue(ninjaB.constructor);
