function foo(){
  this.name = "bar"; 
} 
 
var ninjaA = foo(); 
 
var ninjaB = new foo(); 
dumpValue(ninjaB.name);
