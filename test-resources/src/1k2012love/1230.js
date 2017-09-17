c.width = 800;
	c.height = 800;
        
        function P(x,y){this.x = x;this.y = y;}
        
        
        function w()
        {
            var M=Math, N=M.random, C=M.cos, S=M.sin, Pw=M.pow, H=400, barPtAmt=N() * 10 + 15, webCtr=new P(H, H), w=800, num=1200, halfCirc = 180;
            // e = rays array
            // u = ray_length
            
            
            /*******	       RAY		*******/
            
            function R(p1, p2)
	    {
                // the angle of the current ray
                this.a = A(p1, p2);				
			
                // Get the length of the strand
                this.l = G(p1, p2);
                L(p1, p2);
	    }
            /*** End Ray ***/
               
            
            // get the distance between 2 points
            G = function(p1, p2)
	    {	
		var dx=(p1.x - p2.x), dy=(p1.y - p2.y);
		return M.sqrt((dx*dx) + (dy*dy));
	    };
	    
            
            // get angle between 2 points
	    A = function(p1, p2){
                return M.atan2((p1.y - p2.y), (p1.x - p2.x));
            };
            
            
            // draw a line between 2 points
            L = function(p1, p2){
                /*color redColor = color(random(50, 255), 0, 0);
                //color w = color(2);
                stroke(redColor);*/
        
                //a.strokeStyle = '0';
		a.lineWidth = N()*.6 + .2;
		a.beginPath();
		a.moveTo(p1.x, p1.y);
		a.lineTo(p2.x, p2.y);
		a.stroke();
	    };
            
            
            
            g = function(){
                var incrementer=1, s=N()*.1 + .2;
                z = [];
                e = [];
               
                f = function (j,u,k) {
                        o = s * (-Pw(j, 2) + 40*j + num);
                        q = (M.PI*j)/halfCirc;
                        p = new P(k*(H + o *S(q))+u, 600 - o *C(q));
                }
                
                // go through 60-ish degrees twice. Once for each side of the heart shape.
                for (i=0; i<119; i+=incrementer) {
                    j = i%61;
                    
                    // right side
                    if (i >= 61) {
                        f(j,0,1);
                    }else {
                        f(61-j,w,-1);
                    }
                   
                    // add the point to pts array
                    z.push(p);
                   
                    m = A(p, webCtr);
                    
                    // draw line to the edge
                    if (i%6 ==0) L(p, new P(H +  (C(m) * w), H + (S(m) * w)));
                        
                    e.push(new R(p, webCtr));
                    u = e.length;
                    if (u>1)D(u-2);
                    
                    // move ahead a random amount of degrees
                    incrementer =  ~~(N() * 4 + 1);
                
                    g = z.length;
                    if (i>0) L(p, z[g-2]);
                }
                L(z[g-1], z[0]);
                D(0);
                D(u-1);
            }



        /*
        void mouseClicked() {
          w();
        }
        */
        
      
        
        // draw the bars between rays
        D = function(rayNum) {
            
            // s = point spacing
            // t = this point
            // n = next point
            // r = ray
            // d = distance between point
            // z = array of points (P)
            
            r = e[rayNum];
        
            for (k=1; k<barPtAmt; k++) {
                // randomly DON'T draw a line
                if (N()*5 == 1) continue;
                
                s = (r.l/barPtAmt)*k;
                // find the positions of all the pts that will go on the given ray
                b = new P(H + C(r.a)*s, H + S(r.a)*s);
             
                // if not the last strand
                n = (rayNum != 0) ? e[rayNum - 1] : e[u-1];
               
                // Get a random distance to place the starting point at. it will be within a range above and 
                // below the original point. The further away from the center, the more variation it can have
                d = G(b, webCtr) + N()*(k * .2*2) - k * .2;
                t = new P(H + C(r.a) * d, H + S(r.a) * d);
                
                d = (d > n.l) ? n.l : d;
                L(t, new P(H + C(n.a) * d, H + S(n.a) * d));
            }
        }  
        g();
    }
    
    
    w();