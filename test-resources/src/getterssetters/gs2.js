var x = {
  f: 1,
  get f() { return 2; }
}
TAJS_dumpObject(x);
TAJS_dumpValue(x.f);
x.f = 123; // calls dummy setter, which does nothing
TAJS_dumpValue(x.f);

var x = {
  t: false,
  f: 1,
  set f(v) { this.t = v; }
}
TAJS_dumpObject(x);
x.f = 123;
TAJS_dumpValue(x.t);
TAJS_dumpValue(x.f); // calls dummy getter, which returns undefined

var x = {
  get f() { return 2; },
  f: 1
}
TAJS_dumpObject(x);
TAJS_dumpValue(x.f);
x.f = 123;
TAJS_dumpValue(x.f);

var x = {
  t: false,
  set f(v) { this.t = v; },
  f: 1 // overwriting setter/getter, *not* calling setter!
}
TAJS_dumpObject(x);
x.f = 123;
TAJS_dumpValue(x.t);
TAJS_dumpValue(x.f);

var x = {
  get f() { return 1; },
  get f() { return 2; }
}
TAJS_dumpObject(x);
TAJS_dumpValue(x.f);

var x = {
  q: false,
  set f(v) { this.q = 1; TAJS_dumpValue(v); },
  set f(v) { this.q = 2; TAJS_dumpValue(v); }
}
TAJS_dumpObject(x);
x.f = 123;
TAJS_dumpValue(x.q);
