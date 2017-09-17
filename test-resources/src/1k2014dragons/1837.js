// further notes here maybe:
// http://codepen.io/raurir/pen/KoJLH/

var sw = a.width, 
sh = a.height, 
M = Math,
Mc = M.cos,
Ms = M.sin,
ran = M.random,
pfloat = 0, 
pi = M.PI, 
dragons = [], 
shape = [],


loop = function() {

  a.width = sw; // clear screen

  for ( j = 0; j < 7; j++) {
    if ( !dragons[j] ) dragons[j] = dragon(j); // create dragons initially
    dragons[j]();
  }

  pfloat++;

  requestAnimationFrame(loop);

},


dragon = function(index) {

  var scale = 0.1 + index * index / 49, 
    gx = ran() * sw / scale, 
    gy = sh / scale, 
    lim = 300, // this gets inlined, no good!
    speed = 3 + ran() * 5, 
    direction = pi, //0, //ran() * pi * 2, //ran(0,TAU), 
    direction1 = direction, 
    spine = [];

  return function() {

    // check if dragon flies off screen 
    if (gx < -lim || gx > sw / scale + lim || gy < -lim || gy > sh / scale + lim) {

      // flip them around

      var dx = sw / scale / 2 - gx,
        dy = sh / scale / 2 - gy;

      direction = direction1 = M.atan(dx/dy) + (dy < 0 ? pi : 0);

    } else {

       direction1 += ran() * .1 - .05;

       direction -= (direction - direction1) * .1;

    }

    // move the dragon forwards
    gx += Ms(direction) * speed;
    gy += Mc(direction) * speed;

    // calculate a spine - a chain of points
    // the first point in the array follows a floating position: gx,gy
    // the rest of the chain of points following each other in turn

    for (i=0; i < 70; i++) {

      if (i) {

        if (!pfloat) spine[i] = {x: gx, y: gy}

        var p = spine[i - 1],
          dx = spine[i].x - p.x,
          dy = spine[i].y - p.y,
          d = M.sqrt(dx * dx + dy * dy),
          perpendicular = M.atan(dy/dx) + pi / 2 + (dx < 0 ? pi : 0);

        // make each point chase the previous, but never get too close
        if (d > 4) {
          var mod = .5;
        } else if (d > 2){
          mod = (d - 2) / 4;
        } else {
          mod = 0;
        }

        spine[i].x -= dx * mod;
        spine[i].y -= dy * mod;
        // perpendicular is used to map the coordinates on to the spine
        spine[i].px = Mc(perpendicular);
        spine[i].py = Ms(perpendicular);

        if (i == 20) { // average point in the middle of the wings so the wings remain symmetrical
          var wingPerpendicular = perpendicular;
        }

      } else {

        // i is 0 - first point in spine
        spine[i] = {x: gx, y: gy, px: 0, py: 0}; 

      }

    }

    // map the dragon to the spine
    // the x co-ordinates of each point of the dragon shape are honoured
    // the y co-ordinates of each point of the dragon are mapped to the spine

    c.moveTo(spine[0].x,spine[0].y)

    for (i=0; i < 154; i+=2) { // shape.length * 2 - it's symmetrical, so draw up one side and back down the other

      if (i < 77 ) { // shape.length
        // draw the one half from nose to tail
        var index = i; // even index is x, odd (index + 1) is y of each coordinate
        var L = 1;
      } else {
        // draw the other half from tail back to nose
        index = 152 - i;  
        L = -1;
      }

      var x = shape[index];

      var spineNode = spine[shape[index+1]]; // get the equivalent spine position from the dragon shape

      if (index >= 56) {  // draw tail

        var wobbleIndex = 56 - index; // table wobbles more towards the end

        var wobble = Ms(wobbleIndex / 3 + pfloat * 0.1) * wobbleIndex * L;

        x = 20 - index / 4 + wobble;

        // override the node for the correct tail position
        spineNode = spine[ index * 2 - 83 ];

      } else if (index > 13) {  // draw "flappy wings"

        // 4 is hinge point
        x = 4 + (x-4) * (Ms(( -x / 2 + pfloat) / 25 * speed / 4) + 2) * 2; // feed x into sin to make wings "bend"

        // override the perpindicular lines for the wings
        spineNode.px = Mc(wingPerpendicular);
        spineNode.py = Ms(wingPerpendicular);

      }

      c.lineTo(
        (spineNode.x + x * L * spineNode.px) * scale, 
        (spineNode.y + x * L * spineNode.py) * scale
      );

    }

    c.fill();

  }

}

// the shape of the dragon, converted from a SVG image
'! ((&(&*$($,&.)/-.0,4%3"7$;(@/EAA<?:<9;;88573729/7,6(8&;'.split("").map(function(a,i) {
  shape[i] = a.charCodeAt(0) - 32;
});

loop();