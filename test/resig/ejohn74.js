var ninja = (function(){ 
 function Ninja(){} 
 return new Ninja(); 
})(); 
 
// Make another instance of Ninja 
var ninjaB = new ninja.constructor(); 
 
assert( ninja.constructor == ninjaB.constructor );
dumpValue(ninja.constructor);
dumpValue(ninjaB.constructor);
