var ninja = { 
  yell: function(n){ 
    return n > 0 ? arguments.callee(n-1) + "a" : "hiy"; 
  } 
}; 
assert( ninja.yell(4) == "hiyaaaa" );
