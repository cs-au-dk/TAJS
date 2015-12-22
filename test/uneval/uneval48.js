function X() {
 	TAJS_dumpValue(this);
 	eval("TAJS_dumpValue(this)");
    setInterval("TAJS_dumpValue(this)", 10);
    TAJS_dumpValue(this);
}

new X();
