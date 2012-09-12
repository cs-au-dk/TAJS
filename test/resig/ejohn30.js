function Ninja(){ 
  this.name = "Ninja"; 
} 
 
var ninjaA = Ninja(); 
assert( !ninjaA ); 
 
var ninjaB = new Ninja(); 
assert( ninjaB.name == "Ninja" );
dumpValue(ninjaB.name);