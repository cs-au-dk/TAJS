// borderless window client area
b.style.margin = 0;
b.style.overflow = "hidden";

// init vars, get dimensions of window and resize the canvas to fit
var win = window,
    width = win.innerWidth,
    height = win.innerHeight,
    PI = Math.PI,
    spin = 0;

// canvas resize
c.width = width;
c.height = height;

var hthird = height / 3,
    wthird = width / 3;

// sky
var sg = a.createLinearGradient(0,0,0,hthird*2);
sg.addColorStop(0,"#007eff");
sg.addColorStop(1,"#00d2ff");
a.fillStyle = sg;
a.fillRect(0,0,width,height);

// sun
var g = a.createRadialGradient(wthird/2,hthird/2,0,wthird/2,hthird/2,wthird/5);
g.addColorStop(0,"#fcff00");
g.addColorStop(0.8,"#ffde00");
g.addColorStop(0.81,"rgba(255,222,0,0.5)");
g.addColorStop(1,"rgba(255,222,0,0)");
a.fillStyle = g;
a.beginPath();
a.arc(wthird/2,hthird/2,wthird/5,0,PI*2);
a.fill();

// hills
a.save();
g = a.createLinearGradient(0,hthird*2,0,height);
g.addColorStop(0,"#00a31a");
g.addColorStop(1,"#00d824");
a.fillStyle = g;
a.beginPath();
a.arc(0,hthird*4.5,hthird*3,0,PI,1);
a.arc(wthird*2,hthird*5.5,hthird*4,0,PI,1);
a.fill();
// ensure grass doesn't render into the sky with a clip() operation
a.clip();

// flowers
for (var n=0,y=hthird*1.55,size=1,alpha=0.01,density,maxdensity=width/32; n<hthird/6; n++)
{
   density = maxdensity - n / 4;
   a.globalAlpha = alpha;
   // as we move down the rows, inc size, inc spacing, dec density
   for (var i=-1,dd=width/density; i<density; i++)
   {
      // calc x/y pos with some jitter
      // flower
      a.fillStyle = i%2?"#f40000":"#ffec4f";
      // unicode flower chars
      a.font = size + "pt serif";
      a.fillText(i%2 ? "\u273f" : "\u2740", Math.random() * dd + dd * i - size/2, Math.random() * size + y - size);
   }
   size++;
   y += n;
   alpha += 0.05;
}

a.restore();

// windmill animation
var scale = -hthird/2;
setInterval(function() {

   // restore sky section where the blades are animated
   a.fillStyle = sg; // reuse the sky gradient from earlier - nifty
   a.fillRect(wthird*2.75,hthird*1.45,-hthird*2,-hthird*1.5);
   
   a.save();
   a.translate(wthird*2.3, hthird*1.7);
   
   // - main building
   a.beginPath();
   a.lineTo(0,0);
   a.lineTo(0,scale);
   a.lineTo(scale/2, scale*2.2);
   a.lineTo(scale, scale);
   a.lineTo(scale, 0);
   a.fillStyle = "#8c000a";
   a.fill();
   
   // - door, windows
   a.fillStyle = "#e9c700";
   a.fillRect(scale/4,0,scale*0.5, scale/2);
   a.fillRect(scale/3,scale*0.75,scale*0.33, scale/4);
   
   // - blades
   a.translate(scale/2,scale*1.75);
   a.rotate(spin+=0.01);
   for (i=0; i<4; i++)
   {
      // main blade section
      a.beginPath();
      a.lineTo(scale*1.3,scale*0.25);
      a.lineTo(scale*1.3,-scale*0.25);
      a.lineTo(0,0);
      a.fillStyle = "#f5efcb";
      a.fill();
      
      // panelling
      a.fillStyle = "#cbd599";
      a.fillRect(0,-scale*0.01,scale*1.3,scale*0.02);
      
      // rotate quarter turn before drawing next blade
      a.rotate(PI/2);
   }
   
   // - blade disc (NOTE: doesn't fit in 1024 bytes...)
   //a.beginPath();
   //a.arc(0,0,-scale*0.05,0,PI*2);
   //a.fill();
   
   a.restore();

},12);