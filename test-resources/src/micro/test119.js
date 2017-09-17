function empty() {}

function recursive(disks) {
  var other;
  if (disks == 1) {
    empty();
  } else {
    other = "FOO";
    recursive(disks - 1);
    TAJS_dumpValue(other);
  }
}

recursive(13);
