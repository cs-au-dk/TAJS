var obj = {}; 
var fn = function(){}; 
obj.prop = "some value"; 
fn.prop = "some value"; 
TAJS_assert( obj.prop == fn.prop );
