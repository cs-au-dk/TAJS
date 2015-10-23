(function(){
    var w = 600,
        h = 400,
        R = Math.random,
        paused = d = i = v = 0,
        j,// counter
        g,//gradient
        aux,//guess
        dM = 30,
        dm = -dM,
        p = 5,// the degree pace
        H = {},
        q='#000',
        f = function(x){
            a.font=x+'px Arial';
        },//font
        B = function(){
            this.S = 35+~(-R()*50);
            this.x = w;
            this.y = R() * h;
            this.s = ~(-R()*5);
            this.id = i++;
            this.h = '❤';//R()>.5?'❤':'♥';
            this.dir = -1;
            this.c = 'red';
            H[this.id] = this;
        },
        A = function(){
            B.call(this);
            with(this){S=30;h='➳';s=25;dir=1;y=173,x=115;c=q;}
            this.d = d*Math.PI/180;
        };
    B.prototype.draw = function(){
        with(this){
            if(x+S<0||x-S>w)return destroy();
            a.fillStyle=c;
            a.save();
            f(S);
            a.translate(-S/2,S/3);
            a.fillText(h,x+=s*dir,y+(R()>.5?1:-1));
            a.restore();
        }
    };
    B.prototype.destroy = function(){
        delete H[this.id];
    };
    A.prototype = new B;
    A.prototype.draw=function(){
        with(this){
            a.save();
            a.translate(x,y+10);
            a.rotate(d);
            x+=s*dir;
            y+=Math.sin(d)*s;
            f(S);
            a.fillStyle=c;
            a.fillText(h,0,0);
            a.restore();
        }
        for(j in H)
                if(H[j].h =='❤' && H[j].id != this.id)
                    {
                        var z = H[j];
                            distance = Math.sqrt(Math.pow(this.x-z.x,2)+Math.pow(this.y-z.y,2));
                        if(distance<z.S/2)
                            z.destroy();
                    }
    }
    c.width=w;
    c.height=h;
    c.style.margin='0 auto';
    c.style.display='block';
    a.save();
    b.addEventListener('keyup',function(e){(e.keyCode||e.which)==32&&new A});
    b.addEventListener('keydown',function(e){k=e.keyCode||e.which;if(k==80)paused=!paused;k==38?d-=p:(k==40?d+=p:'');});
    H={};
    setInterval(function(){
        if(paused)
            {
                if(!v)
                    {
                        f(99);
                        a.fillText('paused',150,h/2);
                        v=1;
                    }
                return;
            }
        v=0;
        d = d>dM?dM:d<dm?dm:d;
        c.height=h;
        a.restore();
        if(R()>.95) new B;
        for(j in H)H[j].draw();
        a.fillStyle=q;
        f(70);
        a.save();
        a.translate(105,167);
        a.rotate(d*Math.PI/180);
        a.fillText('♐'/*♂'*/,-8,30);//➳
        a.restore();

        a.rect(0,0,w,h);
        a.fillText('웃',50,h/2);
        a.stroke();
    },50);
})();