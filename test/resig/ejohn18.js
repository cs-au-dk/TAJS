var obj = {}; 
var fn = function(){}; 
obj.prop = "some value"; 
fn.prop = "some value"; 
assert( obj.prop == fn.prop );
