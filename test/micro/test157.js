function recursive(d) {
  var other = true;
  if (d) {
    other = "FOO";
    recursive(!d);
    TAJS_dumpValue(other);
  }
}

recursive(true);
