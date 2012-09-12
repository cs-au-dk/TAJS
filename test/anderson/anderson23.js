    // Demonstrating how type inference is sensative to flow with respect to
    // when a member is accessed as opposed to when it is created. This shows
    // that inference is flow sensative with respect to annotations.

 function f() {
	this.m1 = 1;this;
    }

    function main(x) {
	x = new f();
	x.m2;
	x.m2 = x;
    };

main(1);
