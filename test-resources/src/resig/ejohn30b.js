function foo(){
  this.name = "bar"; 
} 
 
var ninjaA = foo(); 
 
var ninjaB = new foo(); 
TAJS_dumpValue(ninjaB.name);
