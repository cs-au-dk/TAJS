function Person(){} 
Person.prototype.getName = function(){ 
  return this.name; 
}; 
 
function Me(){ 
  this.name = "John Resig"; 
} 
Me.prototype = new Person(); 
 
var me = new Me(); 
assert( me.getName() );
