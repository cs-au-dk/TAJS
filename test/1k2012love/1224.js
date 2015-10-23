var w = 500,
    h = 800,
    M = Math,
    P = [],
    K = 0,
    C = 250,
    B = [],
    O;
dr(a);
c.width = 500;
c.height = 800;
b.onkeydown = function (e) {
    K = e.keyCode == 39 ? 1 : 0;
}
var it = setInterval(function () {
    if (C < 0 || C > 500 || (C > O && C < (O + 10))) {
        ko();
        clearInterval(it);
    }
    if (K) {
        C += 10;
        P.push(C);
    } else {
        C -= 10;
        P.push(C);
    }
    a.fillStyle = '#FFF';
    a.fillRect(0, 0, 500, 800);
    a.strokeText('Score : ' + P.length, 20, 20);
    dr(a);
    pl(a);
}, 100);

function pl(c) {
    for (i = P.length; i > 0; i--) {
        a.strokeStyle = '#A62929';
        a.strokeText('❤', P[i], 400 + ((i - P.length) * 10));
    }
}

function dr(a) {
    a.strokeStyle = '#000';
    B.push([(M.random() * (w-20)) + 10, h]);
    for (i = B.length - 1; i > 0; i--) {
        a.strokeText('↥', B[i][0], B[i][1] + ((i - B.length) * 10));
        if ((i - B.length) * 10 == -410) {
            O = B[i][0];
        }
    }
    for (i = 0; i < h; i += 10) {
        a.strokeText('↥', 0, i);
        a.strokeText('↥', 490, i);
    }
}

function ko() {
    l=0;
    setInterval(function() {
            a.fillStyle = '#FFF';
            a.fillRect(200, 350, 100, 20);
            if(l) {
                a.strokeStyle = '#A62929';
                l=0;
            } else {
                a.strokeStyle = '#380F0F';
                l=1;
            }
            a.strokeText('GAME OVER!', 220, 370);
    }, 500);
}