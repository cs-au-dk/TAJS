
  function f1(x) {return x+100;}
  function f2(x) {return x+10000;}

  var ff = f1;

  function f3( ) {
    ff = f2;
  }

  f3( );

var z =  (function indirect( f ) { return f(2); })( ff );

dumpValue(ff);
dumpValue(z);

assert(z === 10002.0);