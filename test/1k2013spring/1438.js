r = (function(){
    return window.requestAnimationFrame    ||
        window.webkitRequestAnimationFrame ||
        window.mozRequestAnimationFrame    ||
        window.oRequestAnimationFrame      ||
        window.msRequestAnimationFrame     ||
        function (callback) {
            window.setTimeout(callback, 1000 / 60);
        };
})();

var ampt = -0.5, k = 4,
    akIntv = 0.005,
    color = { h: 0, s: 100, l: 50 },
    offsetX = 0,
    offsetY = 0;
    colorIntv = 1,
    phase = 0;

window.onload = function () {
    b.style.margin

    c.style.backgroundColor = "#222";
    setDimensions();

    drawLoop(c);
    c.focus();
}

window.onresize = setDimensions;

function setDimensions () {
    c.width = window.innerWidth;
    c.height = window.innerHeight;
    
    offsetX = c.width/2;
    offsetY = c.height/2;
}

function drawLoop () {
    // clear out the screen first
    a.save();
    a.setTransform(1, 0, 0, 1, 0, 0);
    a.clearRect(0, 0, c.width, c.height);
    a.restore();
    
    // draw the flower
    a.lineWidth = 1;
    var amp = 150 * Math.sin(ampt) + 50;
    for (var theta=-Math.PI; theta<Math.PI; theta+=0.05) {
        var r = amp * Math.sin(k * (theta)) + amp * Math.cos(k * (theta));
        var rad = 5 + Math.sin(amp * theta) * 5;
        
        a.strokeStyle = 'hsl('+color.h+','+color.s+'%,'+color.l+'%'+')';
        
        color.h += colorIntv;
        if (color.h > 255 || color.h < 0)
            colorIntv *= -1;
   
        a.beginPath();
        a.arc(r * Math.cos(theta + phase) + offsetX, r * Math.sin(theta + phase) + offsetY, rad, 0, 2*Math.PI);
        a.stroke();
    }

    // increment and cycle variables if necessary
    k += akIntv;
    if (k > 6 || k < 3)
        akIntv *= -1;

    ampt = ampt > 2*Math.PI ? 0 : ampt + 0.005;
    phase = phase > 2*Math.PI ? 0 : phase + 0.01;

    // queue up for the next cycle
    // closure compiler doesn't obfuscate this right so I just change it manually
    r(drawLoop);
}