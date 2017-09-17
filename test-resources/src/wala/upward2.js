function foo(x) {
  var state = x;

  this.set = function set(y) { state = y; };

  this.get = function get() { return state; };

};


var obj = new foo( function tester1() { return 3; } );

var test1 = ( obj.get() )();

obj.state = obj.set( function tester2() { return 7; } );	

var test2 = ( obj.get() )();
