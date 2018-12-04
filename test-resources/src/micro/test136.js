var x = 42;

function f(n) {
//	TAJS_dumpValue(this);
//	TAJS_dumpValue(n);
	this.next = n;
}

function g() {
	x = new f(x);
}

g();
g();
TAJS_dumpValue(x);
TAJS_dumpValue(x.next); 
TAJS_dumpValue(x.next.next); 
