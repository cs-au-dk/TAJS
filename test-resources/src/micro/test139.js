function doPermute(n) {
	var k = n - 1;
    while (k >= 1) {
      doPermute(n - 1);
      k--;
  }
}

doPermute(2);
