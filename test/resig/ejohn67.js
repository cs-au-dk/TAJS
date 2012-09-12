function Ninja(){ 
  this.swung = true; 
} 
 
var ninjaA = new Ninja(); 
var ninjaB = new Ninja(); 
 
Ninja.prototype.swingSword = function(){ 
  return this.swung; 
}; 
 
assert( ninjaA.swingSword() ); 
assert( ninjaB.swingSword() );
dumpValue(ninjaA.swingSword());
dumpValue(ninjaB.swingSword());