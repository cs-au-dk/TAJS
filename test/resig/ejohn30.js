function Ninja(){ 
  this.name = "Ninja"; 
} 
 
var ninjaA = Ninja(); 
TAJS_assert( !ninjaA ); 
 
var ninjaB = new Ninja(); 
TAJS_assert( ninjaB.name == "Ninja" );
TAJS_dumpValue(ninjaB.name);