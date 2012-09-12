
function BigInteger() {
    this.array = []; 
  if (Math.random())
    this.dAddOffset(); 
}

function bnpDAddOffset(w) {
  this.array[w] += 0;
}

BigInteger.prototype.dAddOffset = bnpDAddOffset;
new BigInteger();



