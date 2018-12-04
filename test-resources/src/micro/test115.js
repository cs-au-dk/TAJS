var x;
if (Math.random())
 x = this;
else
 x = function() {};
x();

var y;
if (Math.random())
 y = null;
else
 y = {};
 
y.a;
