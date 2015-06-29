var ninja = { 
  yell: function(n){ 
    return n > 0 ? ninja.yell(n-1) + "a" : "hiy"; 
  } 
}; 
TAJS_assert( ninja.yell(4), 'isMaybeStrIdentifierParts');
TAJS_assert( typeof(ninja.yell(4)) == "string" );
