var 
    L = [],
    W = a.width,
    H = a.height,
    R=0,
    S=W<H?W:H,
    T=S*-0.00005+0.056; //-0.0000517+0.0565
    
//CLASS P is a 2D Point
P = function(x, y) {
    this.x = x;
    this.y = y;
};

P.prototype={
    //get distance from a point
    gD: function(p) {
        var c = (this.x-p.x), d = (this.y-p.y);
        return Math.sqrt(c*c+d*d);
    },
    //get vector to a point
    gV: function(p) {
        return new P(p.x-this.x, p.y-this.y);
    },
    //normalize
    nor: function(len) {
        var x=this.x, y = this.y, l = Math.sqrt(x*x + y*y);
        this.x=x/l*len;
        this.y=y/l*len;
        return this;
    },
    //add coords of a point
    a: function(p) {
        this.x+=p.x;
        this.y+=p.y;
        return this;
    },
    //go to point P, PPF = Pixels per frame during the walk
    to: function(p, ppf) {
        if(this.gD(p) / ppf>1) {
            p = this.gV(p).nor(ppf).a(this);
        }
        this.x = p.x;
        this.y = p.y;
    },
    //closest point
    cP: function(p, r) {
        var d = this.gD(p);
        if(d<=r) {
            return this.cpy();
        }
        return this.gV(p).nor(d-r).a(this);
    },
    //return a copy of the point
    cpy: function() {
        return new P(this.x, this.y);
    }
};

//Dragon
D = function() {
    //this.H = null; //H=Head is the first cell of the string
    this.N = 0;  //N= number of cell
    this.C = [Math.random()*255, Math.random()*255, Math.random()*255];
    for(var i = 0; i<100; i++) {
        this.a();
    }
};

D.prototype = {
    //set target
    sT: function(p) {
        this.H.sT(p);
    },
    //draw
    dw: function() {
        if(this.H) {
            if(this.H.gD()<3) {
                var a=Math.cos(R)*S*0.5, b=Math.sin(R)*S*0.5;
                this.sT(new P(W/2+(a*0.3+a*Math.random()*0.7), H/2+(b*0.3+b*Math.random()*0.7)));
                //this.sT(new P(W*Math.random()*0.7+this.H.pP.x*0.3, H*Math.random()*0.7+this.H.pP.y*0.3));
            }
            this.H.dw();
        }
    },
    //add a cell
    a: function() {
        if(this.H) {
            this.H.a();
        } else {
            this.H = new C(this.C, new P(W/2, H/2));
        }
        this.N++;
        this.H.sX(1, this.N);
    }
};

//Cell of the dragon
C = function(C, t, pr) {
    this.pr = pr || null;            //previous cell
    //this.nx = null;                    //next cell
    this.t = t;    //target point
    this.pP = this.t;                    //position point
    this.C = C;                      //Color
    this.X = 1;                      //index in the string
};

C.prototype = {
    //get distance
    gD: function() {
        return this.pP.gD(this.t);
    },
    //add a new cell at the end of the string
    a: function() {
        if(this.nx) {
            this.nx.a();
        }
        else {
            this.nx = new C(this.C, this.t.cpy(), this);
        }
    },
    //set index
    sX: function(num, total) {
        if(this.nx) {
            num++;
            this.nx.sX(num, total);
        }
        this.X = 1-(num/total);
    },
    //set target
    sT: function(p) {
        if(this.nx) {
            this.nx.sT(this.pP.cpy());
        }
        this.t = p;
    },
    //draw
    dw: function() {
        
        /*CALCULATE*/
        if(this.pr) {
            this.t = this.pP.cP(this.pr.pP, 10*this.X);
        }
        this.pP.to(this.t, 5);
        
        /*END CALCULATE*/
        
        if(this.nx) {
            this.nx.dw();
        }
        c.fillStyle = "rgb("+Math.round(this.C[0]*this.X)+","+ Math.round(this.C[1]*this.X) +","+Math.round(this.C[2]*this.X)+")";
        
        c.fillRect(this.pP.x-5*this.X, this.pP.y-5*this.X, 10*this.X, 10*this.X);
    }
};

//init the dragons list
for(var i=0; i<10; i++) {
    L[i] = new D();
}

//frame method, is lighter than using requestAnimationFrame
setInterval(function() {
    a.width = W;
    R+=T;
    for(var i in L) {
        L[i].dw();
    }
}, 33);

//user click set a target point to the dragons
a.onmousedown = function(e) {
    for(var i in L) {
        L[i].sT(new P(e.clientX, e.clientY));
    }
};