/**
 * JS1K 2014
 * http://js1k.com/2014-dragons/
 *
 * Demo author: monsieurluge
 * Demo name : Marcel VS the Ugly Angry Little Dragons
 **/

//misc
M = Math;
R = M.random;
A = M.abs;

//init
//L:level
//S:score
//W:arena size (width & height)
L = 0;
S = 0;
W = 500;

//add the player
//type, x, y, dx, dy, zoom, speed, timebeforefire, tiles, size
//O:objects
//P:player
O = [[2, 9, 9, 1, 0, 4, 3, 0, 'ce77ece79e91414e4149e4e4e4', 5]];
P = O[0];

//keys
onkeydown = function (e) {
    k = e.keyCode;
    P[3] = A(k - 38) == 1 ? k - 38 : 0;
    P[4] = A(k - 39) == 1 ? k - 39 : 0;
};

//player fires a bullet
onclick = function (e) {
    P[7] < 0 && B(2, P[1], P[2], e.clientX, e.clientY, 4, P);
};

//game over
function J () {
    clearInterval(I);
    c.fillStyle = '#F30';
    c.fillText('GAME OVER', 50, 9);
}

//create a bullet
//originType, originX, originY, targetX, targetY, zoom, originObject
function B (o, x, y, v, w, z, U) {
    i = v - x;
    j = w - y;

    if (A(i) < A(j)) {
        i = i / A(j);
        j = j / A(j);
    } else {
        j = j / A(i);
        i = i / A(i);
    }

    //create the bullet
    //type, x, y, dx, dy, zoom, speed, N#A, tiles, size, origin
    O.push([3, x, y, i, j, z, 6, 0, '3', 1, o]);

    //Init the "time before next fire"
    U[7] = 20;
}

//collision
function C (o, t) {
    x = o[1] - (t[1] + t[5] * 2.5);
    y = o[2] - (t[2] + t[5] * 2.5);

    return M.sqrt(x * x + y * y) < 14;
}

//destroy an object
function D (o) {
    O.splice(O.indexOf(o), 1);
}

//cycle
I = setInterval(function () {
    //clear the scene
    a.width += 0;
    c.fillStyle = '#EEE';
    c.fillRect(0, 0, W, W);

    //show the level
    c.fillStyle = '#000';
    c.fillText('PTS:' + S, 9, 9);

    //manage objects
    O.forEach(function (o) {
        //some vars
        g = o[0]; //object type
        X = o[1]; //pos x
        Y = o[2]; //pos y
        z = o[5]; //zoom
        v = o[6]; //speed
        s = o[9]; //size

        //reduce the 'next time before firing again'
        o[7]--;

        //ennemy
        if (g < 2) {
            //move to the player
            o[3] = X < P[1] ? 1 : -1;
            o[4] = Y < P[2] ? 1 : -1;

            //fires a bullet
            o[7] < 0 && B(1, X, Y, P[1], P[2], z, o);
        }

        //bullet
        if (g > 2) {
            //check for bullet collision
            O.forEach(function (t) {
                //collision between a player bullet and an ennemy -> destroy the ennemy
                o[10] == 2 && t[0] == 1 && C(o, t) && S++ && D(t);

                //collision between an emmeny bullet and the player -> game over
                o[10] == 1 && t[0] == 2 && C(o, t) && J(I);
            });
        }

        //move the object
        X = o[1] += o[3] * v;
        Y = o[2] += o[4] * v;

        //draw the object
        for (i = 0; i < s * s; i++) {
            p = o[8].charAt(i);
            c.fillStyle = '#' + p + p + p;
            c.fillRect(X + z * (i % s), Y + z * (M.floor(i / s)), z, z);
        }

        //destroy the object if out of the arena
        X < 0 || X > W || Y < 0 || Y > W ? g == 2 ? J() : D(o) : 0;

        //new game ?
        if (O.length < 2)
            //add the ennemies
            for (o = ++L; o > 0; o--)
                //type, x, y, dx, dy, zoom, speed, timebeforefire, tiles, size
                O.push([1, R() * W, R() * W, 0, 0, 4 + R() * L, 1, R() * 30, 'eeeee11e79316bde111ee3e3e', 5]);
    });
}, 33);