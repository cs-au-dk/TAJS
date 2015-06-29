try {
    if (Math.random())
	throw "Exception!"
    else
	throw Object
} catch (e) {
    try {
	TAJS_dumpValue(e)
	if (Math.random())
	    throw Number;
    } catch (e) {
	TAJS_dumpValue(e)
	try {
	    if (Math.random()) 
		throw e
	    else 
		var xx = e;
	} catch (e) {
	    TAJS_dumpValue(e);
	}
    }
}

TAJS_dumpValue(xx);
