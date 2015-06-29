function Ninja(){} 
 
Ninja.prototype.swingSword = function(){ 
  return true; 
}; 
 
var ninjaA = Ninja(); 
TAJS_assert( !ninjaA ); 
 
var ninjaB = new Ninja(); 
TAJS_assert( ninjaB.swingSword() );
