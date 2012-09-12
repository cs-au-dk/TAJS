  function f(x) { this } 
function g(x) { x = 4; x = new f(2) }

g();
