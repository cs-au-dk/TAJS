function Ninja(){} 
var ninja = new Ninja(); 
var ninjaB = new ninja.constructor(); 
 
assert( ninjaB instanceof Ninja );
