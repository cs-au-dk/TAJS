// It started as this then became so much more
// Attribution to http://mathworld.wolfram.com/HeartCurve.html
M=Math;
cos=M.cos;
function heartX(t) {
  return 16 * M.pow(M.sin(t), 3);
}

function heartY(t) {
  return 13 * cos(t) - 5 * cos(2*t) - 2 * cos(3*t) - cos(4*t);
}

a.beginPath();
for (i = 0, cap = 1e3, t = 0; i < cap; i++) {
  t = i ? M.PI * 2 * i/cap : 0;
  a.lineTo(heartX(t) + 50, -heartY(t) + 50);
  a.stroke();
}
a.fill();
a.closePath();