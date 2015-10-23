// Maximize canvas. This line is borrowed from "Particle carriage" by @p01, see http://js1k.com/2011-dysentery/demo/993
c.innerHTML = '<style>*{width:100%;height:100%;margin:0;overflow:hidden}</style>';
// Create shortcuts for some static functions.
A = Math.abs, R = Math.random;
// Initialize speed. How fast do the hearts travel to the camera.
s = .06;
// The canvas has a fixed height of 512 pixels, stretched to fill the window height.
h = 256;
// The width of the canvas is calculated once when the page loads, hopefully nobody resizes the window to much.
W = innerWidth, H = innerHeight;
w = h * W / H;
// Set pixel dimensions of the canvas.
c.width = w * 2, c.height = h * 2;
// Initialize the array of heart objects.
o = [];
// Initialize the array of messages, curent camera position and current points.
m = 'MISS0HIT0NICE0CATCHY0GOOD0LOVELY0GREAT0HEARTY0AWESOME0SUPER0BRILLIANT0PERFECT'.split(L = M = p = 0);
// Initialize relative mouse position on the canvas, ranging from 0 to 1.
J = K = .5;
onmousemove = function(e)
{
  // Store mouse position relative to the window, ranging from 0 to 1.
  J = e.clientX / W, K = e.clientY / H
};
// Helper function to translate all 3D coordinates to 2D.
function T(x, y, z)
{
  // Instead of moving all objects around we are moving the camera only.
  return { x: (x * 99 + L) / z + w, y: (y * 99 + M) / z + h }
}
with (a)
{
  strokeStyle = '#F41',
  lineJoin = 'round',
  // For whatever reason Firefox does not default to "sans-serif" but to "serif".
  font = 'bold 7em serif',
  textAlign = 'center',
  textBaseline = 'middle';
  // Replacing the function with a string does not work because of the with.
  setInterval(function()
  {
    // Number of hearts.
    i = 30;
    // The camera position always changes depending on the mouse position.
    L -= J * 60 - i, M -= K * 60 - i;
    // If the array of heart objects is empty, increase speed and initialize array.
    if (!o[0])
      for (s /= .8; i--; )
        o.push({ x: R() * 18 - 9, y: R() * 18 - 9, z: i / 3 });
    // Clear the canvas with a transparent color, this creates the motion blur effect.
    fillStyle = 'rgba(99,9,9,.2)',
    fillRect(0, 0, w * 2, h * 2);
    for (i in o)
    {
      i = o[i], z = i.z += .003;
      // Translate all 3D coordinates to 2D.
      e = T(i.x + 1, i.y - 1, z),
      f = T(i.x    , i.y + 2, z),
      g = T(i.x - 1, i.y - 1, z);
      // Hearts in the background are transparent.
      globalAlpha = 1 - z / 11,
      // beginPath is required in Opera, else the performance is very bad.
      beginPath(lineWidth = (j = 150 / z) / 2),
      moveTo(f.x, f.y),
      arc(g.x, g.y, j, 2.4, 5),
      // Abusing some empty function calls, executed from the inner to the outer bracket.
      stroke(fill(closePath(arc(e.x, e.y, j, 4, .8))))
    }
    // Required for fillText and fillRect.
    globalAlpha = 1;
    i = o[0];
    // Move the current heart and check if it hit the camera.
    if ((i.z -= s) < 0)
      fillStyle = '#FFF',
      // Increase the points, if there are enough messages, and display the message.
      fillText((m[p = A(i.x * 99 + L) > 150 || A(i.y * 99 + M) > 150 ? 0 : p + 1] || m[--p]) + '!', w, h),
      // Delete the current heart object.
      o = o.slice(1)
  }, 33)
}