function Person(){} 
Person.prototype.dance = function(){}; 
 
function Ninja(){} 
 
// Achieve similar, but non-inheritable, results 
Ninja.prototype = Person.prototype; 
Ninja.prototype = { dance: Person.prototype.dance }; 
 
assert( (new Ninja()) instanceof Person ); // fails!
 
// Only this maintains the prototype chain 
Ninja.prototype = new Person(); 
 
var ninja = new Ninja(); 
assert( ninja instanceof Ninja ); 
assert( ninja instanceof Person ); 
assert( ninja instanceof Object );
