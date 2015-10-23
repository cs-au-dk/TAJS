var W = innerWidth-21;
var H = innerHeight-21;
c.width = W;
c.height = H;
// var blurRadius = Math.min(W,H) / 11;
a.shadowBlur= 5;
a.shadowColor='#000';
var CX = W/2;
var CY = H/2;

var dt = 0;

function frame()
{
    dt += 1;

    a.strokeStyle = '#fde';
    var s = "0123456789abcdef";

    for (var i = 0; i < H/16; ++i)
        for (var j = 0; j < W/16; ++j)
        {
            var cx = Math.sin(dt/7);
            var cy = Math.cos(dt/7);
            var dx = i - CY/16 + cx * dt/2;
            var dy = j - CX/16 + cy * dt/2;
            var dist = dx*dx+dy*dy + Math.abs(dy)*dx;
            var color = (dist*0.05-dt*0.5)&15;
            var x = j * 16;
            var y = i * 16;
            
            for (var k = 0; k < 2; ++k)
            {   
                var S = k?9:15;
                S *= Math.exp(-dist*0.02);
                var U = s[k?15-color:color];                        
                a.fillStyle = '#'+U+((i^j)&1?U+U:'48');
                if (S >=1)
                {
                    if (k == 0)
                    {
                        a.fillRect(x, y, S,S);                  
                    }
                    else
                    {
                        a.fillRect(x+3, y+3, S, S);                            
                    }
                }
            }
        }
}

setInterval(frame, 40);