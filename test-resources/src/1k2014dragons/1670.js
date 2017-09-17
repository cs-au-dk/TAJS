/**
 * NOTES:
 * - remove 'var', dot zero float values, final semi colons, setInterval as string
 * - as using Crusher - some temp vars actually removed as it is better at removing duplicated text!
 *   - e.g. use "Math.sin()" not both with "var Sin=Math.sin()" - it is more efficiently replaced later
 * - try reusing variable names in function code before minify, e.g. x,y,t
 * http://closure-compiler.appspot.com/home
 * http://www.iteral.com/jscrush/
 */
var size = 32;

// generate radial gradient images with transparency
var img = document.createElement("canvas");
img.width = img.height = size << 1;
var ctx = img.getContext("2d");
var radgrad = ctx.createRadialGradient(size, size, size >> 1, size, size, size);  
radgrad.addColorStop(0, "rgba(128,64,32,1)");
radgrad.addColorStop(1, "rgba(128,64,32,0)");
ctx.fillStyle = radgrad;
ctx.fillRect(0, 0, size << 1, size << 1);

// adding another image adds v.little to the final crushed code as it is so similar to the above!
var img2 = document.createElement("canvas");
img2.width = img2.height = size << 1;
ctx = img2.getContext("2d");
radgrad = ctx.createRadialGradient(size, size, size >> 1, size, size, size);  
radgrad.addColorStop(0, "rgba(32,64,128,1)");
radgrad.addColorStop(1, "rgba(32,64,128,0)");
ctx.fillStyle = radgrad;
ctx.fillRect(0, 0, size << 1, size << 1);

// init vars, get dimensions of window
var width = window.innerWidth;
var height = window.innerHeight;

// scale the plasma source to the canvas width/height
// demos look better when they scale to pretty much any screen res - use virtual pixels from aspect ration
var vpx = width / size;
var vpy = height / (size * (width > height ? height/width : width/height));

// NOTE Closure compiler will inline small functions like these

// mix between two values based
function mixf(xy1, xy2, t)
{
   return {
      x: xy1.x * t + xy2.x * (1-t),
      y: xy1.y * t + xy2.y * (1-t)
   }
}

// parametric curve function
function f1(t)
{
   return {
      x: 13*Math.cos(t) - 6*Math.cos(11/6*t),
      y: 13*Math.sin(t) - 6*Math.sin(11/6*t)
   }
}

// parametric curve function
function f2(t)
{
   return {
      x: 6*Math.cos(11/6*t),
      y: 6*Math.sin(11/6*t)
   }
}

// plasma distance function
function dist(x,y,c,d)
{
   return Math.sqrt((x - c) * (x - c) + (y - d) * (y - d));
}

// animation loop - replace function() with quoted string after closure compile step
(f=function() {
   c.save();

   var time = Date.now() / 128;  // time function
   var mix = Math.sin(time*0.1); // sine wave mix function

   // clear bg and setup for compositing
   c.fillStyle = "#000";
   c.fillRect(0,0,width,height);
   c.globalCompositeOperation = "lighter";
   
   // plasma function - based on time and mix
   function plasma(x, y)
   {
      // mix between two plasma functions using a sine wave
      var f1 = ((Math.sin(dist(x + time, y, 128, 128) / 8)
              + Math.sin(dist(x - time, y, 64, 64) / 8)
              + Math.sin(dist(x, y + time / 8, 192, 64) / 8)
              + Math.sin(dist(x, y, 192, 64) / 8)) + 4) * 32;
      var f2 = (128 + (128 * Math.sin(x * 0.06)) +
                128 + (128 * Math.sin(y * 0.03)) +
                128 + (128 * Math.sin(dist(x + time, y - time, width, height) / 8)) +
                128 + (128 * Math.sin(Math.sqrt(x * x + y * y) / 8)) ) / 4;
      return mix * f1 + ((1 - mix) * f2);
   }
   
   // background 3D plasma
   for (var y=-2,x; y<=size+1; y++)    // extend to cover the edges of the screen
   {
      for (x=-4; x<=size+3; x++)       // extend further due to the additional sine sweeps from the "dx * mix"
      {
         // map plasma pixels to canvas pixels using the virtual pixel size calculated earlier
         var t = ~~plasma(x, y)/2;
         // composite, offset and resize the gradient images to generate the snazzy background plasma
         for (var d=4; d>1; d--)
         {
            var dx = (d << 4) - t + vpx;
            var dy = (d << 4) - t + vpy;
            c.drawImage(img,
                        x * vpx + t - d - 128 + (dx < 0 ? dx * mix : 0), y * vpy + t - d - 128 + (dy < 0 ? dy * -mix : 0),
                        Math.abs(dx), Math.abs(dy));   // NOTE! Math.abs() for FF!!! which is what the dx/dy is for bah.
                                                       // did use this before the dx/dy munging: (d << 4) - t + vpx, (d << 4) - t + vpy);
         }
      }
   }

   // foreground parametric curve morphing effect
   mix = Math.sin(Date.now()/2000);
   // center and rotate the curve
   c.translate(width/2, height/2);
   c.rotate(Date.now()/2000 % Math.PI*2 * mix);
   // morph between two parametric curves - very nice effect from a couple of simple curve functions
   for (var t=0; t<Math.PI*12; t+=Math.PI/48)
   {
      dx = mixf(f1(t), f2(t), mix);
      // use a gradient image again to generate a tasty glowly effect   
      c.drawImage(img2, dx.x * height/72, dx.y * height/72, vpx*0.8, vpx*0.8);
   }

   c.restore();
   
   requestAnimationFrame(f);
})();