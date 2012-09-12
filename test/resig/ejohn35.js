function User(first, last){ 
  this.name = first + " " + last; 
} 
 
name = "Resig"; 
var user = User("John", name); 
 
assert( name == "John Resig" );
dumpValue(name);
