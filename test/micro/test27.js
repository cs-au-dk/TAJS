var foo = {a:42, b:foo, c:this, d:Math};
dumpObject(foo);

dumpValue(this);

function Qwe() {
	this.bar = 42;
}

var x = new Qwe();
dumpValue(x.bar);

baz = {a:baz} // ReferenceError

