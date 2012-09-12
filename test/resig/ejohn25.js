var object = {}; 
function fn(){ 
  return this; 
} 

assert( fn() === this );
dumpValue(fn());
dumpValue(this); 

assert( fn.call(object) === object );
dumpValue(fn.call(object));
dumpValue(object);
