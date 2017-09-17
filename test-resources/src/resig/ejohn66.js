function Ninja(){ 
  this.swingSword = function(){ 
    return true; 
  }; 
} 
 
// Should return false, but will be overridden 
Ninja.prototype.swingSword = function(){ 
  return false; 
}; 
 
var ninja = new Ninja(); 
TAJS_assert( ninja.swingSword() );
