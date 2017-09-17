    //Demonstrates a method requiring a member of its receiver that will not
    //exist.

   function A() {
	this.m = f;this;
    }
    
    function f() {
	this.n;
    }

    function main(x) {
	x = new A();
	x.m()
    };

main(2);
