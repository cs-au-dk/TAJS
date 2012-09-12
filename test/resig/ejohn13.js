var ninja = { 
  yell: function(n){ 
    return n > 0 ? ninja.yell(n-1) + "a" : "hiy"; 
  } 
}; 
assert( ninja.yell(4) == "hiyaaaa" ); 
 
var samurai = { yell: ninja.yell }; 
var ninja = null; 
 
try { 
  samurai.yell(4); 
} catch(e){ 
  assert( false );  // should fail!
}