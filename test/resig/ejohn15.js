var ninja = { 
  yell: function(n){ 
    return n > 0 ? arguments.callee(n-1) + "a" : "hiy"; 
  } 
};
TAJS_assert( ninja.yell(4), 'isMaybeStrPrefix');