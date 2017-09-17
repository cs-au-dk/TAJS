var glob = 42;
try {
    print ("Outter try");
    try {
	print ("First nested");
	if (Math.random()) 
	    throw 30;
	else 
	    var x = 10;
    } catch (ee) {
	glob = "42";
	TAJS_dumpValue(ee);
	throw "" + ee;
    }
    throw {ex: "yes"};
} catch (e) {
    TAJS_dumpValue(e);
    TAJS_dumpObject(e);
}

TAJS_dumpValue(glob);
