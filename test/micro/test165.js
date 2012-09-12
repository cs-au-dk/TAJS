function aaaAAA(o) {}
function aaaBBB(o) {}

function foo(o) {
    var z1 = "aaa" + o.t;
    //dumpValue(this.aaaAAA);
    dumpValue(this[z1])
}
function init() {
   foo(A[Math.random()])
   foo(A[Math.random()]);
}

var A = [{t: "AAA"},{t: "BBB"}]
init();
