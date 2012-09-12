function qw(arg2) {
  var local = {}
  arg2.bar();
}

var x = {}

x.bar = function() {
  return this.foo();
}

x.foo = function() {}

x.bar( );

qw(x );

x.foo();
