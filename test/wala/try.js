
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

dumpValue(tryCatch(o, targetOne, targetTwo));
dumpValue(tryFinally(o, targetOne, targetTwo));
dumpValue(tryFinallyLoop(o, targetTwo));
dumpValue(tryCatchFinally(o, targetOne, targetTwo));
dumpValue(tryCatchFinally2(o, targetOne, targetTwo));


