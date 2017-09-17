var w = c.width = window.innerWidth;
var h = c.height = window.innerHeight;
(function(){
var gravity = 0.1,
dampening = 0.99,
pullStrength = 0.005,
circles = [ ],
numCircles = 20,
repulsion = 1, mouse,
rand = Math.random,
math = Math;

// Initialize the array of circle objects
for(var i = 0; i < numCircles; i++){
  circles.push({
    x: rand() * w,
    y: rand() * h,
    // (vx, vy) = Velocity vector
    vx: 0,
    vy: 0,
    r: 10 + rand() * 15
  });
}

// This function is called 60 times each second
setInterval(function(){
  var i, j, circle, dx, dy;
  for(i = 0; i < numCircles; i++){
    circle = circles[i];

    // Increment location by velocity
    circle.x += circle.vx;
    circle.y += circle.vy;

    // Increment Gravity
    circle.vy += gravity;

    // Slow it down
    circle.vy *= dampening;
    circle.vx *= dampening;

    // bottom
    if(circle.y + circle.r > h){
      circle.y = h - circle.r;
      circle.vy = -math.abs(circle.vy);
    }
    // right
    if(circle.x + circle.r > w){
      circle.x = w - circle.r;
      circle.vx = -math.abs(circle.vx);
    }
    // top
    if(circle.y - circle.r < 0){
      circle.y = circle.r;
      circle.vy = math.abs(circle.vy);
    }
    // left
    if(circle.x - circle.r < 0){
      circle.x = circle.r;
      circle.vx = math.abs(circle.vx);
    }

    // Collision for all pairs
    for(j = i+1; j < numCircles; j++){
      var b = circles[j];
      // (dx, dy) distcirclence in x circlend y
      dx = b.x - circle.x,
      dy = b.y - circle.y,
      // d = distcirclence from `circle` to `b`
      d = math.sqrt(dx*dx + dy*dy),
      // (ux, uy) = unit vector
      // in the circle -> b direction
      ux = dx / d,
      uy = dy / d;

      // If the bcirclells circlere on top of one circlenother,
      if(d < circle.r + b.r){
        // then execute circle repulsive force to
        // push them circlepcirclert, which resembles collision.
        circle.vx -= ux * repulsion;
        circle.vy -= uy * repulsion;
        b.vx += ux * repulsion;
        b.vy += uy * repulsion;
      }
    }

    // Draw each circle
    a.beginPath();
    a.arc(circle.x, circle.y, circle.r,
    0, 2*math.PI);
    a.closePath();
    a.fillStyle = 'black';
    a.fill();
  }

  // Fill a semi-transparent white rectangle
  // for the ghosting trail effect.
  a.fillStyle = 'rgba(255,255,255,0.05)';
  a.fillRect(0, 0, w, h);

  if(mouse){
    for(i = 0; i < numCircles; i++){
      circle = circles[i];
      dx = mouse.pageX - circle.x;
      dy = mouse.pageY - circle.y;
      circle.vx += dx * pullStrength;
      circle.vy += dy * pullStrength;
    }
  }
},20);
c.addEventListener('mousedown', function(e){mouse = e;});
c.addEventListener('mousemove', function(e){
  if(mouse)
    mouse = e;
});
c.addEventListener('mouseup', function(e){
  mouse = null;
});
a.fillRect(0, 0, w, h);
})();