var x = 42;
y = 43;
this.z = 44;

function f() {
	this.q = 45;
	return this;
}
var global = f();

TAJS_dumpValue(global.x);
TAJS_dumpValue(global.y);
TAJS_dumpValue(global.z);
TAJS_dumpValue(global.q);
TAJS_dumpValue(global.f);
TAJS_dumpValue(global.global);
TAJS_dumpValue(global);
