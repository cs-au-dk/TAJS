var x = {a:42, b:7, c:1, d: 1234}

var z = 0;
for (var a in x) {
  z += x[a];
  if (z == 123456)
    break;
  else if (z == 49)
    continue;
  if (z == 42)
    delete x["c"]
}

dumpValue(z);
