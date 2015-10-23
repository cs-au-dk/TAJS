var v = c.height = c.width = 256, d = a.getImageData(0, 0, v, v), p = new Uint32Array(d.data.buffer), k = [], s = v, t = 288, u = 0.1, m = "333333333333300022222222222221036666666666666547622222262222110762222222222110076222222622211007511555551511040440040004444044447333333400333337622622250322226662262221032222266226222503222266622622250322222666666665445566661111111000001111", e = Math, f = e.sin, g = e.cos, h = e.tan, n = e.PI / 180, o = 4278190080, q = 64, w = 16;
onkeydown = function(z) {
  k[z.keyCode] = 1
};
onkeyup = function(z) {
  k[z.keyCode] = 0
};
setInterval(function() {
  var z, b, j, l, A, r, y, x, B, C;
  u += k[37] ? 358 <= u ? -358 : 2 : k[39] ? 0 >= u ? 358 : -2 : 0;
  s += k[38] ? g(u * n) : 0;
  t += k[38] ? -f(u * n) : 0;
  for(z = v;z--;) {
    l = u + e.atan((128 - z) / 221) / n;
    l += 0 > l ? 360 : 360 <= l ? -360 : 0;
    0 < l && 180 > l ? (j = t & 65472, b = s - 1 / h(l * n) * (j - t), A = 1 / h(l * n) * q, r = -q, x = (j >> 6) - 1) : (j = (t & 65472) + q, b = s - 1 / h(l * n) * (j - t), A = 1 / h(l * n) * -q, r = q, x = j >> 6);
    for(y = b >> 6;0 < y && y < w && ~m[x * w + y] & 4;) {
      b += A, j += r, y = b >> 6, x += r >> 6
    }
    B = (b - s) * g(u * n) - (j - t) * f(u * n);
    C = 0 < r ? 63 - b : b;
    90 > l || 270 < l ? (b = (s & 65472) + q, j = t - (b - s) * h(l * n), A = q, r = -q * h(l * n), y = b >> 6) : (b = s & 65472, j = t - (b - s) * h(l * n), A = -q, r = q * h(l * n), y = (b >> 6) - 1);
    for(x = j >> 6;0 < x && x < w && ~m[x * w + y] & 4;) {
      b += A, j += r, y += A >> 6, x = j >> 6
    }
    b = (b - s) * g(u * n) - (j - t) * f(u * n);
    j = b < B ? 0 < A ? j : 63 - j : C;
    y = 14144 / (b < B ? b : B);
    x = 128 - (y >> 1);
    for(r = v - x;r--;) {
      r < x ? (p[r * v + z] = o, p[(255 - r) * v + z] = 6316128 | o) : p[r * v + z] = (m[(32 / y * (r - x) & 15) * w + (j / 2 & 15)] & 3) * (4210752 ^ (b < B || q)) | o
    }
  }
  a.putImageData(d, 0, 0)
}, w);