M = Math
R = M.random
B = M.abs
F = M.floor
w = ~~ (a.width / 2) //canvas width
h = ~~ (a.height / 2) //canvas height
I = c.createImageData(w, 1) //1px line
d = I.data
A = a.addEventListener
l = 5 //blobs array length
T = 255
k = 3e9
H = R() * 360 //hue
J = K = 0 //mouse drag coords

O = Array(h + 1)
for (y = h + 1; y--;) {
    O[y] = Array(w + 1)
    for (x = w + 1; x--;)
    O[y][x] = M.sqrt(x * x + y * y)
}
p = [] //blobs array
for (i = l; i--;)
p.push({
    x: R() * w,
    y: R() * h,
    X: R() * 30 - 15, //vx
    Y: R() * 30 - 15 //vy
});

b.style.background = '#000';

A('mousedown', function (e) {
    J = e.clientX
    K = e.clientY
}, false);
A('mouseup', function (e) {
    for (i = l; i--;) {
        z = p[i]
        z.X += (e.clientX - J) / 10
        z.Y += (e.clientY - K) / 10
    }
}, false);

A("mousewheel", W, false);
A("DOMMouseScroll", W, false);

function W(e) {
    k += (e.wheelDelta || -e.detail) * 5e7
    if (k < 15e7) k = 15e7
}

function S() {
    var h = (H / 360) * 6,
        i = F(h),
        q = T * (1 - h + i),
        t = T - q,
        j = i % 6
    if (!j) r = T, g = t, b = 0
    else if (j == 1) r = q, g = T, b = 0
    else if (j == 2) r = 0, g = T, b = t
    else if (j == 3) r = 0, g = q, b = T
    else if (j == 4) r = t, g = 0, b = T
    else /*(j == 5)*/
    r = T, g = 0, b = q
}

~function U() {
    if (++H > 360) H = 0;
    S();

    for (i = l; i--;) {
        z = p[i]
        if ((z.x >= w && z.X >= 0) || (z.x <= 0 && z.X <= 0)) z.X *= -1;
        else if (B(z.X) > 15) z.X -= z.X / 30;

        z.x += z.X;
        if (z.x < 0) z.x = 0;
        else if (z.x > w) z.x = w;

        if ((z.y >= h && z.Y >= 0) || (z.y <= 0 && z.Y <= 0)) z.Y *= -1;
        else if (B(z.Y) > 15) z.Y -= z.Y / 30;

        z.y += z.Y;
        if (z.y < 0) z.y = 0;
        else if (z.y > h) z.y = h;
    }

    for (y = h; y--;) {
        for (x = w; x--;) {
            D = 1; //distance

            for (i = l; i--;) {
                z = p[i];
                D *= O[F(B(z.y - y))][F(B(z.x - x))];
            }

            P = (T - D / k); //pixel brightness

            if (P < 0) P = 0;
            if (P > T) P = T;

            o = x * 4;
            d[o + 3] = P;
            if (P) {
                d[o] = r;
                d[o + 1] = g;
                d[o + 2] = b;
            }
        }
        c.putImageData(I, w / 2, y + h / 2);
    }
    requestAnimationFrame(U);
}()