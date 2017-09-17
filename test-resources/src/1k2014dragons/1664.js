var s = 256,
    i=0,
    k=0,
    J=s*40, 
    r=c.createImageData(s,s),
    d=r.data,
    n=[],
    T=0,
    Q=Math.sin,
    R=Math.sqrt,
    A=Math.abs;

//mix/lerp
function M(s,n,t){return s+(n-s)*t};
//SmoothStep
function S(t){return t*t*(3-2*t)};

b.style.background='#000';

//fill an array with random values
for(i=0; i<s*s; i++)
    n[i]=M(.7,.5,Math.random());

//our 'noise' function.. not a very good one.
function $(X, Y, k) {
    X = A(X+s)*k; //offset to avoid the tile seam in center
    Y = A(Y)*k;
    var tx = S(X - ~~X),
        ty = S(Y - ~~Y),
        m = s-1, //bit mask
        //smooth the random noise a little
        rx0 = ~~X & m,
        ry0 = ~~Y & m,
        rx1 = rx0+1 & m,
        ry1 = ry0+1 & m,
        nx0 = M( n[ry0*m+rx0], n[ry0*m+rx1], tx ),
        nx1 = M( n[ry1*m+rx0], n[ry1*m+rx1], tx );
    return M(nx0, nx1, ty);
}

function G() {
    T+=.02;
    
    //for each pixel
    for (i=J; i<s*s-J; i++) { //nobody's gonna notice a few missing pixels... 

        var u = i%s/s,
            v = 1-~~(i/s)/s;

        //determine ray direction & position
        var rdx = (u*2-1) - Q(T)/8,
            rdy = (v*2-1.5),
            rdz = (-1.15),
            // L = R(rdx*rdx+rdy*rdy+rdz*rdz), //length of ray dir
            px = Q(T)*20, //ray position
            py = 140,//+Q(T)*10,
            pz = 160+Q(T)*30,

            RED=0, BLUE=0, GREEN=0, //color channels
            Y, H, //for the egg
            D=0,  //ray marched distance; TODO: optimize this var out, replace with x or o
            N=50,
            len,
            noise; 

        // rdx/=L; //normalize the direction. looks OK without it.
        // rdy/=L;
        // rdz/=L;

        
        for (k=0,E=1; k<N; k++) {
            //calculate our distance field now..
            D = py;

            
            len = px*px+py*py+pz*pz;
            noise = $(px,pz,.05) + $(px,pz,.5)*.2; 

            if (len > s*s) {
                D = k = 0;
                RED=BLUE=GREEN = py/1e3;
                // BLUE+=pz/1e4;
            } else {
                D = py - noise*90;
                RED = BLUE = GREEN = 4.6*k/N;
                
                RED *= .3;
                GREEN /= 5;
                BLUE *= .1;

                Y=py*.7-55; //oval egg shape
                

                //fake some sparkle on the very tips
                if (pz<50 && py+D*40>74) {
                    RED = S(RED*2);
                    GREEN = S(GREEN*1.75)*.95;
                    BLUE = S(BLUE*1);
                } 

                if ((H=R(px*px+Y*Y+pz*pz)-32) < D) {
                    D = H;
                    
                    RED = GREEN = BLUE = 1-(k/N*1.2*(1-Y/4)) * 1-$(px,py,.9)*.3 - Q(i*noise/500)*.03;
                    GREEN *= .8;
                    RED *= .9;
                    BLUE *= .7;
                }

                // BLUE=A(Math.min(255,BLUE));
                // GREEN=A(Math.min(255,GREEN));
                // RED=A(Math.min(255,RED));                
            }

            if (D < .2) 
                break;

            //continue marching
            px += rdx * D;
            py += rdy * D;
            pz += rdz * D;
        }

        //color correction
        RED+=.1;
        BLUE+=.05;
        GREEN+=.08;

        //apply colors
        d[i*4] = RED*s;
        d[i*4+1] = GREEN*s;
        d[i*4+2] = BLUE*s;
        d[i*4+3] = s;
    }
    c.putImageData(r,0,-40);
    requestAnimationFrame(G); //setInterval is slow in chrome  

}
G();