 function f(x) { null } 
function g(x) { x = 4; x = new f(2) }

g();
