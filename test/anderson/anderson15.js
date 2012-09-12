    // Trying to call a constructor function with a mismatched number of
    // parameters

    function A() {
	this.m = 2;
    }

    function main(x) {
	x = new A(2);
    };

main(1);
