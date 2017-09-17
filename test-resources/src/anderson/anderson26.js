    //Method defined and called. This shows how calling a method puts
    //constraints on the receiver. In this case it adds a member n to A.                
    function A() {
	this.m = f;this;
    }
    
    function f() {
	this.n = 10;
    }

    function main(x) {
	x = new A();
	x.m()
    };

main(1);
