function f(x, y) {
   return eval(x + " = " + y);
}

dumpValue(f("z", "3"));
dumpValue(f("y", "4"));
dumpValue(f("y", "5"));

