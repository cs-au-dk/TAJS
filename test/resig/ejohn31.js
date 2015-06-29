function Ninja(){ 
  this.swung = false; 
   
  // Should return true 
  this.swingSword = function(){ 
    this.swung = !this.swung; 
    return this.swung; 
  }; 
} 
 
var ninja = new Ninja(); 
TAJS_assert( ninja.swingSword() ); 
TAJS_assert( ninja.swung ); 
TAJS_dumpValue(ninja.swung);
 
var ninjaB = new Ninja(); 
TAJS_assert( !ninjaB.swung );
TAJS_dumpValue(ninjaB.swung);
