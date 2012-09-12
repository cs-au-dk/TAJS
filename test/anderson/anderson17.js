    // Trying to define a method with different numbers of paremters

  function f() {  this.m = 2; }

    function h(x) { this.m = x; }

    function A() {
	this.m = f; this;
    }

    function main(x) {
	x = new A();
	x.m = h;
    };
    
main(1);
