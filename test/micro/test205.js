var x = Array.prototype.concat.apply();
dumpValue(this);
dumpValue(x);

function f() {return 42};
var x1 = Function.prototype.apply.apply(f);
dumpValue(x1);
var x2 = f.apply.apply(f);
dumpValue(x2);
var x3 = f.apply.apply.apply(f);
dumpValue(x3);
var x4 = f.call.call.call(f,117);
dumpValue(x4);

dumpValue(this);

function g() {return [this,arguments]};
var y1 = Function.prototype.apply.apply(g, ["foo", ["bar", "baz"]]);
dumpObject(y1);
dumpValue(y1[0].valueOf());
dumpObject(y1[1]);

dumpValue(this);

var y2 = Function.prototype.call.call(g, "foo", "bar", "baz");
dumpObject(y2);
dumpValue(y2[0].valueOf());
dumpObject(y2[1]);

dumpValue(this);

var y3 = Function.prototype.apply.apply(Function.prototype.apply, [g, ["foo", ["bar", "baz"]]]);
dumpObject(y3);
dumpValue(y3[0].valueOf());
dumpObject(y3[1]);

dumpValue(this);

var y4 = Function.prototype.apply.apply(Function.prototype.apply, [Function.prototype.apply, [g, ["foo", ["bar", "baz"]]]]);
dumpObject(y4);
dumpValue(y4[0].valueOf());
dumpObject(y4[1]);

dumpValue(this);

var call = Function.prototype.call;
var z1 = call.call(g, call);
dumpObject(z1);
var z2 = call.call(call, g);
dumpObject(z2);

dumpValue(this);

try {
  var z3 = call.call(call, call);
  dumpValue(z3);
} catch (ex) {
  dumpObject(ex);
}

dumpValue(this);

