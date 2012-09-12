 function A() {
	this.m = f;this;
    }
    
    function f() {
	this.n = 10;
    }

    function main(x) {
	x = new A();
    };
    

main(3);
