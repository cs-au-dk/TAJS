function aaaAAA(o) {}
function aaaBBB(o) {}

function foo(o) {
    var z1 = "aaa" + o.t;
    //TAJS_dumpValue(this.aaaAAA);
    TAJS_dumpValue(this[z1])
}
function init() {
   foo(A[Math.random()])
   foo(A[Math.random()]);
}

var A = [{t: "AAA"},{t: "BBB"}]
init();
