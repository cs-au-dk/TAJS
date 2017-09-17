var w, h, x = t = 0, r = 200;
function f() {
  c.clearRect(0, 0, w, h);
  c.save();
  c.translate(w / 2, h / 2);
  c.rotate(t / Math.PI);
  for (i = 1; i < 20; i++) {
    x = Math.sin(i / 2) * 20;
    c.beginPath();
    if (i % 2 != 0) {
    c.fillStyle = "#000";
    c.arc(x, 0, r - (i * 10), 0, 2 * Math.PI);
    c.fill();
    }
    else {
    c.fillStyle = "#fff";
    c.arc(x, 0, r - (i * 10), 0, 2 * Math.PI);
    c.fill();
    }
  }
  c.restore();
  t += 0.5;
}
onload = function () {
  c.width = w = innerWidth;
  c.height = h = innerHeight;
  setInterval(f, 50);
};