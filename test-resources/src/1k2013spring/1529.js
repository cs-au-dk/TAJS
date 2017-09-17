var gs = [], ms = 0.016, b = 1e3, k = 200, jw = 960, jh = 540, m0 = 0, m1, m2, m3, m4, g, generate, p, gindex, pindex;
        with (c) {
            width = jw;
            height = jh;
            style.border = '1px dotted #AAA';
            onmousemove = function (e) {
                m1 = gs[0].x = e.clientX;
                m2 = gs[0].y = e.clientY;
            }
            onmouseup = function (e) {
                gs.push({ x: m1, y: m2, vx: (m1 - m3) * 3, vy: (m2 - m4) * 3, a: 1, v: 40, ppu: 2, r:0, ps: [] });
            }
        }

        function rand(l, h) { return ~~(Math.random() * (h - l + 1)) + l; }

        //move generators/particles
        function universalupdate(e) {
            e.x += e.vx * ms;
            e.y += e.vy * ms;
            if ((e.x + e.r > jw && e.vx > 0) || (e.x - e.r < 0 && e.vx < 0)) {
                e.vx = -e.vx;
            }
            if ((e.y + e.r > jh && e.vy > 0) || (e.y - e.r < 0 && e.vy < 0)) {
                e.vy = -e.vy;
            }
            e.a -= 0.01;
            return e.a > 0;
        }

        setInterval(function () {
            a.clearRect(0, 0, jw, jh);
            for (gindex = 0; gindex < gs.length; gindex++) {
                g = gs[gindex];
                generate = universalupdate(g);
                //update particles
                for (pindex = 0; pindex < g.ps.length; pindex++) {
                    p = g.ps[pindex];
                    with (a) {
                        globalAlpha = Math.max(p.a, 0);
                        beginPath();
                        arc(p.x, p.y, p.r, 0, 2 * Math.PI, 0);
                        fillStyle = p.c;
                        fill();
                        closePath();
                    }
                    if (!universalupdate(p)) {
                        g.ps.splice(pindex, 1);
                        pindex--;
                    }
                }
                //generate particles
                if (generate) {
                    //generator more particles
                    for (var x = 0; x < g.ppu; x++) {
                        g.ps.push({ x: g.x, y: g.y, vx: rand(-g.v, g.v) + (!gindex ? m1 - m3 : 0), vy: rand(-g.v, g.v) + (!gindex ? m2 - m4 : 0), r: rand(1, 4 + gindex), a: rand(5, 10) / 10, c: '#' + rand(b, 1.67e7).toString(16) });
                    }
                } else if (!g.ps.length) {
                    gs.splice(gindex, 1);
                    gindex--;
                }
            }
            //update mouse velocity
            if (m0 <= 0) {
                m3 = m1;
                m4 = m2;
                m0 = 100;
            }
            m0 -= 16;
        }, 16);
        //create the mouse following generator
        gs[0] = { x: -b, y: -b, vx: 0, vy: 0, a: 9e99, v: 80, ppu: 3, ps: [] }