function X() {
 	dumpValue(this);
 	eval("dumpValue(this)");
    setInterval("dumpValue(this)", 10);
    dumpValue(this);
}

new X();
