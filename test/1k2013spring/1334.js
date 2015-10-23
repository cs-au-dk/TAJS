d = document;
n = b.addEventListener;
l = d.createElement;
h = b.appendChild;
t = 'equestAnimationFrame';
g = window;
q = g['r' + t] || g['mozR' + t] || function(fn) { setTimeout(fn, 16.66); };

b.style.textAlign = 'center';
c.style.border = 'thin solid black';

s = l.call(d, 'h1');
s.innerHTML = 'DIGI-SKETCH';
b.insertBefore(s, c);

s = l.call(d, 'div');
s.innerHTML = 'A=left S=right K=up L=down';
h.call(b, s);

m();

function m(w, kx, ky, v, px, py, s, bg, fg) {
  w = 600;
  c.width = c.height = w;
  b.focus();
  a.fillStyle = bg = '#969696';
  a.fillRect(0,0,w,w);
  v = 1;
  kx = 0;
  ky = 0;
  px = py = 300;

  s = l.call(d, 'button');
  s.innerHTML = 'Clear';
  s.onclick = function () { c.width = w; a.fillStyle = bg; a.fillRect(0,0,w,w); a.fillStyle = fg; };
  h.call(b, s);
  
  s = s.cloneNode();
  s.innerHTML = 'Save';
  s.onclick = function () { g.location = c.toDataURL(); };
  h.call(b, s);

  n.call(b, 'keydown', function (e, k) {
    k = e.keyCode;
    kx = k==65 || k==83 ? k : kx;
    ky = k==75 || k==76 ? k : ky;
  });
  n.call(b, 'keyup', function (e) {
    k = e.keyCode;
    kx = k==65 || k==83 ? -1 : kx;
    ky = k==75 || k==76 ? -1 : ky;
  });

  a.fillStyle = fg = '#1E1E1E';
  function update() {
    if (kx==65) px -= v;
    if (kx==83) px += v;
    if (ky==75) py -= v;
    if (ky==76) py += v;
    a.fillRect(px, py, 1, 1);
    q(update);
  }
  update();
}