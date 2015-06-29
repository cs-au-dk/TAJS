var x = Array.prototype.concat.apply();
TAJS_dumpValue(this);
TAJS_dumpValue(x);

function f() {return 42};
var x1 = Function.prototype.apply.apply(f);
TAJS_dumpValue(x1);
var x2 = f.apply.apply(f);
TAJS_dumpValue(x2);
var x3 = f.apply.apply.apply(f);
TAJS_dumpValue(x3);
var x4 = f.call.call.call(f,117);
TAJS_dumpValue(x4);

TAJS_dumpValue(this);

function g() {return [this,arguments]};
var y1 = Function.prototype.apply.apply(g, ["foo", ["bar", "baz"]]);
TAJS_dumpObject(y1);
TAJS_dumpValue(y1[0].valueOf());
TAJS_dumpObject(y1[1]);

TAJS_dumpValue(this);

var y2 = Function.prototype.call.call(g, "foo", "bar", "baz");
TAJS_dumpObject(y2);
TAJS_dumpValue(y2[0].valueOf());
TAJS_dumpObject(y2[1]);

TAJS_dumpValue(this);

var y3 = Function.prototype.apply.apply(Function.prototype.apply, [g, ["foo", ["bar", "baz"]]]);
TAJS_dumpObject(y3);
TAJS_dumpValue(y3[0].valueOf());
TAJS_dumpObject(y3[1]);

TAJS_dumpValue(this);

var y4 = Function.prototype.apply.apply(Function.prototype.apply, [Function.prototype.apply, [g, ["foo", ["bar", "baz"]]]]);
TAJS_dumpObject(y4);
TAJS_dumpValue(y4[0].valueOf());
TAJS_dumpObject(y4[1]);

TAJS_dumpValue(this);

var call = Function.prototype.call;
var z1 = call.call(g, call);
TAJS_dumpObject(z1);
var z2 = call.call(call, g);
TAJS_dumpObject(z2);

TAJS_dumpValue(this);

try {
  var z3 = call.call(call, call);
  TAJS_dumpValue(z3);
} catch (ex) {
  TAJS_dumpObject(ex);
}

TAJS_dumpValue(this);

