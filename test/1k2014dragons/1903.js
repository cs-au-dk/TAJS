// about 4-track machines: https://www.youtube.com/watch?v=a6m5sG0XWcQ
        function t4(f) { f(0); f(1); f(2); f(3); }

        function move(p) {
            u = p.pageX;
            v = p.pageY;
            t4(function (i) {
                if (b[i].d) {
                    b[i].x = u - 32;
                    b[i].y = v + 24;
                }
            });
            d.d || updateRS(d, j); // if(!d.d) ...
            e.d || updateRS(e, g);
        }

        // inlining 2 save 4 Bytes | miss a pun
        /*
        function dragOn(p) {
            p.touches && move(p.touches[0]);
            t4(function (i) {
                //with (b[i])
                //  d = (x > u - 18) && (y > v - 18) && (x < u) && (y < v);
                b[i].d = (b[i].x > u - 64) && (b[i].y > v) && (b[i].x < u) && (b[i].y < v + 64); // saves 3 bytes in the crushed version over the with-expression
            });
        }

        function dragOff() {
            t4(function (i) {
                b[i].d = 0;
            });
        }*/

        with (b) {
            onmousemove = move;
            addEventListener('touchstart', onmousedown = function (p) {
                p.touches && move(p.touches[0]);
                t4(function (i) {
                    //with (b[i])
                    //  d = (x > u - 18) && (y > v - 18) && (x < u) && (y < v);
                    b[i].d = (b[i].x > u - 64) && (b[i].y > v) && (b[i].x < u) && (b[i].y < v + 64); // #fwiw
                });
            });
            addEventListener('touchend', onmouseup = function () {
                t4(function (i) {
                    b[i].d = 0;
                });
            });
            addEventListener('touchmove', function (p) {
                p.preventDefault();
                move(p.touches[0]);
            });
        }
        k = a;
        b = []; // "framebuffers" and control points in the same array to save some chars
        (function init() {
            w = k.width;
            h = k.height
            t4(function (i) {
                e = d; // cheaper way to access b[2] later on
                f = a; // and b[2].c too

                // in fact, we only need two additional buffers for the feedback loop and the overlay, welcome affluence. :D
                d = b[i] = document.createElement("canvas"); // b[3] at last
                d.width = w; d.height = h;
                a = d.c = d.getContext('2d');

                d.d = 0;
            });
        })();
        g = b[0]; j = b[1];

        M = Math;
        d.r = M.PI / 4; e.r = d.r * 3; // initial parameters for the orginal dragon curve
        d.s = e.s = -2 / 3;
        d.x = 2 / 3 * w;
        d.y = e.y = h / 2;
        e.x = w - d.x;

        // building block for 2D http://en.wikipedia.org/wiki/Lindenmayer_system
        function L(p) {
            with (f) { // f == b[2].c
                save(); // push matrix
                translate(p.x, p.y); rotate(-p.r); scale(p.s, p.s); translate(-w / 2, -h / 2); // multiply matrix
                globalCompositeOperation = 'lighter'; // enable additive blend
                drawImage(d, 0, 0); // add s-scaled copy of b[3] translated to x/y and rotated by r
                fillStyle = "#010203"; fillRect(0, 0, w, h); // increment color in the transformed rectangle
                restore(); // pop matrix
            }
        }
        function updateRS(rs, xy) { // calculate and store length and angle from two control points
            rs.r = M.atan2(xy.x - rs.x, xy.y - rs.y);
            rs.s = M.sqrt((rs.x - xy.x) * (rs.x - xy.x) + (rs.y - xy.y) * (rs.y - xy.y)) / h * 2;
        }
        function updateXY(rs, xy) { // update the other control point from the stored length and angle
            xy.x = rs.x + M.sin(rs.r) * h * rs.s / 2;
            xy.y = rs.y + M.cos(rs.r) * h * rs.s / 2;
        }
        t = 0;
        (function anim() {
            requestAnimationFrame(anim);
            /*
            if (k.height != h) { // because window resize rocks | u2 lazy 2 use F5 | Firefox on Android
                sx = k.width / w;
                sy = k.height / h;
                w = k.width; h = k.height;

                t4(function (i) {
                    with (b[i]) {
                        width = w; height = h; x *= sx; y *= sy;
                    }
                });
            }*/

            updateXY(d, j);
            updateXY(e, g);

            f.fillStyle = "#000";
            f.fillRect(0, 0, w, h); // clear offscreen-rendertarget for the feedback layer
            L(d); L(e); // L-systems are composited of smaller copies of them, now you know

            a.drawImage(e, 0, 0); // draw in a to draw into the feedback loop
            c.drawImage(e, 0, 0); // draw in c to composite with an overlay

            t4(function (i) { // draw the control points
                c.fillStyle = i % 2 ? "#80F" : "#F80";  // if(o.o) booh! no parents, no gain
                c.font = "64px a";
                c.fillText(i < 2 ? "☉" : "↻", b[i].x, b[i].y);
            });

            c.fillText(_, --t, 64); // <marquee>$this</marquee>
        })();