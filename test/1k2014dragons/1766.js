var w = window.innerWidth;
var h = window.innerHeight;

var canvas = document.getElementById('c');
var context = canvas.getContext('2d');

canvas.width = w;
canvas.height = h;

var pi2x = Math.PI * 2;

window.requestAnimFrame = (function(callback) {
    return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
        function(callback) {
            window.setTimeout(callback, 1000 / 60);
        };
})();

function drawText(text,font,xx,yy){
    context.font = font;
    context.fillStyle = "rgb(255,255,255)";
    context.fillText(text, xx, yy);

    var px = [];

    var imgData=context.getImageData(0,0,canvas.width,canvas.height);

    for(x=0; x<imgData.width; x++)
    {
        for(y=0 ; y<imgData.height; y++)
        {
            if(getPixel(imgData,x,y)[3] > 0)
            {
                px.push( [x,y] );
            }
        }
    }

    return px;
}

function getPixel(imgData, x, y) {
    var offset = (x + y * imgData.width) * 4;
    var r = imgData.data[offset+0];
    var g = imgData.data[offset+1];
    var b = imgData.data[offset+2];
    var a = imgData.data[offset+3];

    return [r,g,b,a];
}



function draw(pixels,space,z,moveBy){

    context.lineJoin="bevel";
    context.lineWidth = 1;

    for(var i=0;i<pixels.length;i+=space){

        var x = pixels[i][0];
        var y = pixels[i][1];

        var r = Math.ceil(Math.random()*254);
        var g = Math.ceil(Math.random()*254);
        var b = Math.ceil(Math.random()*254);
        var a = Math.random();

        var style = "rgba("+r+","+g+","+b+","+a+")";

        context.beginPath();
        context.moveTo(x+moveBy,y+moveBy);
        context.lineTo(x+z, y+z);
        context.strokeStyle = style;
        context.closePath();
        context.stroke();

    }

}

function roundedRectangle(x, y, w, h, radius,color,lineSize,fill)
{
    var r = x + w;
    var b = y + h;
    context.beginPath();
    context.strokeStyle=color;
    context.lineWidth=lineSize;
    context.moveTo(x+radius, y);
    context.lineTo(r-radius, y);
    context.quadraticCurveTo(r, y, r, y+radius);
    context.lineTo(r, y+h-radius);
    context.quadraticCurveTo(r, b, r-radius, b);
    context.lineTo(x+radius, b);
    context.quadraticCurveTo(x, b, x, b-radius);
    context.lineTo(x, y+radius);
    context.quadraticCurveTo(x, y, x+radius, y);
    if(fill){
        context.fillStyle=color;
        context.fill();
    }
    context.stroke();
}

function animate(pixels) {
    context.clearRect(0, 0, canvas.width, canvas.height);

    roundedRectangle(20,20,w-60,h-60,5,"black",2,true);

    roundedRectangle(40,40,w-100,h-100,5,"green",2);

    roundedRectangle(w/4,h-50,w/2,25,5,"black",2,true);

    draw(pixels,Math.ceil((Math.random()*5)+5),10,Math.random()*20);

    requestAnimFrame(function() {
        animate(pixels);
    });

}

var size = 120;
var pixels = drawText("JS1K TV :D","bold "+size+"pt Arial",w/2-(size*4),h/2);
animate(pixels);