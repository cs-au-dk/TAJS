w = c.width = innerWidth;
h = c.height = innerHeight;

o = Math.cos;
s = Math.sin;

q = Math.random() * 100;

function a(l, a) {
    c.fillStyle = 'hsla(' + [20, 78 + '%', l + '%', a || 1].join() + ')';
}

function i(x, y, s, r1, r2) {
    c.arc(x, y, s, r1, r2, 1)
    c.fill()
}

g = c.createLinearGradient(w / 2, 0, w / 2, h);
g.addColorStop(0, '#000000');
g.addColorStop(1, '#421d14');

;
(function run() {
    requestAnimationFrame(run);

    c.fillStyle = g
    c.fillRect(0, 0, w, h)

    l = 200;
    while (l -= 1) {
        t = q - l / 40;

        v = (o(t + s(t * 0.31)) + s(t * 0.23)) * (w / 6 - l)
        b = (s(t * 1.3) - o(1 + t * 0.26)) * (h / 6 - l)

        x = w / 2 - v
        y = h / 2 - b

        // console.log(x, y)
        z = (200 - l) / 200;
        u = z * 55 - (s(l * 10) * 4);

        a(u, 1);
        // c.fillRect(,z*30,z*30);
        c.beginPath()
        sliceSize = (Math.pow(z, 4)) * 45 * ((s(l / 1.8) + 2));

        // Size head
        if (l < 20) {
            sliceSize = sliceSize - (20 - l) * 3;
            sliceSize = sliceSize >= 10 ? sliceSize : 10;
        }

        if (l < 8) {
            sliceSize = 18 + l * 3
            i(x, y + 50, sliceSize / .5, 0, Math.PI);
        }

        // DRAW MOUTH
        if (l < 20) {
            a(u, 1);
            i(x, y + 10, sliceSize, 0, Math.PI * 2);
            // c.arc(x, y+sliceSize/2, sliceSize/3, Math.PI*2, 0, true);
            // c.fill();
            // OTherwise body
        } else {
            i(x, y, sliceSize, 0, Math.PI * 2)
        }

        if (l < 30 && l > 15) {
            a(z * 90 - l * 1.5, 1);
            c.save();
            c.beginPath()
            i(x - 560 + l * 40, y - 1200 + l, 1200, 1.6, 7.4);
            c.translate(w, 0);
            c.scale(-1, 1);
            c.beginPath()
            i(((v) - 560 + w / 2 + l * 40), y - 1200 + l, 1200, 1.6, 7.4);
            c.restore();
        }
    }
    q += 0.015;
})()