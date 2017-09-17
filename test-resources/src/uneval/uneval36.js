function f(x, y) {
   return eval(x + " = " + y);
}

TAJS_dumpValue(f("z", "3"));
TAJS_dumpValue(f("y", "4"));
TAJS_dumpValue(f("y", "5"));

