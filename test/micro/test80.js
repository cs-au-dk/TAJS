
function ex() {
    x = 10;
    try {
	while (Math.random()) {
	    x = x + 1;
	    if (Math.random()) 
		throw "Exception!";
	}
    } catch (ee) {
	throw ee;
    }
    return 12345;
}

function f() {
    try {
	try {
	    var xx = ex();
	} catch (e) {
	    throw e;
	}
    } catch (ee) {
	throw ee;
    }
    return xx;
}

try {
    var ret = f() // If f throws an exception ret is undef. 
    dumpValue(ret); //At this point ret should *not* be undef.
} catch(e) {
    dumpValue(e);
}
dumpValue(ret) // ret might be undef here. 


