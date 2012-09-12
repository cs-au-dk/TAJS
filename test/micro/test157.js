function recursive(d) {
  var other;
  if (d) {
    other = "FOO";
    recursive(!d);
    dumpValue(other);
  }
}

recursive(true);
