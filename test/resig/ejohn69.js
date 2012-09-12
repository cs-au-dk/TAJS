function Ninja(){ 
  this.swung = true; 
} 
 
var ninjaA = new Ninja(); 
var ninjaB = new Ninja(); 
 
Ninja.prototype.swing = function(){ 
  this.swung = false; 
  return this; 
}; 
 
assert( !ninjaA.swing().swung ); 
assert( !ninjaB.swing().swung );
dumpValue(ninjaA.swing().swung);
dumpValue(ninjaB.swing().swung);
