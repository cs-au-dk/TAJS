var standalone = false;
try {
  document;
} catch(error) {
  standalone = true;
} finally {
  foo = 42;
}

assert(standalone);
assert(foo === 42);
dumpValue(standalone);
dumpValue(foo);