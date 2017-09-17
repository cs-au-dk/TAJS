var standalone = false;
try {
  document; // ReferenceError
} catch(error) {
  standalone = true;
}
TAJS_assert(standalone);
TAJS_dumpValue(standalone);

