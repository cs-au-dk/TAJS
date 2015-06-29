var foo = {a:42, b:foo, c:this, d:Math};
TAJS_dumpObject(foo);

TAJS_dumpValue(this);

function Qwe() {
	this.bar = 42;
}

var x = new Qwe();
TAJS_dumpValue(x.bar);

baz = {a:baz} // ReferenceError

