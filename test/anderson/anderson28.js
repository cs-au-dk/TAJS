
  function A() {
	this.m = f;
      this.r = "hello";
	this;
    }
    
    function f() {
	this.n = 10;
    }

    function B() {
	this.m = f;this;
    }

    function main() {
	var x;
	var y;
	x = new A();
	x.m();
	y = new B();
	y.m();
    };

main()
