/*
A full implementation of fluid simulation based on SPH (Smoothed-particle hydrodynamics) along with blob effect for the water effect.
Written by Asad Memon.

script assumes these vars available in shim
var b = document.body;
var c = document.getElementsByTagName('canvas')[0];
var a = c.getContext('2d');

*/

var ma= Math, e = ma.abs, g = ma.max, h = ma.min, i = j = mx = my = 0;
c.width = 600;
c.height = 400;
var k = [], l = 0, m = 0, p = [];
tc = c.cloneNode();
tx = tc.getContext("2d");
tc.width = 600;
tc.height = 400;
cycle = 0;
for(prop in a) {
  a[prop[0] + (prop[6] || "")] = a[prop]
}
for(prop in tx) {
  tx[prop[0] + (prop[6] || "")] = a[prop]
}
for(i = 0;2500 > i;i++) {
  p[i] = []
}
c.onmousemove = function(b) {
  b = {x:b.pageX, y:b.pageY, d:0, e:0, a:0, b:0, g:0, h:3, c:0, f:0, l:250};
  k[l++] = b
};
setInterval(function() {
  a.ce(0, 0, 600, 400);
  tx.ce(0, 0, 600, 400);
  m++;
  for(i = l;i--;) {
    var b = k[i];
    if(!(0 > b.l--)) {
      b.a += b.g;
      b.b += b.h + 0.7;
      b.x += b.a;
      b.y += b.b;
      5 > b.x && (b.a += 0.5 * (5 - b.x) - 0.5 * b.a);
      5 > b.y && (b.b += 0.5 * (5 - b.y) - 0.5 * b.b);
      600 < b.x && (b.a += 0.5 * (600 - b.x) - 0.5 * b.a);
      400 < b.y && (b.b += 0.5 * (400 - b.y) - 0.5 * b.b);
      var f = p[b.d + 50 * b.e];
      for(j = f.length;j--;) {
        var d = f[j];
        b != d && 5 > e(b.x - d.x) + e(b.y - d.y) && (d.c += 1 - (e(b.x - d.x) + e(b.y - d.y)) / 25 ^ 3, d.g -= (b.x - d.x) * (1 / (e(b.x - d.x) + e(b.y - d.y))) * 2.8 * ((1 - (e(b.x - d.x) + e(b.y - d.y)) / 25) * (b.f + d.f) / (b.c + d.c)), d.h -= (b.y - d.y) * (1 / (e(b.x - d.x) + e(b.y - d.y))) * 2.8 * ((1 - (e(b.x - d.x) + e(b.y - d.y)) / 25) * (b.f + d.f) / (b.c + d.c)))
      }
      b.f = b.c - 0.1;
      b.g = b.h = b.c = 0;
      b.d = 0.125 * b.x | 0;
      b.e = 0.125 * b.y | 0;
      b.d = g(b.d, 0);
      b.e = g(b.e, 0);
      b.d = h(b.d, 49);
      b.e = h(b.e, 49);
      f.push(b);
      tx.ba();
      f = tx.cR(b.x, b.y, 1, b.x, b.y, 35);
      f.addColorStop(0, "#87CEEB");
      
      f.addColorStop(1, "rgba(135,206,235,0)");
      tx.fillStyle = f;
      tx.arc(b.x, b.y, 35, 0, 6.28);
      tx.fill()
    }
  }
  for(i = 0;2500 > i;i++) {
    p[i].length = 0
  }
  b = tx.gg(0, 0, 600, 400);
  pix = b.data;
  i = 0;
  for(n = pix.length;i < n;i += 4) {
    pix[i + 3] < 160 && (pix[i + 3] = 0)
  }
  a.pg(b, 0, 0);
}, 50);