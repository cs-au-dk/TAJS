function Ninja(){ 
  this.swung = true; 
} 
 
var ninjaA = new Ninja(); 
var ninjaB = new Ninja(); 
 
Ninja.prototype.swingSword = function(){ 
  return this.swung; 
}; 
 
TAJS_assert( ninjaA.swingSword() ); 
TAJS_assert( ninjaB.swingSword() );
TAJS_dumpValue(ninjaA.swingSword());
TAJS_dumpValue(ninjaB.swingSword());