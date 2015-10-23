d = x = y = 256, e = 512, v = w = s = k = 0, t = 16, z = a.createLinearGradient(0, 0, e, e); S = 'a.fillStyle=\"', T = 'a.fillText(\"', R = '16 + (Math.floor(Math.random() * 29)*16)', h = new N, l = [], L = 'l.unshift(new P(x, y))', F = function (A, B, C, D) { a.fillRect(A, B, C, D) }

// Set update
setInterval(U, 120);

// Set canvas width and height
c.width = c.height = e;
z.addColorStop(0.19, '#619');
z.addColorStop(0.78, '#C25');

// Keyboard Input
document.onkeydown = function (f) {
    switch (f.which) {
        // N Pressed
        case 78:
        // Killed = Score = 0
            k = s = 0;
        // Xposition = Yposition = 0
            x = y = d;
        // Clear array
            l.length = 0;
            break;
        // A Pressed 
        case 65:
            v = -t;
            w = 0;
            break;
        // W Pressed 
        case 87:
            v = 0;
            w = -t;
            break;
        // D Pressed 
        case 68:
            v = t;
            w = 0;
            break;
        // S Pressed 
        case 83:
            v = 0;
            w = t;
            break;
    }
}

// Set font size and font
a.font = ("26px Arial");

// Used for creating a new heart
function N() {
    this.x = eval(R);
    this.y = eval(R);
}

// Used for creating a new tail position
function P(A, B) {
    this.x = A;
    this.y = B;
}

// Update
function U() {
    // Fillstyle for background
    a.fillStyle = z;
    // Draw background
    F(0, 0, e, e);
    // Fillstyle for game area
    eval(S + '#FBF"');
    // Draw game area
    F(t, t, 480, 464);
    // Draw scores
    eval(T + 'Score:" + s, t, e-6)');

    // If player x and y are equal to heart x and y; create new heart, increment score, add to tail array.
    x == h.x && y == h.y && (h = new N, s += t, eval(L));

    // Add to tail array at beginning
    eval(L);

    // Remove from end of tail array
    l.pop();

    // Colour and draw heart
    eval(S + '#F00"');
    eval(T + '\u2665", h.x, h.y + t, t)');

    // If the player moves off the screen or is dead from some other way, draw retry text
    // Else, move player
    x < t || y < t || 495 < x || 479 < y || 0 < k ? eval(T + 'N - Retry", d/2, d)') : (eval(S + '#619"'), eval(T + '\u25cf", x += v, (y += w) + t, t)'));

    // Draw all of the tail segments
    // If the player x and y is equal to a tail x and y, kill the player.
    for (var b in l) eval(T + '\u25cf", l[b].x, l[b].y + t, t)'), x == l[b].x && y == l[b].y && (k = 1);
}