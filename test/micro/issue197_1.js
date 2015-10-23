function f(obj) {
  for(var name in obj)
    return false;
  return true;
}

f({aaa: 3});
TAJS_dumpValue("OK");