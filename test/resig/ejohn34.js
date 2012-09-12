function User(first, last){ 
  this.name = first + " " + last; 
} 
 
var user = User("John", "Resig"); 
assert( typeof user == "undefined" );
dumpValue(typeof user);
dumpValue(user);
