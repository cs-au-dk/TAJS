    // Demonstrates how 'non-local' access to a member, this.m.b, affects both
    // B and C. This is because this.m is the upper bound of both receivers.


 function A() {
	this.m = new B();
	this.m = new C();
	this.m.b; 
    }
    
    function B() {
	this.b = 1; this;
    }

    function C() {
	this.c = 2; this;
    };

var x = new A();
