    // Demonstrates how x can share the common members of B and C. The access
    // through x ensures that no definite member requirements are made on
    // members b and c in B and C respectively. See the next examples for what
    // happens when access occurs in a 'non-local' context.

    function A() {
	var x;
	var y;
	x = new B();
	x = new C();
	x.b = 2;
	x.c = 4;
    }

    function B() {
	this.b = 1; this;
    }

    function C() {
	this.c = 2; this;
    };
    
var xx = new A();
