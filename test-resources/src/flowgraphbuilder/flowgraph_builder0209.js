switch (i.move) {case "u":b[D] = !c ? 0 : w(c) - a + "px";w(b[D]) <= -50 && L(i.x, i.y - 1);break;case "d":b[D] = !c ? 0 : w(c) + a + "px";w(b[D]) >= 50 && L(i.x, +i.y + 1);break;case "l":b[E] = !f ? 0 : w(f) - a + "px";w(b[E]) <= -50 && L(i.x - 1, i.y);break;case "r":b[E] = !f ? 0 : w(f) + a + "px";w(b[E]) >= 50 && L(+i.x + 1, i.y)}