// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// +--------------------------------------------------------------+
// |                   JS1K PARALLAX SCROLLING                    |
// +--------------------------------------------------------------+
// |                                                              |
// | Multiple repeating background layers composited on a single  |
// | canvas : Starfield, Fractal mountains, IFS trees.            |
// |                                                              |
// | Uses the mechanized abbreviation of the canvas context       |
// | methods from Marijn Haverbeke, and a custon seedable pseudo  |
// | random number generator.                                     |
// |                                                              |
// | For smart animating, requestAnimationFrame would be better   |
// | than setInterval, but the shortest polyfill i've  managed to |
// | design is 50 bytes long and does not work with Opera (the    |
// | vendor-prefix method is not yet implemented, and it requires |
// | a setTimeout fallback). FYI, here it is :                    |
// |     for (p in w = window)                                    |
// |       /^.{1,6}R.*nF/.test(p) && w[p](animate_function);      |
// |                                                              |
// +--------------------------------------------------------------+
// | Copyright(C) 2013 Frederic Poeydomenge fpoeydomenge@free.fr  |
// +--------------------------------------------------------------+
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  
//=================
// Initializations
//=================

W = 800;        // Screen width
S = W * 2;      // Double screen width
R =             // Current random number
H = 600;        // Screen height
M = [];         // Computed values

c.width  = W;   // Canvas width
c.height = H;   // Canvas height

//=======================================
// PRNG - Pseudo Random Number Generator
//=======================================

function r() {
    R = 997 * R >>> 0;
    return R / 4294967295;
}

//================================
// Mountains - Recursive function
//================================

function m(x1, h1, x2, h2, deep) {
    x2 - x1 > 1
    ? (
        x = x1 + x2 >> 1,
        y = (h1 + h2 + (4 * r() - 2) * H / 6 / (1 << deep)) / 2,
        m(x1, h1, x, y, deep + 1),
        m(x, y, x2, h2, deep + 1)
    )
    : M[x1] = h1;
}

//===============================
// IFS Tree - Recursive function
//===============================

function n(start_x, start_y, length, angle, size) {
    if (size) {
        C.lineWidth = size;
        // C.beginPath
        C.ba();
        // C.moveTo
        C.m(start_x, start_y);
        start_x += length * Math.cos(angle);
        start_y += length * Math.sin(angle);
        C.strokeStyle = '#' + size * 190;
        // C.lineTo
        C.l(start_x, start_y);
        // C.stroke
        C.s();
        for (var i = 0 ; i < 7 ; i++)
            n(start_x, start_y, length * .7, angle + r() * 2 - 1, size - 1);
        // C.closePath
        C.ca();
    }
}

//================
// Creates layers
//================

t = '';
for (z = 0 ; z < 7 ; z++) {

    // Creates a new canvas element (2 screens wide)
    B = c.cloneNode();  // B=document.createElement('canvas');
    B.width = S;        // B.height is inherited from previously defined c element

    // Mechanized abbreviations
    // http://marijnhaverbeke.nl/js1k/
    for (p in C = B.getContext('2d'))
        C[p[0] + (p[6] || '')] = C[p];

    // Layer 0 : IFS Tree
    if (!z)
        for (j = 1 ; j < 4 ; j++)
            n(420 * j, H, 60 * j, -1.57, 5);

    // Layers 1 to 4 : Mountains
    else if (z < 5) {
        j = H / 6 * (5 - z);
        m(0, j, S, j, -2);
        // C.createLinearGradient
        G = C.cL(0, 0, 0, H);
        for (i = 1 ; i < 3 ; i++)
            G.addColorStop(i - 1, 'hsl(' + j / 5 + ',100%,' + 3 * i + '0%)');
        C.fillStyle = G;
        for (i = 0 ; i < S ; i++)
            // C.fillRect
            C.fc(i, M[i], 1, H);
    }
    
    // Layers 5 to 6 : Starfield + Moon
    else {
        C.fillStyle = z > 5 ? '#CCC' : '#FFF';
        for (i = 0 ; i < S ; i++)
            // C.fillRect
            C.fc(r() * S, r() * H, 7 - z, 7 - z);
        // Layers 6 : Moon
        if (z > 5)
            C.font = '160px Arial',
            // C.fillText
            C.fx('\u25cf', W, H / 4);
    }

    // CSS3 multiple background property
    t += 'url(' + B.toDataURL() + '),';

}

//========================
// Initializes the canvas
//========================

c.style.background = t + '#000';

//======================
// Starts the animation
//======================

setInterval(
    function() {
        t = '';
        for (z = 0 ; z < 7 ; z++)
            M[z] += z - 7,
            t += M[z] + 'px 0,';
        c.style.backgroundPosition = t + 0;
    },
    50 / 3
);