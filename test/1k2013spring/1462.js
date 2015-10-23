/*
 * Carlos Reventlov <carlos@reventlov.com>
 *
 * Have you ever tried to catch fireflies?
 *
 * This is my entry for the http://js1k.com/2013-spring/ contest.
 *
 * It's just a peaceful anime-like live picture. I wanted to make something
 * beautiful, with nice graphics that would work on powerful and low-end
 * computers with Chrome, Firefox or Opera and all of that without turning
 * my code into a giant (or tiny) mess.
 *
 * I used three stacked layers, the first layer is for drawing the starry sky
 * background when the program stars, the second layer draws the lightning,
 * and the third just draws the firefly on top of its lightning.
 *
 * In order to cut down the code to 1Kb I had to manually compress it to near
 * 1.1Kb and then expanding it again, this time trying to repeat as much code as
 * I could and leaving some unused bytes for the compressor to use.
 *
 * I wrote a Go version (https://github.com/xiam/jscrush) of the Pass' jscrush
 * (http://www.iteral.com/jscrush/) to be able to use it from command line.
 *
 * Try to catch them! if you can :).
 *
 * */

// Real color of the sky.
b.style.background = '#108';

// Don't want borders.
b.style.margin = 0;

// Layers must be one on top of the other.
c.style.position = 'fixed';

// Function that returns a RGBA color representation.
G = function(a, b, c, d, e) {
  return 'rgba('+[a, b, c, d]+')'
};

// This function adds a layer to the body.
O = function(a, b, c, d, e) {
  // Adding clone.
  b.appendChild(c = a.cloneNode()),
  // Context.
  c.G = c.getContext('2d');
  return c;
};

// Fireflies.
X = [ ];

// Window width.
c.width = innerWidth;

// Window height.
c.height = innerHeight;

/*
 * We first draw the sky.
 * Then we draw the ground.
 * Finally, we draw the stars.
 * */
with (a = O(c, b).G) {

  // Adding layers.
  H = O(c, b);
  I = O(c, b);

// Drawing grass.
  // Notice we are also setting N to a quarter of the screen height.
  for (c = 0, i = N = innerHeight/4; i < 2*innerHeight; i += 7) {

    // Adding fireflies
    if (c < 42)
      X[c++] = {
        // X coordinate.
        x: Math.random()*innerWidth,
        // Y coordinate.
        y: N + Math.random()*(innerHeight - N),
        // Orientation.
        h: Math.random()*(R = (P = Math.PI)*2), // Also sets R and P.
        // Speed
        s: .5
      }

    i^N ?
// Draw stars until the last loop.
    fill(
      // Begin path.
      beginPath(
        // Set color.
        a.fillStyle = '#fff'
      ),
      arc(
        // 1px radio arc.
        Math.random()*innerWidth, Math.random()*N, 1, 0, R
      )
    )
    :
// Drawing the ground.
  // Fill rect.
    fillRect(
      // Just a portion of the screen.
      0, N, innerWidth, innerHeight,
      // Set color.
      a.fillStyle = '#050'
    );

// Grass with perspective.
    for (
    // Initial
      k = .3 * i/N,
      h = 0
    ;
    // Draw grass.

      fill(
        // Begin path.
        beginPath(
          // Grass color.
          a.fillStyle = '#020',
          a.strokeStyle = '#0f0'
        ),
        // Origin
        moveTo(
          h, l = i + (Math.random() - .5)*9
        ),
        d = 42,
        e = 3,
        bezierCurveTo(
          (d += 3)*(Math.random() - .5) + h, l - k*(1 + (Math.random() - .5)) * (e *= 3),
          (d += 3)*(Math.random() - .5) + h, l - k*(1 + (Math.random() - .5)) * (e *= 3),
          (d += 3)*(Math.random() - .5) + h, l - k*(1 + (Math.random() - .5)) * (e *= 3)
        ),
        stroke()
      ),
      h < innerWidth
    ;
    // Increment.
      h += (42+(Math.random() - .5))*k
    );
  }

  // Starting up!
  setInterval(
    function(a,b,c,d,e) {
      // Firefly layer.
      with (a = H.G) {

        // Clears canvas 1 and 2.
        H.width = innerWidth;
        I.width = innerWidth;

        // Drawing fireflies.
        for (c = 0; b = X[c++];) {


          // This is the firefly!
          fill(
            beginPath(
              a.fillStyle = '#ef0'
            ),
            arc(b.x, b.y, 1, 0, R)
          )

          // And its glare.
          with (a = I.G)
            e = createRadialGradient(b.x, b.y, 2, b.x, b.y, d = (72 + (Math.random() - .5))*
            (b.r = .3 + .3*(Math.random() - .5))
            ),
            e.addColorStop(0, G(0,0,0,.7)),
            e.addColorStop(.3, G(0,0,0,.2)),
            e.addColorStop(1, G(0,0,0,0)),
            fill(
              beginPath(
                a.fillStyle = e
              ),
              arc(
                b.x + (Math.random() - .5), b.y + (Math.random() - .5), d, 0, R
              )
            ),
            c^1 ? 0 :
              fill(
                beginPath(
                  a.fillStyle = G(0, 0, 20, .93),
                  a.globalCompositeOperation = 'xor'
                ),
                fillRect(0, 0, innerWidth, innerHeight)
              )

          b.s = (N < b.y) ? Math.max(b.s, (Math.random() - .5))*.9 : 4;

          // Fly!
          b.x += Math.cos(b.h)*b.s
          b.y += Math.sin(b.h)*b.s
          b.t += .4*(Math.random() - .5)
        }
      }
    }, 42,
    // Adding event listener
    b.onmousemove = function(a,b,c,d,e) {
      // If you try to catch 'em and they'll try to scape!
      for (c = 0; b = X[c++];)
        if ((d = b.x - a.clientX)*d + (e = b.y - a.clientY)*e < 1e4)
          b.h = -Math.random()*P, b.s = 9;
    }
  )
};