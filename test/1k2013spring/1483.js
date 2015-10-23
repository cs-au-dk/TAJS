c.style.width = (c.width = 400) + 'px';
    c.style.height = (c.height = 400) + 'px';
    q='/3TY>G4TDT62"ITTB4U1&G4UA442"I4&/3TY>)3T';
    zx=[];
    zy=[];
    zs=[];
    k=new Array(256);
    m=0;
    g=0;
    w=400;
    h=400;
    n=-1;
    b.addEventListener('keydown', function(e){
        k[e.keyCode]=1;
    }, true);
    b.addEventListener('keyup', function(e) {
        k[e.keyCode]=0;
    })
    t=setInterval(function() {
        if (g<0)
            return g++;
        a.clearRect(0,0,w,h);
        a.strokeRect(0,0,w,h);
        if (g==0) {
            g=k[83]?1:g;
            m+=.1;y=50;x=8;
            for (i=0; i<40; i++) {
                s=q.charCodeAt(i)-32;
                for (j=0; j<6; j++) {
                    if ((s&Math.pow(2,j))>0)
                        a.fillRect(x,y+10*Math.sin(m+.2*(j+6*(i%8))),7,7);
                    x+=8;
                }
                if ((i+1)%8==0) {
                    y+=8;
                    x=8;
                }
            }
            a.fillText("Score:"+(n+1), w/2, h/2);
            return;
        }
        if (g==1) {
            n=-1;
            m=0;
            x=w/2;
            y=h/2;
            g=3;
        }
        x+=k[39]&&x<w-12?1:0;
        x-=k[37]&&x>0?1:0;
        y+=k[40]&&y<h-12?1:0;
        y-=k[38]&&y>0?1:0;
        a.fillRect(x,y,10,10);
        if (++m>50) {
            zx[++n]=w*Math.random();
            zy[n]=h*Math.random();
            zs[n]=.15+.5*Math.random();
            m=0;
        }
        for (i=0;i<n;i++) {
            a.fillRect(zx[i],zy[i],5,5);
            if ((x<zx[i]+4)&&(x+10>zx[i])&&(y<zy[i]+4)&&(y+10>zy[i]))
                g=-125;
            zx[i]+=x>zx[i]?zs[i]:-zs[i];
            zy[i]+=y>zy[i]?zs[i]:-zs[i];
        }
        a.fillText(""+(n+1),10,10);
    }, 25);