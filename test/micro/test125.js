x = 0;
function spy() {return this;}
try {throw spy} catch(spy) {spy().x = 1; dumpValue(x === 1);}
dumpValue(x);
