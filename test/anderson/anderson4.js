    // Demonstrates assignment of a non-local member to an object

 function A() {
	this.m = new B();
	this.n = this.m.b;
	this;
    }
    
    function B() {
	this.b = 1; this;
    };


var a = new A();
