var ninja = { 
  yell: function yell(n){ 
    return n > 0 ? yell(n-1) + "a" : "hiy"; 
   } 
}; 
assert( ninja.yell(4) == "hiyaaaa" ); 
		 
var samurai = { yell: ninja.yell }; 
var ninja = {}; 
assert( samurai.yell(4) == "hiyaaaa" );
