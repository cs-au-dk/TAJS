var ninja = { 
  yell: function(n){ 
    return n > 0 ? ninja.yell(n-1) + "a" : "hiy"; 
  } 
};
TAJS_assert( ninja.yell(4), 'isMaybeStrIdentifierParts');
 
var samurai = { yell: ninja.yell }; 
var ninja = null; 
 
try { 
  samurai.yell(4); 
} catch(e){ 
  TAJS_assert( true );
}