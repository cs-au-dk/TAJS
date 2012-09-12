try {
    if (Math.random())
	throw "Exception!"
    else
	throw Object
} catch (e) {
    try {
	dumpValue(e)
	if (Math.random())
	    throw Number;
    } catch (e) {
	dumpValue(e)
	try {
	    if (Math.random()) 
		throw e
	    else 
		var xx = e;
	} catch (e) {
	    dumpValue(e);
	}
    }
}

dumpValue(xx);
