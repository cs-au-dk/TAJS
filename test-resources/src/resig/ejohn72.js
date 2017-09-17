function Ninja(){} 
var ninja = new Ninja(); 
var ninjaB = new ninja.constructor(); 
 
TAJS_assert( ninjaB instanceof Ninja );
