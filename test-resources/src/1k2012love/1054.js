// borderless window client area
var docstyle=b.style;
docstyle.margin = "0px";
docstyle.overflow = "hidden";

// init vars, get dimensions of window and resize the canvas to fit
var win = window,
    width = win.innerWidth,
    height = win.innerHeight,
    M = Math,
    Sin = M.sin,
    Cos = M.cos,
    paletteoffset = 0,
    palette = [];

c.width=width;
c.height=height;

for (var i=0; i<256; i++)
{
   palette[i] = "rgb(255," + ~~(128-(i*.5)) + "," + (255-i) + ")";
   palette[i+256] = "rgb(255," + (255-i) + ",255)";
}

var dist = function dist(a, b, c, d)
{
   return M.sqrt((a - c) * (a - c) + (b - d) * (b - d));
}

// animation frame rendering function
a.globalAlpha = 0.2;
setInterval(function()
{
   var pw = 80, ph = pw * (height/width),             // plasma source width and height
       vpx = width/pw, vpy = height/ph;               // scale the plasma source to the canvas width/height
   
   var time = Date.now() / 64;  // time function
   var mix = Sin(time*0.05);
   
   var colour = function colour(x, y)
   {
      // mix between two plasma functions using a sine wave
      var f1 = ((Sin(dist(x + time, y, 128.0, 128.0) / 8.0)
              + Sin(dist(x - time, y, 64.0, 64.0) / 8.0)
              + Sin(dist(x, y + time / 7, 192.0, 64) / 7.0)
              + Sin(dist(x, y, 192.0, 100.0) / 8.0)) + 4) * 32;
      var f2 = (128 + (128 * Sin(x * 0.0625)) +
              128 + (128 * Sin(y * 0.03125)) +
              128 + (128 * Sin(dist(x + time, y - time, width, height) * 0.125)) +
              128 + (128 * Sin(M.sqrt(x * x + y * y) * 0.125)) ) * 0.25;
      return (mix * f1) + ((1 - mix) * f2);
   }
   
   paletteoffset++;                                         // cycle speed
   var jitter = -vpx/8 + M.random()*vpx/4;                    // Jitter = 4
   for (var y=0,x; y<ph; y++)
   {
      for (x=0; x<pw; x++)
      {
         // map plasma pixels to canvas pixels using the virtual pixel size
         a.fillStyle = palette[~~(colour(x, y) + paletteoffset) % 512];
         a.fillRect(x * vpx + jitter, y * vpy + jitter, vpx, vpy);
      }
   }
   
   for (var x,y,p,z=M.abs(mix*(width/64)),t=0; t<100; t+=0.25)
   {
      x = 16*M.pow(Sin(t),3);
      y = 13*Cos(t) - 5*Cos(2*t) - 2*Cos(3*t) - Cos(4 *t);
      a.fillStyle = palette[512-(~~(colour(x, y) + paletteoffset) % 512)];
      a.fillRect(x*z + width/2, -y*z + height/2, 8, 8);
   }
   
}, 25);