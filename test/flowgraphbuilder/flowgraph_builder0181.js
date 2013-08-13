function tryFinallyLoop( x, targetTwo ) {
  while (x.one < 7) {
    try {
      if (x.one < 3)
        break;
    } finally {
      return x.two();
    }
  }
}
