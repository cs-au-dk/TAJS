function Ninja(name){ 
  this.changeName = function(name){ 
    this.name = name; 
  }; 
 
  this.changeName( name ); 
} 
 
var ninja = new Ninja("John"); 
TAJS_assert( ninja.name == "John" ); 
TAJS_dumpValue(ninja.name);
 
ninja.changeName("Bob"); 
TAJS_assert( ninja.name == "Bob" );
TAJS_dumpValue(ninja.name);
