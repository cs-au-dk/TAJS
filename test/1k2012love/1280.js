// helpful
W=window;
M=Math;

// need these
w=c.width=W.innerWidth;
h=c.height=W.innerHeight;

r=0;

// start clean
a.fillStyle = "#000";
a.fillRect(0,0,w,h);

// this is my cube, for testing.
//n=-1;
//v=[[1,n,-1],[n,n,n],[1,1,n],[n,1,n],[1,n,1],[n,n,1],[1,1,1],[n,1,1]];
//m=[4,5,7,6,0,1,5,4,5,1,3,7,1,3,2,0,6,7,3,2,4,6,2,0];

// projection maths.
function P(a, z, e) {     
    // scale up please
    a*=100;z*=100;
    
    // projection stuff (anaglyph)
    //var d=-300;var m=-3.2;
    //return e+((a-e)/(z-d))*(d-m);
    //return e+((a-e)/(z+800))*(-803.2);       
    return e+((a-e)/(z+300))*(-303.2);       
    
    //return e+((100*a-e)/(100*z-300))*(303);
    //return (a*d)/(z-m);
}

function t(r, c) {
    // r = [[x,y],[x,y],[x,y],[x,y]];
    a.beginPath();
    a.moveTo(r[0][0],r[0][1]);
    
    d=1;
    while(1) {
        if (d==4) d=0;
        a.lineTo(r[d][0],r[d][1]);
        if (d==0) break;
        d++;
    }
    
    a.closePath();
    //a.strokeStyle = "rgba("+c+",.5)";
    a.stroke();

    a.fillStyle = "rgba("+c+",.5)";
    a.fill();
}

function f(r) {
    s=[]; // "the stack"
    
    // maybe I can shorten this with some modular arithmetic?
    for (i=0;i<r.length-3;i+=4) {
        // stack the next four coords because they are a quad.
        k=[];l=[];z=0;
        
        for (j=0;j<4;j++) {
            var m = r[i+j];
            k.push([P(m[0],m[2],-2)+w/2,P(m[1],m[2],0)+h/2]);
            //l.push([P(m[0],m[2],2)+w/2,P(m[1],m[2],0)+h/2]); // --- UNCOMMENT FOR ANAGLYPH
            z+=m[2];
        }
        
        // here z is the average of all z coord values.
        z/=4;
        
        // add to stack
        s.push([k,l,z]);
    }
    
    // sort the faces, with painters algorithm    
    s.sort(function(l, r){return r[2]-l[2];});

    // draw faces, back to front (painter's algorithm)
    // note: we are limited to CONCAVE meshes with this technique.    
    for (i in s) {
        t(s[i][0],"255,40,40");  // LEFT face
        //t(s[i][1],"40,255,255");  // RIGHT face - UNCOMMENT FOR ANAGLYPH
    }
}

// TRANSFORM:
// These have all be integrated into the loop, I've left them in because they're useful and fun!

// e == 0 - rotate x
// e == 1 - rotate y
// e == 2 - rotate z 
// e == 3 - scale by a
// e == 4 - translate by a=[x,y,z]

/*
function T(r,a,e) {
    for (i in r) {
        q=M.cos(a);p=M.sin(a);
        var u=r[i];
        
        x=u[0];y=u[1];z=u[2];

        
        if (e==0) {
            // rotate about x
            u[1]=y*q-z*p;
            u[2]=y*p+z*q;
        }
        
        if (e==1) {
            // rotate about y
            u[0]=x*q-z*p;
            u[2]=x*p+z*q;   
        }
        
        
        if (e==2) {
            // rotate about z
            u[0]=x*q-y*p;
            u[1]=x*p+y*q; 
        }
        
        // scale
        if (e==3) { q[0]*=a;q[1]*=a;q[2]*=a; }
        
        // translate
        if (e==4) { q[0]+=a[0];q[1]+=a[1];q[2]+=a[2]; }
    }
}*/

// heart model
v = [
    // FRONT
    [0,-2,0],    // 0
    [2,0,0],    // 1
    [2,1,0],    // 2
    [0,0,-1],    // 3
    [1,2,0],     // 4
    [0,1,-.7],   // 5
    [-1,2,0],    // 6
    [-2,1,0],   // 7
    [-2,0,0],   // 8
    
    // BACK
    [-2,0,0],    // 9    
    [-2,1,0],    // 10
    [0,0,1],     // 11
    [2,1,0],     // 12
    [2,0,0],     // 13
    [0,1,.7]     // 14    
];
    
m = [
    // FRONT
    0,1,2,3,     // q1                     
    2,3,5,4,     // q2                 
    0,8,7,3,     // q3                    
    7,3,5,6,     // q4
    
    // BACK
    0,9,10,11,   // q5
    10,6,14,11,  // q6
    11,12,4,14,  // q7
    0,13,12,11,  // q8
    
    // SIDES
    //0,0,1,13,
    //2,1,13,12,
    //2,2,12,4,
    //0,0,8,9,
    //7,8,9,10,
    //7,7,6,10,
    
    // TOP
    6,6,5,14,
    4,4,5,14
];

// scene array of hearts.
// [x,y,z,rotation speed,movespeed]
S=[];
for(i=0;i<200;i++) S.push([M.random()*100-50,M.random()*100-50,M.random()*1000,M.random()*.5+.03,M.random()*1+.3]);

setInterval(function() {
    // clear the screen
    a.fillStyle = "rgba(0,0,0,.3)";
    a.fillRect(0,0,w,h); // 'clear screen'
    
    // quad stack
    s=[];
    
    // create a heart from the scene, rotate, transform and add to stack                 
    for (g in S) {
        // get a new set of verts
        V=[];for(i in v){Q=v[i].slice();V.push(Q);}
        
        
        // rotate then move
        for (i in V) {
            var U=S[g];
            
            // ROT
            q=M.cos(U[3]*r);p=M.sin(U[3]*r);
            var u=V[i];
            x=u[0];z=u[2];
            u[0]=x*q-z*p;
            u[2]=x*p+z*q; 
            
            
            // MOVE
            V[i][0] += U[0];
            V[i][1] += U[1];
            V[i][2] += U[2];
        }
        
        // add to stack
        for(i in m) s.push(V[m[i]]);
        
        U[2]-=U[4];
        if(U[2]<-1)U[2]=1000;
    }
    
    // render the stack
    f(s);
    
    // increase timer
    r+=.1;
       
}, 9);