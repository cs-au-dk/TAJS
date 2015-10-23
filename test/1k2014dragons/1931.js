var rnd = Math.random,
        sin = Math.sin,
        canvas = document.createElement('canvas'),
        context = canvas.getContext('2d'),
        w = a.width = 1000,
        h = a.height = 400,
        particles = [],
        poem1,poem2,poem3,iTimeDelta,
        i=j=particleIndex=t=0,
        space = ' ',
        w = a.width,
        h = a.height,
        iTimeStart, S,
        poem = 'Love0Find0Hail0Fear0a0the0your0night0light0fight0dragon!'.split(0),
        step = function () {
            iTimeDelta = Date.now() - iTimeStart;
            i=iTimeDelta>>12;
            // background clear
            c.globalAlpha = .01 + .7*(iTimeDelta%1024)/1024;
            c.fillStyle = '#'+'1480654'.split(0)[i%2];
            c.fillRect(0,0,w,h);

            // draw poem
            c.font = "99px s";
            c.globalAlpha = .5;
            if (~i&1) {
                poem1 = 0|rnd()*4;
                poem2 = 0|rnd()*3 + 4;
                poem3 = (i%3==0) ? 10 : 0|rnd()*3 + 7;
                c.globalAlpha = .1;
            }

            c.fillStyle = '#fff';
            c.fillText(
                    poem[poem1]+space+poem[poem2]+space+poem[poem3],
                    50,
                    h-50
            );
            c.font = "420px s";
            c.globalAlpha /= 9;
            c.fillText(
                    poem[poem1]+space+poem[poem2]+space+poem[poem3],
                    -500 + 500 * sin(iTimeDelta/5e3),
                    h-60
            );

            // draw clouds
            for (i in particles) {
                p = particles[i];

                // animate particles
                p[2]-=200;
                if (p[2] < 0) p[2] = 2e4 + rnd()*1e4;
                c.globalAlpha = 2e3/p[2];
                c.drawImage(
                        canvas,
                        p[0]/p[2]+ w/2 - sin(iTimeDelta/3e3)*400,
                        p[1]/p[2]+ h/2 - sin(iTimeDelta/1e3)*100,
                        9e6/p[2],
                        6e6/p[2]
                );
            }

            if (particles.length < 70)
                particles[particleIndex++]=[rnd()*4e7-2e7,-2e6-rnd()*4e6, rnd()*1e2, rnd()*1e2];
            requestAnimationFrame(step);
        }
    ;
    for(S='RIFF_oO_WAVEfmt '+atob('EAAAAAEAAQBAHwAAQB8AAAEACAA')+'data';++t<2e6;) {
        S+=String.fromCharCode(1|255&(1&(21&(t>>12^t>>14))*t>>8?t>>3:t>>4));
    }
    new Audio( 'data:audio/wav;base64,'+btoa( S ) ).play();
    iTimeStart = Date.now();
    step();
    // generate cloud sprite
    canvas.height = canvas.width = 128;
    for (;i++<200;) {
        context.beginPath();
        context.fillStyle = 'hsla(0,0%,'+((75 + rnd() * 25)&127)+'%,.1)';
        context.arc(32+64* rnd(), 32 + 64* rnd(), rnd() * 12, 0, 7);
        context.fill();
    }