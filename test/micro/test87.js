var a = {b:42, c:true}

function f(x) {
	
	TAJS_dumpState();
	a.b = x;
	TAJS_dumpState();
	
}

TAJS_dumpState();
f(17);
TAJS_dumpState();

TAJS_dumpObject(a);
