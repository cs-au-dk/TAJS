var aa = document.getElementsByTagName('canvas')[0];
var a = 0;
var q = 2;
var c = aa.getContext('2d');
var d = 4;
var e = 'fillStyle';
var f = 'beginPath';
var g = 7;
var h = Math;
var r =  20;
var x = y = 250;
var j = 270;


setInterval(function () {
    if (q == 2 && 270 >= j && a == 1) {
        q = -2;
        j = 270;
        g = 7;
    }
    if (q == -2 && j >= 270 && a == 0) {
        q = 2;
        j = 270;
        g = 7;
    }
    if (q == 2 && 0 >= g) {
        a = 1;
    } else if (q == -2 && 0 >= g) {
        a = 0;
    }
    if (a == 0 && q == 2) {
        g -= 1;
    } else if (a == 1 && q == -2) {
        g -= 1;
    } else if (a == 1 && q == 2) {
        g += 1;
    } else if (a == 0 && q == -2) {
        g += 1;
    }
    if (a == 0) {
        j += 1.3 * g;
    } else if (a == 1) {
        j -= 1.3 * g;
    }
    d = j * h.PI / 180;
    c.clearRect(0, 0, 500, 500);
    for (var i = -2; 3 > i; i++) {
        if (q == i) {
            x = x + y * h.cos(d);
            y = -y * h.sin(d);
        } else {
            x = y = 250;
        }
        c.beginPath();
        c.moveTo(500 / 2 + r * 2 * i + i, -20);
        c.lineTo(x + r * 2 * i, y);
        c.strokeStyle = "#000";
        c.stroke();
        c.beginPath();
        ch((x + r * 2 * i));
    }

    function ch (x) {
        c.arc(x, y, r, 1.5, 3.6, 0);
        c.arc(x, y, r/2, 2.6, 0.5, 1);
        c.arc(x, y, r, 1.6, 3.7, 0);
        c[e] = '#080';
        c.fill();
        c[f]();
        c.arc(x, y, r, 3.7, 5.8, 0);
        c.arc(x, y, r/2, 4.7, 2.6,1);
        c.arc(x, y, r, 3.7, 3.7, 0);
        c[e] = 'red';
        c.fill();
        c[f]();
        c.arc(x, y, r, 5.8, 1.6, 0);
        c.arc(x, y, r/2, 0.5, 4.7,1);
        c.arc(x, y, r, 5.8, 5.8, 0);
        c[e] = '#FF0';
        c.fill();
        c[f]();
        c.arc(x, y, r/2, 0, 6.3, 0);
        c.arc(x, y, r/2.4, 0, 6.3, 1);
        c[e] = '#FFF';
        c.fill();
        c[f]();
        c.arc(x, y, r/2.4, 0, 6.3, 0);
        c[e] = '#00F';
        c.fill();
    };
}, 50);