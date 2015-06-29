
function targetOne( x ) {
  return x;
}

function targetTwo( x ) {
  throw x;
}

function tryCatch( x, targetOne, targetTwo ) {
  try {
    if (x.one < 7)
      return targetOne( x );
    else
      targetTwo( x );
  } catch (e) {
    return e.two();
  }
}

function tryFinally( x, targetOne, targetTwo ) {
  try {
    if (x.one < 7)
      return targetOne( x );
    else
      targetTwo( x );
  } finally {
   return  x.two();
  }
  return x.three();
}

function tryFinallyLoop( x, targetTwo ) {
  while (x.one < 7) {
    try {
      if (x.one < 3)
        break;
      else
        targetTwo( x );
    } finally {
      return x.two();
    }
  }
}

function tryCatchFinally( x, targetOne, targetTwo ) {
  try {
    if (x.one < 7)
      return targetOne( x );
    else
      targetTwo( x );
  } catch (e) {
    e.two();
  } finally {
    return x.three();
  }
}

function tryCatchFinally2( x, targetOne, targetTwo ) {
  try {
    if (x.one < 7)
      return 4;
    else
      targetTwo( x );
  } catch (e) {
    e.two();
  } finally {
    return x.three();
  }
}

o = {
 one: -12,

 two: function two () {
   return this;
 },

 three: function three () {
   return 8;
 }
};

TAJS_dumpValue(tryCatch(o, targetOne, targetTwo));
TAJS_dumpValue(tryFinally(o, targetOne, targetTwo));
TAJS_dumpValue(tryFinallyLoop(o, targetTwo));
TAJS_dumpValue(tryCatchFinally(o, targetOne, targetTwo));
TAJS_dumpValue(tryCatchFinally2(o, targetOne, targetTwo));


