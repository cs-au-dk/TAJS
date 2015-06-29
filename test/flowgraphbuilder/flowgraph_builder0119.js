var glob = 42;
try {
    try {
	if (Math.random()) 
	    throw 30;
    } catch (ee) {
	throw "" + ee;
    }
    throw {ex: "yes"};
} catch (e) {
}

TAJS_dumpValue(glob);
