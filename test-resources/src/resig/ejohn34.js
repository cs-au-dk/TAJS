function User(first, last){ 
  this.name = first + " " + last; 
} 
 
var user = User("John", "Resig"); 
TAJS_assert( typeof user == "undefined" );
TAJS_dumpValue(typeof user);
TAJS_dumpValue(user);
