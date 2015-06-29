function User(first, last){ 
  this.name = first + " " + last; 
} 
 
name = "Resig"; 
var user = User("John", name); 
 
TAJS_assert( name == "John Resig" );
TAJS_dumpValue(name);
