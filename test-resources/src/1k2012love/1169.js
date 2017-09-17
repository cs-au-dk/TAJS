var width, height, fsize, ts, d, h, hd, hi, hx, hy, i, x, y, k, vx, vy;
var layer, pow, air, airLayer, airContext, cloud, airx, size, lw, hdi, path;

width = 1e3;
height = 250; // higher means higher cpu load
c.width = width;
c.height = height;

// Print the text
fsize = 16;
a.font = '16px arial';
a.fillText("Love is in the air", 0, fsize);
ts = 116; // calculated with a.measureText("Love is in the air")
// get pixeldata of the text
d = a.getImageData(0, 0, ts, fsize);

// the waves are four changing heights
h = [50, 0, 99, 0]; // initial values
// hd is the speed of the height change (wave)
hd = [2, -2, 5, 1]; // initial values

// create six cloud layers, the Perlin noise way
// ten or more layers is nicer, but very cpu-intensive
air = [];


for(layer=0; layer<6; layer++) {
    // higher layers have more, smaller clouds
    pow = 2 << layer; // powers of two with bit shifting
    airLayer = air[layer] = document.createElement('canvas');
    airLayer.width = width;
    airLayer.height = height;
    airContext = airLayer.getContext('2d');
    airContext.fillStyle = '#fff'; // white clouds

	// I didn't manage to squeeze some blur in :-|
    //airContext.shadowColor = '#fff';
    //airContext.shadowBlur = 10;

    // every cloud layer has 18*pow circles (clouds)
    for(cloud=0; cloud<18*pow; cloud++) {
        x = Math.random()*width;
        y = Math.random()*height;
        for(i=-1;i<2;i++) { // three times, to wrap the clouds around the edges on the sides
            airContext.beginPath();
            airContext.arc(x+i*width,y,200/pow,0,7); // 7 is full circle Math.ceil(2*Math.PI)
            airContext.closePath();
            airContext.fill();
        }
    }
}

airx=0;

// Profile
//var timestart = Date.now();

setInterval(function () {
    // calculate new heights of waves
    for (hi = 0; hi < 4; hi++) {
        h[hi] += (hdi = hd[hi]);
        // when the height hits top or bottom, turn around
        if (h[hi] < 0 || h[hi] > 99) {
            hd[hi] = (Math.random() * .8 + .4) * h[hi] > 0 ? -1 : 1; // speed of wave effect
            h[hi] -= hdi; // reverse addition to stay within 0-100 range
        }
    }

    // start with a blue sky
    a.globalAlpha = 1;
    a.fillStyle = '#06e';
    a.fillRect(0, 0, width, height);


    a.globalAlpha = .4; // transparency of each cloud layer
    // draw all cloud layers
    for(layer=0; layer<6; layer++) {
        for(lw=0;lw<2;lw++) { // two layers next to each other
            // Use ~~ to align with pixels for performance
            a.drawImage(air[layer], ~~(airx*layer/9%width)-width*lw, 0);
        }
    }



    airx++; // cloud movement

    hx = h[3];
    a.fillStyle = '#e33'; // heart color
    // draw heart in x-axis
    for (x = 0; x < ts; x++) {
        // hx is the strength of the wave effect (0-99)
        // add one tenth of the difference to the height for a slightly smoother effect
        // ~~ is a trick to turn a float in an int
        hx += (h[~~(x / 30) % 4] - hx) / 10;
        // draw heart in y-axis
        for (y = 0; y < fsize; y++) {
            // k is the alphavalue of each heart / pixel in the text (0-50)
            k = d.data[3 + 4 * (x + ts * y)] / 5;
            if (k) {
                a.globalAlpha = k*hx/6000+.1; // alpha value is k and hx combined
                vx = x * 8 + 30.5; // Use .5 to align with pixels
                vy = y * 8 + 50 + (hx / 54 * (y - 23));
                // Draw heart-like shape
                // 1k+ improvement: with bezier curves:
                // See https://developer.mozilla.org/samples/canvas-tutorial/2_6_canvas_beziercurveto.html
                a.beginPath();
                a.moveTo(vx, vy - 1.5);
                path = [3, -3, 6, 0, 0, 6, -6, 0, -3, -3];
                for(i=0; i<10; i+=2) {
                    a.lineTo(vx+path[i], vy+path[i+1]);
                }
                a.closePath();
                a.fill();
            }
        }
    }

// Profile
//    a.fillStyle = '#000';
//    a.globalAlpha = 1;
//    a.fillText(Math.round(airx/(Date.now()-timestart)*10000)/10+' fps', 0, fsize);

}, 50); // 20 fps