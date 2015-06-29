function Person(){} 
Person.prototype.dance = function(){}; 
 
function Ninja(){} 
 
// Achieve similar, but non-inheritable, results 
Ninja.prototype = Person.prototype; 
Ninja.prototype = { dance: Person.prototype.dance }; 
 
TAJS_assert( (new Ninja()) instanceof Person, 'isMaybeFalseButNotTrue' );
 
// Only this maintains the prototype chain 
Ninja.prototype = new Person(); 
 
var ninja = new Ninja(); 
TAJS_assert( ninja instanceof Ninja ); 
TAJS_assert( ninja instanceof Person ); 
TAJS_assert( ninja instanceof Object );
