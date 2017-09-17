var x = {
  foo: function(s) {
    TAJS_assert(s === "str");
  },
  bar: function(b) {
    TAJS_assert(b === false);
  }
 }
x.__proto__.baz = function(c) {
  TAJS_assert(false);
};

var y = {};

var h = function(p) {
  var t = x[p];
  var f = function(arg) {
    t.apply(this, [arg]);  // suspicious: t is a mix of user-defined and native functions!
  }
  y[p] = f;
}

h("foo");
h("bar");

y.foo("str");
y.bar(false);
