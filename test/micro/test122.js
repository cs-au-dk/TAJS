var x = {a:42, b:7, c:1, d: 1234}

var z = 0;
var a;
for (x.e in x) {
  z += 1;
}

dumpValue (z)
