var global = this;
var x = Array.prototype.concat.apply([]);
TAJS_assert(this === global);
TAJS_assert(x.length === 0);

function f() {
  return 42
}
var x1 = Function.prototype.apply.apply(f);
TAJS_assert(x1 === 42);
var x2 = f.apply.apply(f);
TAJS_assert(x2 === 42);
var x3 = f.apply.apply.apply(f);
TAJS_assert(x3 === 42);
var x4 = f.call.call.call(f,117);
TAJS_assert(x4 === 42);

TAJS_assert(this === global);

function g() {
  return [this, arguments]
}
var y1 = Function.prototype.apply.apply(g, ["foo", ["bar", "baz"]]);
TAJS_assert(typeof y1 === 'object');
TAJS_assert(typeof y1[0] === 'object');
TAJS_assert(y1[0].length === 3 /* foo */);
TAJS_assert(typeof y1[1] === 'object');
TAJS_assert(y1[1].length === 2 /* [ ... , ...] */);

TAJS_assert(this === global);

var y2 = Function.prototype.call.call(g, "foo", "bar", "baz");
TAJS_assert(typeof y2 === 'object');
TAJS_assert(typeof y2[0] === 'object');
TAJS_assert(y2[0].length === 3 /* foo */);
TAJS_assert(typeof y2[1] === 'object');
TAJS_assert(y2[1].length === 2 /* [ ... , ...] */);

TAJS_assert(this === global);

var y3 = Function.prototype.apply.apply(Function.prototype.apply, [g, ["foo", ["bar", "baz"]]]);
TAJS_assert(typeof y3 === 'object');
TAJS_assert(typeof y3[0] === 'object');
TAJS_assert(y3[0].length === 3 /* foo */);
TAJS_assert(typeof y3[1] === 'object');
TAJS_assert(y3[1].length === 2 /* [ ... , ...] */);

TAJS_assert(this === global);

var y4 = Function.prototype.apply.apply(Function.prototype.apply, [Function.prototype.apply, [g, ["foo", ["bar", "baz"]]]]);
TAJS_assert(typeof y4 === 'object');
TAJS_assert(typeof y4[0] === 'object');
TAJS_assert(y4[0].length === 3 /* foo */);
TAJS_assert(typeof y4[1] === 'object');
TAJS_assert(y4[1].length === 2 /* [ ... , ...] */);

TAJS_assert(this === global);

var call = Function.prototype.call;
var z1 = call.call(g, call);
TAJS_assert(z1[0] === Function.prototype.call);
var z2 = call.call(call, g);
TAJS_assert(z2[0] === global);

TAJS_assert(this === global);

try {
  call.call(call, call);
  TAJS_assert(false);
} catch (ex) {
  TAJS_assert(this === global);
}

TAJS_assert(this === global);

