// Full source available on GitHub: https://github.com/ThibWeb/js1k-2014
// Timer and smoothTimer;
var t = s = 0;
var positions = [];
// Current positions and points.
var X = Y = x = y = p = C = 0;
var w = a.width;
var h = a.height;

var M = Math;

for (e in c) c[e[0]+e[2]+(e[6]||'')] = c[e];

var circles = [];

// create radial gradient
var grd = c.ceR(0, 0, 0, 0, 0, w + h);
grd.addColorStop(0, '#1E80C2');
grd.addColorStop(1, 'transparent');

function rand(high) {
  return M.random() * (high - 40) + 20;
}

onmousemove = function (e) {
  x = e.pageX;
  y = e.pageY;
};

function loop() {
  with(c) {
    t++;
    s = t / 9;

    // Fill slicer.
    X += (x - X) / 20;
    Y += (y - Y) / 20;
    if (positions.length < 50) {
      positions.push([X, Y]);
    }

    // Draw background.
    globalAlpha = .7;

    fillStyle = grd;
    arc(0, 0, w + h, 0, 7);
    fl();

    // Remove elt from slicer.
    if (t % 2 == 0) {
      positions.shift();
    }

    // Draw slicer.
    lineWidth = 5;
    strokeStyle = 'hsl(' + rand(positions.length) + ',90%,50%)';
    for (var i= 0; i < positions.length - 1; i++) {
      bga();
      mv(positions[i][0], M.cos(-i + s) * 2 - (positions[i][1]-positions[i][1])/9 + positions[i][1] - 9);
      ln(positions[i+1][0], M.cos(-i + s) * 2 - (positions[i+1][1]-positions[i][1])/9 + positions[i+1][1] - 9);
      sr();
      coa();
    }

    // Pop circles.
    if (t % 50 == 0) {
      circles.push([rand(w), rand(h), rand(80), rand(200)]);
    }

    for (var i = 0; i < circles.length; i++) {
      C = circles[i];
      // Draw circles.
      strokeStyle = C[2] > 50 ? 'black' : 'hsl(' + C[3] + ',90%,50%)';
      bga();
      arc(M.cos(s) * 3 + C[0], M.sin(s) * 3 + C[1], C[2], 0, 7);
      coa();
      sr();

      // Check for circle collision.
      if (M.sqrt((X-C[0])*(X-C[0]) + (Y-C[1])*(Y-C[1])) < C[2]) {
        circles.splice(i, 1);
        p++;
        if (C[2] > 50) {
          p = 0;
          positions = [];
        }
      }
    }

    font = '30px arial';
    fillStyle = '#fff';
    flx(p, 20, 40);
  }
  window.requestAnimationFrame(loop);
}

loop();