function recursive(d) {
  var other;
  if (d) {
    other = "FOO";
    recursive(!d);
    TAJS_dumpValue(other);
  }
}

recursive(true);
