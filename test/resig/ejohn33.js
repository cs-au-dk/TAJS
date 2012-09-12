function Ninja(name){ 
  this.changeName = function(name){ 
    this.name = name; 
  }; 
 
  this.changeName( name ); 
} 
 
var ninja = new Ninja("John"); 
assert( ninja.name == "John" ); 
dumpValue(ninja.name);
 
ninja.changeName("Bob"); 
assert( ninja.name == "Bob" );
dumpValue(ninja.name);
