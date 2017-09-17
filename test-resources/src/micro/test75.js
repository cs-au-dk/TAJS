var standalone = false;
try {
  document;
} catch(error) {
  standalone = true;
} finally {
  foo = 42;
}

TAJS_assert(standalone);
TAJS_assert(foo === 42);
TAJS_dumpValue(standalone);
TAJS_dumpValue(foo);