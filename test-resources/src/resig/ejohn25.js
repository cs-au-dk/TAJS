var object = {}; 
function fn(){ 
  return this; 
} 

TAJS_assert( fn() === this );
TAJS_dumpValue(fn());
TAJS_dumpValue(this); 

TAJS_assert( fn.call(object) === object );
TAJS_dumpValue(fn.call(object));
TAJS_dumpValue(object);
