var ninja = { 
  yell: function(n){ 
    return n > 0 ? ninja.yell(n-1) + "a" : "hiy"; 
  } 
}; 
assert( ninja.yell(4) == "hiyaaaa" );
assert( typeof(ninja.yell(4)) == "string" );
