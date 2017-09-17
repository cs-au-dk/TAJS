function Ninja(){ 
  this.swung = true; 
} 
 
var ninjaA = new Ninja(); 
var ninjaB = new Ninja(); 
 
Ninja.prototype.swing = function(){ 
  this.swung = false; 
  return this; 
}; 
 
TAJS_assert( !ninjaA.swing().swung ); 
TAJS_assert( !ninjaB.swing().swung );
TAJS_dumpValue(ninjaA.swing().swung);
TAJS_dumpValue(ninjaB.swing().swung);
