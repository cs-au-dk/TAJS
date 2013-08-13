var x = {a:42, b:7, c:1, d: 1234}

var z = 0;
var a;
for (a in x) {
  z += x[a];
  if (z == 42)
    delete x["c"]
}

dumpValue(z);
