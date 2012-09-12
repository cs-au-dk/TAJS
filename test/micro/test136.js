var x = 42;

function f(n) {
//	dumpValue(this);
//	dumpValue(n);
	this.next = n;
}

function g() {
	x = new f(x);
}

g();
g();
dumpValue(x);
dumpValue(x.next); 
dumpValue(x.next.next); 
