d = c.cloneNode(0);
e = d.getContext('2d');
w = 300;
h = 150;
r=Math.random;

c.width=d.width=w;
c.height=d.height=h;

//Offscreen
e.fillRect(0,0,w,h);
e.fillStyle="#FFF";
e.font="100pt Arial";
e.fillText('❤',92,116);

(function f() {
    x = 0|r() * w;
    y = 0|r() * h;
    h = (335 + r() * 40) % 360;
    v = 1 + r() * 0.8;
    
    p = e.getImageData(x,y,1,1).data[0];
    if (p) {
        s = 0|v*60;
        l = 40;
    } else {
        s = 0|v*60;
        l = 0|v*80;
    }
    a.fillStyle="hsl("+h+","+s+"%,"+l+"%)";
    a.fillText('❤',x,y);
    setTimeout(f,5);
})()