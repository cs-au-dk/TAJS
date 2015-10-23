(function(){
        var s = 600;
        var h = 300;
        c.width = c.height = s;
        c.style.border = "1px solid #000";
        a.lineWidth = .25;

        var points = [];
        var index = -1;
        var p = {x:s/2,y:s/1.5};
        var interval;

        var rate = 0.005;
        var size = 8;

        var detail = .1;
        var roundness = 8;
        var p3 = -.05;
        var p4 = .5;

        var f1 = 250;
        var f1d = true;
        var img;

        function getFill(){
            f1 += (f1d) ? -1: 1 ;
            if (f1 <= 123 || f1 >= 250) f1d = !f1d;
            return 'rgba(255,'+f1+','+f1+', .025)';
        }

        function add(p,q){
            return {x:p.x+q.x,y:p.y+q.y};
        }

        function cartesian( r, a ){
            return {x:Math.cos(a)*r,y:Math.sin(a)*r};
        }

        function polar( ti, r ){
            index++;
            if (!points[index]) points[index] = 0;
            points[index] += ti;
            return cartesian(r,points[index]);
        }

        a.strokeStyle = 'rgba(0,0,0,.2)';
        a.fillStyle = 'rgba(255,255,255,.01)';

        function loop(){
            var rp = polar(rate,size);
            a.beginPath();
            a.moveTo(p.x,p.y);
            a.fillStyle = getFill();
            for (var i=0;i<127;i++){
                index = -1;
                p = add( p , add( polar( detail, roundness), add( polar(p3,rp.x), polar(p4, rp.y) )) );
                a.lineTo( p.x, p.y);
            }
            a.stroke();
            a.fill();

            img = a.getImageData(0,0,c.width, c.height);
            a.clearRect(0,0,c.width, c.height);
            a.putImageData(img,m.x,m.y);
        }

        function mp(e){
            return {x:(( e.pageX - c.offsetLeft ) - h)/h,y:((e.pageY - c.offsetTop ) - h)/h};
        }
        var m;
        c.onmousemove = function(e){
            var p = mp(e);
            rate = .01 * (1-Math.abs(p.y));
            detail = 0.1 + .00025 * p.x;
        };

        c.onmousedown = function(e){
            if (interval){
                interval = m = clearInterval( interval );
            }
            else{
                interval = setInterval( loop, 30 );
                if (!m){
                    a.clearRect(0,0,c.width, c.height);
                    m = mp(e);
                    m.x = Math.round(2 * m.x);
                    m.y = Math.round(2 * m.y);
                }
            }
        };

    })();