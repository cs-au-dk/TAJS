
function getOp(x, op) {
  return x[ "operator" + op ]; // does this return a reference to a function or just the function? (TAJS: just a function)
}

function plusNum(y) {
  return this.val + y;
}

var obj = { val: 7, operatorPlus: plusNum };

var result =  ( getOp(obj, "Plus") )( 6 ); // Rhino gives NaN!
dumpValue(result);
