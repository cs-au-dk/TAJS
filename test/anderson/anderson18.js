    // Type mismatch with method parameters
    
 function f() {  2 }

function h() { "two"}

    function A() {
	this.m = f; this;
    }

    function main(x) {
	x = new A();
	x.m = h;
    };

main(1);
