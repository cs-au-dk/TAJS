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
	dumpValue(ee);
	throw "" + ee;
    }
    throw {ex: "yes"};
} catch (e) {
    dumpValue(e);
    dumpObject(e);
}

dumpValue(glob);
