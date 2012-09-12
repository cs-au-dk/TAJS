function Ninja(){ 
  this.swung = false; 
   
  // Should return true 
  this.swingSword = function(){ 
    this.swung = !this.swung; 
    return this.swung; 
  }; 
} 
 
var ninja = new Ninja(); 
assert( ninja.swingSword() ); 
assert( ninja.swung ); 
dumpValue(ninja.swung);
 
var ninjaB = new Ninja(); 
assert( !ninjaB.swung );
dumpValue(ninjaB.swung);
