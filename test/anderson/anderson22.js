    // Addition of a member outside of the constructor through a local
    // variable
 function f() {
	this.m1 = 1;this;
    }

    function main(x) {
	x = new f();
	x.m2 = x;
	x.m2;
    };
main(1);
