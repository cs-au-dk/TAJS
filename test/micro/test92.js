function tak(x,y) {
 if (x < 1) return y;    
 else return tak(tak(x-1,2),tak(x-1,1));
}

TAJS_dumpValue(tak(1,2));
