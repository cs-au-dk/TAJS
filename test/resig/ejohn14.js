var ninja = { 
  yell: function yell(n){ 
    return n > 0 ? yell(n-1) + "a" : "hiy"; 
   } 
};
TAJS_assert( ninja.yell(4), 'isMaybeStrIdentifierParts');
		 
var samurai = { yell: ninja.yell }; 
var ninja = {};
TAJS_assert( samurai.yell(4), 'isMaybeStrIdentifierParts');
