var a = {b:42, c:true}

function f(x) {
	
	dumpState();
	a.b = x;
	dumpState();
	
}

dumpState();
f(17);
dumpState();

dumpObject(a);
