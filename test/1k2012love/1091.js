c.width = 700;
c.height = 400;
s = c.style;
s.position = 'absolute';
w = window;
k = w.innerWidth / 2 - 350;
t = w.innerHeight / 2 - 200;
s.left = k + 'px';
s.top =  t + 'px';
n = document;
d = n.createElement('div');
d.innerHTML = "Click: in Shift-Click: out<form id='f'><input size=40 id='j'/></form>";
s = d.style;
s.marginTop = '20px';
s.textAlign = 'center';
b.appendChild(d);
j = ge('j');
function ge(s)
{
  return n.getElementById(s);
}
cx = -0.75;
cy = to = 0;
m = 1.0;
function tx(x)
{
  return m * (x / 200 - 1.75) + cx;
}
function ty(y)
{
  return m * (y / 200 - 1) + cy;
}
function z()
{
  if (to)
  {
    w.clearTimeout(to);
  }
  j.value = cx + ',' + cy + ',' + m;
  s = 16;
  py = 0;
  rs();
  function rs()
  {
    for (px = 0; (px < 700); px+=s)
    {
      x = y = i = 0;
      while (x*x + y*y < 4 && i < 767)
      {
        xt = x*x - y*y + tx(px);
        y = 2*x*y + ty(py);
        x = xt;
        i++;
      }
      r = (i & 7) * 32;
      g = (i & 31) * 8;
      b = (i & 63) * 4;
      a.fillStyle = 'rgb(' + r + ',' + g + ',' + b + ')';
      a.fillRect(px, py, s, s);
    }
    py += s;
    if (py >= 400)
    {
      py = 0;
      s >>= 1;
    }
    to = s ? window.setTimeout(rs, 0) : 0;
  }
}
z();
c.onclick = function(e) {
  cx = tx(e.clientX - k);
  cy = ty(e.clientY - t);
  m *= (e.shiftKey ? 2 : 0.5);
  z();
};
ge('f').onsubmit = function(e) {
  p = j.value.split(',');
  if (p.length)
  {
    cx = p[0] - 0;
    cy = p[1] - 0;
    m = p[2] - 0;
  }
  z();
  return false;
};