var x = 42;
y = 43;
this.z = 44;

function f() {
	this.q = 45;
	return this;
}
var global = f();

dumpValue(global.x);
dumpValue(global.y);
dumpValue(global.z);
dumpValue(global.q);
dumpValue(global.f);
dumpValue(global.global);
dumpValue(global);
