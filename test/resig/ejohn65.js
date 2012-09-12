function Ninja(){} 
 
Ninja.prototype.swingSword = function(){ 
  return true; 
}; 
 
var ninjaA = Ninja(); 
assert( !ninjaA ); 
 
var ninjaB = new Ninja(); 
assert( ninjaB.swingSword() );
