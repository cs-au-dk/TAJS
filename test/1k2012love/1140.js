var size = 128;
var sizeSquared = size * size;

//take care of canvas size - make it take up the entire page,
//prevent the scrollbar from appearing and set the background colour
c.width = size;
c.height = size;
c.innerHTML= '<style>*{width:100%;height:100%;margin:0;overflow:hidden;background:#311}</style>';

//handle mouse movement
var mouseX = 0;
var mouseY = 0;
onmousemove = function(e) {
  mouseX = e.clientX / innerWidth;
  mouseY = e.clientY / innerHeight;
};

var image = a.createImageData(size, size);
var data = image.data;

var time = 0;
var step = 1 / size;

//initialise base noise map
var noiseMap = [];
for(var k = 0; k < sizeSquared; ++k)
   noiseMap[k] = Math.random() * 1.5 - 0.5;

//the frame function
setInterval(function()
{
   time += step;
   
   //calculate pixels
   for(var x = 1; x >= 0; x -= step) {
      for(var y = 1; y >= 0; y -= step) {
         var index = (y * size + x) * size * 4;
         
         //distance from the mouse pointer
         var dx = x - mouseX;
         var dy = y - mouseY;
         var distance = Math.sqrt(dx * dx + dy * dy);
         
         //noise values
         var qx = f(x, y);
         var qy = f(x + 2, y + time / 3);

         var rx = f(x + distance * 1.3 + qx / 2 + 0.7, y + qy / 2 + 4);
         var ry = f(x + qx / 2 + 3 * time, y + qy / 2 + 2);

         var noise = f(x + rx / 3, y + ry / 2) * 0.75 + 0.15;
         var w = qx * ry / 2;
         var z = qy * rx / 2;
         
         //calculate colors based on the noise values and distance
         data[index + 0] = color(noise + w / 4);
         data[index + 1] = color(noise / 3 + z / 2 + w);
         data[index + 2] = color(w + z);
         data[index + 3] = color(1 - smoothStep(distance) * (z + w) * 5)
      }
   }
   
   //paint the calculated image, and then paint a red heart under it
   a.putImageData(image, 0, 0);
   a.globalCompositeOperation = 'destination-over';
   a.fillStyle = 'red';
   a.font = size + 'px sans';
   a.fillText('\u2665', size / 5, size * 0.75)
}, 30);

//just a clamp function
function color(a) {
   return 255 * Math.min(Math.max(a, 0), 1);
}

//smooth step ramp function
function smoothStep(x) {
   return (x > 0.2) ? 0 : i(1, 0, x * 5);
}

//interpolation function
//using a 5th order polynomial like this makes the resulting
//image smooth even when it's streched over the entire screen
//remember the original image is only 128 pixels wide (size variable)
function i(a, b, t) {
   t = t * t * t * (6 * t * t - 15 * t + 10);
   return a + (b - a) * t;
}

//noise function, uses base noise map in order to remain a pure function
function n(x, y) {
   var i = Math.abs(x * size + y) % sizeSquared;
   return noiseMap[i];
}

//fractal noise function, uses 2 octaces, has a lot of fixed parameters baked in
function f(x, y) {
   var p1 = p(x * 2.5, y * 2.5);
   var p2 = p(x * 5.0, y * 5.0);
   return p1 + p2 * 0.5;
}

//perlin noise function reduced for shortest code
function p(x, y) {
   var xf = Math.floor(x);
   var yf = Math.floor(y);
   
   return i(i(n(xf, yf), n(xf + 1, yf), x - xf), i(n(xf, yf + 1), n(xf + 1, yf + 1), x - xf), y - yf);
}