var a,b,c,t,i;

function init() {
  a = {
    d:42,
    e: function() { return 0 },
    f: {
      g: -87,
      h: function() { return 1 }
    }
  }
  i = function (){ return true }
  if (Math.random()) {
    b = a
    c = i
  } else if (Math.random()) {
    b = a.f
    c = null
  } else {
    b = null
    c = a
  }
}

init()
TAJS_dumpValue(b)
TAJS_dumpValue(c)

t = b.q
TAJS_dumpValue(b) // should be non-null
TAJS_assert(b, 'isMaybeNull || isMaybeUndef', false)

init()
t = b.f.g
TAJS_dumpValue(b) // should be an object whose 'f' field is non-null
TAJS_dumpValue(b.f) // should be non-null (because b must be a singleton)
TAJS_assert(b, 'isMaybeNull || isMaybeUndef', false)
TAJS_assert(b.f, 'isMaybeNull || isMaybeUndef', false)
TAJS_assertEquals(a, b)

init()
t = c() // (same with constructor calls)
TAJS_dumpValue(c) // should be a function
TAJS_assert(c, 'isMaybeNull || isMaybeUndef', false)
TAJS_assert(typeof c === "function")
TAJS_assertEquals(i, c)

init()
t = b.e()
TAJS_dumpValue(b) // should be an object with an 'e' method
TAJS_dumpValue(b.e) // should be a function
TAJS_assert(b, 'isMaybeNull || isMaybeUndef', false)
TAJS_assert(b.e, 'isMaybeNull || isMaybeUndef', false)
TAJS_assert(typeof b.e === "function")
TAJS_assertEquals(a, b)

init()
t = b.f.h()
TAJS_dumpValue(b) // should be an object whose 'f' field is an object with an 'h' method
TAJS_dumpValue(b.f) // should be non-null
TAJS_dumpValue(b.f.h) // should be a function
TAJS_assert(b, 'isMaybeNull || isMaybeUndef', false)
TAJS_assert(b.f, 'isMaybeNull || isMaybeUndef', false)
TAJS_assert(b.f.h, 'isMaybeNull || isMaybeUndef', false)
TAJS_assert(typeof b.f.h === "function")
TAJS_assertEquals(a, b)

init()
if (!!(typeof c == "function")) {
  TAJS_dumpValue(c)
  TAJS_assert(typeof c === "function")
  TAJS_assertEquals(i, c)
} else {
  TAJS_dumpValue(c)
  TAJS_assert(typeof c !== "function")
}


var a = Math.random();
if (!a) {
  TAJS_dumpValue(a)
} else {
  TAJS_dumpValue(a)
}

var p = {
  get f() {
    return 42;
  }
}

if (typeof p.f == "number") {
  TAJS_dumpValue(p.f)
} else {
  TAJS_dumpValue(p.f)
}
with (p) {
  if (typeof f == "number") {
    TAJS_dumpValue(f)
  } else {
    TAJS_dumpValue(f)
  }
}

init()
TAJS_dumpValue(b.d)
var qqq = 'd'
if (qqq in b)
  TAJS_dumpValue(b.d)
else
  TAJS_dumpValue(b.d)


a = Math.random() ? {p:42} : {q:"foo"}
b = a
if (a.p) {
  TAJS_dumpValue(a)
  TAJS_dumpValue(b)
}
TAJS_dumpValue(a)
TAJS_dumpValue(b)


/*

this.a.b
this.a()
this.a.b()
x.a = c
x.a.b = c
t = x[y]
t = x[y][z]
f().b = 1
...

x.f = (x = null)
x.f = (y[z] = null) // x and y may be aliases, and z may coerce to "f"

if (x.f <= 10) // refine x

*/