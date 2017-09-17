e=[],
q={},
wd=a.width,
ht=a.height,
hs=(ht/60),
ws=(wd/60),
k=o=l=f=false,
t=true,
m='gray';

c.strokeStyle='silver';

for(w=0;w<wd;w+=ws) {
    q[~w]={};
    for(h=0;h<ht-hs;h+=hs) {
        z={
            x:w,
            y:h,
            r:t,
            f:(w<1||w+ws*2>wd||h<1||h+hs*2>ht)?'black':m,
            d:function() {
                g=this;
                if(!g.r) return;
                c.rect(g.x,g.y,ws,hs);
                c.fillStyle=g.f;
                g.r=f
            }
        };
        e.push(z);
        q[~w][~h]=z;
    }
}

function v(is,u) {
    e.push({
        x:is?ws:wd/2,
        y:is?hs:ht/2,
        f:is?'blue':'green',
        d:function(){
            p=this;
            if(o) return;
            if(k) {
                u(p);
                if(p.k==39) p.x+=ws;
                if(p.k==37) p.x-=ws;
                if(p.k==40) p.y+=hs;
                if(p.k==38) p.y-=hs;
                try {
                    z=q[~p.x][~p.y];
                    o=z.f!=m;
                    if(o) {
                        p.f='red';
                        if(is) alert("Lost")
                        else alert("Won")
                    } else {
                        z.f=p.f;
                        z.r=t;
                    }
                } catch(e) {}
            }
            c.rect(p.x,p.y,ws,hs);
            c.fillStyle=p.f;
        }
    });
};

v(t,function(p){
    if(k) { 
        p.k=k;
    }
});

v(f,function(p){
    if(!k) return;
    ts=0,ok=f,l=40-~~(Math.random()*3);
    do {
        tx=p.x,ty=p.y;
        if(l==39) tx+=ws;
        if(l==37) tx-=ws;
        if(l==40) ty+=hs;
        if(l==38) ty-=hs;
        try {
            z=q[~tx][~ty];
            ok = z.f==m;
        }catch(e){}

        if(!ok) {
            l++;
            if(l>40) l=37
        }

        ts++;
    } while(!ok && ts<8) 
    p.k=l;
});

(function dr(){
    for(i in e) {
        c.beginPath();
        e[i].d();
        c.fill();
        c.stroke();
    }
    requestAnimationFrame(dr);
})();

b.onkeydown=function(e) {
    k=e.keyCode;
};