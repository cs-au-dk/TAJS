function P(){this.prototypeProperty = 'x'};
function S(){};
S.prototype = new P;
var name, obj = new S(), x = 0;
for (name in obj) {
    x = 'a'; // the prototype of obj has properties
}
TAJS_assert(x, 'isMaybeSingleStr', true);