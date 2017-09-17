// Do Browsers Dream of Electric Sheep?
// js1k entry by Will Ridgers

/*
 * Special thanks to Scott Draves for his amazing work on the fractal flame
 * algorithm and the electric sheep wallpaper. 
 */

/* a (mostly up to date) index of variables
 *
 * a = [global] canvas 2d context
 * A = random var
 * b = [global] document.body
 * B = random var
 * c = [global] canvas element
 * C = random var
 * d = largest hit per pixel
 * D = document
 * e = colour coordinate
 * E = html element
 * f = function chosen randomly
 * F = render a flame
 * g = m[f]
 * G = element cache
 * h = [global] canvas height
 * H = [global] histogram
 * i = iterator for loop
 * I = init function
 * j = iterator for loop
 * J = D.getElementById('f').value
 * k = iterator for loop
 * l = index of pixel in image buffer
 * m = [global] matrix of coefficients
 * M = Math
 * n = [global] array of palettes
 * N = [global] palette that has been chosen
 * o = [global] array of functions
 * O = [global] array of trig functions
 * p = [global] count of how many F() calls
 * q = 
 * Q = another cache!
 * r = 
 * R = Math.random
 * s = scale value
 * t = temp variable
 * u = scaled x coordinate
 * v = scaled y coordinate
 * w = [global] canvas width
 * W = window, window.setTimeout
 * x = x coordinate
 * y = y coordinate
 * z = [global] image buffer
 * Z = z.data
 */ 

// very useful aliases
D=document;
M=Math;
W=window;

/*
 * Render a flame to canvas
 */
function F() {

    // a while loop is smaller than a for loop
    i = 50000;
    while (i--) {

        // pick a function randomly
        // ~~ is the same as Math.floor (and faster)
        g = m[4][~~(M.random()*m[4].length)];

        // linear equations
        t = g[1] * x + g[2] * y + g[5];
        y = g[3] * x + g[4] * y + g[6];
        // x = t;

        // variables for variations
        j = t*t + y*y;

        // i tried making these equations part of m, but eval is just
        // sooooo slow that it doesn't work very well. :(
        switch(g[0]) {
            case 0:
                // sinusoidal
                x = M.sin(t);
                y = M.sin(y);
            case 1:
                // spherical
                x = t/j;
                y = y/j;
        }

        // scale x and y coordinates to fit in z
        // ~~ is the same as Math.floor (and faster)
        // cache m[3]
        Q = m[3];
        u = ~~((x*w + Q[0])*Q[2]);
        v = ~~((y*h + Q[1])*Q[2]);

        // calculate location of pixel in image buffer
        l = u*w + v;

        // if not defined, make it 1
        // otherwise add 1
        H[l] = H[l] ? H[l]+1 : 1;

        // keep largest hit
        if (H[l] > d)
            d = H[l];
    }

    // create image buffer
    z = a.createImageData(w,h);
    Z = z.data;

    // prepare image buffer
    i = w*h;
    while (i--) {
        // logarithmic scale
        t = M.log(H[i])/M.log(d);

        // cache 4*i
        j=4*i;

        // set nicely!
        Z[j]   = n(t,0); // red
        Z[j+1] = n(t,1); // green
        Z[j+2] = n(t,2); // blue

        // set alpha to full
        Z[j+3] = 255;
    }
    
    // copy buffer to canvas
    a.putImageData(z, 0, 0);

    // do it again!
    // (just calling F() here will cause the browser to crash, teehee)
    W.setTimeout(F, 9);
}

function I() {
    // read config
    m = JSON.parse(J.value);

    // stop people from renderin twice
    // at this point I had only 38 bytes left to play with...
    D.getElementById('b').disabled = 1;

    // setup canvas and window
    w = c.width  = m[0][0];
    h = c.height = m[0][1];

    // make it fullscreen
    // c.style.height = W.innerHeight + 'px';

    // histogram, array of hits
    H = new Array(w*h);
    
    // go!
    F();
}

// make all these variables global.
x = y = d = w = h = H = m = 0;

// just makes things nicer
// c.height = 0;

// make gui element
E = D.createElement('p');
E.innerHTML = '<br><button id=b onclick=I();>dream</button><button onclick=W.open(c.toDataURL(\'image/png\'));>save</button><br><textarea id=f></textarea>';

// add to dom
b.appendChild(E);

// tan gives dark -> high
// sin gives dark -> medium
// cos gives dark -> low
O = [M.tan,M.sin,M.cos];

// compute palette based on t value
function n(i,j) { return 255*O[m[2][j]](i)*i*m[1][j]; }

// drake's cloud flame
// [
//     [1, 0.562482, -0.539599, 0.397861, 0.501088, -0.42992, -0.112404],
//     [1, 0.830039, 0.16248, -0.496174, 0.750468, 0.91022, 0.288389]
// ];

m = [
    [900,500], // resolution
    [1, 1, 1], // colour multiples
    [0, 1, 2], // which O[x] to use
    [900, 500, 1], // focal x, focal y, zoom
    [
        [1, .5, -.5, .4, .5, -.4, 0], // equation type, coefficients a, b, c, d, e, f
        [1, .8, .2, -.4, .7, .9, .3]
    ]
];

// set input area
J = D.getElementById('f');
J.value = JSON.stringify(m);