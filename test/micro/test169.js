if (Math.random()) {
    len = 22
    cols = 21
} else {
    len = 47
    cols = 3
}

function Cell() {
  return {}
}

o = {}
o.rows = new Array(len);
for (row = 0; row != len; ++ row) {
      o.rows[row] = new Array(cols);
      for (col = 0; col != cols; ++ col) {
        o.rows[row][col] = new Cell();
      }
}
dumpValue(o)
