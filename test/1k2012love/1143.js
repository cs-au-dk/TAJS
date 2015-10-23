// Also available at https://github.com/bjornarg/js1k-love
var width = c.width = window.innerWidth;
var height = c.height = window.innerHeight;
var mX = width/2;
var mY = height*.4;
var mZ = 500;
var rY = 0;
var heart_pts = [];
var max_points_out = 250;
var points_out = [];
M = Math;
C = M.cos;
S = M.sin;
P = M.PI;
function heart_curve(t) {
  // Creates a point along the heart curve
  return {
    x: width/6 * M.pow(S(t), 3),
    y: height/-60 * (15*C(t) - 5 * C(2*t) - 2 * C(3*t) - C(4 * t)),
    z: 0
  };
}
function random(max) {
  // Randoms from 0 to max-1
  return M.floor(M.random()*max);
}
function copy(p) {
  // Copies an object
  var newObj = {};
  for (i in p) {
    newObj[i] = p[i];
  }
  return newObj;
}
function rotate(p) {
  // Simplified point rotation (since we're only rotating one axis)
  var x = p.x;
  p.x=C(rY)*x + S(rY)*p.z;
  p.z=-S(rY)*x + C(rY)*p.z;
  return p;
}
function point(p) {
  // Rotate and transform a 3D point to 2D
  var pt = copy(p);
  rotate(pt);
  pt.x = mX+pt.x*mZ/(pt.z+mZ);
  pt.y = mY+pt.y*mZ/(pt.z+mZ);
  return pt;
}
function move_point(p) {
  // Moves a point along the y-axis and checks if it's reached/passed it's 
  // destination. Returns 0 (false) if passed, 1 (true) if not.
  p.y += 2;
  if (p.y > p.to.y) return 0;
  return 1;
}
function draw(p) {
  // Draws a point.
  a.fillStyle=p.c;
  a.beginPath();
  a.arc(p.x, p.y, 4, 0, P*2, 1);
  a.closePath();
  a.fill();
}
function create_point(from, to) {
  // Creates a point with a random color, a starting position and a target
  // point.
  return {
    x: from.x,
    y: from.y,
    z: from.z,
    to: to,
    c: "hsl("+random(361)+","+random(101)+"%,"+random(50)+"%)"
  };
}
for (var t = -P/2; t < P/2; t+=P*.01) {
  // We remove all points between -0.25 and 0.25, because these points lie in
  // the small "dip" in the heart, and would generate a lot of points on
  // almost the same x-position.
  if (t < -.25 || t > .25)
    heart_pts.push({t: heart_curve(t), b: heart_curve(t+P)});
}
setInterval(function () {
  // Partly clear the screen. Using 0.15 alpha will make the points leave a
  // "trail"
  a.fillStyle = "rgba(0,0,0,0.15)";
  a.fillRect(0, 0, width, height);
  if (points_out.length < max_points_out) {
    // Generate two new randomly positioned points. This could probably be
    // optimized.
    L = random(heart_pts.length);
    points_out.push(create_point(heart_pts[L].t, heart_pts[L].b));
    L = random(heart_pts.length);
    points_out.push(create_point(heart_pts[L].t, heart_pts[L].b));
  }
  for (var i = 0; i < points_out.length; i++) {
    var pt = points_out[i];
    if (!move_point(pt)) {
      // Remove points that have passed their destination.
      points_out.splice(i, 1);
    } else {
      draw(point(pt));
    }
  }
  // Make the heart rotate!
  rY += .02;
}, 30);