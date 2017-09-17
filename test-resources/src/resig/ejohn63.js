var count = 0; 
for ( var i = 0; i < 4; i++ ) (function(i){ 
  setTimeout(function(){
    TAJS_assert(i, 'isMaybeNumUInt');
  }, i * 200); 
})(i);
